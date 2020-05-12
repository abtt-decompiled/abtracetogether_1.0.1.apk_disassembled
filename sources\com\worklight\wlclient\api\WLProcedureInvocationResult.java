package com.worklight.wlclient.api;

import com.worklight.common.Logger;
import com.worklight.nativeandroid.common.WLUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WLProcedureInvocationResult extends WLResponse {
    private static final String JSON_KEY_IS_SUCCESSFUL = "isSuccessful";
    private JSONObject jsonResult;

    WLProcedureInvocationResult(WLResponse wLResponse) {
        super(wLResponse);
    }

    public boolean isSuccessful() {
        boolean z;
        String simpleName = getClass().getSimpleName();
        String str = JSON_KEY_IS_SUCCESSFUL;
        Logger.enter(simpleName, str);
        try {
            z = getResult().getBoolean(str);
        } catch (JSONException unused) {
            z = false;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return z;
    }

    public JSONObject getResult() throws JSONException {
        String str = "getResult";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.jsonResult == null) {
            this.jsonResult = WLUtils.convertStringToJSON(getResponseText());
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.jsonResult;
    }

    public String toString() {
        String str = "toString";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append("WLProcedureInvocationResult [isSuccessful=");
        sb.append(isSuccessful());
        sb.append(", result=");
        sb.append(this.jsonResult);
        sb.append("]");
        return sb.toString();
    }
}
