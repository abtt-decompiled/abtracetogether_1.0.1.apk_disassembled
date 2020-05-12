package com.worklight.wlclient.api;

public interface WLLogoutResponseListener {
    void onFailure(WLFailResponse wLFailResponse);

    void onSuccess();
}
