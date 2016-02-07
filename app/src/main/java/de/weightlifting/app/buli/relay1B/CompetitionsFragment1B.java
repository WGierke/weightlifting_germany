package de.weightlifting.app.buli.relay1B;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.CompetitionsFragment;

public class CompetitionsFragment1B extends CompetitionsFragment {

    protected Competitions1B competitions1B;

    protected void getBuliElements() {
        competitions1B = app.getCompetitions1B(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions1B.getItems().size() == 0) {
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
                setCompetitionsListAdaptherWithProtocolFragment(Competitions.casteArray(competitions1B.getItems()), getActivity());
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions1B failed");
                ex.toString();
            }
        }
    }
}
