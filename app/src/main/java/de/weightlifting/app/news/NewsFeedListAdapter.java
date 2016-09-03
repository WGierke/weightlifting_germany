package de.weightlifting.app.news;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.UiHelper;


public class NewsFeedListAdapter extends BaseAdapter {

    private ArrayList<NewsItem> items;
    private Activity activity;
    private LayoutInflater inflater;

    public NewsFeedListAdapter(ArrayList<NewsItem> items, Activity activity) {
        this.items = items;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(ArrayList<NewsItem> items) {
        this.items = items;
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
            view = inflater.inflate(R.layout.news_feed_item, null);
        }

        TextView title = (TextView) view.findViewById(R.id.news_title);
        title.setText(items.get(position).getHeading());

        TextView preview = (TextView) view.findViewById(R.id.news_preview);
        preview.setText(items.get(position).getPreview());

        TextView date = (TextView) view.findViewById(R.id.news_date);
        date.setText(items.get(position).getHumanDate());

        TextView publisher = (TextView) view.findViewById(R.id.news_publisher);
        publisher.setText(items.get(position).getPublisher() + "-Blog");

        if (News.itemsToMark.contains(items.get(position))) {
            UiHelper.colorFade(view, activity.getResources());
            News.itemsToMark.remove(items.get(position));
        }

        ImageView icon = (ImageView) view.findViewById(R.id.news_icon);

        if (items.get(position).hasImage()) {
            ((WeightliftingApp) activity.getApplicationContext()).getImageLoader().displayImage(items.get(position).getImageURL(), icon);
        } else {
            icon.setImageResource(R.drawable.icon_germany_bar);
        }
        return view;
    }
}