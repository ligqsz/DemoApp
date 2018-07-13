package com.pax.demoapp.db.orm.dao;

import com.blankj.utilcode.util.LogUtils;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.table.DatabaseTable;
import com.pax.demoapp.bean.Student;
import com.pax.demoapp.db.orm.base.BaseOrmDao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author ligq
 * @date 2018/7/13
 */
@DatabaseTable(tableName = "student")
public class StudentDao extends BaseOrmDao<Student> {

    private StudentDao() {
        super();
    }

    private static StudentDao instance;

    @Override
    protected RuntimeExceptionDao<Student, Integer> getDao() {
        if (dataDao == null) {
            dataDao = ormHelper.getRuntimeExceptionDao(Student.class);
        }
        return dataDao;
    }

    public static synchronized StudentDao getInstance() {
        if (instance == null) {
            instance = new StudentDao();
        }
        return instance;
    }

    public boolean deleteAllSql() {
        try {
            RuntimeExceptionDao<Student, Integer> dao = getDao();
            dao.executeRawNoArgs("delete from student");
        } catch (RuntimeException e) {
            LogUtils.e(e);
            return false;
        }
        return true;
    }

    public List<Student> findListByNum(String num) {
        try {
            RuntimeExceptionDao<Student, Integer> dao = getDao();
            return dao.queryBuilder().where().eq("number", num).query();
        } catch (SQLException e) {
            LogUtils.e(e);
        }
        return Collections.emptyList();
    }
}
