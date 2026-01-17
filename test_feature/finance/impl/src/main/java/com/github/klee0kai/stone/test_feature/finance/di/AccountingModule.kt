package com.github.klee0kai.stone.test_feature.finance.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.test_feature.finance.accounting.Accounting;
import com.github.klee0kai.stone.test_feature.finance.store.AccountingStore;

@Module
public abstract class AccountingModule {

    @Provide(cache = Provide.CacheType.Soft)
    public Accounting accounting() {
        return null;
    }

    @Provide(cache = Provide.CacheType.Soft)
    public abstract AccountingStore accountingStore();

}
