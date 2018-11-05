package com.pax.demoapp.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.utils.RjTestUtils;
import com.pax.utils.LogUtils;

import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author ligq
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "Convert2Lambda", "RedundantThrows"})
public class RxJavaActivity extends AppCompatActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NestedScrollView scrollView = new NestedScrollView(this);
        LinearLayout.LayoutParams scrollParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(scrollParam);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        layout.setPadding(10, 10, 10, 10);
        scrollView.addView(layout);
        setContentView(scrollView);

        loadParam();
        addListeners();
    }

    private void loadParam() {
        List<String> testMenu = Arrays.asList(MenuConfig.MENU_RX_JAVA);
        addButtons(testMenu);
    }

    private void addButtons(List<String> testMenu) {
        for (String title : testMenu) {
            Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(params);
            button.setAllCaps(false);
            button.setText(title);
            layout.addView(button);
        }
    }


    @SuppressLint("CheckResult")
    private void addListeners() {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View childAt = layout.getChildAt(i);
            int finalI = i;
            RxView.clicks(childAt)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            return childAt instanceof Button;
                        }
                    })
                    .map(new Function<Object, Integer>() {
                        @Override
                        public Integer apply(Object o) throws Exception {
                            return finalI;
                        }
                    })
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            LogUtils.d("click-----" + integer.toString());
                            click(integer);
                        }
                    });
        }
    }

    private void click(Integer item) {
        switch (item) {
            case 0:
                RjTestUtils.testTimer();
                break;
            case 1:
                RjTestUtils.testInterval();
                break;
            case 2:
//                RjTestUtils.testSkip();
                break;
            case 3:
                RjTestUtils.testDebounce();
                break;
            case 4:
                RjTestUtils.testMergeDelayError();
                break;
            case 5:
                RjTestUtils.testSwitchOnNext();
                break;
            case 6:
                RjTestUtils.testDelay();
                break;
            case 7:
                RjTestUtils.testTimeInterval();
                break;
            case 8:
                RjTestUtils.testTimestamp();
                break;
            case 9:
                RjTestUtils.testSubscribeOn(layout);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        RjTestUtils.disposable();
    }
}
