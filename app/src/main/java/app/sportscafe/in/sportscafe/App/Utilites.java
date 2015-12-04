package app.sportscafe.in.sportscafe.App;

/**
 * Created by rb on 30/11/15.
 */
public class Utilites
{

    private static String ArticlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private static String ArticlesImageURL = "https://sportscafe.in/img/es3";
    private static String FixturesURL ="https://sportscafe.in/api/fixtures/getMatchesWithAggregation";
    private static String TeamImg="https://sportscafe.in/img/es3-cfit-w300-h300/scweb/scapp/partials/images/sports";
    private static String ArticleContentURL = "https://sportscafe.in/api/articles/getArticleById/";
    private static final String STATE_ARTICLES = "state_articles";
    private static final String TAG = "LOGGING";

    public static String getTeamImg() {
        return TeamImg;
    }

    public static String getFixturesURL() {
        return FixturesURL;
    }

    public static String getArticlesWithConditionsURL()
    {
        return ArticlesWithConditionsURL;
    }

    public static String getArticlesImageURL()
    {
        return ArticlesImageURL;
    }

    public static String getArticleContentURL()
    {
        return ArticleContentURL;
    }

    public static String getStateArticles()
    {
        return STATE_ARTICLES;
    }

    public static String getTAG()
    {
        return TAG;
    }
}
