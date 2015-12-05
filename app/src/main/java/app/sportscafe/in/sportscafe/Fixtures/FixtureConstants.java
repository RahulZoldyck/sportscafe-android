package app.sportscafe.in.sportscafe.Fixtures;


public class FixtureConstants {
    public static final String DATA="data";
    public static final String TEAMS="teams";
    public static final String GAMETYPE="gameType";
    public static final String INDIVIDUALS="individuals";
    public static final String PLAYERA="playerA";
    public static final String COUNTRY="country";
    public static final String PLAYERID="playerId";
    public static final String PLAYERNAMESHORT="playerNameShort";
    public static final String TEAMID="teamId";
    public static final String TEAMNAMESHORT="teamDisplayNameShort";
    public static final String MATCHES="matches";
    public static final String MATCHLINK="matchReportOrPreviewLink";
    public static final String MATCHSTATUS="matchStatus";
    public static final String TOURNAMENTSUPERNAME="tournamentSuperName";
    public static final String MATCHDATE="matchDate";
    public static final String TOURNAMENTVENUE="tournamentVenue";
    public static final String CITY="city";
    public static final String TOURNAMENTNAME="tournamentName";
    public static final String MATCHSTARTDATE="matchStartDate";
    public static final String MATCHID="matchId";
    public static final String MATCHVENUE="matchVenue";
    public static final String TEAMIDS="teamIds";
    public static final String TOURNAMENTID="tournamentId";
    public static final String MATCHRRESULTS="matchResult";
    public static final String MATCHFINALSCORE="matchFinalScore";
    public static final String[] games={"football","hockey","cricket","badminton"};


    public static String getQuery (String ISOyes,String ISOtomo){
        String rawjson="{\"msg\": {"+
                "\"$redact\": {"+
                "\"$cond\": {"+
                "\"if\": {"+
                "\"$or\": [{"+
                "\"$and\": [{"+
                "\"$gte\": [\""+ISOyes+"\", \"$tournamentStartDate\"]"+
                "}, {"+
                "\"$lt\": [\""+ISOtomo+"\", \"$tournamentEndDate\"]"+
                "}]"+
                " }, {"+
                "\"$and\": [{"+
                "\"$gte\": [\"$matchStartDate\", \""+ISOyes+"\"]"+
                "}, {"+
                "\"$lt\": [\"$matchStartDate\", \""+ISOtomo+"\"]"+
                "}]"+
                " }, {"+
                "\"$and\": [{"+
                "\"$not\": {"+
                "\"$ifNull\": [\"$tournamentStartDate\", false]"+
                "}"+
                "}, {"+
                "\"$not\": {"+
                "\"$ifNull\": [\"$matchStartDate\", false]"+
                "}"+
                "}]"+
                "}]"+
                "},"+
                "\"then\": \"$$DESCEND\","+
                "\"else\": \"$$PRUNE\""+
                "}"+
                "}}"+
                "}\"";

        return rawjson;
    }

}
