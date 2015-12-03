package app.sportscafe.in.sportscafe.App;

/**
 * Created by rb on 30/11/15.
 */
public class Utilites
{
<<<<<<< HEAD
    private String ArticlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private String ArticlesImageURL = "https://sportscafe.in/img/es3";
    private String FixturesURL ="https://sportscafe.in/api/fixtures/getMatchesWithAggregation";

    public String getFixtureURL() {
=======
    private static String ArticlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private static String ArticlesImageURL = "https://sportscafe.in/img/es3";
    private static String FixturesURL ="https://sportscafe.in/api/fixtures/getMatchesWithAggregation";

    public static String getFixtureURL() {
>>>>>>> FETCH_HEAD
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
