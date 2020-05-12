package com.worklight.wlclient.api;

public interface WLResponseListener {
    void onFailure(WLFailResponse wLFailResponse);

    void onSuccess(WLResponse wLResponse);
}
