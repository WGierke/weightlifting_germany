package de.weightlifting.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.NetworkHelper;

/**
 * This class holds the updateable items and information about the last update, update status and update timer.
 */

public abstract class UpdateableWrapper {

    // Refresh if older than 30 minutes
    public static final long TIMER_INVALIDATE = 1000 * 60 * 30;

    // If not yet ready, try again in 30 seconds
    public static final long TIMER_RETRY = 1000 * 30;
    public static ArrayList<UpdateableItem> itemsToMark = new ArrayList<>();
    protected boolean isUpdating = false;
    protected boolean updateFailed = false;
    protected boolean isUpToDate = false;
    protected long lastUpdate = 0;
    protected ArrayList<UpdateableItem> items;

    public UpdateableWrapper() {
        items = new ArrayList<>();
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<UpdateableItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<UpdateableItem> items) {
        this.items = items;
    }

    public UpdateableItem getItem(int position) {
        return items.get(position);
    }

    public boolean needsUpdate() {
        // Update only if last refresh is older than 30 minutes
        long now = new Date().getTime();
        return (lastUpdate < now - TIMER_INVALIDATE);
    }

    /**
     * Parse the given JSON string to the concrete wrapper
     *
     * @param jsonResult JSON string to parse
     */
    protected abstract void updateWrapper(String jsonResult);

    protected abstract void parseFromString(String jsonString);

    protected abstract void refreshItems();

    /**
     * Download the JSON result from the given URL to the specified file
     *
     * @param url      URL to download JSON file from
     * @param fileName File name to save received data in
     * @param tag      Name of the concrete wrapper that should be displayed in the logs
     */
    protected void update(String url, String fileName, String tag) {
        final String FILENAME = fileName;
        final String TAG = tag;

        //Log.i(WeightliftingApp.TAG, "Updating " + TAG + " ...");
        isUpdating = true;
        updateFailed = false;

        Handler callBackHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    Bundle data = msg.getData();
                    String result = data.getString("result");
                    if (result == null || result.equals("")) {
                        //Log.d(WeightliftingApp.TAG, TAG + " returned nothing");
                        isUpdating = false;
                        updateFailed = true;
                        return;
                    }
                    DataHelper.saveIntern(result, FILENAME, WeightliftingApp.getContext());

                    updateWrapper(result);

                    //Log.i(WeightliftingApp.TAG, TAG + " updated");
                } catch (Exception ex) {
                    Log.e(WeightliftingApp.TAG, TAG + " update failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
                isUpToDate = true;
                isUpdating = false;
            }
        };
        NetworkHelper.getWebRequest(url, callBackHandler);
    }
}
