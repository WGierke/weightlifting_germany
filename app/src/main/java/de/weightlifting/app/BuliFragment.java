package de.weightlifting.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

public class BuliFragment extends Fragment {

    protected static final int FRAGMENT_SCHEDULE = 0;
    protected static final int FRAGMENT_COMPETITIONS = 1;
    protected static final int FRAGMENT_TABLE = 2;

    protected void setAdapterItemFromBundle(ViewPager viewPager, BuliFragment fragment) {
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            int subFragmentId = bundle.getInt("subFragmentId");
            if (subFragmentId != 0) {
                viewPager.setCurrentItem(subFragmentId);
            }
            bundle.putInt("subFragmentId", 0);
        }
    }
}
