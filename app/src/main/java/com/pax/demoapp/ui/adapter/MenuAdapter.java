package com.pax.demoapp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.pax.demoapp.R;
import com.pax.demoapp.config.MenuConfig;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Random;

/**
 * @author ligq
 * @date 2018/7/5
 */

public class MenuAdapter extends CommonAdapter<String> {
    private MenuAdapterListener onItemClick;
    private boolean sameHeight;

    public MenuAdapter(Context context, List<String> dataList) {
        super(context, R.layout.layout_menu_item, dataList);
    }

    public MenuAdapter(Context context, List<String> dataList, boolean sameHeight) {
        super(context, R.layout.layout_menu_item, dataList);
        this.sameHeight = sameHeight;
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_item, s);
        View view = holder.getView(R.id.ll_item);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , sameHeight ? MenuConfig.DEFAULT_HEIGHT : randomHeight());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.parseColor(randomColor()));
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setBackgroundColor(Color.parseColor(randomColor()));
                    break;
                case MotionEvent.ACTION_UP:
                    v.setBackgroundColor(Color.parseColor(randomColor()));
                    v.performClick();
                    return true;
                default:
                    break;
            }
            return false;
        });
        view.setOnClickListener(v -> {
            if (onItemClick != null) {
                onItemClick.onItemClick(position);
            }
        });

    }

    public interface MenuAdapterListener {
        void onItemClick(int position);
    }

    public void setOnItemClick(MenuAdapterListener onItemClick) {
        this.onItemClick = onItemClick;
    }


    private String randomColor() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        for (int i = 0; i < 6; i++) {
            int temp = random.nextInt(15) + 1;
            sb.append(Integer.toHexString(temp));
        }
        return sb.toString();
    }

    private int randomHeight() {
        return new Random().nextInt(200) + 100;
    }
}
