package org.example.di;

import com.github.klee0kai.stone.annotations.Module;
import org.example.domain.RobotExtRepository;
import org.example.domain.StoneExtRepository;
import org.example.domain.UserExtRepository;
import org.example.module.InfinityStone;

@Module
public class DomainExtModule extends DomainModule {


    @Override
    public UserExtRepository userRepository() {
        return new UserExtRepository();
    }

    @Override
    public RobotExtRepository robotRepository() {
        return new RobotExtRepository();
    }

    @Override
    public StoneExtRepository stoneRepository(InfinityStone infinityStone) {
        return new StoneExtRepository(infinityStone);
    }
}
