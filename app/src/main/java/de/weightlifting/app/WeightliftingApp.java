package de.weightlifting.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import de.weightlifting.app.buli.Schedule;
import de.weightlifting.app.buli.ScheduleEntry;
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
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.DataHelper;
import de.weightlifting.app.helper.Keys;
import de.weightlifting.app.helper.MemoryCache;
import de.weightlifting.app.helper.NetworkHelper;
import de.weightlifting.app.news.News;

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
    public final String INSTALLATION_FILE = "installation.txt";
    public boolean initializedParse = false;
    public MemoryCache memoryCache;
    public ImageLoader imageLoader;
    public News news;
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
    private String buliFilterMode;
    private String buliFilterText;
    private String blogFilterMode;
    private ArrayList<String> blogFilterPublishers;
    private ArrayList<String> allBlogPublishers = new ArrayList<>();
    private Tracker mTracker;

    public static Context getContext() {
        return mContext;
    }

    public void initialize(Handler callbackHandler) {
        splashCallbackHandler = callbackHandler;
        DataHelper.sendMessage(splashCallbackHandler, SplashActivity.KEY_MESSAGE, getString(R.string.loading_data));

        Log.i(TAG, "Initializing...");

        memoryCache = new MemoryCache();
        imageLoader = ImageLoader.getInstance();

        initNews();
        initFaqs();
        initArchive();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        try {
            Parse.initialize(this, Keys.CONFIG_APP_ID, Keys.CONFIG_CLIENT_KEY);
        } catch (Exception e) {
        }

        mContext = getApplicationContext();

        loadDataFromStorage();
        updateDataForcefully();
        updateSplashScreen();
        loadSettings();
    }

    private void initNews() {
        if (news == null) {
            news = new News();
            allBlogPublishers.add("BVDG");
            allBlogPublishers.add("Speyer");
            allBlogPublishers.add("Schwedt");
            for (String blogPublisher : allBlogPublishers) {
                news.addPublisher(blogPublisher);
            }
            news.addArticleUrlsForPublishers();
        }
    }

    private void initFaqs() {
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.faq_off_signal_heading), getString(R.string.faq_off_signal_question), getString(R.string.faq_off_signal_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.faq_bad_attempt_jerking_heading), getString(R.string.faq_bad_attempt_jerking_question), getString(R.string.faq_bad_attempt_jerking_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.winner_single_competition_heading), getString(R.string.winner_single_competition_question), getString(R.string.winner_single_competition_answer)));
        FaqFragment.faqEntries.add(new FaqItem(getString(R.string.winner_team_competition_heading), getString(R.string.winner_team_competition_question), getString(R.string.winner_team_competition_answer)));
    }

    private void initArchive() {
        ArrayList<String> archivedSeasons = DataHelper.getSeasons();
        Collections.reverse(archivedSeasons);
        ArchiveFragment.archivedSeasonEntries = archivedSeasons;
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
        buliFilterMode = DataHelper.getPreference(API.BULI_FILTER_MODE_KEY, this);
        buliFilterText = DataHelper.getPreference(API.BULI_FILTER_TEXT_KEY, this);
        blogFilterMode = getBlogFilterMode();
        blogFilterPublishers = getBlogFilterPublishers();
    }

    public void loadDataFromStorage() {
        getNews(LOAD_FROM_FILE);
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
        getNews(UPDATE_FORCEFULLY);
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

    public News getNews(int updateMode) {
        initNews();
        return news;
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

    public ArrayList<ScheduleEntry> getFilteredScheduledCompetitions() {
        ArrayList<ScheduleEntry> result = new ArrayList<>();

        Schedule1A schedule1A = getSchedule1A(UPDATE_IF_NECESSARY);
        Schedule1B schedule1B = getSchedule1B(UPDATE_IF_NECESSARY);
        Schedule2North schedule2North = getSchedule2North(UPDATE_IF_NECESSARY);
        Schedule2South schedule2South = getSchedule2South(UPDATE_IF_NECESSARY);
        Schedule2Middle schedule2Middle = getSchedule2Middle(UPDATE_IF_NECESSARY);

        ArrayList<Schedule> schedules = new ArrayList<>();
        schedules.add(schedule1A);
        schedules.add(schedule1B);
        schedules.add(schedule2North);
        schedules.add(schedule2South);
        schedules.add(schedule2Middle);

        switch (buliFilterMode) {
            case API.BULI_FILTER_MODE_RELAY:
                for (int i = 0; i < schedules.size(); i++) {
                    if (schedules.get(i).getRelayName().equals(buliFilterText)) {
                        result.addAll(Schedule.casteArray(schedules.get(i).getItems()));
                        break;
                    }
                }
                break;
            case API.BULI_FILTER_MODE_CLUB:
                for (int i = 0; i < schedules.size(); i++) {
                    ArrayList<ScheduleEntry> currentScheduleEntries = Schedule.casteArray(schedules.get(i).getItems());
                    for (ScheduleEntry s : currentScheduleEntries) {
                        if (s.getHome().contains(buliFilterText) || s.getGuest().contains(buliFilterText)) {
                            result.add(s);
                        }
                    }
                }
                break;
            default:
                for (int i = 0; i < schedules.size(); i++) {
                    result.addAll(Schedule.casteArray(schedules.get(i).getItems()));
                }
                break;
        }

        Collections.sort(result);

        return result;
    }

    public String getBuliFilterMode() {
        if (buliFilterMode == null) {
            buliFilterMode = DataHelper.getPreference(API.BULI_FILTER_MODE_KEY, this);
            if (buliFilterMode == null)
                buliFilterMode = API.BULI_FILTER_MODE_NONE;
        }
        return buliFilterMode;
    }

    public String getBuliFilterText() {
        if (buliFilterText == null)
            buliFilterText = DataHelper.getPreference(API.BULI_FILTER_TEXT_KEY, this);
        return buliFilterText;
    }

    public String getBlogFilterMode() {
        if (blogFilterMode == null) {
            blogFilterMode = DataHelper.getPreference(API.BLOG_FILTER_MODE_KEY, this);
            if (blogFilterMode == null) {
                blogFilterMode = API.BLOG_FILTER_SHOW_ALL;
                blogFilterPublishers = allBlogPublishers;
            }
        }
        return blogFilterMode;
    }

    public ArrayList<String> getBlogFilterPublishers() {
        if (blogFilterPublishers == null) {
            String json = DataHelper.getPreference(API.BLOG_FILTER_TEXT_KEY, this);
            blogFilterPublishers = new Gson().fromJson(json, new TypeToken<ArrayList<String>>() {
            }.getType());
            if (blogFilterPublishers == null) {
                blogFilterPublishers = allBlogPublishers;
            }
            System.out.println(blogFilterPublishers);
        }
        return blogFilterPublishers;
    }

    public void refreshFilterSettings() {
        buliFilterMode = DataHelper.getPreference(API.BULI_FILTER_MODE_KEY, this);
        buliFilterText = DataHelper.getPreference(API.BULI_FILTER_TEXT_KEY, this);

        blogFilterMode = DataHelper.getPreference(API.BLOG_FILTER_MODE_KEY, this);
        String json = DataHelper.getPreference(API.BLOG_FILTER_TEXT_KEY, this);
        blogFilterPublishers = new Gson().fromJson(json, new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public void saveFilterOnline() {
        try {
            String userID;
            if (DataHelper.checkPreference(API.PREFERENCE_USER_ID, this)) {
                userID = DataHelper.getPreference(API.PREFERENCE_USER_ID, this);
                if (userID.length() < 1) {
                    userID = UUID.randomUUID().toString();
                    DataHelper.setPreference(API.PREFERENCE_USER_ID, userID, this);
                }
            } else {
                userID = UUID.randomUUID().toString();
                DataHelper.setPreference(API.PREFERENCE_USER_ID, userID, this);
            }

            ParseObject filterObject = new ParseObject("FilterSetting");
            filterObject.put("userId", userID);
            String filterSetting;
            if (buliFilterMode.equals(API.BULI_FILTER_MODE_NONE))
                filterSetting = "all";
            else
                filterSetting = buliFilterText;
            filterObject.put("filter", filterSetting);
            filterObject.saveInBackground();

            Answers.getInstance().logCustom(new CustomEvent("Filter Saving")
                    .putCustomAttribute("Setting", filterSetting));

            NetworkHelper.sendFilter(userID, filterSetting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null)
            imageLoader = ImageLoader.getInstance();
        return imageLoader;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
