package de.weightlifting.app.buli.relay2B;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.CompetitionsFragment;

public class CompetitionsFragment2B extends CompetitionsFragment {

    protected Competitions2B competitions2B;

    protected void getBuliElements() {
        competitions2B = app.getCompetitions2B(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions2B.getItems().size() == 0) {
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
                setCompetitionsListAdaptherWithProtocolFragment(Competitions.casteArray(competitions2B.getItems()), getActivity());

            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions2B failed");
                ex.toString();
            }
        }
    }
}
