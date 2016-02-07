package de.weightlifting.app.archive;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.FilterCompetitionsFragment;
import de.weightlifting.app.buli.PastCompetition;
import de.weightlifting.app.helper.API;

public class ArchiveFilterCompetitionsFragment extends FilterCompetitionsFragment {

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

    protected Competitions getSpecificCompetitions() {
        return null;
    }

    protected String getTitle() {
        return getString(R.string.nav_buli);
    }
}
