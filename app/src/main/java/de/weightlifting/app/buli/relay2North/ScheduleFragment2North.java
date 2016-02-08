package de.weightlifting.app.buli.relay2North;

import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ScheduleFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment2North extends ScheduleFragment {

    protected void getBuliElements() {
        Schedule2North schedule2North = app.getSchedule2North(WeightliftingApp.UPDATE_IF_NECESSARY);

        try {
            ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule2North.casteArray(schedule2North.getItems()), getActivity());
            listViewBuli.setAdapter(adapter);
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Showing Schedule2North failed");
            ex.toString();
        }
    }
}
