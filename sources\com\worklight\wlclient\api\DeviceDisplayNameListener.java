package com.worklight.wlclient.api;

public interface DeviceDisplayNameListener {
    void onFailure(WLFailResponse wLFailResponse);

    void onSuccess(String str);
}
