package com.github.klee0kai.stone.__hidden__.types.holders

import com.github.klee0kai.stone.__hidden__.StoneScope
import com.github.klee0kai.stone.__hidden__.SwitchCacheParam
import com.github.klee0kai.stone.annotations.component.SwitchCache
import com.github.klee0kai.stone.weakref.Ref
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * Stone Private class
 */
@Suppress("UNCHECKED_CAST")
class SingleItemHolder<T>(
    private val defType: StoneRefType
) {
    private var curRefType: StoneRefType = defType

    private var refHolder: Any? = null
    private val shedTaskCount = atomic(0)


    fun get(): T? = when (curRefType) {
        StoneRefType.StrongObject -> refHolder as T?
        StoneRefType.WeakObject, StoneRefType.SoftObject -> (refHolder as Ref<T?>?)?.get()
        else -> null
    }

    fun getList(): List<T?>? = when (curRefType) {
        StoneRefType.ListObject -> refHolder as MutableList<T>?
        StoneRefType.ListWeakObject, StoneRefType.ListSoftObject -> (refHolder as MutableList<Ref<T?>?>?)?.map { it?.get() }
        else -> null
    }


    fun set(
        creator: Ref<T?>,
        onlyIfNull: Boolean,
    ) {
        if (curRefType == StoneRefType.StrongObject) {
            if (refHolder != null && onlyIfNull) return
            refHolder = creator.get()
            return
        }
        val formatter: (T?) -> Ref<T?> = curRefType.formatter<T>() ?: return
        if (!onlyIfNull) {
            //switch ref type case
            refHolder = formatter(creator.get())
            return
        }

        val ref: Ref<T?>? = refHolder as Ref<T?>?
        if (ref == null || ref.get() == null) {
            refHolder = formatter(creator.get())
        }
    }

    fun setList(
        creator: Ref<List<T?>?>,
        onlyIfNull: Boolean,
    ) {
        if (curRefType == StoneRefType.ListObject) {
            if (!onlyIfNull || refHolder == null) {
                refHolder = creator.get()?.toMutableList()
                return
            }

            // init nulls if needed
            var created: List<T?>? = null
            val list = refHolder as MutableList<T?>
            for (i in list.indices) {
                if (list[i] == null) {
                    if (created == null) created = creator.get()
                    list[i] = created?.get(i)
                }
            }
            return
        }

        val formatter = curRefType.formatter<T>() ?: return
        val refList = refHolder as? MutableList<Ref<T?>?>?
        if (!onlyIfNull || refList == null) {
            refHolder = creator.get()?.map { formatter(it) }?.toMutableList()
            return
        }

        // init nulls if needed
        var created: List<T?>? = null
        for (i in refList.indices) {
            if (refList[i] == null || refList[i]?.get() == null) {
                if (created == null) created = creator.get()
                refList[i] = formatter(created?.get(i))
            }
        }
    }

    fun setRefType(
        refType: StoneRefType,
    ) {
        if (curRefType == refType) return
        if (defType.isList) {
            val ob = this.getList()
            curRefType = refType.forList()
            setList(creator = Ref { ob }, false)
        } else {
            val ob = get()
            curRefType = refType.forSingle()
            set(creator = Ref { ob }, false)
        }
    }


    fun reset() {
        curRefType = defType
        refHolder = null
    }


    fun switchCache(args: SwitchCacheParam) {
        when (args.cache) {
            SwitchCache.CacheType.Default -> {
                setRefType(defType)
                return
            }

            SwitchCache.CacheType.Reset -> {
                reset()
                return
            }

            SwitchCache.CacheType.Weak -> setRefType(StoneRefType.WeakObject)
            SwitchCache.CacheType.Soft -> setRefType(StoneRefType.SoftObject)
            SwitchCache.CacheType.Strong -> setRefType(StoneRefType.StrongObject)
        }

        if (args.time != Duration.INFINITE) {
            shedTaskCount.incrementAndGet()
            StoneScope.stoneCoroutineScope.launch {
                delay(args.time)
                if (shedTaskCount.decrementAndGet() <= 0) {
                    setRefType(defType)
                }
            }
        }
    }
}
