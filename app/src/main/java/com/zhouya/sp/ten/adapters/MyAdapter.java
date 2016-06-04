package com.zhouya.sp.ten.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/6/1
 */
public class MyAdapter extends BaseAdapter{
    private Context mContext;
    private List<Map<String,String>> mLists;

    public MyAdapter(Context context, List<Map<String, String>> lists) {
        mContext = context;
        mLists = lists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
