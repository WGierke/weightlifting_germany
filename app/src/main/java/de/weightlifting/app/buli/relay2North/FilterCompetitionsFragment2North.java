package de.weightlifting.app.buli.relay2North;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.FilterCompetitionsFragment;

public class FilterCompetitionsFragment2North extends FilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_1A);
    }

    protected Competitions2North getSpecificCompetitions() {
        return app.getCompetitions2North(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

