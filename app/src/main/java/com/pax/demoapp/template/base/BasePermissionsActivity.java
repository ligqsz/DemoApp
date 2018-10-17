package com.pax.demoapp.template.base;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity权限基类
 *
 * @author ligq
 * @date 2018/10/17
 */

@SuppressWarnings({"Convert2Lambda", "unused"})
@SuppressLint("Registered")
public class BasePermissionsActivity extends AppCompatActivity {
    public static final String TAG = "BasePermissionsActivity";
    public int requestCode = 0;

    /**
     * 请求权限
     *
     * @param permissions permissions
     * @param requestCode requestCode
     */
    public void requestPermission(String[] permissions, int requestCode) {
        this.requestCode = requestCode;
        //检查权限是否授权
        if (checkPermissions(permissions)) {
            permissionSucceed(requestCode);
        } else {
            List<String> needPermissions = getPermissions(permissions);
            ActivityCompat.requestPermissions(this,
                    needPermissions.toArray(new String[0]), this.requestCode);
        }
    }

    /**
     * 获取权限
     *
     * @param permissions permissions
     * @return List<String>
     */
    private List<String> getPermissions(String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.requestCode) {
            if (verificationPermissions(grantResults)) {
                permissionSucceed(this.requestCode);
            } else {
                permissionFailing(this.requestCode);
                showFailingDialog();
            }
        }
    }

    /**
     * 验证权限
     *
     * @param results results
     * @return boolean
     */
    private boolean verificationPermissions(int[] results) {
        for (int result : results) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions permissions
     * @return boolean
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(BasePermissionsActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 无权限弹出系统提示框
     */
    private void showFailingDialog() {
        new AlertDialog.Builder(this)
                .setTitle("系统提示")
                .setMessage("请求权限失败，是否手动开启？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSettings();
                    }
                }).show();
    }

    /**
     * 打开权限设置界面
     */
    private void startSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    public void permissionFailing(int code) {
        Log.d(TAG, "获取权限失败=" + code);
    }

    public void permissionSucceed(int code) {
        Log.d(TAG, "获取权限成功=" + code);
    }
}