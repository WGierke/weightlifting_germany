package de.weightlifting.app.buli;

import java.util.Date;

import de.weightlifting.app.UpdateableItem;

public class ScheduleEntry extends UpdateableItem implements Comparable<ScheduleEntry> {

    private String guest;
    private String home;
    private String location;
    private Date dateTime;

    public ScheduleEntry() {
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean equals(ScheduleEntry item2) {
        return guest.equals(item2.getGuest()) && home.equals(item2.getHome())
                && location.equals(item2.getLocation()) && dateTime.equals(item2.dateTime);
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    @Override
    public int compareTo(ScheduleEntry s) {
        return getDateTime().compareTo(s.getDateTime());
    }

}
