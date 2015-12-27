package de.weightlifting.app.buli.relay1B;

import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment1B extends ListViewFragment {

    protected void getBuliElements() {
        Schedule1B schedule1B = app.getSchedule1B(WeightliftingApp.UPDATE_IF_NECESSARY);

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
