package de.weightlifting.app.news;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.UiHelper;

public class NewsFeedFragment extends Fragment {

    public News news;
    private WeightliftingApp app;
    private ListView listViewNews;
    private NewsFeedListAdapter adapter;
    private boolean is_loading = false;
    private int visibleItems = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing News Feed fragment");

        View fragment = inflater.inflate(R.layout.news_feed, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        listViewNews = (ListView) fragment.findViewById(R.id.listView_News);
        //listViewNews = UiHelper.enableUpScrolling(listViewNews);

        Runnable refreshRunnable = new Runnable() {
            @Override
            public void run() {
                getNews();
            }
        };
        Handler refreshHandler = new Handler();
        refreshHandler.postDelayed(refreshRunnable, WeightliftingApp.DISPLAY_DELAY);

        return fragment;
    }

    private void getNews() {
        news = app.getNews(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (news.getItems().size() == 0) {
            // No news items yet
            //Log.d(WeightliftingApp.TAG, "Waiting for news...");

            // Check again in a few seconds
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getNews();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, News.TIMER_RETRY);
        } else {
            // We have news items to display
            try {
                adapter = new NewsFeedListAdapter(news.getFirstElements(visibleItems), getActivity());
                listViewNews.setAdapter(adapter);
                listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Show an article fragment and put the selected index as argument
                        Fragment article = new NewsArticleFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("item", position);
                        article.setArguments(bundle);
                        ((MainActivity) getActivity()).addFragment(article, getString(R.string.nav_news), true);
                    }
                });
                listViewNews.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) { }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                                if(!is_loading)
                                {
                                    is_loading = true;
                                    addItems();
                                }
                            }
                        }
                    });
        } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing news feed failed");
                ex.toString();
            }
        }
    }

    private void addItems() {
        visibleItems += 5;
        adapter.setItems(news.getFirstElements(visibleItems));
        adapter.notifyDataSetChanged();
        is_loading = false;
    }
}
