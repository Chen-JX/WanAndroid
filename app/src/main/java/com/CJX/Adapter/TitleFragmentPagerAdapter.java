package com.CJX.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TitleFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private String[] mtitles;//title是导航栏上显示的文字

    public TitleFragmentPagerAdapter(FragmentManager fm, List<Fragment> FragmentList, String[] title){
        super(fm);
        this.mFragmentList = FragmentList;
        this.mtitles = title;
    }

    //获取索引位置的Fragment
    @Override
    public Fragment getItem(int position){
        Fragment fragment = null;
        if(position < mFragmentList.size()){
            fragment = mFragmentList.get(position);
        }else{
            fragment = mFragmentList.get(0);
        }
        return fragment;
    }

    //返回viewpager对应的title的text
    @Override
    public CharSequence getPageTitle(int position){
        if(mtitles != null && mtitles.length > 0){
            return mtitles[position];
        }
        return null;
    }

    //返回获取的数量
    @Override
    public int getCount(){
        return mFragmentList.size();
    }

}
