package com.pax.demoapp.kotlin.entrust

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author      ligq
 * @date        2018/12/18 9:33
 * @version     1.0
 */
class EnTrustDemo {
    class MyDelegate1<in R> : ReadOnlyProperty<R, String> {
        private val initValue = ""
        override fun getValue(thisRef: R, property: KProperty<*>): String {
            return initValue
        }
    }

    class MyDeleagete2<in R> :ReadWriteProperty<R,String>{
        private val initValue = ""
        override fun getValue(thisRef: R, property: KProperty<*>): String {
            return initValue
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: String) {
            property.getter
        }

    }
}