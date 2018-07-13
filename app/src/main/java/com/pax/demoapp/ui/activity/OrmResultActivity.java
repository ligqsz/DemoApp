package com.pax.demoapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.bean.Student;
import com.pax.demoapp.config.IntentKeys;
import com.pax.demoapp.db.orm.dao.StudentDao;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author ligq
 */
public class OrmResultActivity extends AppCompatActivity implements IActivity {
    private List<Student> students;
    private String result = "";
    private static final String[] WORDS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",};

    @Override
    public int getLayoutId() {
        return R.layout.activity_orm_result;
    }

    @Override
    public void initData() {
        students = new ArrayList<>();
        StudentDao dao = StudentDao.getInstance();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        boolean operateResult;
        switch (bundle.getInt(IntentKeys.ORM_POSITION)) {
            case 0:
                Student student = createStudent();
                operateResult = dao.insertData(student);
                result = "insert:" + operateResult + "\n" +
                        "student:" + student.toString();
                break;
            case 1:
                int randomNum = getRandomNum(0);
                operateResult = dao.deleteDataById(randomNum);
                result = "deleteById:" + operateResult + "\n" +
                        "id:" + randomNum;
                break;
            case 2:
                operateResult = dao.deleteAllSql();
                result = "deleteAllSql:" + operateResult;
                break;
            case 3:
                int randomNum1 = getRandomNum(0);
                Student dataById = dao.findDataById(randomNum1);
                if (dataById == null) {
                    result = "update failed:no id = " + randomNum1;
                    return;
                }
                dataById.setAge(getRandomNum(10));
                operateResult = dao.updateData(dataById);
                result = "updateData:" + operateResult + "\n" +
                        "student:" + dataById.toString() + "\n" +
                        "id:" + randomNum1;
                break;
            case 4:
                int randomNum2 = getRandomNum(0);
                Student dataById1 = dao.findDataById(randomNum2);
                if (dataById1 == null) {
                    result = "query failed:no id = " + randomNum2;
                    return;
                }
                result = "id:" + randomNum2;
                students.add(dataById1);
                break;
            case 5:
                List<Student> allData = dao.findAllData();
                students.addAll(allData);
                break;
            case 6:
                List<Student> dataList = dao.findDataList(10);
                students.addAll(dataList);
                break;
            case 7:
                long[] sumOf = dao.countSumOf("d");
                result = "count:" + sumOf[0] + "\n" +
                        "total age:" + sumOf[1];
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        RecyclerView rvResult = findViewById(R.id.rv_orm_result);
        TextView tvResult = findViewById(R.id.operate_result);
        rvResult.setVisibility(students.isEmpty() ? View.GONE : View.VISIBLE);
        tvResult.setVisibility(TextUtils.isEmpty(result) ? View.GONE : View.VISIBLE);
        tvResult.setText(result);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        rvResult.setAdapter(new CommonAdapter<Student>(this, android.R.layout.simple_list_item_1, students) {
            @Override
            protected void convert(ViewHolder holder, Student student, int position) {
                holder.setText(android.R.id.text1, student.toString());
            }
        });
    }

    private Student createStudent() {
        Student student = new Student();
        student.setAge(getRandomNum(10));
        student.setName(getRandomWord());
        student.setNumber(getRandomNum());
        student.setProvince(getRandomWord());
        return student;
    }

    private int getRandomNum(int baseNum) {
        return new Random().nextInt(10) + baseNum;
    }

    public String getRandomNum() {
        String num = String.valueOf(new Random().nextInt(100));
        StudentDao dao = StudentDao.getInstance();
        while (!dao.findListByNum(num).isEmpty()) {
            num = String.valueOf(new Random().nextInt(100));
        }
        return num;
    }

    private String getRandomWord() {
        return WORDS[new Random().nextInt(WORDS.length)];
    }
}
