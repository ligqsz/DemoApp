package com.pax.demoapp.rxjava;

/**
 * @author ligq
 * @date 2018/10/10
 */

class PrintUtils {
    static void print(String s) {
        System.out.println(Thread.currentThread().getName() + ":" + s);
    }

    static Integer parIntSafe(char c) {
        return parIntSafe(Character.toString(c));
    }

    static Integer parIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    static void printLine() {
        print("-----------------------------------------------");
    }
}
