package de.weightlifting.app.news;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;

public class NewsEventsFragment extends Fragment {

    private WeightliftingApp app;
    private Events events;
    private ListView listViewEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing News Event fragment");

        View fragment = inflater.inflate(R.layout.news_events, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        listViewEvents = (ListView) fragment.findViewById(R.id.listView_News);

        getEvents();
        int nextEvent = getNextEvent();
        if (nextEvent > 0) nextEvent -= 1; //previous event fades now
        listViewEvents.setSelection(nextEvent);

        return fragment;
    }

    private void getEvents() {
        events = app.getEvents(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (events.getItems().size() == 0) {
            // No events items yet
            //Log.d(WeightliftingApp.TAG, "Waiting for events...");

            // Check again in a few seconds
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getEvents();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, News.TIMER_RETRY);
        } else {
            // We have events items to display
            try {
                NewsEventsListAdapter adapter = new NewsEventsListAdapter(events.getItems(), getActivity());
                listViewEvents.setAdapter(adapter);
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing events failed");
                ex.toString();
            }
        }
    }

    private int getNextEvent() {
        Calendar now = Calendar.getInstance();
        Integer current_day = now.get(Calendar.DATE);
        SimpleDateFormat df = new SimpleDateFormat("MMMM", Locale.GERMAN);
        String current_month = df.format(now.getTime());
        Boolean monthAlreadyFound = false;

        ArrayList<EventItem> items = Events.casteArray(events.getItems());
        EventItem event;
        for (int i = 0; i < items.size(); i++) {
            event = items.get(i);
            if (!monthAlreadyFound) {
                if (event.getDate().contains(current_month)) {
                    monthAlreadyFound = true;
                    if (Integer.valueOf(event.getDate().split("\\.")[0]) >= current_day) {
                        return i;
                    }
                }
            } else {
                if (event.getDate().contains(current_month)) {
                    if (Integer.valueOf(event.getDate().split("\\.")[0]) >= current_day) {
                        return i;
                    }
                } else
                    return i;
            }
        }
        return 0;
    }


}
