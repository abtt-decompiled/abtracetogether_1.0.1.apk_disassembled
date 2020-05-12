package com.worklight.wlclient.api;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import com.worklight.common.Logger;
import com.worklight.common.WLAnalytics;
import com.worklight.common.WLAnalytics.DeviceEvent;
import java.util.Date;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class WLLifecycleHelper {
    private static final long ACTIVITY_DELAY = 500;
    private static String appSessionID;
    /* access modifiers changed from: private */
    public static Long appUseStartTimestamp;
    private static Context context;
    private static Runnable delayedCheck;
    private static Handler handler;
    private static WLLifecycleHelper instance;
    /* access modifiers changed from: private */
    public static boolean isPaused;
    private static Logger logger = Logger.getInstance(WLLifecycleHelper.class.getName());

    public enum AppClosedBy {
        USER,
        CRASH
    }

    public static synchronized WLLifecycleHelper getInstance() {
        WLLifecycleHelper wLLifecycleHelper;
        synchronized (WLLifecycleHelper.class) {
            if (instance != null) {
                wLLifecycleHelper = instance;
            } else {
                throw new IllegalStateException("WLLifecycleHelper has not yet been initialized. Call WLLifecycleHelper.init()");
            }
        }
        return wLLifecycleHelper;
    }

    public static void init(Context context2) {
        if (instance == null) {
            instance = new WLLifecycleHelper(context2);
        }
    }

    private WLLifecycleHelper(Context context2) {
        isPaused = true;
        handler = new Handler(Looper.getMainLooper());
        appUseStartTimestamp = null;
        context = context2;
    }

    public void onResume() {
        String str = "onResume";
        Logger.enter(getClass().getSimpleName(), str);
        isPaused = false;
        Runnable runnable = delayedCheck;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        logAppForeground();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void onPause() {
        String str = "onPause";
        Logger.enter(getClass().getSimpleName(), str);
        isPaused = true;
        Runnable runnable = delayedCheck;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        Handler handler2 = handler;
        AnonymousClass1 r2 = new Runnable() {
            public void run() {
                String str = "run";
                Logger.enter(getClass().getSimpleName(), str);
                if (WLLifecycleHelper.isPaused) {
                    WLLifecycleHelper.this.logAppBackground();
                    WLLifecycleHelper.appUseStartTimestamp = null;
                }
                Logger.exit(getClass().getSimpleName(), str);
            }
        };
        delayedCheck = r2;
        handler2.postDelayed(r2, ACTIVITY_DELAY);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void logAppForeground() {
        String str = "logAppForeground";
        Logger.enter(getClass().getSimpleName(), str);
        if (appUseStartTimestamp == null) {
            appUseStartTimestamp = Long.valueOf(new Date().getTime());
            appSessionID = UUID.randomUUID().toString();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("$category", DeviceEvent.LIFECYCLE.toString());
                jSONObject.put("$timestamp", appUseStartTimestamp);
                jSONObject.put("$appSessionID", appSessionID);
            } catch (JSONException e) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("JSONException encountered logging app session: ");
                sb.append(e.getMessage());
                logger2.debug(sb.toString());
            }
            WLAnalytics.log("appSession", jSONObject);
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void logAppBackground() {
        String str = "logAppBackground";
        Logger.enter(getClass().getSimpleName(), str);
        logAppSession(false, null);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void logAppCrash(Throwable th) {
        String str = "logAppCrash";
        Logger.enter(getClass().getSimpleName(), str);
        logAppSession(true, th);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void logAppSession(boolean z, Throwable th) {
        String str = "logAppSession";
        Logger.enter(getClass().getSimpleName(), str);
        logDefaultUserContext();
        if (appUseStartTimestamp == null) {
            String str2 = z ? "app crash" : "app session";
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to record an ");
            sb.append(str2);
            sb.append(" without a starting timestamp");
            logger2.debug(sb.toString());
            Logger.exit(getClass().getSimpleName(), str);
            return;
        }
        long time = new Date().getTime();
        AppClosedBy appClosedBy = z ? AppClosedBy.CRASH : AppClosedBy.USER;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("$category", DeviceEvent.LIFECYCLE.toString());
            jSONObject.put("$duration", time - appUseStartTimestamp.longValue());
            jSONObject.put("$closedBy", appClosedBy.toString());
            jSONObject.put("$appSessionID", appSessionID);
        } catch (JSONException e) {
            Logger logger3 = logger;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("JSONException encountered logging app session: ");
            sb2.append(e.getMessage());
            logger3.debug(sb2.toString());
        }
        WLAnalytics.log("appSession", jSONObject, th);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void logDefaultUserContext() {
        String str = "userSwitch";
        String str2 = "logDefaultUserContext";
        Logger.enter(getClass().getSimpleName(), str2);
        if (context.getSharedPreferences(WLAnalytics.SHARED_PREF_KEY, 0).getString(WLAnalytics.SHARED_PREF_KEY_USER_ID, "").isEmpty()) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("$category", str);
                jSONObject.put("$userID", getDefaultUserID());
                jSONObject.put("$appSessionID", getAppSessionID());
                WLAnalytics.log(str, jSONObject);
            } catch (JSONException e) {
                Logger instance2 = Logger.getInstance("wl.analytics");
                StringBuilder sb = new StringBuilder();
                sb.append("JSONException encountered logging initial context: ");
                sb.append(e.getMessage());
                instance2.debug(sb.toString());
                Logger.exit(getClass().getSimpleName(), str2);
                return;
            }
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private String getDefaultUserID() {
        String str;
        String str2 = "getDefaultUserID";
        Logger.enter(getClass().getSimpleName(), str2);
        if (context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
            try {
                str = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            } catch (SecurityException e) {
                logger.warn(e.getMessage());
            }
            String string = Secure.getString(context.getContentResolver(), "android_id");
            Logger.exit(getClass().getSimpleName(), str2);
            StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(str);
            return sb.toString();
        }
        str = "";
        String string2 = Secure.getString(context.getContentResolver(), "android_id");
        Logger.exit(getClass().getSimpleName(), str2);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(string2);
        sb2.append(str);
        return sb2.toString();
    }

    public static String getAppSessionID() {
        String str = appSessionID;
        if (str != null) {
            return str;
        }
        return null;
    }
}
