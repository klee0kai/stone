package com.github.klee0kai.test.mowgli.animal

import com.github.klee0kai.stone.Inject
import com.github.klee0kai.stone.Provider
import com.github.klee0kai.stone.weakref.*
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.github.klee0kai.test.mowgli.body.Blood
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.identity.Conscience
import com.github.klee0kai.test.mowgli.identity.Knowledge
import kotlin.jvm.JvmField

class Mowgli : IAnimal {

    @Inject
    var blood: Blood? = null

    @Inject
    var earth: Earth? = null

    @Inject
    var history: History? = null

    @Inject
    var conscience: Conscience? = null

    @JvmField
    @Inject
    var knowledge: Knowledge? = null

    @JvmField
    @Inject
    var knowledgeWeakRef: WeakRef<Knowledge?>? = null

    @JvmField
    @Inject
    var knowledgeSoftRef: SoftRef<Knowledge?>? = null

    @JvmField
    @Inject
    var knowledgeLazyProvide: LazyProvide<Knowledge?>? = null

    @JvmField
    @Inject
    var knowledgePhantomProvide2: Ref<Knowledge?>? = null

    @Inject
    var knowledgePhantomProvide3: Provider<Knowledge?>? = null

    @JvmField
    @Inject
    var knowledgePhantomProvide: PhantomProvide<Knowledge?>? = null

    @JvmField
    var methodKnowledgeWeakRef: WeakRef<Knowledge?>? = null

    @JvmField
    var methodKnowledgeSoftRef: SoftRef<Knowledge?>? = null

    @JvmField
    var methodKnowledgeLazyProvide: LazyProvide<Knowledge?>? = null

    @JvmField
    var methodKnowledgePhantomProvide2: Ref<Knowledge?>? = null

    @JvmField
    var methodKnowledgePhantomProvide3: Provider<Knowledge?>? = null

    @JvmField
    var methodKnowledgePhantomProvide: PhantomProvide<Knowledge?>? = null

    @Inject
    fun refInject(knowledgeWeakRef: WeakRef<Knowledge?>?, knowledgeSoftRef: SoftRef<Knowledge?>?) {
        methodKnowledgeWeakRef = knowledgeWeakRef
        methodKnowledgeSoftRef = knowledgeSoftRef
    }


    @Inject
    fun wrapperInject(
        knowledgeLazyProvide: LazyProvide<Knowledge?>?,
        knowledgePhantomProvide2: Ref<Knowledge?>?,
        knowledgePhantomProvide3: Provider<Knowledge?>?,
        knowledgePhantomProvide: PhantomProvide<Knowledge?>?
    ) {
        methodKnowledgeLazyProvide = knowledgeLazyProvide
        methodKnowledgePhantomProvide2 = knowledgePhantomProvide2
        methodKnowledgePhantomProvide3 = knowledgePhantomProvide3
        methodKnowledgePhantomProvide = knowledgePhantomProvide
    }
}
