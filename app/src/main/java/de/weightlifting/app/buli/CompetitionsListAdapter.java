package de.weightlifting.app.buli;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.MainActivity;
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
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.buli_competition_item, null);
        }

        TextView home = (TextView) view.findViewById(R.id.buli_competition_home);
        home.setText(items.get(position).getHome());
        if (home.getText().toString().contains(WeightliftingApp.TEAM_NAME))
            home.setText(Html.fromHtml("<u>" + home.getText() + "</u>"));

        TextView guest = (TextView) view.findViewById(R.id.buli_competition_guest);
        guest.setText(items.get(position).getGuest());
        if (guest.getText().toString().contains(WeightliftingApp.TEAM_NAME))
            guest.setText(Html.fromHtml("<u>" + guest.getText() + "</u>"));

        TextView score = (TextView) view.findViewById(R.id.buli_competition_score);
        score.setText(items.get(position).getScore());

        TextView date = (TextView) view.findViewById(R.id.buli_competition_date);
        date.setText(items.get(position).getDate());

        TextView location = (TextView) view.findViewById(R.id.buli_competition_location);
        location.setText(items.get(position).getLocation());

        //Log.d(WeightliftingApp.TAG, "Competition Date: " + items.get(position).getDate() + " " + items.get(position).getHome());
        for(PastCompetition p : Competitions.itemsToMark) {
            //Log.d(WeightliftingApp.TAG, p.getDate() + " " + p.getHome());
        }

        if (Competitions.itemsToMark.contains(items.get(position))) {
            //Log.d(WeightliftingApp.TAG, "item to mark is being displayed");
            UiHelper.colorFade(view, activity.getResources());
            Competitions.itemsToMark.remove(items.get(position));
        }
        return view;
    }
}
