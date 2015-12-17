package de.weightlifting.app.helper.UniversalImageLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.WeightliftingApp;


/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */

/*Modification:
- only show ImageListFragment or ImageGalleryFragment
- imported functionality in Fragment
*/

public class ImageControllerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeightliftingApp app = (WeightliftingApp) getActivity().getApplicationContext();

        Bundle bundle = this.getArguments();
        int frIndex = bundle.getInt("FRAGMENT_INDEX");
        Fragment fr;
        String tag;
        switch (frIndex) {
            default:
            case ImageGridFragment.INDEX:
                //Hands over GALLERY_POSITION
                tag = ImageGridFragment.class.getSimpleName();
                fr = getActivity().getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImageGridFragment();
                    fr.setArguments(bundle);
                }
                break;
            case ImagePagerFragment.INDEX:
                //Hands over GALLERY_POSITION, IMAGE_POSITION
                tag = ImagePagerFragment.class.getSimpleName();
                fr = getActivity().getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImagePagerFragment();
                    fr.setArguments(bundle);
                }
                break;
        }
        ((MainActivity) getActivity()).addFragment(fr, tag, true);

        return null;
    }

}