package de.weightlifting.app.buli.relay2Middle;

import android.util.Log;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ScheduleListAdapter;

public class ScheduleFragment2Middle extends ListViewFragment {

    protected void getBuliElements() {
        Schedule2Middle schedule2Middle = app.getSchedule2Middle(WeightliftingApp.UPDATE_IF_NECESSARY);

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
