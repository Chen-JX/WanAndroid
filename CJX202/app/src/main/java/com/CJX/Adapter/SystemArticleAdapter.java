package com.CJX.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.CJX.bean.HomePageArticle;
import com.CJX.bean.SystemInformationBean;
import com.CJX.util.CommonAdapter;
import com.CJX.util.ViewHolder;
import com.example.cjx20.R;

import java.util.ArrayList;
import java.util.List;
//体系文章信息的适配器
public class SystemArticleAdapter extends CommonAdapter<SystemInformationBean> {


    public SystemArticleAdapter(Context context,  int layoutResourceId,List<SystemInformationBean> data){
        super(context, data, layoutResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext,parent, R.layout.activity_main_system_artical_list,position,convertView);

        TextView title = viewHolder.getView(R.id.tv_system_article_title);
        TextView author = viewHolder.getView(R.id.tv_system_author);
        TextView date = viewHolder.getView(R.id.tv_system_date);

        title.setText(mData.get(position).getTitle());
        author.setText(mData.get(position).getAuthor());
        date.setText(mData.get(position).getNiceDate());

        return viewHolder.getConvertView();
    }
}
