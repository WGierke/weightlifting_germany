package de.weightlifting.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.weightlifting.app.archive.ArchivedSeasonFragment;
import de.weightlifting.app.archive.ArchivedSeasonListAdapter;
import de.weightlifting.app.helper.API;

public class ArchiveFragment extends Fragment {

    public static ArrayList<String> archivedSeasonEntries = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.list_view_no_cover, container, false);

        ArchivedSeasonListAdapter adapter = new ArchivedSeasonListAdapter(archivedSeasonEntries, getActivity());

        ListView archivedSeasonList = (ListView) fragment.findViewById(R.id.list_view);
        archivedSeasonList.setAdapter(adapter);
        archivedSeasonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Show an archived season fragment and put the selected index as argument
                Fragment seasonFragment = new ArchivedSeasonFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(API.SEASON_ITEM_POSITION, position);
                seasonFragment.setArguments(bundle);
                ((MainActivity) getActivity()).addFragment(seasonFragment, archivedSeasonEntries.get(position), true);
            }
        });

        return fragment;
    }
}