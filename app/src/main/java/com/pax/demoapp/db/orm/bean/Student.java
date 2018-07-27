package com.pax.demoapp.db.orm.bean;

import com.j256.ormlite.field.DatabaseField;
import com.pax.demoapp.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author ligq
 * @date 2018/7/13
 */

public class Student implements Serializable {
    private static final String ID_FIELD_NAME = "id";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField(canBeNull = false)
    private int age;
    @DatabaseField(canBeNull = false)
    private String province;
    @DatabaseField(unique = true, canBeNull = false)
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", province='" + province + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
