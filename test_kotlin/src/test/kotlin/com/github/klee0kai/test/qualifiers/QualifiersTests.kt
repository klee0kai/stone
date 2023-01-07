package com.github.klee0kai.test.qualifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.qualifiers.di.QTestComponent
import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType
import com.github.klee0kai.test.qualifiers.di.qualifiers.Token
import com.github.klee0kai.test.qualifiers.di.qualifiers.UserId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.List

class QualifiersTests {
    @Test
    fun singleQualifiersTest() {
        val DI = Stone.createComponent(QTestComponent::class.java)
        val stoneRepository1 = DI.data().stoneRepository(UserId("userId1"))
        val stoneRepositoryNull = DI.data().stoneRepository(UserId(null))
        val stoneRepository2 = DI.data().stoneRepository(UserId("user2"))


        //check created components
        assertNotNull(stoneRepository1)
        assertNotNull(stoneRepositoryNull)
        assertNotNull(stoneRepository2)

        //check created components was different
        assertNotEquals(stoneRepository1.uuid, stoneRepositoryNull.uuid)
        assertNotEquals(stoneRepositoryNull.uuid, stoneRepository2.uuid)

        //check created components got create arguments
        assertEquals(stoneRepository1.userId, "userId1")
        assertNull(stoneRepositoryNull.userId)
        assertEquals(stoneRepository2.userId, "user2")
        val stoneRepository1cached = DI.data().stoneRepository(UserId("userId1"))
        val stoneRepositoryNullCached = DI.data().stoneRepository(UserId(null))
        val stoneRepository2Cached = DI.data().stoneRepository(UserId("user2"))

        // check cached components
        assertEquals(stoneRepository1cached.uuid, stoneRepository1.uuid)
        assertEquals(stoneRepositoryNullCached.uuid, stoneRepositoryNull.uuid)
        assertEquals(stoneRepository2Cached.uuid, stoneRepository2.uuid)
    }

    @Test
    fun enumQualifiersFactoryTest() {
        val DI = Stone.createComponent(QTestComponent::class.java)
        val debugApi = DI.inet().stoneApiFactory(ProductType.DEBUG)
        val demoApi = DI.inet().stoneApiFactory(ProductType.DEMO)
        val demo2Api = DI.inet().stoneApiFactory(ProductType.DEMO)
        val demo3Api = DI.inet().stoneApiFactory(ProductType.DEMO)
        val releaseApi = DI.inet().stoneApiFactory(ProductType.RELEASE)
        val nullApi = DI.inet().stoneApiFactory(null)
        val null2Api = DI.inet().stoneApiFactory(null)


        assertEquals(debugApi.apiUrl, "https://debug.org")
        assertEquals(demoApi.apiUrl, "https://demo.org")
        assertEquals(demo3Api.apiUrl, "https://demo.org")
        assertNull(null2Api.apiUrl)


        val apis = List.of(debugApi, demoApi, demo2Api, demo3Api, releaseApi, nullApi, null2Api)
        for (i in apis.indices) for (j in i + 1 until apis.size) assertNotEquals(apis[i], apis[j])
    }

    @Test
    fun enumQualifiersCachedTest() {
        val DI = Stone.createComponent(QTestComponent::class.java)
        val debugApi = DI.inet().stoneApiSoft(ProductType.DEBUG)
        val demoApi = DI.inet().stoneApiSoft(ProductType.DEMO)
        val demo2Api = DI.inet().stoneApiSoft(ProductType.DEMO)
        val demo3Api = DI.inet().stoneApiSoft(ProductType.DEMO)
        val releaseApi = DI.inet().stoneApiSoft(ProductType.RELEASE)
        val nullApi = DI.inet().stoneApiSoft(null)
        val null2Api = DI.inet().stoneApiSoft(null)


        assertEquals(debugApi.apiUrl, "https://debug.org")
        assertEquals(demoApi.apiUrl, "https://demo.org")
        assertEquals(demo3Api.apiUrl, "https://demo.org")
        assertNull(null2Api.apiUrl)

        val apis = List.of(debugApi, demoApi, releaseApi, nullApi)
        for (i in apis.indices) for (j in i + 1 until apis.size) assertNotEquals(apis[i], apis[j])

        assertEquals(demoApi.uuid, demo2Api.uuid)
        assertEquals(demo2Api.uuid, demo3Api.uuid)
        assertEquals(nullApi.uuid, null2Api.uuid)
    }

