package com.lhb.player.playerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.base.BaseActivity;
import com.lhb.player.playerapp.base.BaseFragment;
import com.lhb.player.playerapp.fragments.HomeFragment;
import com.lhb.player.playerapp.model.bean.WebdavResult;
import com.lhb.player.playerapp.utils.Constants;
import com.lhb.player.playerapp.utils.ShareUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.tool_bar)
    public Toolbar mToolbar;

    @BindView(R.id.root_view)
    public DrawerLayout mDrawerLayout;


    private HomeFragment mHomeFragment;
    //    private PersonalInfoFragment mPersonalInfoFragment;
    private FragmentManager mFm;
    //    private Bundle mWebDavBundle;
//    private Bundle mNasBundle;
    private WebdavResult mWebdavResult = new WebdavResult();

    @Override
    protected void initPresenter() {
    }

    public WebdavResult getWebdavResult() {
        return mWebdavResult;
    }

    @Override
    protected void initView() {
        initBundle();
        initFragment();
        mToolbar.inflateMenu(R.menu.menu);
        mToolbar.setNavigationIcon(R.mipmap.menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }


    private void initBundle() {
        ShareUtil.setUp(this);

        Intent intent = getIntent();
        if (intent != null) {
            mWebdavResult = intent.getParcelableExtra(Constants.KEY_WEBDAV_RESULTS);
            Log.d(TAG, "initBundle: mWebdavResult is -- > " + mWebdavResult);
        }
//        String webDavUser = ShareUtil.getStringValue(Constants.KEY_WEBDAV_USER);
//        String webDavPassword = ShareUtil.getStringValue(Constants.KEY_WEBDAV_PASSWORD);
//        String webDavUrl = ShareUtil.getStringValue(Constants.KEY_WEBDAV_URL);
//        mWebDavBundle = new Bundle();
//        mWebDavBundle.putString(Constants.KEY_WEBDAV_USER, webDavUser);
//        mWebDavBundle.putString(Constants.KEY_WEBDAV_PASSWORD, webDavPassword);
//        mWebDavBundle.putString(Constants.KEY_WEBDAV_URL, webDavUrl);
//
//        mNasBundle = new Bundle();
//        String nasUser = ShareUtil.getStringValue(Constants.KEY_NAS_USER);
//        String nasPassword = ShareUtil.getStringValue(Constants.KEY_NAS_PASSWORD);
//        String nasUrl = ShareUtil.getStringValue(Constants.KEY_NAS_URL);
//        mNasBundle.putString(Constants.KEY_NAS_USER, nasUser);
//        mNasBundle.putString(Constants.KEY_NAS_PASSWORD, nasPassword);
//        mNasBundle.putString(Constants.KEY_NAS_URL, nasUrl);
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction transaction = mFm.beginTransaction();
        if (targetFragment instanceof HomeFragment) {
            mToolbar.setTitle("webDav");
        } else {
            mToolbar.setTitle("NAS");
        }
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container, targetFragment);
        } else {
            transaction.show(targetFragment);
        }
        if (lastOneFragment != null && lastOneFragment != targetFragment) {
            transaction.hide(lastOneFragment);
        }

        lastOneFragment = targetFragment;
        transaction.commit();
    }

    private boolean b = false;

    @Override
    protected void initEvent() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    b = false;
                } else {
                    b = true;
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.action_logout_web:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("server", "webdav");
                        intent.putExtra("exit", true);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.action_refresh:
                        mHomeFragment.refresh();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getContextViewResId() {
        return R.layout.activity_main;
    }

}