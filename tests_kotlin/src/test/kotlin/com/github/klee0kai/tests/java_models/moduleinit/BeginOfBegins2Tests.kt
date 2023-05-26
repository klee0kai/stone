package com.github.klee0kai.tests.java_models.moduleinit

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.base_forest.ForestComponent
import com.github.klee0kai.test.di.base_forest.UnitedModule
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BeginOfBegins2Tests {
    @Test
    fun initByFactory() {
        //Given
        val module: UnitedModule = object : UnitedModule() {
            override fun earth(): Earth {
                return earth
            }

            override fun history(): History? {
                return null
            }
        }
        val DI = Stone.createComponent(ForestComponent::class.java)

        //When
        DI.initUnitedModule(module)

        //Then
        assertEquals(earth, DI.united().earth())
    }

    @Test
    fun initByFactoryClass() {
        //Given
        val DI = Stone.createComponent(ForestComponent::class.java)

        //When
        DI.initUnitedModule(UnitedModuleFactory::class.java)

        //Then
        assertEquals(earth, DI.united().earth())
    }

    class UnitedModuleFactory : UnitedModule() {
        override fun earth(): Earth? {
            return earth
        }

        override fun history(): History? {
            return null
        }
    }

    companion object {
        private val earth = Earth()
    }
}