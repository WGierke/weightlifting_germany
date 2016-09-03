package de.weightlifting.app.news;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.helper.API;


public class NewsFeedFragment extends ListViewFragment {

    public News news;
    private NewsFeedListAdapter adapter;
    private boolean is_loading = false;
    private int visibleItems = 5;

    @Override
    protected void setEmptyListItem() {
        TextView emptyText = (TextView) fragment.findViewById(R.id.emptyArticles);
        emptyText.setVisibility(View.VISIBLE);
        listViewBuli.setEmptyView(emptyText);
    }

    @Override
    protected void getBuliElements() {
        news = app.getNews(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (news.getItems().size() == 0) {
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getBuliElements();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, News.TIMER_RETRY);
        } else {
            try {
                ArrayList<NewsItem> newsItems = News.casteArray(news.getItems());
                Collections.sort(newsItems, Collections.reverseOrder());
                adapter = new NewsFeedListAdapter(newsItems, getActivity());
                listViewBuli.setAdapter(adapter);
                //addItems();
                listViewBuli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Show an article fragment and put the selected index as argument
                        Fragment article = new NewsArticleFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt(API.ITEM, position);
                        article.setArguments(bundle);
                        ((MainActivity) getActivity()).addFragment(article, getString(R.string.nav_news), true);
                    }
                });
                listViewBuli.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && totalItemCount < news.getItems().size()) {
                            if (!is_loading) {
                                is_loading = true;
                                addItems();
                            }
                        }
                    }
                });
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing news feed failed");
                ex.printStackTrace();
            }
        }
    }

    private void addItems() {
        visibleItems += 5;
        Log.d("weightlifting", "adding more");
        //adapter.setItems(news.getFirstElements(visibleItems));

        for (String publisher : News.remainingPublisherArticleUrls.keySet()) {
            ArrayList<String> urls = News.remainingPublisherArticleUrls.get(publisher);
            ArrayList<String> firstUrls;
            if (5 <= urls.size())
                firstUrls = new ArrayList(urls.subList(0, 5));
            else
                firstUrls = urls;
            for (String firstUrl : firstUrls) {
                news.addArticleFromUrl(firstUrl);
            }
            adapter.setItems(News.casteArray(news.getItems()));
        }
        adapter.notifyDataSetChanged();
        is_loading = false;
    }
}