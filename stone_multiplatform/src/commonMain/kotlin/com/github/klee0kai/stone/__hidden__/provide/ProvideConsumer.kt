package com.github.klee0kai.stone.__hidden__.provide

class ProvideConsumer<T> {
    private val _list = mutableListOf<T>()

    val list get() = _list.toList()

    val first: T? get() = _list.firstOrNull()

    fun addAll(
        collection: Collection<T?>
    ) {
        _list.addAll(collection.filterNotNull())
    }

    fun add(element: T?) {
        if (element != null) {
            _list.add(element)
        }
    }

}
