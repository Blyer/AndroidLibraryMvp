package com.ysy.app.core.presenter;

import com.ysy.app.core.business.LoginFunc;
import com.ysy.app.core.business.callback.ILoginFunc;
import com.ysy.app.core.business.callback.LoginResultListener;
import com.ysy.app.core.scene.callback.ILoginScene;

import org.base.library.bean.HttpRequestPackage;

/**
 * Created by YinShengyi on 2017/1/4.
 */
public class LoginPresenter {
    private ILoginScene mLoginScene;
    private ILoginFunc mLoginFunc;

    public LoginPresenter(ILoginScene scene) {
        mLoginScene = scene;
        mLoginFunc = new LoginFunc();
    }

    public void login(HttpRequestPackage httpRequestPackage) {
        mLoginFunc.login(httpRequestPackage, new LoginResultListener() {
            @Override
            public void loginSucess() {
                mLoginScene.loginSuccess();
            }

            @Override
            public void loginFailed(String failedReason) {
                mLoginScene.loginFailed(failedReason);
            }
        });
    }
}
