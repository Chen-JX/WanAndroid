package com.CJX.server;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.CJX.MyApplication;
import com.CJX.bean.Constant;
import com.CJX.bean.HomePageArticle;
import com.CJX.db.HomePageArticleListDatabase;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问Http获取首页文章信息
 */
public class ArticleServer extends Thread{
    private final String TAG = "ArticleServer";

    private int mPosition;
    private Handler mHandler;

    public ArticleServer(int position, Handler handler){
        this.mPosition = position;
        this.mHandler = handler;
    }

    @Override
    public void run() {

        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        message.setData(bundle);
        if(!HttpConnectionUtil.checkNetWork()){

            message.what = Constant.ERROR;
            mHandler.sendMessage(message);

        }else{
            String url = "https://www.wanandroid.com/article/list/"+this.mPosition+"/json";
            Log.d(TAG, "run: -----------> requests article page = " + this.mPosition);
            message.what = Constant.LIST;
            String articleJson = HttpConnectionUtil.sendHttpRequest(url,"","GET");
//            Log.d(TAG, "----------------------> articleJson" + articleJson.substring(0,50));
            ArrayList<HomePageArticle> homePageArticles =  parseJson(articleJson);

            //将数据添加进数据库
            addDataToDatabase(homePageArticles);


            bundle.putParcelableArrayList("result",homePageArticles);
            mHandler.sendMessage(message);
        }
//        message.what = Constant.LIST;
//        ArrayList<HomePageArticle> homePageArticles = getData();
//        bundle.putParcelableArrayList("result",homePageArticles);
//        mHandler.sendMessage(message);
    }

    /**
     * 解析json数据
     * @param json
     * @return ArrayList<HomePageArticle>
     */
    private ArrayList<HomePageArticle> parseJson(String json){
        ArrayList<HomePageArticle> homePageArticles = new ArrayList<HomePageArticle>();
        try{
            JSONObject firstObject = new JSONObject(json);
            JSONObject dataObject = firstObject.getJSONObject("data");
            JSONArray jsonArray = dataObject.optJSONArray("datas");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject childObject = jsonArray.getJSONObject(i);

                HomePageArticle homePageArticle = new HomePageArticle();

                homePageArticle.setAuthor(childObject.optString("author"));
                homePageArticle.setChapterName(childObject.optString("chapterName"));
                homePageArticle.setSuperChapterName(childObject.optString("superChapterName"));
                homePageArticle.setLink(childObject.optString("link"));
                homePageArticle.setNiceDate(childObject.optString("niceDate"));
                homePageArticle.setTitle(childObject.optString("title"));

                homePageArticles.add(homePageArticle);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return homePageArticles;
    }

    /**
     * 把数据存储至数据库
     * */
    private void addDataToDatabase(List<HomePageArticle> homePageArticleList){
        HomePageArticleListDatabase homePageArticleListDatabase = new HomePageArticleListDatabase(MyApplication.getContext(),"HomePageArticleList.db",null,1);
        //homePageArticleListDatabase.getWritableDatabase();
        SQLiteDatabase db = homePageArticleListDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i = 0; i < homePageArticleList.size(); i++){
            HomePageArticle homePageArticle = homePageArticleList.get(i);

            contentValues.put("author",homePageArticle.getAuthor());
            contentValues.put("chapterName",homePageArticle.getChapterName());
            contentValues.put("superChapterName",homePageArticle.getSuperChapterName());
            contentValues.put("link",homePageArticle.getLink());
            contentValues.put("niceDate",homePageArticle.getNiceDate());
            contentValues.put("page",mPosition);
            contentValues.put("title",homePageArticle.getTitle());

            db.insert("HomePageArticleList",null,contentValues);
            contentValues.clear();
        }
    }



    //从数据库中查询数据
    private ArrayList<HomePageArticle> getData(){
        ArrayList<HomePageArticle> homePageArticles = new ArrayList<HomePageArticle>();

        HomePageArticleListDatabase homePageArticleListDatabase = new HomePageArticleListDatabase(MyApplication.getContext(),"HomePageArticleList.db",null,1);
        SQLiteDatabase db = homePageArticleListDatabase.getWritableDatabase();

        //查询表中的数据
        Cursor cursor = db.query("HomePageArticleList",null,null,null,null,null,null);
        try{
            if(cursor.moveToFirst()){
                do{
                    HomePageArticle homePageArticle = new HomePageArticle();

                    homePageArticle.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                    homePageArticle.setChapterName(cursor.getString(cursor.getColumnIndex("chapterName")));
                    homePageArticle.setSuperChapterName(cursor.getString(cursor.getColumnIndex("superChapterName")));
                    homePageArticle.setLink(cursor.getString(cursor.getColumnIndex("link")));
                    homePageArticle.setNiceDate(cursor.getString(cursor.getColumnIndex("niceDate")));
                    homePageArticle.setTitle(cursor.getString(cursor.getColumnIndex("title")));

                    homePageArticles.add(homePageArticle);
                }while(cursor.moveToNext());
            }
        }finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
//        if(cursor.moveToFirst()){
//            do{
//                HomePageArticle homePageArticle = new HomePageArticle();
//
//                homePageArticle.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
//                homePageArticle.setChapterName(cursor.getString(cursor.getColumnIndex("chapterName")));
//                homePageArticle.setSuperChapterName(cursor.getString(cursor.getColumnIndex("superChapterName")));
//                homePageArticle.setLink(cursor.getString(cursor.getColumnIndex("link")));
//                homePageArticle.setNiceDate(cursor.getString(cursor.getColumnIndex("niceDate")));
//                homePageArticle.setTitle(cursor.getString(cursor.getColumnIndex("title")));
//
//                homePageArticles.add(homePageArticle);
//            }while(cursor.moveToNext());
//        }
        cursor.close();
        return homePageArticles;
    }

}
