package com.worklight.wlclient;

import okhttp3.Response;

public interface WLHybridHttpListener {
    void onException(Exception exc);

    void onResponse(Response response);
}
