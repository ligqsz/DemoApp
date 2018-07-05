package com.pax.demoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.pax.demoapp.R;
import com.pax.demoapp.dialog.CustomDialog;
import com.pax.demoapp.dialog.DialogUtils;
import com.pax.demoapp.utils.Density;

/**
 * @author ligq
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Density.setDensity(getApplication(), this);

        findViewById(R.id.alert_confirm).setOnClickListener(view -> DialogUtils.showConfirmDialog(MainActivity.this, new CustomDialog.DialogButtonListener() {
            @Override
            public void onOk() {
                Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        }));

        findViewById(R.id.alert_progress).setOnClickListener(view -> DialogUtils.showProgressDialog(MainActivity.this));
    }

    /**
     * ActivityUtils方式三调用,必须要重写onNewIntent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExitApp = intent.getBooleanExtra("exit", false);
            if (isExitApp) {
                this.finish();
            }
        }
    }
}
