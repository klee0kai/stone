package org.example.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Singleton;
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

    @Singleton(scope = "someScope2")

    public UserExtRepository rep1() {
        return new UserExtRepository();
    }

    @Override
    public RobotExtRepository robotRepository() {
        return new RobotExtRepository();
    }

}
