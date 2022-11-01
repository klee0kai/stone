package org.example.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import org.example.presenter.*;

@Module
public class PresenterExtModule extends PresenterModule {

    @Override
    @Provide(cache = Provide.CacheType.WEAK)
    public UserExtPresenter userPresenter() {
        return new UserExtPresenter();
    }

    @Override
    @Provide(cache = Provide.CacheType.WEAK)
    public RobotExtPresenter robotPresenter() {
        return new RobotExtPresenter();
    }

    @Override
    @Provide(cache = Provide.CacheType.WEAK)
    public StoneExtPresenter stonePresenter(String color) {
        return new StoneExtPresenter();
    }

}
