package de.weightlifting.app.buli;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.API;

public abstract class AbstractFilterCompetitionsFragment extends CompetitionsFragment {

    protected ArrayList<PastCompetition> filteredCompetitions;

    protected Competitions competitions;

    protected abstract Competitions getSpecificCompetitions();

    protected abstract String getTitle();

    protected void getBuliElements() {
        competitions = getSpecificCompetitions();
        if (competitions.getItems().size() == 0) {
            //Log.d(WeightliftingApp.TAG, "Waiting for Competitions...");
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getBuliElements();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, Competitions.TIMER_RETRY);
        } else {
            try {
                Bundle bundle = this.getArguments();
                String clubName = bundle.getString(API.CLUB_NAME);
                filteredCompetitions = filter(Competitions.casteArray(competitions.getItems()), clubName);
                setCompetitionsListAdaptherWithProtocolFragment(filteredCompetitions, getActivity());
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions failed");
                ex.toString();
            }
        }
    }

    protected ArrayList<PastCompetition> filter(ArrayList<PastCompetition> items, String name) {
        ArrayList<PastCompetition> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getHome().contains(name) || items.get(i).getGuest().contains(name)) {
                result.add(items.get(i));
            }
        }
        return result;
    }
}

