package com.github.klee0kai.test.qualifiers.di;

import com.github.klee0kai.stone.annotations.Component;

@Component()
public interface QTestComponent {

    public QInetModule inet();

    public QDataModule data();

}
