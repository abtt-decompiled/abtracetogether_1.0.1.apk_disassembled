package com.worklight.wlclient.auth;

import com.worklight.common.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class AccessToken {
    private static final String DEFAULT_SCOPE = "RegisteredClient";
    private static final String JSON_ACCESS_TOKEN_KEY = "access_token";
    private static final String JSON_SCOPE_KEY = "scope";
    private static final String JSON_TOKEN_EXPIRES_IN = "expires_in";
    private long expireIn;
    private String scope;
    private String value;

    public AccessToken(JSONObject jSONObject) throws JSONException {
        String string = jSONObject.getString(JSON_SCOPE_KEY);
        if (string.isEmpty()) {
            string = "RegisteredClient";
        }
        this.scope = string;
        this.expireIn = System.currentTimeMillis() + ((long) (((Integer) jSONObject.get(JSON_TOKEN_EXPIRES_IN)).intValue() * 1000));
        this.value = jSONObject.getString(JSON_ACCESS_TOKEN_KEY);
    }

    public String getValue() {
        String str = "getValue";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.value;
    }

    public String getAsAuthorizationRequestHeader() {
        String str = "getAsAuthorizationRequestHeader";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ");
        sb.append(getValue());
        return sb.toString();
    }

    public String getAsFormEncodedBodyParameter() {
        String str = "getAsFormEncodedBodyParameter";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append("access_token=");
        sb.append(getValue());
        return sb.toString();
    }

    public String getScope() {
        String str = "getScope";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.scope;
    }

    /* access modifiers changed from: protected */
    public boolean isValidToken() {
        String str = "isValidToken";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.expireIn > System.currentTimeMillis();
    }
}
