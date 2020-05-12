package com.worklight.wlclient.cookie;

import java.util.Collection;
import okhttp3.Cookie;

interface CookieCache extends Iterable<Cookie> {
    void addAll(Collection<Cookie> collection);

    void clear();
}
