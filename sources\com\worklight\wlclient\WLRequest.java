package com.worklight.wlclient;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Pair;
import com.google.common.net.HttpHeaders;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.utils.Version;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLConstants;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.challengehandler.GatewayChallengeHandler;
import com.worklight.wlclient.api.challengehandler.SecurityCheckChallengeHandler;
import com.worklight.wlclient.auth.WLAuthorizationManagerInternal;
import com.worklight.wlclient.ui.UIActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPOutputStream;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okio.Buffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WLRequest {
    private static final String ACCESS_DENIED_ID = "WLClient.accessDenied";
    private static final String ANALYTICS_API_KEY = "com.worklight.oauth.analytics.api.key";
    private static final String ANALYTICS_PASSWORD = "com.worklight.oauth.analytics.password";
    private static final String ANALYTICS_URL_KEY = "com.worklight.oauth.analytics.url";
    private static final String ANALYTICS_USERNAME = "com.worklight.oauth.analytics.username";
    private static final String AUTHENTICATION_SERVER_VERSION_KEY = "server_version";
    private static final String AUTHORIZATION_FAILURES = "failures";
    private static final String AUTHORIZATION_SUCCESSES = "successes";
    private static final String AUTH_FAIL_ID = "WLClient.authFailure";
    private static final String CLOSE_BUTTON_ID = "WLClient.close";
    private static final String ERROR_ID = "WLClient.error";
    private static final String GREATER_THAN_IFIX = "8.0.0.0-IF201701250919";
    private static final String LOGUPLOADER_IDENTIFIER = "loguploader";
    private static final String LOGUPLOADER_RECEIVER_ENDPOINT = "/v1/loguploader";
    private static final String LOGUPLOADER_RECEIVER_PATH_IDENTIFIER = "analytics-receiver";
    private static final String MFP_CHALLENGE = "MFP-Challenge";
    private static final String MINIMUM_SERVER_SUPPORTED = "8.0.2017020513";
    private static final int NUMBER_OF_ALLOWED_REPETITIONS = 7;
    private static final int OAUTH_REDIRECT_STATUS = 222;
    private static final Set<WLRequestPiggybacker> PIGGYBACKERS = Collections.synchronizedSet(new HashSet());
    private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";
    private static Logger logger = Logger.getInstance(WLRequest.class.getName());
    private WLConfig config;
    private int conflictFailCounter;
    private Context context;
    private RequestMethod method;
    private Request okRequest;
    private WLRequestListener requestListener;
    protected WLRequestOptions requestOptions;
    private String requestURL;
    private boolean shouldFailOnChallengeCancel;
    private Map<String, Object> wlAnswers;

    public interface RequestPaths {
        public static final String AUTHENTICATE = "authenticate";
        public static final String DELETE_USER_PREF = "deleteup";
        public static final String EVENTS = "events";
        public static final String GET_CONFIG = "clientLogProfile";
        public static final String HEART_BEAT = "preauth/v1/heartbeat";
        public static final String INIT = "init";
        public static final String INVOKE_PROCEDURE = "query";
        public static final String LOGIN = "login";
        public static final String LOGOUT = "logout";
        public static final String NOTIFICATION = "notifications";
        public static final String SEND_INVOKE_PROCEDURE = "invoke";
        public static final String SET_USER_PREFS = "setup";
        public static final String SSL_CLIENT_AUTH = "sslclientauth";
        public static final String UPLOAD_LOGS = "loguploader";
    }

    public WLRequest(WLRequestListener wLRequestListener, WLRequestOptions wLRequestOptions, WLConfig wLConfig, Context context2) {
        this(wLRequestListener, wLRequestOptions, wLConfig, context2, false);
    }

    public WLRequest(WLRequestListener wLRequestListener, WLRequestOptions wLRequestOptions, WLConfig wLConfig, Context context2, boolean z) {
        this.requestURL = null;
        this.method = RequestMethod.POST;
        this.wlAnswers = new HashMap();
        this.requestListener = wLRequestListener;
        this.requestOptions = wLRequestOptions;
        this.config = wLConfig;
        this.context = context2;
        this.shouldFailOnChallengeCancel = z;
        this.wlAnswers = new HashMap();
        this.conflictFailCounter = 0;
    }

    public static void addRequestPiggybacker(WLRequestPiggybacker wLRequestPiggybacker) {
        PIGGYBACKERS.add(wLRequestPiggybacker);
    }

    public static void removeRequestPiggybacker(WLRequestPiggybacker wLRequestPiggybacker) {
        PIGGYBACKERS.remove(wLRequestPiggybacker);
    }

    public boolean shouldFailOnChallengeCancel() {
        String str = "shouldFailOnChallengeCancel";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.shouldFailOnChallengeCancel;
    }

    public void makeRequest(String str) {
        String str2 = "makeRequest";
        Logger.enter(getClass().getSimpleName(), str2);
        makeRequest(str, false);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void makeRequest(String str, boolean z) {
        StringBuilder sb;
        String str2;
        Logger.enter(getClass().getSimpleName(), "makeRequest");
        synchronized (PIGGYBACKERS) {
            for (WLRequestPiggybacker processOptions : PIGGYBACKERS) {
                processOptions.processOptions(str, this.requestOptions);
            }
        }
        if (z) {
            sb = new StringBuilder();
            sb.append(this.requestOptions.isAZRequest() ? WLAuthorizationManagerInternal.getInstance().getAuthorizationServerRootPath() : this.config.getRootURL());
            str2 = "/";
        } else {
            sb = new StringBuilder();
            str2 = this.config.getAppURL().toExternalForm();
        }
        sb.append(str2);
        sb.append(str);
        this.requestURL = sb.toString();
        if (str.contains("loguploader")) {
            String readSecurityPref = WLConfig.getInstance().readSecurityPref(ANALYTICS_URL_KEY);
            String readSecurityPref2 = WLConfig.getInstance().readSecurityPref(ANALYTICS_USERNAME);
            String readSecurityPref3 = WLConfig.getInstance().readSecurityPref(ANALYTICS_PASSWORD);
            if (!(readSecurityPref == null || !readSecurityPref.contains(LOGUPLOADER_RECEIVER_PATH_IDENTIFIER) || readSecurityPref2 == null || readSecurityPref3 == null)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(readSecurityPref);
                sb2.append(LOGUPLOADER_RECEIVER_ENDPOINT);
                this.requestURL = sb2.toString();
            }
        }
        sendRequest(this.requestURL);
        Logger.exit(getClass().getSimpleName(), "makeRequest");
    }

    private void sendRequest(String str) {
        String str2;
        String str3 = "sendRequest";
        Logger.enter(getClass().getSimpleName(), str3);
        this.okRequest = new Builder().url(str).build();
        addDefaultHeaders();
        if (str.contains(LOGUPLOADER_RECEIVER_PATH_IDENTIFIER)) {
            addAnalyticsHeader();
        }
        addExtraHeaders();
        addExpectedAnswers();
        addOkParams();
        if (this.okRequest.body() == null) {
            str2 = "null";
        } else {
            try {
                RequestBody body = this.okRequest.body();
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                str2 = buffer.readUtf8();
            } catch (IOException unused) {
                System.out.println("did not work");
                str2 = "";
            }
        }
        Logger.logNetworkData(String.format("REQUEST :: \nHeaders : %sMethod : %s\nURL : %s\nBody : %s", new Object[]{this.okRequest.headers(), this.okRequest.method(), this.okRequest.url(), str2}));
        AsynchronousRequestSender.getInstance().sendWLRequestAsync(this);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    private boolean matchMinimumServerFromResponse(WLResponse wLResponse) {
        String str = "matchMinimumServerFromResponse";
        Logger.enter(getClass().getSimpleName(), str);
        boolean z = true;
        if (WLAuthorizationManagerInternal.getInstance().isRegistrationInProgress() && !WLClient.getInstance().isGatewayResponse(wLResponse)) {
            JSONObject responseJSON = wLResponse.getResponseJSON();
            if (responseJSON != null) {
                String str2 = AUTHENTICATION_SERVER_VERSION_KEY;
                if (responseJSON.has(str2)) {
                    try {
                        z = true ^ new Version(responseJSON.getString(str2)).isSmallerThan(new Version(MINIMUM_SERVER_SUPPORTED));
                    } catch (JSONException e) {
                        logger.error(e.getMessage());
                        z = false;
                    }
                }
            }
        }
        if (!z) {
            String str3 = "This version of the MobileFirst client SDK requires a minimal server version greater than IFIX 8.0.0.0-IF201701250919";
            logger.error(str3);
            processFailureResponse(new WLFailResponse(WLErrorCode.MINIMUM_SERVER, str3, null));
        }
        Logger.exit(getClass().getSimpleName(), str);
        return z;
    }

    public void requestFinished(WLResponse wLResponse) {
        String str = "requestFinished";
        Logger.enter(getClass().getSimpleName(), str);
        if (!matchMinimumServerFromResponse(wLResponse)) {
            Logger.exit(getClass().getSimpleName(), str);
            return;
        }
        try {
            checkResponseForSuccesses(wLResponse);
            try {
                boolean checkResponseForChallenges = checkResponseForChallenges(wLResponse);
                if (WLAuthorizationManagerInternal.getInstance().isMfpConflict(wLResponse)) {
                    int i = this.conflictFailCounter;
                    this.conflictFailCounter = i + 1;
                    if (i >= 7) {
                        logger.warn("Reached max attempts of resending request for conflict response");
                        processFailureResponse(wLResponse);
                    } else {
                        resendRequest();
                    }
                }
                if (wLResponse.getStatus() == OAUTH_REDIRECT_STATUS) {
                    processSuccessResponse(wLResponse);
                } else if (!checkResponseForChallenges) {
                    if (wLResponse.getStatus() == 200) {
                        processSuccessResponse(wLResponse);
                    } else if (wLResponse.getStatus() == 201 || wLResponse.getStatus() == 204) {
                        Logger logger2 = logger;
                        StringBuilder sb = new StringBuilder();
                        sb.append("requestFinished with status: ");
                        sb.append(wLResponse.getStatus());
                        logger2.debug(sb.toString());
                        processSuccessResponse(wLResponse);
                    } else {
                        processFailureResponse(wLResponse);
                    }
                }
                Logger.exit(getClass().getSimpleName(), str);
            } catch (Exception e) {
                triggerUnexpectedOnFailure(e);
                Logger.exit(getClass().getSimpleName(), str);
            }
        } catch (Exception e2) {
            triggerUnexpectedOnFailure(e2);
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    private void triggerUnexpectedOnFailure(Exception exc) {
        String str = "triggerUnexpectedOnFailure";
        Logger.enter(getClass().getSimpleName(), str);
        getRequestListener().onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, exc.getMessage(), getOptions()));
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void addDefaultHeaders() {
        String str;
        String str2 = "addDefaultHeaders";
        Logger.enter(getClass().getSimpleName(), str2);
        if (VERSION.SDK_INT >= 21) {
            str = Locale.getDefault().toLanguageTag();
        } else {
            str = Locale.getDefault().toString();
            String str3 = "_";
            if (str.contains(str3)) {
                str = str.replace(str3, "-");
            }
        }
        String str4 = "UNKNOWN";
        Builder header = this.okRequest.newBuilder().header(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest").header(WLConfig.WL_X_VERSION_HEADER, this.config.getApplicationVersion() != null ? this.config.getApplicationVersion() : str4).header(HttpHeaders.ACCEPT_LANGUAGE, str);
        if (this.config.getPlatformVersion() != null) {
            str4 = this.config.getPlatformVersion();
        }
        this.okRequest = header.header(WLConfig.WL_X_PLATFORM_VERSION, str4).build();
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void addExtraHeaders() {
        String str = "addExtraHeaders";
        Logger.enter(getClass().getSimpleName(), str);
        Pair okHeaders = this.requestOptions.getOkHeaders();
        Builder newBuilder = this.okRequest.newBuilder();
        if (okHeaders == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return;
        }
        for (int i = 0; i < ((List) okHeaders.first).size(); i++) {
            newBuilder.header((String) ((List) okHeaders.first).get(i), (String) ((List) okHeaders.second).get(i));
        }
        this.okRequest = newBuilder.build();
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void addAnalyticsHeader() {
        String str = "addAnalyticsHeader";
        Logger.enter(getClass().getSimpleName(), str);
        String readSecurityPref = WLConfig.getInstance().readSecurityPref(ANALYTICS_USERNAME);
        String readSecurityPref2 = WLConfig.getInstance().readSecurityPref(ANALYTICS_PASSWORD);
        if (!(readSecurityPref == null || readSecurityPref2 == null)) {
            this.okRequest = this.okRequest.newBuilder().header(HttpHeaders.AUTHORIZATION, Credentials.basic(readSecurityPref, readSecurityPref2)).build();
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void addOkParams() {
        String str = "addOkParams";
        Logger.enter(getClass().getSimpleName(), str);
        if (getMethod().name().equals(RequestMethod.POST.name()) || getMethod().name().equals(RequestMethod.PUT.name())) {
            addParamsEntityEnclosedReq(this.requestOptions);
        } else {
            addParamsBaseRequest(this.requestOptions);
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void addParamsBaseRequest(WLRequestOptions wLRequestOptions) {
        String str = "addParamsBaseRequest";
        Logger.enter(getClass().getSimpleName(), str);
        HttpUrl.Builder newBuilder = this.okRequest.url().newBuilder();
        HashMap hashMap = new HashMap();
        if (wLRequestOptions.getParameters() != null && !wLRequestOptions.getParameters().isEmpty()) {
            for (String str2 : wLRequestOptions.getParameters().keySet()) {
                hashMap.put(str2, wLRequestOptions.getParameters().get(str2));
                newBuilder.addQueryParameter(str2, (String) wLRequestOptions.getParameters().get(str2));
            }
        }
        String str3 = "true";
        String str4 = "isAjaxRequest";
        hashMap.put(str4, str3);
        String str5 = "x";
        hashMap.put(str5, String.valueOf(Math.random()));
        HttpUrl build = newBuilder.addQueryParameter(str4, str3).addQueryParameter(str5, String.valueOf(Math.random())).build();
        if (RequestMethod.GET.equals(getMethod())) {
            this.okRequest = this.okRequest.newBuilder().get().url(build).build();
        } else {
            this.okRequest = this.okRequest.newBuilder().url(build).build();
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void addParamsEntityEnclosedReq(WLRequestOptions wLRequestOptions) {
        RequestBody requestBody;
        String str = "addParamsEntityEnclosedReq";
        Logger.enter(getClass().getSimpleName(), str);
        FormBody.Builder builder = new FormBody.Builder();
        ArrayList<Pair> arrayList = new ArrayList<>();
        if (wLRequestOptions.getParameters() != null && !wLRequestOptions.getParameters().isEmpty()) {
            for (String str2 : wLRequestOptions.getParameters().keySet()) {
                arrayList.add(new Pair(str2, wLRequestOptions.getParameters().get(str2)));
                builder.add(str2, (String) wLRequestOptions.getParameters().get(str2));
            }
        }
        String str3 = "true";
        if (wLRequestOptions.isJsonContentType()) {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), paramsOkToJsonObj(arrayList).toString());
        } else if (wLRequestOptions.isTextContentType()) {
            requestBody = RequestBody.create(MediaType.parse("text/plain; charset=UTF-8"), paramsOkToJsonObj(arrayList).toString());
        } else {
            String str4 = "isAjaxRequest";
            arrayList.add(new Pair(str4, str3));
            String str5 = "x";
            arrayList.add(new Pair(str5, String.valueOf(Math.random())));
            builder.add(str4, str3);
            requestBody = builder.add(str5, String.valueOf(Math.random())).build();
        }
        if (this.okRequest.url().toString().endsWith("loguploader")) {
            this.okRequest = this.okRequest.newBuilder().header("x-wl-compressed", str3).header(HttpHeaders.CONTENT_ENCODING, "gzip").header(HttpHeaders.CONTENT_TYPE, "text/plain").build();
            JSONObject jSONObject = new JSONObject();
            for (Pair pair : arrayList) {
                try {
                    jSONObject.put((String) pair.first, pair.second);
                } catch (JSONException e) {
                    Logger.exit(getClass().getSimpleName(), str);
                    throw new RuntimeException(e);
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                gZIPOutputStream.write(jSONObject.toString().getBytes("UTF-8"));
                gZIPOutputStream.finish();
                gZIPOutputStream.close();
                this.okRequest = this.okRequest.newBuilder().method(getMethod().name(), RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), byteArrayOutputStream.toByteArray())).build();
                Logger.exit(getClass().getSimpleName(), str);
            } catch (UnsupportedEncodingException e2) {
                logger.error(e2.getMessage(), (Throwable) e2);
                Logger.exit(getClass().getSimpleName(), str);
                throw new RuntimeException(e2);
            } catch (IOException e3) {
                logger.error(e3.getMessage(), (Throwable) e3);
                Logger.exit(getClass().getSimpleName(), str);
                throw new RuntimeException(e3);
            }
        } else {
            this.okRequest = this.okRequest.newBuilder().method(getMethod().name(), requestBody).build();
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    private JSONObject paramsOkToJsonObj(List<Pair<String, String>> list) {
        String str = "paramsOkToJsonObj";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject jSONObject = new JSONObject();
        for (Pair pair : list) {
            try {
                String str2 = (String) pair.second;
                if (str2.startsWith("{")) {
                    jSONObject.put((String) pair.first, new JSONObject(str2));
                } else {
                    jSONObject.put((String) pair.first, str2);
                }
            } catch (JSONException e) {
                Logger.exit(getClass().getSimpleName(), str);
                throw new RuntimeException(e);
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return jSONObject;
    }

    private String addExpectedAnswers() {
        String str = "addExpectedAnswers";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.wlAnswers.isEmpty()) {
            Logger.exit(getClass().getSimpleName(), str);
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        for (Entry entry : this.wlAnswers.entrySet()) {
            String str2 = (String) entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                Logger.exit(getClass().getSimpleName(), str);
                return null;
            }
            try {
                jSONObject.accumulate(str2, value);
            } catch (JSONException e) {
                logger.error(e.getMessage(), (Throwable) e);
                Logger.exit(getClass().getSimpleName(), str);
                throw new RuntimeException(e);
            }
        }
        String jSONObject2 = jSONObject.toString();
        this.requestOptions.addParameter("challengeResponse", jSONObject2);
        this.wlAnswers.clear();
        Logger.exit(getClass().getSimpleName(), str);
        return jSONObject2;
    }

    public WLRequestListener getRequestListener() {
        String str = "getRequestListener";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.requestListener;
    }

    public Request getOkRequest() {
        String str = "getOkRequest";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.okRequest;
    }

    public WLRequestOptions getOptions() {
        String str = "getOptions";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.requestOptions;
    }

    public WLConfig getConfig() {
        String str = "getConfig";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.config;
    }

    public Context getContext() {
        String str = "getContext";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.context;
    }

    private void setExpectedAnswers(List<String> list) {
        String str = "setExpectedAnswers";
        Logger.enter(getClass().getSimpleName(), str);
        for (String put : list) {
            this.wlAnswers.put(put, null);
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void submitAnswer(String str, Object obj) {
        String str2 = "submitAnswer";
        Logger.enter(getClass().getSimpleName(), str2);
        this.wlAnswers.put(str, obj);
        resendIfNeeded();
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void resendIfNeeded() {
        String str = "resendIfNeeded";
        Logger.enter(getClass().getSimpleName(), str);
        Map<String, Object> map = this.wlAnswers;
        boolean z = true;
        if (map != null) {
            Iterator it = map.values().iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next() == null) {
                        z = false;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (z) {
            resendRequest();
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void removeExpectedAnswer(String str) {
        String str2 = "removeExpectedAnswer";
        Logger.enter(getClass().getSimpleName(), str2);
        this.wlAnswers.remove(str);
        resendIfNeeded();
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void checkResponseForSuccesses(WLResponse wLResponse) {
        String str = AUTHORIZATION_SUCCESSES;
        String str2 = "checkResponseForSuccesses";
        Logger.enter(getClass().getSimpleName(), str2);
        JSONObject responseJSON = wLResponse.getResponseJSON();
        if (responseJSON == null) {
            Logger.exit(getClass().getSimpleName(), str2);
            return;
        }
        try {
            if (!responseJSON.has(str)) {
                Logger.exit(getClass().getSimpleName(), str2);
                return;
            }
            JSONObject jSONObject = responseJSON.getJSONObject(str);
            if (jSONObject != null) {
                JSONArray names = jSONObject.names();
                for (int i = 0; i < jSONObject.length(); i++) {
                    String string = names.getString(i);
                    JSONObject jSONObject2 = jSONObject.getJSONObject(string);
                    SecurityCheckChallengeHandler securityCheckChallengeHandler = WLClient.getInstance().getSecurityCheckChallengeHandler(string);
                    if (securityCheckChallengeHandler != null) {
                        securityCheckChallengeHandler.handleSuccess(jSONObject2);
                        securityCheckChallengeHandler.releaseWaitingList();
                    } else {
                        Logger logger2 = logger;
                        StringBuilder sb = new StringBuilder();
                        sb.append("No challenge handler was found for security check ");
                        sb.append(string);
                        sb.append(". Register the challenge handler using registerChallengeHandler().");
                        logger2.warn(sb.toString());
                    }
                }
            }
            Logger.exit(getClass().getSimpleName(), str2);
        } catch (Exception e) {
            triggerUnexpectedOnFailure(e);
        }
    }

    private boolean checkResponseForChallenges(WLResponse wLResponse) {
        String str = "checkResponseForChallenges";
        Logger.enter(getClass().getSimpleName(), str);
        WLUtils.getMessagesBundle();
        boolean z = true;
        if (isWl401(wLResponse)) {
            JSONObject responseJSON = wLResponse.getResponseJSON();
            ArrayList<String> arrayList = new ArrayList<>();
            try {
                JSONObject jSONObject = responseJSON.getJSONObject("challenges");
                JSONArray names = jSONObject.names();
                for (int i = 0; i < names.length(); i++) {
                    arrayList.add(names.getString(i));
                }
                setExpectedAnswers(arrayList);
                for (String str2 : arrayList) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject(str2);
                    SecurityCheckChallengeHandler securityCheckChallengeHandler = WLClient.getInstance().getSecurityCheckChallengeHandler(str2);
                    if (securityCheckChallengeHandler == null) {
                        Logger logger2 = logger;
                        StringBuilder sb = new StringBuilder();
                        sb.append("An unhandled challenge arrived while using security check ");
                        sb.append(str2);
                        sb.append(". Register the challenge handler using registerChallengeHandler().");
                        logger2.error(sb.toString());
                        this.requestListener.onFailure(new WLFailResponse(WLErrorCode.MISSING_CHALLENGE_HANDLER, WLErrorCode.MISSING_CHALLENGE_HANDLER.getDescription(), null));
                        Logger.exit(getClass().getSimpleName(), str);
                        return false;
                    }
                    securityCheckChallengeHandler.startHandleChallenge(this, jSONObject2);
                }
            } catch (JSONException e) {
                Logger logger3 = logger;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Wrong JSON arrived when processing a challenge in a 401 response. With ");
                sb2.append(e.getMessage());
                logger3.debug(sb2.toString(), (Throwable) e);
            }
        } else if (isWl403(wLResponse)) {
            try {
                JSONObject jSONObject3 = wLResponse.getResponseJSON().getJSONObject("failures");
                JSONArray names2 = jSONObject3.names();
                for (int i2 = 0; i2 < names2.length(); i2++) {
                    String string = names2.getString(i2);
                    JSONObject jSONObject4 = jSONObject3.getJSONObject(string);
                    SecurityCheckChallengeHandler securityCheckChallengeHandler2 = WLClient.getInstance().getSecurityCheckChallengeHandler(string);
                    if (securityCheckChallengeHandler2 != null) {
                        securityCheckChallengeHandler2.handleFailure(jSONObject4);
                        securityCheckChallengeHandler2.clearWaitingList();
                    } else {
                        Logger logger4 = logger;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("No challenge handler was found for security check ");
                        sb3.append(string);
                        sb3.append(". Register the challenge handler using registerChallengeHandler()");
                        logger4.warn(sb3.toString());
                    }
                    this.requestListener.onFailure(new WLFailResponse(wLResponse));
                }
            } catch (JSONException e2) {
                Logger logger5 = logger;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Wrong JSON arrived when processing a challenge in a 403 response. with ");
                sb4.append(e2.getMessage());
                logger5.debug(sb4.toString(), (Throwable) e2);
            }
        } else {
            z = handleCustomChallenges(wLResponse);
        }
        Logger.exit(getClass().getSimpleName(), str);
        return z;
    }

    private void showErrorDialogue(String str, String str2, String str3) {
        String str4 = "showErrorDialogue";
        Logger.enter(getClass().getSimpleName(), str4);
        Context context2 = WLClient.getInstance().getContext();
        Intent intent = new Intent(context2, UIActivity.class);
        intent.putExtra(WLConstants.ACTION_ID, WLConstants.EXIT_ACTION);
        intent.putExtra(WLConstants.DIALOGUE_MESSAGE, str2);
        intent.putExtra(WLConstants.DIALOGUE_TITLE, str);
        intent.putExtra(WLConstants.POSITIVE_BUTTON_TEXT, str3);
        intent.addFlags(268435456);
        context2.startActivity(intent);
        Logger.exit(getClass().getSimpleName(), str4);
    }

    private boolean isWl401(WLResponse wLResponse) {
        String str = "isWl401";
        Logger.enter(getClass().getSimpleName(), str);
        List header = wLResponse.getHeader(HttpHeaders.WWW_AUTHENTICATE);
        if (wLResponse.getStatus() == 401 && header != null) {
            String str2 = (String) header.get(0);
            if (str2 != null && str2.equalsIgnoreCase(MFP_CHALLENGE)) {
                Logger.exit(getClass().getSimpleName(), str);
                return true;
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return false;
    }

    private boolean isWl403(WLResponse wLResponse) {
        String str = "isWl403";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return wLResponse.getStatus() == 403 && wLResponse.getResponseJSON() != null && wLResponse.getResponseJSON().has("failures");
    }

    private void processSuccessResponse(WLResponse wLResponse) {
        Logger.enter(getClass().getSimpleName(), "processSuccessResponse");
        synchronized (PIGGYBACKERS) {
            for (WLRequestPiggybacker onSuccess : PIGGYBACKERS) {
                onSuccess.onSuccess(wLResponse);
            }
        }
        this.requestListener.onSuccess(wLResponse);
        Logger.exit(getClass().getSimpleName(), "processSuccessResponse");
    }

    public void processFailureResponse(WLResponse wLResponse) {
        WLFailResponse wLFailResponse;
        Logger.enter(getClass().getSimpleName(), "processFailureResponse");
        if (wLResponse instanceof WLFailResponse) {
            wLFailResponse = (WLFailResponse) wLResponse;
        } else {
            wLFailResponse = new WLFailResponse(wLResponse);
        }
        wLFailResponse.setOptions(this.requestOptions);
        synchronized (PIGGYBACKERS) {
            for (WLRequestPiggybacker onFailure : PIGGYBACKERS) {
                onFailure.onFailure(wLFailResponse);
            }
        }
        this.requestListener.onFailure(wLFailResponse);
        Logger.exit(getClass().getSimpleName(), "processFailureResponse");
    }

    private boolean handleCustomChallenges(WLResponse wLResponse) {
        boolean z;
        String str = "handleCustomChallenges";
        Logger.enter(getClass().getSimpleName(), str);
        GatewayChallengeHandler gatewayChallengeHandler = WLClient.getInstance().getGatewayChallengeHandler(wLResponse);
        if (gatewayChallengeHandler != null) {
            gatewayChallengeHandler.startHandleChallenge(this, wLResponse);
            z = true;
        } else {
            z = false;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return z;
    }

    public void resendRequest() {
        String str = "resendRequest";
        Logger.enter(getClass().getSimpleName(), str);
        String str2 = this.requestURL;
        if (str2 != null) {
            sendRequest(str2);
        } else {
            logger.debug("resendRequest failed: requestURL is null.");
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public RequestMethod getMethod() {
        String str = "getMethod";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.method;
    }

    public void setMethod(RequestMethod requestMethod) {
        String str = "setMethod";
        Logger.enter(getClass().getSimpleName(), str);
        this.method = requestMethod;
        Logger.exit(getClass().getSimpleName(), str);
    }
}
