package com.github.klee0kai.test.inject

import com.github.klee0kai.stone.types.IRef
import com.github.klee0kai.stone.types.LazyProvide
import com.github.klee0kai.stone.types.PhantomProvide
import com.github.klee0kai.test.inject.forest.History
import com.github.klee0kai.test.inject.identity.Knowledge
import javax.inject.Inject
import javax.inject.Provider

class School {
    @Inject
    var historyLazyProvide: LazyProvide<History>? = null

    @Inject
    var knowledgePhantomProvide2: IRef<Knowledge>? = null

    @Inject
    var knowledgePhantomProvide3: Provider<Knowledge>? = null

    @Inject
    var knowledgePhantomProvide: PhantomProvide<Knowledge>? = null
    fun build() {
        Forest.DI!!.inject(this)
    }
}