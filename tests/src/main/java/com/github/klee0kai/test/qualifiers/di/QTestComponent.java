package com.github.klee0kai.test.qualifiers.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.qualifiers.QApp;
import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType;
import com.github.klee0kai.test.qualifiers.di.qualifiers.Token;
import com.github.klee0kai.test.qualifiers.di.qualifiers.UserId;

@Component(
        qualifiers = {ProductType.class, Token.class, UserId.class}
)
public interface QTestComponent {

    public QInetModule inet();

    public QDataModule data();

    void inject(QApp qApp);

    void inject(QApp qApp, ProductType debug);

    void inject(ProductType demo, UserId demo_user_id, Token demo_token,QApp qApp);

    void inject(QApp qApp, Token release_token, ProductType release);
}
