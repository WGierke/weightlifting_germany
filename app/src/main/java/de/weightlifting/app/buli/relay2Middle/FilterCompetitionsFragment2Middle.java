package de.weightlifting.app.buli.relay2Middle;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.AbstractFilterCompetitionsFragment;

public class FilterCompetitionsFragment2Middle extends AbstractFilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_2Middle);
    }

    protected Competitions2Middle getSpecificCompetitions() {
        return app.getCompetitions2Middle(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

