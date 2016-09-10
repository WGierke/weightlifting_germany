package de.weightlifting.app.buli.relay2A;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class TableFragment2A extends TableFragment {

    private Table2A table2A;

    protected void getBuliElements() {
        table2A = app.getTable2A(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table2A.getItems().size() == 0) {
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
                setTableListAdapterWithFilterCompetitionsFragment(Table.casteArray(table2A.getItems()), getActivity(), FilterCompetitionsFragment2A.class);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table2A failed");
                ex.toString();
            }

        }
    }
}
