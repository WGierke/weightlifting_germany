package de.weightlifting.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.weightlifting.app.buli.relay1A.CompetitionsFragment1A;
import de.weightlifting.app.buli.relay1A.TableFragment1A;

public class BuliFragment extends Fragment {

    protected static final int FRAGMENT_COMPETITIONS = 0;
    protected static final int FRAGMENT_TABLE = 1;
}
