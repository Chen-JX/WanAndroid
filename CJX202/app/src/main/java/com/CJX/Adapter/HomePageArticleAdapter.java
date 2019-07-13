package com.CJX.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.CJX.bean.HomePageArticle;
import com.CJX.util.CommonAdapter;
import com.CJX.util.ViewHolder;
import com.example.cjx20.R;

import java.util.List;

public class HomePageArticleAdapter extends CommonAdapter<HomePageArticle> {


    public HomePageArticleAdapter(Context context, int layoutResourceId, List<HomePageArticle> data) {
        super(context,data, layoutResourceId);
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = ViewHolder.getViewHolder(getContext(),parent,R.layout.activity_main_home_page_article_item,position,convertView);

        TextView title = viewHolder.getView(R.id.tv_title);
        TextView author = viewHolder.getView(R.id.tv_author);
        TextView chapterNameView = viewHolder.getView(R.id.tv_chapterName);
        TextView superChapterNameView = viewHolder.getView(R.id.tv_superChapterName);
        TextView dateView = viewHolder.getView(R.id.tv_date);

        title.setText(mData.get(position).getTitle());
        author.setText(mData.get(position).getAuthor());
        chapterNameView.setText(mData.get(position).getChapterName());
        superChapterNameView.setText(mData.get(position).getSuperChapterName());
        dateView.setText(mData.get(position).getNiceDate());

        return viewHolder.getConvertView();
    }


}

