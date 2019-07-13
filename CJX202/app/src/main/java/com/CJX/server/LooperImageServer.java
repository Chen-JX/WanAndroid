package com.CJX.server;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;

import com.CJX.bean.Banner;
import com.CJX.bean.Constant;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LooperImageServer extends Thread {
    private final String TAG = "LooperImageService";

    private final String mAddress = "https://www.wanandroid.com/banner/json";

    private Handler mHandler;
    public LooperImageServer(Handler handler){
        this.mHandler = handler;
    }

    @Override
    public void run() {
        Message message = mHandler.obtainMessage();

        //检查网络
        if(HttpConnectionUtil.checkNetWork()){//网络正常，发起请求
            Bundle bundle = new Bundle();
            message.setData(bundle);
            String json = HttpConnectionUtil.sendHttpRequest(mAddress,null,"GET");
           ArrayList<Banner> bannerList = parseJson(json);

           bundle.putParcelableArrayList("result",bannerList);
           message.what = Constant.LOOP;

        }else{
            message.what = Constant.ERROR;
        }
        mHandler.sendMessage(message);
    }

    /**
     * 解析轮播图的信息
     * @param json
     * @return ArrayList<Banner>
     */
    private ArrayList<Banner> parseJson(String json){
        ArrayList<Banner> list = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.optJSONArray("data");

            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject object = jsonArray.getJSONObject(i);

                Banner banner = new Banner();
                banner.setId(object.optString("id"));
                banner.setImagePath(object.optString("imagePath"));
                banner.setTitle(object.optString("title"));
                banner.setUrl(object.optString("url"));

                list.add(banner);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return list;
    }


}
