package com.CJX.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup{
    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);//得到容器的宽度
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);//得到测量模式

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content
        int width = 0;//用来设置布局应该设置的宽度
        int height = 0;

        //得到每一行的高度和宽度
        int lineHeight = 0;
        int lineWidth = 0;

        int cCount = getChildCount();//得到一共有多少子控件

        for(int i = 0 ; i < cCount ; i++){
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec,heightMeasureSpec);

            //得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            //要进行换行操作
            if(childWidth + lineWidth > sizeWidth){
                width = Math.max(lineWidth, childWidth);
                lineWidth = childWidth;//换行之后，重新开始记录新的一行的宽度

                height += lineHeight;//将这一行的高度累加
                lineHeight = childHeight;

            }else{
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight,childHeight);
            }

            //如果是最后一个子View，将当前记录的最大宽度和当前的lineWidth做比较

            if(i == cCount - 1){
                width = Math.max(lineWidth,childWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    private List<List<View>> mAllView = new ArrayList<List<View>>();
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    /**
     * 控制每一个子View的位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllView.clear();
        mLineHeight.clear();

        int width = getWidth();//当前布局的宽度

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();//存放

        int cCount = getChildCount();
        for(int i = 0 ; i < cCount ; i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //如果需要换行
            if(childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width ){

                mLineHeight.add(lineHeight);//记录每一行的高度

                mAllView.add(lineViews);//当前行的View

                lineWidth = 0;//重置行宽
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;//下一行的高度

                lineViews = new ArrayList<View>();

            }
                lineWidth += childWidth + lp.rightMargin + lp.leftMargin;
                lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
                lineViews.add(child);

        }
        //对最后一行进行处理
        mLineHeight.add(lineHeight);
        mAllView.add(lineViews);


        int left = 0;
        int top = 0;

        int lineNum = mAllView.size();

        //对每一行进行处理
        for(int i = 0 ; i < lineNum ; i++){
            lineViews = mAllView.get(i);
            lineHeight = mLineHeight.get(i);

            //对每一行的每一个View进行处理
            for(int j = 0 ; j < lineViews.size() ; j++){
                View child = lineViews.get(j);
                if(child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            }
            left = 0;
            top += lineHeight;
        }
    }

    /**
     * 找到与当前viewGroup对应的Params
     * @param attrs
     * @return
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
