package de.weightlifting.app.archive;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.CompetitionsFragment;
import de.weightlifting.app.buli.PastCompetition;
import de.weightlifting.app.helper.API;

public class ArchivedFilterCompetitionsFragment extends CompetitionsFragment {

    private Competitions archivedCompetitions;
    private Bundle bundle;

    protected void getBuliElements() {
        try {
            bundle = this.getArguments();
            archivedCompetitions = ArchivedCompetitionsFragment.archivedCompetitions;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            String clubName = bundle.getString(API.CLUB_NAME);
            ArrayList<PastCompetition> filteredCompetitions = filter(Competitions.casteArray(archivedCompetitions.getItems()), clubName);
            setCompetitionsListAdaptherWithProtocolFragment(filteredCompetitions, getActivity());
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Showing archived filter competitions failed");
            ex.toString();
        }

    }

    @Override
    protected void setEmptyListItem() {

    }

    private ArrayList<PastCompetition> filter(ArrayList<PastCompetition> items, String name) {
        ArrayList<PastCompetition> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getHome().equals(name) || items.get(i).getGuest().equals(name)) {
                result.add(items.get(i));
            }
        }
        return result;
    }
}

