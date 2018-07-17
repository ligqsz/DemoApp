package com.pax.demoapp.db.greendao.manager;

import android.content.Context;

import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.db.greendao.bean.DaoMaster;
import com.pax.demoapp.db.greendao.bean.DaoSession;
import com.pax.demoapp.db.greendao.bean.SchoolDao;
import com.pax.demoapp.db.greendao.bean.StudentDao;
import com.pax.demoapp.db.greendao.bean.TeacherDao;

import org.greenrobot.greendao.database.Database;

/**
 * @author ligq
 * @date 2018/7/17
 */

public class DaoManager {
    private static DaoSession daoSession;

    private DaoManager() {
        throw new IllegalArgumentException();
    }

    /**
     * 必须要在Application中去init
     *
     * @param context context,最好是application
     * @param dbName  数据库名称
     */
    public static void initDb(Context context, String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbName);
        Database db = MenuConfig.ENCRYPTED ?
                helper.getEncryptedWritableDb("young") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    /**
     * 获取StudentDao
     *
     * @return StudentDao
     */
    static StudentDao getStudentDao() {
        return daoSession.getStudentDao();
    }

    static TeacherDao getTeacherDao() {
        return daoSession.getTeacherDao();
    }

    static SchoolDao getSchoolDao() {
        return daoSession.getSchoolDao();
    }
}
