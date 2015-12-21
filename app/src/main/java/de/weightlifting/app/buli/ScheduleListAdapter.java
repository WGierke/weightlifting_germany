package de.weightlifting.app.buli;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.API;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.UiHelper;

public class ScheduleListAdapter extends BaseAdapter {

    private ArrayList<ScheduleEntry> items;
    private Activity activity;
    private LayoutInflater inflater;

    public ScheduleListAdapter(ArrayList<ScheduleEntry> items, Activity activity) {
        this.items = items;
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
        WeightliftingApp app = (WeightliftingApp) WeightliftingApp.getContext();

        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.buli_schedule_item, null);
        }

        TextView home = (TextView) view.findViewById(R.id.home);
        home.setText(items.get(position).getHome());
        if (app.getFilterMode().equals(API.FILTER_MODE_CLUB) && home.getText().toString().contains(app.getFilterText()))
            home.setText(Html.fromHtml("<u>" + home.getText() + "</u>"));

        ((TextView) view.findViewById(R.id.vs)).setText(R.string.buli_schedule_vs);

        TextView guest = (TextView) view.findViewById(R.id.guest);
        guest.setText(items.get(position).getGuest());
        if (app.getFilterMode().equals(API.FILTER_MODE_CLUB) && guest.getText().toString().contains(app.getFilterText()))
            guest.setText(Html.fromHtml("<u>" + guest.getText() + "</u>"));

        ((TextView) view.findViewById(R.id.date_at)).setText(R.string.buli_schedule_date_at);

        TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(items.get(position).getDate());

        ((TextView) view.findViewById(R.id.time_at)).setText(R.string.buli_schedule_time_at);

        TextView time = (TextView) view.findViewById(R.id.time);
        time.setText(items.get(position).getTime());

        ((TextView) view.findViewById(R.id.location_in)).setText(R.string.buli_schedule_location_in);

        TextView location = (TextView) view.findViewById(R.id.location);
        location.setText(items.get(position).getLocation());

        if (Table.itemsToMark.contains(items.get(position))) {
            UiHelper.colorFade(view, activity.getResources());
            Table.itemsToMark.remove(items.get(position));
        }

        return view;
    }
}
