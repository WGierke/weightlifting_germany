package de.weightlifting.app.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.weightlifting.app.ImageFragment;
import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;

public class NewsArticleFragment extends Fragment {

    private WeightliftingApp app;
    private NewsItem article;

    private TextView heading;
    private TextView content;
    private TextView date;
    private TextView url;
    private ImageView cover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Article fragment");

        View fragment = inflater.inflate(R.layout.news_article, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        heading = (TextView) fragment.findViewById(R.id.article_title);
        content = (TextView) fragment.findViewById(R.id.article_content);
        date = (TextView) fragment.findViewById(R.id.article_date);
        url = (TextView) fragment.findViewById(R.id.article_url);
        cover = (ImageView) fragment.findViewById(R.id.article_cover);

        // Get article information from bundle
        try {
            Bundle bundle = this.getArguments();
            int position = bundle.getInt("item");
            article = (NewsItem) app.getNews(WeightliftingApp.UPDATE_IF_NECESSARY).getItem(position);
            showArticle();
        } catch (Exception ex) {
            ex.printStackTrace();
            showError();
        }

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new ImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("imageURL", article.getImageURL());
                fr.setArguments(bundle);
                ((MainActivity) getActivity()).addFragment(fr, getString(R.string.nav_news), true);
            }
        });

        return fragment;
    }

    private void showArticle() {
        heading.setText(article.getHeading());
        content.setText(article.getContent());
        date.setText(article.getDate());
        url.setText(Html.fromHtml("<a href=\"" + article.getURL() + "\">" + getString(R.string.news_article_url) + "</a>"));
        url.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        if (article.getImageURL() != null) {
            app.getImageLoader().displayImage(article.getImageURL(), cover);
        } else {
            cover.setImageDrawable(getResources().getDrawable(R.drawable.cover_home));
        }
    }

    private void showError() {
        heading.setText(getString(R.string.news_error_heading));
        content.setText(getString(R.string.news_error_content));
        date.setVisibility(View.GONE);

        cover.setImageDrawable(getResources().getDrawable(R.drawable.cover_error));
    }
}
