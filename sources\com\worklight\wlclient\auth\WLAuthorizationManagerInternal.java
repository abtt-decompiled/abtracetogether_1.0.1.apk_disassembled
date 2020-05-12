package com.worklight.wlclient.auth;

import android.util.Log;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.common.security.WLDeviceAuthManager;
import com.worklight.common.security.WLOAuthCertManager;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.wlclient.AsynchronousRequestSender;
import com.worklight.wlclient.RequestMethod;
import com.worklight.wlclient.WLClientInstanceRegistrationRequest;
import com.worklight.wlclient.WLRequest;
import com.worklight.wlclient.WLRequest.RequestPaths;
import com.worklight.wlclient.WLRequestListener;
import com.worklight.wlclient.api.WLAccessTokenListener;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLConstants;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLLoginResponseListener;
import com.worklight.wlclient.api.WLLogoutResponseListener;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class WLAuthorizationManagerInternal {
    private static final String ANALYTICS_API_KEY = "com.worklight.oauth.analytics.api.key";
    private static final String ANALYTICS_PASSWORD = "com.worklight.oauth.analytics.password";
    private static final String ANALYTICS_URL_KEY = "com.worklight.oauth.analytics.url";
    private static final String ANALYTICS_USERNAME = "com.worklight.oauth.analytics.username";
    private static final String API_VERSION = "v1";
    private static final String BASE_AUTHORIZATION = "az";
    private static final String BASE_PREAUTH = "preauth";
    private static final String BASE_REGISTRATION = "registration";
    private static final String BEARER = "Bearer";
    private static final String CLIENT_ID_OAUTH_LABEL = "com.worklight.oauth.clientid";
    private static final String CLIENT_REGISTRATION_DATA_KEY = "com.worklight.oauth.application.data";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_LOCATION = "Location";
    private static final String JSON_ERROR_DESCRIPTION_KEY = "errorMsg";
    private static final String JSON_ERROR_KEY = "errorCode";
    private static final String JSON_SCOPE_KEY = "scope";
    private static final int MIN_LOGIN_TIMEOUT = 5000;
    private static final String PARAM_AUTHORIZATION_CODE_VALUE = "authorization_code";
    private static final String PARAM_CLIENT_ASSERTION_KEY = "client_assertion";
    private static final String PARAM_CLIENT_ASSERTION_TYPE_KEY = "client_assertion_type";
    private static final String PARAM_CLIENT_ASSERTION_TYPE_VALUE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
    private static final String PARAM_CLIENT_ID_KEY = "client_id";
    private static final String PARAM_CODE_KEY = "code";
    private static final String PARAM_CODE_VALUE = "code";
    private static final String PARAM_GET_REFRESH_TOKEN_KEY = "get_refresh_token";
    private static final String PARAM_GET_REFRESH_TOKEN_VALUE = "TRUE";
    private static final String PARAM_GRANT_TYPE_KEY = "grant_type";
    private static final String PARAM_REDIRECT_URI_KEY = "redirect_uri";
    private static final String PARAM_REDIRECT_URI_VALUE = "://mfpredirecturi";
    private static final String PARAM_REFRESH_TOKEN_KEY = "refresh_token";
    private static final String PARAM_REFRESH_TOKEN_VALUE = "refresh_token";
    private static final String PARAM_RESPONSE_TYPE_KEY = "response_type";
    private static final String PARAM_SCOPE_KEY = "scope";
    private static final String PARAM_SECURITY_CHECK_KEY = "security_check";
    private static final String PATH_LOGOUT = "logout";
    private static final String PATH_OAUTH_AUTHORIZATION = "authorization";
    private static final String PATH_OAUTH_TOKEN = "token";
    private static final String PATH_PREAUTHORIZE = "preauthorize";
    private static final String PATH_REGISTER_CLIENTS_ON_SUCCESS = "clients";
    private static final String PATH_REGISTER_SELF = "self";
    private static final int RESOURCE_TO_SCOPE_CACHE_SIZE = 100;
    private static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";
    private static boolean enableRefreshToken = false;
    private static WLAuthorizationManagerInternal instance;
    /* access modifiers changed from: private */
    public static Logger logger;
    /* access modifiers changed from: private */
    public boolean authorizationInProgress;
    /* access modifiers changed from: private */
    public final Object authorizationInProgressLock = new Object();
    private URL authorizationServerUrl;
    private String clientId = null;
    /* access modifiers changed from: private */
    public boolean isUpdateRegistrationRequest = false;
    /* access modifiers changed from: private */
    public WLAuthQueue obtainAccessTokenQueue;
    /* access modifiers changed from: private */
    public List<WLRequestListener> registrationQueue;
    /* access modifiers changed from: private */
    public final Object registrationQueueLock = new Object();
    private Map<String, String> resourceToScopeCache;
    /* access modifiers changed from: private */
    public Map<String, String> scopeToRefreshToken;
    /* access modifiers changed from: private */
    public Map<String, AccessToken> scopeToToken;
    private boolean shouldCallRegistrationAfterUnknownClientError = true;
    private int timeOut = 0;

    private final class WLAuthorizationRequestListener implements WLResponseListener {
        /* access modifiers changed from: private */
        public final String scope;

        private final class WLTokenRequestListener implements WLResponseListener {
            private WLTokenRequestListener() {
            }

            public void onSuccess(WLResponse wLResponse) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                try {
                    WLAuthorizationManagerInternal.this.onTokenSuccess(wLResponse);
                } catch (JSONException unused) {
                    onFailure(new WLFailResponse(wLResponse));
                }
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.onObtainAccessTokenFailure(WLAuthorizationRequestListener.this.scope, wLFailResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }
        }

        private WLAuthorizationRequestListener(String str) {
            this.scope = str;
        }

        public void onSuccess(WLResponse wLResponse) {
            String str = "onSuccess";
            Logger.enter(getClass().getSimpleName(), str);
            WLAuthorizationManagerInternal.this.onAuthorizationSuccess(this.scope, wLResponse, new WLTokenRequestListener());
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onFailure(WLFailResponse wLFailResponse) {
            String str = "onFailure";
            Logger.enter(getClass().getSimpleName(), str);
            WLAuthorizationManagerInternal.this.onObtainAccessTokenFailure(this.scope, wLFailResponse);
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    public static synchronized WLAuthorizationManagerInternal getInstance() {
        WLAuthorizationManagerInternal wLAuthorizationManagerInternal;
        synchronized (WLAuthorizationManagerInternal.class) {
            if (instance == null) {
                instance = new WLAuthorizationManagerInternal();
            }
            wLAuthorizationManagerInternal = instance;
        }
        return wLAuthorizationManagerInternal;
    }

    public boolean isRegistrationInProgress() {
        String str = "isRegistrationInProgress";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return !this.registrationQueue.isEmpty();
    }

    public Request addAuthorizationHeader(Request request, String str) {
        String str2 = "addAuthorizationHeader";
        Logger.enter(getClass().getSimpleName(), str2);
        if (!str.equals("")) {
            Logger.exit(getClass().getSimpleName(), str2);
            return request.newBuilder().header("Authorization", str).build();
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return request;
    }

    public boolean isMfpConflict(WLResponse wLResponse) {
        String str = "isMfpConflict";
        Logger.enter(getClass().getSimpleName(), str);
        if (wLResponse.getStatus() != 409 || !wLResponse.getHeaders().containsKey("MFP-Conflict")) {
            Logger.exit(getClass().getSimpleName(), str);
            return false;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return true;
    }

    public void obtainAccessToken(final String str, final WLAccessTokenListener wLAccessTokenListener) {
        Logger.enter(getClass().getSimpleName(), "obtainAccessToken");
        logger.debug("obtainAccessToken : entry.");
        synchronized (this.authorizationInProgressLock) {
            AccessToken cachedAccessToken = getCachedAccessToken(str);
            if (cachedAccessToken != null) {
                wLAccessTokenListener.onSuccess(cachedAccessToken);
                logger.debug("obtainAccessToken : Access Token found.");
                Logger.exit(getClass().getSimpleName(), "obtainAccessToken");
                return;
            }
            final String cachedRefreshToken = getCachedRefreshToken(str);
            if (!enableRefreshToken || cachedRefreshToken == null) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("obtainAccessToken enableRefreshToken=");
                sb.append(enableRefreshToken);
                logger2.debug(sb.toString());
                obtainAccessToken(str, (WLResponseListener) new WLResponseListener() {
                    public void onSuccess(WLResponse wLResponse) {
                        String str = "onSuccess";
                        Logger.enter(getClass().getSimpleName(), str);
                        wLAccessTokenListener.onSuccess(WLAuthorizationManagerInternal.this.getCachedAccessToken(str));
                        Logger.exit(getClass().getSimpleName(), str);
                    }

                    public void onFailure(WLFailResponse wLFailResponse) {
                        String str = "onFailure";
                        Logger.enter(getClass().getSimpleName(), str);
                        wLAccessTokenListener.onFailure(wLFailResponse);
                        Logger.exit(getClass().getSimpleName(), str);
                    }
                });
                Logger.exit(getClass().getSimpleName(), "obtainAccessToken");
                return;
            }
            logger.debug("obtainAccessToken : Refresh Token found, call invokeTokenRequest");
            logger.debug("Calling pre-auth with default scope to trigger default app checks");
            invokePreAuthorizationRequest(str, null, new WLResponseListener() {
                public void onSuccess(WLResponse wLResponse) {
                    String str = "onSuccess";
                    Logger.enter(getClass().getSimpleName(), str);
                    WLAuthorizationManagerInternal.logger.debug("Finished pre-authorization - calling Token Endpoint to get Refresh token");
                    WLAuthorizationManagerInternal.this.invokeRefreshTokenRequest(str, cachedRefreshToken, wLAccessTokenListener);
                    Logger.exit(getClass().getSimpleName(), str);
                }

                public void onFailure(WLFailResponse wLFailResponse) {
                    String str = "onFailure";
                    Logger.enter(getClass().getSimpleName(), str);
                    WLAuthorizationManagerInternal.this.onObtainAccessTokenFailure(str, wLFailResponse);
                    Logger.exit(getClass().getSimpleName(), str);
                }
            });
            logger.debug("obtainAccessToken Returning early from obtainToken");
            Logger.exit(getClass().getSimpleName(), "obtainAccessToken");
        }
    }

    /* access modifiers changed from: private */
    public void invokeRefreshTokenRequest(final String str, String str2, final WLAccessTokenListener wLAccessTokenListener) {
        Logger.enter(getClass().getSimpleName(), "invokeRefreshTokenRequest");
        invokeTokenRequest(PARAM_GET_REFRESH_TOKEN_KEY, new WLResponseListener() {
            public void onSuccess(WLResponse wLResponse) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                try {
                    WLAuthorizationManagerInternal.this.onTokenSuccess(wLResponse);
                    AccessToken cachedAccessToken = WLAuthorizationManagerInternal.this.getCachedAccessToken(str);
                    if (cachedAccessToken != null) {
                        WLAuthorizationManagerInternal.logger.debug("obtainAccessToken : Received AccessToken using RefreshToken");
                        wLAccessTokenListener.onSuccess(cachedAccessToken);
                    }
                } catch (JSONException unused) {
                    onFailure(new WLFailResponse(wLResponse));
                }
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                boolean isAuthorizationRequired = WLAuthorizationManagerInternal.this.isAuthorizationRequired(wLFailResponse.getStatus(), wLFailResponse.getHeaders());
                Logger access$000 = WLAuthorizationManagerInternal.logger;
                StringBuilder sb = new StringBuilder();
                sb.append("obtainAccessToken : isAuthRequired=");
                sb.append(isAuthorizationRequired);
                access$000.debug(sb.toString());
                WLAuthorizationManagerInternal.logger.debug("obtainAccessToken :*** failure ***");
                if (isAuthorizationRequired) {
                    WLAuthorizationManagerInternal.this.scopeToRefreshToken.remove(str);
                    WLConfig.getInstance().removeSecurityTokenPref(str);
                }
                WLAuthorizationManagerInternal.this.onObtainAccessTokenFailure(str, wLFailResponse);
                wLAccessTokenListener.onFailure(wLFailResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }
        }, str2);
    }

    private void obtainAccessToken(String str, WLResponseListener wLResponseListener) {
        String str2 = "obtainAccessToken";
        Logger.enter(getClass().getSimpleName(), str2);
        if (str == null) {
            str = WLConstants.DEFAULT_SCOPE;
        }
        if (!this.authorizationInProgress) {
            logger.debug("Set authInProgress from obtainAccessToken.");
            this.authorizationInProgress = true;
            if (shouldRegister()) {
                obtainAccessTokenWithRegistration(str, wLResponseListener);
            } else {
                obtainAccessTokenWithoutRegistration(str, wLResponseListener);
            }
        } else {
            addToObtainAccessTokenQueue(str, wLResponseListener);
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public boolean isAuthorizationRequired(Response response) {
        Logger.enter(getClass().getSimpleName(), "isAuthorizationRequired");
        int code = response.code();
        if (code == 401 || code == 403) {
            Boolean isAuthorizationRequired = isAuthorizationRequired(response.headers("WWW-Authenticate"));
            if (isAuthorizationRequired != null) {
                return isAuthorizationRequired.booleanValue();
            }
        } else if (isMfpConflict(response)) {
            return true;
        }
        return false;
    }

    public boolean isAuthorizationRequired(HttpURLConnection httpURLConnection) {
        String str = "isAuthorizationRequired";
        Logger.enter(getClass().getSimpleName(), str);
        if (httpURLConnection == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return false;
        }
        try {
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 401 || responseCode == 403) {
                Boolean isAuthorizationRequired = isAuthorizationRequired((List) httpURLConnection.getHeaderFields().get("WWW-Authenticate"));
                if (isAuthorizationRequired != null) {
                    return isAuthorizationRequired.booleanValue();
                }
            }
        } catch (IOException unused) {
            logger.debug("Unable to get HttpURLConnection response code");
        }
        return false;
    }

    public boolean isForbidden(Response response) {
        String str = "isForbidden";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return response.code() == 403;
    }

    public boolean isAuthorizationRequired(int i, Map map) {
        String str = "isAuthorizationRequired";
        Logger.enter(getClass().getSimpleName(), str);
        if (map == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return false;
        }
        if (i == 401 || i == 403) {
            String str2 = "WWW-Authenticate";
            if (map.containsKey(str2)) {
                for (String contains : (List) map.get(str2)) {
                    if (contains.contains("Bearer")) {
                        Logger.exit(getClass().getSimpleName(), str);
                        return true;
                    }
                }
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return false;
    }

    public String getClientId() {
        String str = "getClientId";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.clientId == null) {
            this.clientId = WLConfig.getInstance().readSecurityPref(CLIENT_ID_OAUTH_LABEL);
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.clientId;
    }

    public void clearRegistration() {
        Logger.enter(getClass().getSimpleName(), "clearRegistration");
        synchronized (this.registrationQueueLock) {
            clearCachedResourcesAndToken();
            this.clientId = null;
            WLConfig.getInstance().writeSecurityPref(CLIENT_ID_OAUTH_LABEL, null);
            WLConfig.getInstance().writeSecurityPref(CLIENT_REGISTRATION_DATA_KEY, null);
            WLConfig.getInstance().writeSecurityPref(ANALYTICS_URL_KEY, null);
            WLConfig.getInstance().writeSecurityPref(ANALYTICS_API_KEY, null);
            WLOAuthCertManager.getInstance().deleteKeyPair();
        }
        Logger.exit(getClass().getSimpleName(), "clearRegistration");
    }

    public String getAuthorizationScope(HttpURLConnection httpURLConnection) {
        String str = "getAuthorizationScope";
        Logger.enter(getClass().getSimpleName(), str);
        List list = (List) httpURLConnection.getHeaderFields().get("WWW-Authenticate");
        Logger.exit(getClass().getSimpleName(), str);
        return getAuthorizationScope(list);
    }

    public String getAuthorizationScope(Response response) {
        String str = "getAuthorizationScope";
        Logger.enter(getClass().getSimpleName(), str);
        if (response != null) {
            List values = response.headers().values("WWW-Authenticate");
            Logger.exit(getClass().getSimpleName(), str);
            return getAuthorizationScope(values);
        }
        Logger.exit(getClass().getSimpleName(), str);
        return null;
    }

    public String getAuthorizationScope(Map map) {
        String str = "getAuthorizationScope";
        Logger.enter(getClass().getSimpleName(), str);
        if (map != null) {
            Object obj = map.get("WWW-Authenticate");
            if (obj instanceof List) {
                List list = (List) obj;
                Logger.exit(getClass().getSimpleName(), str);
                return getAuthorizationScope((List) new ArrayList(list));
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return null;
    }

    public String getAuthorizationHeader(Response response) {
        String str = "getAuthorizationHeader";
        Logger.enter(getClass().getSimpleName(), str);
        if (response != null) {
            Headers headers = response.headers();
            Logger.exit(getClass().getSimpleName(), str);
            return headers.get("WWW-Authenticate");
        }
        Logger.exit(getClass().getSimpleName(), str);
        return null;
    }

    public void clearCachedResourcesAndToken() {
        String str = "clearCachedResourcesAndToken";
        Logger.enter(getClass().getSimpleName(), str);
        this.scopeToToken.clear();
        this.resourceToScopeCache.clear();
        this.scopeToRefreshToken.clear();
        WLConfig.getInstance().clearSecurityTokenPref();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void clearScopeByResource(Request request) {
        String str = "clearScopeByResource";
        Logger.enter(getClass().getSimpleName(), str);
        this.resourceToScopeCache.remove(String.format("%s_%s", new Object[]{request.url().toString(), request.method()}));
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void invokeAuthorizationRequestForScope(final String str, final WLResponseListener wLResponseListener) {
        String str2 = "invokeAuthorizationRequestForScope";
        Logger.enter(getClass().getSimpleName(), str2);
        invokeAuthorizationRequest(str, new WLResponseListener() {
            public void onSuccess(WLResponse wLResponse) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.onAuthorizationSuccess(str, wLResponse, new WLResponseListener() {
                    public void onSuccess(WLResponse wLResponse) {
                        String str = "onSuccess";
                        Logger.enter(getClass().getSimpleName(), str);
                        try {
                            WLAuthorizationManagerInternal.this.onTokenSuccess(wLResponse);
                            wLResponseListener.onSuccess(wLResponse);
                        } catch (JSONException unused) {
                            onFailure(new WLFailResponse(wLResponse));
                        }
                        Logger.exit(getClass().getSimpleName(), str);
                    }

                    public void onFailure(WLFailResponse wLFailResponse) {
                        String str = "onFailure";
                        Logger.enter(getClass().getSimpleName(), str);
                        WLAuthorizationManagerInternal.this.onObtainAccessTokenFailure(str, wLFailResponse);
                        wLResponseListener.onFailure(wLFailResponse);
                        Logger.exit(getClass().getSimpleName(), str);
                    }
                });
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.onObtainAccessTokenFailure(str, wLFailResponse);
                wLResponseListener.onFailure(wLFailResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void cacheScopeByResource(Request request, String str) {
        String str2 = "cacheScopeByResource";
        Logger.enter(getClass().getSimpleName(), str2);
        if (!(request == null || str == null)) {
            cacheScopeByResource(request.url().toString(), request.method(), str);
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void cacheScopeByResource(String str, String str2, String str3) {
        String str4 = "cacheScopeByResource";
        Logger.enter(getClass().getSimpleName(), str4);
        this.resourceToScopeCache.put(String.format("%s_%s", new Object[]{str, str2}), sortScope(str3));
        Logger.exit(getClass().getSimpleName(), str4);
    }

    public String getScopeByResource(String str, String str2) {
        String str3 = "getScopeByResource";
        Logger.enter(getClass().getSimpleName(), str3);
        Logger.exit(getClass().getSimpleName(), str3);
        return (String) this.resourceToScopeCache.get(String.format("%s_%s", new Object[]{str, str2}));
    }

    public void invokeRegistrationRequest(WLRequestListener wLRequestListener) {
        Logger.enter(getClass().getSimpleName(), "invokeRegistrationRequest");
        synchronized (this.registrationQueueLock) {
            this.registrationQueue.add(wLRequestListener);
            invokeRegistrationRequest();
        }
        Logger.exit(getClass().getSimpleName(), "invokeRegistrationRequest");
    }

    public void login(String str, JSONObject jSONObject, WLLoginResponseListener wLLoginResponseListener) {
        Logger.enter(getClass().getSimpleName(), RequestPaths.LOGIN);
        synchronized (this.authorizationInProgressLock) {
            if (!this.authorizationInProgress) {
                logger.debug("Set authInProgress from login.");
                this.authorizationInProgress = true;
                if (shouldRegister()) {
                    loginWithRegistration(str, jSONObject, wLLoginResponseListener);
                } else {
                    loginWithoutRegistration(str, jSONObject, wLLoginResponseListener);
                }
            } else {
                wLLoginResponseListener.onFailure(new WLFailResponse(WLErrorCode.LOGIN_ALREADY_IN_PROCESS, WLErrorCode.LOGIN_ALREADY_IN_PROCESS.getDescription(), null));
            }
        }
        Logger.exit(getClass().getSimpleName(), RequestPaths.LOGIN);
    }

    public void logout(String str, WLLogoutResponseListener wLLogoutResponseListener) {
        Logger.enter(getClass().getSimpleName(), "logout");
        synchronized (this.authorizationInProgressLock) {
            if (str == null) {
                String str2 = "Security Check must be non-null";
                logger.debug(str2);
                wLLogoutResponseListener.onFailure(new WLFailResponse(WLErrorCode.AUTHORIZATION_FAILURE, str2, null));
            }
            if (!this.authorizationInProgress) {
                logger.debug("Set authInProgress from logout.");
                this.authorizationInProgress = true;
                if (getClientId() != null) {
                    sendLogoutRequest(str, wLLogoutResponseListener, getLogoutParams(str), createRelativePath(BASE_PREAUTH, API_VERSION, "logout"));
                } else {
                    String str3 = "Cannot logout before client is registered.";
                    logger.debug(str3);
                    logger.debug("Reset authInProgress from logout.");
                    this.authorizationInProgress = false;
                    wLLogoutResponseListener.onFailure(new WLFailResponse(WLErrorCode.AUTHORIZATION_FAILURE, str3, null));
                }
            } else {
                wLLogoutResponseListener.onFailure(new WLFailResponse(WLErrorCode.LOGOUT_ALREADY_IN_PROCESS, WLErrorCode.LOGOUT_ALREADY_IN_PROCESS.getDescription(), null));
            }
        }
        Logger.exit(getClass().getSimpleName(), "logout");
    }

    public AccessToken getCachedAccessToken(String str) {
        String str2 = "getCachedAccessToken";
        Logger.enter(getClass().getSimpleName(), str2);
        String sortScope = sortScope(str);
        AccessToken accessToken = (AccessToken) this.scopeToToken.get(sortScope);
        if (accessToken == null) {
            Logger.exit(getClass().getSimpleName(), str2);
            return null;
        } else if (accessToken.isValidToken()) {
            Logger.exit(getClass().getSimpleName(), str2);
            return accessToken;
        } else {
            removeTokenByScope(sortScope);
            Logger.exit(getClass().getSimpleName(), str2);
            return null;
        }
    }

    public String getCachedRefreshToken(String str) {
        String str2 = "getCachedRefreshToken";
        Logger.enter(getClass().getSimpleName(), str2);
        String sortScope = sortScope(str);
        String str3 = (String) this.scopeToRefreshToken.get(sortScope);
        if (str3 == null) {
            str3 = WLConfig.getInstance().readSecurityTokenPref(sortScope);
        }
        if (str3 != null) {
            this.scopeToRefreshToken.put(sortScope, str3);
            Logger.exit(getClass().getSimpleName(), str2);
            return str3;
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return null;
    }

    public void clearAccessToken(AccessToken accessToken) {
        String str = "clearAccessToken";
        Logger.enter(getClass().getSimpleName(), str);
        if (accessToken != null) {
            removeTokenByScope(accessToken.getScope());
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void removeTokenByScope(String str) {
        String str2 = "removeTokenByScope";
        Logger.enter(getClass().getSimpleName(), str2);
        this.scopeToToken.remove(sortScope(str));
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void setLoginTimeout(int i) {
        String str = "setLoginTimeout";
        Logger.enter(getClass().getSimpleName(), str);
        int i2 = i * 1000;
        if (i2 < MIN_LOGIN_TIMEOUT) {
            this.timeOut = MIN_LOGIN_TIMEOUT;
            Log.d(str, "Timeout set is less than default minimum. Resetting to default min 5000");
        } else {
            this.timeOut = i2;
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void setAuthorizationServerUrl(URL url) {
        String str = "setAuthorizationServerUrl";
        Logger.enter(getClass().getSimpleName(), str);
        this.authorizationServerUrl = url;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public String getAuthorizationServerRootPath() {
        String str = "getAuthorizationServerRootPath";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        URL url = this.authorizationServerUrl;
        return url != null ? url.toString() : WLConfig.getInstance().getRootURL();
    }

    public URL getAuthorizationServerRootPathAsURL() {
        String str = "getAuthorizationServerRootPathAsURL";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            Logger.exit(getClass().getSimpleName(), str);
            return this.authorizationServerUrl != null ? this.authorizationServerUrl : new URL(WLConfig.getInstance().getRootURL());
        } catch (MalformedURLException unused) {
            logger.debug("Failed to create URL from server root path");
            Logger.exit(getClass().getSimpleName(), str);
            return null;
        }
    }

    protected WLAuthorizationManagerInternal() {
        logger = Logger.getInstance(WLAuthorizationManagerInternal.class.getSimpleName());
        this.scopeToToken = Collections.synchronizedMap(new HashMap());
        this.scopeToRefreshToken = Collections.synchronizedMap(new HashMap());
        this.resourceToScopeCache = new LRUCache(100);
        this.registrationQueue = new ArrayList();
        this.obtainAccessTokenQueue = new WLAuthQueue();
        this.authorizationInProgress = false;
        enableRefreshToken = WLConfig.getInstance().isRefreshTokenEnabled();
    }

    /* access modifiers changed from: protected */
    public Map<String, Object> getRegistrationParams() throws Exception {
        JSONObject signRegistrationData = signRegistrationData(getRegistrationDataJson());
        HashMap hashMap = new HashMap();
        hashMap.put("signedRegistrationData", signRegistrationData);
        return hashMap;
    }

    private boolean isMfpConflict(Response response) {
        String str = "isMfpConflict";
        Logger.enter(getClass().getSimpleName(), str);
        if (response.code() != 409 || !response.headers().toMultimap().containsKey("MFP-Conflict")) {
            Logger.exit(getClass().getSimpleName(), str);
            return false;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return true;
    }

    private Boolean isAuthorizationRequired(List<String> list) {
        String str = "isAuthorizationRequired";
        Logger.enter(getClass().getSimpleName(), str);
        if (list.size() == 0) {
            Logger.exit(getClass().getSimpleName(), str);
            return Boolean.valueOf(false);
        }
        Iterator it = list.iterator();
        if (it.hasNext()) {
            if (((String) it.next()).contains("Bearer")) {
                Logger.exit(getClass().getSimpleName(), str);
            }
            return Boolean.valueOf(true);
        }
        Logger.exit(getClass().getSimpleName(), str);
        return null;
    }

    private void sendLogoutRequest(final String str, final WLLogoutResponseListener wLLogoutResponseListener, Map<String, Object> map, String str2) {
        String str3 = "sendLogoutRequest";
        Logger.enter(getClass().getSimpleName(), str3);
        sendRequest(str2, map, null, RequestMethod.POST, false, true, new WLResponseListener() {
            public void onSuccess(WLResponse wLResponse) {
                Logger.enter(getClass().getSimpleName(), "onSuccess");
                synchronized (WLAuthorizationManagerInternal.this.authorizationInProgressLock) {
                    WLAuthorizationManagerInternal.this.scopeToToken.clear();
                    WLAuthorizationManagerInternal.this.scopeToRefreshToken.clear();
                    WLConfig.getInstance().clearSecurityTokenPref();
                    Logger access$000 = WLAuthorizationManagerInternal.logger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Logged out successfully from securityCheck");
                    sb.append(str);
                    access$000.debug(sb.toString());
                    WLAuthorizationManagerInternal.logger.debug("Reset authInProgress from logout success.");
                    WLAuthorizationManagerInternal.this.authorizationInProgress = false;
                    wLLogoutResponseListener.onSuccess();
                }
                WLAuthorizationManagerInternal.this.invokeNextAuthorizationRequest();
                Logger.exit(getClass().getSimpleName(), "onSuccess");
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.onLogoutFailure(wLFailResponse, wLLogoutResponseListener);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str3);
    }

    private Map<String, Object> getLogoutParams(String str) {
        String str2;
        HashMap hashMap = new HashMap();
        try {
            str2 = WLOAuthCertManager.getInstance().signJWS(new JWT().toJson(), getClientId());
        } catch (Exception e) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("unable to convert JWT to json");
            sb.append(e.getMessage());
            logger2.debug(sb.toString());
            str2 = null;
        }
        hashMap.put(PARAM_SECURITY_CHECK_KEY, str);
        hashMap.put("client_assertion", str2);
        hashMap.put("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
        return hashMap;
    }

    private void setClientRegisteredData() throws JSONException {
        String str = "setClientRegisteredData";
        Logger.enter(getClass().getSimpleName(), str);
        WLConfig.getInstance().writeSecurityPref(CLIENT_REGISTRATION_DATA_KEY, String.valueOf(WLConfig.getInstance().getClientData()));
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean shouldRegister() {
        String str = "shouldRegister";
        Logger.enter(getClass().getSimpleName(), str);
        boolean z = true;
        if (getClientId() == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return true;
        }
        String readSecurityPref = WLConfig.getInstance().readSecurityPref(CLIENT_REGISTRATION_DATA_KEY);
        try {
            String valueOf = String.valueOf(WLConfig.getInstance().getClientData());
            Logger.exit(getClass().getSimpleName(), str);
            if (readSecurityPref != null && readSecurityPref.equalsIgnoreCase(valueOf)) {
                z = false;
            }
            return z;
        } catch (JSONException unused) {
            Logger.exit(getClass().getSimpleName(), str);
            throw new Error("Could not get current device data");
        }
    }

    /* access modifiers changed from: private */
    public void addToObtainAccessTokenQueue(String str, WLResponseListener wLResponseListener) {
        String str2 = "addToObtainAccessTokenQueue";
        Logger.enter(getClass().getSimpleName(), str2);
        this.obtainAccessTokenQueue.addToAuthorizationQueue(sortScope(str), wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: private */
    public void invokeRegistrationRequest() {
        String str = "invokeRegistrationRequest";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.registrationQueue.size() == 1) {
            try {
                Map registrationParams = getRegistrationParams();
                String createRelativePath = createRelativePath(BASE_REGISTRATION, API_VERSION, PATH_REGISTER_SELF);
                RequestMethod requestMethod = RequestMethod.POST;
                if (getClientId() != null && !getClientId().isEmpty()) {
                    logger.debug("Client exists, application data has changed, call updateRegistration endpoint");
                    requestMethod = RequestMethod.PUT;
                    this.isUpdateRegistrationRequest = true;
                    StringBuilder sb = new StringBuilder();
                    sb.append(createRelativePath);
                    sb.append("/");
                    sb.append(getClientId());
                    createRelativePath = sb.toString();
                }
                sendRequest(createRelativePath, registrationParams, null, requestMethod, true, false, new WLResponseListener() {
                    public void onSuccess(WLResponse wLResponse) {
                        Logger.enter(getClass().getSimpleName(), "onSuccess");
                        synchronized (WLAuthorizationManagerInternal.this.registrationQueueLock) {
                            try {
                                WLAuthorizationManagerInternal.this.onRegistrationSuccess(wLResponse);
                            } catch (Exception e) {
                                WLAuthorizationManagerInternal.logger.error("Unable to finish client instance registration process. ", (Throwable) e);
                                onFailure(new WLFailResponse(wLResponse));
                            }
                            for (WLRequestListener onSuccess : WLAuthorizationManagerInternal.this.registrationQueue) {
                                onSuccess.onSuccess(wLResponse);
                            }
                            WLAuthorizationManagerInternal.this.registrationQueue.clear();
                        }
                        Logger.exit(getClass().getSimpleName(), "onSuccess");
                    }

                    /* JADX WARNING: Removed duplicated region for block: B:19:0x0064 A[LOOP:0: B:17:0x005e->B:19:0x0064, LOOP_END] */
                    public void onFailure(WLFailResponse wLFailResponse) {
                        Logger.enter(getClass().getSimpleName(), "onFailure");
                        synchronized (WLAuthorizationManagerInternal.this.registrationQueueLock) {
                            if (!WLAuthorizationManagerInternal.this.isUpdateRegistrationRequest) {
                                if (!wLFailResponse.getErrorCode().equals(WLErrorCode.APPLICATION_DISABLED)) {
                                    WLAuthorizationManagerInternal.this.clearRegistration();
                                    WLAuthorizationManagerInternal.this.onRegistrationFailure(wLFailResponse);
                                    for (WLRequestListener onFailure : WLAuthorizationManagerInternal.this.registrationQueue) {
                                        onFailure.onFailure(wLFailResponse);
                                    }
                                    WLAuthorizationManagerInternal.this.registrationQueue.clear();
                                    Logger.exit(getClass().getSimpleName(), "onFailure");
                                }
                            }
                            if (wLFailResponse.getStatus() == 403 && wLFailResponse.getErrorMsg().contains("Invalid request.")) {
                                WLAuthorizationManagerInternal.this.clearRegistration();
                                WLAuthorizationManagerInternal.this.invokeRegistrationRequest();
                                return;
                            }
                            WLAuthorizationManagerInternal.this.onRegistrationFailure(wLFailResponse);
                            while (r1.hasNext()) {
                            }
                            WLAuthorizationManagerInternal.this.registrationQueue.clear();
                            Logger.exit(getClass().getSimpleName(), "onFailure");
                        }
                    }
                });
            } catch (Exception e) {
                Logger.exit(getClass().getSimpleName(), str);
                throw new Error(e);
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    private JSONObject signRegistrationData(JSONObject jSONObject) throws Exception {
        String str = "signRegistrationData";
        Logger.enter(getClass().getSimpleName(), str);
        WLOAuthCertManager.getInstance().generateKeyPair();
        String[] split = WLOAuthCertManager.getInstance().signJWS(jSONObject, null).split("\\.");
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("header", split[0]);
        jSONObject2.put("payload", split[1]);
        jSONObject2.put("signature", split[2]);
        Logger.exit(getClass().getSimpleName(), str);
        return jSONObject2;
    }

    private JSONObject getRegistrationDataJson() throws JSONException {
        String str = "getRegistrationDataJson";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("device", WLDeviceAuthManager.getInstance().getDeviceData());
        jSONObject.put("application", WLConfig.getInstance().getApplicationData());
        jSONObject.put("attributes", WLConfig.getInstance().getRegistrationAttributesData());
        Logger.exit(getClass().getSimpleName(), str);
        return jSONObject;
    }

    /* access modifiers changed from: private */
    public void onRegistrationFailure(WLFailResponse wLFailResponse) {
        String str = "onRegistrationFailure";
        Logger.enter(getClass().getSimpleName(), str);
        abortAuthorization(wLFailResponse);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private boolean isUnknownClientError(WLResponse wLResponse) {
        String str = "isUnknownClientError";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject responseJSON = wLResponse.getResponseJSON();
        if (responseJSON != null) {
            try {
                if (wLResponse.getStatus() == 400 && responseJSON.getString(JSON_ERROR_KEY).equals("INVALID_CLIENT_ID")) {
                    Logger.exit(getClass().getSimpleName(), str);
                    return true;
                }
            } catch (JSONException unused) {
                logger.debug("couldn't get: errorCode from response's JSON");
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return false;
    }

    /* access modifiers changed from: private */
    public void onLoginFailure(String str, JSONObject jSONObject, WLFailResponse wLFailResponse, WLLoginResponseListener wLLoginResponseListener) {
        String str2 = "onLoginFailure";
        Logger.enter(getClass().getSimpleName(), str2);
        if (!isUnknownClientError(wLFailResponse) || !this.shouldCallRegistrationAfterUnknownClientError) {
            wLLoginResponseListener.onFailure(wLFailResponse);
            logger.debug("Reset authInProgress from login failure.");
            this.authorizationInProgress = false;
            invokeNextAuthorizationRequest();
        } else {
            this.shouldCallRegistrationAfterUnknownClientError = false;
            logger.error("Client instance registration information is incorrect, attempting to re-register client instance.");
            clearRegistration();
            loginWithRegistration(str, jSONObject, wLLoginResponseListener);
        }
        Logger logger2 = logger;
        StringBuilder sb = new StringBuilder();
        sb.append("Login failed with status code: ");
        sb.append(wLFailResponse.getStatus());
        sb.append(" and error message: ");
        sb.append(wLFailResponse.getErrorMsg());
        logger2.debug(sb.toString());
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: private */
    public void onLogoutFailure(WLFailResponse wLFailResponse, WLLogoutResponseListener wLLogoutResponseListener) {
        String str = "onLogoutFailure";
        Logger.enter(getClass().getSimpleName(), str);
        if (!isUnknownClientError(wLFailResponse) || !this.shouldCallRegistrationAfterUnknownClientError) {
            logger.debug("Reset authInProgress from logout failure 2.");
            this.authorizationInProgress = false;
            invokeNextAuthorizationRequest();
        } else {
            this.shouldCallRegistrationAfterUnknownClientError = false;
            logger.error("Client instance registration information is incorrect.");
            clearRegistration();
            logger.debug("Reset authInProgress from logout failure 1.");
            this.authorizationInProgress = false;
        }
        Logger logger2 = logger;
        StringBuilder sb = new StringBuilder();
        sb.append("Logout failed with status code: ");
        sb.append(wLFailResponse.getStatus());
        sb.append(" and error message: ");
        sb.append(wLFailResponse.getErrorMsg());
        logger2.debug(sb.toString());
        wLLogoutResponseListener.onFailure(wLFailResponse);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private String getErrorDescription(WLFailResponse wLFailResponse, WLErrorCode wLErrorCode) {
        Logger.enter(getClass().getSimpleName(), "getErrorDescription");
        String str = "Location";
        if (wLFailResponse.getHeader(str).size() == 0) {
            return wLFailResponse.getResponseText();
        }
        String str2 = (String) WLUtils.extractParametersFromURL((String) wLFailResponse.getHeader(str).get(0)).get(JSON_ERROR_DESCRIPTION_KEY);
        if (str2 == null) {
            str2 = wLErrorCode.getDescription();
        }
        new WLFailResponse(wLErrorCode, str2, null);
        return str2;
    }

    /* access modifiers changed from: private */
    public void onObtainAccessTokenFailure(final String str, WLFailResponse wLFailResponse) {
        String str2 = "onObtainAccessTokenFailure";
        Logger.enter(getClass().getSimpleName(), str2);
        String errorMsg = wLFailResponse.getErrorMsg();
        if (!isUnknownClientError(wLFailResponse) || !this.shouldCallRegistrationAfterUnknownClientError) {
            if (errorMsg == null) {
                errorMsg = getErrorDescription(wLFailResponse, WLErrorCode.AUTHORIZATION_FAILURE);
            }
            this.obtainAccessTokenQueue.releaseQueueOnFailure(sortScope(str), wLFailResponse);
            invokeNextAuthorizationRequest();
        } else {
            this.shouldCallRegistrationAfterUnknownClientError = false;
            logger.error("Client instance registration information is incorrect, attempting to re-register client instance.");
            clearRegistration();
            invokeRegistrationRequest(new WLRequestListener() {
                public void onSuccess(WLResponse wLResponse) {
                    String str = "onSuccess";
                    Logger.enter(getClass().getSimpleName(), str);
                    WLAuthorizationManagerInternal.this.obtainAccessTokenWithPreAZ(str);
                    Logger.exit(getClass().getSimpleName(), str);
                }

                public void onFailure(WLFailResponse wLFailResponse) {
                    String str = "onFailure";
                    Logger.enter(getClass().getSimpleName(), str);
                    WLAuthorizationManagerInternal.this.obtainAccessTokenQueue.releaseQueueOnFailure(WLAuthorizationManagerInternal.this.sortScope(str), wLFailResponse);
                    WLAuthorizationManagerInternal.this.invokeNextAuthorizationRequest();
                    Logger.exit(getClass().getSimpleName(), str);
                }
            });
        }
        Logger logger2 = logger;
        StringBuilder sb = new StringBuilder();
        sb.append("Obtain AccessToken failed with status code: ");
        sb.append(wLFailResponse.getStatus());
        sb.append(" and error message: ");
        sb.append(errorMsg);
        logger2.debug(sb.toString());
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: private */
    public void onRegistrationSuccess(WLResponse wLResponse) throws Exception {
        String str = "AnalyticsUserPassword";
        String str2 = "AnalyticsUserName";
        String str3 = "AnalyticsAPIKey";
        String str4 = "AnalyticsURL";
        String str5 = "onRegistrationSuccess";
        Logger.enter(getClass().getSimpleName(), str5);
        if (wLResponse.getStatus() == 200) {
            logger.debug("Registration update success");
        } else {
            String firstHeader = wLResponse.getFirstHeader("Location");
            if (firstHeader != null) {
                if (firstHeader.contains(String.format("%s/%s/%s", new Object[]{WLConfig.getInstance().getServerContext(), BASE_REGISTRATION, PATH_REGISTER_CLIENTS_ON_SUCCESS}))) {
                    String[] split = firstHeader.split("/");
                    String str6 = split[split.length - 1];
                    this.clientId = str6;
                    WLConfig.getInstance().writeSecurityPref(CLIENT_ID_OAUTH_LABEL, str6);
                    try {
                        JSONObject responseJSON = wLResponse.getResponseJSON();
                        String str7 = "";
                        String str8 = responseJSON.has(str4) ? (String) responseJSON.get(str4) : str7;
                        String str9 = responseJSON.has(str3) ? (String) responseJSON.get(str3) : str7;
                        String str10 = responseJSON.has(str2) ? (String) responseJSON.get(str2) : str7;
                        String str11 = responseJSON.has(str) ? (String) responseJSON.get(str) : str7;
                        if (str8 != str7) {
                            WLConfig.getInstance().writeSecurityPref(ANALYTICS_URL_KEY, str8);
                        }
                        if (str9 != str7) {
                            WLConfig.getInstance().writeSecurityPref(ANALYTICS_API_KEY, str9);
                        }
                        if (str10 != str7) {
                            WLConfig.getInstance().writeSecurityPref(ANALYTICS_USERNAME, str10);
                        }
                        if (str11 != str7) {
                            WLConfig.getInstance().writeSecurityPref(ANALYTICS_PASSWORD, str11);
                        }
                    } catch (JSONException unused) {
                    }
                } else {
                    Logger.exit(getClass().getSimpleName(), str5);
                    throw new Exception("Registration Response failure - Incorrect Location Header");
                }
            } else {
                Logger.exit(getClass().getSimpleName(), str5);
                throw new Exception("Registration Response failure - Missing Location Header");
            }
        }
        setClientRegisteredData();
        Logger.exit(getClass().getSimpleName(), str5);
    }

    private void invokeTokenRequest(String str, WLResponseListener wLResponseListener, String str2) {
        String str3 = str;
        String str4 = "refresh_token";
        String str5 = PARAM_GET_REFRESH_TOKEN_KEY;
        String str6 = "invokeTokenRequest";
        Logger.enter(getClass().getSimpleName(), str6);
        try {
            String createRelativePath = createRelativePath(BASE_AUTHORIZATION, API_VERSION, PATH_OAUTH_TOKEN);
            String authorizationServerRootPath = getAuthorizationServerRootPath();
            JWT jwt = new JWT();
            StringBuilder sb = new StringBuilder();
            sb.append(authorizationServerRootPath);
            sb.append("/");
            sb.append(createRelativePath);
            jwt.aud = sb.toString();
            jwt.jti = str3;
            String signJWS = WLOAuthCertManager.getInstance().signJWS(jwt.toJson(), getClientId());
            HashMap hashMap = new HashMap();
            boolean equals = str5.equals(str3);
            String str7 = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
            String str8 = "client_assertion_type";
            String str9 = "://mfpredirecturi";
            String str10 = PARAM_REDIRECT_URI_KEY;
            String str11 = PARAM_GRANT_TYPE_KEY;
            String str12 = PARAM_CLIENT_ID_KEY;
            String str13 = "client_assertion";
            if (equals) {
                hashMap.put(str13, signJWS);
                hashMap.put(str12, getClientId());
                hashMap.put(str11, str4);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(WLConfig.getInstance().getProtocol());
                sb2.append(str9);
                hashMap.put(str10, sb2.toString());
                hashMap.put(str8, str7);
                hashMap.put(str4, str2);
            } else {
                hashMap.put(str13, signJWS);
                hashMap.put("code", str3);
                hashMap.put(str12, getClientId());
                hashMap.put(str11, PARAM_AUTHORIZATION_CODE_VALUE);
                StringBuilder sb3 = new StringBuilder();
                sb3.append(WLConfig.getInstance().getProtocol());
                sb3.append(str9);
                hashMap.put(str10, sb3.toString());
                hashMap.put(str8, str7);
                if (enableRefreshToken) {
                    hashMap.put(str5, PARAM_GET_REFRESH_TOKEN_VALUE);
                }
            }
            sendRequest(createRelativePath, hashMap, null, RequestMethod.POST, false, true, wLResponseListener);
            Logger.exit(getClass().getSimpleName(), str6);
        } catch (Exception e) {
            Logger.exit(getClass().getSimpleName(), str6);
            throw new Error(e);
        }
    }

    /* access modifiers changed from: private */
    public void onTokenSuccess(WLResponse wLResponse) throws JSONException {
        String str = "onTokenSuccess";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject responseJSON = wLResponse.getResponseJSON();
        if (responseJSON != null) {
            AccessToken accessToken = new AccessToken(responseJSON);
            String sortScope = sortScope(accessToken.getScope());
            this.scopeToToken.put(sortScope, accessToken);
            if (enableRefreshToken) {
                String string = responseJSON.getString("refresh_token");
                this.scopeToRefreshToken.put(sortScope, string);
                WLConfig.getInstance().writeSecurityTokenPref(sortScope, string);
            }
            this.obtainAccessTokenQueue.releaseQueueOnSuccess(sortScope, wLResponse);
            invokeNextAuthorizationRequest();
            Logger.exit(getClass().getSimpleName(), str);
            return;
        }
        Logger.exit(getClass().getSimpleName(), str);
        throw new JSONException("response.getResponseJSON() returned null");
    }

    private void abortAuthorization(WLFailResponse wLFailResponse) {
        Logger.enter(getClass().getSimpleName(), "abortAuthorization");
        synchronized (this.authorizationInProgressLock) {
            this.obtainAccessTokenQueue.abortAuthorization(wLFailResponse);
            logger.debug("Reset authInProgress from abort authorization.");
            this.authorizationInProgress = false;
        }
        Logger.exit(getClass().getSimpleName(), "abortAuthorization");
    }

    /* access modifiers changed from: private */
    public void invokeNextAuthorizationRequest() {
        Logger.enter(getClass().getSimpleName(), "invokeNextAuthorizationRequest");
        synchronized (this.authorizationInProgressLock) {
            if (!this.obtainAccessTokenQueue.isEmpty()) {
                String nextScopeToObtain = this.obtainAccessTokenQueue.getNextScopeToObtain();
                if (nextScopeToObtain != null) {
                    obtainAccessTokenWithoutRegistration(nextScopeToObtain, (WLResponseListener) this.obtainAccessTokenQueue.get(nextScopeToObtain).get(0));
                }
            } else {
                logger.debug("Reset authInProgress from invokeNextAuthorizationRequest.");
                this.authorizationInProgress = false;
            }
        }
        Logger.exit(getClass().getSimpleName(), "invokeNextAuthorizationRequest");
    }

    /* access modifiers changed from: private */
    public String sortScope(String str) {
        String str2 = "sortScope";
        Logger.enter(getClass().getSimpleName(), str2);
        String str3 = WLConstants.DEFAULT_SCOPE;
        if (str == null) {
            Logger.exit(getClass().getSimpleName(), str2);
            return str3;
        }
        if (!str.contains(str3)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" RegisteredClient");
            str = sb.toString();
        }
        String[] split = str.split("\\s+");
        Arrays.sort(split);
        StringBuilder sb2 = new StringBuilder();
        for (String append : split) {
            sb2.append(append);
            sb2.append(" ");
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return sb2.toString().trim();
    }

    private HashMap<String, Object> getAuthorizationsParams(String str) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(PARAM_RESPONSE_TYPE_KEY, "code");
        hashMap.put(PARAM_CLIENT_ID_KEY, getClientId());
        StringBuilder sb = new StringBuilder();
        sb.append(WLConfig.getInstance().getProtocol());
        sb.append("://mfpredirecturi");
        hashMap.put(PARAM_REDIRECT_URI_KEY, sb.toString());
        if (str == null) {
            str = WLConstants.DEFAULT_SCOPE;
        }
        hashMap.put("scope", str);
        return hashMap;
    }

    private HashMap<String, Object> getPreAuthorizationsParams(String str, JSONObject jSONObject) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(PARAM_CLIENT_ID_KEY, getClientId());
        if (str == null) {
            str = WLConstants.DEFAULT_SCOPE;
        }
        hashMap.put("scope", str);
        if (jSONObject != null) {
            try {
                hashMap.put("challengeResponse", new JSONObject().put(str, jSONObject));
            } catch (JSONException unused) {
                logger.debug("Failed to create JSONObject with credentials");
            }
        }
        return hashMap;
    }

    private void invokePreAuthorizationRequest(String str, JSONObject jSONObject, WLResponseListener wLResponseListener) {
        String str2 = "invokePreAuthorizationRequest";
        Logger.enter(getClass().getSimpleName(), str2);
        sendRequest(createRelativePath(BASE_PREAUTH, API_VERSION, PATH_PREAUTHORIZE), getPreAuthorizationsParams(str, jSONObject), null, RequestMethod.POST, true, false, wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void invokeAuthorizationRequest(String str, WLResponseListener wLResponseListener) {
        String str2 = "invokeAuthorizationRequest";
        Logger.enter(getClass().getSimpleName(), str2);
        sendRequest(createRelativePath(BASE_AUTHORIZATION, API_VERSION, PATH_OAUTH_AUTHORIZATION), getAuthorizationsParams(str), null, RequestMethod.GET, false, true, wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private String createRelativePath(String str, String str2, String str3) {
        String str4 = "createRelativePath";
        Logger.enter(getClass().getSimpleName(), str4);
        Logger.exit(getClass().getSimpleName(), str4);
        return String.format("%s/%s/%s", new Object[]{str, str2, str3});
    }

    private void sendRequest(String str, Map<String, Object> map, HashMap<String, String> hashMap, RequestMethod requestMethod, boolean z, boolean z2, final WLResponseListener wLResponseListener) {
        WLRequest wLRequest;
        String str2 = "sendRequest";
        Logger.enter(getClass().getSimpleName(), str2);
        WLRequestOptions wLRequestOptions = new WLRequestOptions();
        for (String str3 : map.keySet()) {
            wLRequestOptions.addParameter(str3, map.get(str3).toString());
        }
        if (hashMap != null) {
            for (String str4 : hashMap.keySet()) {
                wLRequestOptions.addHeader(str4, (String) hashMap.get(str4));
            }
        }
        int i = this.timeOut;
        if (i > 0) {
            wLRequestOptions.setTimeout(i);
        }
        Map globalHeaders = AsynchronousRequestSender.getInstance().getGlobalHeaders();
        if (globalHeaders != null) {
            for (String str5 : globalHeaders.keySet()) {
                wLRequestOptions.addHeader(str5, (String) globalHeaders.get(str5));
            }
        }
        wLRequestOptions.setJsonContentType(z);
        wLRequestOptions.setAZRequest(z2);
        wLRequestOptions.setResponseListener(wLResponseListener);
        AnonymousClass8 r3 = new WLRequestListener() {
            public void onSuccess(WLResponse wLResponse) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                wLResponseListener.onSuccess(wLResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                wLResponseListener.onFailure(wLFailResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }
        };
        if (getClientId() != null) {
            wLRequest = new WLRequest(r3, wLRequestOptions, WLConfig.getInstance(), WLClient.getInstance().getContext(), true);
        } else {
            wLRequest = new WLClientInstanceRegistrationRequest(r3, wLRequestOptions, WLConfig.getInstance(), WLClient.getInstance().getContext());
        }
        wLRequest.setMethod(requestMethod);
        wLRequest.makeRequest(str, true);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: private */
    public void obtainAccessTokenWithPreAZ(final String str) {
        String str2 = "obtainAccessTokenWithPreAZ";
        Logger.enter(getClass().getSimpleName(), str2);
        invokePreAuthorizationRequest(str, null, new WLResponseListener() {
            public void onSuccess(WLResponse wLResponse) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.logger.debug("Finished pre-authorization process - Sending request to Authorization Endpoint");
                WLAuthorizationManagerInternal.this.invokeAuthorizationRequest(str, new WLAuthorizationRequestListener(str));
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.onObtainAccessTokenFailure(str, wLFailResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void obtainAccessTokenWithRegistration(final String str, final WLResponseListener wLResponseListener) {
        String str2 = "obtainAccessTokenWithRegistration";
        Logger.enter(getClass().getSimpleName(), str2);
        invokeRegistrationRequest(new WLRequestListener() {
            public void onSuccess(WLResponse wLResponse) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.addToObtainAccessTokenQueue(str, wLResponseListener);
                WLAuthorizationManagerInternal.this.obtainAccessTokenWithPreAZ(str);
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                Logger access$000 = WLAuthorizationManagerInternal.logger;
                StringBuilder sb = new StringBuilder();
                sb.append("Authorization failed with status code: ");
                sb.append(wLFailResponse.getStatus());
                sb.append(" and error message: ");
                sb.append(wLFailResponse.getErrorMsg());
                access$000.debug(sb.toString());
                wLResponseListener.onFailure(wLFailResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: private */
    public void onAuthorizationSuccess(String str, WLResponse wLResponse, WLResponseListener wLResponseListener) {
        String str2 = "onAuthorizationSuccess";
        Logger.enter(getClass().getSimpleName(), str2);
        String str3 = (String) wLResponse.getHeader("Location").get(0);
        if (str3 == null) {
            onObtainAccessTokenFailure(str, new WLFailResponse(wLResponse));
        } else {
            String str4 = (String) WLUtils.extractParametersFromURL(str3).get("code");
            if (str4 == null) {
                onObtainAccessTokenFailure(str, new WLFailResponse(wLResponse));
            } else {
                invokeTokenRequest(str4, wLResponseListener, null);
            }
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void loginWithRegistration(final String str, final JSONObject jSONObject, final WLLoginResponseListener wLLoginResponseListener) {
        String str2 = "loginWithRegistration";
        Logger.enter(getClass().getSimpleName(), str2);
        invokeRegistrationRequest(new WLRequestListener() {
            public void onSuccess(WLResponse wLResponse) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.loginWithoutRegistration(str, jSONObject, wLLoginResponseListener);
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.onLoginFailure(str, jSONObject, wLFailResponse, wLLoginResponseListener);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: private */
    public void loginWithoutRegistration(final String str, final JSONObject jSONObject, final WLLoginResponseListener wLLoginResponseListener) {
        String str2 = "loginWithoutRegistration";
        Logger.enter(getClass().getSimpleName(), str2);
        invokePreAuthorizationRequest(str, jSONObject, new WLResponseListener() {
            public void onSuccess(WLResponse wLResponse) {
                Logger.enter(getClass().getSimpleName(), "onSuccess");
                synchronized (WLAuthorizationManagerInternal.this.authorizationInProgressLock) {
                    WLAuthorizationManagerInternal.logger.debug("Succeeded to login to securityCheck");
                    wLLoginResponseListener.onSuccess();
                    WLAuthorizationManagerInternal.logger.debug("Reset authInProgress from login without reg success.");
                    WLAuthorizationManagerInternal.this.authorizationInProgress = false;
                }
                WLAuthorizationManagerInternal.this.invokeNextAuthorizationRequest();
                Logger.exit(getClass().getSimpleName(), "onSuccess");
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                WLAuthorizationManagerInternal.this.onLoginFailure(str, jSONObject, wLFailResponse, wLLoginResponseListener);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void obtainAccessTokenWithoutRegistration(String str, WLResponseListener wLResponseListener) {
        String str2 = "obtainAccessTokenWithoutRegistration";
        Logger.enter(getClass().getSimpleName(), str2);
        addToObtainAccessTokenQueue(str, wLResponseListener);
        obtainAccessTokenWithPreAZ(str);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private String getAuthorizationScope(List list) {
        String str = "getAuthorizationScope";
        Logger.enter(getClass().getSimpleName(), str);
        if (list == null) {
            return null;
        }
        if (list.size() <= 1) {
            String str2 = (String) list.get(0);
            Logger.exit(getClass().getSimpleName(), str);
            return getAuthorizationScope(str2);
        }
        Logger.exit(getClass().getSimpleName(), str);
        throw new Error("Multiple values for 'WWW-Authenticate' header were detected");
    }

    private String getAuthorizationScope(String str) {
        String str2 = "getAuthorizationScope";
        Logger.enter(getClass().getSimpleName(), str2);
        int indexOf = str.indexOf("scope=");
        String replaceAll = indexOf >= 0 ? str.substring(indexOf + 6).replaceAll(Pattern.quote("\""), "") : null;
        Logger.exit(getClass().getSimpleName(), str2);
        return replaceAll;
    }

    public void invokeGetRegistrationDataRequest(final WLRequestListener wLRequestListener) {
        String str = "invokeGetRegistrationDataRequest";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            String clientId2 = getClientId();
            if (clientId2 == null) {
                wLRequestListener.onFailure(new WLFailResponse(WLErrorCode.APPLICATION_NOT_REGISTERED, "application is not registered yet", null));
                Logger.exit(getClass().getSimpleName(), str);
                return;
            }
            String signJWS = WLOAuthCertManager.getInstance().signJWS(new JWT().toJson(), clientId2);
            HashMap hashMap = new HashMap();
            hashMap.put("client_assertion", signJWS);
            hashMap.put("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
            StringBuilder sb = new StringBuilder();
            sb.append(createRelativePath(BASE_REGISTRATION, API_VERSION, PATH_REGISTER_SELF));
            sb.append("/");
            sb.append(clientId2);
            sendRequest(sb.toString(), hashMap, null, RequestMethod.GET, true, false, new WLResponseListener() {
                public void onSuccess(WLResponse wLResponse) {
                    String str = "onSuccess";
                    Logger.enter(getClass().getSimpleName(), str);
                    wLRequestListener.onSuccess(wLResponse);
                    Logger.exit(getClass().getSimpleName(), str);
                }

                public void onFailure(WLFailResponse wLFailResponse) {
                    String str = "onFailure";
                    Logger.enter(getClass().getSimpleName(), str);
                    wLRequestListener.onFailure(wLFailResponse);
                    Logger.exit(getClass().getSimpleName(), str);
                }
            });
            Logger.exit(getClass().getSimpleName(), str);
        } catch (Exception e) {
            Logger.exit(getClass().getSimpleName(), str);
            throw new Error(e);
        }
    }
}
