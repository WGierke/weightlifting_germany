package de.weightlifting.app.buli.relay2South;

import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment2South extends ListViewFragment {

    protected void getBuliElements() {
        Schedule2South schedule2South = app.getSchedule2South(WeightliftingApp.UPDATE_IF_NECESSARY);

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
