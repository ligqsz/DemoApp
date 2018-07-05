package com.pax.demoapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.pax.demoapp.R;

/**
 * @author ligq
 * @date 2018/7/3
 */

public class CustomDialog extends Dialog {
    private String dialogType;
    static final String TYPE_DIALOG_CONFIRM = "type_dialog_confirm";
    static final String TYPE_DIALOG_PROGRESS = "type_dialog_progress";
    private FrameLayout dialogProgress;
    private DialogButtonListener dialogListener;
    private LinearLayout dialogConfirm;

    private CustomDialog(@NonNull Context context) {
        this(context, R.style.dialog_style);
    }

    CustomDialog(Context context, String dialogType) {
        this(context);
        this.dialogType = dialogType;
    }

    private CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.layout_dialog);
        //设置dialog的位置
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = -50;
        window.setAttributes(lp);

        //confirm dialog
        dialogConfirm = findViewById(R.id.dialog_confirm);
        Button btOk = findViewById(R.id.dialog_ok);
        Button btCancel = findViewById(R.id.dialog_cancel);
        btOk.setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onOk();
                CustomDialog.this.dismiss();
            }
        });

        btCancel.setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onCancel();
                CustomDialog.this.dismiss();
            }
        });
        //progress
        dialogProgress = findViewById(R.id.dialog_progress);
        showDialog();
    }

    private void showDialog() {
        if (TextUtils.isEmpty(dialogType)) {
            dialogType = TYPE_DIALOG_CONFIRM;
        }
        switch (dialogType) {
            case TYPE_DIALOG_CONFIRM:
                dialogConfirm.setVisibility(View.VISIBLE);
                break;
            case TYPE_DIALOG_PROGRESS:
                dialogProgress.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public interface DialogButtonListener {
        void onOk();

        void onCancel();
    }

    void setDialogListener(DialogButtonListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}
