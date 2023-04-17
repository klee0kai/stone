package com.github.klee0kai.stone.test_feature.finance.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.test_feature.finance.accounting.Accounting;
import com.github.klee0kai.stone.test_feature.finance.store.AccountingStore;

@Module
public interface AccountingModule {

    Accounting accounting();

    AccountingStore accountingStore();

}
