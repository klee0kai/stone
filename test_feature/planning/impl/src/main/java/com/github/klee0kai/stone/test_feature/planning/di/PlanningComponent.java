package com.github.klee0kai.stone.test_feature.planning.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.Init;
import com.github.klee0kai.stone.closed.IPrivateComponent;
import com.github.klee0kai.stone.test_feature.planning.PlanningRun;

@Component
public interface PlanningComponent extends IPrivateComponent {

    PlanningDependencies dependencies();

    ProjectsModule projectsModule();

    void inject(PlanningRun planningRun);

    @Init
    void initDep(PlanningDependencies planningRun);

}
