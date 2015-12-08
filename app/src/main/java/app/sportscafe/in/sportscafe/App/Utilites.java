package app.sportscafe.in.sportscafe.App;

/**
 * Created by rb on 30/11/15.
 */
public class Utilites
{

    private static String articlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private static String articlesImageURL = "https://sportscafe.in/img/es3";
    private static String fixturesURL ="https://sportscafe.in/api/fixtures/getMatchesWithAggregation";
    private static String teamImg ="https://sportscafe.in/img/es3-cfit-w300-h300/scweb/scapp/partials/images/sports";
    private static String articleContentURL = "https://sportscafe.in/api/articles/getArticleById/";
    private static final String stateArticles = "state_articles";
    private static final String TAG = "LOGGING";
    private static String MVImgURL="https://sportscafe.in/img/es3-cfill-w300-h300/";
    private static String MVImgURL2="https://sportscafe.in/img/es3-cfit-w800-h600/";

    public static String getMVImgURL(int i) {
        switch (i){
            case 0:
                return MVImgURL;
            default:
                return MVImgURL2;
        }

    }

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

    public static String getArticlesImageURL()
    {
        return articlesImageURL;
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
