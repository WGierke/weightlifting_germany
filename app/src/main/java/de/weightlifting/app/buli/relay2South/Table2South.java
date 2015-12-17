package de.weightlifting.app.buli.relay2South;

import de.weightlifting.app.buli.Table;

public class Table2South extends Table {

    public static final String FILE_NAME = "2North_table.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/2North_table.json";
    private final String TAG = "2NorthTable";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
