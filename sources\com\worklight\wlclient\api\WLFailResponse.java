package com.worklight.wlclient.api;

import com.worklight.common.Logger;
import com.worklight.nativeandroid.common.WLUtils;
import java.util.Iterator;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class WLFailResponse extends WLResponse {
    public static final int HTTP_STATUS = -1;
    private static final String JSON_KEY_ERROR_CODE = "errorCode";
    private static final String JSON_KEY_ERROR_MSG = "errorMsg";
    public static final String JSON_KEY_FAILURES = "failures";
    private static Logger logger = Logger.getInstance("wl.failResponse");
    WLErrorCode clientErrorCode;
    String errorCode;
    String errorMsg;

    public WLFailResponse(Response response) {
        super(response);
        parseErrorFromResponse();
        Logger.logNetworkData(String.format("FAIL RESPONSE :: \nErrorCode : %s\nErrorMsg : %s", new Object[]{this.errorCode, this.errorMsg}));
    }

    public WLFailResponse(WLResponse wLResponse) {
        super(wLResponse);
        parseErrorFromResponse();
        Logger.logNetworkData(String.format("FAIL RESPONSE :: \nErrorCode : %s\nErrorMsg : %s", new Object[]{this.errorCode, this.errorMsg}));
    }

    public WLFailResponse(WLErrorCode wLErrorCode, String str, WLRequestOptions wLRequestOptions) {
        super(-1, "", wLRequestOptions);
        setErrorCode(wLErrorCode);
        setErrorMsg(str);
        Logger.logNetworkData(String.format("FAIL RESPONSE :: \nErrorCode : %s\nErrorMsg : %s", new Object[]{this.errorCode, this.errorMsg}));
    }

    public String getErrorStatusCode() {
        String str = "getErrorStatusCode";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.errorCode;
    }

    public WLErrorCode getErrorCode() {
        String str = "getErrorCode";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.clientErrorCode;
    }

    /* access modifiers changed from: 0000 */
    public void setErrorCode(WLErrorCode wLErrorCode) {
        this.clientErrorCode = wLErrorCode;
        this.errorCode = wLErrorCode.name();
        this.errorMsg = wLErrorCode.getDescription();
    }

    public String getErrorMsg() {
        String str = "getErrorMsg";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.errorMsg != null || this.errorCode == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return this.errorMsg;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.errorCode;
    }

    /* access modifiers changed from: 0000 */
    public void setErrorMsg(String str) {
        this.errorMsg = str;
    }

    private void parseErrorFromResponse() {
        String str = JSON_KEY_ERROR_MSG;
        String str2 = JSON_KEY_ERROR_CODE;
        String str3 = "parseErrorFromResponse";
        Logger.enter(getClass().getSimpleName(), str3);
        this.clientErrorCode = WLErrorCode.SERVER_ERROR;
        if (getResponseText() != null && getResponseText().length() > 0) {
            try {
                String responseText = getResponseText();
                if (getResponseJSON() != null && WLUtils.isContainBrackets(responseText)) {
                    setResponseJSON(WLUtils.convertStringToJSON(getResponseText()));
                    if (getResponseJSON().has(str2)) {
                        this.errorCode = getResponseJSON().getString(str2);
                    }
                    if (getResponseJSON().has(str)) {
                        this.errorMsg = getResponseJSON().getString(str);
                    }
                }
                if (this.errorCode == null) {
                    ExtractSecurityCheckErrorCode();
                }
                if (this.errorMsg == null && this.errorCode != null) {
                    this.errorMsg = this.errorCode;
                }
            } catch (Exception e) {
                logger.debug(String.format("Additional error information is not available for the current response and response text is: %s", new Object[]{getResponseText()}), (Throwable) e);
                this.errorCode = WLErrorCode.UNEXPECTED_ERROR.name();
                this.errorMsg = e.toString();
            }
        }
        if (this.errorCode == null) {
            this.errorCode = String.valueOf(getStatus());
        }
        if (this.errorMsg == null) {
            this.errorMsg = this.statusText;
        }
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public String toString() {
        String str = "toString";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" WLFailResponse [errorMsg=");
        sb.append(this.errorMsg);
        sb.append(", errorCode=");
        sb.append(this.errorCode);
        sb.append("]");
        return sb.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x002b  */
    private void ExtractSecurityCheckErrorCode() throws JSONException {
        JSONObject jSONObject;
        String str = "ExtractSecurityCheckErrorCode";
        Logger.enter(getClass().getSimpleName(), str);
        if (getResponseJSON() != null) {
            JSONObject responseJSON = getResponseJSON();
            String str2 = JSON_KEY_FAILURES;
            if (responseJSON.has(str2)) {
                jSONObject = getResponseJSON().getJSONObject(str2);
                if (jSONObject != null) {
                    Iterator keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject((String) keys.next());
                        String str3 = JSON_KEY_ERROR_CODE;
                        if (jSONObject2.has(str3)) {
                            this.errorCode = jSONObject2.getString(str3);
                            String str4 = JSON_KEY_ERROR_MSG;
                            if (jSONObject2.has(str4)) {
                                this.errorMsg = jSONObject2.getString(str4);
                            }
                        }
                    }
                }
                Logger.exit(getClass().getSimpleName(), str);
            }
        }
        jSONObject = null;
        if (jSONObject != null) {
        }
        Logger.exit(getClass().getSimpleName(), str);
    }
}
