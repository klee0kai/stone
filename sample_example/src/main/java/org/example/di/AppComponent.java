package org.example.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.interfaces.IComponent;

@Component
public interface AppComponent extends IComponent {

    AppModule app();

    DataModule data();

    DomainModule domain();

    PresenterModule presenter();

}
