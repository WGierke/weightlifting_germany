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

import de.weightlifting.app.helper.API;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.UiHelper;

public class CompetitionsListAdapter extends BaseAdapter {

    private ArrayList<PastCompetition> items;
    private Activity activity;
    private LayoutInflater inflater;

    public CompetitionsListAdapter(ArrayList<PastCompetition> items, Activity activity) {
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
        WeightliftingApp app = (WeightliftingApp) activity.getApplication();

        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.buli_competition_item, null);
        }

        TextView home = (TextView) view.findViewById(R.id.buli_competition_home);
        home.setText(items.get(position).getHome());
        if (app.getBuliFilterMode().equals(API.BULI_FILTER_MODE_CLUB) && home.getText().toString().contains(app.getBuliFilterText()))
            home.setText(Html.fromHtml("<u>" + home.getText() + "</u>"));

        TextView guest = (TextView) view.findViewById(R.id.buli_competition_guest);
        guest.setText(items.get(position).getGuest());
        if (app.getBuliFilterMode().equals(API.BULI_FILTER_MODE_CLUB) && guest.getText().toString().contains(app.getBuliFilterText()))
            guest.setText(Html.fromHtml("<u>" + guest.getText() + "</u>"));

        TextView score = (TextView) view.findViewById(R.id.date_at);
        score.setText(items.get(position).getScore());

        TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(items.get(position).getDate());

        TextView location = (TextView) view.findViewById(R.id.time);
        location.setText(items.get(position).getLocation());

        if (Competitions.itemsToMark.contains(items.get(position))) {
            UiHelper.colorFade(view, activity.getResources());
            Competitions.itemsToMark.remove(items.get(position));
        }
        return view;
    }
}
