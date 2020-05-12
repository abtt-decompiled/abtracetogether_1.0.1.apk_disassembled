package com.worklight.wlclient.api;

import com.worklight.wlclient.auth.AccessToken;

public interface WLAccessTokenListener {
    void onFailure(WLFailResponse wLFailResponse);

    void onSuccess(AccessToken accessToken);
}
