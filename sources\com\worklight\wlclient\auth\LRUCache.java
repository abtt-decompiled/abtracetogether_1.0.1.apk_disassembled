package com.worklight.wlclient.auth;

import com.worklight.common.Logger;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private int cacheSize;

    protected LRUCache(int i) {
        super(16, 1.0f, true);
        this.cacheSize = i;
    }

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Entry<K, V> entry) {
        String str = "removeEldestEntry";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return size() >= this.cacheSize;
    }
}
