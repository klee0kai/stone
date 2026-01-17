package com.github.klee0kai.stone.test_feature.consulting.dependencies

import com.github.klee0kai.stone.test_feature.consulting.di.ConsultingComponent
import com.github.klee0kai.stone.test_feature.consulting.di.ConsultingComponentStoneComponent
import com.github.klee0kai.stone.test_feature.finance.di.AccountingComponent
import com.github.klee0kai.stone.test_feature.finance.di.AccountingComponentStoneComponent
import com.github.klee0kai.stone.test_feature.hr.di.HrComponent
import com.github.klee0kai.stone.test_feature.hr.di.HrComponentStoneComponent
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull


class ProvideDependenciesTests {

    @Test
    fun hrNoDepsTest() {
        //Given
        val appDI: ConsultingComponent = ConsultingComponentStoneComponent()
        val featureDi: HrComponent = HrComponentStoneComponent()

        //When
        featureDi.initDeps(appDI)

        //Then
        val hrDeps = featureDi.hrDependencies()
        assertEquals(appDI, hrDeps)
    }

    @Test
    fun provideLogisticProjectDepsTest() {
        //Given
        val appDI: ConsultingComponent? = ConsultingComponentStoneComponent()
        val featureDi: AccountingComponent = AccountingComponentStoneComponent()

        //When
        featureDi.initDeps(appDI)

        //Then
        val logisticProject: LogisticProject? = featureDi.dependencies()?.logisticProject()
        assertNotNull(logisticProject)
    }

}
