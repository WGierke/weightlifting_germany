package de.weightlifting.app.buli.relay2A;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.CompetitionsFragment;

public class CompetitionsFragment2A extends CompetitionsFragment {

    protected Competitions2A competitions2A;

    protected void getBuliElements() {
        competitions2A = app.getCompetitions2A(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions2A.getItems().size() == 0) {
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
                setCompetitionsListAdaptherWithProtocolFragment(Competitions.casteArray(competitions2A.getItems()), getActivity());

            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions2A failed");
                ex.toString();
            }
        }
    }
}
