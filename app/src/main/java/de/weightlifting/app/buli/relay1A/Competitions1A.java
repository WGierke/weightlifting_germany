package de.weightlifting.app.buli.relay1A;

import de.weightlifting.app.buli.Competitions;

public class Competitions1A extends Competitions {

    public static final String FILE_NAME = "1A_competitions.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/1A_competitions.json";
    private final String TAG = "Competitions1A";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}