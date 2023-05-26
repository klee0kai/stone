package com.github.klee0kai.stone.test.moduleinit;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
import com.github.klee0kai.test.di.base_forest.IdentityModule;
import com.github.klee0kai.test.di.base_forest.UnitedModule;
import com.github.klee0kai.test.mowgli.community.History;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.identity.Conscience;
import com.github.klee0kai.test.mowgli.identity.Ideology;
import com.github.klee0kai.test.mowgli.identity.Knowledge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeginOfBegins2Tests {

    private static final Earth earth = new Earth();
    private static final Ideology ideology = new Ideology();

    @Test
    public void initByFactory() {
        //Given
        UnitedModule module = new UnitedModule() {
            @Override
            public Earth earth() {
                return earth;
            }

            @Override
            public History history() {
                return null;
            }
        };
        ForestComponent DI = Stone.createComponent(ForestComponent.class);

        //When
        DI.initUnitedModule(module);

        //Then
        assertEquals(earth, DI.united().earth());
    }


    @Test
    public void initByFactoryClass() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);

        //When
        DI.initUnitedModule(UnitedModuleFactory.class);

        //Then
        assertEquals(earth, DI.united().earth());
    }


    @Test
    public void initAllModules() {
        //Given
        UnitedModule module = new UnitedModule() {
            @Override
            public Earth earth() {
                return earth;
            }

            @Override
            public History history() {
                return null;
            }
        };
        IdentityModule identityModule = new IdentityModule() {
            @Override
            public Knowledge knowledge() {
                return null;
            }

            @Override
            public Conscience conscience() {
                return null;
            }

            @Override
            public Ideology ideology() {
                return ideology;
            }
        };

        ForestComponent DI = Stone.createComponent(ForestComponent.class);

        //When
        DI.iniAllModules(module, identityModule);

        //Then
        assertEquals(earth, DI.united().earth());
        assertEquals(ideology, DI.identity().ideology());
    }

    public static class UnitedModuleFactory extends UnitedModule {

        @Override
        public Earth earth() {
            return earth;
        }

        @Override
        public History history() {
            return null;
        }
    }

}
