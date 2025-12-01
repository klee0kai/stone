package com.github.klee0kai.stone.__hidden__.provide

class ProvideBuilder<T>(
    private val provideBody: ProvideBody<T>
) {

    interface ProvideBody<T> {
        fun provide(consumer: ProvideConsumer<T>)
    }

    fun first(): T? {
        val consumer = ProvideConsumer<T>()
        provideBody.provide(consumer)
        return consumer.first
    }

    fun all(): List<T> {
        val consumer = ProvideConsumer<T>()
        provideBody.provide(consumer)
        return consumer.list
    }

}
