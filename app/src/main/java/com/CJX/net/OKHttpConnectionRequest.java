package com.CJX.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.CJX.MyApplication;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpConnectionRequest implements HttpRequest {
    @Override
    public boolean checkNetWork() {
        Context context = MyApplication.getContext();
        if(context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取系统连接服务
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isConnected();
            }
        }
        return false;
    }

    //获取JSON数据
    @Override
    public String getJson(String path, String requestLimit, String method) {
        String data = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request;
        //Response response;
        try{
            if(method.equals("GET")){
                request = new Request.Builder().url(path).build();
            }else{
                RequestBody requestBody = new FormBody.Builder().add("",requestLimit).build();
                request = new Request.Builder().url(path).post(requestBody).build();
            }
            Response response = okHttpClient.newCall(request).execute();
            data = response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }

        return data;
    }

    //得到图片
    @Override
    public Bitmap getImage(String path) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(path).build();
        Bitmap bitmap = null;
        try{
            Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            bitmap = BitmapFactory.decodeStream(is);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
