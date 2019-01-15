package com.pax.demoapp.kotlin.inline

import org.junit.Test

/**
 * apply函数的inline结构分析
 * 从结构上来看apply函数和run函数很像，唯一不同点就是它们各自返回的值不一样，
 * run函数是以闭包形式返回最后一行代码的值，而apply函数的返回的是传入对象的本身。
 * @author      ligq
 * @date        2018/12/18 11:16
 */
class InlineApply {
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
        user?.apply {
            println("username:$name,age:$age,phone:$phone")
        }?.apply {
            name = "wangwu"
            age = 50
            phone = "15071939594"
        }?.run {
            println("username:$name,age:$age,phone:$phone")
        }
    }
}