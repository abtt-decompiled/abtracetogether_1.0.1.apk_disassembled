package ca.albertahealthservices.contacttracing.logging;

import android.os.Build.VERSION;
import android.os.PowerManager;
import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lca/albertahealthservices/contacttracing/logging/CentralLog;", "", "()V", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: CentralLog.kt */
public final class CentralLog {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static PowerManager pm;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fJ \u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u0016\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fJ\b\u0010\u0010\u001a\u00020\fH\u0002J\u0016\u0010\u0011\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fJ\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0004J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\u0016\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fR\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\u0017"}, d2 = {"Lca/albertahealthservices/contacttracing/logging/CentralLog$Companion;", "", "()V", "pm", "Landroid/os/PowerManager;", "getPm", "()Landroid/os/PowerManager;", "setPm", "(Landroid/os/PowerManager;)V", "d", "", "tag", "", "message", "e", "", "getIdleStatus", "i", "setPowerManager", "powerManager", "shouldLog", "", "w", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: CentralLog.kt */
    public static final class Companion {
        private final boolean shouldLog() {
            return false;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final PowerManager getPm() {
            return CentralLog.pm;
        }

        public final void setPm(PowerManager powerManager) {
            CentralLog.pm = powerManager;
        }

        public final void setPowerManager(PowerManager powerManager) {
            Intrinsics.checkParameterIsNotNull(powerManager, "powerManager");
            setPm(powerManager);
        }

        private final String getIdleStatus() {
            if (VERSION.SDK_INT < 23) {
                return " NO-DOZE-FEATURE ";
            }
            PowerManager pm = getPm();
            return (pm == null || true != pm.isDeviceIdleMode()) ? " NOT-IDLE " : " IDLE ";
        }

        public final void d(String str, String str2) {
            Intrinsics.checkParameterIsNotNull(str, "tag");
            Intrinsics.checkParameterIsNotNull(str2, "message");
            Companion companion = this;
            if (companion.shouldLog()) {
                StringBuilder sb = new StringBuilder();
                sb.append(companion.getIdleStatus());
                sb.append(str2);
                Log.d(str, sb.toString());
                SDLog sDLog = SDLog.INSTANCE;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(companion.getIdleStatus());
                sb2.append(str2);
                sDLog.d(str, sb2.toString());
            }
        }

        public final void d(String str, String str2, Throwable th) {
            Intrinsics.checkParameterIsNotNull(str, "tag");
            Intrinsics.checkParameterIsNotNull(str2, "message");
            Companion companion = this;
            if (companion.shouldLog()) {
                StringBuilder sb = new StringBuilder();
                sb.append(companion.getIdleStatus());
                sb.append(str2);
                Log.d(str, sb.toString(), th);
                SDLog sDLog = SDLog.INSTANCE;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(companion.getIdleStatus());
                sb2.append(str2);
                sDLog.d(str, sb2.toString());
            }
        }

        public final void w(String str, String str2) {
            Intrinsics.checkParameterIsNotNull(str, "tag");
            Intrinsics.checkParameterIsNotNull(str2, "message");
            Companion companion = this;
            if (companion.shouldLog()) {
                StringBuilder sb = new StringBuilder();
                sb.append(companion.getIdleStatus());
                sb.append(str2);
                Log.w(str, sb.toString());
                SDLog sDLog = SDLog.INSTANCE;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(companion.getIdleStatus());
                sb2.append(str2);
                sDLog.w(str, sb2.toString());
            }
        }

        public final void i(String str, String str2) {
            Intrinsics.checkParameterIsNotNull(str, "tag");
            Intrinsics.checkParameterIsNotNull(str2, "message");
            Companion companion = this;
            if (companion.shouldLog()) {
                StringBuilder sb = new StringBuilder();
                sb.append(companion.getIdleStatus());
                sb.append(str2);
                Log.i(str, sb.toString());
                SDLog sDLog = SDLog.INSTANCE;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(companion.getIdleStatus());
                sb2.append(str2);
                sDLog.i(str, sb2.toString());
            }
        }

        public final void e(String str, String str2) {
            Intrinsics.checkParameterIsNotNull(str, "tag");
            Intrinsics.checkParameterIsNotNull(str2, "message");
            Companion companion = this;
            if (companion.shouldLog()) {
                StringBuilder sb = new StringBuilder();
                sb.append(companion.getIdleStatus());
                sb.append(str2);
                Log.e(str, sb.toString());
                SDLog sDLog = SDLog.INSTANCE;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(companion.getIdleStatus());
                sb2.append(str2);
                sDLog.e(str, sb2.toString());
            }
        }
    }
}
