package com.github.klee0kai.stone.test_feature.planning.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.interfaces.IComponent;

@Component
public interface PlanningComponent extends IComponent {

    PlanningDependencies dependencies();

    ProjectsModule projectsModule();

}
