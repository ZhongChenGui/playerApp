package com.lhb.player.playerapp.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContextViewResId());
        /**
         *  灰色UI
         * ColorMatrix cm = new ColorMatrix();
         * cm.setSaturation(0);
         * Paint paint = new Paint();
         * paint.setColorFilter(new ColorMatrixColorFilter(cm));
         * View contentView = getWindow().getDecorView();
         * contentView.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
         */

        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();

    }

    protected abstract void initPresenter();

    protected void initEvent() {
        // 添加事件
    }

    // 初始化页面
    protected abstract void initView();


    public abstract int getContextViewResId();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        this.release();
    }

    protected void release() {

    }
}
