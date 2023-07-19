package com.github.klee0kai.test.car.di.wrapped.custom.wrappers;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.types.wrappers.Wrapper;

@WrappersCreator(wrappers = {CarRef.class})
public class CarWrapper implements Wrapper {

    @Override
    public <Wr, T> Wr wrap(Class<Wr> wrapperCl, T original) {
        if (wrapperCl.equals(CarRef.class)) {
            return (Wr) new CarRef<>(original);

        }
        return null;
    }
}
