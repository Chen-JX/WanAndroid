package com.CJX.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.CJX.MyApplication;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnectionUtil {
    private static final String TAG = "HttpConnectionUtil";

    /**
     * 发起Http请求从服务器获得数据
     * @param path:访问路径
     * @param request：如果是POST，则request是请求内容，若是GET，则为null
     * @param method：请求方式
     * @return String
     */
    public static String sendHttpRequest(String path, String request, String method) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(path);

            connection = (HttpURLConnection) url.openConnection();
            if(method.equals("GET")){
                connection.setRequestMethod("GET");
            }else{
                connection.setRequestMethod("POST");
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(request);
            }
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            InputStream in = connection.getInputStream();

            //对获取到的输入流进行读取
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 检查网络是否连接正常
     * @return
     */
    public static Boolean checkNetWork(){
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

    /**
     * 通过url，从服务器端获取图片数据
     * @param path：图片路径
     * @return Bitmap
     */
    public static Bitmap getImage(String path){
        HttpURLConnection connection = null;

        try{
            URL url = new URL(path);
            connection =(HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            Bitmap bit= BitmapFactory.decodeStream(connection.getInputStream());

            return bit;

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }




}
