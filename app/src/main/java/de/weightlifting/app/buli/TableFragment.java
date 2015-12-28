package de.weightlifting.app.buli;

import android.view.View;
import android.widget.TextView;

import de.weightlifting.app.R;

public class TableFragment extends ListViewFragment {

    @Override
    protected void setEmptyListItem() {
        TextView emptyText = (TextView) fragment.findViewById(R.id.emptyTables);
        emptyText.setVisibility(View.VISIBLE);
        listViewBuli.setEmptyView(emptyText);
    }

    protected void getBuliElements() {
    }

}
