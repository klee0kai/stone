package org.example.di;

import com.klee0kai.stone.annotations.Component;
import com.klee0kai.stone.interfaces.IComponent;

@Component
public interface AppComponent extends IComponent {

    DomainModule domain();

    PresenterModule presenter();

}
