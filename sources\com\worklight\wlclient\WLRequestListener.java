package com.worklight.wlclient;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponse;

public interface WLRequestListener {
    void onFailure(WLFailResponse wLFailResponse);

    void onSuccess(WLResponse wLResponse);
}
