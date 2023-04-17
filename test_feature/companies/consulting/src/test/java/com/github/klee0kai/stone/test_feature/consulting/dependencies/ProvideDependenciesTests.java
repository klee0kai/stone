package com.github.klee0kai.stone.test_feature.consulting.dependencies;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.test_feature.consulting.di.ConsultingComponent;
import com.github.klee0kai.stone.test_feature.hr.di.HrComponent;
import com.github.klee0kai.stone.test_feature.hr.di.HrDependencies;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProvideDependenciesTests {

    @Test
    void hrNoDepsTest() {
        //Given
        ConsultingComponent appDI = Stone.createComponent(ConsultingComponent.class);
        HrComponent featureDi = Stone.createComponent(HrComponent.class);

        //When
        featureDi.initDependencies(appDI);

        //Then
        HrDependencies hrDeps = featureDi.hrDependencies();
        assertNotNull(hrDeps);
    }

}
