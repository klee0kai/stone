package com.github.klee0kai.wiki.init;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Module
public interface StarsDependencies2 {

    @Provide(cache = Provide.CacheType.Factory)
    Sun sun();

}
