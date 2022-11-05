package org.example.di;


import com.github.klee0kai.stone.annotations.Module;
import org.example.data.StoneRepository;

@Module
public interface DataModule {

    public StoneRepository stoneRep();

}
