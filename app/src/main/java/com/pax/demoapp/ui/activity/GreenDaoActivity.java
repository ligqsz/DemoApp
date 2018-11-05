package com.pax.demoapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.db.greendao.bean.Student;
import com.pax.demoapp.db.greendao.bean.StudentDao;
import com.pax.demoapp.db.greendao.manager.DaoManager;
import com.pax.demoapp.ui.adapter.MenuAdapter;
import com.pax.utils.LogUtils;
import com.pax.demoapp.utils.OtherUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Arrays;
import java.util.List;

/**
 * @author ligq
 */
public class GreenDaoActivity extends AppCompatActivity implements MenuAdapter.MenuAdapterListener {

    private ScrollView rootLayout;
    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootLayout = new ScrollView(this);
        setContentView(rootLayout);
        initView();

    }

    private void initView() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params0);
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.background_light));
        rootLayout.addView(linearLayout);

        tvResult = new TextView(this);
        RecyclerView rv = new RecyclerView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        tvResult.setLayoutParams(params);
        tvResult.setText("Result:");
        tvResult.setTextSize(20);
        tvResult.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        rv.setLayoutParams(params);
        linearLayout.addView(tvResult);
        linearLayout.addView(rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new CommonAdapter<String>(this, android.R.layout.simple_list_item_1
                , Arrays.asList(MenuConfig.MENU_GREEN_DAO)) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(android.R.id.text1, s);
                holder.getView(android.R.id.text1).setOnClickListener(view -> onItemClick(position));
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        tvResult.setText("Result:");
        switch (position) {
            case 0:
                Student student = createStudent();
                try {
                    long insResult = DaoManager.getStudentDao().insert(student);
                    tvResult.append("row ID:" + insResult);
                } catch (Exception e) {
                    tvResult.append("insert failed:" + e.getMessage());
                }
                break;
            case 1:
                DaoManager.getStudentDao().deleteAll();
                tvResult.append("delete all");
                break;
            case 2:
                Student stu = null;
                try {
                    stu = DaoManager.getStudentDao().queryBuilder().where(
                            StudentDao.Properties.Name.eq("N")).list().get(0);
                } catch (Exception e) {
                    LogUtils.e(e);
                }
                if (stu != null) {
                    stu.setAge(OtherUtils.getRandomNum(30));
                    stu.setProvince(OtherUtils.getRandomWord());
                    DaoManager.getStudentDao().update(stu);
                    tvResult.append("before update:" + stu.toString() + "\n" +
                            "after update:" + stu.toString());
                } else {
                    tvResult.append("update failed");
                }
                break;
            case 3:
                List<Student> list = DaoManager.getStudentDao().queryBuilder().list();
                if (list == null || list.isEmpty()) {
                    tvResult.append("");
                } else {
                    tvResult.append("\n");
                    for (Student student1 : list) {
                        tvResult.append(student1.toString() + "\n");
                    }
                }

                break;
            default:
                break;
        }
    }

    private Student createStudent() {
        Student stu = new Student();
        stu.setAge(OtherUtils.getRandomNum(10));
        stu.setName(OtherUtils.getRandomWord());
        stu.setProvince(OtherUtils.getRandomWord());
        stu.setNumber(OtherUtils.getRandomNum());
        return stu;
    }
}
