package de.weightlifting.app.buli.relay2North;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class TableFragment2North extends TableFragment {

    private Table2North table2North;

    protected void getBuliElements() {
        table2North = app.getTable2North(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table2North.getItems().size() == 0) {
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
                setTableListAdapterWithFilterCompetitionsFragment(Table.casteArray(table2North.getItems()), getActivity(), FilterCompetitionsFragment2North.class);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table2South failed");
                ex.toString();
            }

        }
    }
}
