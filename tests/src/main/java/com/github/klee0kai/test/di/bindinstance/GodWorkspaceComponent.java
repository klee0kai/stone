package com.github.klee0kai.test.di.bindinstance;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.interfaces.IComponent;

@Component
public interface GodWorkspaceComponent extends IComponent {

    SunSystemModule sunSystem();

}
