package com.CJX.server;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

import com.CJX.bean.Constant;
import com.CJX.bean.ProjectArticleCategory;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 项目的目录信息
 */
public class ProjectArticleCategoryServer extends Thread {
    private Handler mHandler;
    private final String mUrl = "https://www.wanandroid.com/project/tree/json";
    private final String TAG = "ArticleCategoryServer";

    public ProjectArticleCategoryServer(Handler handler){
        this.mHandler = handler;
    }

    @Override
    public void run() {
        Message message = Message.obtain();
        if(HttpConnectionUtil.checkNetWork()){
            String json = HttpConnectionUtil.sendHttpRequest(mUrl,null,"GET");
            if(json != null){
                Log.d(TAG, "-------------->category ");
                ArrayList<ProjectArticleCategory> result = parseJson(json);
                Bundle bundle = new Bundle();
                message.setData(bundle);
                bundle.putParcelableArrayList("result",result);
                message.what = Constant.CATEGORY;
            }else{
                Log.d(TAG, "-------------->error");
                message.what = Constant.ERROR;
            }
        }else{
            message.what = Constant.ERROR;
        }
        mHandler.sendMessage(message);
    }

    /**
     * 解析项目文章列表的json数据
     * @param json
     * @return ArrayList<ProjectArticleCategory>
     */
    private ArrayList<ProjectArticleCategory> parseJson(String json){
        ArrayList<ProjectArticleCategory> articleCategories = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.optJSONArray("data");

            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject object = jsonArray.getJSONObject(i);

                ProjectArticleCategory projectArticleCategory = new ProjectArticleCategory();
                projectArticleCategory.setId(object.optString("id"));
                projectArticleCategory.setName(object.optString("name"));

                articleCategories.add(projectArticleCategory);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return articleCategories;

    }
}
