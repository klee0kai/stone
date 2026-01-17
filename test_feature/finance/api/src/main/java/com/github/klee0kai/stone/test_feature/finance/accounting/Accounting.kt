package com.github.klee0kai.stone.test_feature.finance.accounting;

import com.github.klee0kai.stone.test_feature.finance.model.BalanceInfo;

public interface Accounting {

    BalanceInfo currentBalance();

    BalanceInfo plannedBalance(long days);

}
