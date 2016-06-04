package com.zhouya.sp.ten.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/6/1
 */
public class MyRadioButton extends RadioButton{
    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] compoundDrawables = getCompoundDrawables();
        if (compoundDrawables!=null&&compoundDrawables.length>1){
            Drawable compoundDrawable = compoundDrawables[1];
            if (compoundDrawable!=null){
                int drewableHight = compoundDrawable.getIntrinsicHeight();
                int viewHight = getHeight();
                canvas.translate(0,(viewHight-drewableHight)/2);
            }
        }
        super.onDraw(canvas);
    }
}
