package de.weightlifting.app.buli.relay1A;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.CompetitionsFragment;

public class CompetitionsFragment1A extends CompetitionsFragment {

    protected Competitions1A competitions1A;

    protected void getBuliElements() {
        competitions1A = app.getCompetitions1A(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions1A.getItems().size() == 0) {
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
                setCompetitionsListAdaptherWithProtocolFragment(Competitions.casteArray(competitions1A.getItems()), getActivity());
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions1B failed");
                ex.toString();
            }
        }
    }
}
