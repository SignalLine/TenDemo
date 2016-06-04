package com.zhouya.sp.ten;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity implements Animator.AnimatorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);
        ImageView imageView = (ImageView) findViewById(R.id.start_image_bg);
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.animator_start_path);
        animator.setTarget(imageView);
        animator.start();

        animator.addListener(this);

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Intent it = new Intent(this,MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
