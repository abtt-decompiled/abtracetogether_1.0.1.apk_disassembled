package com.worklight.wlclient.api;

import com.worklight.common.Logger;
import com.worklight.wlclient.WLRequest.RequestPaths;
import com.worklight.wlclient.auth.AccessToken;
import com.worklight.wlclient.auth.WLAuthorizationManagerInternal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.json.JSONObject;

public class WLAuthorizationManager {
    private static WLAuthorizationManager instance = new WLAuthorizationManager();

    private WLAuthorizationManager() {
    }

    public static synchronized WLAuthorizationManager getInstance() {
        WLAuthorizationManager wLAuthorizationManager;
        synchronized (WLAuthorizationManager.class) {
            wLAuthorizationManager = instance;
        }
        return wLAuthorizationManager;
    }

    public String getResourceScope(HttpURLConnection httpURLConnection) {
        String str = "getResourceScope";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return WLAuthorizationManagerInternal.getInstance().getAuthorizationScope(httpURLConnection);
    }

    public String getResourceScope(Map map) {
        String str = "getResourceScope";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return WLAuthorizationManagerInternal.getInstance().getAuthorizationScope(map);
    }

    public boolean isAuthorizationRequired(HttpURLConnection httpURLConnection) {
        String str = "isAuthorizationRequired";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return WLAuthorizationManagerInternal.getInstance().isAuthorizationRequired(httpURLConnection);
    }

    public boolean isAuthorizationRequired(int i, Map map) {
        String str = "isAuthorizationRequired";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return WLAuthorizationManagerInternal.getInstance().isAuthorizationRequired(i, map);
    }

    public void obtainAccessToken(String str, WLAccessTokenListener wLAccessTokenListener) {
        String str2 = "obtainAccessToken";
        Logger.enter(getClass().getSimpleName(), str2);
        WLAuthorizationManagerInternal.getInstance().obtainAccessToken(str, wLAccessTokenListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void clearAccessToken(AccessToken accessToken) {
        String str = "clearAccessToken";
        Logger.enter(getClass().getSimpleName(), str);
        WLAuthorizationManagerInternal.getInstance().clearAccessToken(accessToken);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void login(String str, JSONObject jSONObject, WLLoginResponseListener wLLoginResponseListener) {
        String simpleName = getClass().getSimpleName();
        String str2 = RequestPaths.LOGIN;
        Logger.enter(simpleName, str2);
        WLAuthorizationManagerInternal.getInstance().login(str, jSONObject, wLLoginResponseListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void logout(String str, WLLogoutResponseListener wLLogoutResponseListener) {
        String simpleName = getClass().getSimpleName();
        String str2 = RequestPaths.LOGOUT;
        Logger.enter(simpleName, str2);
        WLAuthorizationManagerInternal.getInstance().logout(str, wLLogoutResponseListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void setLoginTimeout(int i) {
        String str = "setLoginTimeout";
        Logger.enter(getClass().getSimpleName(), str);
        WLAuthorizationManagerInternal.getInstance().setLoginTimeout(i);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void setAuthorizationServerUrl(URL url) {
        String str = "setAuthorizationServerUrl";
        Logger.enter(getClass().getSimpleName(), str);
        WLAuthorizationManagerInternal.getInstance().setAuthorizationServerUrl(url);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public URL getAuthorizationServerUrl() {
        String str = "getAuthorizationServerUrl";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return WLAuthorizationManagerInternal.getInstance().getAuthorizationServerRootPathAsURL();
    }
}
