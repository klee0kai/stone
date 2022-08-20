package org.example.di;

import com.github.klee0kai.stone.annotations.Component;
import com.github.klee0kai.stone.interfaces.IComponent;

@Component
public interface AppComponent extends IComponent {

    DomainModule domain();

    PresenterModule presenter();

}
