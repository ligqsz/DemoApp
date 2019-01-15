package com.pax.demoapp.kotlin.serial

import com.twitter.serial.stream.bytebuffer.ByteBufferSerial
import org.junit.Test

/**
 * @author      ligq
 * @date        2019/1/15 14:21
 */
class SerialTest {
    @Test
    fun test() {
        val user = UserSerial.UserBuilder()
                .setAge(20)
                .setName("zhangs")
                .build()
        val serial = ByteBufferSerial()
        val bytes = serial.toByteArray(user, UserSerial.objectSerializer)
        println(user)
        println(bytes.toString())
        println(serial.fromByteArray(bytes, UserSerial.objectSerializer))
    }
}