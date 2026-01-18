package com.github.klee0kai.test.mowgli.animal

import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.github.klee0kai.test.mowgli.body.Blood
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.identity.Conscience
import com.github.klee0kai.test.mowgli.identity.Knowledge
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Provider

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
    var knowledgeWeakRef: WeakReference<Knowledge?>? = null

    @JvmField
    @Inject
    var knowledgeSoftRef: SoftReference<Knowledge?>? = null

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
    var methodKnowledgeWeakRef: WeakReference<Knowledge?>? = null

    @JvmField
    var methodKnowledgeSoftRef: SoftReference<Knowledge?>? = null

    @JvmField
    var methodKnowledgeLazyProvide: LazyProvide<Knowledge?>? = null

    @JvmField
    var methodKnowledgePhantomProvide2: Ref<Knowledge?>? = null

    @JvmField
    var methodKnowledgePhantomProvide3: Provider<Knowledge?>? = null

    @JvmField
    var methodKnowledgePhantomProvide: PhantomProvide<Knowledge?>? = null

    @Inject
    fun refInject(knowledgeWeakRef: WeakReference<Knowledge?>?, knowledgeSoftRef: SoftReference<Knowledge?>?) {
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
