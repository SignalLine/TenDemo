package com.zhouya.sp.ten;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhouya.sp.ten.fragments.DiagramFragment;
import com.zhouya.sp.ten.fragments.MovieFragment;
import com.zhouya.sp.ten.fragments.NovelFragment;
import com.zhouya.sp.ten.fragments.PersonalFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private List<Fragment> mFragments;//存储主界面的fragment
    private ImageView mSetImage;
    private RelativeLayout mLinearLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //异常日志打印
//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(getApplicationContext());
        //-----------------------
        mSetImage = (ImageView) findViewById(R.id.main_title_setImage);
        mLinearLayout = (RelativeLayout) findViewById(R.id.main_linear_title);
        mFragments = new ArrayList<>();

        FragmentManager manager = getSupportFragmentManager();
        if (savedInstanceState == null) {//第一次创建
            Fragment fragment = new MovieFragment();
            mFragments.add(fragment);

            fragment = new NovelFragment();
            mFragments.add(fragment);

            fragment = new DiagramFragment();
            mFragments.add(fragment);

            fragment = new PersonalFragment();
            mFragments.add(fragment);

            FragmentTransaction transaction = manager.beginTransaction();
            int index = 0;
            for (Fragment f : mFragments) {
                //添加
                transaction.add(R.id.main_fragment_container,f,"tag" + index);
                transaction.hide(f);
                index++;
            }
            //显示第一个
            transaction.show(mFragments.get(0));

            //提交事务
            transaction.commit();
        }else{//不是第一次创建
            //FragmentManager的管理
            //根据add的时候，设置tag 来查找Fragment
            for (int i = 0; i < 4; i++) {
                String tag = "tag" + i;
                Fragment f = manager.findFragmentByTag(tag);
                if (f != null) {
                    mFragments.add(f);
                }
            }
        }

        //-Tab切换----------------------------
        RadioGroup group = (RadioGroup) findViewById(R.id.main_tab_bar);
        if (group != null) {
            group.setOnCheckedChangeListener(this);
        }
    }

    //--RadioGroup事件点击监听-------------------
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //改变索引，切换Fragment
        int index = 0;
        switch (checkedId) {
            case R.id.main_tab_movie:
                index = 0;
                mLinearLayout.setVisibility(View.VISIBLE);
                mSetImage.setImageResource(R.mipmap.logo_critic);
                break;
            case R.id.main_tab_novel:
                index = 1;
                mLinearLayout.setVisibility(View.VISIBLE);
                mSetImage.setImageResource(R.mipmap.logo_novel);
                break;
            case R.id.main_tab_diagram:
                index = 2;
                mLinearLayout.setVisibility(View.VISIBLE);
                mSetImage.setImageResource(R.mipmap.logo_diagram);
                break;
            case R.id.main_tab_personal:
                index = 3;
                mLinearLayout.setVisibility(View.GONE);
                break;
        }
        //选中的Fragment 进行显示
        switchFragment(index);
    }
    //选中的Fragment 进行显示
    private void switchFragment(int index) {
        if(index >= 0 && index < mFragments.size()){
            int size = mFragments.size();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            for (int i = 0; i < size; i++) {
                Fragment f = mFragments.get(i);
                if (i == index){
                    transaction.show(f);
                }else{
                    transaction.hide(f);
                }
            }

            transaction.commit();
        }
    }

    boolean isFinish = false;
    @Override
    public void onBackPressed() {
        if(isFinish){
            super.onBackPressed();
        }else{
            isFinish = true;
            Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            isFinish = false;
                        }
                    },2000
            );
        }

    }

    @Override
    protected void onDestroy() {
        mLinearLayout.clearFocus();
        mLinearLayout = null;
        mFragments.clear();
        mFragments = null;
        mSetImage.clearFocus();
        mSetImage = null;
        super.onDestroy();
    }
}
