package de.weightlifting.app.buli.relay1A;

import java.util.ArrayList;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.FilterCompetitionsFragment;
import de.weightlifting.app.buli.PastCompetition;
import de.weightlifting.app.buli.relay1B.Competitions1B;

public class FilterCompetitionsFragment1A extends FilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_1A);
    }

    protected Competitions1A getSpecificCompetitions() {
        return app.getCompetitions1A(WeightliftingApp.UPDATE_IF_NECESSARY);
    }
}

