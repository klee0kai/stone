package com.github.klee0kai.stone.test.app

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.app.di.AppComponent
import com.github.klee0kai.test.core.di.CoreComponent
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class ExtCoreTests {

    @Test
    fun extCoreTest() {
        // Given
        val coreDI = Stone.createComponent(CoreComponent::class.java)
        val appDI = Stone.createComponent(AppComponent::class.java)

        // When
        appDI.ext(coreDI)

        // Then
        assertNotNull(appDI.amanita().get())
        assertNotNull(appDI.duck().get())
    }

}