package com.pax.demoapp.kotlin

import org.junit.Test

/**
 * @author      ligq
 * @date        2019/1/14 16:42
 */
class OperatorTest {
    @Test
    fun testInv() {
        val a = 1
        val ret = a and 2
        println("result:${a and a.inv()}")
    }
}