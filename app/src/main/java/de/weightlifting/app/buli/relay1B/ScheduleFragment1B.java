package de.weightlifting.app.buli.relay1B;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ScheduleListAdapter;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class ScheduleFragment1B extends TableFragment {

    private Schedule1B schedule1B;

    protected void getTable() {
        schedule1B = app.getSchedule1B(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (schedule1B.getItems().size() == 0) {
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
                ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule1B.casteArray(schedule1B.getItems()), getActivity());
                listViewTable.setAdapter(adapter);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Schedule1B failed");
                ex.toString();
            }

        }
    }
}
