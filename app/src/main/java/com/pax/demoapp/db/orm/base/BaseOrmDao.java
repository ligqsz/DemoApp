package com.pax.demoapp.db.orm.base;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pax.utils.LogUtils;
import com.pax.utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ligq
 * @date 2018/7/13
 * 描述:主要是对数据库进行增删改查的基类
 */

@SuppressWarnings("unused")
public abstract class BaseOrmDao<T> {
    protected BaseOrmHelper ormHelper;
    protected RuntimeExceptionDao<T, Integer> dataDao = null;

    public BaseOrmDao() {
        ormHelper = BaseOrmHelper.getInstance(Utils.getApp());
    }

    public boolean insertData(T t) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            dao.create(t);
        } catch (RuntimeException e) {
            LogUtils.e(e);
            return false;
        }
        return true;
    }

    public boolean updateData(T t) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            dao.update(t);
        } catch (RuntimeException e) {
            LogUtils.e(e);
            return false;
        }

        return true;
    }

    public boolean deleteData(T t) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            dao.delete(t);
        } catch (RuntimeException e) {
            LogUtils.e(e);
            return false;
        }
        return true;
    }

    public boolean deleteDataById(int id) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            dao.deleteById(id);
        } catch (RuntimeException e) {
            LogUtils.e(e);
            return false;
        }
        return true;
    }

    public boolean deleteAllList() {
        RuntimeExceptionDao<T, Integer> dao = getDao();
        try {
            List<T> list = findAllData();
            dao.delete(list);
        } catch (RuntimeException e) {
            LogUtils.e(e);
            return false;
        }
        return true;
    }

    public T findDataById(int id) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            return dao.queryForId(id);
        } catch (RuntimeException e) {
            LogUtils.e(e);
        }
        return null;
    }

    public T findDataByName(String name) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            QueryBuilder<T, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("name", name);
            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            LogUtils.e(e);
        }

        return null;
    }

    public List<T> findDataListByName(String name) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            QueryBuilder<T, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("name", name);
            return queryBuilder.query();
        } catch (SQLException e) {
            LogUtils.e(e);
        }

        return Collections.emptyList();
    }

    public List<T> findAllData() {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            return dao.queryForAll();
        } catch (RuntimeException e) {
            LogUtils.e(e);
        }
        return Collections.emptyList();
    }

    public T findLastData() {
        try {
            List<T> list = findAllData();
            if (list != null && !list.isEmpty()) {
                return list.get(list.size() - 1);
            }
        } catch (RuntimeException e) {
            LogUtils.e(e);
        }
        return null;
    }

    /**
     * 倒叙排列,查找指定个数
     *
     * @param count 查找的数据总个数
     * @return 数据集合
     */
    public List<T> findDataList(long count) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            QueryBuilder<T, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder
                    .orderBy("id", false)
                    .limit(count);
            return queryBuilder.query();
        } catch (SQLException e) {
            LogUtils.e(e);
        }
        return new ArrayList<>();
    }

    public long countOf() {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            return dao.countOf();
        } catch (RuntimeException e) {
            LogUtils.e(e);
        }
        return 0;
    }

    /**
     * 统计省份为province的总个数及总年龄
     *
     * @param province 目标省份
     * @return 总个数+总年龄
     */
    public long[] countSumOf(String province) {
        try {
            RuntimeExceptionDao<T, Integer> dao = getDao();
            QueryBuilder<T, Integer> queryBuilder =
                    dao.queryBuilder().selectRaw("COUNT(*), SUM(age)");
            queryBuilder.where().eq("province", province);
            GenericRawResults<String[]> rawResults = dao.queryRaw(queryBuilder.prepare().getStatement());
            return getRawResults(rawResults.getResults());
        } catch (SQLException e) {
            LogUtils.e(e);
            return new long[]{0, 0};
        }
    }

    private long[] getRawResults(List<String[]> results) {
        long[] obj = new long[]{0, 0};
        if (results != null && !results.isEmpty()) {
            String[] value = results.get(0);
            obj[0] = value[0] == null ? 0 : Long.parseLong(value[0]);
            obj[1] = value[1] == null ? 0 : Long.parseLong(value[1]);
        }
        return obj;
    }

    /**
     * 获取各个data的Dao
     *
     * @return dao
     */
    protected abstract RuntimeExceptionDao<T, Integer> getDao();
}
