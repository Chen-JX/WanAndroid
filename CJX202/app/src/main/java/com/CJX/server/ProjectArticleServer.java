package com.CJX.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.CJX.bean.Constant;
import com.CJX.bean.ProjectArticle;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectArticleServer extends Thread {
    private final String TAG = "ProjectArticleServer";
//    private final String mHttpHead = "https://www.wanandroid.com/project/list/";
//    private final String mHttpMiddle = "/json?cid=";
//    private String mId;
    private String mUrl;
//    private int mPage;
    private Handler mHandler;

    public ProjectArticleServer(Handler handler, String id, int page){
        this.mHandler = handler;
//        this.mId = id;
//        this.mPage = page;
        this.mUrl = "https://www.wanandroid.com/project/list/" + page + "/json?cid=" + id;
    }
    @Override
    public void run() {
        Message message = Message.obtain();
        if(HttpConnectionUtil.checkNetWork()){
            String json = HttpConnectionUtil.sendHttpRequest(mUrl, null, "GET");
            ArrayList<ProjectArticle> projectArticles = parseToJson(json);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("result",projectArticles);
            message.setData(bundle);
            message.what = Constant.ARTICLE;
        }else{
            message.what = Constant.ERROR;
        }
        mHandler.sendMessage(message);
    }

    /**
     * 解析项目文章列表的JSON数据
     * @param json
     * @return ArrayList<ProjectArticle>
     */
    private ArrayList<ProjectArticle> parseToJson(String json){
        if(json == null){
            Log.d(TAG, "------------------->parseToJson: ");
        }else{
            Log.d(TAG, "------------------->hhhhhhhhhhhh ");
        }
        ArrayList<ProjectArticle> projectArticles = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);

            //得到第二层数据
            JSONObject twoJsonObject = jsonObject.optJSONObject("data");
            //得到第三层数据
            JSONArray jsonArray = twoJsonObject.optJSONArray("datas");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject data = jsonArray.optJSONObject(i);

                ProjectArticle projectArticle = new ProjectArticle();

                projectArticle.setAuthor(data.optString("author"));
                projectArticle.setChapterId(data.optString("chapterId"));
                projectArticle.setChapterName(data.optString("chapterName"));
                projectArticle.setDesc(data.optString("desc"));
                projectArticle.setEnvelopePic(data.optString("envelopePic"));
                projectArticle.setLink(data.optString("link"));
                projectArticle.setNiceDate(data.optString("niceDate"));
                projectArticle.setSuperChapterName(data.optString("superChapterName"));
                projectArticle.setTitle(data.optString("title"));

                projectArticles.add(projectArticle);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return projectArticles;

    }
}
