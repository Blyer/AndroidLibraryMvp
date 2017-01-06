package org.base.library.callback;

/**
 * Created by YinShengyi on 2016/12/9.
 */
public interface PermissionsResultListener {
    void onPermissionGranted();

    void onPermissionDenied();
}
