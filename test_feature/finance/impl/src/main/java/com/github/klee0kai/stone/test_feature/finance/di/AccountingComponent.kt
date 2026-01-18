package com.github.klee0kai.stone.test_feature.finance.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.Init

@Component
interface AccountingComponent {
    fun dependencies(): AccountingDependencies?

    fun accounting(): AccountingModule?

    @Init
    fun initDeps(deps: AccountingDependencies?)
}
