package com.github.klee0kai.stone.test_feature.finance.accounting

import com.github.klee0kai.stone.test_feature.finance.model.BalanceInfo

class AccountingImpl : Accounting {

    override fun currentBalance(): BalanceInfo? {
        return null
    }

    override fun plannedBalance(days: Long): BalanceInfo? {
        return null
    }
}
