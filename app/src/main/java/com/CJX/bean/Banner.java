package com.CJX.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Banner implements Parcelable {
    private String id;
    private String imagePath;
    private String title;
    private String url;

    public Banner(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 描述
     * 返回的只是一些描述信息
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 完成对象序列化
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imagePath);
        dest.writeString(title);
        dest.writeString(url);
    }

    /**
     * 完成对象反序化
     */
    public static final Parcelable.Creator<Banner> CREATOR = new Creator<Banner>() {
        @Override
        public Banner createFromParcel(Parcel source) {
            return new Banner(source);
        }

        @Override
        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };

    public Banner(Parcel in){
        id = in.readString();
        imagePath = in.readString();
        title = in.readString();
        url = in.readString();
    }

}
