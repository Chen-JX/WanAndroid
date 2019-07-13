package com.CJX.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.CJX.bean.Constant;
import com.CJX.bean.SystemBean;
import com.CJX.server.SystemCategoryServer;
import com.CJX.widget.FlowLayout;
import com.CJX.view.SystemArticleActivity;
import com.example.cjx20.R;

import java.util.ArrayList;
import java.util.List;
//体系数据
public class MainActivitySystemFragment extends Fragment {
    private final String TAG = "SystemFragment";
    private View mView;
    private ArrayList<SystemBean> mSystemBeanList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mView =inflater.inflate(R.layout.activity_main_system, container, false);
            new SystemCategoryServer(handler).start();
            return mView;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.CATEGORY:
                    Bundle bundle = msg.getData();
                    List<SystemBean> systemBeanArrayList = bundle.getParcelableArrayList("result");
                    mSystemBeanList.addAll(systemBeanArrayList);
                    init(systemBeanArrayList);
                    break;

                    case Constant.ERROR:
                        Toast.makeText(getContext(),"网络连接已断开，请检查网络",Toast.LENGTH_LONG).show();
                        break;

            }
        }
    };

    /**
     * 初始化体系的目录列表
     * @param systemBeans
     */
    private void init(List<SystemBean> systemBeans){

          LinearLayout linearLayout = mView.findViewById(R.id.total_View);

          LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        for(int i = 0 ; i < systemBeans.size(); i++){
            View eachView = layoutInflater.inflate(R.layout.activity_main_system_each_category,linearLayout,false);//每一个分类的布局
            FlowLayout flowLayout = eachView.findViewById(R.id.flow_layout);
            TextView title = eachView.findViewById(R.id.tv_system_title);
            title.setText(systemBeans.get(i).getName());

            for(int j = 0; j < (systemBeans.get(i).getTwoCategoryList()).size() ;j++){
                TextView tag =(TextView) layoutInflater.inflate(R.layout.activity_main_system_tag,flowLayout,false);//得到标签
                tag.setText(systemBeans.get(i).getTwoCategoryList().get(j).getChildName());

                tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = ((TextView)v).getText().toString();

                        //将被点击的标签上的字与List中的进行对比
                        for(int i = 0; i < mSystemBeanList.size(); i++){
                            for(int j = 0; j < mSystemBeanList.get(i).getTwoCategoryList().size(); j++){
                                if(mSystemBeanList.get(i).getTwoCategoryList().get(j).getChildName().equals(text)){

                                    String id = mSystemBeanList.get(i).getTwoCategoryList().get(j).getId();
                                    //启动另一个activity
                                    Intent intent = new Intent();
                                    intent.putExtra("id",id);
                                    intent.setClass(getContext(), SystemArticleActivity.class);
                                    getContext().startActivity(intent);
                                }
                            }
                        }
                    }
                });
                flowLayout.addView(tag);
            }
            linearLayout.addView(eachView);
        }
    }


}
