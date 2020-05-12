package com.worklight.wlclient.api;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Pair;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.worklight.common.Logger;
import com.worklight.common.WLAnalytics;
import com.worklight.common.WLConfig;
import com.worklight.common.security.WLDeviceAuthManager;
import com.worklight.common.security.WLOAuthCertManager;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.wlclient.AsynchronousRequestSender;
import com.worklight.wlclient.HttpClientManager;
import com.worklight.wlclient.WLRequest;
import com.worklight.wlclient.WLRequest.RequestPaths;
import com.worklight.wlclient.WLRequestListener;
import com.worklight.wlclient.api.challengehandler.BaseChallengeHandler;
import com.worklight.wlclient.api.challengehandler.GatewayChallengeHandler;
import com.worklight.wlclient.api.challengehandler.SecurityCheckChallengeHandler;
import com.worklight.wlclient.auth.AccessToken;
import com.worklight.wlclient.auth.ClockSyncChallengeHandler;
import com.worklight.wlclient.auth.JWT;
import com.worklight.wlclient.auth.WLAuthorizationManagerInternal;
import com.worklight.wlclient.challengehandler.AuthenticityChallengeHandler;
import com.worklight.wlclient.challengehandler.RegistrationClientIdChallengeHandler;
import com.worklight.wlclient.challengehandler.RemoteDisableChallengeHandler;
import com.worklight.wlclient.cookie.ClearableCookieJar;
import com.worklight.wlclient.cookie.WLPersistentCookie;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

public class WLClient {
    private static final String AUTHENTICITY_REALM = "appAuthenticity";
    private static final String CHALLENGE_HANDLER_NULL_ERROR = "Cannot register 'null' challenge handler";
    private static final String CLOCK_SYNCHRONIZATION = "clockSynchronization";
    private static final String HEART_BEAT_ERROR = "WLClient not initialized - cannot send heart beat message before connect";
    private static final String INVOKE_PROCEDURE_INIT_ERROR = "invokeProcedure() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.";
    private static final String INVOKE_PROCEDURE_INVALID_INVOCATION_DATA = "Error during invocation of remote procedure, because invocation data can't be null.";
    private static final String INVOKE_PROCEDURE_RUN_ERROR = "Error during invocation of remote procedure, because responseListener parameter can't be null.";
    private static final String NO_REALM_REGISTER_ERROR = "Application will exit because the challengeHandler parameter for registerChallengeHandler (challengeHandler) has a null handlerName or securityCheck property. Call this API with a valid reference to challenge handler.";
    private static final String REGISTRATION_CLIENT_ID = "registration-client-id";
    private static final String REMOTE_DISABLE_REALM = "wl_remoteDisableRealm";
    private static final String REQ_PATH_WL_TOKEN = "oauth/token";
    public static final Object WAIT_LOCK = new Object();
    /* access modifiers changed from: private */
    public static Logger logger = Logger.getInstance(WLClient.class.getSimpleName());
    private static WLClient wlClient = null;
    ActivityListener activityListener = null;
    private String appUserId;
    private Hashtable<String, BaseChallengeHandler> chMap;
    private Context context;
    private int heartbeatInterval = 420;
    private JSONObject notificationSubscriptionState = null;
    private Timer timer;

    class ActivityListener implements ActivityLifecycleCallbacks {
        private int activityCount = 0;

        ActivityListener() {
        }

