package com.CJX.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.CJX.bean.Constant;
import com.CJX.bean.HomePageArticle;
import com.CJX.db.HomeArticelDatabase;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 访问Http获取首页文章信息
 */
public class ArticleServer extends Thread{
    private final String TAG = "ArticleServer";

    private int mPosition;
    private Handler mHandler;
   // private ArrayList<HomePageArticle> homePageArticles;

    public ArticleServer(int position, Handler handler){
        this.mPosition = position;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        ArrayList<HomePageArticle> homePageArticles;

        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        message.setData(bundle);
        HomeArticelDatabase database = new HomeArticelDatabase();

        homePageArticles = database.returnData(mPosition);

        //检查数据库中有无数据
        if(homePageArticles.size() == 0){
            Log.d(TAG, "--------------------->NOT ONLINE ");
            //数据库中没有找到数据，从网络中访问数据
            if(!(HttpConnectionUtil.checkNetWork())){
                message.what = Constant.ERROR;
                mHandler.sendMessage(message);
                return;
            }else{
                String url = "https://www.wanandroid.com/article/list/"+this.mPosition+"/json";
                String articleJson = HttpConnectionUtil.sendHttpRequest(url,"","GET");
                homePageArticles =  parseJson(articleJson);
                //将数据存入数据库
                database.insertDataToDatabase(homePageArticles);

            }
        }
        message.what = Constant.LIST;
        bundle.putParcelableArrayList("result",homePageArticles);
        mHandler.sendMessage(message);
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

            String page = dataObject.optString("curPage");

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
                homePageArticle.setPage(page);

                homePageArticles.add(homePageArticle);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return homePageArticles;
    }

}
