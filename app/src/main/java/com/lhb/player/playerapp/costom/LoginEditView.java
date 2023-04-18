package com.lhb.player.playerapp.costom;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.utils.SizeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by howe.zhong
 * on 2022/8/16  10:23
 */
public class LoginEditView extends RelativeLayout {
    private String mValue;
    private boolean isMove = true;
    private TextView mTv;
    private EditText mEditText;
    private boolean mHintPas;

    public LoginEditView(Context context) {
        this(context, null);
    }

    public LoginEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public LoginEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoginEditView);
        mValue = ta.getString(R.styleable.LoginEditView_HintText);
        mHintPas = ta.getBoolean(R.styleable.LoginEditView_HintPassword, false);
        ta.recycle();
        this.setPadding(15, 0, 15, 0);
        this.setBackgroundResource(R.drawable.shape_log_normal_bg);
        initView(context);
    }

    private void initView(Context context) {
        mTv = new TextView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mTv.setLayoutParams(params);
        mTv.setText(mValue);
        mTv.setTextSize(16);
        this.addView(mTv);

        mEditText = new EditText(context);
        LayoutParams edParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        edParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mEditText.setLayoutParams(edParams);
        mEditText.setPadding(5, 0, 0, 0);
        mEditText.setMaxLines(1);
        mEditText.setSingleLine(true);
        if (mHintPas) {
            mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        mEditText.setBackground(null);
        mEditText.setTextSize(16);
        this.addView(mEditText);
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isMove) {
                    ObjectAnimator.ofFloat(mTv, "translationY", 0, -SizeUtils.dip2px(v.getContext(), 20)).start();
                    ObjectAnimator.ofFloat(mTv, "scaleX", 1, 0.8f).start();
                    ObjectAnimator.ofFloat(mTv, "scaleY", 1, 0.8f).start();
                    isMove = false;
                } else {
                    if (mEditText.getText().toString().trim().length() <= 0) {
                        ObjectAnimator.ofFloat(mTv, "translationY", -SizeUtils.dip2px(v.getContext(), 20), 0).start();
                        ObjectAnimator.ofFloat(mTv, "scaleX", 0.8f, 1).start();
                        ObjectAnimator.ofFloat(mTv, "scaleY", 0.8f, 1).start();
                        mEditText.setText("");
                        isMove = true;
                    }
                }
            }
        });


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (c >= 0x4e00 && c <= 0X9fff) { // 根据字节码判断
                            // 如果是中文，则清除输入的字符，否则保留
                            s.delete(i,i+1);
                        }
                    }
                }
            }
        });
    }

    public String getText() {
        return mEditText.getText().toString().trim();
    }

    public void setText(String text) {
        if (text.trim().length() > 0 && isMove) {
            mEditText.setText(text);
            ObjectAnimator.ofFloat(mTv, "translationY", 0, -SizeUtils.dip2px(getContext(), 20)).start();
            ObjectAnimator.ofFloat(mTv, "scaleX", 1, 0.8f).start();
            ObjectAnimator.ofFloat(mTv, "scaleY", 1, 0.8f).start();
            isMove = false;
        }


    }
}
