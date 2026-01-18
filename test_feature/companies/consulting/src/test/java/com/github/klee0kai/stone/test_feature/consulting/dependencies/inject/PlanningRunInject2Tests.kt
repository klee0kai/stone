package com.github.klee0kai.stone.test_feature.consulting.dependencies.inject

import com.github.klee0kai.stone.test_feature.consulting.di.ConsultingComponent
import com.github.klee0kai.stone.test_feature.consulting.di.ConsultingComponentStoneComponent
import com.github.klee0kai.stone.test_feature.finance.model.WorkCalendar
import com.github.klee0kai.stone.test_feature.planning.PlanningRun
import com.github.klee0kai.stone.test_feature.planning.di.PlanningComponent
import com.github.klee0kai.stone.test_feature.planning.di.PlanningComponentStoneComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class PlanningRunInject2Tests {

    @Test
    fun simpleInjectTest() {
        //Given
        val di: ConsultingComponent = ConsultingComponentStoneComponent()
        di.workCalendar(WorkCalendar("test", 240, 8))
        val featureDi: PlanningComponent = PlanningComponentStoneComponent()
        featureDi.initDep(di)
        val planningRun: PlanningRun = PlanningRun(featureDi)

        //When
        planningRun.start()

        //Then
        assertNotNull(planningRun.logisticProject)
        assertNotNull(planningRun.workCalendar)
        assertNotNull(planningRun.securityDepartment)
    }


    @Test
    fun commonComponentsInjectTest() {
        //Given
        val di: ConsultingComponent = ConsultingComponentStoneComponent()
        di.workCalendar(WorkCalendar("test", 240, 8))
        val featureDi: PlanningComponent = PlanningComponentStoneComponent()
        featureDi.initDep(di)
        val planningRun1 = PlanningRun(featureDi)
        val planningRun2 = PlanningRun(featureDi)

        //When
        planningRun1.start()
        planningRun2.start()

        //Then
        assertEquals(planningRun1.logisticProject!!.id, planningRun2.logisticProject!!.id)
        assertEquals(planningRun1.securityDepartment!!.id, planningRun2.securityDepartment!!.id)
        assertEquals(planningRun1.workCalendar!!.uuid, planningRun2.workCalendar!!.uuid)
    }
}
