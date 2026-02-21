package com.github.klee0kai.test.mowgli

import com.github.klee0kai.stone.weakref.Inject
import com.github.klee0kai.stone.weakref.Provider
import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.identity.Knowledge
import kotlin.jvm.JvmField

class School {

    @JvmField
    @Inject
    var historyLazyProvide: LazyProvide<History?>? = null

    @JvmField
    @Inject
    var knowledgePhantomProvide2: Ref<Knowledge?>? = null

    @JvmField
    @Inject
    var knowledgePhantomProvide3: Provider<Knowledge?>? = null

    @JvmField
    @Inject
    var knowledgePhantomProvide: PhantomProvide<Knowledge?>? = null

}
