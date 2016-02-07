package de.weightlifting.app.buli.relay2Middle;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.CompetitionsFragment;

public class CompetitionsFragment2Middle extends CompetitionsFragment {

    protected Competitions2Middle competitions2Middle;

    protected void getBuliElements() {
        competitions2Middle = app.getCompetitions2Middle(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions2Middle.getItems().size() == 0) {
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
                setCompetitionsListAdaptherWithProtocolFragment(Competitions.casteArray(competitions2Middle.getItems()), getActivity());

            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions1B failed");
                ex.toString();
            }
        }
    }
}
