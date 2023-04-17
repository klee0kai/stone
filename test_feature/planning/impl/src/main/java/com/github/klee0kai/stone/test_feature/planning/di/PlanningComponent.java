package com.github.klee0kai.stone.test_feature.planning.di;

import com.github.klee0kai.stone.annotations.component.Component;

@Component
public interface PlanningComponent {

    PlanningDependencies dependencies();

    ProjectsModule projectsModule();

}
