package com.worklight.wlclient.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.worklight.common.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import okhttp3.Cookie;

class SharedPrefsCookiePersistor implements CookiePersistor {
    private final SharedPreferences sharedPreferences;

    public SharedPrefsCookiePersistor(Context context) {
        this(context.getSharedPreferences("CookiePersistence", 0));
    }

    public SharedPrefsCookiePersistor(SharedPreferences sharedPreferences2) {
        this.sharedPreferences = sharedPreferences2;
    }

    public List<Cookie> loadAll() {
        String str = "loadAll";
        Logger.enter(getClass().getSimpleName(), str);
        ArrayList arrayList = new ArrayList(this.sharedPreferences.getAll().size());
        for (Entry value : this.sharedPreferences.getAll().entrySet()) {
            Cookie decode = new SerializableCookie().decode((String) value.getValue());
            if (decode != null) {
                arrayList.add(decode);
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return arrayList;
    }

    public void saveAll(Collection<Cookie> collection) {
        String str = "saveAll";
        Logger.enter(getClass().getSimpleName(), str);
        Editor edit = this.sharedPreferences.edit();
        for (Cookie cookie : collection) {
            edit.putString(createCookieKey(cookie), new SerializableCookie().encode(cookie));
        }
        edit.commit();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void removeAll(Collection<Cookie> collection) {
        String str = "removeAll";
        Logger.enter(getClass().getSimpleName(), str);
        Editor edit = this.sharedPreferences.edit();
        for (Cookie createCookieKey : collection) {
            edit.remove(createCookieKey(createCookieKey));
        }
        edit.commit();
        Logger.exit(getClass().getSimpleName(), str);
    }

    private static String createCookieKey(Cookie cookie) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.secure() ? "https" : "http");
        sb.append("://");
        sb.append(cookie.domain());
        sb.append(cookie.path());
        sb.append("|");
        sb.append(cookie.name());
        return sb.toString();
    }

    public void clear() {
        String str = "clear";
        Logger.enter(getClass().getSimpleName(), str);
        this.sharedPreferences.edit().clear().commit();
        Logger.exit(getClass().getSimpleName(), str);
    }
}
