package com.pax.demoapp.kotlin.entrust

import org.junit.Test

/**
 * 延迟属性Lazy
 * lazy() 是接受一个 lambda 并返回一个 Lazy 实例的函数，返回的实例可以作为实现延迟属性的委托：
 * 第一次调用 get() 会执行已传递给 lazy() 的 lambda 表达式并记录结果， 后续调用 get() 只是返回记录的结果。
 * @author      ligq
 * @date        2018/12/18 9:45
 */
class lazyEntrust {
    val lazyValue: String by lazy {
        println("lazy")
        "lazyValue"
    }

    @Test
    fun test() {
        println(lazyValue)
        println(lazyValue)
    }
}