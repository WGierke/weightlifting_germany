package de.weightlifting.app.archive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.weightlifting.app.ArchiveFragment;
import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.DataHelper;

public class ArchivedSeasonFragment extends Fragment {

    public static ArrayList<String> archivedRelayEntries = new ArrayList<>();
    private int seasonPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.list_view_no_cover, container, false);

        // Get archived season information from bundle
        try {
            Bundle bundle = this.getArguments();
            seasonPosition = bundle.getInt(API.SEASON_ITEM_POSITION);
            String archivedSeason = ArchiveFragment.archivedSeasonEntries.get(seasonPosition);
            archivedRelayEntries = DataHelper.getRelays(archivedSeason);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ArchivedRelayListAdapter adapter = new ArchivedRelayListAdapter(archivedRelayEntries, getActivity());

        ListView archivedSeasonList = (ListView) fragment.findViewById(R.id.list_view);
        archivedSeasonList.setAdapter(adapter);
        archivedSeasonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Show an archived relay fragment and put the selected index as argument
                Fragment archivedRelayFragment = new ArchivedRelayFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(API.SEASON_ITEM_POSITION, seasonPosition);
                bundle.putInt(API.RELAY_ITEM_POSITION, position);
                archivedRelayFragment.setArguments(bundle);
                ((MainActivity) getActivity()).addFragment(archivedRelayFragment, archivedRelayEntries.get(position), true);
            }
        });

        return fragment;
    }
}