package com.github.klee0kai.wiki.start;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.mowgli.galaxy.Earth;

@Module
public class SevenPlanetModule {

    @Provide(cache = Provide.CacheType.Soft)
    public Earth earth() {
        return new Earth();
    }

}
