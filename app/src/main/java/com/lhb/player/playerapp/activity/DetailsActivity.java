package com.lhb.player.playerapp.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.adapter.DetailsItemAdapter;
import com.lhb.player.playerapp.model.bean.DetailsData;
import com.lhb.player.playerapp.presenters.Impl.DetailsPresenterImpl;
import com.lhb.player.playerapp.presenters.Impl.FileListPresenterImpl;
import com.lhb.player.playerapp.utils.Constants;
import com.lhb.player.playerapp.utils.ShareUtil;
import com.lhb.player.playerapp.utils.SizeUtils;
import com.lhb.player.playerapp.utils.UrlUtils;
import com.lhb.player.playerapp.view.IDetailsCallback;
import com.lhb.player.playerapp.view.IFileListCallback;
import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by howe.zhong
 * on 2022/8/14  15:19
 */
public class DetailsActivity extends AppCompatActivity implements IFileListCallback {

    private static final String TAG = "DetailsActivity";
    @BindView(R.id.details_list_view)
    public RecyclerView mSuccessView;

    @BindView(R.id.details_root)
    public ViewGroup mDetailsRoot;

    @BindView(R.id.top_menu_back)
    public ImageView back;

    private DetailsItemAdapter mAdapter;
    private View mEmptyView;
    private View mLoadingView;
    private View mErrorView;
    private State currentState = State.NONE;
    private String mHref;
    private Unbinder mBind;
    private DetailsPresenterImpl mDetailsPresenter;
    private String mUrl;
    private String mUser;
    private String mPassword;
    private FileListPresenterImpl mFileListPresenter;


    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mBind = ButterKnife.bind(this);
        checkIntent();
        initView();
        initListener();
        initPresenter();
        loadData();
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initPresenter() {
//        mDetailsPresenter = new DetailsPresenterImpl(mUrl);
//        mDetailsPresenter.registerViewCallback(this);
        mFileListPresenter = new FileListPresenterImpl();
        mFileListPresenter.registerViewCallback(this);
    }

    private void checkIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mHref = intent.getStringExtra("href");
            if (mHref == null || mHref.isEmpty()) {
                Toast.makeText(this, "非法访问！", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "非法访问！", Toast.LENGTH_SHORT).show();
            finish();
        }
        ShareUtil.setUp(this);
        mUrl = ShareUtil.getStringValue(Constants.KEY_WEBDAV_URL);
        mUrl = UrlUtils.getDomain(mUrl);
        mUser = ShareUtil.getStringValue(Constants.KEY_WEBDAV_USER);
        mPassword = ShareUtil.getStringValue(Constants.KEY_WEBDAV_PASSWORD);
    }

    protected void loadData() {
        mFileListPresenter.getFileList(mUrl + mHref, mUser, mPassword, false);
    }

    protected void initView() {
        loadStateView();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mSuccessView.setLayoutManager(gridLayoutManager);
        mAdapter = new DetailsItemAdapter(mUser, mPassword, mUrl);
        mSuccessView.setAdapter(mAdapter);
        mSuccessView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.right = SizeUtils.dip2px(DetailsActivity.this, 5);
                outRect.left = SizeUtils.dip2px(DetailsActivity.this, 5);
                outRect.top = SizeUtils.dip2px(DetailsActivity.this, 5);
                outRect.left = SizeUtils.dip2px(DetailsActivity.this, 5);
            }
        });
    }

    private void loadStateView() {
        // 空的view
        mEmptyView = LayoutInflater.from(this).inflate(R.layout.fragment_empty, mDetailsRoot, false);
        mDetailsRoot.addView(mEmptyView);

        // 加载中的view
        mLoadingView = LayoutInflater.from(this).inflate(R.layout.fragment_loading, mDetailsRoot, false);
        mDetailsRoot.addView(mLoadingView);

        // 错误的view
        mErrorView = LayoutInflater.from(this).inflate(R.layout.fragment_error, mDetailsRoot, false);
        mDetailsRoot.addView(mErrorView);

        setUpState(State.NONE);

    }

    public void setUpState(State state) {
        this.currentState = state;
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

//    @Override
//    public void onDetailsLoadedSuccess(List<DetailsData> list) {
//        setUpState(State.SUCCESS);
//        mAdapter.setData(list);
//    }

    @Override
    public void onLoadedSuccess(List<DavResource> list) {
        setUpState(State.SUCCESS);
        mAdapter.setData(list);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        mFileListPresenter.unregisterViewCallback(this);
    }
}
