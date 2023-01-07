package com.github.klee0kai.test_kotlin.mowgli

import com.github.klee0kai.stone.types.IRef
import com.github.klee0kai.stone.types.LazyProvide
import com.github.klee0kai.stone.types.PhantomProvide
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.identity.Knowledge
import javax.inject.Inject
import javax.inject.Provider

class University {

    @Inject
  lateinit  var historyLazyProvide: LazyProvide<History>

    @Inject
    lateinit var knowledgePhantomProvide2: IRef<Knowledge>

    @Inject
    lateinit  var knowledgePhantomProvide3: Provider<Knowledge>

    @Inject
    lateinit var knowledgePhantomProvide: PhantomProvide<Knowledge>


    fun build() {
        RainForest.DI.inject(this)
    }

}