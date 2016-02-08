package de.weightlifting.app.buli.relay2Middle;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class TableFragment2Middle extends TableFragment {

    private Table2Middle table2Middle;

    protected void getBuliElements() {
        table2Middle = app.getTable2Middle(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table2Middle.getItems().size() == 0) {
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
                setTableListAdapterWithFilterCompetitionsFragment(Table.casteArray(table2Middle.getItems()), getActivity(), FilterCompetitionsFragment2Middle.class);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table2Middle failed");
                ex.toString();
            }

        }
    }
}
