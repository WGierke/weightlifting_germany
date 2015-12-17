package de.weightlifting.app.buli.relay2South;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;
import de.weightlifting.app.buli.Table;

public class ScheduleFragment2South extends ListViewFragment {

    private Schedule2South schedule2South;

    protected void getBuliElements() {
        schedule2South = app.getSchedule2South(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (schedule2South.getItems().size() == 0) {
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
                ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule2South.casteArray(schedule2South.getItems()), getActivity());
                listViewTable.setAdapter(adapter);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Schedule2South failed");
                ex.toString();
            }

        }
    }
}
