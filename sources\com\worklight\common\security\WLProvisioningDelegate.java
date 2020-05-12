package com.worklight.common.security;

import android.content.Context;

public interface WLProvisioningDelegate {
    void sendCSR(String str, Context context);
}
