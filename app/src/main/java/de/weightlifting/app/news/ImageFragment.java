package de.weightlifting.app.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.API;


public class ImageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.single_image, container, false);
        WeightliftingApp app = (WeightliftingApp) getActivity().getApplicationContext();

        Bundle bundle = this.getArguments();
        String imageURL = bundle.getString(API.IMAGE_URL);

        ImageView imageView = (ImageView) fragment.findViewById(R.id.image);
        app.getImageLoader().displayImage(imageURL, imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        return fragment;
    }
}