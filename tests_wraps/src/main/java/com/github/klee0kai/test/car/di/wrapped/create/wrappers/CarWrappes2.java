package com.github.klee0kai.test.car.di.wrapped.create.wrappers;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.types.wrappers.Wrapper;

import javax.inject.Provider;

@WrappersCreator(wrappers = {CarProvide.class})
public class CarWrappes2 implements Wrapper {

    @Override
    public <Wr, T> Wr wrap(Class<Wr> wrapperCl, Provider<T> originalProvider) {
        if (wrapperCl.equals(CarProvide.class)) {
            return (Wr) new CarProvide<T>(originalProvider::get);
        }
        return null;
    }

    @Override
    public <Wr, T> T unwrap(Wr wrapped) {
        if (wrapped instanceof CarProvide) {
            return ((CarProvide<T>) wrapped).getValue();
        }
        return null;
    }

}
