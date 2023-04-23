package com.github.klee0kai.test.di.house.simple;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.house.kitchen.storagearea.Cookware;

@Module
public interface ToolsModule {

    Cookware cookware();

}
