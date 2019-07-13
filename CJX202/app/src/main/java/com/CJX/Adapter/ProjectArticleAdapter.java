package com.CJX.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.CJX.bean.ProjectArticle;
import com.CJX.util.CommonAdapter;
import com.CJX.util.HttpConnectionUtil;
import com.CJX.util.ViewHolder;
import com.example.cjx20.R;

import java.util.List;

public class ProjectArticleAdapter extends CommonAdapter<ProjectArticle> {

    public ProjectArticleAdapter(Context context, int textViewResourceId, List<ProjectArticle> object){
        super(context,object,textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder = ViewHolder.getViewHolder(mContext,parent,R.layout.activity_main_project_article_item,position,convertView);

        TextView author = (TextView) viewHolder.getView(R.id.tv_project_author);
        TextView date = (TextView) viewHolder.getView(R.id.tv_project_date);
        TextView desc = (TextView) viewHolder.getView(R.id.tv_project_desc);
        TextView title = (TextView) viewHolder.getView(R.id.tv_project_title);
        ImageView image = (ImageView) viewHolder.getView(R.id.image_project_cover);

        title.setText(mData.get(position).getTitle());
        author.setText(mData.get(position).getAuthor());
        date.setText(mData.get(position).getNiceDate());
        desc.setText(mData.get(position).getDesc());
        new MyTask(image).execute(mData.get(position).getEnvelopePic());

        return viewHolder.getConvertView();
    }

    class MyTask extends AsyncTask<String, Void, Bitmap>{
        private ImageView imageView;
        public MyTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            if(HttpConnectionUtil.checkNetWork()){
                bitmap = HttpConnectionUtil.getImage(strings[0]);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
