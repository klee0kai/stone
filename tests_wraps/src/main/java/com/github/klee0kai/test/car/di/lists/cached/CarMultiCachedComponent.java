package com.github.klee0kai.test.car.di.lists.cached;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.wrappers.Ref;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.List;

@Component
public interface CarMultiCachedComponent {

    CarMultiCachedModule cachedModule();

    Ref<Bumper> singleBumper();

    List<Provider<WeakReference<Wheel>>> wheels();

    Wheel wheel();

    List<List<Window>> windows();

    List<Provider<List<Window>>> windowsProviding();

    List<Car> cars();

}
