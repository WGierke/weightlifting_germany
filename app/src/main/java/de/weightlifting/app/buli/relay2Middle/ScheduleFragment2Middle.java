package de.weightlifting.app.buli.relay2Middle;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;
import de.weightlifting.app.buli.Table;

public class ScheduleFragment2Middle extends ListViewFragment {

    private Schedule2Middle schedule2Middle;

    protected void getBuliElements() {
        schedule2Middle = app.getSchedule2Middle(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (schedule2Middle.getItems().size() == 0) {
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
                ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule2Middle.casteArray(schedule2Middle.getItems()), getActivity());
                listViewTable.setAdapter(adapter);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Schedule2Middle failed");
                ex.toString();
            }

        }
    }
}
