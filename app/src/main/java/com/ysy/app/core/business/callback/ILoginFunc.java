package com.ysy.app.core.business.callback;

import org.base.library.bean.HttpRequestPackage;

/**
 * Created by YinShengyi on 2017/1/4.
 */
public interface ILoginFunc {
    void login(HttpRequestPackage httpRequestPackage, LoginResultListener listener);
}
