package org.example.di;

import com.github.klee0kai.stone.annotations.Provide;
import com.github.klee0kai.stone.annotations.Module;
import org.example.presenter.RobotPresenter;
import org.example.presenter.StonePresenter;
import org.example.presenter.UserPresenter;

@Module
public class PresenterModule {

    @Provide(cache = Provide.CacheType.WEAK)
    public UserPresenter userPresenter() {
        return new UserPresenter();
    }

    @Provide(cache = Provide.CacheType.WEAK)
    public RobotPresenter robotPresenter() {
        return new RobotPresenter();
    }

    @Provide(cache = Provide.CacheType.WEAK)
    public StonePresenter stonePresenter(String color) {
        return new StonePresenter();
    }


}
