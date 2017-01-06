package com.ysy.app.core.scene.activity;

import android.view.View;
import android.widget.EditText;

import com.ysy.app.R;
import com.ysy.app.base.AppBaseActivity;
import com.ysy.app.core.presenter.LoginPresenter;
import com.ysy.app.core.scene.callback.ILoginScene;

import org.base.library.bean.HttpRequestPackage;
import org.base.library.utils.StatusBarUtils;
import org.base.library.utils.ToastUtils;
import org.base.library.view.UnifyButton;

public class LoginActivity extends AppBaseActivity implements ILoginScene {

    private EditText et_user_name;
    private EditText et_password;
    private UnifyButton btn_login;

    private LoginPresenter mPresenter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (UnifyButton) findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {
        StatusBarUtils.compat(this, getResources().getColor(R.color.blue_1));
        forbidSwipeFinishActivity();
        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void setListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                mPresenter.login(generateLoginRequestPackage());
            }
        });
    }

    @Override
    protected void begin() {

    }

    @Override
    public String getUserName() {
        return et_user_name.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return et_password.getText().toString().trim();
    }

    @Override
    public void clearUserName() {
        et_user_name.setText("");
    }

    @Override
    public void clearPassword() {
        et_password.setText("");
    }

    @Override
    public void loginSuccess() {
        closeLoadingDialog();
        ToastUtils.show("登录成功");
    }

    @Override
    public void loginFailed(String failedReason) {
        closeLoadingDialog();
        ToastUtils.show(failedReason);
    }

    private HttpRequestPackage generateLoginRequestPackage() {
        HttpRequestPackage httpRequestPackage = new HttpRequestPackage();
        httpRequestPackage.params.put("userName", getUserName());
        httpRequestPackage.params.put("password", getPassword());
        return httpRequestPackage;
    }
}
