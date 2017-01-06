package org.base.library.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jude.swipbackhelper.SwipeBackHelper;

import org.base.library.bean.MessageEvent;
import org.base.library.callback.IDialog;
import org.base.library.callback.PermissionsResultListener;
import org.base.library.dialog.LoadingDialog;
import org.base.library.utils.ActivityCollector;
import org.base.library.utils.MessageEventUtils;
import org.base.library.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YinShengyi on 2017/1/4.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity mActivity;
    private boolean mIsDestroyed = false; // 当前activity是否被销毁
    private boolean mInBackground = false; // 是否处于后台

    private MessageEventUtils mMessageEventUtils; // 总线消息工具
    private PermissionsResultListener mPermissionListener;  // 权限申请之后的监听
    private List<IDialog> mDialogs = new ArrayList<>(); // 所有依附于本activity的dialog
    private LoadingDialog mLoadingDialog; // 加载中对话框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mActivity = this;
        mIsDestroyed = false;
        ActivityCollector.put(this);
        mMessageEventUtils = new MessageEventUtils(new MessageEventUtils.OnProcessMessageEvent() {
            @Override
            public void onProcessMessageEvent(MessageEvent event) {
                processMessageEvent(event);
            }
        });
        mMessageEventUtils.register();

        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(true)
                .setSwipeRelateOffset(300)
                .setSwipeEdgePercent(0.05f);

        initView();
        initData();
        setListener();
        begin();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInBackground) {
            backgroundToFront();
            mInBackground = false;
        }
    }

    @Override
    protected void onStop() {
        mInBackground = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        for (IDialog dialog : mDialogs) {
            dialog.close();
        }
        ActivityCollector.remove(this);
        SwipeBackHelper.onDestroy(this);
        mMessageEventUtils.unregister();
        mIsDestroyed = true;
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.checkEachPermissionsGranted(grantResults)) {
            if (mPermissionListener != null) {
                mPermissionListener.onPermissionGranted();
            }
        } else {
            if (mPermissionListener != null) {
                mPermissionListener.onPermissionDenied();
            }
        }
    }

    /**
     * 获取本activity的布局文件
     */
    protected abstract int getContentViewId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置事件监听
     */
    protected abstract void setListener();

    /**
     * 开始执行操作指令
     */
    protected abstract void begin();

    /**
     * 总线消息处理
     */
    protected void processMessageEvent(MessageEvent event) {
    }

    /**
     * 当前activity由后台切入前台时执行此方法
     * 注意：此方法不可做耗时操作
     */
    protected void backgroundToFront() {
        for (BaseActivity activity : ActivityCollector.getAllActivity()) {
            activity.mInBackground = false;
        }
    }

    /**
     * @param desc        首次申请权限被拒绝后再次申请给用户的描述提示
     * @param permissions 要申请的权限数组
     * @param listener    实现的接口
     */
    protected void requestPermissions(String desc, String[] permissions, PermissionsResultListener listener) {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        mPermissionListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.checkEachSelfPermission(mActivity, permissions)) {// 检查是否声明了权限
                PermissionUtils.requestEachPermissions(mActivity, desc, permissions, 1);
            } else {// 已经申请权限
                if (mPermissionListener != null) {
                    mPermissionListener.onPermissionGranted();
                }
            }
        } else {
            if (mPermissionListener != null) {
                mPermissionListener.onPermissionGranted();
            }
        }
    }

    /**
     * 禁止activity右滑关闭
     */
    public void forbidSwipeFinishActivity() {
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }

    /**
     * 当前activity是否被销毁
     */
    public boolean isDestroyed() {
        return mIsDestroyed;
    }

    /**
     * 显示加载中对话框
     */
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, "");
        }
        mLoadingDialog.show();
    }

    /**
     * 关闭加载中对话框
     */
    public void closeLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.close();
        }
    }

    /**
     * 当前对话框依附于当前Activity中，每个Dialog必须使用此方法
     */
    public void attachDialog(IDialog dialog) {
        mDialogs.add(dialog);
    }

}