    @Test
    fun multiQualifiersFactoryTest() {
        val DI = Stone.createComponent(QTestComponent::class.java)
        val debugApi = DI.inet().userApiFactory(ProductType.DEBUG, Token("token"))
        val demoApi = DI.inet().userApiFactory(ProductType.DEMO, Token("token"))
        val demo2Api = DI.inet().userApiFactory(ProductType.DEMO, Token("token"))
        val demo3Api = DI.inet().userApiFactory(ProductType.DEMO, Token("token1"))
        val releaseApi = DI.inet().userApiFactory(ProductType.RELEASE, Token(null))
        val nullApi = DI.inet().userApiFactory(null, Token("token"))
        val null2Api = DI.inet().userApiFactory(null, Token("token"))
        val null3Api = DI.inet().userApiFactory(null, Token("token2"))

        // check product type arg
        assertEquals(debugApi.apiUrl, "https://debug.org")
        assertEquals(demoApi.apiUrl, "https://demo.org")
        assertEquals(demo3Api.apiUrl, "https://demo.org")
        assertNull(null2Api.apiUrl)

        // check token arg
        assertEquals(demo2Api.token, "token")
        assertEquals(demo3Api.token, "token1")
        assertNull(releaseApi.token)
        assertEquals(null3Api.token, "token2")

        //check factory creating
        val apis = List.of(debugApi, demoApi, demo2Api, demo3Api, releaseApi, nullApi, null2Api, null3Api)
        for (i in apis.indices) for (j in i + 1 until apis.size) assertNotEquals(apis[i], apis[j])
    }

    @Test
    fun multiQualifiersCacheTest() {
        val DI = Stone.createComponent(QTestComponent::class.java)
        val debugApi = DI.inet().userApiStrong(ProductType.DEBUG, Token("token"))
        val demoApi = DI.inet().userApiStrong(ProductType.DEMO, Token("token"))
        val demo2Api = DI.inet().userApiStrong(ProductType.DEMO, Token("token"))
        val demo3Api = DI.inet().userApiStrong(ProductType.DEMO, Token("token1"))
        val releaseApi = DI.inet().userApiStrong(ProductType.RELEASE, Token(null))
        val nullApi = DI.inet().userApiStrong(null, Token("token"))
        val null2Api = DI.inet().userApiStrong(null, Token("token"))
        val null3Api = DI.inet().userApiStrong(null, Token("token2"))
        val null4Api = DI.inet().userApiStrong(null, Token(null))
        val null5Api = DI.inet().userApiStrong(null, Token(null))

        // check product type arg
        assertEquals(debugApi.apiUrl, "https://debug.org")
        assertEquals(demoApi.apiUrl, "https://demo.org")
        assertEquals(demo3Api.apiUrl, "https://demo.org")
        assertNull(null2Api.apiUrl)

        // check token arg
        assertEquals(demo2Api.token, "token")
        assertEquals(demo3Api.token, "token1")
        assertNull(releaseApi.token)
        assertEquals(null3Api.token, "token2")

        //check different component creating
        val apis = List.of(debugApi, demoApi, demo3Api, releaseApi, nullApi, null3Api)
        for (i in apis.indices) for (j in i + 1 until apis.size) assertNotEquals(apis[i], apis[j])

        //check cached components
        assertEquals(demoApi.uuid, demo2Api.uuid)
        assertEquals(nullApi.uuid, null2Api.uuid)
        assertEquals(null4Api.uuid, null5Api.uuid)
    }
}