package com.CJX.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.CJX.bean.Constant;
import com.CJX.server.SearchServer;
import com.CJX.view.SearchResultActivity;
import com.example.cjx20.R;


public class SearchViewLayout extends LinearLayout {
    private String mQuery;
    private int mPage;
    public SearchViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_main_search_view, this);//动态的加入布局文件
        SearchView searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           //单击搜索按钮时触发
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchServer searchServer = new SearchServer(query,handler,0);
                mQuery = query;
                mPage = 0;
                searchServer.start();
                return true;
            }
            //输入字符时触发
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case  Constant.ARTICLE:
                    Bundle bundle = msg.getData();
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.putExtra("page",mPage);
                    intent.putExtra("query",mQuery);
                    intent.setClass(getContext(),SearchResultActivity.class);
                    getContext().startActivity(intent);
                    break;
                case Constant.NULL:
                    break;
                case Constant.ERROR:
                    break;
            }
        }
    };

}
