package app.sportscafe.in.sportscafe.MostViewed;


public class MostViewedConstants {
    public static final String CONTENT="content";
    public static final String SUMMARY="contentSummary";
    public static final String DATA="data";
    public static final String DAY="day";
    public static final String MONTH="month";
    public static final String ARG_ITEM ="item";
    public static final String WEEK="week";
    public static final String TITLE="title";
    public static final String IMAGES="images";
    public static final String FEATURED="featured";
    public static final String PATH ="path";
    public static final String CLASSIFICATION="classifications";
    public static final String SECTIONS="sections";
    public static final String SPORT="sport";
    public static final String ID="_id";
    public static final String QUERY_DAY ="{\"msg\":{\"conditions\":{\"published\":true,\"classifications.sections.misc\":\"mvday\"},\"projection\":{\"content\":0},\"options\":{\"sort\":{\"publishDate\":-1},\"limit\":4}}}";
    public static final String QUERY_WEEK ="{\"msg\":{\"conditions\":{\"published\":true,\"classifications.sections.misc\":\"mvweek\"},\"projection\":{\"content\":0},\"options\":{\"sort\":{\"publishDate\":-1},\"limit\":4}}}";
    public static final String QUERY_MONTH ="{\"msg\":{\"conditions\":{\"published\":true,\"classifications.sections.misc\":\"mvmonth\"},\"projection\":{\"content\":0},\"options\":{\"sort\":{\"publishDate\":-1},\"limit\":4}}}";
    public static final String MODIFICATION_DATE = "modificationDate";
}
