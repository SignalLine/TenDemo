package com.zhouya.sp.ten.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhouya.sp.ten.AboutUsActivity;
import com.zhouya.sp.ten.ErrorActivity;
import com.zhouya.sp.ten.FavoriteActivity;
import com.zhouya.sp.ten.FeedbackActivity;
import com.zhouya.sp.ten.FontActivity;
import com.zhouya.sp.ten.QQLoginActivity;
import com.zhouya.sp.ten.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener {
    //-------------------------
    private static final String APPID = "1105369687";
    private Tencent mTencent;//qq主操作对象
    private IUiListener loginListener;//授权登录监听器
    private IUiListener userInfoListener;//获取用户监听器
    private String scope;//获取信息的范围参数
    private UserInfo mUserInfo;//qq用户信息
    //-------------------------

    private ImageView mImageView;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_personal, container, false);

        //点击关于十个
        LinearLayout liLayout = (LinearLayout) ret.findViewById(R.id.personal_tv_aboutUs);
        liLayout.setOnClickListener(this);
        //点击登录QQ
        mImageView = (ImageView) ret.findViewById(R.id.personal_iv_loginQQ);

        //点击我的收藏
        LinearLayout favorite = (LinearLayout) ret.findViewById(R.id.personal_ll_favorite);
        favorite.setOnClickListener(this);
        //点击字体设置
        LinearLayout font = (LinearLayout) ret.findViewById(R.id.personal_ll_font);
        font.setOnClickListener(this);
        //点击意见反馈
        LinearLayout feedback = (LinearLayout)ret.findViewById(R.id.personal_ll_feedback);
        feedback.setOnClickListener(this);

        return ret;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.personal_tv_aboutUs:
                Intent it = new Intent(getContext(), AboutUsActivity.class);
                startActivity(it);
                break;
            case R.id.personal_ll_font:
                Intent it2 = new Intent(getContext(), FontActivity.class);
                startActivity(it2);
                break;
            case R.id.personal_ll_favorite:
                Intent it3 = new Intent(getContext(), FavoriteActivity.class);
                startActivity(it3);
                break;
            case R.id.personal_ll_feedback:
                Intent it4 = new Intent(getContext(), FeedbackActivity.class);
                startActivity(it4);
                break;
        }
    }

    @Override
    public void onResume() {
        setupViews();
        initData();
        super.onResume();
    }
    //初始化登录点击事件
    private void setupViews(){
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("你点击了使用qq登录按钮");
                login();
            }
        });
    }
    //初始化数据
    private void initData(){
        mTencent = Tencent.createInstance(APPID,getActivity());
        //要所有权限，不用再次申请增量权限，这里不要设置为get_user_info,add_t
        scope = "all";
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Log.d("qq", "有数据返回....");
                if(o == null){
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) o;

                    String msg = jo.getString("msg");

                    System.out.println("json=" + String.valueOf(jo));

                    System.out.println("msg="+msg);
                    if ("sucess".equals(msg)) {
                        Toast.makeText(getActivity(), "登录成功",
                                Toast.LENGTH_LONG).show();

                        String openID = jo.getString("openid");
                        String accessToken = jo.getString("access_token");
                        String expires = jo.getString("expires_in");
                        mTencent.setOpenId(openID);
                        mTencent.setAccessToken(accessToken, expires);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    Intent it = new Intent(getContext(), ErrorActivity.class);
                    startActivity(it);
                }

            }
            /**
             * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
             * "pf":"desktop_m_qq-10000144-android-2002-",
             * "query_authority_cost":448,
             * "authority_cost":-136792089,
             * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
             * "expires_in":7776000,
             * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
             * "msg":"",
             * "access_token":"A2455F491478233529D0106D2CE6EB45",
             * "login_cost":499}
             */
            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };

        userInfoListener = new IUiListener() {
            /**
             * {"is_yellow_year_vip":"0","ret":0,
             * "figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/40",
             * "figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "nickname":"攀爬←蜗牛","yellow_vip_level":"0","is_lost":0,"msg":"",
             * "city":"黄冈","
             * figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/50",
             * "vip":"0","level":"0",
             * "figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "province":"湖北",
             * "is_yellow_vip":"0","gender":"男",
             * "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/30"}
             */
            @Override
            public void onComplete(Object o) {
                if(o == null){
                    return ;
                }
                try {
                    JSONObject jo = (JSONObject) o;
                    int ret = jo.getInt("ret");
                    System.out.println("json=" + String.valueOf(jo));
                    if(ret == 100030){
                        //权限不够，需要增量授权
                        Runnable r = new Runnable() {
                            public void run() {
                                mTencent.reAuth(getActivity(), "all", new IUiListener() {

                                    @Override
                                    public void onError(UiError arg0) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onComplete(Object arg0) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onCancel() {
                                        // TODO Auto-generated method stub

                                    }
                                });
                            }
                        };

                        getActivity().runOnUiThread(r);
                    }else{
                        String nickName = jo.getString("nickname");
                        String gender = jo.getString("gender");

                        Toast.makeText(getActivity(), "你好，" + nickName, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    Intent it = new Intent(getContext(), ErrorActivity.class);
                    startActivity(it);
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };
    }

    private void login() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(getActivity(), scope, loginListener);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, loginListener);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        if (mTencent != null) {
            mTencent.logout(getContext());
        }
        super.onDestroy();
    }


}
