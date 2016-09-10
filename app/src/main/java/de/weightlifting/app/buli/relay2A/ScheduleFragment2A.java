package de.weightlifting.app.buli.relay2A;

import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ScheduleFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment2A extends ScheduleFragment {

    protected void getBuliElements() {
        Schedule2A schedule2A = app.getSchedule2A(WeightliftingApp.UPDATE_IF_NECESSARY);

        try {
            ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule2A.casteArray(schedule2A.getItems()), getActivity());
            listViewBuli.setAdapter(adapter);
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Showing Schedule2A failed");
            ex.toString();
        }
    }
}
