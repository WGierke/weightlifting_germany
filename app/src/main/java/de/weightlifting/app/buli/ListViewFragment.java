package de.weightlifting.app.buli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;

public abstract class ListViewFragment extends Fragment {

    protected WeightliftingApp app;
    protected View fragment;

    protected ListView listViewBuli;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.buli_page, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        listViewBuli = (ListView) fragment.findViewById(R.id.listViewBuli);

        getBuliElements();

        return fragment;
    }

    protected abstract void getBuliElements();
}
