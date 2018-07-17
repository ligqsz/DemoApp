package com.pax.demoapp.db.greendao.manager;

import com.pax.demoapp.db.greendao.bean.SchoolDao;
import com.pax.demoapp.db.greendao.bean.StudentDao;
import com.pax.demoapp.db.greendao.bean.TeacherDao;

/**
 * @author ligq
 * @date 2018/7/17
 */

public class DaoOperator {

    private SchoolDao mSchoolDao;
    private TeacherDao mTeacherDao;
    private StudentDao mStudentDao;

    private static DaoOperator mOperator;

    private DaoOperator() {
        mSchoolDao = DaoManager.getSchoolDao();
        mTeacherDao = DaoManager.getTeacherDao();
        mStudentDao = DaoManager.getStudentDao();
    }

    public static synchronized DaoOperator getInstance() {
        if (mOperator == null) {
            mOperator = new DaoOperator();
        }
        return mOperator;
    }

}
