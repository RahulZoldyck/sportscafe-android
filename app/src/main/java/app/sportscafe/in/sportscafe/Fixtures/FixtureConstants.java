package app.sportscafe.in.sportscafe.Fixtures;


public class FixtureConstants {
    public static final String DATA = "data";
    public static final String TEAMS = "teams";
    public static final String GAME_TYPE = "gameType";
    public static final String INDIVIDUALS = "individuals";
    public static final String PLAYER_A = "playerA";
    public static final String COUNTRY = "country";
    public static final String PLAYER_ID = "playerId";
    public static final String PLAYER_NAME_SHORT = "playerNameShort";
    public static final String TEAM_ID = "teamId";
    public static final String TEAM_DISPLAY_NAME_SHORT = "teamDisplayNameShort";
    public static final String MATCHES = "matches";
    public static final String MATCH_REPORT_OR_PREVIEW_LINK = "matchReportOrPreviewLink";
    public static final String MATCH_STATUS = "matchStatus";
    public static final String TOURNAMENT_SUPER_NAME = "tournamentSuperName";
    public static final String MATCH_DATE = "matchDate";
    public static final String TOURNAMENT_VENUE = "tournamentVenue";
    public static final String CITY = "city";
    public static final String TOURNAMENT_NAME = "tournamentName";
    public static final String MATCH_START_DATE = "matchStartDate";
    public static final String MATCH_ID = "matchId";
    public static final String MATCH_VENUE = "matchVenue";
    public static final String TEAM_IDS = "teamIds";
    public static final String TOURNAMENT_ID = "tournamentId";
    public static final String MATCH_RESULT = "matchResult";
    public static final String MATCH_FINAL_SCORE = "matchFinalScore";
    public static final String[] GAMES = {"football", "hockey", "cricket", "badminton", "tennis", "wrestling"};
    public static final String FOOTBALL = "football";
    public static final String BADMINTON = "badminton";
    public static final String TENNIS = "tennis";
    public static final String WRESTLING = "wrestling";
    public static final String CRICKET = "cricket";
    public static final String HOCKEY = "hockey";


    public static String getQuery(String ISOyes, String ISOtomo) {
        String rawjson = "{\"msg\": {" +
                "\"$redact\": {" +
                "\"$cond\": {" +
                "\"if\": {" +
                "\"$or\": [{" +
                "\"$and\": [{" +
                "\"$gte\": [\"" + ISOyes + "\", \"$tournamentStartDate\"]" +
                "}, {" +
                "\"$lt\": [\"" + ISOtomo + "\", \"$tournamentEndDate\"]" +
                "}]" +
                " }, {" +
                "\"$and\": [{" +
                "\"$gte\": [\"$matchStartDate\", \"" + ISOyes + "\"]" +
                "}, {" +
                "\"$lt\": [\"$matchStartDate\", \"" + ISOtomo + "\"]" +
                "}]" +
                " }, {" +
                "\"$and\": [{" +
                "\"$not\": {" +
                "\"$ifNull\": [\"$tournamentStartDate\", false]" +
                "}" +
                "}, {" +
                "\"$not\": {" +
                "\"$ifNull\": [\"$matchStartDate\", false]" +
                "}" +
                "}]" +
                "}]" +
                "}," +
                "\"then\": \"$$DESCEND\"," +
                "\"else\": \"$$PRUNE\"" +
                "}" +
                "}}" +
                "}\"";

        return rawjson;
    }

}
