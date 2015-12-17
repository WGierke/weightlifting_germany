package de.weightlifting.app.buli.relay1B;

import de.weightlifting.app.buli.Competitions;

public class Competitions1B extends Competitions {

    public static final String FILE_NAME = "1B_competitions.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/1B_competitions.json";
    private final String TAG = "Competitions1B";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
