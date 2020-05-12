package ca.albertahealthservices.contacttracing.streetpass;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/BlacklistEntry;", "", "uniqueIdentifier", "", "timeEntered", "", "(Ljava/lang/String;J)V", "getTimeEntered", "()J", "getUniqueIdentifier", "()Ljava/lang/String;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BlacklistEntry.kt */
public final class BlacklistEntry {
    private final long timeEntered;
    private final String uniqueIdentifier;

    public BlacklistEntry(String str, long j) {
        Intrinsics.checkParameterIsNotNull(str, "uniqueIdentifier");
        this.uniqueIdentifier = str;
        this.timeEntered = j;
    }

    public final long getTimeEntered() {
        return this.timeEntered;
    }

    public final String getUniqueIdentifier() {
        return this.uniqueIdentifier;
    }
}
