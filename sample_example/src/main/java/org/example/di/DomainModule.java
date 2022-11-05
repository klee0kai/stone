package org.example.di;


import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import org.example.domain.StoneInteractor;

@Module
public abstract class DomainModule {

    @Provide(cache = Provide.CacheType.WEAK)
    abstract public StoneInteractor stoneInteractor();

    @Provide(cache = Provide.CacheType.WEAK)
    public StoneInteractor stoneInteractor2(){
        return new StoneInteractor();
    }

}
