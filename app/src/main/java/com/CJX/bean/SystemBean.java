package com.CJX.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
//体系的实体类

public class SystemBean implements Parcelable {
    private String name;
    private List<twoCategory> twoCategoryList;

    public SystemBean(){}

    protected SystemBean(Parcel in) {
        name = in.readString();
    }

    public static final Creator<SystemBean> CREATOR = new Creator<SystemBean>() {
        @Override
        public SystemBean createFromParcel(Parcel in) {
            return new SystemBean(in);
        }

        @Override
        public SystemBean[] newArray(int size) {
            return new SystemBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public static class twoCategory{
        private String childName;
        private String id;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public List<twoCategory> getTwoCategoryList() {
        return twoCategoryList;
    }

    public void setTwoCategoryList(List<twoCategory> twoCategoryList) {
        this.twoCategoryList = twoCategoryList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
