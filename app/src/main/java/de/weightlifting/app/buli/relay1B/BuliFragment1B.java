package de.weightlifting.app.buli.relay1B;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.weightlifting.app.BuliFragment;
import de.weightlifting.app.R;
import de.weightlifting.app.buli.relay1A.CompetitionsFragment1A;

public class BuliFragment1B extends BuliFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(WeightliftingApp.TAG, "Showing Buli fragment");

        View fragment = inflater.inflate(R.layout.pager_tab_strip, container, false);

        BuliCollectionPagerAdapter mBuliCollectionPagerAdapter = new BuliCollectionPagerAdapter(getActivity().getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) fragment.findViewById(R.id.pager);
        mViewPager.setAdapter(mBuliCollectionPagerAdapter);

        return fragment;
    }

    public class BuliCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public BuliCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            switch (position) {
                case FRAGMENT_COMPETITIONS:
                    fragment = new CompetitionsFragment1B();
                    break;
                case FRAGMENT_TABLE:
                    fragment = new TableFragment1B();
                    break;
                default:
                    fragment = null;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            switch (position) {
                case FRAGMENT_COMPETITIONS:
                    title = getString(R.string.buli_competitions);
                    break;
                case FRAGMENT_TABLE:
                    title = getString(R.string.buli_table);
                    break;
                default:
                    title = getString(R.string.buli_1B);
            }
            return title;
        }
    }
}
