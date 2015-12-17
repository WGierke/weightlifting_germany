package de.weightlifting.app.gallery;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.UpdateableItem;
import de.weightlifting.app.UpdateableWrapper;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.JsonParser;

public class Galleries extends UpdateableWrapper {

    public static final String FILE_NAME = "galleries.json";
    public static ArrayList<GalleryItem> itemsToMark = new ArrayList<>();
    public final static int navigationPosition = MainActivity.FRAGMENT_GALLERY;
    public final static int subPosition = 0;
    private final String UPDATE_URL = "https://raw.githubusercontent.com/WGierke/weightlifting_schwedt/updates/production/galleries.json";
    private final String TAG = "Galleries";

    public static ArrayList<GalleryItem> casteArray(ArrayList<UpdateableItem> array) {
        ArrayList<GalleryItem> convertedItems = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            convertedItems.add((GalleryItem) array.get(i));
        }
        return convertedItems;
    }

    public static void markNewItems(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<GalleryItem> oldGalleryItems = casteArray(oldItems);
        ArrayList<GalleryItem> newGalleryItems = casteArray(newItems);
        for (int i = 0; i < newGalleryItems.size(); i++) {
            boolean isNew = true;
            for (int j = 0; j < oldGalleryItems.size(); j++) {
                if (newGalleryItems.get(i).getTitle().equals(oldGalleryItems.get(j).getTitle()) && Arrays.equals(newGalleryItems.get(i).getImageUrls(), oldGalleryItems.get(j).getImageUrls()) && newGalleryItems.get(i).getUrl().equals(oldGalleryItems.get(j).getUrl())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                itemsToMark.add(newGalleryItems.get(i));
            }
        }
    }

    public void refreshItems() {
        super.update(UPDATE_URL, FILE_NAME, TAG);
    }

    protected void updateWrapper(String result) {
        Galleries newItems = new Galleries();
        newItems.parseFromString(result);
        if (items.size() > 0) {
            keepOldReferences(items, newItems.getItems());
            markNewItems(items, newItems.getItems());
        }
        items = newItems.getItems();
    }

    public void parseFromString(String jsonString) {
        //Log.d(WeightliftingApp.TAG, "Parsing gallery JSON...");
        try {
            ArrayList<UpdateableItem> newItems = new ArrayList<>();

            JsonParser jsonParser = new JsonParser();
            jsonParser.getJsonFromString(jsonString);

            JSONArray galleries = jsonParser.getJsonArray("galleries");
            for (int i = 0; i < galleries.length(); i++) {
                try {
                    JSONObject gallery = galleries.getJSONObject(i);

                    GalleryItem item = new GalleryItem();
                    item.setTitle(gallery.getString("title"));
                    item.setUrl(gallery.getString("url"));

                    JSONArray gallery_images = gallery.getJSONArray(("images"));
                    List<String> image_urls = new ArrayList<>();
                    for (int j = 0; j < gallery_images.length(); j++) {
                        image_urls.add(gallery_images.getString(j));
                    }
                    item.setImageUrls(image_urls.toArray(new String[image_urls.size()]));
                    //imageLoader.preloadImage(item.getImageUrls()[0]);
                    newItems.add(item);
                } catch (Exception ex) {
                    Log.e(WeightliftingApp.TAG, "Error while parsing gallery #" + i);
                    ex.printStackTrace();
                }
            }

            setItems(newItems);
            setLastUpdate((new Date()).getTime());
            Log.i(WeightliftingApp.TAG, "Galleries parsed, " + newItems.size() + " items found");
        } catch (Exception ex) {
            Log.e(WeightliftingApp.TAG, "Error while parsing galleries");
            ex.printStackTrace();
        }
    }

    private void keepOldReferences(ArrayList<UpdateableItem> oldItems, ArrayList<UpdateableItem> newItems) {
        ArrayList<GalleryItem> oldGalleryItems = casteArray(oldItems);
        ArrayList<GalleryItem> newGalleryItems = casteArray(newItems);
        for (int i = 0; i < newGalleryItems.size(); i++) {
            for (int j = 0; j < oldGalleryItems.size(); j++) {
                if ((newGalleryItems.get(i)).equals(oldGalleryItems.get(j))) {
                    newGalleryItems.set(i, oldGalleryItems.get(j));
                }
            }
        }
    }
}
