package com.worklight.common;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.worklight.wlclient.WLRequestListener;
import com.worklight.wlclient.api.WLLifecycleHelper;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class WLAnalytics {
    private static final String ANALYTICS_API_KEY = "com.worklight.oauth.analytics.api.key";
    private static final String ANALYTICS_PASSWORD = "com.worklight.oauth.analytics.password";
    private static final String ANALYTICS_URL_KEY = "com.worklight.oauth.analytics.url";
    private static final String ANALYTICS_USERNAME = "com.worklight.oauth.analytics.username";
    private static final String APP_SESSION = "appSession";
    private static final String APP_SESSION_ID = "$appSessionID";
    private static final String CATEGORY = "$category";
    private static final String NETWORK = "network";
    public static final String SHARED_PREF_KEY;
    public static final String SHARED_PREF_KEY_USER_ID;
    private static final String TIMESTAMP = "$timestamp";
    private static final ThreadPoolExecutor ThreadPoolWorkQueue;
    private static final String USER_ID_HASH = "$userID";
    private static final String USER_SWITCH = "userSwitch";
    public static final Object WAIT_LOCK = new Object();
    /* access modifiers changed from: private */
    public static Context context;
    protected static Activity currentActivity;
    private static HashSet<String> enabledDeviceEvents;
    private static final HashSet<String> excludeKeys;
    private static final Logger logger = Logger.getInstance("wl.analytics");

    public enum DeviceEvent {
        LIFECYCLE(WLAnalytics.APP_SESSION),
        NETWORK(WLAnalytics.NETWORK);
        
        private String name;

        private DeviceEvent(String str) {
            this.name = str;
        }

        /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x002e  */
        public static DeviceEvent fromString(String str) {
            char c;
            int hashCode = str.hashCode();
            if (hashCode != -1177474763) {
                if (hashCode == 1843485230 && str.equals(WLAnalytics.NETWORK)) {
                    c = 1;
                    if (c == 0) {
                        return LIFECYCLE;
                    }
                    if (c != 1) {
                        return null;
                    }
                    return NETWORK;
                }
            } else if (str.equals(WLAnalytics.APP_SESSION)) {
                c = 0;
                if (c == 0) {
                }
            }
            c = 65535;
            if (c == 0) {
            }
        }

        public String toString() {
            String str = "toString";
            Logger.enter(getClass().getSimpleName(), str);
            Logger.exit(getClass().getSimpleName(), str);
            return this.name;
        }
    }

    private static class WLActivityLifecycle implements ActivityLifecycleCallbacks {
        private static WLActivityLifecycle instance;

        private WLActivityLifecycle() {
        }

        public static void init(Application application) {
            if (instance == null) {
                instance = new WLActivityLifecycle();
                WLLifecycleHelper.init(WLAnalytics.context);
                application.registerActivityLifecycleCallbacks(instance);
            }
        }

        public void onActivityPaused(Activity activity) {
            String str = "onActivityPaused";
            Logger.enter(getClass().getSimpleName(), str);
            WLLifecycleHelper.getInstance().onPause();
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityResumed(Activity activity) {
            String str = "onActivityResumed";
            Logger.enter(getClass().getSimpleName(), str);
            WLLifecycleHelper.getInstance().onResume();
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityDestroyed(Activity activity) {
            String str = "onActivityDestroyed";
            Logger.enter(getClass().getSimpleName(), str);
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
            String str = "onActivityCreated";
            Logger.enter(getClass().getSimpleName(), str);
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            String str = "onActivitySaveInstanceState";
            Logger.enter(getClass().getSimpleName(), str);
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityStarted(Activity activity) {
            String str = "onActivityStarted";
            Logger.enter(getClass().getSimpleName(), str);
            WLAnalytics.currentActivity = activity;
            Logger.exit(getClass().getSimpleName(), str);
        }

        public void onActivityStopped(Activity activity) {
            String str = "onActivityStopped";
            Logger.enter(getClass().getSimpleName(), str);
            Logger.exit(getClass().getSimpleName(), str);
        }
    }

    static {
        Class<WLAnalytics> cls = WLAnalytics.class;
        HashSet<String> hashSet = new HashSet<>();
        excludeKeys = hashSet;
        hashSet.add("serverIpAddress");
        excludeKeys.add("appID");
        excludeKeys.add("appVersionCode");
        excludeKeys.add("appName");
        excludeKeys.add("appVersion");
        excludeKeys.add("deviceBrand");
        excludeKeys.add("deviceOSversion");
        excludeKeys.add("deviceOS");
        excludeKeys.add("deviceModel");
        excludeKeys.add("deviceID");
        excludeKeys.add("timezone");
        excludeKeys.add("timestamp");
        SHARED_PREF_KEY = cls.getName();
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append(".userID");
        SHARED_PREF_KEY_USER_ID = sb.toString();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 100, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(1000));
        ThreadPoolWorkQueue = threadPoolExecutor;
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                String str = "rejectedExecution";
                Logger.enter(getClass().getSimpleName(), str);
                try {
                    threadPoolExecutor.getQueue().put(runnable);
                } catch (InterruptedException unused) {
                }
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
    }

    public static void init(Application application) {
        DeviceEvent[] values;
        if (context == null) {
            Context applicationContext = application.getApplicationContext();
            context = applicationContext;
            Logger.setContext(applicationContext);
            WLActivityLifecycle.init(application);
            HashSet<String> hashSet = new HashSet<>();
            enabledDeviceEvents = hashSet;
            hashSet.add(USER_SWITCH);
            enabledDeviceEvents.add(APP_SESSION);
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
            for (DeviceEvent deviceEvent : DeviceEvent.values()) {
                if (sharedPreferences.getBoolean(deviceEvent.toString(), false)) {
                    enabledDeviceEvents.add(deviceEvent.toString());
                }
            }
            triggerSendFeedback();
        }
    }

    protected static void deinit() {
        context = null;
    }

    public static void addDeviceEventListener(DeviceEvent deviceEvent) {
        enabledDeviceEvents.add(deviceEvent.toString());
        context.getSharedPreferences(SHARED_PREF_KEY, 0).edit().putBoolean(deviceEvent.toString(), true).commit();
    }

    public static void removeDeviceEventListener(DeviceEvent deviceEvent) {
        if (enabledDeviceEvents.remove(deviceEvent.toString())) {
            context.getSharedPreferences(SHARED_PREF_KEY, 0).edit().putBoolean(deviceEvent.toString(), false).commit();
        }
    }

    public static void triggerFeedbackMode() {
        boolean z;
        boolean z2;
        String readSecurityPref = WLConfig.getInstance().readSecurityPref(ANALYTICS_URL_KEY);
        String readSecurityPref2 = WLConfig.getInstance().readSecurityPref(ANALYTICS_API_KEY);
        String readSecurityPref3 = WLConfig.getInstance().readSecurityPref(ANALYTICS_USERNAME);
        String readSecurityPref4 = WLConfig.getInstance().readSecurityPref(ANALYTICS_PASSWORD);
        String str = "FEEDBACK";
        if (context == null || currentActivity == null) {
            Log.w(str, "Failed to invoke feedback mode since Analytic context is null");
            return;
        }
        if (readSecurityPref != null) {
            z2 = readSecurityPref2 != null;
            z = (readSecurityPref3 == null || readSecurityPref4 == null) ? false : true;
        } else {
            z2 = false;
            z = false;
        }
        if (z2 || z) {
            try {
                Class<String> cls = String.class;
                Class.forName("com.worklight.analytics.feedback.Feedback").getMethod("triggerFeedbackMode", new Class[]{Context.class, Activity.class, cls, cls, cls, cls}).invoke(null, new Object[]{context, currentActivity, readSecurityPref, readSecurityPref2, readSecurityPref3, readSecurityPref4});
            } catch (Throwable th) {
                Log.w(str, "Analytics Feedback module missing. Please add ibmmobilefirstplatformfoundationanalytics module", th);
            }
        } else {
            Log.w(str, "Failed to invoke feedback mode since MFP instance is not supporting in-app feedback");
        }
    }

    private static void triggerSendFeedback() {
        String readSecurityPref = WLConfig.getInstance().readSecurityPref(ANALYTICS_URL_KEY);
        String readSecurityPref2 = WLConfig.getInstance().readSecurityPref(ANALYTICS_API_KEY);
        String readSecurityPref3 = WLConfig.getInstance().readSecurityPref(ANALYTICS_USERNAME);
        String readSecurityPref4 = WLConfig.getInstance().readSecurityPref(ANALYTICS_PASSWORD);
        String str = "FEEDBACK";
        if (context == null || currentActivity == null) {
            Log.w(str, "Failed to invoke feedback mode since Analytic context is null");
        } else if (readSecurityPref != null) {
            try {
                Class<String> cls = String.class;
                Class.forName("com.worklight.analytics.feedback.Feedback").getMethod("sendAppFeedback", new Class[]{Context.class, Activity.class, cls, cls, cls, cls}).invoke(null, new Object[]{context, currentActivity, readSecurityPref, readSecurityPref2, readSecurityPref3, readSecurityPref4});
            } catch (Throwable th) {
                Log.w(str, "Analytics Feedback module missing. Please add ibmmobilefirstplatformfoundationanalytics module", th);
            }
        } else {
            Log.w(str, "Failed to invoke feedback mode since MFP instance is not supporting in-app feedback");
        }
    }

    public static void setContext(Context context2) {
        Logger.setContext(context);
    }

    public static void setUserContext(String str) {
        Context context2 = context;
        if (context2 != null) {
            context2.getSharedPreferences(SHARED_PREF_KEY, 0).edit().putString(SHARED_PREF_KEY_USER_ID, str).commit();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(CATEGORY, USER_SWITCH);
                jSONObject.put(TIMESTAMP, new Date().getTime());
                jSONObject.put(APP_SESSION_ID, WLLifecycleHelper.getAppSessionID());
                jSONObject.put(USER_ID_HASH, str);
            } catch (JSONException e) {
                Logger instance = Logger.getInstance("wl.analytics");
                StringBuilder sb = new StringBuilder();
                sb.append("JSONException encountered logging change in user context: ");
                sb.append(e.getMessage());
                instance.debug(sb.toString());
            }
            log(APP_SESSION, jSONObject);
        }
    }

    public static void unsetUserContext() {
        Context context2 = context;
        if (context2 != null) {
            context2.getSharedPreferences(SHARED_PREF_KEY, 0).edit().remove(SHARED_PREF_KEY_USER_ID).commit();
        }
    }

    public static void enable() {
        Logger.setAnalyticsCapture(true);
    }

    public static void disable() {
        Logger.setAnalyticsCapture(false);
    }

    public static void log(String str, JSONObject jSONObject) {
        log(str, jSONObject, null);
    }

    public static void log(String str, JSONObject jSONObject, Throwable th) {
        Object obj = null;
        if (jSONObject != null) {
            try {
                obj = (String) jSONObject.get(CATEGORY);
            } catch (JSONException e) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("JSONException encountered logging analytics data: ");
                sb.append(e.getMessage());
                logger2.debug(sb.toString());
            }
            if (obj == null || enabledDeviceEvents.contains(obj)) {
                sanitizeCustomMetadata(jSONObject);
                logger.analytics(str, jSONObject, th);
                return;
            }
            return;
        }
        logger.analytics(str, null, null);
    }

    private static void sanitizeCustomMetadata(JSONObject jSONObject) {
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            if (excludeKeys.contains(str)) {
                keys.remove();
                jSONObject.remove(str);
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("Removing ");
                sb.append(str);
                sb.append(" from addition metadata. See Javadoc for valid keys.");
                logger2.info(sb.toString());
            } else {
                try {
                    if (jSONObject.get(str) instanceof JSONObject) {
                        logger.info("Additional metadata should be a flat JSON object.");
                    }
                } catch (JSONException unused) {
                    logger.warn("Invalid metadata JSON structure");
                }
            }
        }
    }

    public static void send() {
        Logger.sendAnalytics(null);
    }

    public static void send(WLRequestListener wLRequestListener) {
        Logger.sendAnalytics(wLRequestListener);
    }

    public static void logAppForeground() {
        WLLifecycleHelper.getInstance().logAppForeground();
    }

    public static void logAppBackground() {
        WLLifecycleHelper.getInstance().logAppBackground();
    }

    public static void logAppCrash(Throwable th) {
        WLLifecycleHelper.getInstance().logAppCrash(th);
    }
}
