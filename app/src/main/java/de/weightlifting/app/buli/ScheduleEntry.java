package de.weightlifting.app.buli;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.weightlifting.app.UpdateableItem;

public class ScheduleEntry extends UpdateableItem implements Comparable<ScheduleEntry> {

    private String date;
    private String time;
    private String guest;
    private String home;
    private String location;

    public ScheduleEntry() {
    }

    public boolean equals(ScheduleEntry item2) {
        return date.equals(item2.getDate()) && guest.equals(item2.getGuest())
                && home.equals(item2.getHome()) && location.equals(item2.getLocation())
                && time.equals(item2.getTime());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date ownDate = new Date();
        Date otherDate = new Date();
        try {
            ownDate = format.parse(getDate());
            otherDate = format.parse(s.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ownDate.compareTo(otherDate);
    }

}
