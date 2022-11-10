package com.github.klee0kai.test.bindinstance.di;


import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.interfaces.IComponent;

import java.nio.file.attribute.UserPrincipal;

@Component(qualifiers = {String.class, UserPrincipal.class})
public abstract class BindInstanceTextComponent implements IComponent {

    public abstract AppModule appModule();

}
