package de.weightlifting.app.buli.relay1A;

import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ScheduleFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment1A extends ScheduleFragment {

    protected void getBuliElements() {
        Schedule1A schedule1A = app.getSchedule1A(WeightliftingApp.UPDATE_IF_NECESSARY);

        try {
            ScheduleListAdapter adapter = new ScheduleListAdapter(Schedule1A.casteArray(schedule1A.getItems()), getActivity());
            listViewBuli.setAdapter(adapter);
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Showing Table1A failed");
            ex.toString();
        }
    }
}
