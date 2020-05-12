package com.worklight.wlclient;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;

public interface WLRequestPiggybacker {
    void onFailure(WLFailResponse wLFailResponse);

    void onSuccess(WLResponse wLResponse);

    void processOptions(String str, WLRequestOptions wLRequestOptions);
}
