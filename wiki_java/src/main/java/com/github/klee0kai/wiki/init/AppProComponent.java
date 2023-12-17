package com.github.klee0kai.wiki.init;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.ExtendOf;

@Component
public interface AppProComponent extends AppComponent {

    @Override
    ProFeatureModule feature();

    @ExtendOf
    void extendComponent(AppComponent parent);

}
