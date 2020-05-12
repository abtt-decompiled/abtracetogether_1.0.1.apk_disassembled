package com.worklight.wlclient.auth;

import ca.albertahealthservices.contacttracing.BuildConfig;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class JWT {
    private static final int DEFAULT_JWT_EXPIRATION = 60000;
    protected String aud;
    protected long exp;
    protected long iat;
    protected String iss;
    protected String jti;
    protected String nbf;
    protected String sub;

    public JWT() {
        WLConfig instance = WLConfig.getInstance();
        WLAuthorizationManagerInternal instance2 = WLAuthorizationManagerInternal.getInstance();
        this.iss = String.format("%s$%s$%s", new Object[]{instance.getPackageName(), "android", instance.getApplicationVersion()});
        this.sub = instance2.getClientId();
        this.exp = instance.getCurrentWithRelativeTime() + BuildConfig.MAX_SCAN_INTERVAL;
        this.iat = instance.getCurrentWithRelativeTime();
    }

    public JSONObject toJson() throws JSONException {
        String str = "toJson";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("iss", this.iss);
        jSONObject.put("sub", this.sub);
        jSONObject.put("aud", this.aud);
        jSONObject.put("exp", this.exp);
        jSONObject.put("nbf", this.nbf);
        jSONObject.put("iat", this.iat);
        jSONObject.put("jti", this.jti);
        Logger.exit(getClass().getSimpleName(), str);
        return jSONObject;
    }
}
