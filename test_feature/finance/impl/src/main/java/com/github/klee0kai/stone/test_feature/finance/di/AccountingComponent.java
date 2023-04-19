package com.github.klee0kai.stone.test_feature.finance.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.interfaces.IComponent;

@Component
public interface AccountingComponent extends IComponent {

    AccountingDependencies dependencies();

    AccountingModule accounting();

}
