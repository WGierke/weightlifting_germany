package de.weightlifting.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;

import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.UiHelper;

public class SettingsFragment extends Fragment {

    WeightliftingApp app;

    private RadioButton radioBuliAll;
    private RadioButton radioBuliRelay;
    private RadioButton radioBuliClub;
    private RadioButton radioBlogAll;
    private RadioButton radioBlogOnly;
    private RadioButton radioBlogNone;

    private Spinner relaySpinner;
    private Spinner clubSpinner;

    private ArrayList<CheckBox> checkBoxesBlogs = new ArrayList<>();
    private ArrayList<String> checkedBlogs = new ArrayList<>();

    private boolean initializedRelay = false;
    private boolean initializedClub = false;
    private boolean initializedBlogs = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        app = (WeightliftingApp) getActivity().getApplication();
        initBuliSettings(fragment);
        initBlogSettings(fragment);
        return fragment;
    }

    private void initBuliSettings(View fragment) {
        radioBuliAll = (RadioButton) fragment.findViewById(R.id.radioFilterNothing);
        radioBuliRelay = (RadioButton) fragment.findViewById(R.id.radioFilterRelay);
        radioBuliClub = (RadioButton) fragment.findViewById(R.id.radioFilterClub);

        radioBuliAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBuliSettings();
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
                    radioBuliRelay.setChecked(true);
                    saveBuliSettings();
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
                    radioBuliClub.setChecked(true);
                    saveBuliSettings();
                } else
                    initializedClub = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //restore settings
        String buliFilterMode = app.getBuliFilterMode();
        String buliFilterText = app.getBuliFilterText();

        if (buliFilterMode == null) {
            radioBuliAll.setChecked(true);
        } else {
            switch (buliFilterMode) {
                case API.BULI_FILTER_MODE_NONE:
                    radioBuliAll.setChecked(true);
                    break;
                case API.BULI_FILTER_MODE_RELAY:
                    radioBuliRelay.setChecked(true);
                    relaySpinner.setSelection(relayAdapter.getPosition(buliFilterText));
                    break;
                case API.BULI_FILTER_MODE_CLUB:
                    radioBuliClub.setChecked(true);
                    clubSpinner.setSelection(clubAdapter.getPosition(buliFilterText));
                    break;
                default:
                    radioBuliAll.setChecked(true);
                    break;
            }
        }
    }

    private boolean allCheckBoxesChecked() {
        return checkedBlogs.size() == checkBoxesBlogs.size();
    }

    private boolean noCheckBoxesChecked() {
        return checkedBlogs.isEmpty();
    }

    private void initBlogSettings(View fragment) {
        radioBlogAll = (RadioButton) fragment.findViewById(R.id.radioBlogAll);
        radioBlogOnly = (RadioButton) fragment.findViewById(R.id.radioBlogOnly);
        radioBlogNone = (RadioButton) fragment.findViewById(R.id.radioBlogNone);
        radioBlogAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBlogSettings();
                for (CheckBox checkBoxesBlog : checkBoxesBlogs) {
                    checkBoxesBlog.setChecked(true);
                }
            }
        });
        radioBlogNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBlogSettings();
                for (CheckBox checkBoxesBlog : checkBoxesBlogs) {
                    checkBoxesBlog.setChecked(false);
                }
            }
        });

        checkBoxesBlogs.add((CheckBox) fragment.findViewById(R.id.checkBVDG));
        checkBoxesBlogs.add((CheckBox) fragment.findViewById(R.id.checkSpeyer));
        checkBoxesBlogs.add((CheckBox) fragment.findViewById(R.id.checkSchwedt));
        checkBoxesBlogs.add((CheckBox) fragment.findViewById(R.id.checkMutterstadt));

        for (CheckBox checkBoxesBlog : checkBoxesBlogs) {
            checkBoxesBlog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkedBlogs.add(buttonView.getText() + "");
                    } else {
                        checkedBlogs.remove(buttonView.getText() + "");
                    }

                    //handle when checkboxes are changed due to clicking on "all" or "none"
                    if (radioBlogAll.isChecked() && allCheckBoxesChecked()) {
                        saveBlogSettings();
                    }
                    if (radioBlogNone.isChecked() && noCheckBoxesChecked()) {
                        saveBlogSettings();
                    }

                    //handle when the automatically checkboxes are changed
                    if (radioBlogAll.isChecked() && !isChecked) {
                        radioBlogOnly.setChecked(true);
                        saveBlogSettings();
                    }
                    if (radioBlogNone.isChecked() && isChecked) {
                        radioBlogOnly.setChecked(true);
                        saveBlogSettings();
                    }

                    //automatically check "all" or "none"
                    if (radioBlogOnly.isChecked() && allCheckBoxesChecked()) {
                        radioBlogAll.setChecked(true);
                        saveBlogSettings();
                    }
                    if (radioBlogOnly.isChecked() && noCheckBoxesChecked()) {
                        radioBlogNone.setChecked(true);
                        saveBlogSettings();
                    }
                    saveBlogSettings();
                }
            });
        }

        //restore settings
        String blogFilterMode = app.getBlogFilterMode();
        ArrayList<String> blogFilterPublishers = app.getBlogFilterPublishers();

        if (blogFilterMode == null) {
            radioBlogAll.setChecked(true);
            for (CheckBox checkBoxesBlog : checkBoxesBlogs) {
                checkBoxesBlog.setChecked(true);
            }
        } else {
            switch (blogFilterMode) {
                case API.BLOG_FILTER_SHOW_ALL:
                    radioBlogAll.setChecked(true);
                    for (CheckBox checkBoxesBlog : checkBoxesBlogs) {
                        checkBoxesBlog.setChecked(true);
                    }
                    break;
                case API.BLOG_FILTER_SHOW_NONE:
                    radioBlogNone.setChecked(true);
                    break;
                case API.BLOG_FILTER_SHOW_CHOSEN:
                    radioBlogOnly.setChecked(true);
                    for (CheckBox checkBoxesBlog : checkBoxesBlogs) {
                        if (blogFilterPublishers.contains(checkBoxesBlog.getText())) {
                            checkBoxesBlog.setChecked(true);
                        }
                    }
                    break;
                default:
                    radioBlogAll.setChecked(true);
                    break;
            }
        }
        initializedBlogs = true;
    }

    private void saveBuliSettings() {
        if (radioBuliAll.isChecked()) {
            DataHelper.setPreference(API.BULI_FILTER_MODE_KEY, API.BULI_FILTER_MODE_NONE, app);
            UiHelper.showToast(getString(R.string.saved_no_filter), getActivity());
        } else if (radioBuliRelay.isChecked()) {
            String selectedRelay = relaySpinner.getSelectedItem().toString();
            DataHelper.setPreference(API.BULI_FILTER_MODE_KEY, API.BULI_FILTER_MODE_RELAY, app);
            DataHelper.setPreference(API.BULI_FILTER_TEXT_KEY, selectedRelay, app);
            UiHelper.showToast(getString(R.string.saved_relay_filter, selectedRelay), getActivity());
        } else if (radioBuliClub.isChecked()) {
            String selectedClub = clubSpinner.getSelectedItem().toString();
            DataHelper.setPreference(API.BULI_FILTER_MODE_KEY, API.BULI_FILTER_MODE_CLUB, app);
            DataHelper.setPreference(API.BULI_FILTER_TEXT_KEY, selectedClub, app);
            UiHelper.showToast(getString(R.string.saved_club_filter, selectedClub), getActivity());
        }
        app.refreshBuliFilterSettings();
        app.saveBuliFilterOnline();
    }

    private void saveBlogSettings() {
        if (!initializedBlogs) {
            return;
        }

        if (radioBlogAll.isChecked()) {
            DataHelper.setPreference(API.BLOG_FILTER_MODE_KEY, API.BLOG_FILTER_SHOW_ALL, app);
            app.setBlogFilterMode(API.BLOG_FILTER_SHOW_ALL);
            UiHelper.showToast(getString(R.string.saved_all_blogs), app);
        } else if (radioBlogNone.isChecked()) {
            DataHelper.setPreference(API.BLOG_FILTER_MODE_KEY, API.BLOG_FILTER_SHOW_NONE, app);
            checkedBlogs.clear();
            app.setBlogFilterMode(API.BLOG_FILTER_SHOW_NONE);
            UiHelper.showToast(getString(R.string.saved_no_blogs), app);
        } else {
            DataHelper.setPreference(API.BLOG_FILTER_MODE_KEY, API.BLOG_FILTER_SHOW_CHOSEN, app);
            app.setBlogFilterMode(API.BLOG_FILTER_SHOW_CHOSEN);
            app.setBlogFilterPublishers(checkedBlogs);
            UiHelper.showToast(getResources().getQuantityString(R.plurals.saved_chosen_blogs, checkedBlogs.size(), TextUtils.join(", ", checkedBlogs)), app);
        }
        DataHelper.setPreference(API.BLOG_FILTER_TEXT_KEY, new Gson().toJson(checkedBlogs), app);
        app.setBlogFilterPublishers(checkedBlogs);
        app.saveBlogFilterOnline();
    }
}


