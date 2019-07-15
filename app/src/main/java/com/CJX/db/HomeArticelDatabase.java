package com.CJX.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.CJX.MyApplication;
import com.CJX.bean.HomePageArticle;

import java.util.ArrayList;

public class HomeArticelDatabase {
    private HomeArticelDatabaseHelper databaseHelper = new HomeArticelDatabaseHelper(MyApplication.getContext(),"HomePageArticleList.db",null,1);

    //返回查询结果
    public ArrayList<HomePageArticle> returnData(){
        return getData();
    }

    private void insertData(ArrayList<HomePageArticle> homePageArticleList){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i = 0; i < homePageArticleList.size(); i++){
            HomePageArticle homePageArticle = homePageArticleList.get(i);

            contentValues.put("author",homePageArticle.getAuthor());
            contentValues.put("chapterName",homePageArticle.getChapterName());
            contentValues.put("superChapterName",homePageArticle.getSuperChapterName());
            contentValues.put("link",homePageArticle.getLink());
            contentValues.put("niceDate",homePageArticle.getNiceDate());
            contentValues.put("page",homePageArticle.getPage());
            contentValues.put("title",homePageArticle.getTitle());

            db.insert("HomePageArticleList",null,contentValues);
            contentValues.clear();
        }
        db.close();
    }

    /**
     *从数据库中查询数据
     */
    private ArrayList<HomePageArticle> getData(){
        ArrayList<HomePageArticle> homePageArticles = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        //查询表中的数据
        Cursor cursor = db.query("HomePageArticleList",null,null,null,null,null,null);

        while(cursor.moveToNext()){
            HomePageArticle homePageArticle = new HomePageArticle();

            homePageArticle.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            homePageArticle.setChapterName(cursor.getString(cursor.getColumnIndex("chapterName")));
            homePageArticle.setSuperChapterName(cursor.getString(cursor.getColumnIndex("superChapterName")));
            homePageArticle.setLink(cursor.getString(cursor.getColumnIndex("link")));
            homePageArticle.setNiceDate(cursor.getString(cursor.getColumnIndex("niceDate")));
            homePageArticle.setTitle(cursor.getString(cursor.getColumnIndex("title")));

            homePageArticles.add(homePageArticle);
        }
        cursor.close();
        db.close();//关闭数据库

        return homePageArticles;
    }

    /**
     * 更新数据库中的数据
     */
    private void updateData(ArrayList<HomePageArticle> homePageArticleList,){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        for(int i = 0; i <  homePageArticleList.size(); i++){
            HomePageArticle homePageArticle = homePageArticleList.get(i);

            values.put("author",homePageArticle.getAuthor());
            values.put("chapterName",homePageArticle.getChapterName());
            values.put("superChapterName",homePageArticle.getSuperChapterName());
            values.put("link",homePageArticle.getLink());
            values.put("niceDate",homePageArticle.getNiceDate());
            values.put("page",homePageArticle.getPage());
            values.put("title",homePageArticle.getTitle());

            db.update("HomePageArticleList",values,null,null);
        }


    }


}
