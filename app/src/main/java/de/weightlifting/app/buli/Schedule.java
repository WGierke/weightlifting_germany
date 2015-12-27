package de.weightlifting.app.buli;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import de.weightlifting.app.UpdateableItem;
import de.weightlifting.app.UpdateableWrapper;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.JsonParser;

public class Schedule extends UpdateableWrapper {

    public static final String FILE_NAME = ".json";
    private final String UPDATE_URL = "";
    private final String TAG = "Schedule";

    public String getRelayName() { return "RELAY_NAME"; }

    public static ArrayList<ScheduleEntry> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<ScheduleEntry> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((ScheduleEntry) array.get(i));
        }
        return convertedItems;
    }

    public void refreshItems() { super.update(UPDATE_URL, FILE_NAME, TAG); }

    protected void updateWrapper(String result) {
        Schedule newItems = new Schedule();
        newItems.parseFromString(result);
        items = newItems.getItems();
    }

    public void parseFromString(String jsonString) {
        try {
            ArrayList<UpdateableItem> newBuliTableItems = new ArrayList<>();

            JsonParser jsonParser = new JsonParser();
            jsonParser.getJsonFromString(jsonString);

            JSONArray table = jsonParser.getJsonArray("scheduled_competitions");
            for (int i = 0; i < table.length(); i++) {
                try {
                    JSONObject jsonScheduleEntry = table.getJSONObject(i);
                    ScheduleEntry scheduleEntry = new ScheduleEntry();
                    scheduleEntry.setDate(jsonScheduleEntry.getString("date"));
                    scheduleEntry.setGuest(jsonScheduleEntry.getString("guest"));
                    scheduleEntry.setHome(jsonScheduleEntry.getString("home"));
                    scheduleEntry.setLocation(jsonScheduleEntry.getString("location"));
                    scheduleEntry.setTime(jsonScheduleEntry.getString("time"));

                    newBuliTableItems.add(scheduleEntry);
                } catch (Exception ex) {
                    Log.e(WeightliftingApp.TAG, "Error while parsing schedule entry #" + i);
                    ex.printStackTrace();
                }
            }
            setItems(newBuliTableItems);
            setLastUpdate((new Date()).getTime());
            Log.i(WeightliftingApp.TAG, "Schedule items parsed, " + newBuliTableItems.size() + " items found");
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Error while parsing scheduled competitions");
            ex.printStackTrace();
        }
    }
}
