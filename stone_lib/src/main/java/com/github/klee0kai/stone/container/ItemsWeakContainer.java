package com.github.klee0kai.stone.container;

import com.github.klee0kai.stone.annotations.Provide;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;

public class ItemsWeakContainer {

    private static final int CLEAN_COUNT = 100;

    private static final HashMap<String, WeakReference<Object>> weakItems = new HashMap<>();
    private static final HashMap<String, Reference<Object>> softItems = new HashMap<>();
    private static final HashMap<String, Object> strongItems = new HashMap<>();


    public static synchronized <T> T putRef(String key, Provide.CacheType cacheType, T a) {
        switch (cacheType) {
            case WEAK:
                weakItems.put(key, new WeakReference<>(a));
                return a;
            case SOFT:
                softItems.put(key, new SoftReference<>(a));
                return a;
            case STRONG:
                strongItems.put(key, a);
                return a;
            default:
                return null;
        }
    }

    public static synchronized void remove(String key) {
        weakItems.remove(key);
        softItems.remove(key);
        strongItems.remove(key);
    }

    public static synchronized void removeScope(String scope) {
        scope = scope + ".";
        for (Map<String, ? extends Object> map : Arrays.asList(weakItems, softItems, strongItems)) {
            List<String> rmKeys = new LinkedList<>();
            for (String key : map.keySet()) {
                if (key.startsWith(scope))
                    rmKeys.add(key);
            }
            for (String rmKey : rmKeys)
                map.remove(rmKey);
        }
    }


    /**
     * run garbage collector for container
     *
     * @param includeSoftRefs true - also force clean useless soft references
     */
    public static synchronized void gc(boolean includeSoftRefs) {
        if (includeSoftRefs) {
            for (String key : softItems.keySet()) {
                Reference ref = softItems.get(key);
                if (ref != null)
                    softItems.put(key, new WeakReference<>(ref.get()));
            }
        }
        System.gc();
        System.gc();
        clearWeakLinksContainer();
        clearSoftLinksContainer();
        if (includeSoftRefs) {
            for (String key : softItems.keySet()) {
                Reference ref = softItems.get(key);
                if (ref != null)
                    softItems.put(key, new SoftReference<>(ref.get()));
            }
        }

    }

    public static synchronized <T> T get(String a) {
        Object o = weakItems.get(a) != null ? weakItems.get(a).get() : null;
        if (o == null) {
            weakItems.remove(a);
            o = softItems.get(a) != null ? softItems.get(a).get() : null;
        }
        if (o == null) {
            softItems.remove(a);
            o = strongItems.get(a);
        }
        clearWeakLinksContainer();
        clearSoftLinksContainer();
        return o != null ? (T) o : null;
    }

    private static synchronized void clearWeakLinksContainer() {
        if (weakItems.size() < CLEAN_COUNT) return;
        for (String key : weakItems.keySet()) {
            if (weakItems.get(key) == null || weakItems.get(key).get() == null)
                weakItems.remove(key);
        }
    }

    private static synchronized void clearSoftLinksContainer() {
        if (softItems.size() < CLEAN_COUNT) return;
        for (String key : softItems.keySet()) {
            if (softItems.get(key) == null || softItems.get(key).get() == null)
                softItems.remove(key);
        }
    }

    /**
     * Use only tests
     */
    private static synchronized void clear() {
        weakItems.clear();
        softItems.clear();
        strongItems.clear();
    }

}
