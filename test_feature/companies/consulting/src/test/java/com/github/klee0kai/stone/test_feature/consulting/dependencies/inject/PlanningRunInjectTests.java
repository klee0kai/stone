package com.github.klee0kai.stone.test_feature.consulting.dependencies.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.test_feature.consulting.di.ConsultingComponent;
import com.github.klee0kai.stone.test_feature.finance.model.WorkCalendar;
import com.github.klee0kai.stone.test_feature.planning.PlanningRun;
import com.github.klee0kai.stone.test_feature.planning.di.PlanningComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlanningRunInjectTests {

    @Test
    public void simpleInjectTest() {
        //Given
        ConsultingComponent di = Stone.createComponent(ConsultingComponent.class);
        di.workCalendar(new WorkCalendar("test", 240, 8));
        PlanningComponent featureDi = Stone.createComponent(PlanningComponent.class);
        featureDi.initDep(di);
        PlanningRun planningRun = new PlanningRun(featureDi);

        //When
        planningRun.start();

        //Then
        assertNotNull(planningRun.logisticProject);
        assertNotNull(planningRun.workCalendar);
        assertNotNull(planningRun.securityDepartment);
    }


    @Test
    public void commonComponentsInjectTest() {
        //Given
        ConsultingComponent di = Stone.createComponent(ConsultingComponent.class);
        di.workCalendar(new WorkCalendar("test", 240, 8));
        PlanningComponent featureDi = Stone.createComponent(PlanningComponent.class);
        featureDi.initDep(di);
        PlanningRun planningRun1 = new PlanningRun(featureDi);
        PlanningRun planningRun2 = new PlanningRun(featureDi);

        //When
        planningRun1.start();
        planningRun2.start();

        //Then
        assertEquals(planningRun1.logisticProject.getId(), planningRun2.logisticProject.getId());
        assertEquals(planningRun1.securityDepartment.getId(), planningRun2.securityDepartment.getId());
        assertEquals(planningRun1.workCalendar.uuid, planningRun2.workCalendar.uuid);
    }

}
