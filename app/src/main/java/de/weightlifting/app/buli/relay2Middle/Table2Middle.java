package de.weightlifting.app.buli.relay2Middle;

import de.weightlifting.app.buli.Table;

public class Table2Middle extends Table {

    public static final String FILE_NAME = "2Middle_table.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/2Middle_table.json";
    private final String TAG = "2MiddleTable";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
