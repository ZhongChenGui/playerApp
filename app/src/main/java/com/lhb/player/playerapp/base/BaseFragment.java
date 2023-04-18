package com.lhb.player.playerapp.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.activity.LoginActivity;
import com.lhb.player.playerapp.activity.MainActivity;
import com.lhb.player.playerapp.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mLinkServer;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY, LINK
    }

    private Unbinder mBind;
    private FrameLayout mBaseContainer;
    private MainActivity mActivity;

    @OnClick(R.id.network_error_tips)
    public void retry() {
        LogUtils.d(BaseFragment.class, "click retry....");
        onRetryClick();
    }

    protected void onRetryClick() {
        // 重新加载网络请求
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @OnClick(R.id.link_web_server_text)
    public void linkServer() {
        LogUtils.d(BaseFragment.class, "click linkServer....");
        onLinkServer();
    }

    protected void onLinkServer() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("server", "webdav");
        startActivity(intent);
        mActivity.finish();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater, container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStateView(inflater, container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        checkData(mActivity);
        initPresenter();
        loadData();
        return rootView;
    }

    protected abstract void checkData(MainActivity activity);

    /**
     * 如果子类需要监听事件时， 覆盖方法
     */
    protected void initListener() {

    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        // 成功的view
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);

        // 加载中的view
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);

        // 错误的view
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);

        // 空的view
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);
        // 连接服务器的view
        mLinkServer = loadLinkServerView(inflater, container);
        mBaseContainer.addView(mLinkServer);

        setUpState(State.NONE);

    }

    public void setUpState(State state) {
        this.currentState = state;
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
        mLinkServer.setVisibility(currentState == State.LINK ? View.VISIBLE : View.GONE);
    }

    private View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    private View loadLinkServerView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_link_server, container, false);
    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected void initView(View rootView) {
        // 初始化视图
    }

    protected void initPresenter() {
        // 创建Presenter
    }

    protected void loadData() {
        // 加载数据
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
        release();
    }

    protected void release() {
        // 释放资源
    }

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int resId = getResourcesId();
        return inflater.inflate(resId, container, false);
    }

    protected abstract int getResourcesId();
}
