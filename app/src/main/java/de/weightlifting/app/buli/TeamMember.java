package de.weightlifting.app.buli;

import de.weightlifting.app.UpdateableItem;

public class TeamMember extends UpdateableItem {

    private String name;
    private String year;
    private String snatching;
    private String jerking;
    private String max_score;
    private String imageURL;

    public TeamMember() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSnatching() {
        return snatching;
    }

    public void setSnatching(String snatching) {
        this.snatching = snatching;
    }

    public String getJerking() {
        return jerking;
    }

    public void setJerking(String jerking) {
        this.jerking = jerking;
    }

    public String getMaxScore() {
        return max_score;
    }

    public void setMaxScore(String max_score) {
        this.max_score = max_score;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean equals(TeamMember item2) {
        return name.equals(item2.getName()) && year.equals(item2.getYear())
                && snatching.equals(item2.getSnatching()) && jerking.equals(item2.getJerking())
                && max_score.equals(item2.getMaxScore()) && imageURL.equals(item2.getImageURL());
    }
}
