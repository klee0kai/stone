package org.example.di;

import com.github.klee0kai.stone.annotations.Singletone;
import com.github.klee0kai.stone.annotations.Module;
import org.example.presenter.RobotPresenter;
import org.example.presenter.StonePresenter;
import org.example.presenter.UserPresenter;

@Module
public class PresenterModule {

    @Singletone(cache = Singletone.CacheType.WEAK)
    public UserPresenter userPresenter() {
        return new UserPresenter();
    }

    @Singletone(cache = Singletone.CacheType.WEAK)
    public RobotPresenter robotPresenter() {
        return new RobotPresenter();
    }

    @Singletone(cache = Singletone.CacheType.WEAK)
    public StonePresenter stonePresenter(String color) {
        return new StonePresenter();
    }


}
