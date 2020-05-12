package com.worklight.wlclient.api;

public interface WLLoginResponseListener {
    void onFailure(WLFailResponse wLFailResponse);

    void onSuccess();
}
