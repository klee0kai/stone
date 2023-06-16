package com.github.klee0kai.stone.test_feature.finance.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.Init;
import com.github.klee0kai.stone.closed.IComponent;

@Component
public interface AccountingComponent extends IComponent {

    AccountingDependencies dependencies();

    AccountingModule accounting();

    @Init
    void initDeps(AccountingDependencies deps);

}
