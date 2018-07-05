package com.pax.demoapp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.pax.demoapp.R;
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

    public MenuAdapter(Context context, List<String> dataList) {
        super(context, R.layout.layout_menu_item, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_item, s);
        View view = holder.getView(R.id.ll_item);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , randomHeight());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.parseColor(randomColor()));
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
