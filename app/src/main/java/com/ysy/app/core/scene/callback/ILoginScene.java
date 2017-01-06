package com.ysy.app.core.scene.callback;

/**
 * Created by YinShengyi on 2017/1/4.
 */
public interface ILoginScene {
    String getUserName();
    String getPassword();
    void clearUserName();
    void clearPassword();
    void loginSuccess();
    void loginFailed(String failedReason);
}
