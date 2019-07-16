package com.CJX.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.CJX.MyApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpURLConnectionRequest implements HttpRequest {

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

    @Override
    public String getJson(String path, String requestLimit, String method) {
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
                out.writeBytes(requestLimit);
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

    @Override
    public Bitmap getImage(String request) {
        HttpURLConnection connection = null;

        try{
            URL url = new URL(request);
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
