package com.CJX.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectArticleCategory implements Parcelable {
    private String id;
    private String name;
    public ProjectArticleCategory(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<ProjectArticleCategory> CREATOR = new Creator<ProjectArticleCategory>() {
        @Override
        public ProjectArticleCategory createFromParcel(Parcel source) {
            return new ProjectArticleCategory(source);
        }

        @Override
        public ProjectArticleCategory[] newArray(int size) {
            return new ProjectArticleCategory[size];
        }
    };

    public ProjectArticleCategory(Parcel in){
        id = in.readString();
        name = in.readString();
    }
}
