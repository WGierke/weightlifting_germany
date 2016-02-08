package de.weightlifting.app.buli.relay1A;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class TableFragment1A extends TableFragment {

    private Table table1A;

    protected void getBuliElements() {
        table1A = app.getTable1A(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table1A.getItems().size() == 0) {
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
                setTableListAdapterWithFilterCompetitionsFragment(Table.casteArray(table1A.getItems()), getActivity(), FilterCompetitionsFragment1A.class);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table1A failed");
                ex.toString();
            }

        }
    }
}
