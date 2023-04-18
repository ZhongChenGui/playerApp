package com.lhb.player.playerapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.costom.LoginEditView;
import com.lhb.player.playerapp.costom.MyCheckBox;
import com.lhb.player.playerapp.model.bean.WebdavResult;
import com.lhb.player.playerapp.utils.CheckDataUtil;
import com.lhb.player.playerapp.utils.Constants;
import com.lhb.player.playerapp.utils.ShareUtil;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by howe.zhong
 * on 2022/8/15  16:19
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Unbinder mBind;

    @BindView(R.id.login_top)
    public Toolbar mToolbar;

    @BindView(R.id.protocol)
    public LinearLayout mProtocol;

    @BindView(R.id.protocol_text)
    public TextView mProtocolText;

    @BindView(R.id.url)
    public EditText url;

    @BindView(R.id.user)
    public LoginEditView user;
    //
    @BindView(R.id.password)
    public LoginEditView password;
    //
    @BindView(R.id.port)
    public LoginEditView port;

    @BindView(R.id.check_password)
    public MyCheckBox mCheckPassword;

    @BindView(R.id.auto_login)
    public MyCheckBox mAutoLogin;
    //
    @BindView(R.id.connect)
    public TextView connect;    //

    @BindView(R.id.auto_login_tip)
    public TextView auto_login_tip;


    private Handler mHandler;
    private boolean mRememberPassword;  // 是否记住密码
    private boolean mAutoLoginB;   // 是否自动登录
    private boolean isExit = false;   // 是否自动登录
    private String mWebDavUser;
    private String mWebDavPassword;
    private String mWebDavUrl;

    private textCallback mCallback = new textCallback() {
        @Override
        public void onCall(boolean success, WebdavResult webdavResult) {
            if (auto_login_tip != null) {
                auto_login_tip.setVisibility(View.GONE);
            }
            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(Constants.KEY_WEBDAV_RESULTS, webdavResult);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "连接失败！请检查是否存在 TetePlay 文件夹或者账号密码", Toast.LENGTH_LONG).show();
            }
        }
    };
    private String webDavPort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            isExit = intent.getBooleanExtra("exit", false);
            Log.d(TAG, "onCreate: isExit is -- > " + isExit);
        }
        setContentView(R.layout.activity_login);
        mHandler = new Handler();
        mBind = ButterKnife.bind(this);
        initView();
        ShareUtil.setUp(this);
        mAutoLoginB = ShareUtil.getBooleanValue(Constants.KEY_WEBDAV_AUTO_LOGIN);
        isHttps = ShareUtil.getBooleanValue(Constants.KEY_WEBDAV_IS_HTTPS);
        if (isHttps) {
            mProtocolText.setText("https");
        } else {
            mProtocolText.setText("http");
        }
        mRememberPassword = ShareUtil.getBooleanValue(Constants.KEY_WEBDAV_REMEMBER_PASSWORD);
        mWebDavUser = ShareUtil.getStringValue(Constants.KEY_WEBDAV_USER);
        mWebDavPassword = ShareUtil.getStringValue(Constants.KEY_WEBDAV_PASSWORD);
        webDavPort = ShareUtil.getStringValue(Constants.KEY_WEBDAV_PORT);
        mWebDavUrl = ShareUtil.getStringValue(Constants.KEY_WEBDAV_URL);
        mCheckPassword.setCheck(mRememberPassword);
        mAutoLogin.setCheck(mAutoLoginB);
        initListener();
        if (mAutoLoginB && !isExit) {
            if (!TextUtils.isEmpty(mWebDavUser) && !TextUtils.isEmpty(mWebDavPassword) && !TextUtils.isEmpty(mWebDavUrl)) {
                auto_login_tip.setVisibility(View.VISIBLE);
                textLink(mWebDavUser, mWebDavPassword, mWebDavUrl, mCallback);
            }
        }
        if (mRememberPassword) {
            user.setText(mWebDavUser);
            password.setText(mWebDavPassword);
            port.setText(webDavPort);
        }
        if (!TextUtils.isEmpty(mWebDavUrl)) {
            Uri uri = Uri.parse(mWebDavUrl);
            url.setText(uri.getHost() + uri.getPath());
        }
