package com.worklight.wlclient.api;

import com.google.common.net.HttpHeaders;
import com.worklight.common.Logger;
import com.worklight.nativeandroid.common.WLUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class WLResponse {
    private static Logger logger = Logger.getInstance("wl.response");
    private Map<String, List<String>> headers;
    private WLRequestOptions requestOptions;
    private byte[] responseBytes;
    private JSONObject responseJSON;
    private String responseText;
    private int status;
    protected String statusText;

    public WLResponse(Response response) {
        String str = HttpHeaders.CONTENT_ENCODING;
        this.status = response.code();
        this.statusText = response.message();
        this.headers = response.headers().toMultimap();
        try {
            if (this.status != 204) {
                if (response.header(str) != null) {
                    Logger logger2 = logger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Content encoding is ");
                    sb.append(response.header(str));
                    logger2.trace(sb.toString());
                    if (response.header(str).equalsIgnoreCase("gzip")) {
                        this.responseBytes = WLUtils.readStreamToByteArray(new GZIPInputStream(response.body().byteStream()));
                    } else {
                        this.responseBytes = response.body().bytes();
                    }
                } else {
                    if (response.header(HttpHeaders.CONTENT_LENGTH) != null) {
                        logger.trace("Response does not include a Content-Encoding header. Attempting to read response body.");
                    }
                    this.responseBytes = WLUtils.readStreamToByteArray(response.body().byteStream());
                }
                Logger.logNetworkData(String.format("RESPONSE :: \nHeaders : %s\nStatus : %s\nStatusText : %s\nResponseText : %s", new Object[]{getHeaders(), Integer.valueOf(getStatus()), getStatusText(), getResponseText()}));
            }
        } catch (Exception e) {
            Logger logger3 = logger;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Error getting content from server response: ");
            sb2.append(e.getMessage());
            logger3.error(sb2.toString(), (Throwable) e);
            Logger.logNetworkData(String.format("RESPONSE :: \n%s", new Object[]{e.getMessage()}));
        }
    }

    WLResponse(WLResponse wLResponse) {
        this.status = wLResponse.status;
        this.statusText = wLResponse.statusText;
        this.requestOptions = wLResponse.requestOptions;
        this.responseText = wLResponse.responseText;
        this.responseJSON = wLResponse.responseJSON;
        this.responseBytes = wLResponse.responseBytes;
        this.headers = wLResponse.getHeaders();
        Logger.logNetworkData(String.format("RESPONSE :: \nHeaders : %s\nStatus : %s\nStatusText : %s\nResponseText : %s", new Object[]{getHeaders(), Integer.valueOf(getStatus()), getStatusText(), getResponseText()}));
    }

    WLResponse(int i, String str, WLRequestOptions wLRequestOptions) {
        this.status = i;
        this.requestOptions = wLRequestOptions;
        this.responseText = str;
        this.headers = new HashMap();
        responseTextToJSON(str);
        Logger.logNetworkData(String.format("RESPONSE :: \nHeaders : %s\nStatus : %s\nStatusText : %s\nResponseText : %s", new Object[]{getHeaders(), Integer.valueOf(getStatus()), getStatusText(), getResponseText()}));
    }

    private void responseTextToJSON(String str) {
        String str2 = "responseTextToJSON";
        Logger.enter(getClass().getSimpleName(), str2);
        int indexOf = this.responseText.indexOf(123);
        int lastIndexOf = this.responseText.lastIndexOf(125);
        if (indexOf == -1 || lastIndexOf == -1) {
            this.responseJSON = null;
            Logger.exit(getClass().getSimpleName(), str2);
            return;
        }
        this.responseText.substring(indexOf, lastIndexOf + 1);
        try {
            this.responseJSON = new JSONObject(str);
        } catch (JSONException e) {
            this.responseJSON = null;
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Response from MobileFirst Platform server failed because could not read JSON from response with text ");
            sb.append(str);
            logger2.error(sb.toString(), (Throwable) e);
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public int getStatus() {
        String str = "getStatus";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.status;
    }

    public Object getInvocationContext() {
        String str = "getInvocationContext";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.requestOptions == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return null;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.requestOptions.getInvocationContext();
    }

    public byte[] getResponseBytes() {
        String str = "getResponseBytes";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.responseBytes;
    }

    public String getResponseText() {
        String str = "getResponseText";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.responseText == null) {
            if (this.responseBytes == null) {
                this.responseText = "";
            } else {
                this.responseText = new String(this.responseBytes);
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.responseText;
    }

    public void setResponseText(String str) {
        String str2 = "setResponseText";
        Logger.enter(getClass().getSimpleName(), str2);
        this.responseText = str;
        responseTextToJSON(str);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: 0000 */
    public void setInvocationContext(Object obj) {
        this.requestOptions.setInvocationContext(obj);
    }

    public WLRequestOptions getOptions() {
        String str = "getOptions";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.requestOptions;
    }

    public void setOptions(WLRequestOptions wLRequestOptions) {
        String str = "setOptions";
        Logger.enter(getClass().getSimpleName(), str);
        this.requestOptions = wLRequestOptions;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public List<String> getHeader(String str) {
        String str2 = "getHeader";
        Logger.enter(getClass().getSimpleName(), str2);
        for (String str3 : this.headers.keySet()) {
            if (str3.equalsIgnoreCase(str)) {
                Logger.exit(getClass().getSimpleName(), str2);
                return (List) this.headers.get(str3);
            }
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return null;
    }

    public String getFirstHeader(String str) {
        String str2 = "getFirstHeader";
        Logger.enter(getClass().getSimpleName(), str2);
        List header = getHeader(str);
        if (header != null) {
            Logger.exit(getClass().getSimpleName(), str2);
            return (String) header.get(0);
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return null;
    }

    public JSONObject getResponseJSON() {
        String str = "getResponseJSON";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.responseJSON == null) {
            getResponseText();
            if (this.responseText != null) {
                responseTextToJSON(getResponseText());
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.responseJSON;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WLResponse [invocationContext=");
        WLRequestOptions wLRequestOptions = this.requestOptions;
        sb.append(wLRequestOptions != null ? wLRequestOptions.getInvocationContext() : "null");
        sb.append(", responseText=");
        sb.append(getResponseText());
        sb.append(", status=");
        sb.append(this.status);
        sb.append("]");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void setResponseJSON(JSONObject jSONObject) {
        String str = "setResponseJSON";
        Logger.enter(getClass().getSimpleName(), str);
        this.responseJSON = jSONObject;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public String getStatusText() {
        String str = "getStatusText";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.statusText;
    }

    public void setStatusText(String str) {
        String str2 = "setStatusText";
        Logger.enter(getClass().getSimpleName(), str2);
        this.statusText = str;
        Logger.exit(getClass().getSimpleName(), str2);
    }
}
