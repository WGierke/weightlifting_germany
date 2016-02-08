package de.weightlifting.app.buli.relay2South;

import android.os.Handler;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.TableFragment;

public class TableFragment2South extends TableFragment {

    private Table2South table2South;

    protected void getBuliElements() {
        table2South = app.getTable2South(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (table2South.getItems().size() == 0) {
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
                setTableListAdapterWithFilterCompetitionsFragment(Table.casteArray(table2South.getItems()), getActivity(), FilterCompetitionsFragment2South.class);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing Table2South failed");
                ex.toString();
            }

        }
    }
}
