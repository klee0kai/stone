package com.github.klee0kai.stone.test

import com.github.klee0kai.thekey.stone.ksp.StoneProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SimpleKspTests {

    @Test
    fun simpleTest() {

        val compilation = KotlinCompilation().apply {
            sources = listOf(SourceFile.fromResources("SimpleCarComponent.kt"))
            symbolProcessorProviders = listOf(StoneProcessorProvider())
            inheritClassPath = true
            messageOutputStream = System.out
        }

        val result = compilation.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

        assertTrue(
            result.messages.contains("CarInjectModule has duplicate"),
            result.messages
        )
    }

}