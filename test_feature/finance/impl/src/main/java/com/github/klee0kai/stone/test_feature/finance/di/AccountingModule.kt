package com.github.klee0kai.stone.test_feature.finance.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.test_feature.finance.accounting.Accounting
import com.github.klee0kai.stone.test_feature.finance.store.AccountingStore

@Module
abstract class AccountingModule {

    @Provide(cache = Provide.CacheType.Soft)
    open fun accounting(): Accounting? {
        return null
    }

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun accountingStore(): AccountingStore?
}
