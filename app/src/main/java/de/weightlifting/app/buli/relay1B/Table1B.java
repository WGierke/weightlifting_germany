package de.weightlifting.app.buli.relay1B;

import de.weightlifting.app.buli.Table;

public class Table1B extends Table {

    public static final String FILE_NAME = "1B_table.json";
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_germany/master/production/1B_table.json";
    private final String TAG = "1BTable";

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }
}
