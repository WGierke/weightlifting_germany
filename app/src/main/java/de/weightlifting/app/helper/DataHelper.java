package de.weightlifting.app.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;

import de.weightlifting.app.R;
import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.Table;

public class DataHelper {

    public static final String PREF_FILE_NAME = "weightlifting_ger_preferences";
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    public static String inputStreamToString(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return new String(total);
    }

    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    public static String getDeviceOs() {
        return "Android " + android.os.Build.VERSION.RELEASE;
    }

    public static String getAppVersion(Application application) {
        try {
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "n.a.";
        }
    }

    public static int getAppVersionCode(Application application) {
        try {
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static void setPreference(String prev_name, String prev_value, Application application) {
        SharedPreferences preferences = application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(prev_name, prev_value);
        editor.apply();
    }

    public static void setPreference(String prev_name, Boolean prev_value, Application application) {
        SharedPreferences preferences = application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(prev_name, prev_value);
        editor.apply();
    }

    public static void deletePreference(String prev_name, Application application) {
        SharedPreferences preferences = application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(prev_name);
        editor.apply();
    }

    public static Boolean checkPreference(String prev_name, Application application) {
        SharedPreferences preferences = application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.contains(prev_name);
    }

    public static String getPreference(String prev_name, Application application) {
        SharedPreferences preferences = application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(prev_name, null);
    }

    public static Boolean getPreference(String prev_name, Boolean prev_default, Application application) {
        SharedPreferences preferences = application.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(prev_name, prev_default);
    }

    public static String getSetting(String prev_name, Application application) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(application);
        return sharedPref.getString(prev_name, null);
    }

    public static Boolean getSetting(String prev_name, Boolean prev_default, Application application) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(application);
        return sharedPref.getBoolean(prev_name, prev_default);
    }

    public static String readIntern(String fileName, Context context) {
        try {
            FileInputStream in = context.getApplicationContext().openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            //Log.d(WeightliftingApp.TAG, "read from " + fileName + " " + sb.toString().substring(0, 20) + "...");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void saveIntern(String content, String fileName, Context context) {
        try {
            FileOutputStream fos = context.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
            //Log.d(WeightliftingApp.TAG, "saved in " + fileName + " content: " + content.substring(0, 20) + "...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendMessage(Handler mHandler, String key, String value) {
        //Log.d(WeightliftingApp.TAG, "Sending message to handler: [" + key + "] " + value);
        Bundle data = new Bundle();
        data.putString(key, value);
        Message message = new Message();
        message.setData(data);
        mHandler.sendMessage(message);
    }

    public static void sendMessage(Handler mHandler, String key, int value) {
        //Log.d(WeightliftingApp.TAG, "Sending message to handler: [" + key + "] " + String.valueOf(value));
        Bundle data = new Bundle();
        data.putInt(key, value);
        Message message = new Message();
        message.setData(data);
        mHandler.sendMessage(message);
    }

    public static String getStringFromStream(InputStream inputStream) {
        if (inputStream != null) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String trimString(String string, int length) {
        if (string.length() > length) {
            String result = string.substring(0, length);
            if (result.contains(" ")) {
                result = result.substring(0, result.lastIndexOf(" "));
                result = result + " ...";
            }
            return result;
        } else {
            return string;
        }
    }

    /**
     * @return List of archived seasons e.g. ("2006/2007", "2007/2008")
     */
    public static ArrayList<String> getSeasons() {
        ArrayList<String> seasons = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for (int i = 0; i < fields.length - 1; i++) {
            String name = fields[i].getName();
            if (name.contains("table") || name.contains("competitions")) {
                String season = name.split("_")[0].substring(1);
                season = "20" + season.substring(0, 2) + "/20" + season.substring(2);
                if (!seasons.contains(season)) {
                    seasons.add(season);
                }
            }
        }
        return seasons;
    }

    /**
     * @param filterSeason Season to filter for e.g. "2006/2007"
     * @return Leage and relay of the specified season e.g. ("1. Bundesliga - Staffel Nord", "2. Bundesliga - Staffel Südwest")
     */
    public static ArrayList<String> getRelays(String filterSeason) {
        String season = filterSeason.split("/")[0].substring(2);
        season += filterSeason.split("/")[1].substring(2);

        ArrayList<String> seasons = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for (int i = 0; i < fields.length - 1; i++) {
            String name = fields[i].getName();
            if (name.contains("table") || name.contains("competitions")) {
                if (name.contains("r" + season)) {
                    String league = String.valueOf(name.split("_")[1].charAt(0));
                    String relay = name.split("_")[1].substring(1);
                    relay = relay.replace("ue", "ü").replace("oe", "ö").replace("ae", "ä");
                    relay = Character.toUpperCase(relay.charAt(0)) + relay.substring(1);
                    String leagueRelay = league + ". Bundesliga - Staffel " + relay;
                    if (!seasons.contains(leagueRelay)) {
                        seasons.add(leagueRelay);
                    }
                }
            }
        }
        return seasons;
    }

    /**
     * @param filterSeason Season to filter for e.g. "2006/2007"
     * @param filterRelay  Relay to filter for e.g. "1. Bundesliga - Staffel Süd"
     * @return Prefix of files that contain data about the specified season and relay e.g. r0607_1sued_
     */
    private static String getFileNamePrefixFromSeasonRelay(String filterSeason, String filterRelay) {
        String resourceName = "r";
        resourceName += filterSeason.split("/")[0].substring(2);
        resourceName += filterSeason.split("/")[1].substring(2);
        resourceName += "_";
        resourceName += String.valueOf(filterRelay.charAt(0));
        filterRelay = filterRelay.split("Staffel ")[1].toLowerCase();
        filterRelay = filterRelay.replace("ü", "ue").replace("ä", "ae").replace("ö", "oe");
        resourceName += filterRelay + "_";
        return resourceName;
    }

    /**
     * @param filterSeason Season to filter for e.g. "2006/2007"
     * @param filterRelay  Relay to filter for e.g. "1. Bundesliga - Staffel Süd"
     * @return Competitions instance for specified season and relay e.g. content of r0607_1sued_competitions.json
     */
    public static Competitions getCompetitionFromSeasonRelay(String filterSeason, String filterRelay, Context context) {
        String resourceName = getFileNamePrefixFromSeasonRelay(filterSeason, filterRelay) + "competitions";
        InputStream ins = context.getResources().openRawResource(
                context.getResources().getIdentifier(resourceName, "raw", context.getPackageName()));
        String content = getStringFromStream(ins);
        Competitions competitions = new Competitions();
        competitions.parseFromString(content);
        return competitions;
    }

    /**
     * @param filterSeason Season to filter for e.g. "2006/2007"
     * @param filterRelay  Relay to filter for e.g. "1. Bundesliga - Staffel Süd"
     * @return Table instance for specified season and relay e.g. content of r0607_1sued_table.json
     */
    public static Table getTableFromSeasonRelay(String filterSeason, String filterRelay, Context context) {
        String resourceName = getFileNamePrefixFromSeasonRelay(filterSeason, filterRelay) + "table";
        InputStream ins = context.getResources().openRawResource(
                context.getResources().getIdentifier(resourceName, "raw", context.getPackageName()));
        String content = getStringFromStream(ins);
        Table table = new Table();
        table.parseFromString(content);
        return table;
    }
}
