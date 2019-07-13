package com.CJX.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HomePageArticleListDatabase extends SQLiteOpenHelper {
    private Context mContext;
    public static final String CREATE_HOMEPAGEARTICLELIST = "create table HomePageArticleList ("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "chapterName text, "
            + "superChapterName text,"
            + "link text, "
            + "niceDate text, "
            + "page integer,"
            + "title text)";

    public HomePageArticleListDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
//TODO 第一次下载更新数据库，检查数据两种情况，数据库不存在，没有缓存数据，均为不能显示
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOMEPAGEARTICLELIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
