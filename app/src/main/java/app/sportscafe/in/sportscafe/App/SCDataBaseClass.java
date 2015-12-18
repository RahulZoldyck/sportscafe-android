package app.sportscafe.in.sportscafe.App;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rb on 17/12/15.
 */
public class SCDataBaseClass
{
    private static final String DATABASE_NAME = "SportsCafeDatabase";
    private static final String TABLE_NAME = "Articles";
    private static final String _ID = "_id";
    private static final String ARTICLE_ID = "article_id";
    private static final String TITLE = "title";
    private static final String SUMMARY = "summary";
    private static final String CONTENT = "content";
    private static final String IMAGEURL = "imageUrl";
    private static final String AUTHOR = "author";
    private static final String SPORT = "sport";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String ARTICLE_TYPE = "articleType";
    private static final String CREDITS = "credits";

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            _ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ARTICLE_ID+" INTEGER, "+TITLE+" TEXT, "+
            SUMMARY+" TEXT, "+CONTENT+" TEXT, "+
            IMAGEURL+" TEXT, "+AUTHOR+" TEXT, "+
            SPORT+" TEXT, "+DATE+" TEXT, "+
            TIME+" TEXT, "+ARTICLE_TYPE+" TEXT, "+
            CREDITS+" TEXT);";
    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS";

    private static int dbVersion = 1;

    public class SCDBHelper extends SQLiteOpenHelper
    {
        public SCDBHelper(Context context)
        {
            super(context, DATABASE_NAME, null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.d(Utilites.getTAG(),"Updating Database from "+oldVersion+" to "+newVersion);
            db.execSQL(DELETE_TABLE+" "+TABLE_NAME);
            onCreate(db);
        }


    }
}
