package com.github.klee0kai.stone.__hidden__.types.holders

import com.github.klee0kai.stone.__hidden__.StoneScope
import com.github.klee0kai.stone.__hidden__.SwitchCacheParam
import com.github.klee0kai.stone.annotations.component.SwitchCache
import com.github.klee0kai.stone.weakref.Ref
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Stone Private class
 */
@Suppress("UNCHECKED_CAST")
class MapItemHolder<Key, T>(
    private val defType: StoneRefType
) {

    private var curRefType: StoneRefType = defType


    private val refMap: HashMap<Key?, Any?> = HashMap()
    private val shedTaskCount = atomic(0)


    fun get(
        key: Key?,
    ): T? {
        val holder = refMap[key] ?: return null
        return when (curRefType) {
            StoneRefType.StrongObject -> holder as T?
            StoneRefType.WeakObject, StoneRefType.SoftObject -> (holder as Ref<T?>?)?.get()
            else -> null
        }
    }

    fun getList(
        key: Key?,
    ): List<T>? {
        val holder = refMap[key] ?: return null
        return when (curRefType) {
            StoneRefType.ListObject -> holder as MutableList<T>?
            StoneRefType.ListWeakObject, StoneRefType.ListSoftObject -> {
                (holder as MutableList<Ref<T?>?>?)?.mapNotNull { it?.get() }
            }

            else -> null
        }
    }

    fun getListNullable(
        key: Key?,
    ): List<T?>? {
        val holder = refMap[key] ?: return null
        return when (curRefType) {
            StoneRefType.ListObject -> holder as MutableList<T>?
            StoneRefType.ListWeakObject, StoneRefType.ListSoftObject -> {
                (holder as MutableList<Ref<T?>?>?)?.map { it?.get() }
            }

            else -> null
        }
    }

    fun set(
        key: Key?,
        onlyIfNull: Boolean,
        creator: Ref<T?>,
    ) {
        val refHolder = refMap[key]
        if (curRefType == StoneRefType.StrongObject) {
            if (onlyIfNull && refHolder != null) return
            refMap[key] = creator.get()
            return
        }
        val formatter: (T?) -> Ref<T?> = curRefType.formatter<T>() ?: return
        if (!onlyIfNull) {
            //switch ref type case
            refMap[key] = formatter(creator.get())
            return
        }

        val ref: Ref<T?>? = refHolder as Ref<T?>?
        if (ref == null || ref.get() == null) {
            refMap[key] = formatter(creator.get())
        }
    }

    fun setList(
        key: Key?,
        onlyIfNull: Boolean,
        creator: Ref<List<T?>?>,
    ) {
        val refHolder = refMap[key]
        if (curRefType == StoneRefType.ListObject) {
            if (!onlyIfNull || refHolder == null) {
                refMap[key] = creator.get()?.toMutableList()
                return
            }

            // init nulls if needed
            var created: List<T?>? = null
            val refList = refHolder as MutableList<T?>
            for (i in refList.indices) {
                if (refList[i] == null) {
                    if (created == null) created = creator.get()
                    refList[i] = created?.get(i)
                }
            }
            return
        }

        val formatter = curRefType.formatter<T>() ?: return
        val refList = refHolder as? MutableList<Ref<T?>?>
        if (!onlyIfNull || refList == null) {
            //switch ref type case
            refMap[key] = creator.get()?.map { formatter(it) }?.toMutableList()
            return
        }

        var created: List<T?>? = null
        for (i in refList.indices) {
            if (refList[i] == null || refList[i]?.get() == null) {
                if (created == null) created = creator.get()
                refList[i] = formatter(created?.get(i))
            }
        }
    }


    fun setRefType(refType: StoneRefType) {
        if (curRefType == refType) return
        if (defType.isList) {
            val listMap: HashMap<Key?, List<T?>?> = HashMap()
            for (key in refMap.keys) listMap[key] = getListNullable(key)
            curRefType = refType.forList()
            for (key in listMap.keys) setList(key, onlyIfNull = false) { listMap[key] }
        } else {
            val itemMap: HashMap<Key?, T?> = HashMap()
            for (key in refMap.keys) itemMap[key] = get(key)
            curRefType = refType.forSingle()
            for (key in itemMap.keys) set(key, onlyIfNull = false) { itemMap[key] }
        }
    }


    fun remove(key: Key?) {
        refMap.remove(key)
    }


    fun reset() {
        curRefType = defType
        refMap.clear()
    }


    fun clearNulls() {
        val keys: Set<Key?> = HashSet(refMap.keys)
        if (!curRefType.isList) {
            for (key in keys) {
                if (get(key) == null) {
                    refMap.remove(key)
                }
            }
        } else {
            for (key in keys) {
                val list = getListNullable(key)
                if (list?.none { it != null } == true) {
                    refMap.remove(key)
                }
            }
        }
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

        if (args.time > 0) {
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
