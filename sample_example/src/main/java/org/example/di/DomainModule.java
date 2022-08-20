package org.example.di;


import com.klee0kai.stone.annotations.Singletone;
import com.klee0kai.stone.annotations.Module;
import org.example.domain.RobotRepository;
import org.example.domain.UserRepository;

@Module
public class DomainModule {


    public UserRepository userRepository() {
        return new UserRepository();
    }

    public RobotRepository robotRepository() {
        return new RobotRepository();
    }

}
