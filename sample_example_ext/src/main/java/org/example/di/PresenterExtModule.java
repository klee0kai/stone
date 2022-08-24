package org.example.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Singleton;
import org.example.presenter.*;

@Module
public class PresenterExtModule extends PresenterModule {

    @Override
    @Singleton(cache = Singleton.CacheType.WEAK)
    public UserExtPresenter userPresenter() {
        return new UserExtPresenter();
    }

    @Override
    @Singleton(cache = Singleton.CacheType.WEAK)
    public RobotExtPresenter robotPresenter() {
        return new RobotExtPresenter();
    }

    @Override
    @Singleton(cache = Singleton.CacheType.WEAK)
    public StoneExtPresenter stonePresenter(String color) {
        return new StoneExtPresenter();
    }

}
