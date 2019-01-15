package com.pax.demoapp.kotlin.inline

import org.junit.Test

/**
 * with函数的适用的场景
 * 适用于调用同一个类的多个方法时，可以省去类名重复，直接调用类的方法即可，
 * 经常用于Android中RecyclerView中onBinderViewHolder中，数据model的属性映射到UI上
 * @author      ligq
 * @date        2018/12/18 10:59
 */
class InlineWith {
    class User(map: MutableMap<String, Any?>) {
        var name: String by map
        var age: Int by map
        var phone: String by map
    }

    @Test
    fun test() {
        val user = User(mutableMapOf("name" to "zhangs"
                , "age" to 30
                , "phone" to "13197181393"))
        val result = with(user) {
            "name:$name,age:$age,phone:$phone"
        }
        println(result)
    }
}