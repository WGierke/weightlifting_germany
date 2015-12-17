package de.weightlifting.app.news;

import de.weightlifting.app.UpdateableItem;
import de.weightlifting.app.helper.DataHelper;

public class EventItem extends UpdateableItem {

    private String title;
    private String date;
    private String location;
    private String preview;

    public EventItem() {

    }

    public boolean equals(EventItem item2) {
        return title.equals(item2.getTitle()) && date.equals(item2.getDate()) && location.equals(item2.getLocation()) && preview.equals(item2.getPreview());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setPreview(DataHelper.trimString(title, 50));
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
