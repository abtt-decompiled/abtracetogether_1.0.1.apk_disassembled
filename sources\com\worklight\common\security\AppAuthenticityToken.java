package com.worklight.common.security;

import android.content.Context;

public class AppAuthenticityToken {
    public native String a1(Context context, String str);

    public AppAuthenticityToken() {
        System.loadLibrary("authjni");
    }
}
