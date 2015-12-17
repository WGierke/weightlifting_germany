package de.weightlifting.app.buli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;

public abstract class ScheduleFragment extends Fragment {

    protected WeightliftingApp app;
    protected View fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.buli_page, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        getSchedule();

        return fragment;
    }

    protected abstract void getSchedule();
}
