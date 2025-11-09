package stone.wrappers.creators

import javax.inject.Provider

interface ProviderWrapper {
    /**
     * Provide wrapped object.
     *
     * @param wrapperCl        type of wrapper
     * @param originalProvider original object provider
     * @param <Wr>             type of wrapper
     * @param <T>              type of providing original object
     * @return wrapped object provider
    </T></Wr> */
    fun <Wr, T> wrap(wrapperCl: Class<Wr?>?, originalProvider: Provider<T?>?): Wr?
}
