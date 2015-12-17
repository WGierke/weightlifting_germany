package de.weightlifting.app.buli.relay2North;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;
import de.weightlifting.app.buli.Table;

public class ScheduleFragment2North extends ListViewFragment {

    private Schedule2North schedule2North;

    protected void getBuliElements() {
        schedule2North = app.getSchedule2North(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (schedule2North.getItems().size() == 0) {
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
                ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule2North.casteArray(schedule2North.getItems()), getActivity());
                listViewTable.setAdapter(adapter);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Schedule2North failed");
                ex.toString();
            }

        }
    }
}
