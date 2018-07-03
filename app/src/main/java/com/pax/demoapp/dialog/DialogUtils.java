package com.pax.demoapp.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * @author ligq
 * @date 2018/7/3
 */

public class DialogUtils {
    @SuppressLint("StaticFieldLeak")
    private static CustomDialog mDialog;

    private DialogUtils() {
        throw new IllegalArgumentException();
    }

    public static void showConfirmDialog(Context context, CustomDialog.DialogButtonListener listener) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }
        CustomDialog dialog = getDialog(context, CustomDialog.TYPE_DIALOG_CONFIRM
                , null, false, false);
        dialog.setDialogListener(listener);
    }

    public static void showProgressDialog(Context context) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }
        getDialog(context, CustomDialog.TYPE_DIALOG_PROGRESS
                , null, false, true);
    }

    private static CustomDialog getDialog(Context context, String type, DialogInterface.OnDismissListener dismissListener
            , boolean isTouchOutDismiss, boolean isBackDismiss) {
        dismissDialog();
        CustomDialog dialog = new CustomDialog(context, type);
        mDialog = dialog;
        dialog.show();
        dialog.setOnDismissListener(dismissListener);
        dialog.setCanceledOnTouchOutside(isTouchOutDismiss);
        if (!isBackDismiss) {
            dialog.setOnKeyListener((dialog1, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
        }
        return dialog;
    }

    public static void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
