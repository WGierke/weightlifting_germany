package de.weightlifting.app.buli.relay2B;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.AbstractFilterCompetitionsFragment;

public class FilterCompetitionsFragment2B extends AbstractFilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_2B);
    }

    protected Competitions2B getSpecificCompetitions() {
        return app.getCompetitions2B(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

