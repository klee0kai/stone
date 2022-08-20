package org.example.di;

import com.klee0kai.stone.annotations.Module;
import org.example.domain.RobotExtRepository;
import org.example.domain.RobotRepository;
import org.example.domain.UserExtRepository;
import org.example.domain.UserRepository;

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


}
