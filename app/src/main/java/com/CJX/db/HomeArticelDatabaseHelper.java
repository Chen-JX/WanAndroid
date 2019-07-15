package com.CJX.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HomeArticelDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String CREATE_HOMEPAGEARTICLELIST = "create table HomePageArticleList ("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "chapterName text, "
            + "superChapterName text,"
            + "link text, "
            + "niceDate text, "
            + "page interger,"
            + "title text)";


    public HomeArticelDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOMEPAGEARTICLELIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
