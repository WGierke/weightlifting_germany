package de.weightlifting.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.buli.ScheduleEntry;
import de.weightlifting.app.buli.ScheduleListAdapter;
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.UiHelper;

public class HomeFragment extends Fragment {

    public static boolean homeInForeground;
    private WeightliftingApp app;
    private View fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = (WeightliftingApp) getActivity().getApplicationContext();
        fragment = inflater.inflate(R.layout.fragment_home, container, false);
        TextView homeText = (TextView) fragment.findViewById(R.id.home_text);
        final ImageView v = (ImageView) fragment.findViewById(R.id.cover_home);
        UiHelper.animateHomeCover(v, getContext());

        String filterText;
        if (app.getBuliFilterMode().equals(API.BULI_FILTER_MODE_NONE))
            filterText = getString(R.string.all_competitions_and_placings);
        else
            filterText = app.getBuliFilterText();

        homeText.setText(getString(R.string.home_text) + " (" + filterText + "):");

        getFileredSchedules();

        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        HomeFragment.homeInForeground = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeFragment.homeInForeground = true;
    }

    public void getFileredSchedules() {
        ArrayList<ScheduleEntry> filteredSchedule = app.getFilteredScheduledCompetitions();
        try {
            ListView listViewTable = (ListView) fragment.findViewById(R.id.listViewFilteredSchedules);
            TextView emptyText = (TextView) fragment.findViewById(android.R.id.empty);
            listViewTable.setEmptyView(emptyText);
            ScheduleListAdapter adapter = new ScheduleListAdapter(filteredSchedule, getActivity());
            listViewTable.setAdapter(adapter);
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Showing filtered schedule failed");
            ex.toString();
        }
    }
}
