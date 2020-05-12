package ca.albertahealthservices.contacttracing.idmanager;

import ca.albertahealthservices.contacttracing.logging.CentralLog;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\u0010R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0012"}, d2 = {"Lca/albertahealthservices/contacttracing/idmanager/TemporaryID;", "", "startTime", "", "tempID", "", "expiryTime", "(JLjava/lang/String;J)V", "getExpiryTime", "()J", "getStartTime", "getTempID", "()Ljava/lang/String;", "isValidForCurrentTime", "", "print", "", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: TemporaryID.kt */
public final class TemporaryID {
    public static final Companion Companion = new Companion(null);
    private static final String TAG = "TempID";
    private final long expiryTime;
    private final long startTime;
    private final String tempID;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lca/albertahealthservices/contacttracing/idmanager/TemporaryID$Companion;", "", "()V", "TAG", "", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: TemporaryID.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public TemporaryID(long j, String str, long j2) {
        Intrinsics.checkParameterIsNotNull(str, "tempID");
        this.startTime = j;
        this.tempID = str;
        this.expiryTime = j2;
    }

    public final long getStartTime() {
        return this.startTime;
    }

    public final String getTempID() {
        return this.tempID;
    }

    public final long getExpiryTime() {
        return this.expiryTime;
    }

    public final boolean isValidForCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = (long) 1000;
        return currentTimeMillis > this.startTime * j && currentTimeMillis < this.expiryTime * j;
    }

    public final void print() {
        long j = (long) 1000;
        long j2 = this.startTime * j;
        long j3 = this.expiryTime * j;
        ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("[TempID] Start time: ");
        sb.append(j2);
        String sb2 = sb.toString();
        String str = TAG;
        companion.d(str, sb2);
        ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion2 = CentralLog.Companion;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("[TempID] Expiry time: ");
        sb3.append(j3);
        companion2.d(str, sb3.toString());
    }
}
