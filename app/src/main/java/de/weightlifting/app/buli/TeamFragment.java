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

import de.weightlifting.app.ImageFragment;
import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;

public class TeamFragment extends Fragment {

    private WeightliftingApp app;

    private ListView listViewTeam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Buli Team fragment");

        View fragment = inflater.inflate(R.layout.buli_page, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        ImageView cover = (ImageView) fragment.findViewById(R.id.cover_buli);
        cover.setImageDrawable(getResources().getDrawable(R.drawable.cover_team));

        listViewTeam = (ListView) fragment.findViewById(R.id.listView_Buli);

        getTeam();

        return fragment;
    }


    private void getTeam() {
        final Team buliTeam = app.getTeam(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (buliTeam.getItems().size() == 0) {
            // No news items yet
            //Log.d(WeightliftingApp.TAG, "Waiting for buli team...");

            // Check again in a few seconds
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getTeam();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, Team.TIMER_RETRY);
        } else {
            // We have buliTeam items to display
            try {
                TeamListAdapter adapter = new TeamListAdapter(Team.casteArray(buliTeam.getItems()), getActivity());
                listViewTeam.setAdapter(adapter);
                listViewTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Fragment fr = new ImageFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("imageURL", Team.casteArray(buliTeam.getItems()).get(position).getImageURL());
                        fr.setArguments(bundle);
                        ((MainActivity) getActivity()).addFragment(fr, getString(R.string.nav_buli), true);
                    }
                });

            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing buliTeam failed");
                ex.toString();
            }

        }
    }
}
