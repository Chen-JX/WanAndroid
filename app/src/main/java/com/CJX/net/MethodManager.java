package com.CJX.net;

import android.graphics.Bitmap;

public class MethodManager {
    private HttpRequest httpRequest = new OKHttpConnectionRequest();

    public boolean checkNetWork(){

        return httpRequest.checkNetWork();
    }
    //获取Json数据
    public String getData(String path, String request, String method){
        return httpRequest.getJson(path, request, method);
    }

    //获取图片数据
    public Bitmap getBitmap(String request){
        return httpRequest.getImage(request);
    }




}
