package com.pax.demoapp.db.orm.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pax.demoapp.db.exception.DbRuntimeException;
import com.pax.demoapp.db.orm.bean.Student;
import com.pax.demoapp.db.orm.upgrade.DbUpgrader;
import com.pax.demoapp.utils.LogUtils;

import java.sql.SQLException;

/**
 * @author ligq
 * @date 2018/7/13
 */

public class BaseOrmHelper extends OrmLiteSqliteOpenHelper {
    /**
     * DB Name
     */
    private static final String DATABASE_NAME = "data.db";
    /**
     * DB version
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * DB Upgrader packagePath
     */
    private static final String UPGRADER_PATH = "com.pax.demoapp.db.orm.upgrade";
    private static BaseOrmHelper instance;

    private BaseOrmHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    private BaseOrmHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Student.class);
        } catch (SQLException e) {
            LogUtils.e(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                DbUpgrader.upgrade(database, connectionSource, i, i + 1, UPGRADER_PATH);
            }
        } catch (IllegalArgumentException e) {
            throw new DbRuntimeException(e);
        }
    }

    /**
     * get the Singleton of the DB Helper
     *
     * @param context the context object
     * @return the Singleton of DB helper
     */
    static synchronized BaseOrmHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BaseOrmHelper(context);
        }
        return instance;
    }
}
