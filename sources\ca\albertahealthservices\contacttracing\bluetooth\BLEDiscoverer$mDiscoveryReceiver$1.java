package ca.albertahealthservices.contacttracing.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Parcelable;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.ShortCompanionObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0012\u0010\b\u001a\u00020\u00032\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0002¨\u0006\u000b"}, d2 = {"ca/albertahealthservices/contacttracing/bluetooth/BLEDiscoverer$mDiscoveryReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "processScanResult", "scanResult", "Landroid/bluetooth/le/ScanResult;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BLEDiscoverer.kt */
public final class BLEDiscoverer$mDiscoveryReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ BLEDiscoverer this$0;

    BLEDiscoverer$mDiscoveryReceiver$1(BLEDiscoverer bLEDiscoverer) {
        this.this$0 = bLEDiscoverer;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        String action = intent.getAction();
        if (action != null) {
            int hashCode = action.hashCode();
            if (hashCode != -1780914469) {
                if (hashCode != 6759640) {
                    if (hashCode == 1167529923 && action.equals("android.bluetooth.device.action.FOUND")) {
                        Parcelable parcelableExtra = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                        Intrinsics.checkExpressionValueIsNotNull(parcelableExtra, "intent.getParcelableExtr…toothDevice.EXTRA_DEVICE)");
                        BluetoothDevice bluetoothDevice = (BluetoothDevice) parcelableExtra;
                        short shortExtra = intent.getShortExtra("android.bluetooth.device.extra.RSSI", ShortCompanionObject.MIN_VALUE);
                        Companion companion = CentralLog.Companion;
                        String access$getTAG$p = this.this$0.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Scanned Device address: ");
                        sb.append(bluetoothDevice.getAddress());
                        sb.append(" @ ");
                        sb.append(shortExtra);
                        companion.i(access$getTAG$p, sb.toString());
                        if (bluetoothDevice.getUuids() == null) {
                            Companion companion2 = CentralLog.Companion;
                            String access$getTAG$p2 = this.this$0.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Nope. No uuids cached for address: ");
                            sb2.append(bluetoothDevice.getAddress());
                            companion2.w(access$getTAG$p2, sb2.toString());
                        }
                    }
                } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_STARTED")) {
                    CentralLog.Companion.i(this.this$0.TAG, "Discovery started");
                }
            } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED")) {
                CentralLog.Companion.i(this.this$0.TAG, "Discovery ended");
            }
        }
    }

    private final void processScanResult(ScanResult scanResult) {
        if (scanResult != null) {
            scanResult.getDevice();
            scanResult.getRssi();
            Integer num = null;
            if (VERSION.SDK_INT >= 26) {
                int intValue = Integer.valueOf(scanResult.getTxPower()).intValue();
            }
        }
    }
}
