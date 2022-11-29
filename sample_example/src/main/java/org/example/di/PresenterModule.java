package org.example.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import org.example.presenter.StonePresenter;

@Module
public class PresenterModule {


    @Provide(cache = Provide.CacheType.Weak)
    public StonePresenter stonePresenter() {
        return new StonePresenter();
    }

}
