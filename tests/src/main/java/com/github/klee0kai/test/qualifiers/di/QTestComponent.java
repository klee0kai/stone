package com.github.klee0kai.test.qualifiers.di;

import com.github.klee0kai.stone.annotations.component.Component;

@Component()
public interface QTestComponent {

    public QInetModule inet();

    public QDataModule data();

}
