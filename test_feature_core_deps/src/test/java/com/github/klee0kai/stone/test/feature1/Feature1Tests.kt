package com.github.klee0kai.stone.test.feature1

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.core.di.CoreComponent
import com.github.klee0kai.test.feature1.di.Feature1Component
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class Feature1Tests {

    @Test
    fun provideCoreDependenciesTests() {
        // Given
        val coreDI = Stone.createComponent(CoreComponent::class.java)
        val featureDI = Stone.createComponent(Feature1Component::class.java)

        // When
        featureDI.initCoreDependencies(coreDI)

        // Then
        assertNotNull(featureDI.fir())
    }

}