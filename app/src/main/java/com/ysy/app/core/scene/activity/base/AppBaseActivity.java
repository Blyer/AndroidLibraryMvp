package com.ysy.app.core.scene.activity.base;

import org.base.library.activity.BaseActivity;
import org.base.library.bean.MessageEvent;
import org.base.library.bean.ResponseResult;
import org.base.library.constants.MsgEventConstants;
import org.base.library.dialog.UnifyDialog;
import org.base.library.utils.ActivityCollector;
import org.base.library.utils.ToastUtils;

/**
 * Created by YinShengyi on 2017/1/4.
 */
public abstract class AppBaseActivity extends BaseActivity {
    @Override
    protected void backgroundToFront() {
        ToastUtils.show("切入前台");
        super.backgroundToFront();
    }

    @Override
    protected void processMessageEvent(MessageEvent event) {
        super.processMessageEvent(event);
        switch (event.id) {
            case MsgEventConstants.NET_REQUEST_RESULT:
                ResponseResult responseResult = (ResponseResult) event.data;
                if (responseResult.getCode() == 1) {
                    UnifyDialog dialog = new UnifyDialog(mActivity, "", "该账号已在其他设备登录", "知道了");
                    dialog.setOnRightBtnClickListener(new UnifyDialog.OnRightBtnClickListener() {
                        @Override
                        public void onRightBtnClick() {
                            ActivityCollector.finishAll();
                        }
                    });
                    dialog.show();
                }
                break;
        }
    }
}
