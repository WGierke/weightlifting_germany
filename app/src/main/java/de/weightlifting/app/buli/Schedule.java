package de.weightlifting.app.buli;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.weightlifting.app.UpdateableItem;
import de.weightlifting.app.UpdateableWrapper;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.NetworkHelper;

public class Schedule extends UpdateableWrapper {

    private final String UPDATE_URL = "/get_schedule?relay=";
    private final String TAG = "Schedule";

    public String getLeagueRelay() {
        return "";
    }

    public String getFileName() { return "schedule.json"; }

    public static ArrayList<ScheduleEntry> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<ScheduleEntry> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((ScheduleEntry) array.get(i));
        }
        return convertedItems;
    }

    public String getRelayName() {
        return "RELAY_NAME";
    }

    public void refreshItems() {
        super.update(NetworkHelper.BASE_SERVER_BULI_URL + UPDATE_URL + URLEncoder.encode(getLeagueRelay()), getFileName(), TAG);
    }

    protected void updateWrapper(String result) {
        Schedule newItems = new Schedule();
        newItems.parseFromString(result);
        items = newItems.getItems();
    }

    public void parseFromString(String jsonString) {
        try {
            ArrayList<UpdateableItem> newBuliTableItems = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray table = jsonObject.getJSONArray("schedule");

            for (int i = 0; i < table.length(); i++) {
                try {
                    JSONObject jsonScheduleEntry = table.getJSONObject(i);
                    ScheduleEntry scheduleEntry = new ScheduleEntry();
                    scheduleEntry.setGuest(jsonScheduleEntry.getString("guest"));
                    scheduleEntry.setHome(jsonScheduleEntry.getString("home"));
                    scheduleEntry.setLocation(jsonScheduleEntry.getString("location"));

                    String oldDateTime = jsonScheduleEntry.getString("date") + " " + jsonScheduleEntry.getString("time");
                    DateFormat format = new SimpleDateFormat("dd.MM.yyyy kk:mm");
                    Date newDateTime = new Date();
                    try {
                        newDateTime = format.parse(oldDateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    scheduleEntry.setDateTime(newDateTime);

                    newBuliTableItems.add(scheduleEntry);
                } catch (Exception ex) {
                    Log.e(WeightliftingApp.TAG, "Error while parsing schedule entry #" + i);
                    ex.printStackTrace();
                }
            }
            setItems(newBuliTableItems);
            setLastUpdate((new Date()).getTime());
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Error while parsing scheduled competitions");
            ex.printStackTrace();
        }
    }
}
