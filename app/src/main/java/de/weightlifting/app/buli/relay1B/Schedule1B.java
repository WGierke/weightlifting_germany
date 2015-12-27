package de.weightlifting.app.buli.relay1B;

import de.weightlifting.app.buli.Schedule;

public class Schedule1B extends Schedule {

    public static final String FILE_NAME = "1B_schedule.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/1B_schedule.json";
    private final String TAG = "Schedule1B";

    public String getRelayName() { return "1. Bundesliga - Staffel B"; }

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
