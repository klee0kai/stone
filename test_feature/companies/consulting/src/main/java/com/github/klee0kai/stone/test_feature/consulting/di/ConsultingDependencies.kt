package com.github.klee0kai.stone.test_feature.consulting.di;

import com.github.klee0kai.stone.test_feature.finance.di.AccountingDependencies;
import com.github.klee0kai.stone.test_feature.hr.di.HrDependencies;
import com.github.klee0kai.stone.test_feature.planning.di.PlanningDependencies;

public interface ConsultingDependencies extends HrDependencies, PlanningDependencies, AccountingDependencies {
}
