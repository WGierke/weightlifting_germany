package de.weightlifting.app.buli.relay1A;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.AbstractFilterCompetitionsFragment;

public class FilterCompetitionsFragment1A extends AbstractFilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_1A);
    }

    protected Competitions1A getSpecificCompetitions() {
        return app.getCompetitions1A(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

