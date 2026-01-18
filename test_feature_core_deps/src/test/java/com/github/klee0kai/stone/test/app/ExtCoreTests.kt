package com.github.klee0kai.stone.test.app

import com.github.klee0kai.test.app.di.AppComponentStoneComponent
import com.github.klee0kai.test.core.di.CoreComponentStoneComponent
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class ExtCoreTests {

    @Test
    fun extCoreTest() = runBlocking {
        // Given
        val coreDI = CoreComponentStoneComponent()
        val appDI = AppComponentStoneComponent()

        // When
        appDI.ext(coreDI)

        // Then
        assertNotNull(appDI.amanita().get())
        assertNotNull(appDI.duck().get())
    }

}