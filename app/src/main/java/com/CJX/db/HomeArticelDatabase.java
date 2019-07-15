package com.CJX.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.CJX.MyApplication;
import com.CJX.bean.HomePageArticle;

import java.util.ArrayList;

public class HomeArticelDatabase {
    private final String TAG = "HomeArticelDatabase";
    private HomeArticelDatabaseHelper databaseHelper = new HomeArticelDatabaseHelper(MyApplication.getContext(),"HomePageArticleList.db",null,1);

    //返回查询结果
    public ArrayList<HomePageArticle> returnData(int position){
        return getData(position + 1);
    }

    public void insertDataToDatabase(ArrayList<HomePageArticle> homePageArticleList){
        insertData(homePageArticleList);
    }

    public void updateDataInDatabase(ArrayList<HomePageArticle> homePageArticleList){
        updateData(homePageArticleList);
    }

    //向数据库中插入数据
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
     *根据页码从数据库中查询数据,position 应该加1
     */
    private ArrayList<HomePageArticle> getData(int position){
        ArrayList<HomePageArticle> homePageArticles = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();


        //查询表中的数据
        Cursor cursor = db.query("HomePageArticleList",null,"page = ?",new String[]{position + ""},null,null,null);

        while(cursor.moveToNext()){
            HomePageArticle homePageArticle = new HomePageArticle();

            homePageArticle.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            homePageArticle.setChapterName(cursor.getString(cursor.getColumnIndex("chapterName")));
            homePageArticle.setSuperChapterName(cursor.getString(cursor.getColumnIndex("superChapterName")));
            homePageArticle.setLink(cursor.getString(cursor.getColumnIndex("link")));
            homePageArticle.setNiceDate(cursor.getString(cursor.getColumnIndex("niceDate")));
            homePageArticle.setTitle(cursor.getString(cursor.getColumnIndex("title")));

            homePageArticles.add(homePageArticle);
//            Log.d(TAG,"--------------------> query Success");
        }
        cursor.close();
        db.close();//关闭数据库


        return homePageArticles;
    }

    /**
     * 更新数据库中的数据
     */
    //TODO 在相同的页数的地方更新数据
    private void updateData(ArrayList<HomePageArticle> homePageArticleList){
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

            db.update("HomePageArticleList",values,"page = ?",new String[]{homePageArticle.getPage()});
        }
        db.close();
        Log.d(TAG,"--------------------> update Success");
    }


}
