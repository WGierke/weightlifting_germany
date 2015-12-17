package de.weightlifting.app.buli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;

public abstract class TableFragment extends Fragment {

    protected WeightliftingApp app;
    protected View fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Buli Table fragment");

        fragment = inflater.inflate(R.layout.buli_page, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        getTable();

        return fragment;
    }

    protected abstract void getTable();
}
