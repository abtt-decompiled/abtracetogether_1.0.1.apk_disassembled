package com.worklight.wlclient;

import android.content.Context;
import com.worklight.common.WLConfig;
import com.worklight.wlclient.api.WLRequestOptions;

public class WLClientInstanceRegistrationRequest extends WLRequest {
    public WLClientInstanceRegistrationRequest(WLRequestListener wLRequestListener, WLRequestOptions wLRequestOptions, WLConfig wLConfig, Context context) {
        super(wLRequestListener, wLRequestOptions, wLConfig, context);
    }
}
