package de.weightlifting.app.news;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public static final String FILE_NAME = "news.json";
    public static final String url = "news.json";
    public static ArrayList<NewsItem> itemsToMark = new ArrayList<>();
    public static HashMap<String, ArrayList<String>> remainingPublisherArticleUrls = new HashMap<>();

    public static ArrayList<NewsItem> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<NewsItem> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((NewsItem) array.get(i));
        }
        return convertedItems;
    }

    public static void markNewItems(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<NewsItem> oldNewsItems = casteArray(oldItems);
        ArrayList<NewsItem> newNewsItems = casteArray(newItems);
        for (int i = 0; i < newNewsItems.size(); i++) {
            boolean isNew = true;
            for (int j = 0; j < oldNewsItems.size(); j++) {
                if (newNewsItems.get(i).getContent().equals(oldNewsItems.get(j).getContent()) && newNewsItems.get(i).getDate().equals(oldNewsItems.get(j).getDate()) && newNewsItems.get(i).getHeading().equals(oldNewsItems.get(j).getHeading()) && newNewsItems.get(i).getImageURL().equals(oldNewsItems.get(j).getImageURL()) && newNewsItems.get(i).getURL().equals(oldNewsItems.get(j).getURL())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                itemsToMark.add(newNewsItems.get(i));
            }
        }
    }

    public void refreshItems() {
        final String TAG = "News";
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
                    //DataHelper.saveIntern(result, FILENAME, WeightliftingApp.getContext());

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

    protected void updateWrapper(String result) {
        News newItems = new News();
        newItems.parseFromString(result);
        if (items.size() > 0) {
            keepOldReferences(items, newItems.getItems());
            markNewItems(items, newItems.getItems());
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

    public void addArticleFromUrl(String url) {
        url = NetworkHelper.BASE_SERVER_URL + "/get_article?url=" + url;
        try {
            Handler parseArticleHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        Bundle data = msg.getData();
                        String result = data.getString(API.HANDLER_RESULT_KEY);
                        NewsItem newsItem = getNewsItemFromString(result);
                        items.add(newsItem);
                        System.out.println("added news item " + newsItem.getHeading());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            NetworkHelper.sendAuthenticatedHttpGetRequest(url, parseArticleHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addPublisher(String publisher) {
        remainingPublisherArticleUrls.put(publisher, new ArrayList<String>());
    }

    public void addArticleUrlsForPublishers() {
        for (String publisher : remainingPublisherArticleUrls.keySet()) {
            addArticleUrlsForPublisher(publisher);
        }
    }

    public void addArticleUrlsForPublisher(final String publisher) {
        String url = NetworkHelper.BASE_SERVER_URL + "/get_articles?publisher=" + publisher;

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
                                remainingPublisherArticleUrls.get(publisher).add(url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    private void keepOldReferences(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
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

    public ArrayList<NewsItem> getFirstElements(int n) {
        if (n <= items.size())
            return new ArrayList(items.subList(0, n));
        else
            return casteArray(items);
    }

    public ArrayList<UpdateableItem> getFilteredItemsByPublishers(ArrayList<String> publishers) {
        ArrayList<UpdateableItem> filteredItems = new ArrayList<>();
        NewsItem newsItem;
        for (UpdateableItem item : items) {
            newsItem = (NewsItem) item;
            if(publishers.contains(newsItem.getPublisher())) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }
}