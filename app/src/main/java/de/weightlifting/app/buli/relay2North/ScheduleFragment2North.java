package de.weightlifting.app.buli.relay2North;

import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment2North extends ListViewFragment {

    protected void getBuliElements() {
        Schedule2North schedule2North = app.getSchedule2North(WeightliftingApp.UPDATE_IF_NECESSARY);

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
