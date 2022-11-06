package org.example.di;


import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import org.example.data.StoneRepository;

@Module
public interface DataModule {

    @Provide(cache = Provide.CacheType.STRONG)
    public StoneRepository stoneRep();

}
