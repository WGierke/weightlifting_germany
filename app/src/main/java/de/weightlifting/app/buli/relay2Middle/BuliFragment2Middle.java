package de.weightlifting.app.buli.relay2Middle;

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

public class BuliFragment2Middle extends BuliFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.pager_tab_strip, container, false);

        BuliCollectionPagerAdapter mBuliCollectionPagerAdapter = new BuliCollectionPagerAdapter(getActivity().getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) fragment.findViewById(R.id.pager);
        mViewPager.setAdapter(mBuliCollectionPagerAdapter);
        setAdapterItemFromBundle(mViewPager, this);

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
                case FRAGMENT_SCHEDULE:
                    fragment = new ScheduleFragment2Middle();
                    break;
                case FRAGMENT_COMPETITIONS:
                    fragment = new CompetitionsFragment2Middle();
                    break;
                case FRAGMENT_TABLE:
                    fragment = new TableFragment2Middle();
                    break;
                default:
                    fragment = null;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            switch (position) {
                case FRAGMENT_SCHEDULE:
                    title = getString(R.string.buli_schedule);
                    break;
                case FRAGMENT_COMPETITIONS:
                    title = getString(R.string.buli_competitions);
                    break;
                case FRAGMENT_TABLE:
                    title = getString(R.string.buli_table);
                    break;
                default:
                    title = getString(R.string.buli_2Middle);
            }
            return title;
        }
    }
}
