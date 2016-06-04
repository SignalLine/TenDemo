package com.zhouya.sp.ten;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FontActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup font_tab_bar;
    private TextView font_text_text1,font_text_text2;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("字体设置");
            actionBar.setLogo(R.mipmap.back);
        }
        //获取文字的id
        font_text_text1 = (TextView)findViewById(R.id.font_text_text1);
        font_text_text2 = (TextView)findViewById(R.id.font_text_text2);

        //RadioGroup
        font_tab_bar = (RadioGroup) findViewById(R.id.font_tab_bar);
        if (font_tab_bar != null) {
            font_tab_bar.setOnCheckedChangeListener(this);
        }

        mSharedPreferences = getSharedPreferences("FontSetting", MODE_PRIVATE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = true;
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return ret;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.font_tab_small_font:
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putFloat("novelFont1", 11f)
                        .putFloat("novelFont2", 13f)
                        .putFloat("movieFont1", 13f)
                        .putFloat("movieFont2", 11f)
                        .putFloat("diagramFont1", 11f)
                        .putFloat("diagramFont2", 11f);
                editor.commit();
                font_text_text1.setTextSize(12);
                font_text_text2.setTextSize(12);
                break;
            case R.id.font_tab_middle_font:
                SharedPreferences.Editor editor1 = mSharedPreferences.edit();
                editor1.putFloat("novelFont1", 16f)
                        .putFloat("novelFont2", 18f)
                        .putFloat("movieFont1", 18f)
                        .putFloat("movieFont2", 16f)
                        .putFloat("diagramFont1", 16f)
                        .putFloat("diagramFont2", 16f);
                editor1.commit();
                font_text_text1.setTextSize(16);
                font_text_text2.setTextSize(16);
                break;
            case R.id.font_tab_large_font:
                SharedPreferences.Editor editor2 = mSharedPreferences.edit();
                editor2.putFloat("novelFont1", 20f)
                        .putFloat("novelFont2", 24f)
                        .putFloat("movieFont1", 24f)
                        .putFloat("movieFont2", 20f)
                        .putFloat("diagramFont1", 20f)
                        .putFloat("diagramFont2", 20f);
                editor2.commit();
                font_text_text1.setTextSize(18);
                font_text_text2.setTextSize(18);
                break;
        }
    }
}
