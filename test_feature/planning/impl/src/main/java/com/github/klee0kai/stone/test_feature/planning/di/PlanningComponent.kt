package com.github.klee0kai.stone.test_feature.planning.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.Init
import com.github.klee0kai.stone.test_feature.planning.PlanningRun

@Component
interface PlanningComponent {
    fun dependencies(): PlanningDependencies?

    fun projectsModule(): ProjectsModule?

    fun inject(planningRun: PlanningRun?)

    @Init
    fun initDep(planningRun: PlanningDependencies?)
}
