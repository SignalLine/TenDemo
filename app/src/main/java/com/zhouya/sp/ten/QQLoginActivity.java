package com.zhouya.sp.ten;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 *
 * 通过调用Tencent类的login函数发起登录/校验登录态。
 *
 * 该API具有两个作用：
 * （1）如果开发者没有调用mTencent实例的setOpenId、setAccessToken API，
 *      则该API执行正常的登录操作；
 * （2）如果开发者先调用mTencent实例的setOpenId、setAccessToken
 *      API，则该API执行校验登录态的操作。如果登录态有效，则返回成功给应用，
 *      如果登录态失效，则会自动进入登录流程，将最新的登录态数据返回给应用
 *
 *
 */
public class QQLoginActivity extends AppCompatActivity {
    private static final String APPID = "1105369687";

    private EditText et1,et2;
    private Tencent mTencent;//qq主操作对象
    private IUiListener loginListener;//授权登录监听器
    private IUiListener userInfoListener;//获取用户监听器
    private String scope;//获取信息的范围参数
    private UserInfo mUserInfo;//qq用户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqlogin);

        setupViews();
        initData();
    }

    private void setupViews(){
        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText2);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("你点击了使用qq登录按钮");
                login();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("开始获取用户信息");
                if (mTencent.getQQToken() == null) {
                    System.out.println("qqtoken == null");
                }
                mUserInfo = new UserInfo(QQLoginActivity.this, mTencent.getQQToken());
                mUserInfo.getUserInfo(userInfoListener);
            }
        });
    }

    private void initData(){
        mTencent = Tencent.createInstance(APPID,QQLoginActivity.this);
        //要所有权限，不用再次申请增量权限，这里不要设置为get_user_info,add_t
        scope = "all";
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Log.d("qq","有数据返回....");
                if(o == null){
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) o;

                    String msg = jo.getString("msg");

                    System.out.println("json=" + String.valueOf(jo));

                    System.out.println("msg="+msg);
                    if ("sucess".equals(msg)) {
                        Toast.makeText(QQLoginActivity.this, "登录成功",
                                Toast.LENGTH_LONG).show();

                        String openID = jo.getString("openid");
                        String accessToken = jo.getString("access_token");
                        String expires = jo.getString("expires_in");
                        mTencent.setOpenId(openID);
                        mTencent.setAccessToken(accessToken, expires);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
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
                                mTencent.reAuth(QQLoginActivity.this, "all", new IUiListener() {

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

                        QQLoginActivity.this.runOnUiThread(r);
                    }else{
                        String nickName = jo.getString("nickname");
                        String gender = jo.getString("gender");

                        Toast.makeText(QQLoginActivity.this, "你好，" + nickName, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // TODO: handle exception
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
            mTencent.login(QQLoginActivity.this, scope, loginListener);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, loginListener);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mTencent != null) {
            mTencent.logout(this);
        }
        super.onDestroy();
    }
}
