package org.example.di;

import com.klee0kai.stone.annotations.Item;
import com.klee0kai.stone.annotations.Module;
import org.example.presenter.RobotPresenter;
import org.example.presenter.StonePresenter;
import org.example.presenter.UserPresenter;

@Module
public class PresenterModule {

    @Item(cache = Item.CacheType.WEAK)
    public UserPresenter userPresenter() {
        return new UserPresenter();
    }

    @Item(cache = Item.CacheType.WEAK)
    public RobotPresenter robotPresenter() {
        return new RobotPresenter();
    }

    @Item(cache = Item.CacheType.WEAK)
    public StonePresenter stonePresenter() {
        return new StonePresenter();
    }


}
