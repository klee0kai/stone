package com.github.klee0kai.test_kotlin.mowgli.animal

import com.github.klee0kai.stone.types.wrappers.IRef
import com.github.klee0kai.stone.types.wrappers.LazyProvide
import com.github.klee0kai.stone.types.wrappers.PhantomProvide
import com.github.klee0kai.test.mowgli.animal.IAnimal
import com.github.klee0kai.test.mowgli.body.Blood
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.identity.Conscience
import com.github.klee0kai.test.mowgli.identity.Knowledge
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Provider

class Gorilla : IAnimal {

    @Inject
    var blood: Blood? = null

    @Inject
    var earth: Earth? = null

    @Inject
    var history: History? = null

    @Inject
    var conscience: Conscience? = null

    @Inject
    lateinit var knowledge: Knowledge

    @Inject
    lateinit var knowledgeWeakRef: WeakReference<Knowledge>

    @Inject
    lateinit var knowledgeSoftRef: SoftReference<Knowledge>

    @Inject
    var knowledgeLazyProvide: LazyProvide<Knowledge>? = null

    @Inject
    var knowledgePhantomProvide2: IRef<Knowledge>? = null

    @Inject
    var knowledgePhantomProvide3: Provider<Knowledge>? = null

    @Inject
    var knowledgePhantomProvide: PhantomProvide<Knowledge>? = null

}