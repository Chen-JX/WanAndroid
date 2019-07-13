package com.CJX.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.CJX.bean.HomePageArticle;
import com.CJX.util.CommonAdapter;
import com.CJX.util.ViewHolder;
import com.example.cjx20.R;

import java.util.List;
//搜索结果的适配器
public class SearchArticleAdapter extends CommonAdapter<HomePageArticle> {

    public SearchArticleAdapter(Context context, int layoutResourceId, List<HomePageArticle> data) {
        super(context, data, layoutResourceId);


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext,parent,R.layout.activity_main_home_page_article_item,position,convertView);

        TextView title = viewHolder.getView(R.id.tv_title);
        TextView author = viewHolder.getView(R.id.tv_author);
        TextView chapterNameView = viewHolder.getView(R.id.tv_chapterName);
        TextView superChapterNameView = viewHolder.getView(R.id.tv_superChapterName);
        TextView dateView = viewHolder.getView(R.id.tv_date);

        title.setText(Html.fromHtml(mData.get(position).getTitle()));
        author.setText(mData.get(position).getAuthor());
        chapterNameView.setText(mData.get(position).getChapterName());
        superChapterNameView.setText(mData.get(position).getSuperChapterName());
        dateView.setText(mData.get(position).getNiceDate());

        return viewHolder.getConvertView();
    }


}

