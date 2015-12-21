package de.weightlifting.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import de.weightlifting.app.buli.relay1A.Competitions1A;
import de.weightlifting.app.buli.relay1A.Schedule1A;
import de.weightlifting.app.buli.relay1A.Table1A;
import de.weightlifting.app.buli.relay1B.Competitions1B;
import de.weightlifting.app.buli.relay1B.Schedule1B;
import de.weightlifting.app.buli.relay1B.Table1B;
import de.weightlifting.app.buli.relay2Middle.Competitions2Middle;
import de.weightlifting.app.buli.relay2Middle.Schedule2Middle;
import de.weightlifting.app.buli.relay2Middle.Table2Middle;
import de.weightlifting.app.buli.relay2North.Competitions2North;
import de.weightlifting.app.buli.relay2North.Schedule2North;
import de.weightlifting.app.buli.relay2North.Table2North;
import de.weightlifting.app.buli.relay2South.Competitions2South;
import de.weightlifting.app.buli.relay2South.Schedule2South;
import de.weightlifting.app.buli.relay2South.Table2South;
import de.weightlifting.app.faq.FaqItem;
import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.ImageLoader;
import de.weightlifting.app.helper.MemoryCache;

public class WeightliftingApp extends Application {

    public static final String TAG = "WeightliftingLog";
    public final static int UPDATE_STATUS_SUCCESSFUL = 200;
    public final static int UPDATE_STATUS_FAILED = 201;
    public final static int UPDATE_STATUS_PENDING = 202;
    public final static int UPDATE_FORCEFULLY = 1;
    public final static int UPDATE_IF_NECESSARY = 2;
    public final static int LOAD_FROM_FILE = 3;
    public static boolean isUpdatingAll = false;
    private static Context mContext;
    public boolean initializedParse = false;
    public MemoryCache memoryCache;
    public ImageLoader imageLoader;
    public Schedule1A schedule1A;
    public Competitions1A competitions1A;
    public Table1A table1A;
    public Schedule1B schedule1B;
    public Competitions1B competitions1B;
    public Table1B table1B;
    public Schedule2North schedule2North;
    public Competitions2North competitions2North;
    public Table2North table2North;
    public Schedule2South schedule2South;
    public Competitions2South competitions2South;
    public Table2South table2South;
    public Schedule2Middle schedule2Middle;
    public Competitions2Middle competitions2Middle;
    public Table2Middle table2Middle;
    public Handler splashCallbackHandler;
    private String filterMode;
    private String filterText;

    public static Context getContext() {
        if (mContext == null)
            mContext = getContext();
        return mContext;
    }

