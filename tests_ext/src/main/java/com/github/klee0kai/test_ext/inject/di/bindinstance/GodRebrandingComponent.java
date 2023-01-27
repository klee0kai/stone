package com.github.klee0kai.test_ext.inject.di.bindinstance;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.di.bindinstance.simple.GodWorkspaceComponent;

@Component
public interface GodRebrandingComponent extends GodWorkspaceComponent {

    @Override
    SunSystemRebrandingModule sunSystem();
}
