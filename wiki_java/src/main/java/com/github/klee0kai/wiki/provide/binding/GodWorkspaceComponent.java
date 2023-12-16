package com.github.klee0kai.wiki.provide.binding;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Component
public interface GodWorkspaceComponent {

    SunSystemModule sunSystem();

    Sun sun();

}