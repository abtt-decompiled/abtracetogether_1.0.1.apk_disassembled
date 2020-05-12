package com.worklight.wlclient.api;

import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.wlclient.AsynchronousRequestSender;
import com.worklight.wlclient.HttpClientManager;
import com.worklight.wlclient.RequestMethod;
import com.worklight.wlclient.WLNativeAPIUtils;
import com.worklight.wlclient.auth.AccessToken;
import com.worklight.wlclient.auth.WLAuthorizationManagerInternal;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;
import org.json.JSONObject;

public class WLResourceRequest {
    public static final String BEARER_ERROR_INVALID_TOKEN = "invalid_token";
    private static final String BINARY_CONTENT_TYPE = "application/octet-stream";
    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static final int DEFAULT_TIMEOUT = 30000;
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String HEAD = "HEAD";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final int MAX_RETRY_COUNT = 5;
    private static final int NUMBER_OF_ALLOWED_REPETITIONS = 7;
    public static final String OPTIONS = "OPTIONS";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    private static final String STRING_CONTENT_TYPE = "text/plain";
    public static final String TRACE = "TRACE";
    private static final String URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getInstance("wl.resource_request");
    private int conflictFailCounter;
    private RequestMethod method;
    private Request okRequest;
    private Map<String, List<String>> queryParameters;
    /* access modifiers changed from: private */
    public String scope;
    private int timeout;
    private URL url;

    private class ObtainAccessTokenListener implements WLAccessTokenListener {
        private int attempt;
        private Request okRequest;
        private WLResponseListener wlListener;

        public ObtainAccessTokenListener(Request request, WLResponseListener wLResponseListener, int i) {
            this.okRequest = request;
            this.wlListener = wLResponseListener;
            this.attempt = i;
        }

