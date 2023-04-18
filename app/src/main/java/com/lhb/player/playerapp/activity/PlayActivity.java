package com.lhb.player.playerapp.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.adapter.NumListAdapter;
import com.lhb.player.playerapp.base.BaseActivity;
import com.lhb.player.playerapp.presenters.Impl.FileListPresenterImpl;
import com.lhb.player.playerapp.utils.CodeUtil;
import com.lhb.player.playerapp.utils.Constants;
import com.lhb.player.playerapp.utils.ShareUtil;
import com.lhb.player.playerapp.utils.UrlUtils;
import com.lhb.player.playerapp.view.IFileListCallback;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.io.FileDescriptor;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by howe.zhong
 * on 2022/8/11  17:39
 */
public class PlayActivity extends AppCompatActivity implements NumListAdapter.OnNumClickListener, IFileListCallback {

    private static final String TAG = "PlayActivity";
    @BindView(R.id.nice_video_player)
    public NiceVideoPlayer mNiceVideoPlayer;

    @BindView(R.id.num_list)
    public RecyclerView mNumList;

    @BindView(R.id.play_top_menu_back)
    public ImageView back;
    @BindView(R.id.play_root)
    public ViewGroup play_root;
    @BindView(R.id.play_success)
    public ViewGroup mSuccessView;


    private String mHref;
    //    private String[] mData;
    private NumListAdapter mAdapter;
    private String currentUrl;
    private TxVideoPlayerController mController;
    private String mUrl;
    private String mUser;
    private String mPassword;
    private FileListPresenterImpl mFileListPresenter;
    private Unbinder mBind;
    private View mEmptyView;
    private View mLoadingView;
    private View mErrorView;
    private State currentState = State.NONE;
    private String mTitle;
    private String mDefaultTitle;
    private DavResource currentPlay;
    private List<DavResource> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mBind = ButterKnife.bind(this);
        if (checkIntent()) return;
        initView();
        initPresenter();
        initListener();
        initData();
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
        mFileListPresenter = new FileListPresenterImpl();
        mFileListPresenter.registerViewCallback(this);
    }

//    @Override
//    protected void initPresenter() {
//        mFileListPresenter = new FileListPresenterImpl();
//        mFileListPresenter.registerViewCallback(this);
//    }
//
//    @Override
//    protected void initEvent() {
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }


    private void initView() {

        ShareUtil.setUp(this);
        mUrl = ShareUtil.getStringValue(Constants.KEY_WEBDAV_URL);
        mUrl = UrlUtils.getDomain(mUrl);
        mUser = ShareUtil.getStringValue(Constants.KEY_WEBDAV_USER);
        mPassword = ShareUtil.getStringValue(Constants.KEY_WEBDAV_PASSWORD);

        loadStateView();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, RecyclerView.VERTICAL, false);
        mNumList.setLayoutManager(gridLayoutManager);
        mAdapter = new NumListAdapter();

//        Arrays.sort(mData);
//        Log.d(TAG, "initView: mData " + mData);
//        mAdapter.setData(mData);

        mAdapter.setOnNumClickListener(this);
        mNumList.setAdapter(mAdapter);
