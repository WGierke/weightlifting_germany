package de.weightlifting.app.buli.relay1B;

import android.view.View;
import android.widget.TextView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.FilterCompetitionsFragment;

public class FilterCompetitionsFragment1B extends FilterCompetitionsFragment {

    protected String getTitle() {
        return getString(R.string.buli_1B);
    }

    protected Competitions1B getSpecificCompetitions() {
        return app.getCompetitions1B(WeightliftingApp.UPDATE_IF_NECESSARY);
    }

    @Override
    protected void setEmptyListItem() {
        TextView emptyText = (TextView) fragment.findViewById(R.id.emptyCompetitions);
        emptyText.setVisibility(View.VISIBLE);
        listViewBuli.setEmptyView(emptyText);
    }
}

