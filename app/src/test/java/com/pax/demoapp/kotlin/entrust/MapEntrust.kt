package com.pax.demoapp.kotlin.entrust

import org.junit.Test

/**
 * @author      ligq
 * @date        2018/12/18 9:57
 */
class MapEntrust {

    class User(val map: Map<String, Any?>) {
        val name: String by map
        val age: Int by map
    }

    @Test
    fun test() {
        val user = User(mapOf("name" to "lee", "age" to 28))
        println("name:${user.name},age:${user.age}")
    }
}

