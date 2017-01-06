package com.ysy.app.application;

import org.base.library.BuildConfig;
import org.base.library.application.BaseApplication;

/**
 * Created by YinShengyi on 2017/1/4.
 */
public class AppApplication extends BaseApplication {
    @Override
    protected boolean isRelease() {
        return "release".equals(BuildConfig.BUILD_TYPE);
    }
}
