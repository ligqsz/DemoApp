package com.pax.demoapp.common.design;

/**
 * refer to <a href = "https://github.com/yangchong211/YCBlogs/blob/master/android/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/01.%E5%8D%95%E4%BE%8B%E6%A8%A1%E5%BC%8F.md"/>
 *
 * @author ligq
 * @date 2018/11/14 14:06
 */
public class Singleton {
    /**
     * 静态变量
     */
    private static volatile Singleton singleton;

    /**
     * 私有构造函数
     */
    private Singleton() {
    }

    /**
     * DCL双重校验模式代码获取单例实例
     * 代码分析
     * 这种模式的亮点在于getInstance（）方法上，其中对singleton 进行了两次判断是否空，
     * 第一层判断是为了避免不必要的同步，第二层的判断是为了在null的情况下才创建实例。
     *
     * @return Singleton instance
     */
    public static Singleton getInstance() {
        //第一层校验
        if (singleton == null) {
            //第二层校验
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    /**
     * 静态内部类单例模式
     * 优缺点
     * 优点：延迟加载，线程安全（java中class加载时互斥的），也减少了内存消耗
     * 代码分析
     * 当第一次加载Singleton 类的时候并不会初始化INSTANCE ，只有第一次调用Singleton 的getInstance（）方法时才会导致INSTANCE 被初始化。
     * 因此，第一次调用getInstance（）方法会导致虚拟机加载SingletonHolder 类，这种方式不仅能够确保单例对象的唯一性，同时也延迟了单例的实例化。
     *
     * @return Singleton instance
     */
    public static Singleton getInstance2() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 定义的静态内部类
     */
    private static class SingletonHolder {
        /**
         * 创建实例的地方
         */
        private static final Singleton INSTANCE = new Singleton();

        private SingletonHolder() {

        }
    }
}
