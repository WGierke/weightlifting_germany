package de.weightlifting.app.buli.relay2C;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.CompetitionsFragment;

public class CompetitionsFragment2C extends CompetitionsFragment {

    protected Competitions2C competitions2C;

    protected void getBuliElements() {
        competitions2C = app.getCompetitions2C(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions2C.getItems().size() == 0) {
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
                setCompetitionsListAdaptherWithProtocolFragment(Competitions.casteArray(competitions2C.getItems()), getActivity());
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions2C failed");
                ex.toString();
            }
        }
    }
}
