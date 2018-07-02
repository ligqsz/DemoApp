package com.pax.demoapp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ligq
 * @date 2018/6/29
 */

public class MatchTest {
    @Test
    public void test1() {
        String str = "ooooo20180629_123a";
        String str2 = "_123a";
        String regex = "(ooo+?[0-9]{8})(?)_[a-z0-9A-Z]{4}?";
        boolean matchStr1 = str.matches(regex);
        boolean matchStr2 = str2.matches(regex);
        System.out.println("str:" + matchStr1 + "\n" +
                "str2:" + matchStr2);
    }

    @Test
    public void test2() {
        String regex = "^\\W\\d{4,10}$";
        String str1 = "1065662083";
        String str2 = ".3211213";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher1 = pattern.matcher(str1);
        Matcher matcher2 = pattern.matcher(str2);
        boolean result1 = matcher1.matches();
        boolean result2 = matcher2.matches();
        System.out.println("result1:" + result1 + "\n" +
                "result2:" + result2);

    }

    @Test
    public void test3() {
        String regex = "[\\w-]+\\.";
        String str1 = "aaa";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str1);
        boolean result = matcher.matches();
        System.out.println("result1:" + result);
    }
}
