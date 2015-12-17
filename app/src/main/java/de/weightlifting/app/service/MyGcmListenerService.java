package de.weightlifting.app.service;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import de.weightlifting.app.WeightliftingApp;
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
        //Log.d(WeightliftingApp.TAG, msg);
        String[] parts = msg.split("#");
        //Log.d(WeightliftingApp.TAG, "GCM message: " + parts[0]);

        if (parts.length != 4 || !TextUtils.isDigitsOnly(parts[3])) {
            Log.e(WeightliftingApp.TAG, "Number of parts: " + String.valueOf(parts.length) + ", notificationID: " + parts[3]);
        } else {
            //Log.d(WeightliftingApp.TAG, parts[0]);
            //Log.d(WeightliftingApp.TAG, parts[1]);
            //Log.d(WeightliftingApp.TAG, parts[2]);
            //Log.d(WeightliftingApp.TAG, parts[3]);
//            Looper.prepare();
            UiHelper.showNotification(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), this);
        }
    }
}