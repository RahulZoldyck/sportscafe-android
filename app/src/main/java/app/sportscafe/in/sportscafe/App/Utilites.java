package app.sportscafe.in.sportscafe.App;

/**
 * Created by rb on 30/11/15.
 */
public class Utilites
{

    public static final String ARTICLE_TYPE_MVWEEK = "mvweek";
    public static final String ARTICLE_TYPE_MVMONTH = "mvmonth";
    public static final String PREFERENCES = "myPref";
    public static final String GAME_PREF_ID = "gamePref";
    private static String articlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private static String initialImageURL = "https://sportscafe.in/img/es3";
    private static String fixturesURL ="https://sportscafe.in/api/fixtures/getMatchesWithAggregation";
    private static String teamImg ="https://sportscafe.in/img/es3-cfit-w300-h300/scweb/scapp/partials/images/sports";
    private static String articleContentURL = "https://sportscafe.in/api/articles/getArticleById/";
    private static final String stateArticles = "state_articles";
    private static final String TAG = "LOGGING";
    public static final String image_width = "800";
    public static final String ARTICLE_TYPE_MVDAY="mvday";
    public static final String image_height = "400";

    public static String getTeamImg() {
        return teamImg;
    }

    public static String getFixturesURL() {
        return fixturesURL;
    }

    public static String getArticlesWithConditionsURL()
    {
        return articlesWithConditionsURL;
    }

    public static String getInitialImageURL(String width,String height,String quality,String articleImageURL)
    {
        return initialImageURL+"-cfill-w"+width+"-h"+height+"-qn"+quality+"/"+articleImageURL;
    }

    public static String getArticleContentURL()
    {
        return articleContentURL;
    }

    public static String getStateArticles()
    {
        return stateArticles;
    }

    public static String getTAG()
    {
        return TAG;
    }
}
