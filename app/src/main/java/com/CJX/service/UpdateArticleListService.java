package com.CJX.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.CJX.bean.HomePageArticle;
import com.CJX.db.HomeArticelDatabase;
import com.CJX.util.HttpConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdateArticleListService extends Service {
//    private static final int FRIST_ARTICLE = 1;
    public UpdateArticleListService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 5 * 60 * 1000;//5分钟更新一次
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, UpdateArticleListService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        alarmManager.cancel(pi);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            HomePageArticle homePageArticle = (HomePageArticle) bundle.get("article");

            //如果数据更新，则发出通知
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            Notification notification = new NotificationCompat.Builder(this,)
        }
    };
    //更新数据
    //TODO 得到数据库中的第一条数据，将他与最新更新的数据的link对比，
    private void updateData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取数据库中已缓存的页码
                HomeArticelDatabase database = new HomeArticelDatabase();
                int page = database.getPage();
                String pLink = null;//之前的网络网址
                String nLink = null;//从网络中获取的网址
                ArrayList<HomePageArticle> articles = null;

                //获取之前存储的数据
                SharedPreferences pref = getSharedPreferences("linkData",MODE_PRIVATE);
                pLink = pref.getString("link","");

                //获取从网络获取的数据
                if(HttpConnectionUtil.checkNetWork()){
                    String url = "https://www.wanandroid.com/article/list/"+ 0 +"/json";
                    String articleJson = HttpConnectionUtil.sendHttpRequest(url,"","GET");
                    articles = parseJson(articleJson);
                    nLink = articles.get(0).getLink();

                }
                //如果数据更新
                if(!(pLink.equals(nLink))){
                    database.updateDataInDatabase(articles);

                    Message message = Message.obtain();
//                    message.what = FRIST_ARTICLE;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("article",articles.get(0));
                    message.setData(bundle);
                    handler.sendMessage(message);

                    for(int i = 1; (i + 1) <= page; i++) {
                        if(HttpConnectionUtil.checkNetWork()){
                            String url = "https://www.wanandroid.com/article/list/"+ i +"/json";
                            String articleJson = HttpConnectionUtil.sendHttpRequest(url,"","GET");
                            ArrayList<HomePageArticle> articlesList = parseJson(articleJson);

                            database.updateDataInDatabase(articlesList);
                        }
                    }

                }

//                // 通过网络连接获取数据
//                for(int i = 1; (i + 1) <= page; i++) {
//                    if(HttpConnectionUtil.checkNetWork()){
//                        String url = "https://www.wanandroid.com/article/list/"+ i +"/json";
//                        String articleJson = HttpConnectionUtil.sendHttpRequest(url,"","GET");
//                        articles = parseJson(articleJson);
//
//                    }
//                }
//                if(HttpConnectionUtil.checkNetWork()){
//                    String url = "https://www.wanandroid.com/article/list/"+ 0 +"/json";
//                    String articleJson = HttpConnectionUtil.sendHttpRequest(url,"","GET");
//                }

            }
        }).start();
    }

    private ArrayList<HomePageArticle> parseJson(String json){
        ArrayList<HomePageArticle> homePageArticles = new ArrayList<HomePageArticle>();
        try{
            JSONObject firstObject = new JSONObject(json);
            JSONObject dataObject = firstObject.getJSONObject("data");

            String page = dataObject.optString("curPage");

            JSONArray jsonArray = dataObject.optJSONArray("datas");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject childObject = jsonArray.getJSONObject(i);

                HomePageArticle homePageArticle = new HomePageArticle();

                homePageArticle.setAuthor(childObject.optString("author"));
                homePageArticle.setChapterName(childObject.optString("chapterName"));
                homePageArticle.setSuperChapterName(childObject.optString("superChapterName"));
                homePageArticle.setLink(childObject.optString("link"));
                homePageArticle.setNiceDate(childObject.optString("niceDate"));
                homePageArticle.setTitle(childObject.optString("title"));
                homePageArticle.setPage(page);

                homePageArticles.add(homePageArticle);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return homePageArticles;
    }
//    }
}
