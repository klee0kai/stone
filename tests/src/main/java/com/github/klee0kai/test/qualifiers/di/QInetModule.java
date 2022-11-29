package com.github.klee0kai.test.qualifiers.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.net.StoneApi;
import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType;
import com.github.klee0kai.test.qualifiers.di.qualifiers.Token;


@Module
public class QInetModule {

    @Provide(cache = Provide.CacheType.Factory)
    public StoneApi stoneApiFactory(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.Strong)
    public StoneApi stoneApiStrong(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.Soft)
    public StoneApi stoneApiSoft(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.Weak)
    public StoneApi stoneApiWeak(ProductType productType) {
        return StoneApi.create(productType);
    }


    @Provide(cache = Provide.CacheType.Factory)
    public StoneApi userApiFactory(ProductType productType, Token token) {
        StoneApi api = StoneApi.create(productType);
        api.token = token.token;
        return api;
    }

    @Provide(cache = Provide.CacheType.Strong)
    public StoneApi userApiStrong(ProductType productType, Token token) {
        StoneApi api = StoneApi.create(productType);
        api.token = token.token;
        return api;
    }

    @Provide(cache = Provide.CacheType.Soft)
    public StoneApi userApiSoft(ProductType productType, Token token) {
        StoneApi api = StoneApi.create(productType);
        api.token = token.token;
        return api;
    }

    @Provide(cache = Provide.CacheType.Weak)
    public StoneApi userApiWeak(ProductType productType, Token token) {
        StoneApi api = StoneApi.create(productType);
        api.token = token.token;
        return api;
    }


}
