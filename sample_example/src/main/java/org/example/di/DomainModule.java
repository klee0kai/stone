package org.example.di;


import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Singleton;
import org.example.domain.RobotRepository;
import org.example.domain.StoneRepository;
import org.example.domain.UserRepository;
import org.example.module.InfinityStone;

@Module
public class DomainModule {

    public UserRepository userRepository() {
        return new UserRepository();
    }

    @Singleton(scope = "someScope")
    public RobotRepository robotRepository() {
        return new RobotRepository();
    }

    @Singleton(cache = Singleton.CacheType.STRONG, scope = "stone")
    public StoneRepository stoneRepository(InfinityStone infinityStone) {
        return new StoneRepository(infinityStone);
    }

}
