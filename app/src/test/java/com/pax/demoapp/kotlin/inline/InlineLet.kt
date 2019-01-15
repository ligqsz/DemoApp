package com.pax.demoapp.kotlin.inline

import org.junit.Test
import kotlin.properties.Delegates

/**
 * let函数适用的场景
 * 场景一: 最常用的场景就是使用let函数处理需要针对一个可null的对象统一做判空处理。
 * 场景二: 然后就是需要去明确一个变量所处特定的作用域范围内可以使用
 * @author      ligq
 * @date        2018/12/18 10:38
 */
class InlineLet {
    class User {
        var name: String by Delegates.vetoable("") { _, _, newValue ->
            newValue.length < 6
        }

        var age: Int = 0
        fun printName() {
            println("name:$name")
        }
    }

    @Test
    fun test() {
        User().let {
            it.name = "wangwu"
            it.printName()
            it.name = "lisi"
            it.printName()
        }

        var user: User? = null
        user?.printName()
        user = User()
        user.let {
            it.name = "lisi"
            it.age = 100
        }
    }

}