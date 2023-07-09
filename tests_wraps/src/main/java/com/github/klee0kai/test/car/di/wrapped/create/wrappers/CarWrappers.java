package com.github.klee0kai.test.car.di.wrapped.create.wrappers;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.types.wrappers.Wrapper;

import javax.inject.Provider;

@WrappersCreator(wrappers = {CarLazy.class})
public class CarWrappers implements Wrapper {

    @Override
    public <Wr, T> Wr wrap(Class<Wr> wrapperCl, Provider<T> originalProvider) {
        if (wrapperCl.equals(CarLazy.class)) {
            return (Wr) new CarLazy<T>(originalProvider::get);
        }
        return null;
    }

    @Override
    public <Wr, T> T unwrap(Wr wrapped) {
        if (wrapped instanceof CarLazy) {
            return ((CarLazy<T>) wrapped).getValue();
        }
        return null;
    }

}
