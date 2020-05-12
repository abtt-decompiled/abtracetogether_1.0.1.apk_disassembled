package com.worklight.wlclient;

import com.worklight.wlclient.api.WLAuthorizationException;

public interface WLClientInstanceIdListener {
    void onFailure(WLAuthorizationException wLAuthorizationException);

    void onSuccess(String str);
}