//        user.setText("p1");
//        password.setText("zcg20010911");
//        port.setText("80");
//        url.setText("42.193.12.9/webdav");
    }

    private boolean isHttps = true;

    private void initListener() {
        mProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: isHttps is -- > " + isHttps);
                if (isHttps) {
                    mProtocolText.setText("http");
                    isHttps = false;
                } else {
                    mProtocolText.setText("https");
                    isHttps = true;
                }
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLink();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_webdav:
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private void clickLink() {
        String tarUrl = url.getText().toString().trim();
        if (tarUrl.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入正确的url地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isHttps) {
            if (!tarUrl.startsWith("https://")) {
                tarUrl = "https://" + tarUrl;
            }
        } else {
            if (!tarUrl.startsWith("http://")) {
                tarUrl = "http://" + tarUrl;
            }
        }

        String portText = port.getText().trim();
        Uri uri = Uri.parse(tarUrl);
        if (!TextUtils.isEmpty(portText) && uri.getPort() == -1) {
            String scheme = uri.getScheme();
            String host = uri.getHost();
            String path = uri.getPath();
            tarUrl = scheme + "://" + host + ":" + portText + path;
        }
        Log.d(TAG, "clickLink: tarUrl is -- > " + tarUrl);
        String tarUser = user.getText().trim();
        if (tarUser.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        String tarPassword = password.getText().trim();
        if (tarPassword.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        textLink(tarUser, tarPassword, tarUrl, mCallback);
    }

    private void initView() {
        mToolbar.inflateMenu(R.menu.login_menu);
    }

    private void textLink(String user, String pas, String url, textCallback callback) {
        auto_login_tip.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpSardine okHttpSardine = new OkHttpSardine();
                    okHttpSardine.setCredentials(user, pas);
                    WebdavResult webdavResult = new WebdavResult();
                    List<WebdavResult.WebdavResultBean> webdavResults = new ArrayList<>();
                    List<DavResource> list = okHttpSardine.list(url + "/" + Constants.HOME_DIRECTORY);
                    if (list.size() > 1) {
                        list = list.subList(1, list.size());
                        List<DavResource> davResourceList = CheckDataUtil.checkDirectory(list);
                        for (DavResource davResource : davResourceList) {
                            URI href = davResource.getHref();
                            String name = davResource.getName();
                            webdavResults.add(new WebdavResult.WebdavResultBean(name, href.toString()));
                        }
                    }
                    webdavResult.setBeans(webdavResults);
                    ShareUtil.saveValue(Constants.KEY_WEBDAV_URL, url);
                    ShareUtil.saveValue(Constants.KEY_WEBDAV_USER, user);
                    ShareUtil.saveValue(Constants.KEY_WEBDAV_PASSWORD, pas);
                    ShareUtil.saveBoolean(Constants.KEY_WEBDAV_IS_HTTPS, isHttps);
                    int portText = Uri.parse(url).getPort();
                    ShareUtil.saveValue(Constants.KEY_WEBDAV_PORT, portText == -1 ? "" : String.valueOf(portText));
                    ShareUtil.saveBoolean(Constants.KEY_WEBDAV_AUTO_LOGIN, mAutoLogin.getCheck());
                    ShareUtil.saveBoolean(Constants.KEY_WEBDAV_REMEMBER_PASSWORD, mCheckPassword.getCheck());
                    postCallback(true, callback, webdavResult);
                } catch (Exception e) {
                    postCallback(false, callback, null);
                }

            }
        }).start();
    }

    private void postCallback(boolean b, textCallback callback, WebdavResult webdavResults) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onCall(b, webdavResults);
            }
        });
    }


    private interface textCallback {
        void onCall(boolean success, WebdavResult webdavResults);

    }

    @Override
    protected void onDestroy() {
        if (mBind != null) {
            mBind.unbind();
        }
        super.onDestroy();
    }
}