//        mNiceVideoPlayer.continueFromLastPosition(false);
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
    }

    @SuppressLint("CheckResult")
    private void initPlayer() {
        String authHeader = CodeUtil.getB64Auth(mUser, mPassword);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", authHeader);
        mNiceVideoPlayer.releasePlayer();
        mNiceVideoPlayer.setUp(mUrl + currentUrl, headers);
        Log.d(TAG, "initPlayer: mUrl + currentUrl is - > " + mUrl + currentUrl);
//        mNiceVideoPlayer.setUp("https://bvcdn.linkbroad.com/broadview/f526886f73b928c6772705021b18e01c/60ed363872d7f1f7a63ece99.mp4", null);

        mController = new TxVideoPlayerController(this);
        mController.setTitle(mDefaultTitle + " - " + mTitle);
        mController.setLenght(98000);
//        RequestOptions options = new RequestOptions();
//        options.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL)
//                .frame(1000000)
//                .centerCrop();
//        Glide.with(this)
//                .setDefaultRequestOptions(options)
//                .load(new GlideUrl(mUrl + currentUrl, header))
//                .centerCrop()
//                .into(mController.imageView());

        LazyHeaders header = new LazyHeaders.Builder().addHeader("Authorization", authHeader).build();
        Glide.with(this.getApplicationContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .error(R.drawable.video))
                .load(new GlideUrl(mUrl + currentUrl, header))
                .into(mController.imageView());

        Log.d(TAG, "initPlayer: .....................");
        mNiceVideoPlayer.setController(mController);
        mNiceVideoPlayer.setOnVideoCompletionListener(new NiceVideoPlayer.OnVideoCompletionListener() {
            @Override
            public void onVideoCompletion() {
                int i = mDatas.indexOf(currentPlay);
                if (i < mDatas.size() - 1) {
                    playNextVideo(mDatas.get(i + 1));
                }
            }
        });
    }

    private void initData() {
        mFileListPresenter.getFileList(mUrl + mHref, mUser, mPassword, true);
    }

    private void loadStateView() {
        // 空的view
        mEmptyView = LayoutInflater.from(this).inflate(R.layout.fragment_empty, play_root, false);
        play_root.addView(mEmptyView);

        // 加载中的view
        mLoadingView = LayoutInflater.from(this).inflate(R.layout.fragment_loading, play_root, false);
        play_root.addView(mLoadingView);

        // 错误的view
        mErrorView = LayoutInflater.from(this).inflate(R.layout.fragment_error, play_root, false);
        play_root.addView(mErrorView);

        setUpState(State.NONE);

    }

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    public void setUpState(State state) {
        this.currentState = state;
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    private boolean checkIntent() {
        Intent intent = getIntent();
        if (intent != null) {
//            mTitle = intent.getStringExtra("title");
            mHref = intent.getStringExtra("href");
            mDefaultTitle = intent.getStringExtra("title");
            mDefaultTitle = UrlUtils.decode(mDefaultTitle);
//            mData = intent.getStringArrayExtra("data");
//            if (mHref == null || mData == null || mData.length <= 0) {
            if (mHref == null) {
                Log.e(TAG, "onCreate: 请携带参数..");
                Toast.makeText(this, "没有任何数据!", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
        } else {
            Log.e(TAG, "onCreate: 请携带参数..");
            Toast.makeText(this, "请携带参数!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mNiceVideoPlayer.isPlaying()) {
            mNiceVideoPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNiceVideoPlayer != null && mNiceVideoPlayer.isPaused()) {
            mNiceVideoPlayer.restart();
        }
    }

    @Override
    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        // 所以在Activity中onBackPress要交给NiceVideoPlayer先处理。
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind = null;
        }
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onNumClick(DavResource davResource) {
        String url = davResource.getHref().toString();
        if (currentUrl.equals(url)) {
            return;
        }
        playNextVideo(davResource);
    }

    private void playNextVideo(DavResource davResource) {
        currentPlay = davResource;
        currentUrl = currentPlay.getHref().toString();
        mController.setTitle(mDefaultTitle + " - " + UrlUtils.decode(currentPlay.getName()));
        mNiceVideoPlayer.releasePlayer();
        String authHeader = CodeUtil.getB64Auth(mUser, mPassword);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", authHeader);
        mNiceVideoPlayer.setUp(mUrl + currentUrl, headers);
        mNiceVideoPlayer.start();
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onLoadedSuccess(List<DavResource> list) {
        setUpState(State.SUCCESS);
        Collections.sort(list, new Comparator<DavResource>() {
            @Override
            public int compare(DavResource o1, DavResource o2) {
                String name1 = o1.getName();
                String name2 = o2.getName();
                try {
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(name1);
                    Matcher m2 = p.matcher(name2);
                    m.find();
                    m2.find();
                    return Integer.parseInt(m.group()) - Integer.parseInt(m2.group());
                } catch (Exception e) {
                    return name1.compareTo(name2);
                }

            }
        });
        this.mDatas = list;
        Log.d(TAG, "onLoadedSuccess: mDatas is -- > " + mDatas);
        currentPlay = list.get(0);
        currentUrl = String.valueOf(currentPlay.getHref());
        mTitle = UrlUtils.decode(currentPlay.getName());
        mAdapter.setData(mDatas);
        initPlayer();
    }
}
