package de.weightlifting.app.gallery;


import java.util.Arrays;

import de.weightlifting.app.UpdateableItem;

public class GalleryItem extends UpdateableItem {

    private String title;
    private String[] imageUrls;
    private String url;

    public GalleryItem() {

    }

    public boolean equals(GalleryItem item2) {
        return title.equals(item2.getTitle()) && url.equals(item2.getUrl()) && Arrays.equals(imageUrls, item2.getImageUrls());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getImageUrls() {
        return this.imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

}