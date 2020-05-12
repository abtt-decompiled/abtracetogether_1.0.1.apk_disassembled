package ca.albertahealthservices.contacttracing.idmanager;

import android.content.Context;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.CoroutineContext;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Job;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ!\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011H\u0002¢\u0006\u0002\u0010\u0012J\u001b\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00112\u0006\u0010\u0014\u001a\u00020\u0004H\u0002¢\u0006\u0002\u0010\u0015J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u000b\u001a\u00020\fJ\u001e\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0002J\u000e\u0010\u001a\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u001b\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u0006X\u0005¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006 "}, d2 = {"Lca/albertahealthservices/contacttracing/idmanager/TempIDManager;", "Lkotlinx/coroutines/CoroutineScope;", "()V", "TAG", "", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "bmValid", "", "context", "Landroid/content/Context;", "convertToQueue", "Ljava/util/Queue;", "Lca/albertahealthservices/contacttracing/idmanager/TemporaryID;", "tempIDArray", "", "([Lca/albertahealthservices/contacttracing/idmanager/TemporaryID;)Ljava/util/Queue;", "convertToTemporaryIDs", "tempIDString", "(Ljava/lang/String;)[Lca/albertahealthservices/contacttracing/idmanager/TemporaryID;", "getTemporaryIDs", "Lkotlinx/coroutines/Job;", "getValidOrLastTemporaryID", "tempIDQueue", "needToRollNewTempID", "needToUpdate", "retrieveTemporaryID", "storeTemporaryIDs", "", "packet", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: TempIDManager.kt */
public final class TempIDManager implements CoroutineScope {
    public static final TempIDManager INSTANCE = new TempIDManager();
    private static final String TAG = "TempIDManager";
    private final /* synthetic */ CoroutineScope $$delegate_0 = CoroutineScopeKt.MainScope();

    public CoroutineContext getCoroutineContext() {
        return this.$$delegate_0.getCoroutineContext();
    }

    private TempIDManager() {
    }

    public final void storeTemporaryIDs(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "packet");
        CentralLog.Companion.d(TAG, "[TempID] Storing temporary IDs into internal storage...");
        FilesKt.writeText$default(new File(context.getFilesDir(), "tempIDs"), str, null, 2, null);
    }

    public final TemporaryID retrieveTemporaryID(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        File file = new File(context.getFilesDir(), "tempIDs");
        if (!file.exists()) {
            return null;
        }
        String readText$default = FilesKt.readText$default(file, null, 1, null);
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("[TempID] fetched broadcastmessage from file:  ");
        sb.append(readText$default);
        companion.d(TAG, sb.toString());
        return getValidOrLastTemporaryID(context, convertToQueue(convertToTemporaryIDs(readText$default)));
    }

    private final TemporaryID getValidOrLastTemporaryID(Context context, Queue<TemporaryID> queue) {
        Companion companion = CentralLog.Companion;
        String str = TAG;
        companion.d(str, "[TempID] Retrieving Temporary ID");
        long currentTimeMillis = System.currentTimeMillis();
        int i = 0;
        while (true) {
            if (queue.size() <= 1) {
                break;
            }
            TemporaryID temporaryID = (TemporaryID) queue.peek();
            temporaryID.print();
            if (temporaryID.isValidForCurrentTime()) {
                CentralLog.Companion.d(str, "[TempID] Breaking out of the loop");
                break;
            }
            queue.poll();
            i++;
        }
        TemporaryID temporaryID2 = (TemporaryID) queue.peek();
        long j = (long) 1000;
        long startTime = temporaryID2.getStartTime() * j;
        long expiryTime = temporaryID2.getExpiryTime() * j;
        Companion companion2 = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("[TempID Total number of items in queue: ");
        sb.append(queue.size());
        companion2.d(str, sb.toString());
        Companion companion3 = CentralLog.Companion;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[TempID Number of items popped from queue: ");
        sb2.append(i);
        companion3.d(str, sb2.toString());
        Companion companion4 = CentralLog.Companion;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("[TempID] Current time: ");
        sb3.append(currentTimeMillis);
        companion4.d(str, sb3.toString());
        Companion companion5 = CentralLog.Companion;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("[TempID] Start time: ");
        sb4.append(startTime);
        companion5.d(str, sb4.toString());
        Companion companion6 = CentralLog.Companion;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("[TempID] Expiry time: ");
        sb5.append(expiryTime);
        companion6.d(str, sb5.toString());
        CentralLog.Companion.d(str, "[TempID] Updating expiry time");
        Preference.INSTANCE.putExpiryTimeInMillis(context, expiryTime);
        Intrinsics.checkExpressionValueIsNotNull(temporaryID2, "foundTempID");
        return temporaryID2;
    }

    private final TemporaryID[] convertToTemporaryIDs(String str) {
        Gson create = new GsonBuilder().create();
        Intrinsics.checkExpressionValueIsNotNull(create, "GsonBuilder().create()");
        TemporaryID[] temporaryIDArr = (TemporaryID[]) create.fromJson(str, TemporaryID[].class);
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("[TempID] After GSON conversion: ");
        sb.append(temporaryIDArr[0].getTempID());
        sb.append(' ');
        sb.append(temporaryIDArr[0].getStartTime());
        companion.d(TAG, sb.toString());
        Intrinsics.checkExpressionValueIsNotNull(temporaryIDArr, "tempIDResult");
        return temporaryIDArr;
    }

    private final Queue<TemporaryID> convertToQueue(TemporaryID[] temporaryIDArr) {
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("[TempID] Before Sort: ");
        sb.append(temporaryIDArr[0]);
        String sb2 = sb.toString();
        String str = TAG;
        companion.d(str, sb2);
        if (temporaryIDArr.length > 1) {
            ArraysKt.sortWith(temporaryIDArr, new TempIDManager$convertToQueue$$inlined$sortBy$1());
        }
        Companion companion2 = CentralLog.Companion;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("[TempID] After Sort: ");
        sb3.append(temporaryIDArr[0]);
        companion2.d(str, sb3.toString());
        Queue<TemporaryID> linkedList = new LinkedList<>();
        for (TemporaryID offer : temporaryIDArr) {
            linkedList.offer(offer);
        }
        Companion companion3 = CentralLog.Companion;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("[TempID] Retrieving from Queue: ");
        sb4.append((TemporaryID) linkedList.peek());
        companion3.d(str, sb4.toString());
        return linkedList;
    }

    public final Job getTemporaryIDs(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return BuildersKt__Builders_commonKt.launch$default(this, null, null, new TempIDManager$getTemporaryIDs$1(context, null), 3, null);
    }

    public final boolean needToUpdate(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        long nextFetchTimeInMillis = Preference.INSTANCE.getNextFetchTimeInMillis(context);
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = currentTimeMillis >= nextFetchTimeInMillis;
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("Need to update and fetch TemporaryIDs? ");
        sb.append(nextFetchTimeInMillis);
        sb.append(" vs ");
        sb.append(currentTimeMillis);
        sb.append(": ");
        sb.append(z);
        companion.i(TAG, sb.toString());
        return z;
    }

    public final boolean needToRollNewTempID(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        long expiryTimeInMillis = Preference.INSTANCE.getExpiryTimeInMillis(context);
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = currentTimeMillis >= expiryTimeInMillis;
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("[TempID] Need to get new TempID? ");
        sb.append(expiryTimeInMillis);
        sb.append(" vs ");
        sb.append(currentTimeMillis);
        sb.append(": ");
        sb.append(z);
        companion.d(TAG, sb.toString());
        return z;
    }

    public final boolean bmValid(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return System.currentTimeMillis() < Preference.INSTANCE.getExpiryTimeInMillis(context);
    }
}
