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
    private View bottomView; //尾文件
    private View headView; //头文件
    private int totalItemCounts;//用于表示是下拉还是上拉
    private int lassVisible; //上拉
    private int firstVisible; //下拉
    private LoadListener loadListener; //接口回调
    private int bottomHeight;//尾文件高度
    private int headHeight; //头文件高度
    private int YLoad;//位置
    boolean isLoading;//加载状态
    private TextView headText;//头文件textView显示加载文字
    private ProgressBar progressBar;//加载进度

    public PullToRefreshListView(Context context) {
        this(context,null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,   0   );
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        //拿到头布局文件xml
        headView= LinearLayout.inflate(context, R.layout.listview_head, null);
        headText=(TextView) headView.findViewById(R.id.tv_head);
        progressBar=(ProgressBar) headView.findViewById(R.id.progress_head);

        //拿到尾布局文件
        bottomView = LinearLayout.inflate(context, R.layout.listview_bottom, null);
        //测量尾文件高度
        bottomView.measure(0,0);//包括测量模式和测量值
        //拿到高度
        bottomHeight=bottomView.getMeasuredHeight();
        //隐藏view
        bottomView.setPadding(0, -bottomHeight, 0, 0);
        headView.measure(0, 0);
        headHeight=headView.getMeasuredHeight();
        headView.setPadding(0,-headHeight, 0, 0);
        //添加listView底部
        this.addFooterView(bottomView);
        //添加到listView头部
        this.addHeaderView(headView);
        //设置拉动监听
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                YLoad = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int paddingY = headHeight + (moveY - YLoad)/2;
                if(paddingY < 0){
                    headText.setText("下拉刷新........");
                    progressBar.setVisibility(View.GONE);
                }
                if(paddingY > 0){
                    headText.setText("松开刷新........");
                    progressBar.setVisibility(View.GONE);
                }
                headView.setPadding(0, paddingY, 0, 0);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 对滑动状态进行监听
     * @param view
     * @param scrollState
     */
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(totalItemCounts == lassVisible && scrollState == SCROLL_STATE_IDLE){
            if(!isLoading){
                isLoading=true;
                bottomView.setPadding(0, 0, 0, 0);
                //加载数据
                loadListener.onLoad();
            }
        }

        if(firstVisible == 0 && scrollState == SCROLL_STATE_IDLE){
            headView.setPadding(0, 0, 0, 0);
            headText.setText("正在刷新.......");
            progressBar.setVisibility(View.VISIBLE);
            loadListener.PullLoad();
        }
    }

    /**
     * 上拉加载，下拉刷新的事件的具体处理的接口
     */
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

    /**
     * 加载完成后上下底布局再次省略
     */
    public void loadComplete(){
        isLoading=false;
        bottomView.setPadding(0, -bottomHeight, 0, 0);
        headView.setPadding(0, -headHeight, 0, 0);
    }

    /**
     * 设置接口
     * @param loadListener
     */
    public void setInterface(LoadListener loadListener){
        this.loadListener=loadListener;
    }


}
