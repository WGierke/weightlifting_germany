package de.weightlifting.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.weightlifting.app.gallery.Galleries;
import de.weightlifting.app.gallery.GalleryOverviewAdapter;
import de.weightlifting.app.helper.UniversalImageLoader.ImageControllerFragment;
import de.weightlifting.app.helper.UniversalImageLoader.ImageGridFragment;
import de.weightlifting.app.news.News;

public class GalleriesFragment extends Fragment {

    private WeightliftingApp app;
    private View fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Gallery fragment");

        fragment = inflater.inflate(R.layout.fragment_galleries, container, false);
        app = (WeightliftingApp) getActivity().getApplicationContext();

        Runnable refreshRunnable = new Runnable() {
            @Override
            public void run() {
                getGalleries();
            }
        };
        Handler refreshHandler = new Handler();
        refreshHandler.postDelayed(refreshRunnable, WeightliftingApp.DISPLAY_DELAY);

        return fragment;
    }

    private void getGalleries() {
        Galleries galleries = app.getGalleries(WeightliftingApp.UPDATE_IF_NECESSARY);
        if (galleries.getItems().size() == 0) {
            // No news items yet
            //Log.d(WeightliftingApp.TAG, "Waiting for galleries...");

            // Check again in a few seconds
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    getGalleries();
                }
            };
            Handler refreshHandler = new Handler();
            refreshHandler.postDelayed(refreshRunnable, News.TIMER_RETRY);
        } else {
            // We have news items to display
            try {
                ListView listViewGalleries = (ListView) fragment.findViewById(R.id.listView_galleries);
                GalleryOverviewAdapter adapter = new GalleryOverviewAdapter(Galleries.casteArray(galleries.getItems()), getActivity());
                listViewGalleries.setAdapter(adapter);
                listViewGalleries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Fragment imageController = new ImageControllerFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("FRAGMENT_INDEX", ImageGridFragment.INDEX);
                        bundle.putInt("GALLERY_POSITION", position);
                        imageController.setArguments(bundle);
                        ((MainActivity) getActivity()).addFragment(imageController, getString(R.string.nav_gallery), true);

                    }


                });
            } catch (Exception ex) {
                Log.e(WeightliftingApp.TAG, "Showing gallery failed");
                ex.printStackTrace();
            }
        }
    }
}
