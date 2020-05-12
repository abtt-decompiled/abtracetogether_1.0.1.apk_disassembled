package com.worklight.wlclient.api;

import com.worklight.common.Logger;

public class WLAuthorizationException extends Exception {
    private static final long serialVersionUID = 2292528487037814844L;
    private WLFailResponse response;

    public WLAuthorizationException(WLFailResponse wLFailResponse) {
        super(wLFailResponse.errorMsg);
        this.response = wLFailResponse;
    }

    public WLFailResponse getWLFailResponse() {
        String str = "getWLFailResponse";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.response;
    }
}
