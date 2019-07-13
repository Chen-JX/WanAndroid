package com.CJX.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.CJX.Adapter.ProjectArticleAdapter;
import com.CJX.Adapter.ProjectArticleCategoryAdapter;
import com.CJX.bean.Constant;

import com.CJX.bean.ProjectArticle;
import com.CJX.bean.ProjectArticleCategory;
import com.CJX.server.ProjectArticleCategoryServer;
import com.CJX.server.ProjectArticleServer;
import com.CJX.server.SearchServer;
import com.CJX.view.WebActivity;
import com.CJX.widget.PullToRefreshListView;
import com.example.cjx20.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivityProjectFragment extends Fragment implements PullToRefreshListView.LoadListener{
    private View mView;
    private List<ProjectArticleCategory> mProjectArticleCategoryList = new ArrayList<>();//目录内容
    private List<ProjectArticle> mProjectArticle = new ArrayList<>();//文章列表数据
    private int mMode = 0;
    private int mOffset;
    private int mIndex;
    private int mPage = 1;
    private String ID;
    private PullToRefreshListView  pullToRefreshListView;//文章展示用的ListView
    ProjectArticleAdapter projectArticleAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_main_project, container, false);
        startThread();
        return  mView;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what){
                case Constant.CATEGORY:
                    List<ProjectArticleCategory> projectArticleCategoryList = bundle.getParcelableArrayList("result");
                    mProjectArticleCategoryList.addAll(projectArticleCategoryList);
                    initCategory(projectArticleCategoryList);
                    break;

                    case Constant.ARTICLE:
                        List<ProjectArticle> projectArticles = bundle.getParcelableArrayList("result");

                        for(int i = 0; i<projectArticles.size() ;i++){
                            if(mMode == 0)
                                mProjectArticle.add(i,projectArticles.get(i));
                            else if(mMode == 1&&!projectArticles.get(i).getLink().equals(mProjectArticle.get(i).getLink()))
                                mProjectArticle.add(0,projectArticles.get(i));
                            else if(mMode == -1)
                                mProjectArticle.add(mProjectArticle.size(),projectArticles.get(i));
                        }

                        initArticle();
                        if(mMode == -1)
                            pullToRefreshListView.setSelectionFromTop(mIndex,mOffset);

                        break;
                    case Constant.ERROR:
                        Toast.makeText(getContext(),"网络连接已断开，请检查网络",Toast.LENGTH_LONG).show();
                        break;
            }
        }
    };
    //开启线程
    public void startThread(){
        new ProjectArticleCategoryServer(handler).start();
    }

    /**
     * 初始化项目分类目录列表
     * @param list
     */
    public void initCategory(List<ProjectArticleCategory> list){
        ListView listView = mView.findViewById(R.id.list_project_type);
        ProjectArticleCategoryAdapter projectArticleCategoryAdapter = new ProjectArticleCategoryAdapter(getContext(),list,R.layout.activity_main_project_category_item);
        listView.setAdapter(projectArticleCategoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ID = mProjectArticleCategoryList.get(position).getId();
                new ProjectArticleServer(handler,ID,1).start();//获取到每个分类下的文章
            }
        });
    }

    /**
     * 初始化项目文章列表
     */
    public void initArticle(){
        pullToRefreshListView = mView.findViewById(R.id.list_project_article);
        projectArticleAdapter = new ProjectArticleAdapter(getContext(),R.layout.activity_main_project_article_item,mProjectArticle);
        pullToRefreshListView.setInteface(this);
        pullToRefreshListView.setAdapter(projectArticleAdapter);

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mProjectArticle.get(position).getLink();

                Intent intent = new Intent();

                intent.putExtra("url",url);
                intent.setClass(getContext(), WebActivity.class);
                getContext().startActivity(intent);
            }
        });

    }

    /**
     * 实现下拉刷新
     */
    public void PullLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMode = 1;
                ProjectArticleServer projectArticleServer = new ProjectArticleServer(handler,ID,mPage);
                projectArticleServer.start();
                //list.add(0, "这是下拉刷新添加的数据");
                // 更新数据
                projectArticleAdapter.notifyDataSetChanged();
                // 加载完毕
                pullToRefreshListView.loadComplete();
            }
        }, 3000);

    }

    /**
     * 实现上拉加载
     */
    @Override
    public void onLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mIndex =  pullToRefreshListView.getFirstVisiblePosition();
                mOffset =  pullToRefreshListView.getChildAt(0).getTop();

                mMode = -1;
                ProjectArticleServer searchServer = new ProjectArticleServer(handler,ID,++mPage);
                searchServer.start();
                projectArticleAdapter.notifyDataSetChanged();
                // 加载完毕
                pullToRefreshListView.loadComplete();

            }
        }, 3000);
    }
}
