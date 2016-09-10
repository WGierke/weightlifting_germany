package de.weightlifting.app.buli.relay2A;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.AbstractFilterCompetitionsFragment;

public class FilterCompetitionsFragment2A extends AbstractFilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_2A);
    }

    protected Competitions2A getSpecificCompetitions() {
        return app.getCompetitions2A(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

