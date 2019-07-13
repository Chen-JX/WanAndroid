package com.CJX.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonAdapter<T> extends ArrayAdapter {
    protected List<T> mData;
    protected Context mContext;
    protected int mLayoutResourceId;

    public CommonAdapter(Context context, List<T> data, int layoutResourceId ){
        super(context,layoutResourceId,data);
        this.mData = data;
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;

    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
