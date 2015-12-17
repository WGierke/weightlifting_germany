package de.weightlifting.app.news;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.UpdateableItem;
import de.weightlifting.app.helper.UiHelper;

public class NewsEventsListAdapter extends BaseAdapter {

    private ArrayList<EventItem> items;
    private Activity activity;
    private LayoutInflater inflater;

    public NewsEventsListAdapter(ArrayList<UpdateableItem> items, Activity activity) {
        this.items = Events.casteArray(items);
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.news_events_item, null);
        }

        TextView title = (TextView) view.findViewById(R.id.event_title);
        title.setText(items.get(position).getTitle());

        TextView date = (TextView) view.findViewById(R.id.event_date);
        date.setText(items.get(position).getDate());

        TextView location = (TextView) view.findViewById(R.id.event_location);
        location.setText(items.get(position).getLocation());

        if (Events.itemsToMark.contains(items.get(position))) {
            UiHelper.colorFade(view, activity.getResources());
            Events.itemsToMark.remove(items.get(position));
        }

        return view;
    }
}
