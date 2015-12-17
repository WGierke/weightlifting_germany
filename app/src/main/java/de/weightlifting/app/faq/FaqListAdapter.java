package de.weightlifting.app.faq;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.R;

public class FaqListAdapter extends BaseAdapter {

    private ArrayList<FaqItem> items;
    private LayoutInflater inflater;

    public FaqListAdapter(ArrayList<FaqItem> items, Activity activity) {
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
            view = inflater.inflate(R.layout.faq_item, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.faqs_heading);
        heading.setText(items.get(position).getHeader());

        TextView question = (TextView) view.findViewById(R.id.faqs_question);
        question.setText(items.get(position).getQuestion());

        return view;
    }
}
