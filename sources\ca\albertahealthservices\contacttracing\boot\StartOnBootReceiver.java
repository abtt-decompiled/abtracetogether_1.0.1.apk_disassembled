package ca.albertahealthservices.contacttracing.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"Lca/albertahealthservices/contacttracing/boot/StartOnBootReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: StartOnBootReceiver.kt */
public final class StartOnBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        if (Intrinsics.areEqual((Object) "android.intent.action.BOOT_COMPLETED", (Object) intent.getAction())) {
            String str = "StartOnBootReceiver";
            CentralLog.Companion.d(str, "boot completed received");
            try {
                CentralLog.Companion.d(str, "Attempting to start service");
                Utils.INSTANCE.scheduleStartMonitoringService(context, 500);
            } catch (Throwable th) {
                Companion companion = CentralLog.Companion;
                String localizedMessage = th.getLocalizedMessage();
                Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
                companion.e(str, localizedMessage);
                th.printStackTrace();
            }
        }
    }
}
