package com.worklight.wlclient.cookie;

import android.content.Context;
import com.worklight.wlclient.HttpClientManager;

public class WLPersistentCookie {
    public static ClearableCookieJar generate(Context context) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

    public static ClearableCookieJar getCookies() {
        return (ClearableCookieJar) HttpClientManager.getInstance().getOkHttpClient().cookieJar();
    }
}
