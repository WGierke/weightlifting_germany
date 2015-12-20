package de.weightlifting.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import de.weightlifting.app.helper.UiHelper;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_settings, container, false);

        ArrayAdapter<CharSequence> clubAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.clubs_1516, android.R.layout.simple_spinner_item);
        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner clubSpinner = (Spinner) fragment.findViewById(R.id.spinnerClub);
        clubSpinner.setSelected(true);
        clubSpinner.setSelection(1);
        clubSpinner.setVisibility(View.VISIBLE);
        clubSpinner.setPrompt("Select your club");

        clubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clubSpinner.setSelection(position);
                String selState = (String) clubSpinner.getSelectedItem();
                UiHelper.showToast(selState, getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        clubSpinner.setAdapter(clubAdapter);
        clubSpinner.setSelection(0);
        //clubSpinner.setEnabled(false);

        ArrayAdapter<CharSequence> relayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.relays_1516, android.R.layout.simple_spinner_item);
        relayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner relaySpinner = (Spinner) fragment.findViewById(R.id.spinnerRelay);
        relaySpinner.setSelection(relayAdapter.getPosition("Oder-Sund-Team"));
        relaySpinner.setAdapter(relayAdapter);

        //relaySpinner.setEnabled(false);

        return fragment;
    }
}


