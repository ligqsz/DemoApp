package com.pax.demoapp.kotlin.inline

import org.junit.Test

/**
 * run函数的适用场景
 * 适用于let,with函数任何场景。因为run函数是let,with两个函数结合体，
 * 准确来说它弥补了let函数在函数体内必须使用it参数替代对象，在run函数中可以像with函数一样可以省略，
 * 直接访问实例的公有属性和方法，另一方面它弥补了with函数传入对象判空问题，在run函数中可以像let函数一样做判空处理
 * @author      ligq
 * @date        2018/12/18 11:10
 */
class InlineRun {
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

        user?.run {
            println("username:$name,age:$age,phone:$phone")
        }
    }
}