package de.weightlifting.app.helper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.junit.Test;

public class NetworkHelperTest {

    @Test
    public void testSendAuthenticatedHttpGetRequest() throws Exception {
        System.out.println("Test");
        Handler getResultHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    Bundle data = msg.getData();
                    String result = data.getString("result");
                    if (result == null || result.equals("")) {
                        return;
                    }
                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        };
    }
}