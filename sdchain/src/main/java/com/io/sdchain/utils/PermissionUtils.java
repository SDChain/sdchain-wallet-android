package com.io.sdchain.utils;

import android.content.Context;

import com.io.sdchain.R;
import com.orhanobut.logger.Logger;
import com.xiey94.xydialog.dialog.XyDialog2;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.SettingService;

import java.util.List;

/**
 * @author : xiey
 * @project name : sdchian.
 * @package name  : com.io.sdchian.utils.
 * @date : 2018/6/27.
 * @signature : do my best.
 * @explain :
 */
public class PermissionUtils {
    private Context context;
    private String[] requestPermissions;
    private String errHint;
    private RequestPermission request;

    public PermissionUtils getInstance(Context context) {
        this.context = context;
        return this;
    }

    public PermissionUtils permissions(String... permissions) {
        this.requestPermissions = permissions;
        return this;
    }

    public PermissionUtils errHint(String errHint) {
        this.errHint = errHint;
        return this;
    }

    public PermissionUtils permission(RequestPermission request) {
        this.request = request;
        return this;
    }

    public void start() {
        AndPermission.with(context)
                .permission(requestPermissions)
                .onGranted(permissions -> {
                    //succeed
                    if (request != null) {
                        request.success(permissions);
                    }
                })
                .onDenied(permissions -> {
                    //failed
                    Logger.e("Application permission failed");
                    if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                        // These permissions are always denied by the user。
                        // Here, a Dialog is used to show that an application cannot continue to run without these permissions, asking the user whether to authorize in the Settings.
                        if (request != null) {
                            request.failed(permissions);
                        }
                        SettingService settingService = AndPermission.permissionSetting(context);
                        new XyDialog2.Builder(context)
                                .title(ResStringUtils.getString(R.string.info418))
                                .message(ResStringUtils.getString(R.string.info419) + errHint + ResStringUtils.getString(R.string.info420))
                                .setPositiveButtonListener(ResStringUtils.getString(R.string.info421), (o, dialog, i) -> {
                                    // If the user agrees to set:
                                    settingService.execute();
                                    dialog.dismiss();
                                })
                                .setNegativeButtonListener(ResStringUtils.getString(R.string.info422), (o, dialog, i) -> {
                                    // If the user does not agree to set it：
                                    settingService.cancel();
                                    dialog.dismiss();
                                })
                                .createNoticeDialog()
                                .show();
                    }
                })
                .rationale((context1, permissions, executor) -> {
                    Logger.e("Application permission denied");
                    if (request != null) {
                        request.refuse(context1, permissions, executor);
                    }
                    //reapply
                    executor.execute();
                })
                .start();
    }

    public RequestPermission requestPermission;

    public void setRequest(RequestPermission request) {
        this.requestPermission = request;
    }

    public interface RequestPermission {
        void success(List<String> permissions);

        default void failed(List<String> permissions) {
        }

        default void refuse(Context context, List<String> permissions, RequestExecutor executor) {
        }


    }
}
