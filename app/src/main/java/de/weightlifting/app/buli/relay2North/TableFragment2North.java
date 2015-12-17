package de.weightlifting.app.buli.relay2North;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableEntry;
import de.weightlifting.app.buli.TableListAdapter;

public class TableFragment2North extends ListViewFragment {

    private Table2North table2North;

    protected void getBuliElements() {
        table2North = app.getTable2North(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table2North.getItems().size() == 0) {
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getBuliElements();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, Table.TIMER_RETRY);
        } else {
            // We have Table items to display
            try {
                ListView listViewTable = (ListView) fragment.findViewById(R.id.listViewBuli);
                TableListAdapter adapter = new TableListAdapter(Table.casteArray(table2North.getItems()), getActivity());
                listViewTable.setAdapter(adapter);
                listViewTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Show the competitions the club already had
                        Fragment protocol = new FilterCompetitionsFragment2North();
                        Bundle bundle = new Bundle();
                        TableEntry entry = (TableEntry) table2North.getItem(position);
                        bundle.putString("club-name", entry.getClub());
                        protocol.setArguments(bundle);
                        ((MainActivity) getActivity()).addFragment(protocol, entry.getClub(), true);
                    }
                });

            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table2South failed");
                ex.toString();
            }

        }
    }
}
