package com.CJX.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.CJX.bean.Constant;
import com.CJX.bean.HomePageArticle;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * 发起Http请求，获取首页文章数据
 */
public class HomePageArticleServer extends Thread{
//    private final String mHomePageHttpHead = "https://www.wanandroid.com/article/list/";
//    private String mPage;
//    private String mHttpBottom = "/json";
//    private Handler mHandler;
//
//
//    public HomePageArticleServer(Handler handler, String page){
//        this.mHandler = handler;
//        this.mPage = page;
//    }
//
//    @Override
//    public void run() {
//        Message message = Message.obtain();
//
//        if(HttpConnectionUtil.checkNetWork()){
//                String json = HttpConnectionUtil.sendHttpRequest(mHomePageHttpHead + mPage + mHttpBottom,null,"GET");
//            if(json == null){//如果没有内容返回
//                message.what = Constant.NULL;
//            }else{
//                ArrayList<HomePageArticle> result = parseJson(json);
//                Bundle bundle = new Bundle();
//                message.setData(bundle);
//                bundle.putParcelableArrayList("result",result);
//                message.what = Constant.ARTICLE;
//            }
//        }else{
//            message.what = Constant.ERROR;
//        }
//        mHandler.sendMessage(message);
//    }
//
//    private ArrayList<HomePageArticle> parseJson(String json){
//        ArrayList<HomePageArticle> homePageArticles = new ArrayList<>();
//        try{
//            JSONObject firstObject = new JSONObject(json);
//            JSONObject dataObject = firstObject.getJSONObject("data");
//            JSONArray jsonArray = dataObject.optJSONArray("datas");
//
//            for(int i = 0; i < jsonArray.length(); i++){
//                JSONObject childObject = jsonArray.getJSONObject(i);
//
//                HomePageArticle homePageArticle = new HomePageArticle();
//
//                homePageArticle.setAuthor(childObject.optString("author"));
//                homePageArticle.setChapterName(childObject.optString("chapterName"));
//                homePageArticle.setSuperChapterName(childObject.optString("superChapterName"));
//                homePageArticle.setLink(childObject.optString("link"));
//                homePageArticle.setNiceDate(childObject.optString("niceDate"));
//                homePageArticle.setTitle(childObject.optString("title"));
//
//                homePageArticles.add(homePageArticle);
//            }
//        }catch(JSONException e){
//            e.printStackTrace();
//        }
//        return homePageArticles;
//    }
}
