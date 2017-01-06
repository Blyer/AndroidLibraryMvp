package com.ysy.app.core.business.callback;

/**
 * Created by YinShengyi on 2017/1/4.
 */
public interface LoginResultListener {
    void loginSucess();
    void loginFailed(String failedReason);
}
