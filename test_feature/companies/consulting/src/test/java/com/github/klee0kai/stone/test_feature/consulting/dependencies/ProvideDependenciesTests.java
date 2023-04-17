package com.github.klee0kai.stone.test_feature.consulting.dependencies;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.test_feature.hr.di.HrComponent;
import com.github.klee0kai.stone.test_feature.hr.di.HrDependencies;
import org.junit.jupiter.api.Test;

public class ProvideDependenciesTests {

    @Test
    void hrNoDepsTest() {
        //Given
        HrComponent di = Stone.createComponent(HrComponent.class);
        HrDependencies hrDeps = di.hrDependencies();

        //Then
    }

}
