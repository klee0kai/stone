package com.github.klee0kai.test.qualifiers.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.net.StoneApi;
import com.github.klee0kai.test.qualifiers.ProductType;


@Module
public class QInetModule {

    @Provide(cache = Provide.CacheType.FACTORY)
    public StoneApi stoneApiFactory(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.STRONG)
    public StoneApi stoneApiStrong(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.SOFT)
    public StoneApi stoneApiSoft(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.WEAK)
    public StoneApi stoneApiWeak(ProductType productType) {
        return StoneApi.create(productType);
    }


    @Provide(cache = Provide.CacheType.FACTORY)
    public StoneApi userApiFactory(ProductType productType, String token) {
        StoneApi api = StoneApi.create(productType);
        api.token = token;
        return api;
    }

    @Provide(cache = Provide.CacheType.STRONG)
    public StoneApi userApiStrong(ProductType productType, String token) {
        StoneApi api = StoneApi.create(productType);
        api.token = token;
        return api;
    }

    @Provide(cache = Provide.CacheType.SOFT)
    public StoneApi userApiSoft(ProductType productType, String token) {
        StoneApi api = StoneApi.create(productType);
        api.token = token;
        return api;
    }

    @Provide(cache = Provide.CacheType.WEAK)
    public StoneApi userApiWeak(ProductType productType, String token) {
        StoneApi api = StoneApi.create(productType);
        api.token = token;
        return api;
    }


}
