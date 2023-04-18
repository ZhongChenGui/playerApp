package com.lhb.player.playerapp.costom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lhb.player.playerapp.R;


/**
 * Created by howe.zhong
 * on 2022/8/16  13:52
 */
public class MyCheckBox extends RelativeLayout implements View.OnClickListener {
    private String mText;
    private Drawable mDrawable;
    private boolean check = false;
    private ImageView mIv;

    public MyCheckBox(Context context) {
        this(context, null);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyCheckBox);
        mText = ta.getString(R.styleable.MyCheckBox_text);
        mDrawable = ta.getDrawable(R.styleable.MyCheckBox_icon);
        ta.recycle();
        this.setPadding(30, 10, 30, 10);
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
//        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        this.setLayoutParams(layoutParams);
        this.setBackgroundResource(R.drawable.shape_log_normal_bg);
        initView(context);
        this.setClickable(true);
        this.setOnClickListener(this);
    }

    private void initView(Context context) {
        TextView tv = new TextView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_VERTICAL);
        tv.setText(mText);
        tv.setTextSize(20);
        tv.setLayoutParams(params);
        tv.setTextColor(Color.BLACK);
        this.addView(tv);

        mIv = new ImageView(context);
        LayoutParams params1 = new LayoutParams(dip2px(context, 32), dip2px(context, 32));
        params1.addRule(ALIGN_PARENT_RIGHT);
        params1.addRule(CENTER_VERTICAL);
        mIv.setLayoutParams(params1);
        mIv.setImageDrawable(mDrawable);
        this.addView(mIv);
        mIv.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        if (check) {
            // 切换到未选中
            show(R.drawable.shape_log_normal_bg, GONE);
            check = false;
        } else {
            // 切换到选中
            show(R.drawable.shape_check_check_bg, VISIBLE);
            check = true;
        }
    }

    public void check() {

    }

    private void show(int p, int visible) {
        this.setBackgroundResource(p);
        mIv.setVisibility(visible);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check2) {
        check = check2;
        if (check2) {
            // 切换到选中
            show(R.drawable.shape_check_check_bg, VISIBLE);
        } else {
            // 切换到未选中
            show(R.drawable.shape_log_normal_bg, GONE);
        }
    }

}
