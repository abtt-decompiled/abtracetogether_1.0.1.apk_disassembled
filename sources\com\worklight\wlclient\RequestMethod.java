package com.worklight.wlclient;

import com.worklight.common.Logger;

public enum RequestMethod {
    GET(r2),
    POST(r3),
    PUT(r4),
    DELETE(r5),
    HEAD(r6),
    OPTIONS(r7),
    TRACE(r8);
    
    private String name;

    private RequestMethod(String str) {
        this.name = str;
    }

    public String toString() {
        String str = "toString";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.name;
    }

    public static RequestMethod fromSring(String str) {
        RequestMethod[] values;
        for (RequestMethod requestMethod : values()) {
            if (str.equalsIgnoreCase(requestMethod.name)) {
                return requestMethod;
            }
        }
        return null;
    }
}
