package de.weightlifting.app.buli.relay2South;

import de.weightlifting.app.buli.Competitions;

public class Competitions2South extends Competitions {

    public static final String FILE_NAME = "2North_competitions.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/2North_competitions.json";
    private final String TAG = "Competitions2South";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
