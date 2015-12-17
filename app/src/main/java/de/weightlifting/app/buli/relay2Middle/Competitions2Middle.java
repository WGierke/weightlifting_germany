package de.weightlifting.app.buli.relay2Middle;

import de.weightlifting.app.buli.Competitions;

public class Competitions2Middle extends Competitions {

    public static final String FILE_NAME = "2Middle_competitions.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/2Middle_competitions.json";
    private final String TAG = "Competitions2Middle";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
