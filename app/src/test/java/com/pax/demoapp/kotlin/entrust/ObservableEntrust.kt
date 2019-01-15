package com.pax.demoapp.kotlin.entrust

import org.junit.Test
import kotlin.properties.Delegates

/**
 * 可观察属性 Observable
 * 当属性值改变的时候运行函数方法。
 * Delegates.observable() 接受两个参数：初始值和修改时处理程序（handler）。
 * 每当我们给属性赋值时会调用该处理程序（在赋值后执行）。它有三个参数：被赋值的属性、旧值和新值
 *
 * Vetoable 使用也是通过 类Delegates.vetoable()调用，同样也是两个参数，第一个是初始化值,
 * 第二个是属性值变化事件的响应器(handler)，是可观察属性(Observable)的一个特例，
 * 不同的是给属性赋值的时候会加以判断，是否要将新值赋于该变量。
 * @author      ligq
 * @date        2018/12/18 9:48
 */
class ObservableEntrust {
    class User {
        var name: String by Delegates.observable("zhangs") { property, oldValue, newValue ->
            println("${property.name}:$oldValue -> $newValue")
        }

        var age: String by Delegates.vetoable("0") { _, _, newValue ->
            newValue.toInt() < 30
        }
    }

    @Test
    fun test() {
        val user = User()
        user.name = "lisi"
        user.name = "wangw"
        user.age = "100"
        println("age:${user.age}")
        user.age = "20"
        println("age:${user.age}")
    }

}