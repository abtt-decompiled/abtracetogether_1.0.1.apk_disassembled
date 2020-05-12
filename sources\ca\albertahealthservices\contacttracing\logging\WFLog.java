package ca.albertahealthservices.contacttracing.logging;

import com.worklight.common.Logger;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lca/albertahealthservices/contacttracing/logging/WFLog;", "", "()V", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: WFLog.kt */
public final class WFLog {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static final String TAG = TAG;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004J\u0018\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\fJ\u0018\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\fJ\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lca/albertahealthservices/contacttracing/logging/WFLog$Companion;", "", "()V", "TAG", "", "log", "", "message", "logDebug", "logError", "logErrorWithException", "e", "", "logFatalWithException", "logInfo", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: WFLog.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void logError(String str) {
            Intrinsics.checkParameterIsNotNull(str, "message");
            Logger.getInstance(WFLog.TAG).error(str);
            Logger.send();
        }

        public final void logErrorWithException(String str, Throwable th) {
            Intrinsics.checkParameterIsNotNull(str, "message");
            Logger.getInstance(WFLog.TAG).error(str, new JSONObject(), th);
            Logger.send();
        }

        public final void logFatalWithException(String str, Throwable th) {
            Intrinsics.checkParameterIsNotNull(str, "message");
            Logger.getInstance(WFLog.TAG).fatal(str, new JSONObject(), th);
            Logger.send();
        }

        public final void logDebug(String str) {
            Intrinsics.checkParameterIsNotNull(str, "message");
            Logger.getInstance(WFLog.TAG).debug(str);
            Logger.send();
        }

        public final void logInfo(String str) {
            Intrinsics.checkParameterIsNotNull(str, "message");
            Logger.getInstance(WFLog.TAG).error(str);
            Logger.send();
        }

        public final void log(String str) {
            Intrinsics.checkParameterIsNotNull(str, "message");
            Logger.getInstance(WFLog.TAG).log(str);
            Logger.send();
        }
    }
}
