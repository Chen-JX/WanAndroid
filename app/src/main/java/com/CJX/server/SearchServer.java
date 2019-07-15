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

public class SearchServer extends Thread{

    private String mUrl;
    private String mRequest;

    private Handler mHandler;
    public SearchServer(String request, Handler handler, int page){
        this.mRequest = "页码=" + page + "&k="+ request;
//        this.mPage = page;
        this.mHandler = handler;
        this.mUrl = "https://www.wanandroid.com/article/query/" + page + "/json";
    }

    @Override
    public void run() {
        Message message = Message.obtain();
        if(HttpConnectionUtil.checkNetWork()){
            String json = HttpConnectionUtil.sendHttpRequest(mUrl, mRequest,"POST");
            Log.d("Search Json Result", "run: "+json);
            if(json == null){
                message.what = Constant.NULL;//没有查询到相应的结果
            }else{
                ArrayList<HomePageArticle> result = parseJson(json);
                Bundle bundle = new Bundle();
                message.setData(bundle);
                bundle.putParcelableArrayList("result",result);
                message.what = Constant.ARTICLE;
            }
        }else{
            message.what = Constant.ERROR;//网络出现错误
        }
        mHandler.sendMessage(message);
    }

    /**
     * 解析搜索内容的JSON数据
     * @param json
     * @return ArrayList<HomePageArticle>
     */
    private ArrayList<HomePageArticle> parseJson(String json){
        ArrayList<HomePageArticle> homePageArticles = new ArrayList<>();
        try{
            JSONObject firstObject = new JSONObject(json);
            JSONObject dataObject = firstObject.getJSONObject("data");
            JSONArray jsonArray = dataObject.optJSONArray("datas");
            if(jsonArray.length() == 0){
                return null;
            }else{
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
            }

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
        }catch(JSONException e){
            e.printStackTrace();
        }
        return homePageArticles;
    }

}
