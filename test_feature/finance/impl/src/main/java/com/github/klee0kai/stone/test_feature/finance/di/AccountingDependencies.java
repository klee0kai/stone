package com.github.klee0kai.stone.test_feature.finance.di;

import com.github.klee0kai.stone.annotations.dependencies.Dependencies;
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartment;
import com.github.klee0kai.stone.test_feature.hr.department.SoftwareDepartment;
import com.github.klee0kai.stone.test_feature.planning.project.BuildFactoryProject;
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProject;

@Dependencies
public interface AccountingDependencies {

    SecurityDepartment securityDepartment();

    SoftwareDepartment softwareDepartment();

    LogisticProject logisticProject();

    BuildFactoryProject buildFactoryProject();

}
