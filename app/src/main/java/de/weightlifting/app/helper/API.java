package de.weightlifting.app.helper;

public class API {
    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_NEWS = 2;
    public static final int FRAGMENT_BULI_1A = 4;
    public static final int FRAGMENT_BULI_1B = 5;
    public static final int FRAGMENT_BULI_2NORTH = 7;
    public static final int FRAGMENT_BULI_2MIDDLE = 8;
    public static final int FRAGMENT_BULI_2SOUTH = 9;
    public static final int FRAGMENT_ARCHIVE = 11;
    public static final int FRAGMENT_FAQ = 13;
    public static final int FRAGMENT_CONTACT = 15;

    public final static String ITEM = "item";
    public final static String SEASON_ITEM_POSITION = "seasonItem";
    public final static String RELAY_ITEM_POSITION = "relayItem";
    public final static String PROTOCOL_URL = "protocolUrl";
    public final static String COMPETITION_PARTIES = "competitionParties";
    public final static String CLUB_NAME = "clubName";
    public final static String IMAGE_URL = "imageUrl";

    //TITLE#TEXT#DESCRIPTION#FRAGMENT_ID#FRAGMENT_SUB_ID
    //Neue Tabellenergebnisse#1. Potsdam|2.Berlin#2. Bundesliga - Staffel Nordost#5#2
    public static final String FILTER_MODE_KEY = "FILTER_MODE";
    public static final String FILTER_MODE_NONE = "FILER_NONE";
    public static final String FILTER_MODE_RELAY = "FILTER_RELAY";
    public static final String FILTER_MODE_CLUB = "FILTER_CLUB";

    public static final String FILTER_TEXT_KEY = "FILTER_TEXT_KEY";

    public static final String PREFERENCE_USER_ID = "PREFERENCE_USER_ID";

    public static final String HANDLER_RESULT_KEY = "RESULT_KEY";

    //HTTP Post Param<-> Server
    //Token:    token - value
    //Filter:   userId - user_id, filterSetting - filter_setting,   createdAt - timestamp
    //Protocol: competitionParties - competition_parties,           createdAt - timestamp
}
