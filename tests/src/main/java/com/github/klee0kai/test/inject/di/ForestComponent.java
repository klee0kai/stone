package com.github.klee0kai.test.inject.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.inject.Horse;
import com.github.klee0kai.test.inject.Mowgli;
import com.github.klee0kai.test.inject.Snake;

@Component
public interface ForestComponent {

    UnitedModule united();

    IdentityModule identity();

    void inject(Horse horse);

    void inject(Mowgli mowgli);

    void inject(Snake snake);
}
