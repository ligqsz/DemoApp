package com.pax.demoapp.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pax.demoapp.R;
import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.template.base.BaseActivity;
import com.pax.demoapp.ui.adapter.MenuAdapter;
import com.pax.utils.KeyboardUtils;
import com.pax.utils.ToastUtils;

import java.util.Arrays;
import java.util.List;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_GO;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;
import static android.view.inputmethod.EditorInfo.IME_ACTION_SEND;

/**
 * @author ligq
 */
public class EditTextActivity extends BaseActivity implements MenuAdapter.MenuAdapterListener {

    private List<String> dataMenu;
    private EditText et;
    private LinearLayout.LayoutParams params;
    private LinearLayout llEdit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_text;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        dataMenu = Arrays.asList(MenuConfig.MENU_EDIT);
        initView();
    }

    public void initView() {
        llEdit = findViewById(R.id.ll_edit_test);
        RecyclerView rv = new RecyclerView(this);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rv.setLayoutParams(params);
        rv.setLayoutManager(new LinearLayoutManager(this));
        MenuAdapter mAdapter = new MenuAdapter(this, dataMenu, true);
        mAdapter.setOnItemClick(this);
        rv.setAdapter(mAdapter);
        llEdit.addView(rv);

        createEditText();
        setEditTextListener();
    }

    private void createEditText() {
        et = new EditText(this);
        params.setMargins(20, 20, 20, 20);
        et.setLayoutParams(params);
        et.setImeOptions(IME_ACTION_SEARCH);
        et.setSingleLine(true);
        llEdit.addView(et);
    }

    @Override
    public void onItemClick(int position) {
        KeyboardUtils.hideSoftInput(et);
        llEdit.removeView(et);
        createEditText();
        switch (position) {
            case 0:
                et.setImeOptions(IME_ACTION_DONE);
                break;
            case 1:
                et.setImeOptions(IME_ACTION_GO);
                break;
            case 2:
                et.setImeOptions(IME_ACTION_NEXT);
                break;
            case 3:
                et.setImeOptions(IME_ACTION_NONE);
                break;
            case 4:
                et.setImeOptions(IME_ACTION_SEARCH);
                break;
            case 5:
                et.setImeOptions(IME_ACTION_SEND);
                break;
            default:
                break;
        }
        setEditTextListener();
    }

    private void setEditTextListener() {
        et.setOnEditorActionListener((v, actionId, event) -> {
            switch (actionId) {
                case IME_ACTION_DONE:
                    ToastUtils.showShort(dataMenu.get(0));
                    break;
                case IME_ACTION_GO:
                    ToastUtils.showShort(dataMenu.get(1));
                    break;
                case IME_ACTION_NEXT:
                    ToastUtils.showShort(dataMenu.get(2));
                    break;
                case IME_ACTION_NONE:
                    ToastUtils.showShort(dataMenu.get(3));
                    break;
                case IME_ACTION_SEARCH:
                    ToastUtils.showShort(dataMenu.get(4));
                    break;
                case IME_ACTION_SEND:
                    ToastUtils.showShort(dataMenu.get(5));
                    break;
                default:
                    break;
            }
            return false;
        });
    }

}
