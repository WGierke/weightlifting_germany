package de.weightlifting.app.buli;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.archive.ArchiveFilterCompetitionsFragment;
import de.weightlifting.app.helper.API;

import java.util.ArrayList;

public class TableFragment extends ListViewFragment {

    @Override
    protected void setEmptyListItem() {
        TextView emptyText = (TextView) fragment.findViewById(R.id.emptyTables);
        emptyText.setVisibility(View.VISIBLE);
        listViewBuli.setEmptyView(emptyText);
    }

    protected void getBuliElements() {
    }

    public void setTableListAdapterWithFilterCompetitionsFragment(final ArrayList<TableEntry> tableItems, Activity activity, final Class filterCompetitionsFragmentClass) {
        TableListAdapter adapter = new TableListAdapter(tableItems, activity);
        listViewBuli.setAdapter(adapter);
        listViewBuli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object filterCompetitionsFragment;
                try {
                    filterCompetitionsFragment = filterCompetitionsFragmentClass.newInstance();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                    filterCompetitionsFragment = new ArchiveFilterCompetitionsFragment();
                } catch (IllegalAccessException e) {
                    filterCompetitionsFragment = new ArchiveFilterCompetitionsFragment();
                }
                Fragment protocol = (Fragment) filterCompetitionsFragment;
                Bundle bundle = new Bundle();
                TableEntry entry = tableItems.get(position);
                bundle.putString(API.CLUB_NAME, entry.getClub());
                protocol.setArguments(bundle);
                ((MainActivity) getActivity()).addFragment(protocol, entry.getClub(), true);
            }
        });
    }

}
