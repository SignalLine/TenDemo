package com.zhouya.sp.ten.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/5/31
 */
public class DiagramDetails {
    //加载图片地址
    public static final String PICTURE_DISPLAY_URL = "http://api.shigeten.net/";
    private int id;
    private String title;
    private String authorbrief;
    private String image1;
    private String text1;
    private String text2;

    public void parseJson(JSONObject jo) throws JSONException {
        id = jo.getInt("id");
        title = jo.getString("title");
        authorbrief = jo.getString("authorbrief");
        image1 = PICTURE_DISPLAY_URL + jo.getString("image1");
        text1 = jo.getString("text1");
        text2 = jo.getString("text2");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorbrief() {
        return authorbrief;
    }

    public String getImage1() {
        return image1;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }
}
