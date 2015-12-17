package de.weightlifting.app.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    JSONArray jsonValues;

    public JsonParser() {

    }

    public void getJsonFromString(String json_string) {
        try {
            jsonValues = new JSONArray(json_string);
        } catch (Exception e) {
            jsonValues = null;
            e.printStackTrace();
            return;
        }
    }

    public JSONArray getJsonArray(String key) {
        try {
            String res;
            JSONObject jsonObject = getJsonObject(0);
            res = jsonObject.getString(key);
            return new JSONArray(res);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public JSONObject getJsonObject(int index) {
        try {
            return jsonValues.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}