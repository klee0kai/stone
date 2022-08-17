package org.example.di;

import com.klee0kai.stone.annotations.Item;
import com.klee0kai.stone.annotations.Module;
import org.example.presenter.RobotPresenter;
import org.example.presenter.StonePresenter;
import org.example.presenter.UserPresenter;

@Module
public class PresenterModule {

    @Item
    public UserPresenter userPresenter() {
        return new UserPresenter();
    }

    @Item
    public RobotPresenter robotPresenter() {
        return new RobotPresenter();
    }

    @Item
    public StonePresenter stonePresenter() {
        return new StonePresenter();
    }


}
