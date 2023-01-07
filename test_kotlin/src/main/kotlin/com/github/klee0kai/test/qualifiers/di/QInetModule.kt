package com.github.klee0kai.test.qualifiers.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.net.StoneApi
import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType
import com.github.klee0kai.test.qualifiers.di.qualifiers.Token

@Module
open class QInetModule {
    @Provide(cache = Provide.CacheType.Factory)
    open fun stoneApiFactory(productType: ProductType?): StoneApi {
        return StoneApi.create(productType)
    }

    @Provide(cache = Provide.CacheType.Strong)
    open fun stoneApiStrong(productType: ProductType): StoneApi {
        return StoneApi.create(productType)
    }

    @Provide(cache = Provide.CacheType.Soft)
    open fun stoneApiSoft(productType: ProductType?): StoneApi {
        return StoneApi.create(productType)
    }

    @Provide(cache = Provide.CacheType.Weak)
    open fun stoneApiWeak(productType: ProductType?): StoneApi {
        return StoneApi.create(productType)
    }

    @Provide(cache = Provide.CacheType.Factory)
    open fun userApiFactory(productType: ProductType?, token: Token): StoneApi {
        val api: StoneApi = StoneApi.create(productType)
        api.token = token.token
        return api
    }

    @Provide(cache = Provide.CacheType.Strong)
    open fun userApiStrong(productType: ProductType?, token: Token): StoneApi {
        val api: StoneApi = StoneApi.create(productType)
        api.token = token.token
        return api
    }

    @Provide(cache = Provide.CacheType.Soft)
    open fun userApiSoft(productType: ProductType?, token: Token): StoneApi {
        val api: StoneApi = StoneApi.create(productType)
        api.token = token.token
        return api
    }

    @Provide(cache = Provide.CacheType.Weak)
    open fun userApiWeak(productType: ProductType?, token: Token): StoneApi {
        val api: StoneApi = StoneApi.create(productType)
        api.token = token.token
        return api
    }
}