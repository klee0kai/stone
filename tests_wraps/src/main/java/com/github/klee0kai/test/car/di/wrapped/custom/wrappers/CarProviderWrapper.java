package com.github.klee0kai.test.car.di.wrapped.custom.wrappers;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.wrappers.creators.ProviderWrapper;

import javax.inject.Provider;

@WrappersCreator(wrappers = {CarLazy.class, CarProvide.class})
public class CarProviderWrapper implements ProviderWrapper {

    @Override
    public <Wr, T> Wr wrap(Class<Wr> wrapperCl, Provider<T> originalProvider) {
        if (wrapperCl.equals(CarLazy.class)) {
            return (Wr) new CarLazy<T>(originalProvider::get);
        }
        if (wrapperCl.equals(CarProvide.class)) {
            return (Wr) new CarProvide<T>(originalProvider::get);
        }
        return null;
    }

}
