package de.weightlifting.app.buli.relay2South;

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
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableEntry;
import de.weightlifting.app.buli.TableFragment;
import de.weightlifting.app.buli.TableListAdapter;

public class TableFragment2South extends TableFragment {

    private Table2South table2South;

    protected void getBuliElements() {
        table2South = app.getTable2South(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table2South.getItems().size() == 0) {
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
                TableListAdapter adapter = new TableListAdapter(Table.casteArray(table2South.getItems()), getActivity());
                listViewTable.setAdapter(adapter);
                listViewTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Show the competitions the club already had
                        Fragment protocol = new FilterCompetitionsFragment2South();
                        Bundle bundle = new Bundle();
                        TableEntry entry = (TableEntry) table2South.getItem(position);
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
