package com.github.klee0kai.stone.test_feature.consulting.dependencies;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.test_feature.consulting.di.ConsultingComponent;
import com.github.klee0kai.stone.test_feature.finance.di.AccountingComponent;
import com.github.klee0kai.stone.test_feature.hr.di.HrComponent;
import com.github.klee0kai.stone.test_feature.hr.di.HrDependencies;
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProvideDependencies2Tests {

    @Test
    void hrNoDepsTest() {
        //Given
        ConsultingComponent appDI = Stone.createComponent(ConsultingComponent.class);
        HrComponent featureDi = Stone.createComponent(HrComponent.class);

        //When
        featureDi.initDeps(appDI);

        //Then
        HrDependencies hrDeps = featureDi.hrDependencies();
        assertEquals(appDI, hrDeps);
    }

    @Test
    void provideLogisticProjectDepsTest() {
        //Given
        ConsultingComponent appDI = Stone.createComponent(ConsultingComponent.class);
        AccountingComponent featureDi = Stone.createComponent(AccountingComponent.class);

        //When
        featureDi.initDeps(appDI);

        //Then
        LogisticProject logisticProject = featureDi.dependencies().logisticProject();
        assertNotNull(logisticProject);
    }

}
