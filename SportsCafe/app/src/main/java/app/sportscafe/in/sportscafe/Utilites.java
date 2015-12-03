package app.sportscafe.in.sportscafe;

/**
 * Created by rb on 30/11/15.
 */
public class Utilites
{
    private String ArticlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private String ArticlesImageURL = "https://sportscafe.in/img/es3";
    private String FixturesURL ="https://sportscafe.in/api/fixtures/getMatchesWithAggregation";

    public String getFixtureURL() {
        return FixturesURL;
    }

    public String ret_getArticlesWithConditionsURL()
    {
        return ArticlesWithConditionsURL;
    }
    public String ret_ImageURL()
    {
        return ArticlesImageURL;
    }
}
