package com.github.klee0kai.test.qualifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.net.StoneApi
import com.github.klee0kai.test.data.StoneRepository
import com.github.klee0kai.test.qualifiers.di.QTestComponent
import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType
import com.github.klee0kai.test.qualifiers.di.qualifiers.Token
import com.github.klee0kai.test.qualifiers.di.qualifiers.UserId
import javax.inject.Inject

class QApp {
    var DI: QTestComponent = Stone.createComponent(QTestComponent::class.java)

    @Inject
    public var stoneRepository: StoneRepository? = null

    @Inject
    public var stoneApi: StoneApi? = null
    fun startSimple() {
        DI.inject(this)
    }

    fun startDebug1() {
        DI.inject(this, ProductType.DEBUG)
    }

    fun startDemo1() {
        //check independence to sequence
        DI.inject(ProductType.DEMO, UserId("demo_user_id"), Token("demo_token"), this)
    }

    fun startRelease() {
        DI.inject(this, Token("release_token"), ProductType.RELEASE)
    }
}