package de.weightlifting.app.helper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkHelper {

    public static void getWebRequest(final String url, Handler handler) {
        final Handler mHandler = handler;
        (new Thread() {
            @Override
            public void run() {
                Bundle data = new Bundle();
                try {
                    //Log.d(WeightliftingApp.TAG, "Requesting " + url);
                    String result = getRequest(url);
                    if (result.contains("!DOCTYPE"))
                        result = null;
                    data.putString("result", result);
                } catch (Exception ex) {
                    //Log.d(WeightliftingApp.TAG, "Error while fetching " + url + ":" + ex.getMessage());
                    data.putString("result", "");
                }
                Message message = new Message();
                message.setData(data);
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public static String getRequest(String myurl) throws Exception {
        InputStream is;
        String result;

        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        is = conn.getInputStream();
        result = DataHelper.inputStreamToString(is);

        if (is != null) {
            is.close();
        }

        return result;
    }
}
