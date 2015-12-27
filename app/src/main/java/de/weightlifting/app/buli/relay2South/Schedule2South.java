package de.weightlifting.app.buli.relay2South;

import de.weightlifting.app.buli.Schedule;

public class Schedule2South extends Schedule {

    public static final String FILE_NAME = "2South_schedule.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/2South_schedule.json";
    private final String TAG = "Schedule2South";

    public String getRelayName() { return "2. Bundesliga - Staffel Südwest"; }

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
