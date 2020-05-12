package com.worklight.wlclient.api;

import android.util.Pair;
import com.worklight.common.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WLRequestOptions {
    private boolean fromChallenge;
    private Object invocationContext;
    private boolean isAZRequest;
    private boolean isJsonContentType;
    private boolean isTextContentType;
    private List<String> okHeaderValues = new ArrayList();
    private List<String> okHeaders = new ArrayList();
    private Map<String, String> parameters = new HashMap();
    private WLResponseListener responseListener;
    private int timeout = 10000;

    public int getTimeout() {
        String str = "getTimeout";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.timeout;
    }

    public void setTimeout(int i) {
        String str = "setTimeout";
        Logger.enter(getClass().getSimpleName(), str);
        this.timeout = i;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public Object getInvocationContext() {
        String str = "getInvocationContext";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.invocationContext;
    }

    public void setInvocationContext(Object obj) {
        String str = "setInvocationContext";
        Logger.enter(getClass().getSimpleName(), str);
        this.invocationContext = obj;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public WLResponseListener getResponseListener() {
        String str = "getResponseListener";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.responseListener;
    }

    public void setResponseListener(WLResponseListener wLResponseListener) {
        String str = "setResponseListener";
        Logger.enter(getClass().getSimpleName(), str);
        this.responseListener = wLResponseListener;
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* access modifiers changed from: 0000 */
    public void addParameters(Map<String, String> map) {
        this.parameters.putAll(map);
    }

    public void addParameter(String str, String str2) {
        String str3 = "addParameter";
        Logger.enter(getClass().getSimpleName(), str3);
        this.parameters.put(str, str2);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    /* access modifiers changed from: 0000 */
    public String getParameter(String str) {
        Map<String, String> map = this.parameters;
        return map != null ? (String) map.get(str) : "";
    }

    public void addHeader(String str, String str2) {
        String str3 = "addHeader";
        Logger.enter(getClass().getSimpleName(), str3);
        this.okHeaders.add(str);
        this.okHeaderValues.add(str2);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public Pair<List<String>, List<String>> getOkHeaders() {
        return new Pair<>(this.okHeaders, this.okHeaderValues);
    }

    public boolean isFromChallenge() {
        String str = "isFromChallenge";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.fromChallenge;
    }

    public void setFromChallenge(boolean z) {
        String str = "setFromChallenge";
        Logger.enter(getClass().getSimpleName(), str);
        this.fromChallenge = z;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean isJsonContentType() {
        String str = "isJsonContentType";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.isJsonContentType;
    }

    public void setJsonContentType(boolean z) {
        String str = "setJsonContentType";
        Logger.enter(getClass().getSimpleName(), str);
        this.isJsonContentType = z;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean isAZRequest() {
        String str = "isAZRequest";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.isAZRequest;
    }

    public void setAZRequest(boolean z) {
        String str = "setAZRequest";
        Logger.enter(getClass().getSimpleName(), str);
        this.isAZRequest = z;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean isTextContentType() {
        String str = "isTextContentType";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.isTextContentType;
    }

    public void setTextContentType(boolean z) {
        String str = "setTextContentType";
        Logger.enter(getClass().getSimpleName(), str);
        this.isTextContentType = z;
        Logger.exit(getClass().getSimpleName(), str);
    }
}
