package de.weightlifting.app.buli.relay2C;

import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ScheduleFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment2C extends ScheduleFragment {

    protected void getBuliElements() {
        Schedule2C schedule2C = app.getSchedule2C(WeightliftingApp.UPDATE_IF_NECESSARY);

        try {
            ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule2C.casteArray(schedule2C.getItems()), getActivity());
            listViewBuli.setAdapter(adapter);
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Showing Schedule2C failed");
            ex.toString();
        }
    }
}
