package com.CJX.server;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.CJX.bean.Constant;
import com.CJX.util.HttpConnectionUtil;

import java.util.ArrayList;
import java.util.List;
//通过图片的URL加载得到图片
public class ImageServer extends Thread {
    private final String TAG = "ImageServer";


    private List<String> mUrl;
    private Handler mHandler;


    public ImageServer(List<String> url, Handler handler){
        this.mUrl = url;
        this.mHandler = handler;
    }
    @Override
    public void run() {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        message.setData(bundle);
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        if(HttpConnectionUtil.checkNetWork()){
            for(int i = 0 ; i < mUrl.size() ; i++){
                if(!HttpConnectionUtil.checkNetWork()){
                    message.what = Constant.ERROR;
                    mHandler.sendMessage(message);
                }
                Bitmap bitmap = HttpConnectionUtil.getImage(mUrl.get(i));
                bitmaps.add(bitmap);

            }
            message.what = Constant.IMAGE;
            bundle.putParcelableArrayList("result",bitmaps);
//            mHandler.sendMessage(message);
        }
        else{
            message.what = Constant.ERROR;
        }
        mHandler.sendMessage(message);
    }

}
