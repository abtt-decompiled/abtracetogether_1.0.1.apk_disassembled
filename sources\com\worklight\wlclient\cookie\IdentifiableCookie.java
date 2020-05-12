package com.worklight.wlclient.cookie;

import com.worklight.common.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import okhttp3.Cookie;

class IdentifiableCookie {
    private Cookie cookie;

    static List<IdentifiableCookie> decorateAll(Collection<Cookie> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (Cookie identifiableCookie : collection) {
            arrayList.add(new IdentifiableCookie(identifiableCookie));
        }
        return arrayList;
    }

    IdentifiableCookie(Cookie cookie2) {
        this.cookie = cookie2;
    }

    /* access modifiers changed from: 0000 */
    public Cookie getCookie() {
        return this.cookie;
    }

    public boolean equals(Object obj) {
        String str = "equals";
        Logger.enter(getClass().getSimpleName(), str);
        boolean z = false;
        if (!(obj instanceof IdentifiableCookie)) {
            Logger.exit(getClass().getSimpleName(), str);
            return false;
        }
        IdentifiableCookie identifiableCookie = (IdentifiableCookie) obj;
        if (identifiableCookie.cookie.name().equals(this.cookie.name()) && identifiableCookie.cookie.domain().equals(this.cookie.domain()) && identifiableCookie.cookie.path().equals(this.cookie.path()) && identifiableCookie.cookie.secure() == this.cookie.secure() && identifiableCookie.cookie.hostOnly() == this.cookie.hostOnly()) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        String str = "hashCode";
        Logger.enter(getClass().getSimpleName(), str);
        int hashCode = ((((((((527 + this.cookie.name().hashCode()) * 31) + this.cookie.domain().hashCode()) * 31) + this.cookie.path().hashCode()) * 31) + (this.cookie.secure() ^ true ? 1 : 0)) * 31) + (this.cookie.hostOnly() ^ true ? 1 : 0);
        Logger.exit(getClass().getSimpleName(), str);
        return hashCode;
    }
}
