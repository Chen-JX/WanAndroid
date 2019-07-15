package com.CJX.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.CJX.Adapter.TitleFragmentPagerAdapter;
import com.CJX.Fragment.MainActivityHomePageFragment;
import com.CJX.Fragment.MainActivityProjectFragment;
import com.CJX.Fragment.MainActivitySystemFragment;
import com.example.cjx20.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private String[] mTitles = new String[]{"首页", "项目", "体系"};
    private TitleFragmentPagerAdapter mTitleFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//隐藏标题栏
        initView();
    }

    /**
     * 初始化可滑动底部菜单栏
     */
    private void initView(){
        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        mFragmentList.add(new MainActivityHomePageFragment());
        mFragmentList.add(new MainActivityProjectFragment());
        mFragmentList.add(new MainActivitySystemFragment());

        //使用适配器将ViewPager与Fragment绑定
        mTitleFragmentPagerAdapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(),mFragmentList,mTitles);
        mViewPager.setAdapter(mTitleFragmentPagerAdapter);

        //将viewPager与tabLayout进行绑定
        mTabLayout = (TabLayout)findViewById(R.id.tablayout_main_Activity);
        mTabLayout.setupWithViewPager(mViewPager);

        for(int i = 0 ; i < mTitles.length ; i++){
            mTabLayout.getTabAt(i).setText(mTitles[i]);
        }
    }
}
