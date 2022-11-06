package org.example.di;


import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import org.example.data.StoneRepository;

@Module
public interface DataModule {

    @Provide(cache = Provide.CacheType.STRONG)
    public StoneRepository stoneRep();

}