    public void initialize(Handler callbackHandler) {
        splashCallbackHandler = callbackHandler;
        DataHelper.sendMessage(splashCallbackHandler, SplashActivity.KEY_MESSAGE, getString(R.string.loading_data));

        Log.i(TAG, "Initializing...");

        memoryCache = new MemoryCache();
        imageLoader = new ImageLoader(getApplicationContext());

        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.faq_off_signal_heading), getString(R.string.faq_off_signal_question), getString(R.string.faq_off_signal_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.faq_bad_attempt_jerking_heading), getString(R.string.faq_bad_attempt_jerking_question), getString(R.string.faq_bad_attempt_jerking_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.winner_single_competition_heading), getString(R.string.winner_single_competition_question), getString(R.string.winner_single_competition_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.winner_team_competition_heading), getString(R.string.winner_team_competition_question), getString(R.string.winner_team_competition_answer)));

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
        loadSettings();
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

    private void loadSettings() {
        filterMode = DataHelper.getPreference(API.FILTER_MODE_KEY, this);
        filterText = DataHelper.getPreference(API.FILTER_TEXT_KEY, this);
    }

    public void loadDataFromStorage() {
        getSchedule1A(LOAD_FROM_FILE);
        getCompetitions1A(LOAD_FROM_FILE);
        getTable1A(LOAD_FROM_FILE);
        getSchedule1B(LOAD_FROM_FILE);
        getCompetitions1B(LOAD_FROM_FILE);
        getTable1B(LOAD_FROM_FILE);
        getSchedule2North(LOAD_FROM_FILE);
        getCompetitions2North(LOAD_FROM_FILE);
        getTable2North(LOAD_FROM_FILE);
        getSchedule2South(LOAD_FROM_FILE);
        getCompetitions2South(LOAD_FROM_FILE);
        getTable2South(LOAD_FROM_FILE);
        getSchedule2Middle(LOAD_FROM_FILE);
        getCompetitions2Middle(LOAD_FROM_FILE);
        getTable2Middle(LOAD_FROM_FILE);
    }

    public void updateDataForcefully() {
        //Update everything and save it on storage
        getSchedule1A(UPDATE_FORCEFULLY);
        getCompetitions1A(UPDATE_FORCEFULLY);
        getTable1A(UPDATE_FORCEFULLY);
        getSchedule1B(UPDATE_FORCEFULLY);
        getCompetitions1B(UPDATE_FORCEFULLY);
        getTable1B(UPDATE_FORCEFULLY);
        getSchedule2North(UPDATE_FORCEFULLY);
        getCompetitions2North(UPDATE_FORCEFULLY);
        getTable2North(UPDATE_FORCEFULLY);
        getSchedule2South(UPDATE_FORCEFULLY);
        getCompetitions2South(UPDATE_FORCEFULLY);
        getTable2South(UPDATE_FORCEFULLY);
        getSchedule2Middle(UPDATE_FORCEFULLY);
        getCompetitions2Middle(UPDATE_FORCEFULLY);
        getTable2Middle(UPDATE_FORCEFULLY);
    }

    public int getUpdateStatus() {
        if (schedule1A.updateFailed || competitions1A.updateFailed || table1A.updateFailed ||
                schedule1B.updateFailed || competitions1B.updateFailed || table1B.updateFailed ||
                schedule2North.updateFailed || competitions2North.updateFailed || table2North.updateFailed ||
                schedule2South.updateFailed || competitions2South.updateFailed || table2South.updateFailed ||
                schedule2Middle.updateFailed || competitions2Middle.updateFailed || table2Middle.updateFailed) {

            isUpdatingAll = false;
            return UPDATE_STATUS_FAILED;
        }
        if (schedule1A.isUpToDate && competitions1A.isUpToDate && table1A.isUpToDate &&
                schedule1B.isUpToDate && competitions1B.isUpToDate && table1B.isUpToDate &&
                schedule2North.isUpToDate && competitions2North.isUpToDate && table2North.isUpToDate &&
                schedule2South.isUpToDate && competitions2South.isUpToDate && table2South.isUpToDate &&
                schedule2Middle.isUpToDate && competitions2Middle.isUpToDate && table2Middle.isUpToDate) {

            isUpdatingAll = false;
            return UPDATE_STATUS_SUCCESSFUL;
        } else
            return UPDATE_STATUS_PENDING;
    }

    public void setFinishUpdateFlags(boolean value) {
        if (schedule1A != null)
            schedule1A.isUpToDate = value;
        if (competitions1A != null)
            competitions1A.isUpToDate = value;
        if (table1A != null)
            table1A.isUpToDate = value;
        if (schedule1B != null)
            schedule1B.isUpToDate = value;
        if (competitions1B != null)
            competitions1B.isUpToDate = value;
        if (table1B != null)
            table1B.isUpToDate = value;
        if (schedule2North != null)
            schedule2North.isUpToDate = value;
        if (competitions2North != null)
            competitions2North.isUpToDate = value;
        if (table2North != null)
            table2North.isUpToDate = value;
        if (schedule2South != null)
            schedule2South.isUpToDate = value;
        if (competitions2South != null)
            competitions2South.isUpToDate = value;
        if (table2South != null)
            table2South.isUpToDate = value;
        if (schedule2Middle != null)
            schedule2Middle.isUpToDate = value;
        if (competitions2Middle != null)
            competitions2Middle.isUpToDate = value;
        if (table2Middle != null)
            table2Middle.isUpToDate = value;
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
            e.printStackTrace();
        }
        return myInstance;
    }

    public Schedule1A getSchedule1A(int updateMode) {
        schedule1A = (Schedule1A) getWrapperItems(schedule1A, Schedule1A.class, updateMode);
        return schedule1A;
    }

    public Competitions1A getCompetitions1A(int updateMode) {
        competitions1A = (Competitions1A) getWrapperItems(competitions1A, Competitions1A.class, updateMode);
        return competitions1A;
    }

    public Table1A getTable1A(int updateMode) {
        table1A = (Table1A) getWrapperItems(table1A, Table1A.class, updateMode);
        return table1A;
    }

    public Schedule1B getSchedule1B(int updateMode) {
        schedule1B = (Schedule1B) getWrapperItems(schedule1B, Schedule1B.class, updateMode);
        return schedule1B;
    }

    public Competitions1B getCompetitions1B(int updateMode) {
        competitions1B = (Competitions1B) getWrapperItems(competitions1B, Competitions1B.class, updateMode);
        return competitions1B;
    }

    public Table1B getTable1B(int updateMode) {
        table1B = (Table1B) getWrapperItems(table1B, Table1B.class, updateMode);
        return table1B;
    }

    public Schedule2North getSchedule2North(int updateMode) {
        schedule2North = (Schedule2North) getWrapperItems(schedule2North, Schedule2North.class, updateMode);
        return schedule2North;
    }

    public Competitions2North getCompetitions2North(int updateMode) {
        competitions2North = (Competitions2North) getWrapperItems(competitions2North, Competitions2North.class, updateMode);
        return competitions2North;
    }

    public Table2North getTable2North(int updateMode) {
        table2North = (Table2North) getWrapperItems(table2North, Table2North.class, updateMode);
        return table2North;
    }

    public Schedule2South getSchedule2South(int updateMode) {
        schedule2South = (Schedule2South) getWrapperItems(schedule2South, Schedule2South.class, updateMode);
        return schedule2South;
    }

    public Competitions2South getCompetitions2South(int updateMode) {
        competitions2South = (Competitions2South) getWrapperItems(competitions2South, Competitions2South.class, updateMode);
        return competitions2South;
    }

    public Table2South getTable2South(int updateMode) {
        table2South = (Table2South) getWrapperItems(table2South, Table2South.class, updateMode);
        return table2South;
    }

    public Schedule2Middle getSchedule2Middle(int updateMode) {
        schedule2Middle = (Schedule2Middle) getWrapperItems(schedule2Middle, Schedule2Middle.class, updateMode);
        return schedule2Middle;
    }

    public Competitions2Middle getCompetitions2Middle(int updateMode) {
        competitions2Middle = (Competitions2Middle) getWrapperItems(competitions2Middle, Competitions2Middle.class, updateMode);
        return competitions2Middle;
    }

    public Table2Middle getTable2Middle(int updateMode) {
        table2Middle = (Table2Middle) getWrapperItems(table2Middle, Table2Middle.class, updateMode);
        return table2Middle;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null)
            imageLoader = new ImageLoader(getApplicationContext());
        return imageLoader;
    }

    public String getFilterMode() {
        if (filterMode == null)
            filterMode = DataHelper.getPreference(API.FILTER_MODE_KEY, this);
        return filterMode;
    }

    public String getFilterText() {
        if (filterText == null)
            filterText = DataHelper.getPreference(API.FILTER_TEXT_KEY, this);
        return filterText;
    }

    public void refreshFilterSettings() {
        filterMode = DataHelper.getPreference(API.FILTER_MODE_KEY, this);
        filterText = DataHelper.getPreference(API.FILTER_TEXT_KEY, this);
    }
}
