package com.CJX.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class HomePageArticle implements Parcelable {
    private String author;

    private String chapterName;
    private String link;
    private String niceDate;

    private String title;
    private String superChapterName;
    private String page;

    public HomePageArticle(){}

    protected HomePageArticle(Parcel in) {
        author = in.readString();
        chapterName = in.readString();
        link = in.readString();
        niceDate = in.readString();
        title = in.readString();
        superChapterName = in.readString();
        page = in.readString();
    }

    public static final Creator<HomePageArticle> CREATOR = new Creator<HomePageArticle>() {
        @Override
        public HomePageArticle createFromParcel(Parcel in) {
            return new HomePageArticle(in);
        }

        @Override
        public HomePageArticle[] newArray(int size) {
            return new HomePageArticle[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
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

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public void setPage(String page){
        this.page = page;
    }

    public String getPage(){return page;}
    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(chapterName);
        dest.writeString(link);
        dest.writeString(niceDate);
        dest.writeString(title);
        dest.writeString(superChapterName);
        dest.writeString(page);
    }
}
