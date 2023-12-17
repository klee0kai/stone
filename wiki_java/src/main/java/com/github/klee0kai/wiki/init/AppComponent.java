package com.github.klee0kai.wiki.init;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.Init;

@Component
public interface AppComponent {

    FeatureModule feature();

    StarsDependencies starsDependencies();

    StarsDependencies2 starsDependencies2();

    @Init
    void initFeatureModule(FeatureModule featureModule);

}
