package com.CJX.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.CJX.bean.Constant;
import com.CJX.bean.SystemBean;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 获得项目的目录信息
 */
public class SystemCategoryServer extends Thread{
    private final String PATH = "https://www.wanandroid.com/tree/json";
    private Handler mHandler;
    public SystemCategoryServer(Handler handler){
        this.mHandler = handler;
    }
    @Override
    public void run() {
        Message message = Message.obtain();
        if(HttpConnectionUtil.checkNetWork()){
            String json = HttpConnectionUtil.sendHttpRequest(PATH,null,"GET");
            ArrayList<SystemBean> systemBeanArrayList = parseJson(json);

            Bundle bundle = new Bundle();
            message.setData(bundle);
            bundle.putParcelableArrayList("result",systemBeanArrayList);

            message.what = Constant.CATEGORY;
        }else{
            message.what = Constant.ERROR;
        }
        mHandler.sendMessage(message);
    }

    /**
     * 解析项目目录JSON数据
     * @param json
     * @return ArrayList<SystemBean>
     */
    private ArrayList<SystemBean> parseJson(String json){
        ArrayList<SystemBean> systemBeanArrayList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("data");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject child = jsonArray.getJSONObject(i);

                List<SystemBean.twoCategory> categoryArrayList = new ArrayList<>();

                //获得二级目录
                JSONArray childArray = child.optJSONArray("children");
                for(int j = 0 ; j < childArray.length(); j++){
                    JSONObject childObject = childArray.getJSONObject(j);
                    SystemBean.twoCategory twoCategory = new SystemBean.twoCategory();
                    twoCategory.setChildName(childObject.optString("name"));
                    twoCategory.setId(childObject.optString("id"));
                    categoryArrayList.add(twoCategory);
                }

                SystemBean systemBean = new SystemBean();
                systemBean.setName(child.optString("name"));
                systemBean.setTwoCategoryList(categoryArrayList);

                systemBeanArrayList.add(systemBean);

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return systemBeanArrayList;
    }
}
