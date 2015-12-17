package de.weightlifting.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.Date;

import de.weightlifting.app.buli.Competitions;
import de.weightlifting.app.buli.Table;
import de.weightlifting.app.faq.FaqItem;
import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.ImageLoader;
import de.weightlifting.app.helper.MemoryCache;

public class WeightliftingApp extends Application {

    public static final String TAG = "WeightliftingLog";
    public static final String TEAM_NAME = "Oder-Sund-Team";
    public static final int DISPLAY_DELAY = 500;
    public final static int UPDATE_STATUS_SUCCESSFUL = 200;
    public final static int UPDATE_STATUS_FAILED = 201;
    public final static int UPDATE_STATUS_PENDING = 202;
    public final static int UPDATE_FORCEFULLY = 1;
    public final static int UPDATE_IF_NECESSARY = 2;
    public final static int LOAD_FROM_FILE = 3;
    public static Context mContext;
    public static boolean isUpdatingAll = false;
    private static MainActivity mActivity;
    public boolean isInitialized = false;
    public boolean initializedParse = false;
    public MemoryCache memoryCache;
    public ImageLoader imageLoader;
    public Competitions competitions;
    public Table table;
    public Handler splashCallbackHandler;

    public void initialize(Handler callbackHandler) {
        splashCallbackHandler = callbackHandler;
        DataHelper.sendMessage(splashCallbackHandler, SplashActivity.KEY_MESSAGE, getString(R.string.loading_data));

        Log.i(TAG, "Initializing...");
        long dateStart = new Date().getTime();

        memoryCache = new MemoryCache();
        imageLoader = new ImageLoader(getApplicationContext());

        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.faq_off_signal_heading), getString(R.string.faq_off_signal_question), getString(R.string.faq_off_signal_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.faq_bad_attempt_jerking_heading), getString(R.string.faq_bad_attempt_jerking_question), getString(R.string.faq_bad_attempt_jerking_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.winner_single_competition_heading), getString(R.string.winner_single_competition_question), getString(R.string.winner_single_competition_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.winner_team_competition_heading), getString(R.string.winner_team_competition_question), getString(R.string.winner_team_competition_answer)));

        long dateDiff = (new Date().getTime() - dateStart);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

        mContext = getApplicationContext();

        loadDataFromStorage();

        updateDataForcefully();

        updateSplashScreen();

        isInitialized = true;

        Log.i(TAG, "Initialized (" + String.valueOf(dateDiff) + "ms)");
    }

    private void updateSplashScreen() {
        switch (getUpdateStatus()) {
            case UPDATE_STATUS_PENDING:
                Runnable refreshRunnable = new Runnable() {
                    @Override
                    public void run() {
                        updateSplashScreen();
                    }
                };
                //Log.d(TAG, "Update status: pending");
                Handler refreshHandler = new Handler();
                refreshHandler.postDelayed(refreshRunnable, 200);
                break;
            case UPDATE_STATUS_SUCCESSFUL:
                //Log.d(TAG, "Update status: Success");
                DataHelper.sendMessage(splashCallbackHandler, SplashActivity.KEY_MESSAGE, SplashActivity.MESSAGE_INITIALIZED);
                break;
            case UPDATE_STATUS_FAILED:
                //Log.d(TAG, "Update status: Failed");
                DataHelper.sendMessage(splashCallbackHandler, SplashActivity.KEY_STATUS, SplashActivity.STATUS_ERROR_NETWORK);
                break;
        }
    }

    public void loadDataFromStorage() {
        getCompetitions(LOAD_FROM_FILE);
        getTable(LOAD_FROM_FILE);
    }

    public void updateDataForcefully() {
        //Update everything and save it on storage
        //Log.d(TAG, "updating everything");
        getCompetitions(UPDATE_FORCEFULLY);
        getTable(UPDATE_FORCEFULLY);
    }

    public int getUpdateStatus() {
        //Log.d(WeightliftingApp.TAG, news.isUpToDate + " " + events.isUpToDate + " " + team.isUpToDate + " " + competitions.isUpToDate + " " + table.isUpToDate + " " + galleries.isUpToDate);
        if (competitions.updateFailed || table.updateFailed) {
            isUpdatingAll = false;
            return UPDATE_STATUS_FAILED;
        }
        if (competitions.isUpToDate && table.isUpToDate) {
            isUpdatingAll = false;
            return UPDATE_STATUS_SUCCESSFUL;
        } else
            return UPDATE_STATUS_PENDING;
    }

    public void setFinishUpdateFlags(boolean value) {
        if (competitions != null)
            competitions.isUpToDate = value;
        if (table != null)
            table.isUpToDate = value;
    }

    public UpdateableWrapper getWrapperItems(UpdateableWrapper myInstance, Class<?> myClass, int mode) {
        try {
            if (myInstance == null)
                myInstance = (UpdateableWrapper) myClass.newInstance();

            if (mode == UPDATE_FORCEFULLY) {
                //Log.d(TAG, "started forced update for " + myClass.getName());
                myInstance.refreshItems();
                return myInstance;
            }

            if (mode == LOAD_FROM_FILE) {
                String fileName = myClass.getDeclaredField("FILE_NAME").get(myInstance).toString();
                File file = getApplicationContext().getFileStreamPath(fileName);
                if (file.exists()) {
                    String fileContent = DataHelper.readIntern(fileName, getApplicationContext());
                    if (!fileContent.equals("")) {
                        myInstance.parseFromString(fileContent);
                        myInstance.setLastUpdate(new File(getFilesDir() + "/" + fileName).lastModified());
                        //Log.d(TAG, myClass.getName() + ": read from memory:" + fileContent.substring(0, 20) + "...");
                    }
                }
            }

            if (mode == UPDATE_IF_NECESSARY) {
                if (myInstance.needsUpdate() && !myInstance.isUpdating && !isUpdatingAll) {
                    myInstance.refreshItems();
                }
            }
        } catch (Exception e) {
            //Log.d(TAG, "Error in getWrapperItems");
            e.printStackTrace();
        }
        return myInstance;
    }

    public Competitions getCompetitions(int updateMode) {
        competitions = (Competitions) getWrapperItems(competitions, Competitions.class, updateMode);
        return competitions;
    }

    public Table getTable(int updateMode) {
        table = (Table) getWrapperItems(table, Table.class, updateMode);
        return table;
    }

    public void setActivity(MainActivity activity) {
        mActivity = activity;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null)
            imageLoader = new ImageLoader(getApplicationContext());
        return imageLoader;
    }
}
