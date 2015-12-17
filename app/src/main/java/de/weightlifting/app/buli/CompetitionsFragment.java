package de.weightlifting.app.buli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;

public abstract class CompetitionsFragment extends Fragment {

    protected WeightliftingApp app;
    protected View fragment;

    protected ListView listViewCompetitions;

    protected Competitions competitions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Buli Competitions fragment");

        fragment = inflater.inflate(R.layout.buli_page, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        listViewCompetitions = (ListView) fragment.findViewById(R.id.listView_Buli);

        getCompetitions();

        return fragment;
    }

    protected abstract void getCompetitions();
}
