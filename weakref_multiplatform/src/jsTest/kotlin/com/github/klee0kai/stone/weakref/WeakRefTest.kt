package com.github.klee0kai.stone.weakref

import kotlin.test.Test

class WeakRefTest {

    data class SimpleDataClass(
        val value: String,
    )

    @Test
    fun simpleTest() {
        console.log("SimpleDataClass")
        val obj = SimpleDataClass("hee")
        val link = WeakRef<SimpleDataClass>(obj)
//        js("console.log")(obj)
//        val jsObj0: dynamic = js("{ name: 'Alex', age: 17 }")
//        val jsObj: dynamic = js("new WeakRef ({ name: 'Alex', age: 17 })")

        println("name " + link.get())

//        val a = js("")(obj)

    }

}