        public boolean isAppInBackground() {
            String str = "isAppInBackground";
            Logger.enter(getClass().getSimpleName(), str);
            Logger.exit(getClass().getSimpleName(), str);
            return this.activityCount == 0;
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
            String str = "onActivityCreated";
            Logger.enter(getClass().getSimpleName(), str);
            Logger access$000 = WLClient.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("on activity created ");
            sb.append(activity.getClass().getName());
            access$000.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityDestroyed(Activity activity) {
            String str = "onActivityDestroyed";
            Logger.enter(getClass().getSimpleName(), str);
            Logger access$000 = WLClient.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("on activity destroyed ");
            sb.append(activity.getClass().getName());
            access$000.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityPaused(Activity activity) {
            String str = "onActivityPaused";
            Logger.enter(getClass().getSimpleName(), str);
            this.activityCount--;
            Logger access$000 = WLClient.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("on activity paused ");
            sb.append(activity.getClass().getName());
            sb.append(" . activity count = ");
            sb.append(this.activityCount);
            access$000.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityResumed(Activity activity) {
            String str = "onActivityResumed";
            Logger.enter(getClass().getSimpleName(), str);
            this.activityCount++;
            Logger access$000 = WLClient.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("on activity resumed ");
            sb.append(activity.getClass().getName());
            sb.append(" . activity count = ");
            sb.append(this.activityCount);
            access$000.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            String str = "onActivitySaveInstanceState";
            Logger.enter(getClass().getSimpleName(), str);
            Logger access$000 = WLClient.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("on activity save instance state ");
            sb.append(activity.getClass().getName());
            access$000.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityStarted(Activity activity) {
            String str = "onActivityStarted";
            Logger.enter(getClass().getSimpleName(), str);
            Logger access$000 = WLClient.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("on activity started ");
            sb.append(activity.getClass().getName());
            access$000.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityStopped(Activity activity) {
            String str = "onActivityStopped";
            Logger.enter(getClass().getSimpleName(), str);
            Logger access$000 = WLClient.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("on activity stopped ");
            sb.append(activity.getClass().getName());
            access$000.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    class HeartBeatTask extends TimerTask {
        public static final String CLIENT_ASSERTION = "client_assertion";
        public static final String CLIENT_ASSERTION_TYPE = "client_assertion_type";
        public static final String PARAM_CLIENT_ASSERTION_TYPE_VALUE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
        private Context context = null;

        HeartBeatTask(Context context2) {
            this.context = context2;
        }

        public void run() {
            String str = "run";
            Logger.enter(getClass().getSimpleName(), str);
            String clientId = WLAuthorizationManagerInternal.getInstance().getClientId();
            if (WLClient.isApplicationSentToBackground() || clientId == null) {
                Logger.exit(getClass().getSimpleName(), str);
                return;
            }
            WLRequestOptions wLRequestOptions = new WLRequestOptions();
            try {
                wLRequestOptions.addParameter(CLIENT_ASSERTION, WLOAuthCertManager.getInstance().signJWS(new JWT().toJson(), clientId));
            } catch (Exception unused) {
                WLClient.logger.debug("unable to convert JWT to json");
            }
            wLRequestOptions.addParameter(CLIENT_ASSERTION_TYPE, PARAM_CLIENT_ASSERTION_TYPE_VALUE);
            HeartbeatListener heartbeatListener = new HeartbeatListener();
            WLClient instance = WLClient.getInstance();
            if (instance != null) {
                new WLRequest(heartbeatListener, wLRequestOptions, WLConfig.getInstance(), instance.getContext()).makeRequest(RequestPaths.HEART_BEAT, true);
            }
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    class HeartbeatListener implements WLRequestListener {
        HeartbeatListener() {
        }

        public void onFailure(WLFailResponse wLFailResponse) {
            String str = "onFailure";
            Logger.enter(getClass().getSimpleName(), str);
            Logger access$000 = WLClient.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to send heartbeat. Response:  ");
            sb.append(wLFailResponse.toString());
            access$000.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onSuccess(WLResponse wLResponse) {
            String str = "onSuccess";
            Logger.enter(getClass().getSimpleName(), str);
            WLClient.logger.debug("Heartbeat sent successfully");
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    class InvokeProcedureRequestListener implements WLRequestListener {
        InvokeProcedureRequestListener() {
        }

        public void onFailure(WLFailResponse wLFailResponse) {
            String str = "onFailure";
            Logger.enter(getClass().getSimpleName(), str);
            wLFailResponse.getOptions().getResponseListener().onFailure(new WLProcedureInvocationFailResponse(wLFailResponse));
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onSuccess(WLResponse wLResponse) {
            String str = "onSuccess";
            Logger.enter(getClass().getSimpleName(), str);
            WLProcedureInvocationResult wLProcedureInvocationResult = new WLProcedureInvocationResult(wLResponse);
            if (wLProcedureInvocationResult.isSuccessful()) {
                wLProcedureInvocationResult.getOptions().getResponseListener().onSuccess(wLProcedureInvocationResult);
            } else {
                WLProcedureInvocationFailResponse wLProcedureInvocationFailResponse = new WLProcedureInvocationFailResponse(wLResponse);
                wLProcedureInvocationFailResponse.getOptions().getResponseListener().onFailure(wLProcedureInvocationFailResponse);
            }
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    public Context getContext() {
        String str = "getContext";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.context;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getGlobalHeaders() {
        return AsynchronousRequestSender.getInstance().getGlobalHeaders();
    }

    /* access modifiers changed from: protected */
    public Hashtable<String, BaseChallengeHandler> getchMap() {
        return this.chMap;
    }

    private WLClient(Context context2) {
        this.context = context2;
        this.chMap = new Hashtable<>();
        registerDefaultChallengeHandlers();
        if (VERSION.SDK_INT >= 14) {
            Application application = (Application) context2.getApplicationContext();
            ActivityListener activityListener2 = new ActivityListener();
            this.activityListener = activityListener2;
            application.registerActivityLifecycleCallbacks(activityListener2);
        }
    }

    public static WLClient createInstance(Context context2) {
        if (wlClient != null) {
            logger.debug("WLClient has already been created.");
            releaseInstance();
        }
        wlClient = new WLClient(context2);
        WLConfig.createInstance(context2);
        WLConfig.getInstance().writeWLPref(WLConfig.ENABLE_LEGACY_HTTP, "false");
        HttpClientManager.createInstance(context2);
        WLUtils.clearState();
        WLAnalytics.init((Application) context2.getApplicationContext());
        return wlClient;
    }

    public static WLClient getInstance() {
        WLClient wLClient = wlClient;
        if (wLClient != null) {
            return wLClient;
        }
        throw new RuntimeException("WLClient object has not been created. You must call WLClient.createInstance first.");
    }

    protected static void releaseInstance() {
        wlClient = null;
    }

    public void addGlobalHeader(String str, String str2) {
        String str3 = "addGlobalHeader";
        Logger.enter(getClass().getSimpleName(), str3);
        AsynchronousRequestSender.getInstance().addGlobalHeader(str, str2);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public void removeGlobalHeader(String str) {
        String str2 = "removeGlobalHeader";
        Logger.enter(getClass().getSimpleName(), str2);
        AsynchronousRequestSender.getInstance().removeGlobalHeader(str);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void invokeProcedure(WLProcedureInvocationData wLProcedureInvocationData, final WLResponseListener wLResponseListener, final WLRequestOptions wLRequestOptions) {
        String str = "invokeProcedure";
        Logger.enter(getClass().getSimpleName(), str);
        if (wLProcedureInvocationData == null) {
            Logger.exit(getClass().getSimpleName(), str);
            throw new IllegalArgumentException(INVOKE_PROCEDURE_INVALID_INVOCATION_DATA);
        } else if (wLResponseListener != null) {
            if (wLRequestOptions == null) {
                wLRequestOptions = new WLRequestOptions();
            }
            wLRequestOptions.setResponseListener(wLResponseListener);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("/adapters/");
                sb.append(wLProcedureInvocationData.getAdapter());
                sb.append("/");
                sb.append(wLProcedureInvocationData.getProcedure());
                WLResourceRequest wLResourceRequest = new WLResourceRequest(new URI(sb.toString()), "GET", wLRequestOptions.getTimeout());
                wLRequestOptions.addParameter("params", convertParametersToString(wLProcedureInvocationData.getParameters()));
                for (String str2 : wLRequestOptions.getParameters().keySet()) {
                    wLResourceRequest.setQueryParameter(str2, (String) wLRequestOptions.getParameters().get(str2));
                }
                Pair okHeaders = wLRequestOptions.getOkHeaders();
                if (okHeaders == null) {
                    Logger.exit(getClass().getSimpleName(), str);
                    return;
                }
                for (int i = 0; i < ((List) okHeaders.first).size(); i++) {
                    wLResourceRequest.addHeader((String) ((List) okHeaders.first).get(i), (String) ((List) okHeaders.second).get(i));
                }
                wLResourceRequest.send(new WLResponseListener() {
                    public void onSuccess(WLResponse wLResponse) {
                        String str = "onSuccess";
                        Logger.enter(getClass().getSimpleName(), str);
                        wLResponse.setOptions(wLRequestOptions);
                        wLResponseListener.onSuccess(wLResponse);
                        Logger.exit(getClass().getSimpleName(), str);
                    }

                    public void onFailure(WLFailResponse wLFailResponse) {
                        String str = "onFailure";
                        Logger.enter(getClass().getSimpleName(), str);
                        wLFailResponse.setOptions(wLRequestOptions);
                        wLResponseListener.onFailure(wLFailResponse);
                        Logger.exit(getClass().getSimpleName(), str);
                    }
                });
                Logger.exit(getClass().getSimpleName(), str);
            } catch (URISyntaxException e) {
                Logger.exit(getClass().getSimpleName(), str);
                throw new RuntimeException(e);
            }
        } else {
            Logger.exit(getClass().getSimpleName(), str);
            throw new IllegalArgumentException(INVOKE_PROCEDURE_RUN_ERROR);
        }
    }

    public void connect(WLResponseListener wLResponseListener) {
        String str = "connect";
        Logger.enter(getClass().getSimpleName(), str);
        connect(wLResponseListener, null);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void connect(final WLResponseListener wLResponseListener, WLRequestOptions wLRequestOptions) {
        String str = "connect";
        Logger.enter(getClass().getSimpleName(), str);
        if (wLResponseListener == null) {
            wLResponseListener = new WLResponseListener() {
                public void onSuccess(WLResponse wLResponse) {
                    String str = "onSuccess";
                    Logger.enter(getClass().getSimpleName(), str);
                    Logger.exit(getClass().getSimpleName(), str);
                }

                public void onFailure(WLFailResponse wLFailResponse) {
                    String str = "onFailure";
                    Logger.enter(getClass().getSimpleName(), str);
                    Logger.exit(getClass().getSimpleName(), str);
                }
            };
        }
        WLAuthorizationManager.getInstance().obtainAccessToken("", new WLAccessTokenListener() {
            public void onSuccess(AccessToken accessToken) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                wLResponseListener.onSuccess(new WLResponse(Callback.DEFAULT_DRAG_ANIMATION_DURATION, accessToken.getValue(), null));
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                wLResponseListener.onFailure(wLFailResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str);
    }

    private String convertParametersToString(Object[] objArr) {
        String str = "convertParametersToString";
        Logger.enter(getClass().getSimpleName(), str);
        if (objArr == null || objArr.length == 0) {
            Logger.exit(getClass().getSimpleName(), str);
            return "[]";
        }
        int i = 0;
        String str2 = "[";
        while (true) {
            String str3 = "\"";
            if (i < objArr.length - 1) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str3);
                sb.append(objArr[i]);
                sb.append("\",");
                str2 = sb.toString();
                i++;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(str3);
                sb2.append(objArr[objArr.length - 1]);
                sb2.append("\"]");
                String sb3 = sb2.toString();
                Logger.exit(getClass().getSimpleName(), str);
                return sb3;
            }
        }
    }

    public void invokeProcedure(WLProcedureInvocationData wLProcedureInvocationData, WLResponseListener wLResponseListener) {
        String str = "invokeProcedure";
        Logger.enter(getClass().getSimpleName(), str);
        invokeProcedure(wLProcedureInvocationData, wLResponseListener, null);
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:14|15|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0066, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0075, code lost:
        throw new java.lang.IllegalArgumentException(java.lang.String.format("The Certificate File is not in valid format. Make sure you’re supplying certificate in a DER format", new java.lang.Object[0]));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0068 */
    public void pinTrustedCertificatePublicKey(String str) throws IllegalArgumentException {
        InputStream inputStream;
        String str2 = "pinTrustedCertificatePublicKey";
        Logger.enter(getClass().getSimpleName(), str2);
        try {
            HttpClientManager.getInstance().pinTrustedCertificatePublicKey(null);
            if (WLConfig.getInstance().isEncrypted()) {
                StringBuilder sb = new StringBuilder();
                sb.append(WLConfig.getInstance().getApplicationAbsolutePathToExternalAppFiles());
                sb.append("/");
                sb.append(str);
                inputStream = new FileInputStream(sb.toString());
            } else {
                inputStream = this.context.getAssets().open(str);
            }
            HttpClientManager.getInstance().pinTrustedCertificatePublicKey(getCertificateFromStream(inputStream));
            try {
                inputStream.close();
            } catch (Exception unused) {
            }
            Logger.exit(getClass().getSimpleName(), str2);
        } catch (Exception unused2) {
            throw new IllegalArgumentException(String.format("Cannot find %s, make sure that certificate file name is correct", new Object[]{str}));
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0077, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r2.add(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0083, code lost:
        throw r14;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0079 */
    public void pinTrustedCertificatePublicKey(String[] strArr) throws IllegalArgumentException {
        String str = "pinTrustedCertificatePublicKey";
        Logger.enter(getClass().getSimpleName(), str);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        if (strArr != null) {
            Certificate[] certificateArr = new Certificate[strArr.length];
            Object obj = null;
            InputStream inputStream = null;
            for (String str2 : strArr) {
                try {
                    if (WLConfig.getInstance().isEncrypted()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(WLConfig.getInstance().getApplicationAbsolutePathToExternalAppFiles());
                        sb.append("/");
                        sb.append(str2);
                        inputStream = new FileInputStream(sb.toString());
                    } else {
                        inputStream = this.context.getAssets().open(str2);
                    }
                } catch (Exception unused) {
                    arrayList3.add(str2);
                }
                obj = getCertificateFromStream(inputStream);
                if (obj != null && (obj instanceof X509Certificate)) {
                    arrayList.add(obj);
                }
                try {
                    inputStream.close();
                } catch (Exception unused2) {
                }
            }
            try {
                HttpClientManager.getInstance().pinMultipleTrustedCertificatePublicKey((Certificate[]) arrayList.toArray(new Certificate[0]));
                if (arrayList2.size() != 0) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("No valid certificates were found");
                    sb2.append(arrayList2);
                    throw new IllegalArgumentException(sb2.toString());
                } else if (arrayList3.size() != 0) {
                    throw new IllegalArgumentException("Cannot find , make sure that certificate file name is correct");
                }
            } catch (Exception e) {
                if (arrayList.isEmpty()) {
                    HttpClientManager.getInstance().pinTrustedCertificatePublicKey(null);
                    throw new IllegalArgumentException(String.format("No valid certificates found to pin", new Object[0]));
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Some Certificate file does not contain a valid certificate. Make sure you’re supplying certificate in a DER format");
                sb3.append(e.toString());
                throw new IllegalArgumentException(String.format(sb3.toString(), new Object[0]));
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    private Certificate getCertificateFromStream(InputStream inputStream) {
        String str = "getCertificateFromStream";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            Certificate generateCertificate = CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
            if (generateCertificate != null) {
                Logger.exit(getClass().getSimpleName(), str);
                return generateCertificate;
            }
            Logger.exit(getClass().getSimpleName(), str);
            return null;
        } catch (CertificateException unused) {
            HttpClientManager.getInstance().pinTrustedCertificatePublicKey(null);
            Logger.exit(getClass().getSimpleName(), str);
            throw new IllegalArgumentException(String.format("The Certificate File is not in valid format. Make sure you’re supplying certificate in a DER format", new Object[0]));
        }
    }

    public void setHeartBeatInterval(int i) {
        String str = "setHeartBeatInterval";
        Logger.enter(getClass().getSimpleName(), str);
        this.heartbeatInterval = i;
        Timer timer2 = this.timer;
        if (timer2 != null) {
            timer2.cancel();
            this.timer = null;
        }
        sendHeartBeat();
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void sendHeartBeat() {
        String str = "sendHeartBeat";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.timer == null && this.heartbeatInterval > 0) {
            Timer timer2 = new Timer();
            this.timer = timer2;
            HeartBeatTask heartBeatTask = new HeartBeatTask(this.context);
            int i = this.heartbeatInterval;
            timer2.scheduleAtFixedRate(heartBeatTask, (long) (i * 1000), (long) (i * 1000));
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public static boolean isApplicationSentToBackground() {
        if (VERSION.SDK_INT >= 14) {
            return wlClient.activityListener.isAppInBackground();
        }
        logger.error("This version of Android SDK is not supported. Check MobileFirst documentation for the minimal Android SDK support.");
        return true;
    }

    private void registerDefaultChallengeHandlers() {
        String str = "registerDefaultChallengeHandlers";
        Logger.enter(getClass().getSimpleName(), str);
        registerChallengeHandler(new RemoteDisableChallengeHandler("wl_remoteDisableRealm"));
        registerChallengeHandler(new AuthenticityChallengeHandler(AUTHENTICITY_REALM));
        registerChallengeHandler(new RegistrationClientIdChallengeHandler(REGISTRATION_CLIENT_ID));
        registerChallengeHandler(new ClockSyncChallengeHandler(CLOCK_SYNCHRONIZATION));
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void registerChallengeHandler(BaseChallengeHandler baseChallengeHandler) {
        String str = "registerChallengeHandler";
        Logger.enter(getClass().getSimpleName(), str);
        if (baseChallengeHandler != null) {
            String handlerName = baseChallengeHandler.getHandlerName();
            if (handlerName != null) {
                this.chMap.put(handlerName, baseChallengeHandler);
                Logger.exit(getClass().getSimpleName(), str);
                return;
            }
            Logger logger2 = logger;
            String str2 = NO_REALM_REGISTER_ERROR;
            logger2.error(str2);
            Logger.exit(getClass().getSimpleName(), str);
            throw new RuntimeException(str2);
        }
        Logger logger3 = logger;
        String str3 = CHALLENGE_HANDLER_NULL_ERROR;
        logger3.error(str3);
        Logger.exit(getClass().getSimpleName(), str);
        throw new RuntimeException(str3);
    }

    public BaseChallengeHandler getBaseChallengeHandler(String str) {
        String str2 = "getBaseChallengeHandler";
        Logger.enter(getClass().getSimpleName(), str2);
        if (str == null) {
            Logger.exit(getClass().getSimpleName(), str2);
            return null;
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return (BaseChallengeHandler) this.chMap.get(str);
    }

    public SecurityCheckChallengeHandler getSecurityCheckChallengeHandler(String str) {
        String str2 = "getSecurityCheckChallengeHandler";
        Logger.enter(getClass().getSimpleName(), str2);
        BaseChallengeHandler baseChallengeHandler = getBaseChallengeHandler(str);
        if (baseChallengeHandler == null || !(baseChallengeHandler instanceof SecurityCheckChallengeHandler)) {
            Logger.exit(getClass().getSimpleName(), str2);
            return null;
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return (SecurityCheckChallengeHandler) baseChallengeHandler;
    }

    public GatewayChallengeHandler getGatewayChallengeHandler(String str) {
        String str2 = "getGatewayChallengeHandler";
        Logger.enter(getClass().getSimpleName(), str2);
        BaseChallengeHandler baseChallengeHandler = getBaseChallengeHandler(str);
        if (baseChallengeHandler == null || !(baseChallengeHandler instanceof GatewayChallengeHandler)) {
            Logger.exit(getClass().getSimpleName(), str2);
            return null;
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return (GatewayChallengeHandler) baseChallengeHandler;
    }

    public GatewayChallengeHandler getGatewayChallengeHandler(WLResponse wLResponse) {
        String str = "getGatewayChallengeHandler";
        Logger.enter(getClass().getSimpleName(), str);
        for (Entry value : this.chMap.entrySet()) {
            BaseChallengeHandler baseChallengeHandler = (BaseChallengeHandler) value.getValue();
            if (baseChallengeHandler instanceof GatewayChallengeHandler) {
                GatewayChallengeHandler gatewayChallengeHandler = (GatewayChallengeHandler) baseChallengeHandler;
                if (gatewayChallengeHandler.canHandleResponse(wLResponse)) {
                    Logger.exit(getClass().getSimpleName(), str);
                    return gatewayChallengeHandler;
                }
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return null;
    }

    public boolean isGatewayResponse(WLResponse wLResponse) {
        String str = "isGatewayResponse";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return getGatewayChallengeHandler(wLResponse) != null;
    }

    private String getAppUserId() {
        String str = "getAppUserId";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.appUserId;
    }

    /* access modifiers changed from: protected */
    public String getWLServerURL() {
        String str = "getWLServerURL";
        Logger.enter(getClass().getSimpleName(), str);
        String serverContext = WLConfig.getInstance().getServerContext();
        String host = WLConfig.getInstance().getAppURL().getHost();
        if (serverContext != null && serverContext.trim().length() > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(host);
            sb.append(serverContext);
            host = sb.toString();
        }
        Logger.exit(getClass().getSimpleName(), str);
        return host;
    }

    public void setServerUrl(URL url) {
        String str;
        String str2 = "setServerUrl";
        Logger.enter(getClass().getSimpleName(), str2);
        String url2 = url.toString();
        Logger logger2 = logger;
        StringBuilder sb = new StringBuilder();
        sb.append("set MobileFirst Platform server URL To:");
        sb.append(url2);
        logger2.debug(sb.toString());
        if (url2.endsWith("/")) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(url2);
            sb2.append("api");
            str = sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(url2);
            sb3.append("/api");
            str = sb3.toString();
        }
        WLConfig.getInstance().setServerUrl(str);
        resetServerContext();
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void resetServerContext() {
        String str = "resetServerContext";
        Logger.enter(getClass().getSimpleName(), str);
        logger.debug("reset HTTP client context");
        AsynchronousRequestSender.reset(getContext());
        for (BaseChallengeHandler clearChallengeRequests : getchMap().values()) {
            clearChallengeRequests.clearChallengeRequests();
        }
        WLAuthorizationManagerInternal.getInstance().clearRegistration();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public URL getServerUrl() {
        String str = "getServerUrl";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            Logger.exit(getClass().getSimpleName(), str);
            return new URL(WLConfig.getInstance().getRootURL());
        } catch (MalformedURLException unused) {
            Logger.exit(getClass().getSimpleName(), str);
            throw new RuntimeException("Invalid server url");
        }
    }

    public String resetDeviceID() {
        String str = "resetDeviceID";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return WLDeviceAuthManager.getInstance().regenerateDeviceID(this.context);
    }

    public void setDeviceID(String str) {
        String str2 = "setDeviceID";
        Logger.enter(getClass().getSimpleName(), str2);
        WLDeviceAuthManager.getInstance().overrideDeviceID(str, this.context);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public CookieStore getCookieStore() {
        return ((CookieManager) HttpClientManager.getInstance().getOkHttpClient().cookieJar()).getCookieStore();
    }

    public ClearableCookieJar getPersistentCookies() {
        String str = "getPersistentCookies";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return WLPersistentCookie.getCookies();
    }

    public void getDeviceDisplayName(DeviceDisplayNameListener deviceDisplayNameListener) {
        String str = "getDeviceDisplayName";
        Logger.enter(getClass().getSimpleName(), str);
        WLDeviceAuthManager.getInstance().getDeviceDisplayName(deviceDisplayNameListener);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void setDeviceDisplayName(String str, WLRequestListener wLRequestListener) {
        String str2 = "setDeviceDisplayName";
        Logger.enter(getClass().getSimpleName(), str2);
        WLDeviceAuthManager.getInstance().setDeviceDisplayName(str, wLRequestListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }
}
