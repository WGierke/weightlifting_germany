package de.weightlifting.app.archive;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.R;

public class ArchivedSeasonListAdapter extends BaseAdapter {

    private ArrayList<String> items;
    private LayoutInflater inflater;

    public ArchivedSeasonListAdapter(ArrayList<String> items, Activity activity) {
        this.items = items;
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
            view = inflater.inflate(R.layout.archive_item, null);
        }

        TextView season = (TextView) view.findViewById(R.id.archive_item_season);
        season.setText(items.get(position));

        return view;
    }
}
