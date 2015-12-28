package de.weightlifting.app.buli.relay1A;

import de.weightlifting.app.buli.Schedule;

public class Schedule1A extends Schedule {

    public static final String FILE_NAME = "1A_schedule.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/1A_schedule.json";
    private final String TAG = "Schedule1A";

    public String getRelayName() {
        return "1. Bundesliga - Staffel A";
    }

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
