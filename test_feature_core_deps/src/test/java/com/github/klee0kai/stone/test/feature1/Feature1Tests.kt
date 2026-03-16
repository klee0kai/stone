package com.github.klee0kai.stone.test.feature1

import com.github.klee0kai.test.core.di.CoreComponentStoneComponent
import com.github.klee0kai.test.feature1.di.Feature1ComponentStoneComponent
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class Feature1Tests {

    @Test
    fun provideCoreDependenciesTests() = runBlocking {
        // Given
        val coreDI = CoreComponentStoneComponent()
        val featureDI = Feature1ComponentStoneComponent()

        // When
        featureDI.initCoreDependencies(coreDI)

        // Then
        assertNotNull(featureDI.fir().get())
    }

}