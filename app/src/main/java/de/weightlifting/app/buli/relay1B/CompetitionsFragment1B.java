package de.weightlifting.app.buli.relay1B;

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
import de.weightlifting.app.buli.CompetitionsFragment;
import de.weightlifting.app.buli.CompetitionsListAdapter;
import de.weightlifting.app.buli.ProtocolFragment;
import de.weightlifting.app.helper.API;

public class CompetitionsFragment1B extends CompetitionsFragment {

    protected Competitions1B competitions1B;

    protected void getBuliElements() {
        competitions1B = app.getCompetitions1B(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions1B.getItems().size() == 0) {
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
                CompetitionsListAdapter adapter = new CompetitionsListAdapter(Competitions.casteArray(competitions1B.getItems()), getActivity());
                listViewBuli.setAdapter(adapter);
                listViewBuli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Show the protocol which belongs to the competition
                        Fragment protocol = new ProtocolFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(API.PROTOCOL_URL, Competitions.casteArray(competitions1B.getItems()).get(position).getProtocolUrl());
                        protocol.setArguments(bundle);
                        ((MainActivity) getActivity()).addFragment(protocol, getString(R.string.buli_1B), true);
                    }
                });

            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions1B failed");
                ex.toString();
            }
        }
    }
}
