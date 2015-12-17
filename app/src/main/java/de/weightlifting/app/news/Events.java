package de.weightlifting.app.news;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.UpdateableItem;
import de.weightlifting.app.UpdateableWrapper;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.JsonParser;

public class Events extends UpdateableWrapper {

    public final static String FILE_NAME = "events.json";
    public static final int navigationPosition = MainActivity.FRAGMENT_NEWS;
    public final static int subPosition = 1;
    public static ArrayList<EventItem> itemsToMark = new ArrayList<>();
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_schwedt/updates/production/events.json";
    private final String TAG = "Events";

    public static ArrayList<EventItem> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<EventItem> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((EventItem) array.get(i));
        }
        return convertedItems;
    }

    public static void markNewItems(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<EventItem> oldEventItems = casteArray(oldItems);
        ArrayList<EventItem> newEventItems = casteArray(newItems);
        for (int i = 0; i < newEventItems.size(); i++) {
            boolean isNew = true;
            for (int j = 0; j < oldEventItems.size(); j++) {
                if (newEventItems.get(i).getLocation().equals(oldEventItems.get(j).getLocation()) && newEventItems.get(i).getDate().equals(oldEventItems.get(j).getDate()) && newEventItems.get(i).getTitle().equals(oldEventItems.get(j).getTitle()) && newEventItems.get(i).getPreview().equals(oldEventItems.get(j).getPreview())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                itemsToMark.add(newEventItems.get(i));
            }
        }
    }

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }

    protected void updateWrapper(String result) {
        Events newItems = new Events();
        newItems.parseFromString(result);
        if (items.size() > 0) {
            keepOldReferences(items, newItems.getItems());
            markNewItems(items, newItems.getItems());
        }
        items = newItems.getItems();
    }

    private void keepOldReferences(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<EventItem> oldEventItems = casteArray(oldItems);
        ArrayList<EventItem> newEventItems = casteArray(newItems);
        for (int i = 0; i < newEventItems.size(); i++) {
            for (int j = 0; j < oldEventItems.size(); j++) {
                if ((newEventItems.get(i)).equals(oldEventItems.get(j))) {
                    newEventItems.set(i, oldEventItems.get(j));
                }
            }
        }
    }

    public void parseFromString(String jsonString) {
        //Log.d(WeightliftingApp.TAG, "Parsing events JSON...");
        try {
            ArrayList<UpdateableItem> newItems = new ArrayList<>();

            JsonParser jsonParser = new JsonParser();
            jsonParser.getJsonFromString(jsonString);

            JSONArray events = jsonParser.getJsonArray("events");
            for (int i = 0; i < events.length(); i++) {
                try {
                    JSONObject event = events.getJSONObject(i);

                    EventItem item = new EventItem();
                    item.setTitle(event.getString("title"));
                    item.setDate(event.getString("date"));
                    item.setLocation(event.getString("location"));
                    newItems.add(item);
                } catch (Exception ex) {
                    Log.e(WeightliftingApp.TAG, "Error while parsing event item #" + i);
                    //ex.printStackTrace();
                    Log.e(WeightliftingApp.TAG, ex.getMessage());
                }
            }
            setItems(newItems);
            setLastUpdate((new Date()).getTime());
            Log.i(WeightliftingApp.TAG, "Events parsed, " + newItems.size() + " items found");
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Event parsing failed");
            ex.printStackTrace();
        }
    }
}
