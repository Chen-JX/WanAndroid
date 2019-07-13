package com.CJX.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectArticle implements Parcelable {
    private String author;
    private String chapterId;
    private String chapterName;//
//    private String courseId;
    private String desc;//描述
    private String envelopePic;//图片
    //private String id;//根据分类查询
    private String link;//文章链接
    private String niceDate;
//    private String superChapterId;
    private String superChapterName;
//    private String url1;//分类对应的链接的后部分
    private String name;//所属分类名称
    private String title;//文章题目

    public ProjectArticle(){}
    protected ProjectArticle(Parcel in) {
        author = in.readString();
        chapterId = in.readString();
        chapterName = in.readString();
        desc = in.readString();
        envelopePic = in.readString();
        //id = in.readString();
        link = in.readString();
        niceDate = in.readString();
        superChapterName = in.readString();
        name = in.readString();
        title = in.readString();
    }

    public static final Creator<ProjectArticle> CREATOR = new Creator<ProjectArticle>() {
        @Override
        public ProjectArticle createFromParcel(Parcel in) {
            return new ProjectArticle(in);
        }

        @Override
        public ProjectArticle[] newArray(int size) {
            return new ProjectArticle[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

//    public String getCourseId() {
//        return courseId;
//    }
//
//    public void setCourseId(String courseId) {
//        this.courseId = courseId;
//    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

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

//    public String getSuperChapterId() {
//        return superChapterId;
//    }
//
//    public void setSuperChapterId(String superChapterId) {
//        this.superChapterId = superChapterId;
//    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

//    public String getUrl1() {
//        return url1;
//    }
//
//    public void setUrl1(String url1) {
//        this.url1 = url1;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(chapterId);
        dest.writeString(chapterName);
        dest.writeString(desc);
        dest.writeString(envelopePic);
        dest.writeString(link);
        dest.writeString(niceDate);
        dest.writeString(superChapterName);
        dest.writeString(name);
        dest.writeString(title);
    }
}
