package de.weightlifting.app.buli.relay2Middle;

import de.weightlifting.app.buli.Schedule;

public class Schedule2Middle extends Schedule {

    public static final String FILE_NAME = "2Middle_schedule.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/2Middle_schedule.json";
    private final String TAG = "2MiddleSchedule";

    public String getRelayName() { return "2. Bundesliga - Staffel Mitte"; }

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
