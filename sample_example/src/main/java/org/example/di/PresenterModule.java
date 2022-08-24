package org.example.di;

import com.github.klee0kai.stone.annotations.Singleton;
import com.github.klee0kai.stone.annotations.Module;
import org.example.presenter.RobotPresenter;
import org.example.presenter.StonePresenter;
import org.example.presenter.UserPresenter;

@Module
public class PresenterModule {

    @Singleton(cache = Singleton.CacheType.WEAK)
    public UserPresenter userPresenter() {
        return new UserPresenter();
    }

    @Singleton(cache = Singleton.CacheType.WEAK)
    public RobotPresenter robotPresenter() {
        return new RobotPresenter();
    }

    @Singleton(cache = Singleton.CacheType.WEAK)
    public StonePresenter stonePresenter(String color) {
        return new StonePresenter();
    }


}
