package com.github.klee0kai.test.bindinstance.di;


import com.github.klee0kai.stone.annotations.Component;
import com.github.klee0kai.stone.interfaces.IComponent;

@Component
public abstract class BindInstanceTextComponent implements IComponent {

    public abstract AppModule appModule();

}
