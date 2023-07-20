package com.github.klee0kai.tests.kotlin_models.qualifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.type.wrappers.getValue
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test_kotlin.di.compfactory.CompFactoryComponent
import com.github.klee0kai.test_kotlin.tech.components.Monitor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.Reference

class MonitorFactoryWrappersTests {

    @Test
    fun lazyProvideTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor: Ref<Monitor> = DI.monitorLazy()

        //Then
        assertEquals(
            monitor.get().uuid,
            monitor.get().uuid
        )
    }


    @Test
    fun softRefProvideTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor: Reference<Monitor> = DI.monitorSoft()

        //Then
        assertNotNull(monitor.get())
    }

    @Test
    fun weakRefProvideTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor: Reference<Monitor> = DI.monitorWeak()

        //Then
        assertNotNull(monitor.get())
    }

    @Test
    fun phantom1ProvideTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor: Ref<Monitor> = DI.monitorPhantomProvide()

        //Then
        assertNotEquals(
            monitor.get().uuid,
            monitor.get().uuid
        )
    }

    @Test
    fun phantomProvideTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor: Ref<Monitor> = DI.monitorPhantomProvide()

        //Then
        assertNotEquals(
            monitor.get().uuid,
            monitor.get().uuid
        )
    }

    @Test
    fun phantom3ProvideTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor = DI.monitorProviderIRef()

        //Then
        assertNotEquals(
            monitor.get().uuid,
            monitor.get().uuid
        )
    }


    @Test
    fun kotlinLazyProvideTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor = DI.monitorLazyDelegate()

        //Then
        assertEquals(
            monitor.value.uuid,
            monitor.value.uuid
        )
    }



    @Test
    fun lazyDelegateTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor by DI.monitorLazy()

        //Then
        assertEquals(
            monitor.uuid,
            monitor.uuid
        )
    }


    @Test
    fun softRefDelegateTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor by DI.monitorSoft()

        //Then
        assertNotNull(monitor)
    }

    @Test
    fun weakRefDelegateTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor by DI.monitorWeak()

        //Then
        assertNotNull(monitor)
    }

    @Test
    fun phantom1DelegateTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor by DI.monitorPhantomProvide()

        //Then
        assertNotEquals(
            monitor.uuid,
            monitor.uuid
        )
    }

    @Test
    fun phantom2DelegateTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor by DI.monitorPhantomProvide()

        //Then
        assertNotEquals(
            monitor.uuid,
            monitor.uuid
        )
    }

    @Test
    fun phantom3DelegateTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor by DI.monitorProviderIRef()

        //Then
        assertNotEquals(
            monitor.uuid,
            monitor.uuid
        )
    }


    @Test
    fun kotlinLazyDelegateTest() {
        //Given
        val DI = Stone.createComponent(CompFactoryComponent::class.java)

        //When
        val monitor by DI.monitorLazyDelegate()

        //Then
        assertEquals(
            monitor.uuid,
            monitor.uuid
        )
    }




}




