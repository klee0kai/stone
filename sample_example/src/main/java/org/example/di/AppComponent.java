package org.example.di;

import com.klee0kai.stone.annotations.Component;

@Component
public interface AppComponent {

    DomainModule domain();

    PresenterModule presenter();

}
