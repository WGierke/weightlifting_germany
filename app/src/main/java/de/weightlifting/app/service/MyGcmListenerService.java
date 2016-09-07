package de.weightlifting.app.service;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.UiHelper;

/**
 * Receive GCM messages
 */

public class MyGcmListenerService extends com.google.android.gms.gcm.GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        sendNotification(data.getString("update"));
    }

    @Override
    public void onDeletedMessages() {
        sendNotification("Deleted messages on server");
    }

    @Override
    public void onMessageSent(String msgId) {
        sendNotification("Upstream message sent. Id=" + msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
        sendNotification("Upstream message send error. Id=" + msgId + ", error" + error);
    }

    //Example message: New Article#Victory in Görlitz#Push the button ...#4#1
    private void sendNotification(String msg) {
        String[] parts = msg.split("#");

        if (parts.length != 5 || !TextUtils.isDigitsOnly(parts[3]) || !TextUtils.isDigitsOnly(parts[4])) {
            Log.e(WeightliftingApp.TAG, "Number of parts: " + String.valueOf(parts.length) + ", notificationID: " + parts[3]);
        } else {
            String buliFilterMode = DataHelper.getPreference(API.BULI_FILTER_MODE_KEY, getApplication());
            String buliFilterText = DataHelper.getPreference(API.BULI_FILTER_TEXT_KEY, getApplication());
            String blogFilterMode = DataHelper.getPreference(API.BLOG_FILTER_MODE_KEY, getApplication());
            String blogFilterText = DataHelper.getPreference(API.BLOG_FILTER_TEXT_KEY, getApplication());

            String title = parts[0];
            String message = parts[1];
            String description = parts[2];
            int fragmentId = Integer.parseInt(parts[3]);
            int subFragmentId = Integer.parseInt(parts[4]);

            if (fragmentId == API.FRAGMENT_NEWS) {
                if (blogMatches(blogFilterMode, blogFilterText, description)) {
                    UiHelper.showNotification(title, message, description, fragmentId, subFragmentId, this);
                }
            } else {
                if (buliMatches(buliFilterMode, buliFilterText, message, description)) {
                    UiHelper.showNotification(title, message, description, fragmentId, subFragmentId, this);
                }
            }
        }
    }

    private boolean buliMatches(String buliFilterMode, String buliFilterText, String message, String description) {
        return buliFilterMode == null || buliFilterMode.equals(API.BULI_FILTER_MODE_NONE) || message.contains(buliFilterText) || description.contains(buliFilterText);
    }

    private boolean blogMatches(String blogFilterMode, String blogFilterText, String description) {
        if (blogFilterMode == null || blogFilterMode.equals(API.BLOG_FILTER_SHOW_ALL)) {
            return true;
        }
        if (blogFilterMode.equals(API.BLOG_FILTER_SHOW_NONE)) {
            return false;
        }
        ArrayList<String> blogFilterPublishers = new Gson().fromJson(blogFilterText, new TypeToken<ArrayList<String>>() {
        }.getType());
        for (String blogFilterPublisher : blogFilterPublishers) {
            if (description.contains(blogFilterPublisher)) {
                return true;
            }
        }
        return false;
    }
}