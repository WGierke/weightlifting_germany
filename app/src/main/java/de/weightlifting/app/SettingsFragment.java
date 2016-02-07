package de.weightlifting.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.UiHelper;

public class SettingsFragment extends Fragment {

    private RadioButton btnAll;
    private RadioButton btnRelay;
    private RadioButton btnClub;

    private Spinner relaySpinner;
    private Spinner clubSpinner;

    private boolean initializedRelay = false;
    private boolean initializedClub = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_settings, container, false);

        WeightliftingApp app = (WeightliftingApp) getActivity().getApplication();

        //init radio buttons
        btnAll = (RadioButton) fragment.findViewById(R.id.radioFilterNothing);
        btnRelay = (RadioButton) fragment.findViewById(R.id.radioFilterRelay);
        btnClub = (RadioButton) fragment.findViewById(R.id.radioFilterClub);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        //init spinners
        ArrayAdapter<CharSequence> relayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.relays_1516, android.R.layout.simple_spinner_item);
        relayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relaySpinner = (Spinner) fragment.findViewById(R.id.spinnerRelay);
        relaySpinner.setAdapter(relayAdapter);

        ArrayAdapter<CharSequence> clubAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.clubs_1516, android.R.layout.simple_spinner_item);
        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubSpinner = (Spinner) fragment.findViewById(R.id.spinnerClub);
        clubSpinner.setAdapter(clubAdapter);

        relaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initializedRelay) {
                    btnRelay.setChecked(true);
                    saveSettings();
                } else
                    initializedRelay = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        clubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initializedClub) {
                    btnClub.setChecked(true);
                    saveSettings();
                } else
                    initializedClub = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //restore settings
        String filterMode = app.getFilterMode();
        String filterText = app.getFilterText();

        if (filterMode == null) {
            btnAll.setChecked(true);
        } else {
            switch (filterMode) {
                case API.FILTER_MODE_NONE:
                    btnAll.setChecked(true);
                    break;
                case API.FILTER_MODE_RELAY:
                    btnRelay.setChecked(true);
                    relaySpinner.setSelection(relayAdapter.getPosition(filterText));
                    break;
                case API.FILTER_MODE_CLUB:
                    btnClub.setChecked(true);
                    clubSpinner.setSelection(clubAdapter.getPosition(filterText));
                    break;
                default:
                    btnAll.setChecked(true);
                    break;
            }
        }

        return fragment;
    }

    private void saveSettings() {
        WeightliftingApp app = (WeightliftingApp) getActivity().getApplication();
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
        app.refreshFilterSettings();
        app.saveFilterOnline();
    }
}


