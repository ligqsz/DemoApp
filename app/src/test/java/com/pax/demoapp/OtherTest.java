package com.pax.demoapp;

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
}
