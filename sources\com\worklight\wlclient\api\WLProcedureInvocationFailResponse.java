package com.worklight.wlclient.api;

import com.worklight.common.Logger;
import com.worklight.nativeandroid.common.WLUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public final class WLProcedureInvocationFailResponse extends WLFailResponse {
    private static final String JSON_KEY_ERROR_MESSAGES = "errors";
    private JSONObject jsonResult;

    WLProcedureInvocationFailResponse(WLResponse wLResponse) {
        super(wLResponse);
    }

    WLProcedureInvocationFailResponse(WLFailResponse wLFailResponse) {
        super((WLResponse) wLFailResponse);
        setErrorCode(wLFailResponse.getErrorCode());
        setErrorMsg(wLFailResponse.getErrorMsg());
    }

    public List<String> getProcedureInvocationErrors() {
        String str = JSON_KEY_ERROR_MESSAGES;
        String str2 = "getProcedureInvocationErrors";
        Logger.enter(getClass().getSimpleName(), str2);
        List<String> arrayList = new ArrayList<>();
        try {
            if (!(getResult() == null || getResult().get(str) == null)) {
                arrayList = WLUtils.convertJSONArrayToList(getResult().getJSONArray(str));
            }
        } catch (JSONException unused) {
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return arrayList;
    }

    public JSONObject getResult() throws JSONException {
        String str = "getResult";
        Logger.enter(getClass().getSimpleName(), str);
        String responseText = getResponseText();
        if (this.jsonResult == null && responseText != null && responseText.length() > 0) {
            this.jsonResult = WLUtils.convertStringToJSON(responseText);
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.jsonResult;
    }

    @Deprecated
    public JSONObject getJSONResult() throws JSONException {
        String str = "getJSONResult";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return getResult();
    }
}
