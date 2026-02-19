package com.github.klee0kai.stone.test.moduleinit

import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.di.base_forest.UnitedModule
import com.github.klee0kai.test.mowgli.galaxy.Earth
import kotlin.test.Test
import kotlin.test.assertEquals

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

    companion object {
        private val earth: Earth = Earth()
    }
}
