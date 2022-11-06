package org.example.di;


import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import org.example.domain.StoneInteractor;

@Module
public abstract class DomainModule {

    @Provide(cache = Provide.CacheType.SOFT)
    abstract public StoneInteractor stoneInteractor();

    @Provide(cache = Provide.CacheType.SOFT)
    public StoneInteractor stoneInteractor2() {
        return new StoneInteractor();
    }

}
