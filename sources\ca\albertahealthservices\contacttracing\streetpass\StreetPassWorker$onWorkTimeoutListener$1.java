package ca.albertahealthservices.contacttracing.streetpass;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.streetpass.Work.OnWorkTimeoutListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"ca/albertahealthservices/contacttracing/streetpass/StreetPassWorker$onWorkTimeoutListener$1", "Lca/albertahealthservices/contacttracing/streetpass/Work$OnWorkTimeoutListener;", "onWorkTimeout", "", "work", "Lca/albertahealthservices/contacttracing/streetpass/Work;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: StreetPassWorker.kt */
public final class StreetPassWorker$onWorkTimeoutListener$1 implements OnWorkTimeoutListener {
    final /* synthetic */ StreetPassWorker this$0;

    StreetPassWorker$onWorkTimeoutListener$1(StreetPassWorker streetPassWorker) {
        this.this$0 = streetPassWorker;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0108 A[Catch:{ Exception -> 0x010c }] */
    public void onWorkTimeout(Work work) {
        Object obj;
        BluetoothGatt gatt;
        Intrinsics.checkParameterIsNotNull(work, "work");
        if (!this.this$0.isCurrentlyWorkedOn(work.getDevice().getAddress())) {
            CentralLog.Companion.i(this.this$0.TAG, "Work already removed. Timeout ineffective??.");
        }
        Companion companion = CentralLog.Companion;
        String access$getTAG$p = this.this$0.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Work timed out for ");
        sb.append(work.getDevice().getAddress());
        sb.append(" @ ");
        sb.append(work.getConnectable().getRssi());
        sb.append(" queued for ");
        sb.append(work.getChecklist().getStarted().getTimePerformed() - work.getTimeStamp());
        sb.append("ms");
        companion.e(access$getTAG$p, sb.toString());
        Companion companion2 = CentralLog.Companion;
        String access$getTAG$p2 = this.this$0.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(work.getDevice().getAddress());
        sb2.append(" work status: ");
        sb2.append(work.getChecklist());
        sb2.append('.');
        companion2.e(access$getTAG$p2, sb2.toString());
        String str = ": ";
        if (!work.getChecklist().getConnected().getStatus()) {
            Companion companion3 = CentralLog.Companion;
            String access$getTAG$p3 = this.this$0.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("No connection formed for ");
            sb3.append(work.getDevice().getAddress());
            companion3.e(access$getTAG$p3, sb3.toString());
            String address = work.getDevice().getAddress();
            Work access$getCurrentWork$p = this.this$0.currentWork;
            if (access$getCurrentWork$p != null) {
                BluetoothDevice device = access$getCurrentWork$p.getDevice();
                if (device != null) {
                    obj = device.getAddress();
                    if (Intrinsics.areEqual((Object) address, obj)) {
                        this.this$0.currentWork = null;
                    }
                    gatt = work.getGatt();
                    if (gatt != null) {
                        gatt.close();
                    }
                    this.this$0.finishWork(work);
                }
            }
            obj = null;
            if (Intrinsics.areEqual((Object) address, obj)) {
            }
            try {
                gatt = work.getGatt();
                if (gatt != null) {
                }
            } catch (Exception e) {
                Companion companion4 = CentralLog.Companion;
                String access$getTAG$p4 = this.this$0.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Unexpected error while attempting to close clientIf to ");
                sb4.append(work.getDevice().getAddress());
                sb4.append(str);
                sb4.append(e.getLocalizedMessage());
                companion4.e(access$getTAG$p4, sb4.toString());
            }
            this.this$0.finishWork(work);
        } else if (!work.getChecklist().getConnected().getStatus() || work.getChecklist().getDisconnected().getStatus()) {
            Companion companion5 = CentralLog.Companion;
            String access$getTAG$p5 = this.this$0.TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Disconnected but callback not invoked in time. Waiting.: ");
            sb5.append(work.getDevice().getAddress());
            sb5.append(str);
            sb5.append(work.getChecklist());
            companion5.e(access$getTAG$p5, sb5.toString());
        } else {
            String str2 = "Failed to clean up work, bluetooth state likely changed or other device's advertiser stopped: ";
            if (work.getChecklist().getReadCharacteristic().getStatus() || work.getChecklist().getWriteCharacteristic().getStatus() || work.getChecklist().getSkipped().getStatus()) {
                Companion companion6 = CentralLog.Companion;
                String access$getTAG$p6 = this.this$0.TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Connected but did not disconnect in time for ");
                sb6.append(work.getDevice().getAddress());
                companion6.e(access$getTAG$p6, sb6.toString());
                try {
                    BluetoothGatt gatt2 = work.getGatt();
                    if (gatt2 != null) {
                        gatt2.disconnect();
                    }
                    if (work.getGatt() == null) {
                        this.this$0.currentWork = null;
                        this.this$0.finishWork(work);
                    }
                } catch (Throwable th) {
                    Companion companion7 = CentralLog.Companion;
                    String access$getTAG$p7 = this.this$0.TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str2);
                    sb7.append(th.getLocalizedMessage());
                    companion7.e(access$getTAG$p7, sb7.toString());
                }
            } else {
                Companion companion8 = CentralLog.Companion;
                String access$getTAG$p8 = this.this$0.TAG;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Connected but did nothing for ");
                sb8.append(work.getDevice().getAddress());
                companion8.e(access$getTAG$p8, sb8.toString());
                try {
                    BluetoothGatt gatt3 = work.getGatt();
                    if (gatt3 != null) {
                        gatt3.disconnect();
                    }
                    if (work.getGatt() == null) {
                        this.this$0.currentWork = null;
                        this.this$0.finishWork(work);
                    }
                } catch (Throwable th2) {
                    Companion companion9 = CentralLog.Companion;
                    String access$getTAG$p9 = this.this$0.TAG;
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str2);
                    sb9.append(th2.getLocalizedMessage());
                    companion9.e(access$getTAG$p9, sb9.toString());
                }
            }
        }
    }
}
