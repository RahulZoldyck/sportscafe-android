package app.sportscafe.in.sportscafe.App;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rb on 17/12/15.
 */
public class SCDataBaseClass {
    static SCDBHelper scdbHelper;

    public SCDataBaseClass(Context context) {
        scdbHelper = new SCDBHelper(context);
    }

    public void insertData(ArrayList<Article> articles) {

        SQLiteDatabase db = scdbHelper.getWritableDatabase();

        for (int i = 0; i < articles.size(); i++) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataBaseConstants.ARTICLE_ID, articles.get(i).getId());
            contentValues.put(DataBaseConstants.TITLE, articles.get(i).getTitle());
            contentValues.put(DataBaseConstants.SUMMARY, articles.get(i).getSummary());
            contentValues.put(DataBaseConstants.CONTENT, articles.get(i).getContent());
            contentValues.put(DataBaseConstants.IMAGEURL, articles.get(i).getImageUrl());
            contentValues.put(DataBaseConstants.AUTHOR, articles.get(i).getAuthor());
            contentValues.put(DataBaseConstants.SPORT, articles.get(i).getSport());
            contentValues.put(DataBaseConstants.DATE, articles.get(i).getDate());
            contentValues.put(DataBaseConstants.ARTICLE_TYPE, articles.get(i).getArticleType());
            contentValues.put(DataBaseConstants.CREDITS, articles.get(i).getCredits());
            contentValues.put(DataBaseConstants.SLUG, articles.get(i).getSlug());
            contentValues.put(DataBaseConstants.ARTICLE_ID+"_"+DataBaseConstants.ARTICLE_TYPE, articles.get(i).getId()+"_"+articles.get(i).getArticleType());

            if (articles.get(i).getContent() != null) {
                if (!articles.get(i).getContent().equals(""))
                    contentValues.put(DataBaseConstants.ARTICLE_DOWNLOADED, true);
                else
                    contentValues.put(DataBaseConstants.ARTICLE_DOWNLOADED, false);
            } else
                contentValues.put(DataBaseConstants.ARTICLE_DOWNLOADED, false);
            long id = db.insert(DataBaseConstants.TABLE_NAME, null, contentValues);
        }
    }
    public  ArrayList<Article> getArticleList(String... articleTypes){
        ArrayList<Article> articles = new ArrayList<>();

        SQLiteDatabase database;
        database = scdbHelper.getWritableDatabase();
        Cursor cursor = database.query(DataBaseConstants.TABLE_NAME, DataBaseConstants.getColumns(),
                null, null, null, null, DataBaseConstants.DATE+" DESC");
        for (String articletype :articleTypes) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                if (cursor.getString(9).equals(articletype))
                {
                    Article articleTemp = SCDataBaseClass.cursorToArticle(cursor);
                    articles.add(articleTemp);
                }
                cursor.moveToNext();
            }

        }
        cursor.close();
        return articles;
    }
    public  ArrayList<Article> getMyFeeds(String... gamesPrefs){
        ArrayList<Article> articles = new ArrayList<>();

            SQLiteDatabase database;
            database = scdbHelper.getWritableDatabase();
            Cursor cursor = database.query(true,DataBaseConstants.TABLE_NAME, DataBaseConstants.getColumns(), null,null,
                    DataBaseConstants.ARTICLE_ID, null,DataBaseConstants.DATE+" DESC",null);

            cursor.moveToFirst();
            boolean isPreferred;
            while (!cursor.isAfterLast()) {
                isPreferred=false;
                for (String game : gamesPrefs ){
                    if (cursor.getString(7).equals(game)){
                        isPreferred=true;
                    }
                }
                if (isPreferred) {
                    Article articleTemp = SCDataBaseClass.cursorToArticle(cursor);
                    articles.add(articleTemp);
                }
                cursor.moveToNext();
            }

        cursor.close();
        return articles;
    }

    public static Article cursorToArticle(Cursor cursor) {
        Article article = new Article();
        article.setId(cursor.getString(1));
        article.setTitle(cursor.getString(2));
        article.setSummary(cursor.getString(3));
        article.setContent(cursor.getString(4));
        article.setImageUrl(cursor.getString(5));
        article.setAuthor(cursor.getString(6));
        article.setSport(cursor.getString(7));
        article.setDate(cursor.getString(8));
        article.setArticleType(cursor.getString(9));
        article.setCredits(cursor.getString(10));
        article.setSlug(cursor.getString(12));
        return article;
    }

    public static class SCDBHelper extends SQLiteOpenHelper {
        private static final String CREATE_TABLE = "CREATE TABLE " + DataBaseConstants.TABLE_NAME + " (" +
                DataBaseConstants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataBaseConstants.ARTICLE_ID + " INTEGER , " + DataBaseConstants.TITLE + " TEXT, " +
                DataBaseConstants.SUMMARY + " TEXT, " + DataBaseConstants.CONTENT + " TEXT, " +
                DataBaseConstants.IMAGEURL + " TEXT, " + DataBaseConstants.AUTHOR + " TEXT, " +
                DataBaseConstants.SPORT + " TEXT, " + DataBaseConstants.DATE + " TEXT, " +
                DataBaseConstants.ARTICLE_TYPE + " TEXT, " +
                DataBaseConstants.CREDITS + " TEXT, " + DataBaseConstants.SLUG + " TEXT, "+
                DataBaseConstants.ARTICLE_DOWNLOADED + " TEXT, " +
                DataBaseConstants.ARTICLE_ID+"_"+DataBaseConstants.ARTICLE_TYPE +" TEXT UNIQUE);";
        private static final String DELETE_TABLE = "DROP TABLE IF EXISTS";
        private static int dbVersion = 8;

        public SCDBHelper(Context context) {
            super(context, DataBaseConstants.DATABASE_NAME, null, dbVersion);
            Log.d(Utilites.getTAG(), "Constructor Called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(Utilites.getTAG(), CREATE_TABLE);
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(Utilites.getTAG(), "Updating Database from " + oldVersion + " to " + newVersion);
            db.execSQL(DELETE_TABLE + " " + DataBaseConstants.TABLE_NAME);
            onCreate(db);
        }


    }
}
