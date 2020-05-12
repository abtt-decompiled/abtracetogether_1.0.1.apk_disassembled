package com.worklight.wlclient.cookie;

import com.worklight.common.Logger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import okhttp3.Cookie;

class SetCookieCache implements CookieCache {
    /* access modifiers changed from: private */
    public Set<IdentifiableCookie> cookies = new HashSet();

    private class SetCookieCacheIterator implements Iterator<Cookie> {
        private Iterator<IdentifiableCookie> iterator;

        public SetCookieCacheIterator() {
            this.iterator = SetCookieCache.this.cookies.iterator();
        }

        public boolean hasNext() {
            String str = "hasNext";
            Logger.enter(getClass().getSimpleName(), str);
            Logger.exit(getClass().getSimpleName(), str);
            return this.iterator.hasNext();
        }

        public Cookie next() {
            String str = "next";
            Logger.enter(getClass().getSimpleName(), str);
            Logger.exit(getClass().getSimpleName(), str);
            return ((IdentifiableCookie) this.iterator.next()).getCookie();
        }

        public void remove() {
            String str = "remove";
            Logger.enter(getClass().getSimpleName(), str);
            this.iterator.remove();
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    public void addAll(Collection<Cookie> collection) {
        String str = "addAll";
        Logger.enter(getClass().getSimpleName(), str);
        for (IdentifiableCookie identifiableCookie : IdentifiableCookie.decorateAll(collection)) {
            this.cookies.remove(identifiableCookie);
            this.cookies.add(identifiableCookie);
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void clear() {
        String str = "clear";
        Logger.enter(getClass().getSimpleName(), str);
        this.cookies.clear();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public Iterator<Cookie> iterator() {
        String str = "iterator";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return new SetCookieCacheIterator();
    }
}
