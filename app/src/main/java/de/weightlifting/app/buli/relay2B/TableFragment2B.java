package de.weightlifting.app.buli.relay2B;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class TableFragment2B extends TableFragment {

    private Table2B table2B;

    protected void getBuliElements() {
        table2B = app.getTable2B(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table2B.getItems().size() == 0) {
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
                setTableListAdapterWithFilterCompetitionsFragment(Table.casteArray(table2B.getItems()), getActivity(), FilterCompetitionsFragment2B.class);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table2B failed");
                ex.toString();
            }

        }
    }
}
