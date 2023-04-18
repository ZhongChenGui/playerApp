package com.xiao.nicevideoplayer;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by
 * 切换倍速对话框（
 */
public class ChangeSpeedDialog extends Dialog {

    private LinearLayout mLinearLayout;
    private int mCurrentCheckedIndex = 1;

    public ChangeSpeedDialog(Context context) {
        super(context, R.style.dialog_change_clarity);
        init(context);
    }

    private void init(Context context) {
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSpeedNotChanged();
                }
                ChangeSpeedDialog.this.dismiss();
            }
        });

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.MATCH_PARENT);
        setContentView(mLinearLayout, params);
    }

    @Override
    public void show() {
        WindowManager.LayoutParams windowParams = getWindow().getAttributes();
        windowParams.height = NiceUtil.getScreenHeight(getContext());
        windowParams.width = NiceUtil.getScreenWidth(getContext());
        getWindow().setAttributes(windowParams);
        super.show();
    }

    public void setSpeed(List<Float> mFloats, int defaultSpeed) {
        for (int i = 0; i < mFloats.size(); i++) {
            TextView itemView = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.item_change_clarity, mLinearLayout, false);
            itemView.setTag(i);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int checkIndex = (int) v.getTag();
                        if (checkIndex != mCurrentCheckedIndex) {
                            for (int j = 0; j < mLinearLayout.getChildCount(); j++) {
                                mLinearLayout.getChildAt(j).setSelected(checkIndex == j);
                            }
                            mListener.onSpeedChanged(checkIndex);
                            mCurrentCheckedIndex = checkIndex;
                        } else {
                            mListener.onSpeedNotChanged();
                        }
                    }
                    ChangeSpeedDialog.this.dismiss();
                }
            });
            itemView.setText(mFloats.get(i) + "x");
            itemView.setSelected(i == defaultSpeed);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                    itemView.getLayoutParams();
            params.topMargin = (i == 0) ? 0 : NiceUtil.dp2px(getContext(), 16f);
            mLinearLayout.addView(itemView, params);
        }
    }


    public interface OnSpeedChangedListener {
        /**
         * 切换播放倍数后回调
         *
         * @param speedIndex 切换到的倍数的索引值
         */
        void onSpeedChanged(int speedIndex);

        /**
         * 倍数没有切换，比如点击了空白位置，或者点击的是之前的倍数
         */
        void onSpeedNotChanged();
    }

    private OnSpeedChangedListener mListener;

    public void setOnClarityCheckedListener(OnSpeedChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onBackPressed() {
        // 按返回键时回调倍数没有变化
        if (mListener != null) {
            mListener.onSpeedNotChanged();
        }
        super.onBackPressed();
    }
}
