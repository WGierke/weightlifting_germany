package de.weightlifting.app.buli.relay2South;

import de.weightlifting.app.buli.Schedule;

public class Schedule2South extends Schedule {

    public static final String FILE_NAME = "2North_schedule.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/2North_schedule.json";
    private final String TAG = "2NorthSchedule";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
