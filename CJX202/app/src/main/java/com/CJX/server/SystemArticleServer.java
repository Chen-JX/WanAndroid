package com.CJX.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.CJX.bean.Constant;
import com.CJX.bean.SystemInformationBean;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 获取体系文章列表信息
 */
public class SystemArticleServer extends Thread{
    private Handler mHandler;
//    private final String mHttpStart = "https://www.wanandroid.com/article/list/";
//    private final String mHttpButton = "/json?cid=";
    private String mUrl;
//    private int mPage;
//    private String mId;
    public SystemArticleServer(Handler handler, int page, String id){
        this.mHandler = handler;
//        this.mPage = page;
        this.mUrl = "https://www.wanandroid.com/article/list/" + page + "/json?cid=" + id;
//        this.mId = id;
    }

    @Override
    public void run() {
        Message message = Message.obtain();
        if(HttpConnectionUtil.checkNetWork()){
            String json = HttpConnectionUtil.sendHttpRequest(mUrl,null,"GET");
            if(json != null){
                Bundle bundle = new Bundle();
                ArrayList<SystemInformationBean> systemInformationBeans = parseJson(json);
                bundle.putParcelableArrayList("result",systemInformationBeans);
                message.setData(bundle);
                message.what = Constant.ARTICLE;
            }else{
                message.what = Constant.NULL;
            }
        }else{
            message.what = Constant.ERROR;
        }
        mHandler.sendMessage(message);
    }

    /**
     * 解析体系文章信息的JSON数据
     * @param json
     * @return ArrayList<SystemInformationBean>
     */
    private ArrayList<SystemInformationBean> parseJson(String json){
        ArrayList<SystemInformationBean> systemInformationBeans = new ArrayList<>();
        try{
            JSONObject firstObject = new JSONObject(json);
            JSONObject secondObject = firstObject.optJSONObject("data");
            JSONArray data = secondObject.optJSONArray("datas");

            for(int i = 0; i < data.length(); i++){
                JSONObject childData = data.optJSONObject(i);

                SystemInformationBean systemInformationBean = new SystemInformationBean();
                systemInformationBean.setAuthor(childData.optString("author"));
                systemInformationBean.setLink(childData.optString("link"));
                systemInformationBean.setNiceDate(childData.optString("niceDate"));
                systemInformationBean.setTitle(childData.optString("title"));

                systemInformationBeans.add(systemInformationBean);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return systemInformationBeans;

    }
}
