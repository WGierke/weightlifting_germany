package de.weightlifting.app.buli;

import de.weightlifting.app.UpdateableItem;

public class PastCompetition extends UpdateableItem {

    private String location;
    private String date;
    private String home;
    private String guest;
    private String score;
    private String url;

    public PastCompetition() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getProtocolUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(PastCompetition item2) {
        return location.equals(item2.getLocation()) && date.equals(item2.getDate())
                && home.equals(item2.getHome()) && guest.equals(item2.getGuest())
                && score.equals(item2.getScore()) && url.equals(item2.getProtocolUrl());
    }

}
