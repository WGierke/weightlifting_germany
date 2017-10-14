package de.weightlifting.app.news;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.weightlifting.app.UpdateableItem;
import de.weightlifting.app.UpdateableWrapper;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.NetworkHelper;


public class News extends UpdateableWrapper {

    public static ArrayList<String> newArticleUrlsToMark = new ArrayList<>();
    public static HashMap<String, ArrayList<String>> existingPublisherArticleUrls = new HashMap<>();
    public static HashMap<String, ArrayList<String>> remainingPublisherArticleUrls = new HashMap<>();
    public static boolean isUpdating = false;
    public static boolean updateFailed = false;

    public static ArrayList<NewsItem> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<NewsItem> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((NewsItem) array.get(i));
        }
        return convertedItems;
    }

    public void refreshItems() {
    }

    protected void updateWrapper(String result) {
        News newItems = new News();
        newItems.parseFromString(result);
        if (items.size() > 0) {
            keepOldReferences(items, newItems.getItems());
        }
        items = newItems.getItems();
    }

    public void parseFromString(String jsonString) {
        try {
            ArrayList<UpdateableItem> newItems = new ArrayList<>();
            newItems.add(getNewsItemFromString(jsonString));
            setItems(newItems);
            setLastUpdate((new Date()).getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public NewsItem getNewsItemFromString(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject result = jsonObject.getJSONObject("result");

        NewsItem item = new NewsItem();
        item.setPublisher(result.getString("publisher"));
        item.setHeading(result.getString("heading"));
        item.setContent(result.getString("content"));
        item.setPreview(DataHelper.trimString(item.getContent().replace("\n", "").replace("\r", ""), 30));
        item.setURL(result.getString("url"));
        item.setImageURL(result.getString("image"));

        String epochString = result.getString("date").replace(".0", "");
        long epoch = Long.parseLong(epochString);
        item.setDate(new Date(epoch * 1000));
        return item;
    }

    /**
     * Add an article given its URL either from cached local storage or by downloading it
     */
    public void addArticleFromUrl(final String articleUrl) {
        File file = WeightliftingApp.getContext().getFileStreamPath(URLEncoder.encode(articleUrl));
        if (file.exists()) {
            try {
                String fileContent = DataHelper.readIntern(URLEncoder.encode(articleUrl), WeightliftingApp.getContext());
                if (!fileContent.equals("")) {
                    NewsItem newsItem = getNewsItemFromString(fileContent);
                    if (!existingPublisherArticleUrls.get(newsItem.getPublisher()).contains(articleUrl)) {
                        items.add(newsItem);
                        ArrayList oldExistingPublisherArticleUrls = existingPublisherArticleUrls.get(newsItem.getPublisher());
                        oldExistingPublisherArticleUrls.add(articleUrl);
                        existingPublisherArticleUrls.put(newsItem.getPublisher(), oldExistingPublisherArticleUrls);
                        remainingPublisherArticleUrls.get(newsItem.getPublisher()).remove(articleUrl);
                    }
                }
            } catch (Exception e) {
                loadArticleFromUrl(articleUrl);
            }
        } else {
            loadArticleFromUrl(articleUrl);
        }
    }

    /**
     * Download an article
     */
    private void loadArticleFromUrl(final String url) {
        String requestUrl = NetworkHelper.BASE_SERVER_MAIN_URL + "/get_article?url=" + url;
        try {
            Handler parseArticleHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        Bundle data = msg.getData();
                        String result = data.getString(API.HANDLER_RESULT_KEY);
                        DataHelper.saveIntern(result, URLEncoder.encode(url), WeightliftingApp.getContext());
                        NewsItem newsItem = getNewsItemFromString(result);
                        if (!existingPublisherArticleUrls.get(newsItem.getPublisher()).contains(newsItem.getURL())) {
                            items.add(newsItem);
                            ArrayList oldExistingPublisherArticleUrls = existingPublisherArticleUrls.get(newsItem.getPublisher());
                            oldExistingPublisherArticleUrls.add(newsItem.getURL());
                            existingPublisherArticleUrls.put(newsItem.getPublisher(), oldExistingPublisherArticleUrls);
                        }
                    } catch (Exception e) {
                        if (News.isUpdating) {
                            News.updateFailed = true;
                        }
                        e.printStackTrace();
                    }
                }
            };
            NetworkHelper.sendAuthenticatedHttpGetRequest(requestUrl, parseArticleHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addPublisher(String publisher) {
        existingPublisherArticleUrls.put(publisher, new ArrayList<String>());
        remainingPublisherArticleUrls.put(publisher, new ArrayList<String>());
    }

    public void addArticleUrlsForPublishers() {
        for (String publisher : remainingPublisherArticleUrls.keySet()) {
            addArticleUrlsForPublisher(publisher, 0);
        }
    }

    public void refreshArticleUrlsForPublishers() {
        for (String publisher : remainingPublisherArticleUrls.keySet()) {
            remainingPublisherArticleUrls.get(publisher).clear();
        }
        items.clear();
        addArticleUrlsForPublishers();
    }

    private boolean articleUrlAlreadyExists(String newUrl, String publisher) {
        for (String existingUrl : remainingPublisherArticleUrls.get(publisher)) {
            if (existingUrl.equals(newUrl)) {
                return true;
            }
        }
        for (UpdateableItem item : items) {
            NewsItem newsItem = (NewsItem) item;
            if (newsItem.getURL().equals(newUrl)) {
                return true;
            }
        }
        File file = WeightliftingApp.getContext().getFileStreamPath(URLEncoder.encode(newUrl));
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private boolean remainingArticleUrlsAreInitialized() {
        for (String publisher : remainingPublisherArticleUrls.keySet()) {
            if (remainingPublisherArticleUrls.get(publisher).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Download 10 articles of a publisher for the given offset
     */
    public void addArticleUrlsForPublisher(final String publisher, final int offset) {
        String url = NetworkHelper.BASE_SERVER_MAIN_URL + "/get_articles?publisher=" + publisher + "&offset=" + String.valueOf(offset);

        try {
            Handler addArticlesHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        Bundle data = msg.getData();
                        String result = data.getString(API.HANDLER_RESULT_KEY);

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray urls = jsonObject.getJSONArray("result");

                        for (int i = 0; i < urls.length(); i++) {
                            try {
                                String url = urls.getJSONObject(i).getString("url");
                                if (WeightliftingApp.initializedNews) {
                                    if (!articleUrlAlreadyExists(url, publisher)) {
                                        newArticleUrlsToMark.add(url);
                                    }
                                }
                                if (!remainingPublisherArticleUrls.get(publisher).contains(url)) {
                                    remainingPublisherArticleUrls.get(publisher).add(url);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (remainingArticleUrlsAreInitialized()) {
                            WeightliftingApp.initializedNews = true;
                            News.isUpdating = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            NetworkHelper.sendAuthenticatedHttpGetRequest(url, addArticlesHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void keepOldReferences
            (ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<NewsItem> oldNewItems = casteArray(oldItems);
        ArrayList<NewsItem> newNewsItems = casteArray(newItems);
        for (int i = 0; i < newNewsItems.size(); i++) {
            for (int j = 0; j < oldNewItems.size(); j++) {
                if ((newNewsItems.get(i)).equals(oldNewItems.get(j))) {
                    newNewsItems.set(i, oldNewItems.get(j));
                }
            }
        }
    }

    public ArrayList<UpdateableItem> getFilteredItemsByPublishers
            (ArrayList<String> publishers) {
        ArrayList<UpdateableItem> filteredItems = new ArrayList<>();
        NewsItem newsItem;
        for (UpdateableItem item : items) {
            newsItem = (NewsItem) item;
            if (publishers.contains(newsItem.getPublisher())) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }
}