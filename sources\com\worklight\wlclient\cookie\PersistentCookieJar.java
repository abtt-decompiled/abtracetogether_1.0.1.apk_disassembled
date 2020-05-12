package com.worklight.wlclient.cookie;

import com.worklight.common.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

class PersistentCookieJar implements ClearableCookieJar {
    private CookieCache cache;
    private CookiePersistor persistor;

    public PersistentCookieJar(CookieCache cookieCache, CookiePersistor cookiePersistor) {
        this.cache = cookieCache;
        this.persistor = cookiePersistor;
        cookieCache.addAll(cookiePersistor.loadAll());
    }

    public synchronized void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        Logger.enter(getClass().getSimpleName(), "saveFromResponse");
        this.cache.addAll(list);
        this.persistor.saveAll(filterPersistentCookies(list));
        Logger.exit(getClass().getSimpleName(), "saveFromResponse");
    }

    private static List<Cookie> filterPersistentCookies(List<Cookie> list) {
        ArrayList arrayList = new ArrayList();
        for (Cookie cookie : list) {
            if (cookie.persistent()) {
                arrayList.add(cookie);
            }
        }
        return arrayList;
    }

    public synchronized List<Cookie> loadForRequest(HttpUrl httpUrl) {
        ArrayList arrayList;
        Logger.enter(getClass().getSimpleName(), "loadForRequest");
        ArrayList arrayList2 = new ArrayList();
        arrayList = new ArrayList();
        Iterator it = this.cache.iterator();
        while (it.hasNext()) {
            Cookie cookie = (Cookie) it.next();
            if (isCookieExpired(cookie)) {
                arrayList2.add(cookie);
                it.remove();
            } else if (cookie.matches(httpUrl)) {
                arrayList.add(cookie);
            }
        }
        this.persistor.removeAll(arrayList2);
        Logger.exit(getClass().getSimpleName(), "loadForRequest");
        return arrayList;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    public synchronized void clearSession() {
        Logger.enter(getClass().getSimpleName(), "clearSession");
        this.cache.clear();
        this.cache.addAll(this.persistor.loadAll());
        Logger.exit(getClass().getSimpleName(), "clearSession");
    }

    public synchronized void clear() {
        Logger.enter(getClass().getSimpleName(), "clear");
        this.cache.clear();
        this.persistor.clear();
        Logger.exit(getClass().getSimpleName(), "clear");
    }
}
