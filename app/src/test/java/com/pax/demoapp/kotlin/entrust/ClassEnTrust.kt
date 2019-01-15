package com.pax.demoapp.kotlin.entrust

import org.junit.Test

/**
 * 类委托
 * 类 Derived 可以继承一个接口 Base，并将其所有共有的方法委托给一个指定的对象，
 * 也就是说把类 Derived 因继承而需要实现的方法委托给一个对象，从而不需要在该类内显式的实现
 * 如果 Derived 类没有写‘by b’，那么就必须重写print方法，否则编译器会报错。
 * @author      ligq
 * @date        2018/12/18 9:32
 */
class ClassEnTrust {
    interface Base {
        fun print()
    }

    class BaseImpl(private val x: Int) : Base {
        override fun print() {
            print(x)
        }
    }

    class Derived(b: Base) : Base by b

    @Test
    fun test() {
        val b = BaseImpl(10)
        Derived(b).print()
    }
}