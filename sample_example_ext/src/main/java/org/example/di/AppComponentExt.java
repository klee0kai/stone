package org.example.di;

import com.github.klee0kai.stone.annotations.Component;

@Component
public interface AppComponentExt extends AppComponent {

    @Override
    DomainExtModule domain();

    @Override
    PresenterExtModule presenter();


}
