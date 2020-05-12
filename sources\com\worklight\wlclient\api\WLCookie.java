package com.worklight.wlclient.api;

import com.worklight.common.Logger;
import java.util.Date;
import java.util.Map;

public class WLCookie {
    static final String DOMAIN = "DOMAIN";
    static final String EXPIRY_DATE = "EXPIRY_DATE";
    static final String NAME = "NAME";
    static final String PATH = "PATH";
    static final String SECURE = "SECURE";
    static final String VALUE = "VALUE";
    private Map<String, String> cookies;
    private Date expirationDate;

    WLCookie(Map<String, String> map) {
        this.cookies = map;
    }

    /* access modifiers changed from: 0000 */
    public void setExpirationDate(Date date) {
        this.expirationDate = date;
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public String getName() {
        String str = "getName";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return (String) this.cookies.get(NAME);
    }

    public String getPath() {
        String str = "getPath";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return (String) this.cookies.get(PATH);
    }

    public String getValue() {
        String str = "getValue";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return (String) this.cookies.get(VALUE);
    }

    public String getDomain() {
        String str = "getDomain";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return (String) this.cookies.get(DOMAIN);
    }

    public boolean isSecure() {
        String str = "isSecure";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return Boolean.valueOf((String) this.cookies.get(SECURE)).booleanValue();
    }

    public Date getExpiryDate() {
        String str = "getExpiryDate";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.expirationDate;
    }
}
