package com.pax.demoapp.kotlin.serial

import com.twitter.serial.`object`.Builder
import com.twitter.serial.serializer.BuilderSerializer
import com.twitter.serial.serializer.ObjectSerializer
import com.twitter.serial.serializer.SerializationContext
import com.twitter.serial.stream.SerializerInput
import com.twitter.serial.stream.SerializerOutput

/**
 * @author      ligq
 * @date        2019/1/15 14:22
 */
class UserSerial {
    var name: String? = null
    var age: Int = 0
    var address: String = "china"

    companion object {
        val objectSerializer: ObjectSerializer<UserSerial> = UserSerializer()
    }

    class UserBuilder : Builder<UserSerial> {
        var name: String? = null
        var age: Int = 0
        var address: String? = null
        override fun build(): UserSerial {
            val user = UserSerial()
            user.age = this.age
            user.name = this.name
            return user
        }

        fun setName(name: String): UserBuilder {
            this.name = name
            return this
        }

        fun setAge(age: Int): UserBuilder {
            this.age = age
            return this
        }

        fun setAddress(address: String): UserBuilder {
            this.address = address
            return this
        }
    }

    private class UserSerializer() : BuilderSerializer<UserSerial, UserBuilder>() {
        private var version = 0

        constructor(version: Int) : this() {
            this.version = version
        }

        override fun serializeObject(context: SerializationContext, output: SerializerOutput<out SerializerOutput<*>>, `object`: UserSerial) {
            output.writeInt(`object`.age)
                    .writeString(`object`.name)
                    .takeIf { version > 0 }?.writeString(`object`.address)
        }

        override fun createBuilder(): UserBuilder {
            return UserBuilder()
        }

        override fun deserializeToBuilder(context: SerializationContext, input: SerializerInput, builder: UserBuilder, versionNumber: Int) {
            builder.age = input.readInt()
            builder.name = input.readString()
            if (version > 0) {
                builder.address = input.readString()
            }
        }
    }
}