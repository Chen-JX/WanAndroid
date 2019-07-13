package com.CJX.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cjx20.R;

public class PullToRefreshListView extends ListView implements OnScrollListener {
    private View mBottomView; //尾文件
    private View mHeadView; //头文件
    private int totalItemCounts;//用于表示是下拉还是上拉
    private int lassVisible; //上拉
    private int firstVisible; //下拉
    private LoadListener loadListener; //接口回调
    private int bottomHeight;//尾文件高度
    private int headHeight; //头文件高度
    private int YLoad;//位置
    boolean isLoading;//加载状态
    private TextView headText;//头文件textView显示加载文字
//    private TextView headTime;//头文件textView显示加载时间
    private ProgressBar progressBar;//加载进度

    public PullToRefreshListView(Context context) {
        super(context,null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs,   0   );
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        //拿到头布局文件xml
        mHeadView= LinearLayout.inflate(context, R.layout.listview_head, null);
        headText=(TextView) mHeadView.findViewById(R.id.headtxt);
//        headTime=(TextView) mHeadView.findViewById(R.id.timetxt);
        progressBar=(ProgressBar) mHeadView.findViewById(R.id.headprogress);
//        headTime.setText("上次更新时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

        //拿到尾布局文件
        mBottomView = LinearLayout.inflate(context, R.layout.listview_bottom, null);
        //测量尾文件高度
        mBottomView.measure(0,0);
        //拿到高度
        bottomHeight=mBottomView.getMeasuredHeight();
        //隐藏view
        mBottomView.setPadding(0, -bottomHeight, 0, 0);
        mHeadView.measure(0, 0);
        headHeight=mHeadView.getMeasuredHeight();
        mHeadView.setPadding(0,-headHeight, 0, 0);
        //添加listView底部
        this.addFooterView(mBottomView);
        //添加到listView头部
        this.addHeaderView(mHeadView);
        //设置拉动监听
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                YLoad=(int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int moveY=(int) ev.getY();
                int paddingY=headHeight+(moveY-YLoad)/2;
                if(paddingY<0){
                    headText.setText("下拉刷新........");
                    progressBar.setVisibility(View.GONE);
                }
                if(paddingY>0){
                    headText.setText("松开刷新........");
                    progressBar.setVisibility(View.GONE);
                }
                mHeadView.setPadding(0, paddingY, 0, 0);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(totalItemCounts == lassVisible && scrollState == SCROLL_STATE_IDLE){
            if(!isLoading){
                isLoading=true;
                mBottomView.setPadding(0, 0, 0, 0);
                //加载数据
                loadListener.onLoad();
            }
        }
        Log.i("TGA", "firstVisible----"+firstVisible);
        Log.i("TGA", "状态？"+(firstVisible==0));

        if(firstVisible == 0 && scrollState == SCROLL_STATE_IDLE){
            mHeadView.setPadding(0, 0, 0, 0);
            headText.setText("正在刷新.......");
            progressBar.setVisibility(View.VISIBLE);
            loadListener.PullLoad();
        }
    }

    //接口回调
    public interface LoadListener{
        void onLoad();
        void PullLoad();
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        this.firstVisible=firstVisibleItem;
        this.lassVisible=firstVisibleItem+visibleItemCount;
        this.totalItemCounts=totalItemCount;
    }

    //加载完成
    public void loadComplete(){
        isLoading=false;
        mBottomView.setPadding(0, -bottomHeight, 0, 0);
        mHeadView.setPadding(0, -headHeight, 0, 0);
    }

    public void setInteface(LoadListener loadListener){
        this.loadListener=loadListener;
    }


}



