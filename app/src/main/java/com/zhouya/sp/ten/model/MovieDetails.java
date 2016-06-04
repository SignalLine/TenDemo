package com.zhouya.sp.ten.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/5/30
 */
public class MovieDetails {
    private String id;
    private String title;
    private String author;
    private String authorbrief;
    private int times;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private String text5;

    private String movieInfo;//用于存储  ---剧情简介

    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String imageforplay;

    private String realtitle;
    //解析数据
    public void parseJson(JSONObject jo) throws JSONException {
        if (jo != null) {
            id = jo.getString("id");//id
            title = jo.getString("title");//title标题
            author = jo.getString("author");//作者
            authorbrief = jo.getString("authorbrief");
            times = jo.getInt("times");
            text1 = jo.getString("text1");
            text2 = jo.getString("text2");
            text3 = jo.getString("text3");
            text4 = jo.getString("text4");
            text5 = jo.getString("text5");

            movieInfo = text2.substring(0, text2.indexOf("\r\n"));
            text2 = text2.substring(text2.indexOf("\r\n") + 1);

            image1 = jo.getString("image1");
            image2 = jo.getString("image2");
            image3 = jo.getString("image3");
            image4 = jo.getString("image4");
            imageforplay = jo.getString("imageforplay");

            realtitle = jo.getString("realtitle");
        }
    }

    public String getMovieInfo() {
        return movieInfo;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorbrief() {
        return authorbrief;
    }

    public int getTimes() {
        return times;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }

    public String getText4() {
        return text4;
    }

    public String getText5() {
        return text5;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getImage4() {
        return image4;
    }

    public String getImageforplay() {
        return imageforplay;
    }

    public String getRealtitle() {
        return realtitle;
    }
}
