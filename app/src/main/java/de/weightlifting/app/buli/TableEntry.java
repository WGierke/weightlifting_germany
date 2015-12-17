package de.weightlifting.app.buli;

import de.weightlifting.app.UpdateableItem;

public class TableEntry extends UpdateableItem {

    private String place;
    private String club;
    private String score;
    private String max_score;
    private String cardinal_points;

    public TableEntry() {

    }

    public boolean equals(TableEntry item2) {
        return place.equals(item2.getPlace()) && club.equals(item2.getClub())
                && score.equals(item2.getScore()) && max_score.equals(item2.getMaxScore())
                && cardinal_points.equals(item2.getCardinalPoints());
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getMaxScore() {
        return max_score;
    }

    public void setMaxScore(String max_score) {
        this.max_score = max_score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCardinalPoints() {
        return cardinal_points;
    }

    public void setCardinalPoints(String cardinal_points) {
        this.cardinal_points = cardinal_points;
    }

}
