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

public class Competitions extends UpdateableWrapper {

    public static final String FILE_NAME = "competitions.json";
    public static ArrayList<PastCompetition> itemsToMark = new ArrayList<>();
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_schwedt/updates/production/past_competitions.json";
    private final String TAG = "Competitions";

    public static ArrayList<PastCompetition> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<PastCompetition> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((PastCompetition) array.get(i));
        }
        return convertedItems;
    }

    public static void addItemsToMark(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<PastCompetition> oldCompetitionItems = casteArray(oldItems);
        ArrayList<PastCompetition> newCompetitionItems = casteArray(newItems);
        for (int i = 0; i < newCompetitionItems.size(); i++) {
            boolean isNew = true;
            for (int j = 0; j < oldCompetitionItems.size(); j++) {
                if (newCompetitionItems.get(i).getLocation().equals(oldCompetitionItems.get(j).getLocation()) && newCompetitionItems.get(i).getDate().equals(oldCompetitionItems.get(j).getDate()) && newCompetitionItems.get(i).getGuest().equals(oldCompetitionItems.get(j).getGuest()) && newCompetitionItems.get(i).getHome().equals(oldCompetitionItems.get(j).getHome()) && newCompetitionItems.get(i).getProtocolUrl().equals(oldCompetitionItems.get(j).getProtocolUrl()) && newCompetitionItems.get(i).getScore().equals(oldCompetitionItems.get(j).getScore())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                Log.d("WeightliftingLog", "new competition: " + newCompetitionItems.get(i).getDate());
                itemsToMark.add(newCompetitionItems.get(i));
            }
        }
    }

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }

    protected void updateWrapper(String result) {
        Competitions newItems = new Competitions();
        newItems.parseFromString(result);
        if (items.size() > 0) {
            keepOldReferences(items, newItems.getItems());
            addItemsToMark(items, newItems.getItems());
        }
        items = newItems.getItems();
    }

    public void parseFromString(String jsonString) {
        //Log.d(WeightliftingApp.TAG, "Parsing competitions JSON...");
        try {
            ArrayList<UpdateableItem> newItems = new ArrayList<>();

            JsonParser jsonParser = new JsonParser();
            jsonParser.getJsonFromString(jsonString);

            // parse past competitions
            JSONArray competitions = jsonParser.getJsonArray("past_competitions");
            //Log.d(WeightliftingApp.TAG, competitions.length() + " competitions found");
            for (int i = 0; i < competitions.length(); i++) {
                try {
                    JSONObject jsonCompoetition = competitions.getJSONObject(i);
                    PastCompetition competition = new PastCompetition();
                    competition.setLocation(jsonCompoetition.getString("location"));
                    competition.setDate(jsonCompoetition.getString("date"));
                    competition.setHome(jsonCompoetition.getString("home"));
                    competition.setGuest(jsonCompoetition.getString("guest"));
                    competition.setScore(jsonCompoetition.getString("score"));
                    competition.setUrl(jsonCompoetition.getString("url"));

                    newItems.add(competition);
                } catch (Exception ex) {
                    Log.e(WeightliftingApp.TAG, "Error while parsing competition #" + i);
                    ex.printStackTrace();
                }
            }
            setItems(newItems);
            setLastUpdate((new Date()).getTime());
            Log.i(WeightliftingApp.TAG, "BuliCompetition items parsed, " + newItems.size() + " items found");
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Error while parsing competitions");
            ex.printStackTrace();
        }
    }

    private void keepOldReferences(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<PastCompetition> oldCompetitions = casteArray(oldItems);
        ArrayList<PastCompetition> newCompetitions = casteArray(newItems);
        for (int i = 0; i < newCompetitions.size(); i++) {
            for (int j = 0; j < oldCompetitions.size(); j++) {
                if ((newCompetitions.get(i)).equals(oldCompetitions.get(j))) {
                    newCompetitions.set(i, oldCompetitions.get(j));
                }
            }
        }
    }

}
