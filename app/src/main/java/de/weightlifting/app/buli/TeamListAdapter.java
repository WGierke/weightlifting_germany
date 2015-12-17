package de.weightlifting.app.buli;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.UiHelper;

public class TeamListAdapter extends BaseAdapter {

    private ArrayList<TeamMember> items;
    private Activity activity;
    private LayoutInflater inflater;
    private TeamMember item;

    public TeamListAdapter(ArrayList<TeamMember> items, Activity activity) {
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
        item = items.get(position);
        if (convertView == null) {
            view = inflater.inflate(R.layout.buli_team_item, null);
        }

        Resources res = activity.getResources();

        TextView name = (TextView) view.findViewById(R.id.buli_member_name);
        name.setText(item.getName());

        TextView year = (TextView) view.findViewById(R.id.buli_member_year);
        year.setText(res.getString(R.string.buli_year) + ": " + item.getYear());

        TextView snatching = (TextView) view.findViewById(R.id.buli_member_snatching);
        snatching.setText(res.getString(R.string.buli_snatching) + ": " + item.getSnatching() + " kg");

        TextView jerking = (TextView) view.findViewById(R.id.buli_member_jerking);
        jerking.setText(res.getString(R.string.buli_jerking) + ": " + item.getJerking() + " kg");

        TextView maxPoints = (TextView) view.findViewById(R.id.buli_member_max_score);
        maxPoints.setText(res.getString(R.string.buli_relative_points) + ": " + item.getMaxScore());

        if (Team.itemsToMark.contains(item)) {
            UiHelper.colorFade(view, res);
            Team.itemsToMark.remove(item);
        }

        ImageView icon = (ImageView) view.findViewById(R.id.buli_member_image);

        if (item.getImageURL() != null) {
            //NullPointerException
            ((WeightliftingApp) activity.getApplicationContext()).getImageLoader().displayImage(item.getImageURL(), icon);
        } else {
            // Show default cover image
        }

        return view;
    }
}
