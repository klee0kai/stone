package org.example.di;

import com.klee0kai.stone.annotations.Module;
import com.klee0kai.stone.annotations.Singletone;
import org.example.presenter.*;

@Module
public class PresenterExtModule extends PresenterModule {

    @Override
    @Singletone(cache = Singletone.CacheType.WEAK)
    public UserExtPresenter userPresenter() {
        return new UserExtPresenter();
    }

    @Override
    @Singletone(cache = Singletone.CacheType.WEAK)
    public RobotExtPresenter robotPresenter() {
        return new RobotExtPresenter();
    }

    @Override
    @Singletone(cache = Singletone.CacheType.WEAK)
    public StoneExtPresenter stonePresenter() {
        return new StoneExtPresenter();
    }

}
