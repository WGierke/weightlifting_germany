package de.weightlifting.app.buli.relay2C;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class TableFragment2C extends TableFragment {

    private Table2C table2C;

    protected void getBuliElements() {
        table2C = app.getTable2C(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table2C.getItems().size() == 0) {
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
                setTableListAdapterWithFilterCompetitionsFragment(Table.casteArray(table2C.getItems()), getActivity(), FilterCompetitionsFragment2C.class);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table2C failed");
                ex.toString();
            }

        }
    }
}
