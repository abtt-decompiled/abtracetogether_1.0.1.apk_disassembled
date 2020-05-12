package com.worklight.wlclient;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import androidx.core.os.EnvironmentCompat;
import com.worklight.common.Logger;
import com.worklight.common.WLAnalytics;
import com.worklight.common.WLAnalytics.DeviceEvent;
import com.worklight.common.WLConfig;
import com.worklight.common.security.WLDeviceAuthManager;
import com.worklight.wlclient.auth.WLAuthorizationManagerInternal;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;

public final class OkHttpInterceptor implements Interceptor {
    private static final String LOG_TAG = OkHttpInterceptor.class.getName();
    protected static final String REQUEST_METADATA = "requestMetadata";
    private final Context context;
    private String trackingId;
    private String url;
    private WLConfig wlConfig;

    public OkHttpInterceptor(WLConfig wLConfig, Context context2) {
        this.wlConfig = wLConfig;
        this.context = context2;
    }

    public Response intercept(Chain chain) throws IOException {
        String str = "$outboundTimestamp";
        String str2 = "intercept";
        Logger.enter(getClass().getSimpleName(), str2);
        Request request = chain.request();
        String httpUrl = request.url().toString();
        String str3 = "?";
        if (httpUrl.contains(str3)) {
            httpUrl = httpUrl.substring(0, httpUrl.indexOf(str3));
        }
        this.url = httpUrl;
        this.trackingId = UUID.randomUUID().toString();
        Request build = request.newBuilder().addHeader("x-wl-analytics-tracking-id", this.trackingId).build();
        JSONObject jSONObject = new JSONObject();
        String str4 = EnvironmentCompat.MEDIA_UNKNOWN;
        try {
            str4 = WLDeviceAuthManager.getInstance().getDeviceUUID(this.context);
        } catch (Exception e) {
            try {
                Logger.getInstance(LOG_TAG).error("Could not get device id from WLDeviceAuthManager.", (Throwable) e);
            } catch (JSONException unused) {
            }
        }
        jSONObject.put("deviceID", str4);
        jSONObject.put("os", "android");
        jSONObject.put("clientID", WLAuthorizationManagerInternal.getInstance().getClientId());
        jSONObject.put("osVersion", VERSION.RELEASE);
        jSONObject.put("brand", Build.BRAND);
        jSONObject.put("model", Build.MODEL);
        jSONObject.put("mfpAppName", WLConfig.getInstance().getAppId());
        jSONObject.put("mfpAppVersion", WLConfig.getInstance().getApplicationVersion());
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0);
            jSONObject.put("appVersionDisplay", packageInfo.versionName);
            StringBuilder sb = new StringBuilder();
            sb.append(packageInfo.versionCode);
            sb.append("");
            jSONObject.put("appVersionCode", sb.toString());
            jSONObject.put("appStoreId", packageInfo.packageName);
        } catch (NameNotFoundException e2) {
            Logger.getInstance(LOG_TAG).error("Could not get PackageInfo.", (Throwable) e2);
        }
        String appLabel = getAppLabel(this.context);
        String str5 = "appStoreLabel";
        if (appLabel != null) {
            appLabel = URLEncoder.encode(appLabel, "utf-8");
        }
        jSONObject.put(str5, appLabel);
        Request build2 = build.newBuilder().addHeader("x-mfp-analytics-metadata", jSONObject.toString()).build();
        JSONObject jSONObject2 = new JSONObject();
        try {
            if (build2.body() != null) {
                long contentLength = build2.body().contentLength();
                if (contentLength != -1) {
                    jSONObject2.put("$bytesSent", contentLength);
                }
            }
            long time = new Date().getTime();
            jSONObject2.put("$path", this.url);
            jSONObject2.put("$category", DeviceEvent.NETWORK.toString());
            jSONObject2.put("$trackingid", this.trackingId);
            jSONObject2.put(str, time);
            jSONObject2.put("$requestMethod", build2.method());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(loggerMessage(this.url));
            sb2.append(" outbound");
            WLAnalytics.log(sb2.toString(), null);
        } catch (JSONException unused2) {
        }
        Response proceed = chain.proceed(build2);
        try {
            Long l = (Long) jSONObject2.get(str);
            long time2 = new Date().getTime();
            long longValue = time2 - l.longValue();
            jSONObject2.put("$inboundTimestamp", time2);
            jSONObject2.put("$roundTripTime", longValue);
            jSONObject2.put("$responseCode", proceed.code());
            ResponseBody body = proceed.body();
            if (body != null) {
                long contentLength2 = body.contentLength();
                if (contentLength2 != -1) {
                    jSONObject2.put("$bytesReceived", contentLength2);
                }
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(loggerMessage(this.url));
            sb3.append(" inbound");
            WLAnalytics.log(sb3.toString(), jSONObject2);
        } catch (Exception unused3) {
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return proceed;
    }

    private String loggerMessage(String str) {
        String str2 = "loggerMessage";
        Logger.enter(getClass().getSimpleName(), str2);
        if (str.contains(this.wlConfig.getHost())) {
            Logger.exit(getClass().getSimpleName(), str2);
            return "InternalRequestSender";
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return "ExternalRequestSender";
    }

    public String getAppLabel(Context context2) {
        ApplicationInfo applicationInfo;
        String str = "getAppLabel";
        Logger.enter(getClass().getSimpleName(), str);
        PackageManager packageManager = context2.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context2.getApplicationInfo().packageName, 0);
        } catch (NameNotFoundException e) {
            Logger.getInstance(LOG_TAG).error("Could not get ApplicationInfo.", (Throwable) e);
            applicationInfo = null;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : EnvironmentCompat.MEDIA_UNKNOWN);
    }
}
