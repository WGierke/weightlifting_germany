package de.weightlifting.app.buli.relay1A;

import de.weightlifting.app.buli.Table;

public class Table1A extends Table {

    public static final String FILE_NAME = "1A_table.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/1A_table.json";
    private final String TAG = "1ATable";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
