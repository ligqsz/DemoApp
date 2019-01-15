package com.pax.demoapp.common.design;

/**
 * Builder 模式
 * 参照:<a href = "https://github.com/yangchong211/YCBlogs/blob/master/android/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/02.Builder%E6%A8%A1%E5%BC%8F.md"/>
 * 经典demo参照:<a href = "https://github.com/yangchong211/YCDialog"/>
 * 调用方式:
 * new BuilderDemo.UserBuilder("yc","10086")
 * .age(24)
 * .address("beijing")
 * .phone("13667225184")
 * .build();
 *
 * @author ligq
 * @date 2018/11/14 14:30
 */
public class BuilderDemo {
    private final String name;
    private final String cardID;
    private final int age;
    private final String address;
    private final String phone;

    public BuilderDemo(UserBuilder userBuilder) {
        this.name = userBuilder.name;
        this.cardID = userBuilder.cardID;
        this.age = userBuilder.age;
        this.address = userBuilder.address;
        this.phone = userBuilder.phone;
    }

    public static class UserBuilder {
        private final String name;
        private final String cardID;
        private int age;
        private String address;
        private String phone;

        public BuilderDemo build() {
            return new BuilderDemo(this);
        }

        public UserBuilder(String name, String cardID) {
            this.name = name;
            this.cardID = cardID;
        }

        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
    }
}
