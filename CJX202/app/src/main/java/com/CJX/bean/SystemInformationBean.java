package com.CJX.bean;

import android.os.Parcel;
import android.os.Parcelable;

//体系每个分类下的详情界面
public class SystemInformationBean implements Parcelable {
    private String author;
    private String link;//需要跳转的url
    private String niceDate;
    private String title;

    public SystemInformationBean(){}

    protected SystemInformationBean(Parcel in) {
        author = in.readString();
        link = in.readString();
        niceDate = in.readString();
        title = in.readString();
    }

    public static final Creator<SystemInformationBean> CREATOR = new Creator<SystemInformationBean>() {
        @Override
        public SystemInformationBean createFromParcel(Parcel in) {
            return new SystemInformationBean(in);
        }

        @Override
        public SystemInformationBean[] newArray(int size) {
            return new SystemInformationBean[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(link);
        dest.writeString(niceDate);
        dest.writeString(title);
    }
}
