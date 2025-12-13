package com.github.klee0kai.stone.test.moduleinit

import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.di.base_forest.UnitedModule
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BeginOfBeginsTests {

    @Test
    fun initByFactory() {
        //Given
        val module: UnitedModule = object : UnitedModule() {
            override fun earth(): Earth = earth
            override fun history() = null
        }
        val DI = ForestComponentStoneComponent()

        //When
        DI.initUnitedModule(module)

        //Then
        assertEquals(earth, DI.united().earth())
    }


    @Test
    fun initByFactoryClass() {
        //Given
        val DI = ForestComponentStoneComponent()

        //When
        DI.initUnitedModule(UnitedModuleFactory::class.java as UnitedModule?)

        //Then
        assertEquals(earth, DI.united().earth())
    }

    class UnitedModuleFactory : UnitedModule() {
        override fun earth(): Earth = earth
        override fun history(): History? = null
    }

    companion object {
        private val earth: Earth = Earth()
    }
}
