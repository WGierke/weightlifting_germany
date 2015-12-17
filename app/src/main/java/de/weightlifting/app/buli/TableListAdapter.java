package de.weightlifting.app.buli;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.UiHelper;

public class TableListAdapter extends BaseAdapter {

    private ArrayList<TableEntry> items;
    private Activity activity;
    private LayoutInflater inflater;

    public TableListAdapter(ArrayList<TableEntry> items, Activity activity) {
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
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.buli_table_item, null);
        }

        Resources res = activity.getResources();

        TextView place = (TextView) view.findViewById(R.id.buli_table_place);
        place.setText(items.get(position).getPlace() + ". " + res.getString(R.string.buli_place));

        TextView club = (TextView) view.findViewById(R.id.buli_table_club);
        club.setText(items.get(position).getClub());
        if (club.getText().toString().contains(WeightliftingApp.TEAM_NAME))
            club.setText(Html.fromHtml("<u>" + club.getText() + "</u>"));

        TextView score = (TextView) view.findViewById(R.id.buli_table_score);
        score.setText(items.get(position).getScore() + " " + res.getString(R.string.buli_relative_points));

        TextView cardinalPoints = (TextView) view.findViewById(R.id.buli_table_cardinal_points);
        cardinalPoints.setText(items.get(position).getCardinalPoints() + " " + res.getString(R.string.buli_points));

        TextView maxPoints = (TextView) view.findViewById(R.id.buli_table_max_score);
        maxPoints.setText(items.get(position).getMaxScore() + " " + res.getString(R.string.buli_max_score));

        if (Table.itemsToMark.contains(items.get(position))) {
            UiHelper.colorFade(view, activity.getResources());
            Table.itemsToMark.remove(items.get(position));
        }

        return view;
    }
}
