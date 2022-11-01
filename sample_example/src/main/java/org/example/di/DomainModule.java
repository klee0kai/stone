package org.example.di;


import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import org.example.domain.RobotRepository;
import org.example.domain.StoneRepository;
import org.example.domain.UserRepository;
import org.example.module.InfinityStone;

@Module
public class DomainModule {

    public UserRepository userRepository() {
        return new UserRepository();
    }

    @Provide
    public RobotRepository robotRepository() {
        return new RobotRepository();
    }

    @Provide(cache = Provide.CacheType.STRONG)
    public StoneRepository stoneRepository(InfinityStone infinityStone) {
        return new StoneRepository(infinityStone);
    }

}
