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
import de.weightlifting.app.SettingsFragment;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.buli.ListViewFragment;
import de.weightlifting.app.helper.API;


public class NewsFeedFragment extends ListViewFragment {

    public News news;
    private NewsFeedListAdapter adapter;
    private boolean is_loading = false;

    @Override
    protected void setEmptyListItem() {
        TextView emptyText = (TextView) fragment.findViewById(R.id.emptyArticles);
        if(app.getBlogFilterMode().equals(API.BLOG_FILTER_SHOW_NONE)) {
            emptyText.setText(R.string.news_no_articles_settings);
            emptyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).addFragment(new SettingsFragment(), getString(R.string.settings), true);
                }
            });
        } else {
            emptyText.setText(R.string.news_no_articles_internet);
        }
        emptyText.setVisibility(View.VISIBLE);
        listViewBuli.setEmptyView(emptyText);
    }

    @Override
    protected void getBuliElements() {
        news = app.getNews(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (news.getFilteredItemsByPublishers(app.getBlogFilterPublishers()).size() == 0) {
            ArrayList<String> firstUrls = getFirstUrlsAndRemove(10, true);
            for (String firstUrl : firstUrls) {
                news.addArticleFromUrl(firstUrl);
            }
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
                Collections.sort(news.getItems(), Collections.reverseOrder());
                adapter = new NewsFeedListAdapter(news.getFilteredItemsByPublishers(app.getBlogFilterPublishers()), getActivity());
                listViewBuli.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
                        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
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
        ArrayList<String> firstUrls = getFirstUrlsAndRemove(3, true);
        for (String firstUrl : firstUrls) {
            news.addArticleFromUrl(firstUrl);
        }
        is_loading = false;
        Runnable refreshRunnable = new Runnable() {
            @Override
            public void run() {
                Collections.sort(news.getItems(), Collections.reverseOrder());
                adapter.setItems(news.getFilteredItemsByPublishers(app.getBlogFilterPublishers()));
                adapter.notifyDataSetChanged();
                listViewBuli.invalidateViews();
            }
        };
        Handler refreshHandler = new Handler();
        refreshHandler.postDelayed(refreshRunnable, News.TIMER_RETRY);
    }

    private ArrayList<String> getFirstUrlsAndRemove(int n, boolean removeUrl) {
        ArrayList<String> firstUrls = new ArrayList<>();
        for (String publisher : app.getBlogFilterPublishers()) {
            ArrayList<String> urls = News.remainingPublisherArticleUrls.get(publisher);
            if (n <= urls.size())
                firstUrls.addAll(urls.subList(0, n));
            else
                firstUrls.addAll(urls);

            if (removeUrl) {
                for (String firstUrl : firstUrls) {
                    News.remainingPublisherArticleUrls.get(publisher).remove(firstUrl);
                }
            }
        }
        return firstUrls;
    }
}