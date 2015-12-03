package app.sportscafe.in.sportscafe.App;

/**
 * Created by rb on 30/11/15.
 */
public class Utilites
{

    private static String ArticlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private static String ArticlesImageURL = "https://sportscafe.in/img/es3";
    private static String FixturesURL ="https://sportscafe.in/api/fixtures/getMatchesWithAggregation";

    public static String getFixtureURL() {
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
}
