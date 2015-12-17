package de.weightlifting.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.weightlifting.app.faq.FaqAnswerFragment;
import de.weightlifting.app.faq.FaqItem;
import de.weightlifting.app.faq.FaqListAdapter;

public class FaqFragment extends Fragment {

    public static ArrayList<FaqItem> faqEntries = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Faq fragment");

        View fragment = inflater.inflate(R.layout.fragment_faq, container, false);

        FaqListAdapter adapter = new FaqListAdapter(faqEntries, getActivity());

        ListView faqMenu = (ListView) fragment.findViewById(R.id.listView_faqs);
        faqMenu.setAdapter(adapter);
        faqMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Show an article fragment and put the selected index as argument
                Fragment faqAnswer = new FaqAnswerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("item", position);
                faqAnswer.setArguments(bundle);
                ((MainActivity) getActivity()).addFragment(faqAnswer, getString(R.string.nav_faq), true);
            }
        });

        return fragment;
    }
}