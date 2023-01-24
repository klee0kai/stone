package com.github.klee0kai.stone.test.moduleinit;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
import com.github.klee0kai.test.di.base_forest.UnitedModule;
import com.github.klee0kai.test.mowgli.community.History;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeginOfBeginsTests {

    private static final Earth earth = new Earth();

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
        DI.init(module);

        //Then
        assertEquals(earth, DI.united().earth());
    }


    @Test
    public void initByFactoryClass() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);

        //When
        DI.init(UnitedModuleFactory.class);

        //Then
        assertEquals(earth, DI.united().earth());
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
