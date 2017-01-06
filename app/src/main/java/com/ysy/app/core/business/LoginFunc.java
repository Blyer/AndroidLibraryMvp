package com.ysy.app.core.business;

import com.ysy.app.constants.UrlConstants;
import com.ysy.app.core.business.callback.ILoginFunc;
import com.ysy.app.core.business.callback.LoginResultListener;

import org.base.library.bean.HttpRequestPackage;
import org.base.library.bean.ResponseResult;
import org.base.library.enums.HttpMethod;
import org.base.library.utils.HttpUtils;

/**
 * Created by YinShengyi on 2017/1/4.
 */
public class LoginFunc implements ILoginFunc {
    private HttpUtils mHttpUtils = new HttpUtils();

    @Override
    public void login(HttpRequestPackage httpRequestPackage, final LoginResultListener listener) {
        httpRequestPackage.url = UrlConstants.host + "/login.txt";
        httpRequestPackage.method = HttpMethod.GET;
        mHttpUtils.request(httpRequestPackage, new HttpUtils.OnRequestListener() {
            @Override
            public void success(ResponseResult result) {
                if (result.getCode() == 0) {
                    if (listener != null) {
                        listener.loginSucess();
                    }
                } else {
                    listener.loginFailed(result.getMessage());
                }
            }

            @Override
            public void failed(String reason) {
                if (listener != null) {
                    listener.loginFailed(reason);
                }
            }
        });
    }
}
