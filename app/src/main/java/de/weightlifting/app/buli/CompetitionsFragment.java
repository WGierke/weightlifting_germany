package de.weightlifting.app.buli;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;

public class CompetitionsFragment extends Fragment {

    protected WeightliftingApp app;
    protected View fragment;

    protected ImageView cover;
    protected ListView listViewCompetitions;

    protected Competitions competitions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Buli Competitions fragment");

        fragment = inflater.inflate(R.layout.buli_page, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        cover = (ImageView) fragment.findViewById(R.id.cover_buli);
        cover.setImageDrawable(getResources().getDrawable(R.drawable.cover_competition));

        listViewCompetitions = (ListView) fragment.findViewById(R.id.listView_Buli);

        getCompetitions();

        return fragment;
    }

    protected void getCompetitions() {
        competitions = app.getCompetitions(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (competitions.getItems().size() == 0) {
            // No news items yet
            //Log.d(WeightliftingApp.TAG, "Waiting for competitions...");

            // Check again in a few seconds
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getCompetitions();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, Competitions.TIMER_RETRY);
        } else {
            // We have competitions to display
            try {
                CompetitionsListAdapter adapter = new CompetitionsListAdapter(Competitions.casteArray(competitions.getItems()), getActivity());
                listViewCompetitions.setAdapter(adapter);
                listViewCompetitions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Show the protocol which belongs to the competition
                        Fragment protocol = new ProtocolFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("protocol-url", Competitions.casteArray(competitions.getItems()).get(position).getProtocolUrl());
                        protocol.setArguments(bundle);
                        ((MainActivity) getActivity()).addFragment(protocol, getString(R.string.nav_buli), true);
                    }
                });

            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing competitions failed");
                ex.toString();
            }

        }
    }
}
