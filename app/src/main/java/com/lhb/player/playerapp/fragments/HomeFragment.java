package com.lhb.player.playerapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.activity.DetailsActivity;
import com.lhb.player.playerapp.activity.LoginActivity;
import com.lhb.player.playerapp.activity.MainActivity;
import com.lhb.player.playerapp.adapter.DetailsItemAdapter;
import com.lhb.player.playerapp.adapter.HomeItemAdapter;
import com.lhb.player.playerapp.adapter.HomePageItemAdapter;
import com.lhb.player.playerapp.base.BaseFragment;
import com.lhb.player.playerapp.model.bean.WebdavResult;
import com.lhb.player.playerapp.presenters.Impl.FileListPresenterImpl;
import com.lhb.player.playerapp.utils.Constants;
import com.lhb.player.playerapp.utils.ShareUtil;
import com.lhb.player.playerapp.utils.SizeUtils;
import com.lhb.player.playerapp.view.IFileListCallback;
import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by howe.zhong
 * on 2022/8/9  9:50
 */
public class HomeFragment extends BaseFragment implements IFileListCallback {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.home_list_view)
    public RecyclerView mHomeListView;

    private HomeItemAdapter mAdapter;
    private FileListPresenterImpl mFileListPresenter;

    private boolean isLogin = false;
    private String mUser;
    private String mPassword;
    private String mUrl;
    private WebdavResult mWebdavResult;

    @Override
    protected int getResourcesId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void checkData(MainActivity activity) {
        if (activity != null) {
            mWebdavResult = activity.getWebdavResult();
            if (mWebdavResult != null && mWebdavResult.getBeans().size() > 0) {
                isLogin = true;
            }
        }
    }

    @Override
    protected void onRetryClick() {
        ShareUtil.setUp(this.getContext());
        mFileListPresenter.getFileList(
                ShareUtil.getStringValue(Constants.KEY_WEBDAV_URL) + "/" + Constants.HOME_DIRECTORY,
                ShareUtil.getStringValue(Constants.KEY_WEBDAV_USER),
                ShareUtil.getStringValue(Constants.KEY_WEBDAV_PASSWORD), false);
    }

    public void refresh() {
        onRetryClick();
    }

    @Override
    protected void initView(View rootView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        mHomeListView.setLayoutManager(gridLayoutManager);
        mHomeListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.right = SizeUtils.dip2px(getContext(), 5);
                outRect.left = SizeUtils.dip2px(getContext(), 5);
                outRect.top = SizeUtils.dip2px(getContext(), 5);
                outRect.left = SizeUtils.dip2px(getContext(), 5);
            }
        });
        mAdapter = new HomeItemAdapter();
        mHomeListView.setAdapter(mAdapter);
    }


    @Override
    protected void initPresenter() {
        mFileListPresenter = new FileListPresenterImpl();
        mFileListPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        if (isLogin) {
            List<WebdavResult.WebdavResultBean> beans = mWebdavResult.getBeans();
            setUpState(State.SUCCESS);
            mAdapter.setData(beans);
        } else {
            setUpState(State.LINK);
        }
    }


    @Override
    public void onError() {
        Log.d(TAG, "onError: ............");
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        Log.d(TAG, "onLoading: ...................");
        setUpState(State.LOADING);

    }

    @Override
    public void onLoadedSuccess(List<DavResource> list) {
        setUpState(State.SUCCESS);
        List<WebdavResult.WebdavResultBean> webdavResults = new ArrayList<>();
        for (DavResource davResource : list) {
            WebdavResult.WebdavResultBean webdavResultBean = new WebdavResult.WebdavResultBean(davResource.getName(), davResource.getHref().toString());
            webdavResults.add(webdavResultBean);
        }
        mAdapter.setData(webdavResults);
        mWebdavResult.setBeans(webdavResults);
        Log.d(TAG, "onHomeDataLoaded: .....................");
    }

    @Override
    public void onEmpty() {
        Log.d(TAG, "onEmpty: ......................");
        setUpState(State.EMPTY);
    }

    @Override
    protected void release() {
        mFileListPresenter.unregisterViewCallback(this);
    }
}
