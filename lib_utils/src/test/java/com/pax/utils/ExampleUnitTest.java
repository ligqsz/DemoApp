package com.pax.utils;

import com.pax.constants.TimeConstants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testTimeUtils() {
//        String format = "yyyy-MM-dd HH:mm:ss";
        String time1 = "2018-10-25 00:00:00";
        String time2 = "2017-10-29 10:00:00";
        long span = TimeUtils.getTimeSpan(time1, time2, TimeConstants.DAY);
        System.out.println("getTimeSpan:" + span);
    }
}