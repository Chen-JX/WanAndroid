package com.CJX.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.CJX.MyApplication;
import com.CJX.bean.Banner;
import com.CJX.server.ImageServer;
import com.CJX.util.HttpConnectionUtil;
import com.CJX.view.MainActivity;
import com.CJX.view.WebActivity;


import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 轮播图的适配器
 */
public class LooperPagerAdapter extends PagerAdapter {

    private List<Banner> mBannerList = new ArrayList<>();
   // private List<Bitmap> mBitmaps = new ArrayList<>();
    private Context context;

    //设置数据
    public void setData(List<Banner> listBanner, Context context){
        mBannerList.clear();//清空之前的数据
        mBannerList.addAll(listBanner);
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mBannerList.size() != 0)
            return Integer.MAX_VALUE;//实现无限轮播
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
       return view==o;
    }

    //初始化
    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        int realPosition = position%mBannerList.size();
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //imageView.setImageBitmap(mBitmaps.get(realPosition));
        new MyTask(imageView).execute(mBannerList.get(realPosition).getImagePath());
        container.addView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  url = mBannerList.get(position).getUrl();
                Intent intent = new Intent();

                intent.putExtra("url",url);
                intent.setClass(context, WebActivity.class);
                (context).startActivity(intent);
            }
        });

        return imageView;
    }
    //销毁，避免内存溢出，从而可以循环播放
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       // container.removeView((View) object);
    }

    /**
     * 对图片进行异步加载
     */
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
