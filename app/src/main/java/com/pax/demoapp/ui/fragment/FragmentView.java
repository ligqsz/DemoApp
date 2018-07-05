package com.pax.demoapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.ui.activity.PagerActivity;

/**
 * @author ligq
 * @date 2018/7/2
 */
public class FragmentView extends Fragment {

    private Bundle arg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arg = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);
        TextView tv = view.findViewById(R.id.tv);
        int page = arg.getInt(PagerActivity.PAGER_NUM);

        if (page == 1) {
            view.setBackgroundResource(R.color.colorAccent);
        } else if (page == 2) {
            view.setBackgroundResource(R.color.greed);
        } else if (page == 3) {
            view.setBackgroundResource(R.color.red);
        } else if (page == 4) {
            view.setBackgroundResource(R.color.colorPrimary);
        }

        tv.setText(arg.getString(PagerActivity.TITLE));
        return view;
    }


    public static FragmentView newInstance(Bundle args) {
        FragmentView fragment = new FragmentView();
        fragment.setArguments(args);
        return fragment;
    }

}
