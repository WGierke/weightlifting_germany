package de.weightlifting.app.buli.relay2Middle;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.CompetitionsListAdapter;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.buli.ProtocolFragment;

public class CompetitionsFragment2Middle extends ListViewFragment {

    protected Competitions2Middle competitions2Middle;

    protected void getBuliElements() {
        competitions2Middle = app.getCompetitions2Middle(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions2Middle.getItems().size() == 0) {
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getBuliElements();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, Competitions.TIMER_RETRY);
        } else {
            try {
                CompetitionsListAdapter adapter = new CompetitionsListAdapter(Competitions.casteArray(competitions2Middle.getItems()), getActivity());
                listViewBuli.setAdapter(adapter);
                listViewBuli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Show the protocol which belongs to the competition
                        Fragment protocol = new ProtocolFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("protocol-url", Competitions.casteArray(competitions2Middle.getItems()).get(position).getProtocolUrl());
                        protocol.setArguments(bundle);
                        ((MainActivity) getActivity()).addFragment(protocol, getString(R.string.buli_1A), true);
                    }
                });

            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions1B failed");
                ex.toString();
            }
        }
    }
}
