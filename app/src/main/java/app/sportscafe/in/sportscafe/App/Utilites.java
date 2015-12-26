package app.sportscafe.in.sportscafe.App;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.TimeZone;

import app.sportscafe.in.sportscafe.R;

public class Utilites {

    public static final String ARTICLE_TYPE_MVWEEK = "mvweek";
    public static final String ARTICLE_TYPE_MVMONTH = "mvmonth";
    public static final String PREFERENCES = "myPref";
    public static final String GAME_PREF_ID = "gamePref";
    public static final String image_width = "800";
    public static final String ARTICLE_TYPE_MVDAY = "mvday";
    public static final String image_height = "400";
    private static final String stateArticles = "state_articles";
    private static final String TAG = "LOGGING";
    private static String sportscafeURL = "https://sportscafe.in/";
    private static String articlesWithConditionsURL = "https://sportscafe.in/api/articles/getArticlesWithConditions";
    private static String initialImageURL = "https://sportscafe.in/img/es3";
    private static String fixturesURL = "https://sportscafe.in/api/fixtures/getMatchesWithAggregation";
    private static String teamImg = "https://sportscafe.in/img/es3-cfit-w300-h300/scweb/scapp/partials/images/sports";
    private static String articleContentURL = "https://sportscafe.in/api/articles/getArticleById/";

    public static String getTeamImg() {
        return teamImg;
    }

    public static String getFixturesURL() {
        return fixturesURL;
    }

    public static String getArticlesWithConditionsURL() {
        return articlesWithConditionsURL;
    }

    public static String getInitialImageURL(String width, String height, String quality, String articleImageURL) {
        return initialImageURL + "-cfill-w" + width + "-h" + height + "-qn" + quality + "/" + articleImageURL;
    }

    public static String getArticleContentURL() {
        return articleContentURL;
    }

    public static String getStateArticles() {
        return stateArticles;
    }

    public static String getTAG() {
        return TAG;
    }

    public static String getSportscafeURL()
    {
        return sportscafeURL;
    }

    public static String getQuery(String... pref) {
        JSONArray or = new JSONArray();
        JSONObject obj;
        for (String sport : pref) {
            obj = new JSONObject();
            try {
                obj.put("classifications.sections.sport", sport);
                or.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "{\"msg\":{\"conditions\":{\"published\":true," +
                "\"classifications.sections.articleType\":\"match report\",\"$or\":" + or.toString() + "}," +
                "\"projection\":{\"content\":0},\"options\":{\"sort\":{\"publishDate\":-1},\"skip\":0," +
                "\"limit\":8}}}";

    }
    public static class ArticleComparator implements Comparator<Article>
    {
        Context context;
        public ArticleComparator(Context context)
        {
            this.context = context;
        }
        @Override
        public int compare(Article article1, Article article2) {
            Resources resources = context.getResources();
            SimpleDateFormat format=new SimpleDateFormat(resources.getString(R.string.parseISO));
            format.setTimeZone(TimeZone.getTimeZone(resources.getString(R.string.gmt)));
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            try
            {
                Log.d(Utilites.getTAG(),article1.getTitle());
                calendar1.setTime(format.parse(article1.getDate()));
                calendar2.setTime(format.parse(article2.getDate()));
            } catch (ParseException e)
            {
                Log.e(Utilites.getTAG(),e.toString());
            }
            return calendar1.compareTo(calendar2);
        }

    }
}
