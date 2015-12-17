package de.weightlifting.app.faq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.weightlifting.app.FaqFragment;
import de.weightlifting.app.R;

public class FaqAnswerFragment extends Fragment {

    private FaqItem faq;

    private TextView heading;
    private TextView question;
    private TextView answer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Article fragment");

        View fragment = inflater.inflate(R.layout.faq_answer, container, false);

        heading = (TextView) fragment.findViewById(R.id.faq_heading);
        question = (TextView) fragment.findViewById(R.id.faq_question);
        answer = (TextView) fragment.findViewById(R.id.faq_answer);

        // Get article information from bundle
        try {
            Bundle bundle = this.getArguments();
            int position = bundle.getInt("item");
            faq = FaqFragment.faqEntries.get(position);
            showAnswer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return fragment;
    }


    private void showAnswer() {
        heading.setText(faq.getHeader());
        question.setText(faq.getQuestion());
        answer.setText(faq.getAnswer());
    }
}
