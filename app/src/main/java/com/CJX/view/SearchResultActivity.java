package com.CJX.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.CJX.Adapter.SearchArticleAdapter;
import com.CJX.bean.Constant;
import com.CJX.bean.HomePageArticle;
import com.CJX.server.SearchServer;
import com.CJX.widget.PullToRefreshListView;
import com.example.cjx20.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过搜索框搜索得到的界面
 */
public class SearchResultActivity extends AppCompatActivity implements PullToRefreshListView.LoadListener {
    private final String TAG = "SearchResultActivity";
    private PullToRefreshListView mListView;
    private List<HomePageArticle> mArticles;
    private SearchArticleAdapter mHomePageArticleAdapter;
    private String query;
    private int page;//当前搜索结果所处页码

    private int mode;
    private int index;
    private int offset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        mArticles = bundle.getParcelableArrayList("result");
        query = this.getIntent().getStringExtra("query");
        page = this.getIntent().getIntExtra("page",0);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_search_result);

        mHomePageArticleAdapter = new SearchArticleAdapter(this,R.layout.activity_main_home_page_article_item,mArticles);
        mListView = this.findViewById(R.id.list_view_search_result);
        mListView.setInterface(this);
        mListView.setAdapter(mHomePageArticleAdapter);
    }


    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case  Constant.ARTICLE:
                    Bundle bundle = msg.getData();
                    ArrayList<HomePageArticle> articleList = bundle.getParcelableArrayList("result");

                    Log.d(TAG, "handleMessage: requests new data  :" + articleList.size());
                    for(int i = 0; i<articleList.size() ;i++) {
                        if(mode == 1){
                            if (!articleList.get(i).getLink().equals(mArticles.get(0).getLink()))
                                mArticles.add(0,articleList.get(i));
                            else
                                break;
                        }
                        else
                            mArticles.add(mArticles.size(),articleList.get(i));
                    }
                    init();
                    if(mode == -1)
                        mListView.setSelectionFromTop(index,offset);//设置偏移量
                    break;

                case Constant.ERROR:
                    Toast.makeText(getApplicationContext(),"网络连接已断开，请检查网络",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void init(){
        mHomePageArticleAdapter = new SearchArticleAdapter(this,R.layout.activity_main_home_page_article_item,mArticles);
        mListView = this.findViewById(R.id.list_view_search_result);
        mListView.setInterface(this);
        mListView.setAdapter(mHomePageArticleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  url = mArticles.get(i).getLink();
                Intent intent = new Intent();

                intent.putExtra("url",url);
                intent.setClass(getApplicationContext(), WebActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    /**
     * 上拉刷新
     */
    @Override
    public void PullLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mode = 1;
                SearchServer searchServer = new SearchServer(query,handler1,0);
                searchServer.start();
                mHomePageArticleAdapter.notifyDataSetChanged();
                // 加载完毕
                mListView.loadComplete();
            }
        }, 3000);
    }

    /**
     * 下拉加载更多
     */
    @Override
    public void onLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                index = mListView.getFirstVisiblePosition();//可以看到的当前的第一个item
                offset = mListView.getChildAt(0).getTop();

                mode = -1;
                SearchServer searchServer = new SearchServer(query,handler1,++page);
                searchServer.start();
                mHomePageArticleAdapter.notifyDataSetChanged();
                // 加载完毕
                mListView.loadComplete();

            }
        }, 3000);
    }

}
