package com.pax.demoapp.kotlin.inline

import org.junit.Test

/**
 * also函数的inline结构分析
 * also函数的结构实际上和let很像唯一的区别就是返回值的不一样，let是以闭包的形式返回，
 * 返回函数体内最后一行的值，如果最后一行为空就返回一个Unit类型的默认值。
 * 而also函数返回的则是传入对象的本身
 * @author      ligq
 * @date        2018/12/18 11:32
 */
class InlineAlso {
    class User(map: MutableMap<String, Any?>) {
        var name: String by map
        var age: Int by map
        var phone: String by map
    }

    @Test
    fun test() {
        val user: User? = User(mutableMapOf("name" to "zhangs"
                , "age" to 30
                , "phone" to "13197181393"))
        user?.also {
            println("username:${it.name},age:${it.age},phone:${it.phone}")
        }?.also {
            it.name = "wangwu"
            it.age = 50
            it.phone = "15071939594"
        }?.run {
            println("username:$name,age:$age,phone:$phone")
        }
    }
}