package com.zhouya.sp.ten.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/5/31
 */
public class NovelDetails {
    private int id;
    private String title;
    private String author;
    private String authorbrief;
    private int times;
    private String summary;
    private String text;

    public void parseJson(JSONObject jo) throws JSONException {
        id = jo.getInt("id");
        title = jo.getString("title");
        author = jo.getString("author");
        authorbrief = jo.getString("authorbrief");
        times = jo.getInt("times");
        summary = jo.getString("summary");
        text = jo.getString("text");
    }

    public int getId() {
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

    public String getSummary() {
        return summary;
    }

    public String getText() {
        return text;
    }
}
