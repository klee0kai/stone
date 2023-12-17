package com.github.klee0kai.wiki.wrapping;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.wrappers.creators.ProviderWrapper;

import javax.inject.Provider;

@WrappersCreator(wrappers = {CustomLazy.class})
public class CustomWrapper implements ProviderWrapper {

    @Override
    public <Wr, T> Wr wrap(Class<Wr> wrapperCl, Provider<T> originalProvider) {
        if (wrapperCl.equals(CustomLazy.class)) {
            return (Wr) new CustomLazy<T>(originalProvider::get);
        }
        return null;
    }

}
