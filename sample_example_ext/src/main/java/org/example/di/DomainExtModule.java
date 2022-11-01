package org.example.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import org.example.domain.RobotExtRepository;
import org.example.domain.UserExtRepository;

@Module
public class DomainExtModule extends DomainModule {

    @Override
    public UserExtRepository userRepository() {
        return new UserExtRepository();
    }

    @Provide(scope = "someScope2")

    public UserExtRepository rep1() {
        return new UserExtRepository();
    }

    @Override
    public RobotExtRepository robotRepository() {
        return new RobotExtRepository();
    }

}
