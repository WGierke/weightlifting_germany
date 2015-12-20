package de.weightlifting.app;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.UiHelper;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_settings, container, false);

        //init spinners
        Log.d(WeightliftingApp.TAG, (DataHelper.getPreference(API.FILTER_TEXT_KEY, getActivity().getApplication()) == null) + "");
        ArrayAdapter<CharSequence> relayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.relays_1516, android.R.layout.simple_spinner_item);
        relayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner relaySpinner = (Spinner) fragment.findViewById(R.id.spinnerRelay);
        relaySpinner.setAdapter(relayAdapter);

        ArrayAdapter<CharSequence> clubAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.clubs_1516, android.R.layout.simple_spinner_item);
        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner clubSpinner = (Spinner) fragment.findViewById(R.id.spinnerClub);
        clubSpinner.setAdapter(clubAdapter);

        //init radio buttons
        final RadioButton btnAll = (RadioButton) fragment.findViewById(R.id.radioFilterNothing);
        final RadioButton btnRelay = (RadioButton) fragment.findViewById(R.id.radioFilterRelay);
        final RadioButton btnClub = (RadioButton) fragment.findViewById(R.id.radioFilterClub);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubSpinner.setEnabled(false);
                relaySpinner.setEnabled(false);
            }
        });

        btnRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubSpinner.setEnabled(false);
                relaySpinner.setEnabled(true);
            }
        });

        btnClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubSpinner.setEnabled(true);
                relaySpinner.setEnabled(false);
            }
        });

        //restore settings
        String filterMode = DataHelper.getPreference(API.FILTER_MODE_KEY, getActivity().getApplication());
        String filterText = DataHelper.getPreference(API.FILTER_TEXT_KEY, getActivity().getApplication());

        if(filterMode == null) {
            btnAll.setChecked(true);
        } else {
            switch (filterMode) {
                case API.FILTER_MODE_NONE:
                    btnAll.setChecked(true);
                    relaySpinner.setEnabled(false);
                    clubSpinner.setEnabled(false);
                    break;
                case API.FILTER_MODE_RELAY:
                    btnRelay.setChecked(true);
                    relaySpinner.setSelection(relayAdapter.getPosition(filterText));
                    relaySpinner.setEnabled(true);
                    clubSpinner.setEnabled(false);
                    break;
                case API.FILTER_MODE_CLUB:
                    btnClub.setChecked(true);
                    clubSpinner.setSelection(clubAdapter.getPosition(filterText));
                    relaySpinner.setEnabled(false);
                    clubSpinner.setEnabled(true);
                    break;
                default:
                    btnAll.setChecked(true);
                    break;
            }
        }

        Button saveButton = (Button) fragment.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application app = getActivity().getApplication();
                if (btnAll.isChecked()) {
                    DataHelper.setPreference(API.FILTER_MODE_KEY, API.FILTER_MODE_NONE, app);
                    UiHelper.showToast(getString(R.string.saved_no_filter), getActivity());
                } else if (btnRelay.isChecked()) {
                    String selectedRelay = relaySpinner.getSelectedItem().toString();
                    DataHelper.setPreference(API.FILTER_MODE_KEY, API.FILTER_MODE_RELAY, app);
                    DataHelper.setPreference(API.FILTER_TEXT_KEY, selectedRelay, app);
                    UiHelper.showToast(getString(R.string.saved_relay_filter, selectedRelay), getActivity());
                } else if (btnClub.isChecked()) {
                    String selectedClub = clubSpinner.getSelectedItem().toString();
                    DataHelper.setPreference(API.FILTER_MODE_KEY, API.FILTER_MODE_CLUB, app);
                    DataHelper.setPreference(API.FILTER_TEXT_KEY, selectedClub, app);
                    UiHelper.showToast(getString(R.string.saved_club_filter, selectedClub), getActivity());
                }
            }
        });

        return fragment;
    }
}


