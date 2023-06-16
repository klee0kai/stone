package com.github.klee0kai.stone.test_feature.finance.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.Init;

@Component
public interface AccountingComponent {

    AccountingDependencies dependencies();

    AccountingModule accounting();

    @Init
    void initDeps(AccountingDependencies deps);

}
