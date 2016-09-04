package de.weightlifting.app.service;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import de.weightlifting.app.helper.API;
import de.weightlifting.app.WeightliftingApp;
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

    //Example message: New Article#Victory in Görlitz#Push the button ...#4
    private void sendNotification(String msg) {
        String[] parts = msg.split("#");

        if (parts.length != 5 || !TextUtils.isDigitsOnly(parts[3]) || !TextUtils.isDigitsOnly(parts[4])) {
            Log.e(WeightliftingApp.TAG, "Number of parts: " + String.valueOf(parts.length) + ", notificationID: " + parts[3]);
        } else {
            String filterMode = DataHelper.getPreference(API.BULI_FILTER_MODE_KEY, getApplication());
            String filterText = DataHelper.getPreference(API.BULI_FILTER_TEXT_KEY, getApplication());

            //title - parts[0]
            //message - parts[1]
            //description - parts[2]
            //fragmentId - parts[3]
            //subFragmentId - parts[4]

            if (filterMode == null || filterMode.equals(API.BULI_FILTER_MODE_NONE) || msg.contains(filterText)) {
                UiHelper.showNotification(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), this);
            }
        }
    }
}