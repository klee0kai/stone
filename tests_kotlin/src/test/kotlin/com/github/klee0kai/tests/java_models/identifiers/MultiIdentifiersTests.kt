package com.github.klee0kai.tests.java_models.identifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.base_phone.PhoneComponent
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class MultiIdentifiersTests {
    @Test
    fun differentCreatePhoneTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

        //When
        val androidDemo = DI.components().phoneOs(PhoneOsType.Android, null)
        val android11 = DI.components().phoneOs(PhoneOsType.Android, PhoneOsVersion("11"))

        //Then
        assertNotEquals(androidDemo.uuid, android11.uuid)
    }

    @Test
    fun differentMethodDifferentCacheTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

        //When
        val android11 = DI.components().phoneOs(PhoneOsType.Android, PhoneOsVersion("11"))
        val androidSimple = DI.components().phoneOs(PhoneOsType.Android)

        //Then
        assertNotEquals(androidSimple.uuid, android11.uuid)
    }

    @Test
    fun cacheByIdentifiersTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

        //When
        val android11 = DI.components().phoneOs(PhoneOsType.Android, PhoneOsVersion("11"))
        val android11_2 = DI.components().phoneOs(PhoneOsType.Android, PhoneOsVersion("11"))

        //Then
        assertEquals(android11.uuid, android11_2.uuid)
    }

    @Test
    fun sepCacheByIdentifiersTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

        //When
        val android11 = DI.components().phoneOs(PhoneOsType.Android, PhoneOsVersion("11"))
        val android12 = DI.components().phoneOs(PhoneOsType.Android, PhoneOsVersion("12"))

        //Then
        assertNotEquals(android11.uuid, android12.uuid)
    }
}