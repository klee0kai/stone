package com.github.klee0kai.stone.test_feature.finance.accounting

import com.github.klee0kai.stone.test_feature.finance.model.BalanceInfo

interface Accounting {

    fun currentBalance(): BalanceInfo?

    fun plannedBalance(days: Long): BalanceInfo?

}
