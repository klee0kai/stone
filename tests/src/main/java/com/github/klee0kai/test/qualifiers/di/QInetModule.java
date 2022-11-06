package com.github.klee0kai.test.qualifiers.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import com.github.klee0kai.test.net.StoneApi;
import com.github.klee0kai.test.qualifiers.ProductType;


@Module
public class QInetModule {

    @Provide(cache = Provide.CacheType.FACTORY)
    StoneApi stoneApiFactory(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.STRONG)
    StoneApi stoneApiStrong(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.SOFT)
    StoneApi stoneApiSoft(ProductType productType) {
        return StoneApi.create(productType);
    }

    @Provide(cache = Provide.CacheType.WEAK)
    StoneApi stoneApiWeak(ProductType productType) {
        return StoneApi.create(productType);
    }


}
