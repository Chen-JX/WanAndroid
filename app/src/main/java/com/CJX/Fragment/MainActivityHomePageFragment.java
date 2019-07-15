package com.CJX.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.CJX.Adapter.HomePageArticleAdapter;
import com.CJX.Adapter.LooperPagerAdapter;
import com.CJX.bean.Banner;
import com.CJX.bean.Constant;
import com.CJX.bean.HomePageArticle;
import com.CJX.server.ArticleServer;
import com.CJX.server.ImageServer;
import com.CJX.server.LooperImageServer;
import com.CJX.view.WebActivity;
import com.CJX.widget.PullToRefreshListView;
import com.example.cjx20.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
public class MainActivityHomePageFragment extends Fragment implements PullToRefreshListView.LoadListener {
    private final String TAG = "HomePageFragment";
    private View mView;
    private HomePageArticleAdapter mHomePageArticleAdapter;
    private List<HomePageArticle> listView  = new ArrayList<HomePageArticle>();
    private PullToRefreshListView mPullToRefreshListView;
    private ArrayList<Banner> mBannerArrayList;
    private int mPosition = 0;
    private int mMode = 0;
    private int mOffset;
    private int mIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_main_home_page, container, false);
        //获取轮播图信息
        getLopperImageData();
        getArticleData();
        return mView;
    }
   @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what){
                case Constant.LOOP://获取轮播图信息
                    // ArrayList<Banner> bannerArrayList = bundle.getParcelableArrayList("result");
                    mBannerArrayList = bundle.getParcelableArrayList("result");
                    Log.d(TAG, "--------------->handleMessage: ");
                    Log.d(TAG, mBannerArrayList.get(0).getTitle());
                    initLooperPager(mBannerArrayList);

                    break;
                case Constant.LIST:
                    ArrayList<HomePageArticle> articleList = bundle.getParcelableArrayList("result");

                    for(int i = 0; i<articleList.size() ;i++){
                        if(mMode == 0)
                            listView.add(i,articleList.get(i));
                        else if(mMode == 1&&!articleList.get(i).getLink().equals(listView.get(i).getLink()))
                            listView.add(0,articleList.get(i));
                        else if(mMode == -1)
                            listView.add(listView.size(),articleList.get(i));
                    }
                    initList();
                    if(mMode == -1)
                        mPullToRefreshListView.setSelectionFromTop(mIndex,mOffset);
                    break;

                case Constant.ERROR:
                    Toast.makeText(getContext(),"网络连接已断开，请检查网络",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    /**
     * 初始化文章列表信息
     */
    private void initList(){
        mPullToRefreshListView = mView.findViewById(R.id.list_view);
        mPullToRefreshListView.setInterface(this);
        mHomePageArticleAdapter = new HomePageArticleAdapter(mView.getContext(),R.layout.activity_main_home_page_article_item,listView);

        mPullToRefreshListView.setAdapter(mHomePageArticleAdapter);
        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  url = listView.get(i).getLink();
                Intent intent = new Intent();

                intent.putExtra("url",url);
                intent.setClass(getContext(), WebActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * 初始化首页轮播图
     * @param list
     */
    private void initLooperPager(List<Banner> list){
        LooperPagerAdapter looperPagerAdapter = new LooperPagerAdapter();
        final ViewPager viewPager = mView.findViewById(R.id.loop_pager);
        viewPager.setAdapter(looperPagerAdapter);
        looperPagerAdapter.setData(list,getContext());
        looperPagerAdapter.notifyDataSetChanged();

        //实现无限轮播
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();//得到当前轮播图
                viewPager.setCurrentItem(++currentItem,false);
                handler.postDelayed(this,3000);
            }
        };
        handler.post(runnable);
    }

    /**
     * 得到首页轮播图的信息
     */
    public void getLopperImageData(){
        LooperImageServer looperImageServer = new LooperImageServer(handler);
        looperImageServer.start();
    }

    /**
     * 通过后台得到首页文章列表信息
     */
    public void getArticleData(){
        ArticleServer articleServer = new ArticleServer(mPosition++,handler);
        articleServer.start();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void PullLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                mMode = 1;
                ArticleServer articleServer = new ArticleServer(0,handler);
                articleServer.start();
                mHomePageArticleAdapter.notifyDataSetChanged();
                // 加载完毕
                mPullToRefreshListView.loadComplete();
            }
        }, 3000);

    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mIndex = mPullToRefreshListView.getFirstVisiblePosition();
                mOffset = mPullToRefreshListView.getChildAt(0).getTop();
                Log.d(TAG, "run: ------------>offset = " + mOffset);
                mMode = -1;
                ArticleServer articleServer = new ArticleServer(mPosition++,handler);
                articleServer.start();
                mHomePageArticleAdapter.notifyDataSetChanged();
                // 加载完毕
                mPullToRefreshListView.loadComplete();

            }
        }, 3000);
    }

}
