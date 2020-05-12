package com.worklight.wlclient.api;

import com.worklight.common.Logger;
import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class WLProcedureInvocationResponse extends WLProcedureInvocationResult {
    @Deprecated
    public WLProcedureInvocationResponse(WLResponse wLResponse) {
        super(wLResponse);
    }

    @Deprecated
    public JSONObject getJSONResult() throws JSONException {
        String str = "getJSONResult";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return super.getResult();
    }
}
