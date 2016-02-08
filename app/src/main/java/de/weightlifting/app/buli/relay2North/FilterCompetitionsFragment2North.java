package de.weightlifting.app.buli.relay2North;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.AbstractFilterCompetitionsFragment;

public class FilterCompetitionsFragment2North extends AbstractFilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_2North);
    }

    protected Competitions2North getSpecificCompetitions() {
        return app.getCompetitions2North(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

