package de.weightlifting.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.buli.relay1A.BuliFragment1A;
import de.weightlifting.app.buli.relay1B.BuliFragment1B;
import de.weightlifting.app.buli.relay2A.BuliFragment2A;
import de.weightlifting.app.buli.relay2B.BuliFragment2B;
import de.weightlifting.app.buli.relay2C.BuliFragment2C;
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.UiHelper;
import de.weightlifting.app.news.News;
import de.weightlifting.app.news.NewsFeedFragment;
import de.weightlifting.app.service.RegistrationIntentService;

public class MainActivity extends AppCompatActivity {

    private WeightliftingApp app;
    private Toolbar mToolbar;
    private CharSequence mTitle;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Can show an indeterminate progress circle in the action bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        app = (WeightliftingApp) getApplicationContext();

        mTracker = app.getDefaultTracker();

        initNavigation(savedInstanceState);

        showFragmentFromBundle();
    }

    private void showFragmentFromBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int fragmentId = extras.getInt("fragmentId");
            if (fragmentId != 0) {
                showFragment(fragmentId);
            }
        }
    }

    private void initNavigation(Bundle savedInstanceState) {
        PrimaryDrawerItem nav_home = new PrimaryDrawerItem().withName(R.string.nav_home).withIcon(R.drawable.nav_home);
        PrimaryDrawerItem nav_news = new PrimaryDrawerItem().withName(R.string.nav_news).withIcon(R.drawable.nav_news);
        SectionDrawerItem nav_buli1_section = new SectionDrawerItem().withName(R.string.firstNationalLeague);
        PrimaryDrawerItem nav_buli_1A = new PrimaryDrawerItem().withName(R.string.nav_buli_1A).withIcon(R.drawable.nav_buli);
        PrimaryDrawerItem nav_buli_1B = new PrimaryDrawerItem().withName(R.string.nav_buli_1B).withIcon(R.drawable.nav_buli);
        SectionDrawerItem nav_buli2_section = new SectionDrawerItem().withName(R.string.secondNationalLeague);
        PrimaryDrawerItem nav_buli_2A = new PrimaryDrawerItem().withName(R.string.nav_buli_2A).withIcon(R.drawable.nav_buli);
        PrimaryDrawerItem nav_buli_2B = new PrimaryDrawerItem().withName(R.string.nav_buli_2B).withIcon(R.drawable.nav_buli);
        PrimaryDrawerItem nav_buli_2C = new PrimaryDrawerItem().withName(R.string.nav_buli_2C).withIcon(R.drawable.nav_buli);
        PrimaryDrawerItem nav_archive = new PrimaryDrawerItem().withName(R.string.nav_archive).withIcon(R.drawable.nav_archive);
        PrimaryDrawerItem nav_faq = new PrimaryDrawerItem().withName(R.string.nav_faq).withIcon(R.drawable.nav_faq);
        PrimaryDrawerItem nav_info = new PrimaryDrawerItem().withName(R.string.nav_info).withIcon(R.drawable.nav_info);

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        nav_home,
                        new DividerDrawerItem(),
                        nav_news,
                        nav_buli1_section,
                        nav_buli_1A,
                        nav_buli_1B,
                        nav_buli2_section,
                        nav_buli_2A,
                        nav_buli_2B,
                        nav_buli_2C,
                        new DividerDrawerItem(),
                        nav_archive,
                        new DividerDrawerItem(),
                        nav_faq,
                        new DividerDrawerItem(),
                        nav_info,
                        new DividerDrawerItem()
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        showFragment(position);
                        return false;
                    }
                })
                .build();

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            showFragment(API.FRAGMENT_HOME);
            setTitle(R.string.app_name);
        }

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void showAsyncUpdateResults() {
        switch (app.getUpdateStatus()) {
            case WeightliftingApp.UPDATE_STATUS_PENDING:
                Runnable refreshRunnable = new Runnable() {
                    @Override
                    public void run() {
                        showAsyncUpdateResults();
                    }
                };
                Handler refreshHandler = new Handler();
                refreshHandler.postDelayed(refreshRunnable, 200);
                return;
            case WeightliftingApp.UPDATE_STATUS_SUCCESSFUL:
                showCountedNewElements(true);
                break;
            case WeightliftingApp.UPDATE_STATUS_FAILED:
                showCountedNewElements(false);
                break;
        }
    }

    private void showCountedNewElements(boolean updatedSuccessfully) {
        int newElements = Competitions.itemsToMark.size() + Table.itemsToMark.size() + News.newArticleUrlsToMark.size();
        if (updatedSuccessfully)
            UiHelper.showToast(getResources().getString(R.string.updated_all_successfully), getApplicationContext());
        else
            UiHelper.showToast(getResources().getString(R.string.updated_all_unsuccessfully), getApplicationContext());
        UiHelper.showToast(getResources().getQuantityString(R.plurals.new_elements, newElements, newElements), getApplicationContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (WeightliftingApp.isUpdatingAll) {
                    UiHelper.showToast(getResources().getString(R.string.updating_in_progress), getApplicationContext());
                } else {
                    WeightliftingApp.isUpdatingAll = true;
                    app.setFinishUpdateFlags(false);
                    try {
                        app.updateDataForcefully();
                        app.updateNewsForcefully();
                        showAsyncUpdateResults();
                    } catch (Exception e) {
                        e.printStackTrace();
                        UiHelper.showToast(getResources().getString(R.string.updated_all_unsuccessfully), getApplicationContext());
                    }
                }
                return true;
            case R.id.action_settings:
                addFragment(new SettingsFragment(), getString(R.string.settings), true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void showFragment(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        switch (position) {
            case API.FRAGMENT_HOME:
                fragment = new HomeFragment();
                setTitle(getString(R.string.nav_home));
                break;
            case API.FRAGMENT_NEWS:
                fragment = new NewsFeedFragment();
                setTitle(getString(R.string.nav_news));
                break;
            case API.FRAGMENT_BULI_1A:
                fragment = new BuliFragment1A();
                setTitle(getString(R.string.buli_1A));
                break;
            case API.FRAGMENT_BULI_1B:
                fragment = new BuliFragment1B();
                setTitle(getString(R.string.buli_1B));
                break;
            case API.FRAGMENT_BULI_2A:
                fragment = new BuliFragment2B();
                setTitle(getString(R.string.buli_2A));
                break;
            case API.FRAGMENT_BULI_2C:
                fragment = new BuliFragment2C();
                setTitle(getString(R.string.buli_2C));
                break;
            case API.FRAGMENT_BULI_2B:
                fragment = new BuliFragment2A();
                setTitle(getString(R.string.buli_2B));
                break;
            case API.FRAGMENT_ARCHIVE:
                fragment = new ArchiveFragment();
                setTitle(getString(R.string.nav_archive));
                break;
            case API.FRAGMENT_FAQ:
                fragment = new FaqFragment();
                setTitle(getString(R.string.nav_faq));
                break;
            case API.FRAGMENT_INFO:
                fragment = new InfoFragment();
                setTitle(getString(R.string.nav_info));
                break;
            default:
                break;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int fragmentId = extras.getInt(API.NOTIFICATION_FRAGMENT_ID);
            int subFragmentId = extras.getInt(API.NOTIFICATION_SUBFRAGMENT_ID);
            if (fragmentId == position && subFragmentId != 0) {
                fragment.setArguments(extras);
                getIntent().removeExtra(API.NOTIFICATION_FRAGMENT_ID);
                getIntent().removeExtra(API.NOTIFICATION_SUBFRAGMENT_ID);
            }
        }

        mTracker.setScreenName((String) mTitle);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        replaceFragment(fragment, mTitle.toString());

        invalidateOptionsMenu();
    }

    public void replaceFragment(Fragment fragment, String title) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.frame_container, fragment, title);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
            setTitle(title);
            fragmentManager.popBackStack();
        } else {
            Log.e(WeightliftingApp.TAG, "Fragment is null");
        }
    }

    public void addFragment(Fragment fragment, String title, Boolean backStack) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.frame_container, fragment, title);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            if (backStack)
                transaction.addToBackStack(title);

            transaction.commit();

            setTitle(title);
        } else {
            Log.e(WeightliftingApp.TAG, "Fragment is null");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        showFragmentFromBundle();
    }
}
