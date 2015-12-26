package app.sportscafe.in.sportscafe.App;

public class DataBaseConstants
{
    public static final String DATABASE_NAME = "SportsCafeDatabase";
    public static final String TABLE_NAME = "Articles";
    public static final String _ID = "_id";
    public static final String ARTICLE_ID = "article_id";
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String CONTENT = "content";
    public static final String IMAGEURL = "imageUrl";
    public static final String AUTHOR = "author";
    public static final String SPORT = "sport";
    public static final String DATE = "date";
    public static final String ARTICLE_TYPE = "articleType";
    public static final String CREDITS = "credits";
    public static final String ARTICLE_DOWNLOADED = "articleDownloaded";
    public static final String SLUG = "slug";

    public static final String[] getColumns()
    {
        String columns[] = new String[13];
        columns[0] = _ID;
        columns[1] = ARTICLE_ID;
        columns[2] = TITLE;
        columns[3] = SUMMARY;
        columns[4] = CONTENT;
        columns[5] = IMAGEURL;
        columns[6] = AUTHOR;
        columns[7] = SPORT;
        columns[8] = DATE;
        columns[9] = ARTICLE_TYPE;
        columns[10] = CREDITS;
        columns[11] = ARTICLE_DOWNLOADED;
        columns[12] = SLUG;
        return columns;
    }
}
