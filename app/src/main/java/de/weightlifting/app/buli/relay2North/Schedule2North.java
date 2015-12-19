package de.weightlifting.app.buli.relay2North;

import de.weightlifting.app.buli.Schedule;

public class Schedule2North extends Schedule {

    public static final String FILE_NAME = "2North_schedule.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/2North_schedule.json";
    private final String TAG = "Schedule2North";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
