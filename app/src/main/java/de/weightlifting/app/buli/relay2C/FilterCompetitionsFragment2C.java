package de.weightlifting.app.buli.relay2C;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.AbstractFilterCompetitionsFragment;

public class FilterCompetitionsFragment2C extends AbstractFilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_2C);
    }

    protected Competitions2C getSpecificCompetitions() {
        return app.getCompetitions2C(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

