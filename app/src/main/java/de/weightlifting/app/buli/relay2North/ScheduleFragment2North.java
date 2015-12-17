package de.weightlifting.app.buli.relay2North;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ScheduleListAdapter;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class ScheduleFragment2North extends TableFragment {

    private Schedule2North schedule2North;

    protected void getTable() {
        schedule2North = app.getSchedule2North(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (schedule2North.getItems().size() == 0) {
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getTable();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, Table.TIMER_RETRY);
        } else {
            // We have Table items to display
            try {
                ListView listViewTable = (ListView) fragment.findViewById(R.id.listView_Buli);
                ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule2North.casteArray(schedule2North.getItems()), getActivity());
                listViewTable.setAdapter(adapter);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table2North failed");
                ex.toString();
            }

        }
    }
}
