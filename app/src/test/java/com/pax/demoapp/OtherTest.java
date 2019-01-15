package com.pax.demoapp;

import com.pax.demoapp.rxjava.PrintUtils;

import org.junit.Test;

import java.util.Random;

/**
 * @author ligq
 * @date 2018/7/5
 */

public class OtherTest {
    @Test
    public void testRandom() {
        System.out.println("random.nextInt(16):" + randomColor());
    }

    private String randomColor() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        for (int i = 0; i < 6; i++) {
            int temp = random.nextInt(16) + 1;
            sb.append(Integer.toHexString(temp));
        }
        return sb.toString();
    }

    @Test
    public void test() {
        int a = 1;
        int b = 2;
        int ret = a | b;
        PrintUtils.print("a | b:" + ret);
        PrintUtils.print("ret^a:" + (ret ^ a));

    }
}