package de.weightlifting.app.buli;

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

public class Team extends UpdateableWrapper {

    public static final String FILE_NAME = "team.json";
    public static ArrayList<TeamMember> itemsToMark = new ArrayList<>();
    public final static int navigationPosition = MainActivity.FRAGMENT_BULI;
    public final static int subPosition = 0;
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_schwedt/updates/production/team.json";
    private final String TAG = "Team";

    public static ArrayList<TeamMember> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<TeamMember> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((TeamMember) array.get(i));
        }
        return convertedItems;
    }

    public static void markNewItems(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<TeamMember> oldTeamItems = casteArray(oldItems);
        ArrayList<TeamMember> newTeamItems = casteArray(newItems);
        for (int i = 0; i < newTeamItems.size(); i++) {
            boolean isNew = true;
            for (int j = 0; j < oldTeamItems.size(); j++) {
                if (newTeamItems.get(i).getName().equals(oldTeamItems.get(j).getName()) && newTeamItems.get(i).getSnatching().equals(oldTeamItems.get(j).getSnatching()) && newTeamItems.get(i).getJerking().equals(oldTeamItems.get(j).getJerking()) && newTeamItems.get(i).getMaxScore().equals(oldTeamItems.get(j).getMaxScore()) && newTeamItems.get(i).getYear().equals(oldTeamItems.get(j).getYear()) && newTeamItems.get(i).getImageURL().equals(oldTeamItems.get(j).getImageURL())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                itemsToMark.add(newTeamItems.get(i));
            }
        }
    }

    public void refreshItems() { super.update(UPDATE_URL, FILE_NAME, TAG); }

    protected void updateWrapper(String result) {
        Team newItems = new Team();
        newItems.parseFromString(result);
        if (items.size() > 0) {
            keepOldReferences(items, newItems.getItems());
            markNewItems(items, newItems.getItems());
        }
        items = newItems.getItems();
    }

    private void keepOldReferences(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<TeamMember> oldTeamMember = casteArray(oldItems);
        ArrayList<TeamMember> newTeamMember = casteArray(newItems);
        for (int i = 0; i < newTeamMember.size(); i++) {
            for (int j = 0; j < oldTeamMember.size(); j++) {
                if ((newTeamMember.get(i)).equals(oldTeamMember.get(j))) {
                    newTeamMember.set(i, oldTeamMember.get(j));
                }
            }
        }
    }

    public void parseFromString(String jsonString) {
        //Log.d(WeightliftingApp.TAG, "Parsing buliTeam JSON...");
        try {
            ArrayList<UpdateableItem> newItems = new ArrayList<>();

            JsonParser jsonParser = new JsonParser();
            jsonParser.getJsonFromString(jsonString);

            // parse team
            JSONArray team = jsonParser.getJsonArray("team");
            //Log.d(WeightliftingApp.TAG, team.length() + " team members found");
            for (int i = 0; i < team.length(); i++) {
                try {
                    JSONObject jsonMember = team.getJSONObject(i);
                    TeamMember member = new TeamMember();
                    member.setName(jsonMember.getString("name"));
                    member.setYear(jsonMember.getString("year"));
                    member.setSnatching(jsonMember.getString("snatching"));
                    member.setJerking(jsonMember.getString("jerking"));
                    member.setMaxScore(jsonMember.getString("max_score"));
                    member.setImageURL(jsonMember.getString("image"));

                    newItems.add(member);
                } catch (Exception ex) {
                    Log.e(WeightliftingApp.TAG, "Error while parsing buli team member #" + i);
                    ex.printStackTrace();
                }
            }
            setItems(newItems);
            setLastUpdate((new Date()).getTime());
            Log.i(WeightliftingApp.TAG, "Team items parsed, " + newItems.size() + " items found");
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Error while parsing buli team");
            ex.printStackTrace();
        }
    }
}
