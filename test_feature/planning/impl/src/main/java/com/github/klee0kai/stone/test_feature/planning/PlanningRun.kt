package com.github.klee0kai.stone.test_feature.planning;

import com.github.klee0kai.stone.test_feature.finance.model.WorkCalendar;
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartment;
import com.github.klee0kai.stone.test_feature.planning.di.PlanningComponent;
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProject;

import javax.inject.Inject;

public class PlanningRun {

    @Inject
    public WorkCalendar workCalendar;

    @Inject
    public LogisticProject logisticProject;

    @Inject
    public SecurityDepartment securityDepartment;

    private final PlanningComponent component;

    public PlanningRun(PlanningComponent component) {
        this.component = component;
    }

    public void start() {
        component.inject(this);
    }

}
