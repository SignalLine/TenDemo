package com.zhouya.sp.ten.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.utils.MySQLiteOpenHelper;
import com.zhouya.sp.ten.ErrorActivity;
import com.zhouya.sp.ten.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/5/30
 */
public class NetworkTask extends AsyncTask<String,Integer,byte[]> {

    private Context mContext;
    private NetworkTaskCallback mCallback;
//    private ProgressDialog mProgressDialog;
//    private AnimationDrawable mDrawable;
    private MySQLiteOpenHelper sqLiteHelper;

    public NetworkTask(Context context, NetworkTaskCallback callback) {
        mContext = context;
        mCallback = callback;
    }
    public NetworkTask(Context context,MySQLiteOpenHelper sqLiteHelper, NetworkTaskCallback callback) {
        mContext = context;
        mCallback = callback;
        this.sqLiteHelper = sqLiteHelper;
    }

    @Override
    protected void onPreExecute() {
//
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//
//        mDrawable = (AnimationDrawable) mContext.getDrawable(R.drawable.drawable_elec_loading);

//        mProgressDialog.setProgressDrawable(mDrawable);
//        mProgressDialog.setMessage("玩命加载中...");
//        mProgressDialog.show();
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_progress_round);
        // 3.给动画设置插值器
//        animation.setInterpolator(new LinearInterpolator());
        //2播放
//        txtShan.startAnimation(animation);
//        mProgressDialog.startAnimation(animation);
    }

    @Override
    protected byte[] doInBackground(String... params) {

        byte[] ret = null;

        if (params != null) {
            int len = params.length;
            if(len > 0){
                //TODO:缓存Cache加载的数据
                String url = params[0];

                ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null) {
                    //联网
                    ret = HttpTools.doGet(params[0]);
                }else {
                    Intent it = new Intent(mContext, ErrorActivity.class);
                    it.putExtra("url",params[0]);
                    mContext.startActivity(it);
                }
            }
        }

        return ret;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {

        if (mCallback != null) {
            mCallback.onTaskFinished(bytes);
//            mProgressDialog.dismiss();
//            mProgressDialog.cancel();
//            mProgressDialog = null;
        }

    }
}
