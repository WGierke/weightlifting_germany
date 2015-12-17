package de.weightlifting.app.buli.relay1B;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;
import de.weightlifting.app.buli.Table;

public class ScheduleFragment1B extends ListViewFragment {

    private Schedule1B schedule1B;

    protected void getBuliElements() {
        schedule1B = app.getSchedule1B(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (schedule1B.getItems().size() == 0) {
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
                ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule1B.casteArray(schedule1B.getItems()), getActivity());
                listViewTable.setAdapter(adapter);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Schedule1B failed");
                ex.toString();
            }

        }
    }
}
