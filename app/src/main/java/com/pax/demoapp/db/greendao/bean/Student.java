package com.pax.demoapp.db.greendao.bean;


import com.pax.demoapp.utils.LogUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 与Teacher关系为多对多,与School关系为多对一
 *
 * @author ligq
 * @date 2018/7/13
 */
@Entity(nameInDb = "STUDENT_TABLE", active = true
        //定义跨越多个列的索引
        , indexes = {
        @Index(value = "name DESC", unique = true)
})
public class Student {
    private static final String ID_FIELD_NAME = "id";

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "STUDENT_NAME")
    private String name;
    @NotNull
    private int age;
    @NotNull
    private String province;
    @Index(unique = true)
    private String number;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1943931642)
    private transient StudentDao myDao;

    @Generated(hash = 397843462)
    public Student(Long id, String name, int age, @NotNull String province, String number) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.province = province;
        this.number = number;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }

    /**
     * 通过流的方式对Student进行深克隆
     *
     * @return Object
     */
    public Object cloneData() {
        Object data = null;
        try {
            // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，
            // 而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            // 将流序列化成对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            data = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LogUtils.e(e);
        }
        return data;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", province='" + province + '\'' +
                ", number='" + number + '\'' +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                '}';
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1701634981)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentDao() : null;
    }
}
