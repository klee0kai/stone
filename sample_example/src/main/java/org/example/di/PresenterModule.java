package org.example.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import org.example.presenter.StonePresenter;

@Module
public class PresenterModule {


    @Provide(cache = Provide.CacheType.WEAK)
    public StonePresenter stonePresenter() {
        return new StonePresenter();
    }

}
