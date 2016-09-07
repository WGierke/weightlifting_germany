/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.weightlifting.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.NetworkHelper;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private SharedPreferences sharedPreferences;
    private String newToken;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            newToken = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //Log.d(TAG, "GCM Registration Token: " + newToken);
            String oldToken = sharedPreferences.getString(GCMPreferences.TOKEN, "");
            boolean sentToServer = sharedPreferences.getBoolean(GCMPreferences.SENT_TOKEN_TO_SERVER, false);

            if (!newToken.equals(oldToken) || !sentToServer) {
                sendRegistrationToServer(newToken);
            }
        } catch (Exception e) {
            sharedPreferences.edit().putBoolean(GCMPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    /**
     * Persist registration to third-party servers.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Log.d(WeightliftingApp.TAG, "Sending new token: " + token);

        //notify service that token was successfully uploaded
        Handler callBackHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    Bundle data = msg.getData();
                    String result = data.getString(API.HANDLER_RESULT_KEY);
                    if (result.equals("Added token successfully") || result.equals("This token is already saved")) {
                        Log.d(TAG, "sendRegistrationToServer - Success: " + result);
                        sharedPreferences.edit().putBoolean(GCMPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                        sharedPreferences.edit().putString(GCMPreferences.TOKEN, newToken).apply();
                    } else {
                        sharedPreferences.edit().putBoolean(GCMPreferences.SENT_TOKEN_TO_SERVER, false).apply();
                        Log.d(TAG, "sendRegistrationToServer - Failure: " + result);
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                Looper.myLooper().quit();
            }
        };
        NetworkHelper.sendToken(token, callBackHandler);
        Looper.loop();
    }
}