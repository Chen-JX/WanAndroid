package com.CJX.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.CJX.Adapter.SystemArticleAdapter;
import com.CJX.bean.Constant;
import com.CJX.bean.HomePageArticle;
import com.CJX.bean.SystemInformationBean;
import com.CJX.server.SearchServer;
import com.CJX.server.SystemArticleServer;
import com.CJX.widget.PullToRefreshListView;
import com.example.cjx20.R;

import java.util.ArrayList;
import java.util.List;

public class SystemArticleActivity extends AppCompatActivity implements PullToRefreshListView.LoadListener{
    private String mId;
    private int mPage = 0;
    private final String TAG = "SearchResultActivity";
    private PullToRefreshListView mListView;
    private List<SystemInformationBean> mArticles = new ArrayList<>();
    private SystemArticleAdapter mSystemArticleAdapter;

    private int mode = 0;
    private int index;
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       this.mId = this.getIntent().getStringExtra("id");
       new SystemArticleServer(handler,mPage,mId).start();//获取数据

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_system_article);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case Constant.ARTICLE:
                    Bundle bundle = msg.getData();
                    ArrayList<SystemInformationBean> systemInformationBeanArrayList =  bundle.getParcelableArrayList("result");
                    for(int i = 0; i<systemInformationBeanArrayList.size() ;i++){
                        if(mode == 0)
                            mArticles.add(i,systemInformationBeanArrayList.get(i));
                        else if(mode == 1&&!systemInformationBeanArrayList.get(i).getLink().equals(mArticles.get(i).getLink()))
                            mArticles.add(0,systemInformationBeanArrayList.get(i));
                        else if(mode == -1){
                            mArticles.add(mArticles.size(),systemInformationBeanArrayList.get(i));
                            mListView.setSelectionFromTop(index,offset);
                        }

                    }
                    initListView();
                    break;
                    case Constant.ERROR:
                        Toast.makeText(getApplicationContext(),"网络连接已断开，请检查网络",Toast.LENGTH_LONG).show();
                        break;
                        case Constant.NULL:

                            break;
            }
        }
    };

    @Override
    public void onLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mode = 1;
                SystemArticleServer systemArticleServer = new SystemArticleServer(handler,mPage,mId);
                systemArticleServer.start();
                // 这里处理请求返回的结果（这里使用模拟数据）
                //list.add(0, "这是下拉刷新添加的数据");
                // 更新数据

                mSystemArticleAdapter.notifyDataSetChanged();
                // 加载完毕
                mListView.loadComplete();
            }
        }, 3000);
    }

    @Override
    public void PullLoad() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                index = mListView.getFirstVisiblePosition();
                offset = mListView.getChildAt(0).getTop();

                mode = -1;
                SystemArticleServer systemArticleServer = new SystemArticleServer(handler,mPage,mId);
                systemArticleServer.start();
                mSystemArticleAdapter.notifyDataSetChanged();
                // 加载完毕
                mListView.loadComplete();

            }
        }, 3000);

    }

    private void initListView(){
        this.mListView = (PullToRefreshListView)findViewById(R.id.system_article_list);
        mListView.setInterface(this);
        mSystemArticleAdapter = new SystemArticleAdapter(getApplicationContext(),
                R.layout.activity_main_system_artical_list,mArticles);
        mListView.setAdapter(mSystemArticleAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mArticles.get(position-1).getLink();
                Log.d(TAG, "-------------------->url=" + url);

                Intent intent = new Intent();
                intent.putExtra("url",url);
                intent.setClass(getApplicationContext(), WebActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });
    }
}
