package de.weightlifting.app.news;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.weightlifting.app.UpdateableItem;

public class NewsItem extends UpdateableItem implements Comparable<NewsItem> {

    private String publisher;
    private String heading;
    private String content;
    private String preview;
    private String url;
    private String imageURL;
    private Date date;

    public NewsItem() {

    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url_full) {
        this.url = url_full;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String url_image) {
        this.imageURL = url_image;
    }

    public boolean equals(NewsItem item2) {
        return heading.equals(item2.getHeading()) && content.equals(item2.getContent())
                && url.equals(item2.getURL()) && imageURL.equals(item2.getImageURL())
                && date.equals(item2.getDate());
    }

    public boolean hasImage() {
        return imageURL != null && imageURL.length() > 0;
    }

    public String getHumanDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(getDate());
    }

    @Override
    public int compareTo(NewsItem another) {
        return getDate().compareTo(another.getDate());
    }
}

