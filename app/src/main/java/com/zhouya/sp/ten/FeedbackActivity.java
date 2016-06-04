package com.zhouya.sp.ten;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setLogo(R.mipmap.back);
            actionBar.setTitle("意见反馈");
        }

        //设置微信和qq的账号
        final TextView weixin = (TextView) findViewById(R.id.feedback_text_weixin);
        final TextView qq = (TextView) findViewById(R.id.feedback_text_QQ);
        //设置复制到剪切板
        LinearLayout weixinLayout = (LinearLayout) findViewById(R.id.feedback_ll_text_weixin);
        LinearLayout qqLayout = (LinearLayout) findViewById(R.id.feedback_ll_text_qq);

        if (qq != null && weixin != null) {
            weixin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            qq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //设置微信
            if (weixinLayout != null) {
                weixinLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String wxMsg = weixin.getText().toString();
                        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clipData = new ClipData(ClipData.newPlainText("wxMsg",wxMsg));
//                        cmb.setPrimaryClip(clipData);
                        cmb.setText(wxMsg.trim());
                        Toast.makeText(FeedbackActivity.this, "已为您复制\"" + wxMsg + "\"到剪切板", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            //设置qq
            if (qqLayout != null) {
                qqLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String qqMsg = qq.getText().toString();
                        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clipData = new ClipData(ClipData.newPlainText("wxMsg",wxMsg));
//                        cmb.setPrimaryClip(clipData);
                        cmb.setText(qqMsg.trim());
                        Toast.makeText(FeedbackActivity.this, "已为您复制\"" + qqMsg + "\"到剪切板", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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
}
