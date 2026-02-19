package com.github.klee0kai.stone.test.moduleinit

import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.di.base_forest.IdentityModule
import com.github.klee0kai.test.di.base_forest.UnitedModule
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.identity.Conscience
import com.github.klee0kai.test.mowgli.identity.Ideology
import com.github.klee0kai.test.mowgli.identity.Knowledge
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.Test
import kotlin.test.assertEquals

class BeginOfBegins2Tests {

    @Test
    fun initByFactory() {
        //Given
        val module = object : UnitedModule() {
            override fun earth(): Earth = earth
            override fun history(): History? = null

        }
        val DI = ForestComponentStoneComponent()

        //When
        DI.initUnitedModule(module)

        //Then
        assertEquals(earth, DI.united().earth())
    }

    @Test
    fun initAllModules() {
        //Given
        val module: UnitedModule = object : UnitedModule() {
            override fun earth(): Earth = earth
            override fun history(): History? = null

        }
        val identityModule: IdentityModule = object : IdentityModule {
            override fun knowledge(): Knowledge? = null
            override fun conscience(): Conscience? = null
            override fun ideology(): Ideology = ideology
        }

        val DI = ForestComponentStoneComponent()

        //When
        DI.iniAllModules(module, identityModule)

        //Then
        assertEquals(earth, DI.united().earth())
        assertEquals(ideology, DI.identity().ideology())

    }

    companion object {
        private val earth: Earth = Earth()
        private val ideology: Ideology = Ideology()
    }

}
