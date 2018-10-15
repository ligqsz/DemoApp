package com.pax.demoapp.rxjava;

/**
 * @author ligq
 * @date 2018/10/10
 */

class Utils {
    static void print(String s) {
        System.out.println(Thread.currentThread().getName() + ":" + s);
    }

    static Integer parIntSafe(char c) {
        try {
            return Integer.parseInt(Character.toString(c));
        } catch (Exception e) {
            return -1;
        }
    }

    static void printLine() {
        print("-----------------------------------------------");
    }
}
