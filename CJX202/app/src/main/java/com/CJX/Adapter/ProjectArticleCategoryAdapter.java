package com.CJX.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.CJX.bean.ProjectArticleCategory;
import com.CJX.util.CommonAdapter;
import com.CJX.util.ViewHolder;
import com.example.cjx20.R;

import java.util.List;
//项目文章
public class ProjectArticleCategoryAdapter extends CommonAdapter<ProjectArticleCategory> {

    public ProjectArticleCategoryAdapter(Context context, List<ProjectArticleCategory> data, int textViewResourceId){
        super(context,data,textViewResourceId);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder = ViewHolder.getViewHolder(mContext,parent, R.layout.activity_main_project_category_item,position,convertView);

        TextView category = viewHolder.getView(R.id.tv_project_category_item);
        category.setText(mData.get(position).getName());

        return viewHolder.getConvertView();
    }
}
