package com.github.klee0kai.test.inject

import com.github.klee0kai.stone.types.IRef
import com.github.klee0kai.stone.types.LazyProvide
import com.github.klee0kai.stone.types.PhantomProvide
import com.github.klee0kai.test.inject.forest.Blood
import com.github.klee0kai.test.inject.forest.Earth
import com.github.klee0kai.test.inject.forest.History
import com.github.klee0kai.test.inject.forest.IAnimal
import com.github.klee0kai.test.inject.identity.Conscience
import com.github.klee0kai.test.inject.identity.Knowledge
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

    @Inject
    var knowledge: Knowledge? = null

    @Inject
    var knowledgeWeakRef: WeakReference<Knowledge>? = null

    @Inject
    var knowledgeSoftRef: SoftReference<Knowledge>? = null

    @Inject
    var knowledgeLazyProvide: LazyProvide<Knowledge>? = null

    @Inject
    var knowledgePhantomProvide2: IRef<Knowledge>? = null

    @Inject
    var knowledgePhantomProvide3: Provider<Knowledge>? = null

    @Inject
    var knowledgePhantomProvide: PhantomProvide<Knowledge>? = null
    override fun born() {
        Forest.DI!!.inject(this)
    }
}