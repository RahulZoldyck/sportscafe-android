package app.sportscafe.in.sportscafe;

/**
 * Created by rb on 30/11/15.
 */
public class Utilites
{
    private String getArticlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private String getArticlesImageURL = "https://sportscafe.in/img/es3";
    private String getFixtures="https://sportscafe.in/api/fixtures/getMatchesWithAggregation";

    public String getFixtureURL() {
        return getFixtures;
    }

    public String ret_getArticlesWithConditionsURL()
    {
        return getArticlesWithConditionsURL;
    }
    public String ret_ImageURL()
    {
        return getArticlesImageURL;
    }
}
