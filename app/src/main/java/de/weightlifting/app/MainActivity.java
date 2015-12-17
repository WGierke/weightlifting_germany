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

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.helper.UiHelper;
import de.weightlifting.app.service.RegistrationIntentService;

public class MainActivity extends AppCompatActivity {

    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_NEWS = 1;
    public static final int FRAGMENT_BULI = 2;
    public static final int FRAGMENT_FAQ = 4;
    public static final int FRAGMENT_CONTACT = 5;
    private WeightliftingApp app;
    private Toolbar mToolbar;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Can show an indeterminate progress circle in the action bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        app = (WeightliftingApp) getApplicationContext();
        app.setActivity(this);

        initNavigation(savedInstanceState);

        showFragmentFromBundle();
    }

    private void showFragmentFromBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int fragmentId = extras.getInt("fragmentId");
            if (fragmentId != 0) {
                //Log.d(WeightliftingApp.TAG, "Fragment to open: " + fragmentId);
                showFragment(fragmentId);
            }
        }
    }

    private void initNavigation(Bundle savedInstanceState) {

        PrimaryDrawerItem nav_home = new PrimaryDrawerItem().withName(R.string.nav_home).withIcon(R.drawable.nav_home);
        PrimaryDrawerItem nav_buli = new PrimaryDrawerItem().withName(R.string.nav_buli).withIcon(R.drawable.nav_buli);
        PrimaryDrawerItem nav_faq = new PrimaryDrawerItem().withName(R.string.nav_faq).withIcon(R.drawable.nav_help);
        PrimaryDrawerItem nav_contact = new PrimaryDrawerItem().withName(R.string.nav_contact).withIcon(R.drawable.nav_contact);

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        nav_home,
                        new DividerDrawerItem(),
                        nav_buli,
                        new DividerDrawerItem(),
                        nav_faq,
                        new DividerDrawerItem(),
                        nav_contact,
                        new DividerDrawerItem()
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        showFragment(position / 2);
                        return false;
                    }
                })
                .build();

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            showFragment(FRAGMENT_HOME);
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
        int newElements = Competitions.itemsToMark.size() + Table.itemsToMark.size();
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
                        showAsyncUpdateResults();
                    } catch (Exception e) {
                        //Log.d(WeightliftingApp.TAG, "Error while updating all");
                        e.printStackTrace();
                        UiHelper.showToast(getResources().getString(R.string.updated_all_unsuccessfully), getApplicationContext());
                    }
                }
                return true;
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
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                setTitle(getString(R.string.nav_home));
                break;
            case FRAGMENT_BULI:
                fragment = new BuliFragment();
                setTitle(getString(R.string.nav_buli));
                break;
            case FRAGMENT_FAQ:
                fragment = new FaqFragment();
                setTitle(getString(R.string.nav_faq));
                break;
            case FRAGMENT_CONTACT:
                fragment = new ContactFragment();
                setTitle(getString(R.string.nav_contact));
                break;
            default:
                break;
        }

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
        //Log.d(WeightliftingApp.TAG, "resumed");
    }
}
