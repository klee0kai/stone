package org.example.di;


import com.klee0kai.stone.annotations.Item;
import com.klee0kai.stone.annotations.Module;
import org.example.domain.RobotRepository;
import org.example.domain.UserRepository;

@Module
public class DomainModule {


    @Item
    public UserRepository userRepository() {
        return new UserRepository();
    }

    @Item
    public RobotRepository robotRepository() {
        return new RobotRepository();
    }

}
