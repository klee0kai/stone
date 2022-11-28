
package com.dirgub.klee0kai.stone.text_ext.inject;

import com.github.klee0kai.test_ext.inject.OldForest;
import com.github.klee0kai.test_ext.inject.OldHorse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class OldInjectTests {

    @Test
    public void simpleInjectTest() {
        // create common component for all app
        OldForest forest = new OldForest();
        forest.create();
        forest.old();

        //use sub app components
        OldHorse horse = new OldHorse();
        horse.born();

        //check inject is completed
        assertNotNull(horse.blood);
        assertNotNull(horse.knowledge);
        assertNotNull(horse.conscience);
        assertNotNull(horse.oldKnowledge);
        assertNotNull(horse.osteoarthritis);
    }



}
