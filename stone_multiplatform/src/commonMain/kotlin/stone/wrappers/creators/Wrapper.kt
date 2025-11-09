package stone.wrappers.creators

interface Wrapper {
    /**
     * Provide wrapped object.
     *
     * @param wrapperCl type of wrapper
     * @param original  original object
     * @param <Wr>      type of wrapper
     * @param <T>       type of providing original object
     * @return wrapped object provider
    </T></Wr> */
    fun <Wr, T> wrap(wrapperCl: Class<Wr?>?, original: T?): Wr?
}
