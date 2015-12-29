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

public class Table extends UpdateableWrapper {

    public static final String FILE_NAME = "";
    public static ArrayList<TableEntry> itemsToMark = new ArrayList<>();
    private final String UPDATE_URL = "";
    private final String TAG = "Table";

    public static ArrayList<TableEntry> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<TableEntry> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((TableEntry) array.get(i));
        }
        return convertedItems;
    }

    public static void markNewItems(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<TableEntry> oldTableItems = casteArray(oldItems);
        ArrayList<TableEntry> newTableItems = casteArray(newItems);
        for (int i = 0; i < newTableItems.size(); i++) {
            boolean isNew = true;
            TableEntry newTE = newTableItems.get(i);
            TableEntry oldTE;
            for (int j = 0; j < oldTableItems.size(); j++) {
                oldTE = oldTableItems.get(j);
                if (newTE.getClub().equals(oldTE.getClub()) && newTE.getScore().equals(oldTE.getScore()) && newTE.getCardinalPoints().equals(oldTE.getCardinalPoints()) && newTE.getMaxScore().equals(oldTE.getMaxScore()) && newTE.getPlace().equals(oldTE.getPlace())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                itemsToMark.add(newTableItems.get(i));
            }
        }
    }

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }

    protected void updateWrapper(String result) {
        Table newItems = new Table();
        newItems.parseFromString(result);
        if (items.size() > 0) {
            keepOldReferences(items, newItems.getItems());
            markNewItems(items, newItems.getItems());
        }
        items = newItems.getItems();
    }

    public void parseFromString(String jsonString) {
        //Log.d(WeightliftingApp.TAG, "Parsing buli table JSON...");
        try {
            ArrayList<UpdateableItem> newBuliTableItems = new ArrayList<>();

            JsonParser jsonParser = new JsonParser();
            jsonParser.getJsonFromString(jsonString);

            JSONArray table = jsonParser.getJsonArray("table");
            //Log.d(WeightliftingApp.TAG, table.length() + " table entries found");
            for (int i = 0; i < table.length(); i++) {
                try {
                    JSONObject jsonTableEntry = table.getJSONObject(i);
                    TableEntry tableEntry = new TableEntry();
                    tableEntry.setPlace(jsonTableEntry.getString("place"));
                    tableEntry.setClub(jsonTableEntry.getString("club"));
                    tableEntry.setScore(jsonTableEntry.getString("score"));
                    tableEntry.setMaxScore(jsonTableEntry.getString("max_score"));
                    tableEntry.setCardinalPoints(jsonTableEntry.getString("cardinal_points"));

                    newBuliTableItems.add(tableEntry);
                } catch (Exception ex) {
                    Log.e(WeightliftingApp.TAG, "Error while parsing table entry #" + i);
                    ex.printStackTrace();
                }
            }
            setItems(newBuliTableItems);
            setLastUpdate((new Date()).getTime());
            Log.i(WeightliftingApp.TAG, "Table items parsed, " + newBuliTableItems.size() + " items found");
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Error while parsing buli table");
            ex.printStackTrace();
        }
    }

    private void keepOldReferences(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<TableEntry> oldTableItems = casteArray(oldItems);
        ArrayList<TableEntry> newTableItems = casteArray(newItems);
        for (int i = 0; i < newTableItems.size(); i++) {
            for (int j = 0; j < oldTableItems.size(); j++) {
                if ((newTableItems.get(i)).equals(oldTableItems.get(j))) {
                    newTableItems.set(i, oldTableItems.get(j));
                }
            }
        }
    }
}
