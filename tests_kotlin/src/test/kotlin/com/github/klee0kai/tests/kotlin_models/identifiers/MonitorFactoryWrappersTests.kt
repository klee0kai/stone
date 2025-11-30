package com.github.klee0kai.tests.kotlin_models.identifiers

import com.github.klee0kai.test_kotlin.di.compfactory.CompFactoryComponent
import com.github.klee0kai.test_kotlin.tech.components.Monitor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.Reference

class MonitorFactoryWrappersTests {

    @Test
    fun lazyProvideTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorLazy()

        //Then
        assertEquals(
            monitor.get()!!.uuid,
            monitor.get()!!.uuid
        )
    }


    @Test
    fun softRefProvideTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor: Reference<Monitor> = DI.monitorSoft()

        //Then
        assertNotNull(monitor.get())
    }

    @Test
    fun weakRefProvideTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor: Reference<Monitor> = DI.monitorWeak()

        //Then
        assertNotNull(monitor.get())
    }

    @Test
    fun phantom1ProvideTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorPhantomProvide()

        //Then
        assertNotEquals(
            monitor.get()!!.uuid,
            monitor.get()!!.uuid
        )
    }

    @Test
    fun phantomProvideTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorPhantomProvide()

        //Then
        assertNotEquals(
            monitor.get()!!.uuid,
            monitor.get()!!.uuid
        )
    }

    @Test
    fun phantom3ProvideTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

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
        val DI: CompFactoryComponent = TODO()

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
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorLazy()

        //Then
        assertEquals(
            monitor.get()!!.uuid,
            monitor.get()!!.uuid
        )
    }


    @Test
    fun softRefDelegateTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorSoft()

        //Then
        assertNotNull(monitor.get())
    }

    @Test
    fun weakRefDelegateTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorWeak()

        //Then
        assertNotNull(monitor.get())
    }

    @Test
    fun phantom1DelegateTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorPhantomProvide()

        //Then
        assertNotEquals(
            monitor.get()!!.uuid,
            monitor.get()!!.uuid
        )
    }

    @Test
    fun phantom2DelegateTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorPhantomProvide()

        //Then
        assertNotEquals(
            monitor.get()!!.uuid,
            monitor.get()!!.uuid
        )
    }

    @Test
    fun phantom3DelegateTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor = DI.monitorProviderIRef()

        //Then
        assertNotEquals(
            monitor.get()!!.uuid,
            monitor.get()!!.uuid
        )
    }


    @Test
    fun kotlinLazyDelegateTest() {
        //Given
        val DI: CompFactoryComponent = TODO()

        //When
        val monitor by DI.monitorLazyDelegate()

        //Then
        assertEquals(
            monitor.uuid,
            monitor.uuid
        )
    }


}




