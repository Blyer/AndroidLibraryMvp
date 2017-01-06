package com.ysy.app.core.scene.activity;

import android.Manifest;
import android.view.View;

import com.ysy.app.R;
import com.ysy.app.core.scene.activity.base.AppBaseActivity;

import org.base.library.callback.PermissionsResultListener;
import org.base.library.utils.BaseUtils;
import org.base.library.utils.StatusBarUtils;
import org.base.library.utils.ToastUtils;
import org.base.library.view.UnifyButton;

public class MainActivity extends AppBaseActivity implements View.OnClickListener {

    private UnifyButton btn_1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btn_1 = (UnifyButton) findViewById(R.id.btn_1);
    }

    @Override
    protected void initData() {
        StatusBarUtils.compat(mActivity, getResources().getColor(R.color.blue_1));
        forbidSwipeFinishActivity();
    }

    @Override
    protected void setListener() {
        btn_1.setOnClickListener(this);
    }

    @Override
    protected void begin() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                requestPermissions("没这个权限没法拨打电话哦~", new String[]{Manifest.permission.CALL_PHONE}, new PermissionsResultListener() {
                    @Override
                    public void onPermissionGranted() {
                        BaseUtils.callPhone(mActivity, "10086");
                    }

                    @Override
                    public void onPermissionDenied() {
                        ToastUtils.show("您拒绝了拨打电话的权限");
                    }
                });
                break;
        }
    }

}
