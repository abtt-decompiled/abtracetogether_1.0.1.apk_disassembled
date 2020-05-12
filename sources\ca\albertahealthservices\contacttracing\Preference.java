package ca.albertahealthservices.contacttracing;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0011J\u0010\u0010\u001a\u001a\u00020\u00042\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011J\u000e\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010 \u001a\u00020\u0004J\u0016\u0010!\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u0013J\u0016\u0010#\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u0015J\u0016\u0010%\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u001dJ\u0016\u0010&\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u0015J\u0016\u0010'\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010(\u001a\u00020\u0015J\u0016\u0010)\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u0015J\u0016\u0010*\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010+\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010,\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u0013J\u0016\u0010-\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010.\u001a\u00020/J\u0016\u00100\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010.\u001a\u00020/R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"Lca/albertahealthservices/contacttracing/Preference;", "", "()V", "ANNOUNCEMENT", "", "CHECK_POINT", "EXPIRY_TIME", "IS_ONBOARDED", "LAST_FETCH_TIME", "LAST_PURGE_TIME", "NEXT_FETCH_TIME", "PHONE_NUMBER", "PREF_ID", "UUID", "UUID_RETRY_ATTEMPTS", "getAnnouncement", "context", "Landroid/content/Context;", "getCheckpoint", "", "getExpiryTimeInMillis", "", "getLastFetchTimeInMillis", "getLastPurgeTime", "getNextFetchTimeInMillis", "getPhoneNumber", "getUUID", "getUUIDRetryAttempts", "isOnBoarded", "", "putAnnouncement", "", "announcement", "putCheckpoint", "value", "putExpiryTimeInMillis", "time", "putIsOnBoarded", "putLastFetchTimeInMillis", "putLastPurgeTime", "lastPurgeTime", "putNextFetchTimeInMillis", "putPhoneNumber", "putUUID", "putUUIDRetryAttempts", "registerListener", "listener", "Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;", "unregisterListener", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: Preference.kt */
public final class Preference {
    private static final String ANNOUNCEMENT = "ANNOUNCEMENT";
    private static final String CHECK_POINT = "CHECK_POINT";
    private static final String EXPIRY_TIME = "EXPIRY_TIME";
    public static final Preference INSTANCE = new Preference();
    private static final String IS_ONBOARDED = "IS_ONBOARDED";
    private static final String LAST_FETCH_TIME = "LAST_FETCH_TIME";
    private static final String LAST_PURGE_TIME = "LAST_PURGE_TIME";
    private static final String NEXT_FETCH_TIME = "NEXT_FETCH_TIME";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String PREF_ID = "Tracer_pref";
    private static final String UUID = "UUID";
    private static final String UUID_RETRY_ATTEMPTS = "UUID_RETRY_ATTEMPTS";

    private Preference() {
    }

    public final int getUUIDRetryAttempts(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context.getSharedPreferences(PREF_ID, 0).getInt(UUID_RETRY_ATTEMPTS, 0);
    }

    public final void putUUIDRetryAttempts(Context context, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        context.getSharedPreferences(PREF_ID, 0).edit().putInt(UUID_RETRY_ATTEMPTS, i).apply();
    }

    public final void putUUID(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "value");
        context.getSharedPreferences(PREF_ID, 0).edit().putString(UUID, str).apply();
    }

    public final String getUUID(Context context) {
        String str = "";
        if (context == null) {
            return str;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ID, 0);
        if (sharedPreferences == null) {
            return str;
        }
        String string = sharedPreferences.getString(UUID, str);
        return string != null ? string : str;
    }

    public final void putIsOnBoarded(Context context, boolean z) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        context.getSharedPreferences(PREF_ID, 0).edit().putBoolean(IS_ONBOARDED, z).apply();
    }

    public final boolean isOnBoarded(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context.getSharedPreferences(PREF_ID, 0).getBoolean(IS_ONBOARDED, false);
    }

    public final void putPhoneNumber(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "value");
        context.getSharedPreferences(PREF_ID, 0).edit().putString(PHONE_NUMBER, str).apply();
    }

    public final String getPhoneNumber(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        String str = "";
        String string = context.getSharedPreferences(PREF_ID, 0).getString(PHONE_NUMBER, str);
        return string != null ? string : str;
    }

    public final void putCheckpoint(Context context, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        context.getSharedPreferences(PREF_ID, 0).edit().putInt(CHECK_POINT, i).apply();
    }

    public final int getCheckpoint(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context.getSharedPreferences(PREF_ID, 0).getInt(CHECK_POINT, 0);
    }

    public final long getLastFetchTimeInMillis(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context.getSharedPreferences(PREF_ID, 0).getLong(LAST_FETCH_TIME, 0);
    }

    public final void putLastFetchTimeInMillis(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        context.getSharedPreferences(PREF_ID, 0).edit().putLong(LAST_FETCH_TIME, j).apply();
    }

    public final void putNextFetchTimeInMillis(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        context.getSharedPreferences(PREF_ID, 0).edit().putLong(NEXT_FETCH_TIME, j).apply();
    }

    public final long getNextFetchTimeInMillis(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context.getSharedPreferences(PREF_ID, 0).getLong(NEXT_FETCH_TIME, 0);
    }

    public final void putExpiryTimeInMillis(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        context.getSharedPreferences(PREF_ID, 0).edit().putLong(EXPIRY_TIME, j).apply();
    }

    public final long getExpiryTimeInMillis(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context.getSharedPreferences(PREF_ID, 0).getLong(EXPIRY_TIME, 0);
    }

    public final void putAnnouncement(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "announcement");
        context.getSharedPreferences(PREF_ID, 0).edit().putString(ANNOUNCEMENT, str).apply();
    }

    public final String getAnnouncement(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        String str = "";
        String string = context.getSharedPreferences(PREF_ID, 0).getString(ANNOUNCEMENT, str);
        return string != null ? string : str;
    }

    public final void putLastPurgeTime(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        context.getSharedPreferences(PREF_ID, 0).edit().putLong(LAST_PURGE_TIME, j).apply();
    }

    public final long getLastPurgeTime(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context.getSharedPreferences(PREF_ID, 0).getLong(LAST_PURGE_TIME, 0);
    }

    public final void registerListener(Context context, OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(onSharedPreferenceChangeListener, "listener");
        context.getSharedPreferences(PREF_ID, 0).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public final void unregisterListener(Context context, OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(onSharedPreferenceChangeListener, "listener");
        context.getSharedPreferences(PREF_ID, 0).unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}
