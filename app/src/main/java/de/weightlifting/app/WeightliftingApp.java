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
import de.weightlifting.app.buli.relay2A.Competitions2A;
import de.weightlifting.app.buli.relay2A.Schedule2A;
import de.weightlifting.app.buli.relay2A.Table2A;
import de.weightlifting.app.buli.relay2B.Competitions2B;
import de.weightlifting.app.buli.relay2B.Schedule2B;
import de.weightlifting.app.buli.relay2B.Table2B;
import de.weightlifting.app.buli.relay2C.Competitions2C;
import de.weightlifting.app.buli.relay2C.Schedule2C;
import de.weightlifting.app.buli.relay2C.Table2C;
import de.weightlifting.app.faq.FaqItem;
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.DataHelper;
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
    public static boolean initializedNews = false;
    private static Context mContext;
    public MemoryCache memoryCache;
    public ImageLoader imageLoader;
    public News news;
    public Schedule1A schedule1A;
    public Competitions1A competitions1A;
    public Table1A table1A;
    public Schedule1B schedule1B;
    public Competitions1B competitions1B;
    public Table1B table1B;
    public Schedule2B schedule2B;
    public Competitions2B competitions2B;
    public Table2B table2B;
    public Schedule2C schedule2C;
    public Competitions2C competitions2C;
    public Table2C table2C;
    public Schedule2A schedule2A;
    public Competitions2A competitions2A;
    public Table2A table2A;
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
            allBlogPublishers.add("Mutterstadt");
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
        getSchedule2B(LOAD_FROM_FILE);
        getCompetitions2B(LOAD_FROM_FILE);
        getTable2B(LOAD_FROM_FILE);
        getSchedule2C(LOAD_FROM_FILE);
        getCompetitions2C(LOAD_FROM_FILE);
        getTable2C(LOAD_FROM_FILE);
        getSchedule2A(LOAD_FROM_FILE);
        getCompetitions2A(LOAD_FROM_FILE);
        getTable2A(LOAD_FROM_FILE);
    }

    public void updateDataForcefully() {
        //Update everything and save it on storage
        getSchedule1A(UPDATE_FORCEFULLY);
        getCompetitions1A(UPDATE_FORCEFULLY);
        getTable1A(UPDATE_FORCEFULLY);
        getSchedule1B(UPDATE_FORCEFULLY);
        getCompetitions1B(UPDATE_FORCEFULLY);
        getTable1B(UPDATE_FORCEFULLY);
        getSchedule2B(UPDATE_FORCEFULLY);
        getCompetitions2B(UPDATE_FORCEFULLY);
        getTable2B(UPDATE_FORCEFULLY);
        getSchedule2C(UPDATE_FORCEFULLY);
        getCompetitions2C(UPDATE_FORCEFULLY);
        getTable2C(UPDATE_FORCEFULLY);
        getSchedule2A(UPDATE_FORCEFULLY);
        getCompetitions2A(UPDATE_FORCEFULLY);
        getTable2A(UPDATE_FORCEFULLY);
    }

    public int getUpdateStatus() {
        if (schedule1A.updateFailed || competitions1A.updateFailed || table1A.updateFailed ||
                schedule1B.updateFailed || competitions1B.updateFailed || table1B.updateFailed ||
                schedule2B.updateFailed || competitions2B.updateFailed || table2B.updateFailed ||
                schedule2C.updateFailed || competitions2C.updateFailed || table2C.updateFailed ||
                schedule2A.updateFailed || competitions2A.updateFailed || table2A.updateFailed || News.updateFailed) {

            isUpdatingAll = false;
            return UPDATE_STATUS_FAILED;
        }
        if (schedule1A.isUpToDate && competitions1A.isUpToDate && table1A.isUpToDate &&
                schedule1B.isUpToDate && competitions1B.isUpToDate && table1B.isUpToDate &&
                schedule2B.isUpToDate && competitions2B.isUpToDate && table2B.isUpToDate &&
                schedule2C.isUpToDate && competitions2C.isUpToDate && table2C.isUpToDate &&
                schedule2A.isUpToDate && competitions2A.isUpToDate && table2A.isUpToDate && !News.isUpdating) {

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
        if (schedule2B != null)
            schedule2B.isUpToDate = value;
        if (competitions2B != null)
            competitions2B.isUpToDate = value;
        if (table2B != null)
            table2B.isUpToDate = value;
        if (schedule2C != null)
            schedule2C.isUpToDate = value;
        if (competitions2C != null)
            competitions2C.isUpToDate = value;
        if (table2C != null)
            table2C.isUpToDate = value;
        if (schedule2A != null)
            schedule2A.isUpToDate = value;
        if (competitions2A != null)
            competitions2A.isUpToDate = value;
        if (table2A != null)
            table2A.isUpToDate = value;
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
                String fileName = (String) myClass.getDeclaredMethod("getFileName").invoke(myInstance);
                File file = getApplicationContext().getFileStreamPath(fileName);
                if (file.exists()) {
                    String fileContent = DataHelper.readIntern(fileName, getApplicationContext());
                    if (!fileContent.equals("")) {
                        myInstance.parseFromString(fileContent);
                        myInstance.setLastUpdate(new File(getFilesDir() + "/" + fileName).lastModified());
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
        try {
            if (news == null)
                initNews();

            if (updateMode == UPDATE_FORCEFULLY) {
                news.refreshArticleUrlsForPublishers();
                return news;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Schedule2A getSchedule2A(int updateMode) {
        schedule2A = (Schedule2A) getWrapperItems(schedule2A, Schedule2A.class, updateMode);
        return schedule2A;
    }

    public Competitions2A getCompetitions2A(int updateMode) {
        competitions2A = (Competitions2A) getWrapperItems(competitions2A, Competitions2A.class, updateMode);
        return competitions2A;
    }

    public Table2A getTable2A(int updateMode) {
        table2A = (Table2A) getWrapperItems(table2A, Table2A.class, updateMode);
        return table2A;
    }

    public Schedule2B getSchedule2B(int updateMode) {
        schedule2B = (Schedule2B) getWrapperItems(schedule2B, Schedule2B.class, updateMode);
        return schedule2B;
    }

    public Competitions2B getCompetitions2B(int updateMode) {
        competitions2B = (Competitions2B) getWrapperItems(competitions2B, Competitions2B.class, updateMode);
        return competitions2B;
    }

    public Table2B getTable2B(int updateMode) {
        table2B = (Table2B) getWrapperItems(table2B, Table2B.class, updateMode);
        return table2B;
    }

    public Schedule2C getSchedule2C(int updateMode) {
        schedule2C = (Schedule2C) getWrapperItems(schedule2C, Schedule2C.class, updateMode);
        return schedule2C;
    }

    public Competitions2C getCompetitions2C(int updateMode) {
        competitions2C = (Competitions2C) getWrapperItems(competitions2C, Competitions2C.class, updateMode);
        return competitions2C;
    }

    public Table2C getTable2C(int updateMode) {
        table2C = (Table2C) getWrapperItems(table2C, Table2C.class, updateMode);
        return table2C;
    }

    public ArrayList<ScheduleEntry> getFilteredScheduledCompetitions() {
        ArrayList<ScheduleEntry> result = new ArrayList<>();

        Schedule1A schedule1A = getSchedule1A(UPDATE_IF_NECESSARY);
        Schedule1B schedule1B = getSchedule1B(UPDATE_IF_NECESSARY);
        Schedule2B schedule2B = getSchedule2B(UPDATE_IF_NECESSARY);
        Schedule2C schedule2C = getSchedule2C(UPDATE_IF_NECESSARY);
        Schedule2A schedule2A = getSchedule2A(UPDATE_IF_NECESSARY);

        ArrayList<Schedule> schedules = new ArrayList<>();
        schedules.add(schedule1A);
        schedules.add(schedule1B);
        schedules.add(schedule2B);
        schedules.add(schedule2C);
        schedules.add(schedule2A);

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
                DataHelper.setPreference(API.BLOG_FILTER_MODE_KEY, blogFilterMode, this);
            }
        }
        return blogFilterMode;
    }

    public void setBlogFilterMode(String blogFilterMode) {
        this.blogFilterMode = blogFilterMode;
    }

    private void tryLoadingBlogFilterPublishersFromJson(String json) {
        try {
            blogFilterPublishers = new Gson().fromJson(json, new TypeToken<ArrayList<String>>() {
            }.getType());
        } catch (Exception ignored) {
        }
        if (blogFilterPublishers == null) {
            blogFilterPublishers = allBlogPublishers;
        }
    }

    public ArrayList<String> getBlogFilterPublishers() {
        if (blogFilterPublishers == null) {
            String json = DataHelper.getPreference(API.BLOG_FILTER_TEXT_KEY, this);
            tryLoadingBlogFilterPublishersFromJson(json);
        }
        return blogFilterPublishers;
    }

    public void setBlogFilterPublishers(ArrayList<String> blogFilterPublishers) {
        this.blogFilterPublishers = blogFilterPublishers;
    }

    public void refreshBuliFilterSettings() {
        buliFilterMode = DataHelper.getPreference(API.BULI_FILTER_MODE_KEY, this);
        buliFilterText = DataHelper.getPreference(API.BULI_FILTER_TEXT_KEY, this);
    }

    public String getUserId() {
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
        return userID;
    }

    public void saveBuliFilterOnline() {
        try {
            String userID = getUserId();

            String filterSetting;
            if (buliFilterMode.equals(API.BULI_FILTER_MODE_NONE))
                filterSetting = "all";
            else
                filterSetting = buliFilterText;

            Answers.getInstance().logCustom(new CustomEvent("Filter Saving")
                    .putCustomAttribute("Setting", filterSetting));

            NetworkHelper.sendFilter(userID, filterSetting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBlogFilterOnline() {
        try {
            String userID = getUserId();

            String filterSetting;
            switch (blogFilterMode) {
                case API.BLOG_FILTER_SHOW_NONE:
                    filterSetting = "none";
                    break;
                case API.BLOG_FILTER_SHOW_CHOSEN:
                    filterSetting = new Gson().toJson(blogFilterPublishers);
                    break;
                default:
                    filterSetting = "all";
                    break;
            }

            Answers.getInstance().logCustom(new CustomEvent("Blog Filter Saving")
                    .putCustomAttribute("BlogSetting", filterSetting));

            NetworkHelper.sendBlogFilter(userID, filterSetting);
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

    public void updateNewsForcefully() {
        getNews(WeightliftingApp.UPDATE_FORCEFULLY);
        News.isUpdating = true;
        News.updateFailed = false;
    }
}
