package de.weightlifting.app.archive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.weightlifting.app.R;

public class ArchivedRelayFragment extends Fragment {

    private static final int FRAGMENT_ARCHIVED_COMPETITIONS = 0;
    private static final int FRAGMENT_ARCHIVED_TABLE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.pager_tab_strip, container, false);

        ArchivedRelayPagerAdapter archivedRelayPagerAdapter = new ArchivedRelayPagerAdapter(getActivity().getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) fragment.findViewById(R.id.pager);
        mViewPager.setAdapter(archivedRelayPagerAdapter);

        return fragment;
    }

    public class ArchivedRelayPagerAdapter extends FragmentStatePagerAdapter {
        public ArchivedRelayPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            switch (position) {
                case FRAGMENT_ARCHIVED_COMPETITIONS:
                    fragment = new ArchivedCompetitionsFragment();
                    break;
                case FRAGMENT_ARCHIVED_TABLE:
                    fragment = new ArchivedTableFragment();
                    break;
                default:
                    fragment = null;
                    break;
            }
            fragment.setArguments(getArguments());
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
                case FRAGMENT_ARCHIVED_COMPETITIONS:
                    title = getString(R.string.buli_competitions);
                    break;
                case FRAGMENT_ARCHIVED_TABLE:
                    title = getString(R.string.buli_table);
                    break;
                default:
                    title = getString(R.string.nav_buli);
            }
            return title;
        }
    }
}
