package com.lhb.player.playerapp.costom;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.lhb.player.playerapp.R;


public class LoadingView extends AppCompatImageView {
    private static final String TAG = "LoadingView";
    private float mDegrees = 0;
    private boolean isLoading = true;

    public LoadingView(@NonNull Context context) {
        this(context, null);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isLoading = true;
        startLoading();
    }

    private void startLoading() {
        post(new Runnable() {
            @Override
            public void run() {
                mDegrees += 30;
                if (mDegrees >= 360) {
                    mDegrees = 0;
                }
                invalidate();
                if (getVisibility() != VISIBLE || !isLoading) {
                    removeCallbacks(this);
                } else {
                    postDelayed(this, 50);
                }
            }
        });
    }

    private void stopLoading() {
        isLoading = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
