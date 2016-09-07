package de.weightlifting.app.helper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import de.weightlifting.app.WeightliftingApp;

public class NetworkHelper {

    public static final String BASE_SERVER_URL = "http://weightliftinggermany.appspot.com";

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

    public static void sendToken(String token, Handler handler) {
        try {
            String data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
            String url = BASE_SERVER_URL + "/add_token";
            sendAuthenticatedHttpPostRequest(url, data, handler);
        } catch (UnsupportedEncodingException ignored) {
            ignored.printStackTrace();
        }

//        try {
//            sendToSlack(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void sendProtocolShare(String competitionParties) {
        try {
            String data = URLEncoder.encode("competitionParties", "UTF-8") + "=" + URLEncoder.encode(competitionParties, "UTF-8");
            String url = BASE_SERVER_URL + "add_protocol";
            sendAuthenticatedHttpPostRequest(url, data, new Handler());
        } catch (UnsupportedEncodingException ignored) {
            ignored.printStackTrace();
        }
    }

    public static void sendFilter(String userId, String filterSetting) {
        try {
            String data = URLEncoder.encode("userId", "UTF-8") + "=" + URLEncoder.encode(userId, "UTF-8");
            data += "&" + URLEncoder.encode("filterSetting", "UTF-8") + "=" + URLEncoder.encode(filterSetting, "UTF-8");
            String url = BASE_SERVER_URL + "/add_filter";
            sendAuthenticatedHttpPostRequest(url, data, new Handler());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void sendBlogFilter(String userId, String blogFilterSetting) {
        try {
            String data = URLEncoder.encode("userId", "UTF-8") + "=" + URLEncoder.encode(userId, "UTF-8");
            data += "&" + URLEncoder.encode("blogFilterSetting", "UTF-8") + "=" + URLEncoder.encode(blogFilterSetting, "UTF-8");
            String url = BASE_SERVER_URL + "/add_blog_filter";
            sendAuthenticatedHttpPostRequest(url, data, new Handler());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void sendAuthenticatedHttpPostRequest(final String url, final String data, final Handler handler) {
        (new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                Bundle resultBundle = new Bundle();
                try {
                    byte[] dataBytes = data.getBytes();
                    int dataLength = dataBytes.length;
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("charset", "utf-8");
                    conn.setRequestProperty("Content-Length", Integer.toString(dataLength));
                    conn.setRequestProperty("X-Secret-Key", Keys.SECRET_KEY);
                    conn.setUseCaches(false);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuffer = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responseBuffer.append(inputLine);
                    }
                    in.close();
                    String response = responseBuffer.toString();
                    resultBundle.putString(API.HANDLER_RESULT_KEY, response);
                } catch (Exception e) {
                    Log.d(WeightliftingApp.TAG, "posting authenticated data failed: " + e.getMessage());
                }
                message.setData(resultBundle);
                handler.sendMessage(message);
            }
        }).start();
    }

    public static void sendAuthenticatedHttpGetRequest(final String url, final Handler handler) {
        (new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                Bundle resultBundle = new Bundle();
                try {
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("charset", "utf-8");
                    conn.setRequestProperty("X-Secret-Key", Keys.SECRET_KEY);
                    conn.setUseCaches(false);

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuffer = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responseBuffer.append(inputLine);
                    }
                    in.close();
                    String response = responseBuffer.toString();
                    resultBundle.putString(API.HANDLER_RESULT_KEY, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message.setData(resultBundle);
                handler.sendMessage(message);
            }
        }).start();
    }

//    public static void sendToSlack(String message) throws Exception {
//        net.steppschuh.slackmessagebuilder.message.Message message2 = new MessageBuilder()
//                .setChannel("#germany")
//                .setUsername("Weightlifting Germany")
//                .setText(message)
//                .build();

//        Webhook webhook = new Webhook(Keys.SLACK_WEB_HOOK);
//        webhook.postMessage(message2);
//    }
}
