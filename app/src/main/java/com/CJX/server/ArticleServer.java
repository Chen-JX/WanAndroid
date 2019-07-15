package com.CJX.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.CJX.bean.Constant;
import com.CJX.bean.HomePageArticle;
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

    public ArticleServer(int position, Handler handler){
        this.mPosition = position;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        message.setData(bundle);
        if(!(HttpConnectionUtil.checkNetWork())){
            message.what = Constant.ERROR;
            mHandler.sendMessage(message);

        }else{
            String url = "https://www.wanandroid.com/article/list/"+this.mPosition+"/json";
            Log.d(TAG, "run: -----------> requests article page = " + this.mPosition);
            message.what = Constant.LIST;
            String articleJson = HttpConnectionUtil.sendHttpRequest(url,"","GET");
            ArrayList<HomePageArticle> homePageArticles =  parseJson(articleJson);
            bundle.putParcelableArrayList("result",homePageArticles);
            mHandler.sendMessage(message);
        }

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
            //TODO 页码为第一个参数
            String page = firstObject.optString("");

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