        public void onSuccess(AccessToken accessToken) {
            String str = "onSuccess";
            Logger.enter(getClass().getSimpleName(), str);
            this.okRequest = WLAuthorizationManagerInternal.getInstance().addAuthorizationHeader(this.okRequest, accessToken.getAsAuthorizationRequestHeader());
            WLResourceRequest.this.executeRequest(this.okRequest, WLResourceRequest.this.getOkHttpClient(), this.wlListener, this.attempt);
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onFailure(WLFailResponse wLFailResponse) {
            String str = "onFailure";
            Logger.enter(getClass().getSimpleName(), str);
            Logger access$400 = WLResourceRequest.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Resource request failed with status:");
            sb.append(wLFailResponse.getStatus());
            sb.append(" and error: ");
            sb.append(wLFailResponse.getErrorMsg());
            access$400.debug(sb.toString());
            if (wLFailResponse.getStatus() == 500) {
                WLResourceRequest.this.scope = null;
                WLAuthorizationManagerInternal.getInstance().clearScopeByResource(this.okRequest);
            }
            this.wlListener.onFailure(wLFailResponse);
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    public WLResourceRequest(URI uri, String str) {
        this(uri, str, 30000);
    }

    public WLResourceRequest(URI uri, String str, String str2) {
        this(uri, str, 30000, str2);
    }

    public WLResourceRequest(URI uri, String str, int i) {
        initFields(uri, str, i);
    }

    public WLResourceRequest(URI uri, String str, int i, String str2) {
        initFields(uri, str, i);
        this.scope = str2;
    }

    private void initFields(URI uri, String str, int i) {
        String str2 = "initFields";
        Logger.enter(getClass().getSimpleName(), str2);
        if (uri == null || uri.toString() == null || uri.toString().isEmpty()) {
            try {
                uri = new URI("/");
            } catch (URISyntaxException unused) {
                Logger.exit(getClass().getSimpleName(), str2);
                StringBuilder sb = new StringBuilder();
                sb.append("'");
                sb.append(uri);
                sb.append("' is not a valid relative or absolute URL.");
                throw new IllegalStateException(sb.toString());
            }
        }
        makeAbsoluteUrl(extractQueryParamsFromUrl(uri));
        setMethod(str);
        this.timeout = i;
        this.okRequest = new Builder().url(getUrl()).build();
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public URL getUrl() {
        String str = "getUrl";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.url;
    }

    public String getMethod() {
        String str = "getMethod";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.method.toString();
    }

    private void setMethod(String str) {
        String str2 = "setMethod";
        Logger.enter(getClass().getSimpleName(), str2);
        RequestMethod fromSring = RequestMethod.fromSring(str);
        if (fromSring != null) {
            this.method = fromSring;
            Logger.exit(getClass().getSimpleName(), str2);
            return;
        }
        Logger.exit(getClass().getSimpleName(), str2);
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(str);
        sb.append("' is not a valid HTTP method verb.");
        throw new IllegalArgumentException(sb.toString());
    }

    public Map<String, String> getQueryParameters() {
        HashMap hashMap = new HashMap();
        for (String str : this.queryParameters.keySet()) {
            List list = (List) this.queryParameters.get(str);
            if (list == null || list.isEmpty()) {
                hashMap.put(str, null);
            } else {
                hashMap.put(str, list.iterator().next());
            }
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public String getQueryString() {
        String str;
        String str2 = "getQueryString";
        Logger.enter(getClass().getSimpleName(), str2);
        Iterator it = this.queryParameters.keySet().iterator();
        String str3 = "";
        while (true) {
            str = "&";
            if (!it.hasNext()) {
                break;
            }
            String str4 = (String) it.next();
            List<String> list = (List) this.queryParameters.get(str4);
            if (list.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(str4);
                sb.append("=&");
                str3 = sb.toString();
            } else {
                for (String str5 : list) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    sb2.append(str4);
                    sb2.append("=");
                    sb2.append(str5);
                    sb2.append(str);
                    str3 = sb2.toString();
                }
            }
        }
        if (str3.endsWith(str)) {
            str3 = str3.substring(0, str3.length() - 1);
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return str3;
    }

    public void setQueryParameters(HashMap<String, String> hashMap) {
        String str = "setQueryParameters";
        Logger.enter(getClass().getSimpleName(), str);
        this.queryParameters = new HashMap();
        for (String str2 : hashMap.keySet()) {
            setQueryParameter(str2, (String) hashMap.get(str2));
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void setQueryParameter(String str, String str2) {
        String str3 = "setQueryParameter";
        Logger.enter(getClass().getSimpleName(), str3);
        LinkedList linkedList = new LinkedList();
        linkedList.add(str2);
        this.queryParameters.put(str, linkedList);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public Map<String, List<String>> getAllHeaders() {
        Request request = this.okRequest;
        if (request != null) {
            return request.headers().toMultimap();
        }
        return new HashMap();
    }

    public List<String> getHeaders(String str) {
        String str2 = "getHeaders";
        Logger.enter(getClass().getSimpleName(), str2);
        if (this.okRequest != null) {
            Logger.exit(getClass().getSimpleName(), str2);
            return this.okRequest.headers().values(str);
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return new ArrayList();
    }

    public void removeHeaders(String str) {
        String str2 = "removeHeaders";
        Logger.enter(getClass().getSimpleName(), str2);
        Request request = this.okRequest;
        if (request != null) {
            this.okRequest = request.newBuilder().removeHeader(str).build();
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void setHeaders(Map<String, List<String>> map) {
        String str = "setHeaders";
        Logger.enter(getClass().getSimpleName(), str);
        for (Entry entry : map.entrySet()) {
            this.okRequest = this.okRequest.newBuilder().removeHeader((String) entry.getKey()).build();
            for (String str2 : (List) entry.getValue()) {
                Request request = this.okRequest;
                if (request != null) {
                    this.okRequest = request.newBuilder().addHeader((String) entry.getKey(), str2).build();
                }
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    private Request setGlobalHeaders(Request request) {
        String str = "setGlobalHeaders";
        Logger.enter(getClass().getSimpleName(), str);
        if (request != null) {
            Map globalHeaders = AsynchronousRequestSender.getInstance().getGlobalHeaders();
            if (globalHeaders != null) {
                for (String str2 : globalHeaders.keySet()) {
                    request = request.newBuilder().addHeader(str2, (String) globalHeaders.get(str2)).build();
                }
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return request;
    }

    public void addHeader(String str, String str2) {
        String str3 = "addHeader";
        Logger.enter(getClass().getSimpleName(), str3);
        Request request = this.okRequest;
        if (request != null) {
            this.okRequest = request.newBuilder().addHeader(str, str2).build();
        }
        Logger.exit(getClass().getSimpleName(), str3);
    }

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

    private MediaType resolveContentType(String str) {
        String str2 = "resolveContentType";
        Logger.enter(getClass().getSimpleName(), str2);
        String str3 = this.okRequest.headers().get("Content-Type");
        Logger.exit(getClass().getSimpleName(), str2);
        return str3 == null ? MediaType.parse(str) : MediaType.parse(str3);
    }

    public void send(WLResponseListener wLResponseListener) {
        String str = "send";
        Logger.enter(getClass().getSimpleName(), str);
        addQueryParamsToUrl();
        Request build = this.okRequest.newBuilder().url(getUrl()).method(getMethod(), HttpMethod.requiresRequestBody(getMethod()) ? RequestBody.create(resolveContentType(STRING_CONTENT_TYPE), "") : null).build();
        this.okRequest = build;
        sendRequest(build, wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void send(String str, WLResponseListener wLResponseListener) {
        String str2 = "send";
        Logger.enter(getClass().getSimpleName(), str2);
        addQueryParamsToUrl();
        MediaType resolveContentType = resolveContentType(STRING_CONTENT_TYPE);
        RequestBody requestBody = (!HttpMethod.requiresRequestBody(getMethod()) || str != null) ? str != null ? RequestBody.create(resolveContentType, str) : null : RequestBody.create(resolveContentType, "");
        Request build = this.okRequest.newBuilder().url(getUrl()).method(getMethod(), requestBody).build();
        this.okRequest = build;
        sendRequest(build, wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void send(Map<String, String> map, WLResponseListener wLResponseListener) {
        RequestBody requestBody;
        String str = "send";
        Logger.enter(getClass().getSimpleName(), str);
        FormBody.Builder builder = new FormBody.Builder();
        boolean z = false;
        if (map != null) {
            for (Entry entry : map.entrySet()) {
                builder.add((String) entry.getKey(), (String) entry.getValue());
                z = true;
            }
            requestBody = builder.build();
        } else {
            requestBody = null;
        }
        if (HttpMethod.requiresRequestBody(getMethod()) && !z) {
            requestBody = RequestBody.create(resolveContentType(URL_ENCODED_CONTENT_TYPE), "");
        }
        addQueryParamsToUrl();
        Request build = this.okRequest.newBuilder().url(getUrl()).method(getMethod(), requestBody).build();
        this.okRequest = build;
        sendRequest(build, wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void send(JSONObject jSONObject, WLResponseListener wLResponseListener) {
        String str = "send";
        Logger.enter(getClass().getSimpleName(), str);
        addQueryParamsToUrl();
        MediaType resolveContentType = resolveContentType(JSON_CONTENT_TYPE);
        RequestBody requestBody = (!HttpMethod.requiresRequestBody(getMethod()) || jSONObject != null) ? jSONObject != null ? RequestBody.create(resolveContentType, jSONObject.toString()) : null : RequestBody.create(resolveContentType, "");
        Request build = this.okRequest.newBuilder().url(getUrl()).method(getMethod(), requestBody).build();
        this.okRequest = build;
        sendRequest(build, wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void send(byte[] bArr, WLResponseListener wLResponseListener) {
        String str = "send";
        Logger.enter(getClass().getSimpleName(), str);
        addQueryParamsToUrl();
        MediaType resolveContentType = resolveContentType(BINARY_CONTENT_TYPE);
        RequestBody requestBody = (!HttpMethod.requiresRequestBody(getMethod()) || bArr != null) ? bArr != null ? RequestBody.create(resolveContentType, bArr) : null : RequestBody.create(resolveContentType, "".getBytes());
        Request build = this.okRequest.newBuilder().url(getUrl()).method(getMethod(), requestBody).build();
        this.okRequest = build;
        sendRequest(build, wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private URI extractQueryParamsFromUrl(URI uri) {
        String str;
        String str2 = "extractQueryParamsFromUrl";
        Logger.enter(getClass().getSimpleName(), str2);
        this.queryParameters = new HashMap();
        if (uri.getScheme() == null) {
            String rootURL = WLConfig.getInstance().getRootURL();
            if (uri.toString().charAt(0) != '/') {
                StringBuilder sb = new StringBuilder();
                sb.append(rootURL);
                sb.append("/");
                rootURL = sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(rootURL);
            sb2.append(uri.toString());
            str = sb2.toString();
        } else {
            str = uri.toString();
        }
        try {
            new URL(str);
        } catch (MalformedURLException e) {
            Logger logger2 = logger;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("URL could be malformed or use default schema ports: ");
            sb3.append(str);
            sb3.append(" ");
            sb3.append(e.getLocalizedMessage());
            logger2.warn(sb3.toString());
        }
        HttpUrl parse = HttpUrl.parse(str);
        int querySize = parse.querySize();
        for (int i = 0; i < querySize; i++) {
            String queryParameterName = parse.queryParameterName(i);
            String queryParameterValue = parse.queryParameterValue(i);
            List list = (List) this.queryParameters.get(queryParameterName);
            if (list == null) {
                list = new LinkedList();
                this.queryParameters.put(queryParameterName, list);
            }
            list.add(queryParameterValue);
        }
        String uri2 = uri.toString();
        int indexOf = uri2.indexOf(63);
        if (indexOf > 0) {
            try {
                URI uri3 = new URI(uri2.substring(0, indexOf));
                Logger.exit(getClass().getSimpleName(), str2);
                return uri3;
            } catch (URISyntaxException unused) {
                Logger.exit(getClass().getSimpleName(), str2);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("'");
                sb4.append(uri);
                sb4.append("' is not a valid relative or absolute URL.");
                throw new IllegalStateException(sb4.toString());
            }
        } else {
            Logger.exit(getClass().getSimpleName(), str2);
            return uri;
        }
    }

    private void sendRequest(Request request, WLResponseListener wLResponseListener) {
        String str = "sendRequest";
        Logger.enter(getClass().getSimpleName(), str);
        this.conflictFailCounter = 0;
        OkHttpClient okHttpClient = getOkHttpClient();
        Request userAgentHeader = WLNativeAPIUtils.setUserAgentHeader(request);
        WLAuthorizationManagerInternal instance = WLAuthorizationManagerInternal.getInstance();
        if (this.scope == null && userAgentHeader != null) {
            this.scope = instance.getScopeByResource(userAgentHeader.url().toString(), userAgentHeader.method());
        }
        String str2 = this.scope;
        if (str2 == null) {
            executeRequest(this.okRequest, okHttpClient, wLResponseListener, 0);
        } else {
            instance.obtainAccessToken(str2, (WLAccessTokenListener) new ObtainAccessTokenListener(userAgentHeader, wLResponseListener, 0));
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* access modifiers changed from: private */
    public void executeRequest(Request request, OkHttpClient okHttpClient, final WLResponseListener wLResponseListener, final int i) {
        String str = "executeRequest";
        Logger.enter(getClass().getSimpleName(), str);
        okHttpClient.newCall(setGlobalHeaders(request)).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                WLResourceRequest.this.handleOnFailure(iOException, wLResponseListener);
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onResponse(Call call, Response response) throws IOException {
                String str = "onResponse";
                Logger.enter(getClass().getSimpleName(), str);
                WLResourceRequest.this.handleOnSuccess(response, wLResponseListener, i);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void addQueryParamsToUrl() {
        String str = "addQueryParamsToUrl";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.queryParameters.isEmpty()) {
            Logger.exit(getClass().getSimpleName(), str);
            return;
        }
        HttpUrl.Builder newBuilder = HttpUrl.parse(this.url.toString()).newBuilder();
        for (String str2 : this.queryParameters.keySet()) {
            List<String> list = (List) this.queryParameters.get(str2);
            if (list.isEmpty()) {
                newBuilder.addQueryParameter(str2, null);
            } else {
                for (String addQueryParameter : list) {
                    newBuilder.addQueryParameter(str2, addQueryParameter);
                }
            }
        }
        try {
            this.url = newBuilder.build().url();
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("final url ");
            sb.append(this.url);
            logger2.info(sb.toString());
        } catch (Exception e) {
            Logger logger3 = logger;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("unexpected error: failed to  addQueryParamsToUrl ");
            sb2.append(e.getLocalizedMessage());
            logger3.error(sb2.toString());
            throw new Error(e);
        }
    }

    private void makeAbsoluteUrl(URI uri) {
        String str = "makeAbsoluteUrl";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            if (uri.getScheme() == null) {
                String rootURL = WLConfig.getInstance().getRootURL();
                if (uri.toString().charAt(0) != '/') {
                    StringBuilder sb = new StringBuilder();
                    sb.append(rootURL);
                    sb.append("/");
                    rootURL = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(rootURL);
                sb2.append(uri.toString());
                this.url = new URL(sb2.toString());
            } else {
                this.url = uri.toURL();
            }
            Logger.exit(getClass().getSimpleName(), str);
        } catch (MalformedURLException unused) {
            Logger.exit(getClass().getSimpleName(), str);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("'");
            sb3.append(uri);
            sb3.append("' is not a valid relative or absolute URL.");
            throw new IllegalStateException(sb3.toString());
        }
    }

    private void setContentType(String str) {
        String str2 = "setContentType";
        Logger.enter(getClass().getSimpleName(), str2);
        String str3 = "Content-Type";
        if (getHeaders(str3).size() == 0) {
            addHeader(str3, str);
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: private */
    public void handleOnFailure(IOException iOException, WLResponseListener wLResponseListener) {
        String str = "handleOnFailure";
        Logger.enter(getClass().getSimpleName(), str);
        if (iOException instanceof SocketTimeoutException) {
            logger.debug(WLErrorCode.REQUEST_TIMEOUT.getDescription());
            wLResponseListener.onFailure(new WLFailResponse(WLErrorCode.REQUEST_TIMEOUT, WLErrorCode.REQUEST_TIMEOUT.getDescription(), null));
        } else {
            logger.debug(WLErrorCode.UNEXPECTED_ERROR.getDescription());
            wLResponseListener.onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, iOException.getMessage(), null));
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* access modifiers changed from: private */
    public void handleOnSuccess(Response response, WLResponseListener wLResponseListener, int i) {
        String str = "handleOnSuccess";
        Logger.enter(getClass().getSimpleName(), str);
        WLAuthorizationManagerInternal instance = WLAuthorizationManagerInternal.getInstance();
        logger.debug("WLResourceRequest - entry");
        WLResponse wLResponse = new WLResponse(response);
        if (WLAuthorizationManagerInternal.getInstance().isMfpConflict(wLResponse)) {
            logger.debug("WLResourceRequest - isMfpConflict");
            int i2 = this.conflictFailCounter;
            this.conflictFailCounter = i2 + 1;
            if (i2 >= 7) {
                String str2 = "Reached max attempts of resending request for conflict response";
                logger.warn(str2);
                wLResponseListener.onFailure(new WLFailResponse(WLErrorCode.AUTHORIZATION_FAILURE, str2, null));
            } else {
                executeRequest(this.okRequest, getOkHttpClient(), wLResponseListener, i);
            }
        } else if (instance.isAuthorizationRequired(response)) {
            logger.debug("WLResourceRequest - isAuthorizationRequired");
            if (i < 5) {
                if (instance.isForbidden(response)) {
                    logger.debug("WLResourceRequest - 403");
                    String authorizationScope = instance.getAuthorizationScope(response);
                    this.scope = authorizationScope;
                    instance.cacheScopeByResource(this.okRequest, authorizationScope);
                } else {
                    logger.debug("WLResourceRequest: received 401");
                    String authorizationHeader = instance.getAuthorizationHeader(response);
                    instance.cacheScopeByResource(this.okRequest, WLConstants.DEFAULT_SCOPE);
                    if (authorizationHeader.contains(BEARER_ERROR_INVALID_TOKEN)) {
                        instance.removeTokenByScope(this.scope);
                    }
                }
                logger.debug("WLResourceRequest: Request for new access token");
                instance.obtainAccessToken(this.scope, (WLAccessTokenListener) new ObtainAccessTokenListener(this.okRequest, wLResponseListener, i + 1));
            } else {
                logger.warn(WLErrorCode.AUTHORIZATION_FAILURE.getDescription());
                WLErrorCode wLErrorCode = WLErrorCode.AUTHORIZATION_FAILURE;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot retrieve a valid authorization header for ");
                sb.append(this.okRequest.url().toString());
                sb.append(". Check resource and authorization server configuration.");
                wLResponseListener.onFailure(new WLFailResponse(wLErrorCode, sb.toString(), null));
            }
        } else if (WLClient.getInstance().isGatewayResponse(wLResponse)) {
            WLAuthorizationManagerInternal.getInstance().clearAccessToken(WLAuthorizationManagerInternal.getInstance().getCachedAccessToken(this.scope));
            instance.obtainAccessToken(this.scope, (WLAccessTokenListener) new ObtainAccessTokenListener(this.okRequest, wLResponseListener, i + 1));
        } else if (response.isSuccessful()) {
            Logger.processAutomaticTrigger();
            wLResponseListener.onSuccess(wLResponse);
        } else {
            logger.debug(response.toString());
            wLResponseListener.onFailure(new WLFailResponse(wLResponse));
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* access modifiers changed from: private */
    public OkHttpClient getOkHttpClient() {
        String str = "getOkHttpClient";
        Logger.enter(getClass().getSimpleName(), str);
        OkHttpClient.Builder newBuilder = HttpClientManager.getInstance().getOkHttpClient().newBuilder();
        newBuilder.readTimeout((long) this.timeout, TimeUnit.MILLISECONDS);
        newBuilder.connectTimeout((long) this.timeout, TimeUnit.MILLISECONDS);
        newBuilder.writeTimeout((long) this.timeout, TimeUnit.MILLISECONDS);
        Logger.exit(getClass().getSimpleName(), str);
        return newBuilder.build();
    }
}
