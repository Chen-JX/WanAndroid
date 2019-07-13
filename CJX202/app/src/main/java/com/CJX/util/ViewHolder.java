package com.CJX.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
//    private int mPosition;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position){
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
//        mPosition = position;
        mConvertView.setTag(this);
    }

    public static ViewHolder getViewHolder(Context context, ViewGroup parent, int layoutId, int position, View convertView){
        if(convertView == null){
            return new ViewHolder(context,parent,layoutId,position);
        }
        else{
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
//            viewHolder.mPosition = position;
            return viewHolder;
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    public <T extends View>T getView(int viewId){
        if(mViews.get(viewId) == null){
            T view = (T)mConvertView.findViewById(viewId);
            mViews.append(viewId,view);
            return view;
        }else{
            return (T)mViews.get(viewId);
        }
    }
}
