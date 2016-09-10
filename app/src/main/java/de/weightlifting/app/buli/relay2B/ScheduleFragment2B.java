package de.weightlifting.app.buli.relay2B;

import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ScheduleFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment2B extends ScheduleFragment {

    protected void getBuliElements() {
        Schedule2B schedule2B = app.getSchedule2B(WeightliftingApp.UPDATE_IF_NECESSARY);

        try {
            ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule2B.casteArray(schedule2B.getItems()), getActivity());
            listViewBuli.setAdapter(adapter);
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Showing Schedule2B failed");
            ex.toString();
        }
    }
}
