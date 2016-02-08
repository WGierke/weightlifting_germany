package de.weightlifting.app.buli.relay2South;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.AbstractFilterCompetitionsFragment;

public class FilterCompetitionsFragment2South extends AbstractFilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_2South);
    }

    protected Competitions2South getSpecificCompetitions() {
        return app.getCompetitions2South(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

