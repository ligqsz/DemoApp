package com.pax.demoapp.utils;

import com.pax.demoapp.db.orm.dao.StudentDao;

import java.util.Random;

/**
 * @author ligq
 * @date 2018/7/17
 */

public class OtherUtils {
    private static final String[] WORDS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",};

    private OtherUtils() {
        throw new IllegalArgumentException();
    }

    public static int getRandomNum(int baseNum) {
        return new Random().nextInt(10) + baseNum;
    }

    public static String getRandomNum() {
        String num = String.valueOf(new Random().nextInt(100));
        StudentDao dao = StudentDao.getInstance();
        while (!dao.findListByNum(num).isEmpty()) {
            num = String.valueOf(new Random().nextInt(100));
        }
        return num;
    }

    public static String getRandomWord() {
        return WORDS[new Random().nextInt(WORDS.length)];
    }
}
