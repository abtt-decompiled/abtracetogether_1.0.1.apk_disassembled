package com.google.android.material.internal;

import android.os.Build;

public class ManufacturerUtils {
    private ManufacturerUtils() {
    }

    public static boolean isMeizuDevice() {
        return Build.MANUFACTURER.equalsIgnoreCase("meizu");
    }

    public static boolean isLGDevice() {
        return Build.MANUFACTURER.equalsIgnoreCase("lg");
    }
}
