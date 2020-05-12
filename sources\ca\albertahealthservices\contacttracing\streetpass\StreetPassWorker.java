package ca.albertahealthservices.contacttracing.streetpass;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ca.albertahealthservices.contacttracing.BuildConfig;
import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.bluetooth.gatt.GATTKt;
import ca.albertahealthservices.contacttracing.idmanager.TempIDManager;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.protocol.BlueTrace;
import ca.albertahealthservices.contacttracing.protocol.BlueTraceProtocol;
import ca.albertahealthservices.contacttracing.protocol.CentralInterface;
import ca.albertahealthservices.contacttracing.services.BluetoothMonitoringService;
import ca.albertahealthservices.contacttracing.streetpass.Work.OnWorkTimeoutListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0003123B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0015J\u0006\u0010&\u001a\u00020'J\u000e\u0010(\u001a\u00020'2\u0006\u0010%\u001a\u00020\u0015J\u0010\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020+H\u0002J\u0010\u0010,\u001a\u00020$2\b\u0010-\u001a\u0004\u0018\u00010\u0006J\b\u0010.\u001a\u00020'H\u0002J\u0006\u0010/\u001a\u00020'J\u0006\u00100\u001a\u00020'R\u000e\u0010\u0005\u001a\u00020\u0006XD¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX.¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00060\rR\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0018\u001a\u00020\u0019¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u000bX.¢\u0006\u0002\n\u0000R\u0012\u0010\u001d\u001a\u00060\u001eR\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u000bX.¢\u0006\u0002\n\u0000R\u0014\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00150\"X\u0004¢\u0006\u0002\n\u0000¨\u00064"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "TAG", "", "blacklist", "", "Lca/albertahealthservices/contacttracing/streetpass/BlacklistEntry;", "blacklistHandler", "Landroid/os/Handler;", "blacklistReceiver", "Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker$BlacklistReceiver;", "bluetoothManager", "Landroid/bluetooth/BluetoothManager;", "characteristicV2", "Ljava/util/UUID;", "getContext", "()Landroid/content/Context;", "currentWork", "Lca/albertahealthservices/contacttracing/streetpass/Work;", "localBroadcastManager", "Landroidx/localbroadcastmanager/content/LocalBroadcastManager;", "onWorkTimeoutListener", "Lca/albertahealthservices/contacttracing/streetpass/Work$OnWorkTimeoutListener;", "getOnWorkTimeoutListener", "()Lca/albertahealthservices/contacttracing/streetpass/Work$OnWorkTimeoutListener;", "queueHandler", "scannedDeviceReceiver", "Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker$ScannedDeviceReceiver;", "serviceUUID", "timeoutHandler", "workQueue", "Ljava/util/concurrent/PriorityBlockingQueue;", "addWork", "", "work", "doWork", "", "finishWork", "getConnectionStatus", "device", "Landroid/bluetooth/BluetoothDevice;", "isCurrentlyWorkedOn", "address", "prepare", "terminateConnections", "unregisterReceivers", "BlacklistReceiver", "CentralGattCallback", "ScannedDeviceReceiver", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: StreetPassWorker.kt */
public final class StreetPassWorker {
    /* access modifiers changed from: private */
    public final String TAG;
    /* access modifiers changed from: private */
    public final List<BlacklistEntry> blacklist;
    /* access modifiers changed from: private */
    public Handler blacklistHandler;
    private final BlacklistReceiver blacklistReceiver;
    private final BluetoothManager bluetoothManager;
    /* access modifiers changed from: private */
    public final UUID characteristicV2;
    private final Context context;
    /* access modifiers changed from: private */
    public Work currentWork;
    private LocalBroadcastManager localBroadcastManager;
    private final OnWorkTimeoutListener onWorkTimeoutListener;
    private Handler queueHandler;
    private final ScannedDeviceReceiver scannedDeviceReceiver;
    /* access modifiers changed from: private */
    public final UUID serviceUUID;
    /* access modifiers changed from: private */
    public Handler timeoutHandler;
    /* access modifiers changed from: private */
    public final PriorityBlockingQueue<Work> workQueue = new PriorityBlockingQueue<>(5, Collections.reverseOrder());

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker$BlacklistReceiver;", "Landroid/content/BroadcastReceiver;", "(Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker;)V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: StreetPassWorker.kt */
    public final class BlacklistReceiver extends BroadcastReceiver {
        public BlacklistReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(intent, "intent");
            if (Intrinsics.areEqual((Object) GATTKt.ACTION_DEVICE_PROCESSED, (Object) intent.getAction())) {
                String stringExtra = intent.getStringExtra(GATTKt.DEVICE_ADDRESS);
                Companion companion = CentralLog.Companion;
                String access$getTAG$p = StreetPassWorker.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Adding to blacklist: ");
                sb.append(stringExtra);
                companion.d(access$getTAG$p, sb.toString());
                Intrinsics.checkExpressionValueIsNotNull(stringExtra, "deviceAddress");
                BlacklistEntry blacklistEntry = new BlacklistEntry(stringExtra, System.currentTimeMillis());
                StreetPassWorker.this.blacklist.add(blacklistEntry);
                StreetPassWorker.access$getBlacklistHandler$p(StreetPassWorker.this).postDelayed(new StreetPassWorker$BlacklistReceiver$onReceive$1(this, blacklistEntry), BluetoothMonitoringService.Companion.getBlacklistDuration());
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ \u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J \u0010\u0010\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\"\u0010\u0011\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J\"\u0010\u0013\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0018\u0010\u0015\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0016"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker$CentralGattCallback;", "Landroid/bluetooth/BluetoothGattCallback;", "work", "Lca/albertahealthservices/contacttracing/streetpass/Work;", "(Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker;Lca/albertahealthservices/contacttracing/streetpass/Work;)V", "getWork", "()Lca/albertahealthservices/contacttracing/streetpass/Work;", "endWorkConnection", "", "gatt", "Landroid/bluetooth/BluetoothGatt;", "onCharacteristicRead", "characteristic", "Landroid/bluetooth/BluetoothGattCharacteristic;", "status", "", "onCharacteristicWrite", "onConnectionStateChange", "newState", "onMtuChanged", "mtu", "onServicesDiscovered", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: StreetPassWorker.kt */
    public final class CentralGattCallback extends BluetoothGattCallback {
        final /* synthetic */ StreetPassWorker this$0;
        private final Work work;

        public CentralGattCallback(StreetPassWorker streetPassWorker, Work work2) {
            Intrinsics.checkParameterIsNotNull(work2, "work");
            this.this$0 = streetPassWorker;
            this.work = work2;
        }

        public final Work getWork() {
            return this.work;
        }

        public final void endWorkConnection(BluetoothGatt bluetoothGatt) {
            Intrinsics.checkParameterIsNotNull(bluetoothGatt, "gatt");
            Companion companion = CentralLog.Companion;
            String access$getTAG$p = this.this$0.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Ending connection with: ");
            BluetoothDevice device = bluetoothGatt.getDevice();
            Intrinsics.checkExpressionValueIsNotNull(device, "gatt.device");
            sb.append(device.getAddress());
            companion.i(access$getTAG$p, sb.toString());
            bluetoothGatt.disconnect();
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x012d  */
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            Object obj;
            if (bluetoothGatt != null) {
                String str = "gatt.device";
                if (i2 == 0) {
                    Companion companion = CentralLog.Companion;
                    String access$getTAG$p = this.this$0.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Disconnected from other GATT server - ");
                    BluetoothDevice device = bluetoothGatt.getDevice();
                    Intrinsics.checkExpressionValueIsNotNull(device, str);
                    sb.append(device.getAddress());
                    companion.i(access$getTAG$p, sb.toString());
                    this.work.getChecklist().getDisconnected().setStatus(true);
                    this.work.getChecklist().getDisconnected().setTimePerformed(System.currentTimeMillis());
                    StreetPassWorker.access$getTimeoutHandler$p(this.this$0).removeCallbacks(this.work.getTimeoutRunnable());
                    Companion companion2 = CentralLog.Companion;
                    String access$getTAG$p2 = this.this$0.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Timeout removed for ");
                    sb2.append(this.work.getDevice().getAddress());
                    companion2.i(access$getTAG$p2, sb2.toString());
                    String address = this.work.getDevice().getAddress();
                    Work access$getCurrentWork$p = this.this$0.currentWork;
                    if (access$getCurrentWork$p != null) {
                        BluetoothDevice device2 = access$getCurrentWork$p.getDevice();
                        if (device2 != null) {
                            obj = device2.getAddress();
                            if (Intrinsics.areEqual((Object) address, obj)) {
                                this.this$0.currentWork = null;
                            }
                            bluetoothGatt.close();
                            this.this$0.finishWork(this.work);
                        }
                    }
                    obj = null;
                    if (Intrinsics.areEqual((Object) address, obj)) {
                    }
                    bluetoothGatt.close();
                    this.this$0.finishWork(this.work);
                } else if (i2 != 2) {
                    Companion companion3 = CentralLog.Companion;
                    String access$getTAG$p3 = this.this$0.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Connection status for ");
                    BluetoothDevice device3 = bluetoothGatt.getDevice();
                    Intrinsics.checkExpressionValueIsNotNull(device3, str);
                    sb3.append(device3.getAddress());
                    sb3.append(": ");
                    sb3.append(i2);
                    companion3.i(access$getTAG$p3, sb3.toString());
                    endWorkConnection(bluetoothGatt);
                } else {
                    Companion companion4 = CentralLog.Companion;
                    String access$getTAG$p4 = this.this$0.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Connected to other GATT server - ");
                    BluetoothDevice device4 = bluetoothGatt.getDevice();
                    Intrinsics.checkExpressionValueIsNotNull(device4, str);
                    sb4.append(device4.getAddress());
                    companion4.i(access$getTAG$p4, sb4.toString());
                    bluetoothGatt.requestConnectionPriority(0);
                    bluetoothGatt.requestMtu(512);
                    this.work.getChecklist().getConnected().setStatus(true);
                    this.work.getChecklist().getConnected().setTimePerformed(System.currentTimeMillis());
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x005d  */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x006a  */
        /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
        public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
            String str;
            if (!this.work.getChecklist().getMtuChanged().getStatus()) {
                boolean z = true;
                this.work.getChecklist().getMtuChanged().setStatus(true);
                this.work.getChecklist().getMtuChanged().setTimePerformed(System.currentTimeMillis());
                Companion companion = CentralLog.Companion;
                String access$getTAG$p = this.this$0.TAG;
                StringBuilder sb = new StringBuilder();
                if (bluetoothGatt != null) {
                    BluetoothDevice device = bluetoothGatt.getDevice();
                    if (device != null) {
                        str = device.getAddress();
                        sb.append(str);
                        sb.append(" MTU is ");
                        sb.append(i);
                        sb.append(". Was change successful? : ");
                        if (i2 != 0) {
                            z = false;
                        }
                        sb.append(z);
                        companion.i(access$getTAG$p, sb.toString());
                        if (bluetoothGatt == null) {
                            boolean discoverServices = bluetoothGatt.discoverServices();
                            Companion companion2 = CentralLog.Companion;
                            String access$getTAG$p2 = this.this$0.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Attempting to start service discovery on ");
                            BluetoothDevice device2 = bluetoothGatt.getDevice();
                            Intrinsics.checkExpressionValueIsNotNull(device2, "gatt.device");
                            sb2.append(device2.getAddress());
                            sb2.append(": ");
                            sb2.append(discoverServices);
                            companion2.i(access$getTAG$p2, sb2.toString());
                            return;
                        }
                        return;
                    }
                }
                str = null;
                sb.append(str);
                sb.append(" MTU is ");
                sb.append(i);
                sb.append(". Was change successful? : ");
                if (i2 != 0) {
                }
                sb.append(z);
                companion.i(access$getTAG$p, sb.toString());
                if (bluetoothGatt == null) {
                }
            }
        }

        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            Intrinsics.checkParameterIsNotNull(bluetoothGatt, "gatt");
            String str = "gatt.device";
            if (i != 0) {
                Companion companion = CentralLog.Companion;
                String access$getTAG$p = this.this$0.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("No services discovered on ");
                BluetoothDevice device = bluetoothGatt.getDevice();
                Intrinsics.checkExpressionValueIsNotNull(device, str);
                sb.append(device.getAddress());
                companion.w(access$getTAG$p, sb.toString());
                endWorkConnection(bluetoothGatt);
                return;
            }
            Companion companion2 = CentralLog.Companion;
            String access$getTAG$p2 = this.this$0.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Discovered ");
            sb2.append(bluetoothGatt.getServices().size());
            sb2.append(" services on ");
            BluetoothDevice device2 = bluetoothGatt.getDevice();
            Intrinsics.checkExpressionValueIsNotNull(device2, str);
            sb2.append(device2.getAddress());
            companion2.i(access$getTAG$p2, sb2.toString());
            BluetoothGattService service = bluetoothGatt.getService(this.this$0.serviceUUID);
            String str2 = "WTF? ";
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(this.this$0.characteristicV2);
                if (characteristic != null) {
                    boolean readCharacteristic = bluetoothGatt.readCharacteristic(characteristic);
                    Companion companion3 = CentralLog.Companion;
                    String access$getTAG$p3 = this.this$0.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Attempt to read characteristic of our service on ");
                    BluetoothDevice device3 = bluetoothGatt.getDevice();
                    Intrinsics.checkExpressionValueIsNotNull(device3, str);
                    sb3.append(device3.getAddress());
                    sb3.append(": ");
                    sb3.append(readCharacteristic);
                    companion3.i(access$getTAG$p3, sb3.toString());
                } else {
                    Companion companion4 = CentralLog.Companion;
                    String access$getTAG$p4 = this.this$0.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str2);
                    BluetoothDevice device4 = bluetoothGatt.getDevice();
                    Intrinsics.checkExpressionValueIsNotNull(device4, str);
                    sb4.append(device4.getAddress());
                    sb4.append(" does not have our characteristic");
                    companion4.e(access$getTAG$p4, sb4.toString());
                    endWorkConnection(bluetoothGatt);
                }
            }
            if (service == null) {
                Companion companion5 = CentralLog.Companion;
                String access$getTAG$p5 = this.this$0.TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str2);
                BluetoothDevice device5 = bluetoothGatt.getDevice();
                Intrinsics.checkExpressionValueIsNotNull(device5, str);
                sb5.append(device5.getAddress());
                sb5.append(" does not have our service");
                companion5.e(access$getTAG$p5, sb5.toString());
                endWorkConnection(bluetoothGatt);
            }
        }

        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            Intrinsics.checkParameterIsNotNull(bluetoothGatt, "gatt");
            Intrinsics.checkParameterIsNotNull(bluetoothGattCharacteristic, "characteristic");
            Companion companion = CentralLog.Companion;
            String access$getTAG$p = this.this$0.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Read Status: ");
            sb.append(i);
            companion.i(access$getTAG$p, sb.toString());
            String str = "characteristic.uuid";
            String str2 = ": ";
            String str3 = "gatt.device";
            if (i != 0) {
                Companion companion2 = CentralLog.Companion;
                String access$getTAG$p2 = this.this$0.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to read characteristics from ");
                BluetoothDevice device = bluetoothGatt.getDevice();
                Intrinsics.checkExpressionValueIsNotNull(device, str3);
                sb2.append(device.getAddress());
                sb2.append(str2);
                sb2.append(i);
                companion2.w(access$getTAG$p2, sb2.toString());
            } else {
                Companion companion3 = CentralLog.Companion;
                String access$getTAG$p3 = this.this$0.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Characteristic read from ");
                BluetoothDevice device2 = bluetoothGatt.getDevice();
                Intrinsics.checkExpressionValueIsNotNull(device2, str3);
                sb3.append(device2.getAddress());
                sb3.append(str2);
                sb3.append(bluetoothGattCharacteristic.getStringValue(0));
                companion3.i(access$getTAG$p3, sb3.toString());
                Companion companion4 = CentralLog.Companion;
                String access$getTAG$p4 = this.this$0.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("onCharacteristicRead: ");
                sb4.append(this.work.getDevice().getAddress());
                sb4.append(" - [");
                sb4.append(this.work.getConnectable().getRssi());
                sb4.append(']');
                companion4.i(access$getTAG$p4, sb4.toString());
                if (BlueTrace.INSTANCE.supportsCharUUID(bluetoothGattCharacteristic.getUuid())) {
                    try {
                        BlueTrace blueTrace = BlueTrace.INSTANCE;
                        UUID uuid = bluetoothGattCharacteristic.getUuid();
                        Intrinsics.checkExpressionValueIsNotNull(uuid, str);
                        BlueTraceProtocol implementation = blueTrace.getImplementation(uuid);
                        byte[] value = bluetoothGattCharacteristic.getValue();
                        CentralInterface central = implementation.getCentral();
                        Intrinsics.checkExpressionValueIsNotNull(value, "dataBytes");
                        String address = this.work.getDevice().getAddress();
                        Intrinsics.checkExpressionValueIsNotNull(address, "work.device.address");
                        ConnectionRecord processReadRequestDataReceived = central.processReadRequestDataReceived(value, address, this.work.getConnectable().getRssi(), this.work.getConnectable().getTransmissionPower());
                        if (processReadRequestDataReceived != null) {
                            Utils.INSTANCE.broadcastStreetPassReceived(this.this$0.getContext(), processReadRequestDataReceived);
                        }
                    } catch (Throwable th) {
                        Companion companion5 = CentralLog.Companion;
                        String access$getTAG$p5 = this.this$0.TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Failed to process read payload - ");
                        sb5.append(th.getMessage());
                        companion5.e(access$getTAG$p5, sb5.toString());
                    }
                }
                this.work.getChecklist().getReadCharacteristic().setStatus(true);
                this.work.getChecklist().getReadCharacteristic().setTimePerformed(System.currentTimeMillis());
            }
            if (BlueTrace.INSTANCE.supportsCharUUID(bluetoothGattCharacteristic.getUuid())) {
                BlueTrace blueTrace2 = BlueTrace.INSTANCE;
                UUID uuid2 = bluetoothGattCharacteristic.getUuid();
                Intrinsics.checkExpressionValueIsNotNull(uuid2, str);
                BlueTraceProtocol implementation2 = blueTrace2.getImplementation(uuid2);
                if (TempIDManager.INSTANCE.bmValid(this.this$0.getContext())) {
                    bluetoothGattCharacteristic.setValue(implementation2.getCentral().prepareWriteRequestData(implementation2.getVersionInt(), this.work.getConnectable().getRssi(), this.work.getConnectable().getTransmissionPower()));
                    boolean writeCharacteristic = bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
                    Companion companion6 = CentralLog.Companion;
                    String access$getTAG$p6 = this.this$0.TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Attempt to write characteristic to our service on ");
                    BluetoothDevice device3 = bluetoothGatt.getDevice();
                    Intrinsics.checkExpressionValueIsNotNull(device3, str3);
                    sb6.append(device3.getAddress());
                    sb6.append(str2);
                    sb6.append(writeCharacteristic);
                    companion6.i(access$getTAG$p6, sb6.toString());
                    return;
                }
                Companion companion7 = CentralLog.Companion;
                String access$getTAG$p7 = this.this$0.TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Expired BM. Skipping attempt to write characteristic to our service on ");
                BluetoothDevice device4 = bluetoothGatt.getDevice();
                Intrinsics.checkExpressionValueIsNotNull(device4, str3);
                sb7.append(device4.getAddress());
                companion7.i(access$getTAG$p7, sb7.toString());
                endWorkConnection(bluetoothGatt);
                return;
            }
            Companion companion8 = CentralLog.Companion;
            String access$getTAG$p8 = this.this$0.TAG;
            StringBuilder sb8 = new StringBuilder();
            sb8.append("Not writing to ");
            BluetoothDevice device5 = bluetoothGatt.getDevice();
            Intrinsics.checkExpressionValueIsNotNull(device5, str3);
            sb8.append(device5.getAddress());
            sb8.append(". Characteristic ");
            sb8.append(bluetoothGattCharacteristic.getUuid());
            sb8.append(" is not supported");
            companion8.w(access$getTAG$p8, sb8.toString());
            endWorkConnection(bluetoothGatt);
        }

        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            Intrinsics.checkParameterIsNotNull(bluetoothGatt, "gatt");
            Intrinsics.checkParameterIsNotNull(bluetoothGattCharacteristic, "characteristic");
            if (i != 0) {
                Companion companion = CentralLog.Companion;
                String access$getTAG$p = this.this$0.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to write characteristics: ");
                sb.append(i);
                companion.i(access$getTAG$p, sb.toString());
            } else {
                CentralLog.Companion.i(this.this$0.TAG, "Characteristic wrote successfully");
                this.work.getChecklist().getWriteCharacteristic().setStatus(true);
                this.work.getChecklist().getWriteCharacteristic().setTimePerformed(System.currentTimeMillis());
            }
            endWorkConnection(bluetoothGatt);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker$ScannedDeviceReceiver;", "Landroid/content/BroadcastReceiver;", "(Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker;)V", "TAG", "", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: StreetPassWorker.kt */
    public final class ScannedDeviceReceiver extends BroadcastReceiver {
        private final String TAG = "ScannedDeviceReceiver";

        public ScannedDeviceReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (Intrinsics.areEqual((Object) StreetPassKt.ACTION_DEVICE_SCANNED, (Object) intent.getAction())) {
                    BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    ConnectablePeripheral connectablePeripheral = (ConnectablePeripheral) intent.getParcelableExtra(GATTKt.CONNECTION_DATA);
                    boolean z = true;
                    boolean z2 = bluetoothDevice != null;
                    if (connectablePeripheral == null) {
                        z = false;
                    }
                    Companion companion = CentralLog.Companion;
                    String str = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Device received: ");
                    sb.append(bluetoothDevice != null ? bluetoothDevice.getAddress() : null);
                    sb.append(". Device present: ");
                    sb.append(z2);
                    sb.append(", Connectable Present: ");
                    sb.append(z);
                    companion.i(str, sb.toString());
                    if (bluetoothDevice != null && connectablePeripheral != null) {
                        if (StreetPassWorker.this.addWork(new Work(bluetoothDevice, connectablePeripheral, StreetPassWorker.this.getOnWorkTimeoutListener()))) {
                            StreetPassWorker.this.doWork();
                        }
                    }
                }
            }
        }
    }

    public StreetPassWorker(Context context2) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        this.context = context2;
        List<BlacklistEntry> synchronizedList = Collections.synchronizedList(new ArrayList());
        Intrinsics.checkExpressionValueIsNotNull(synchronizedList, "Collections.synchronizedList(ArrayList())");
        this.blacklist = synchronizedList;
        this.scannedDeviceReceiver = new ScannedDeviceReceiver();
        this.blacklistReceiver = new BlacklistReceiver();
        UUID fromString = UUID.fromString(BuildConfig.BLE_SSID);
        Intrinsics.checkExpressionValueIsNotNull(fromString, "UUID.fromString(BuildConfig.BLE_SSID)");
        this.serviceUUID = fromString;
        UUID fromString2 = UUID.fromString(BuildConfig.V2_CHARACTERISTIC_ID);
        Intrinsics.checkExpressionValueIsNotNull(fromString2, "UUID.fromString(BuildConfig.V2_CHARACTERISTIC_ID)");
        this.characteristicV2 = fromString2;
        this.TAG = "StreetPassWorker";
        Object systemService = this.context.getSystemService("bluetooth");
        if (systemService != null) {
            this.bluetoothManager = (BluetoothManager) systemService;
            LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this.context);
            Intrinsics.checkExpressionValueIsNotNull(instance, "LocalBroadcastManager.getInstance(context)");
            this.localBroadcastManager = instance;
            this.onWorkTimeoutListener = new StreetPassWorker$onWorkTimeoutListener$1(this);
            prepare();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.bluetooth.BluetoothManager");
    }

    public static final /* synthetic */ Handler access$getBlacklistHandler$p(StreetPassWorker streetPassWorker) {
        Handler handler = streetPassWorker.blacklistHandler;
        if (handler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("blacklistHandler");
        }
        return handler;
    }

    public static final /* synthetic */ Handler access$getTimeoutHandler$p(StreetPassWorker streetPassWorker) {
        Handler handler = streetPassWorker.timeoutHandler;
        if (handler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("timeoutHandler");
        }
        return handler;
    }

    public final Context getContext() {
        return this.context;
    }

    public final OnWorkTimeoutListener getOnWorkTimeoutListener() {
        return this.onWorkTimeoutListener;
    }

    private final void prepare() {
        this.localBroadcastManager.registerReceiver(this.scannedDeviceReceiver, new IntentFilter(StreetPassKt.ACTION_DEVICE_SCANNED));
        this.localBroadcastManager.registerReceiver(this.blacklistReceiver, new IntentFilter(GATTKt.ACTION_DEVICE_PROCESSED));
        this.timeoutHandler = new Handler();
        this.queueHandler = new Handler();
        this.blacklistHandler = new Handler();
        Companion companion = CentralLog.Companion;
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Service UUID ");
        sb.append(this.serviceUUID);
        companion.d(str, sb.toString());
        Companion companion2 = CentralLog.Companion;
        String str2 = this.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("characteristicV2 ");
        sb2.append(this.characteristicV2);
        companion2.d(str2, sb2.toString());
    }

    public final boolean isCurrentlyWorkedOn(String str) {
        Work work = this.currentWork;
        if (work != null) {
            return Intrinsics.areEqual((Object) work.getDevice().getAddress(), (Object) str);
        }
        return false;
    }

    public final boolean addWork(Work work) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(work, "work");
        if (isCurrentlyWorkedOn(work.getDevice().getAddress())) {
            Companion companion = CentralLog.Companion;
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(work.getDevice().getAddress());
            sb.append(" is being worked on, not adding to queue");
            companion.i(str, sb.toString());
            return false;
        }
        if (BluetoothMonitoringService.Companion.getUseBlacklist()) {
            Iterable iterable = this.blacklist;
            Collection arrayList = new ArrayList();
            for (Object next : iterable) {
                if (Intrinsics.areEqual((Object) ((BlacklistEntry) next).getUniqueIdentifier(), (Object) work.getDevice().getAddress())) {
                    arrayList.add(next);
                }
            }
            if (!((List) arrayList).isEmpty()) {
                Companion companion2 = CentralLog.Companion;
                String str2 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(work.getDevice().getAddress());
                sb2.append(" is in blacklist, not adding to queue");
                companion2.i(str2, sb2.toString());
                return false;
            }
        }
        Iterable iterable2 = this.workQueue;
        Collection arrayList2 = new ArrayList();
        for (Object next2 : iterable2) {
            if (Intrinsics.areEqual((Object) ((Work) next2).getDevice().getAddress(), (Object) work.getDevice().getAddress())) {
                arrayList2.add(next2);
            }
        }
        if (((List) arrayList2).isEmpty()) {
            this.workQueue.offer(work);
            Handler handler = this.queueHandler;
            if (handler == null) {
                Intrinsics.throwUninitializedPropertyAccessException("queueHandler");
            }
            handler.postDelayed(new StreetPassWorker$addWork$3(this, work), BluetoothMonitoringService.Companion.getMaxQueueTime());
            Companion companion3 = CentralLog.Companion;
            String str3 = this.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Added to work queue: ");
            sb3.append(work.getDevice().getAddress());
            companion3.i(str3, sb3.toString());
            return true;
        }
        Companion companion4 = CentralLog.Companion;
        String str4 = this.TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(work.getDevice().getAddress());
        sb4.append(" is already in work queue");
        companion4.i(str4, sb4.toString());
        Iterator it = this.workQueue.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual((Object) ((Work) obj).getDevice().getAddress(), (Object) work.getDevice().getAddress())) {
                break;
            }
        }
        boolean remove = this.workQueue.remove((Work) obj);
        boolean offer = this.workQueue.offer(work);
        Companion companion5 = CentralLog.Companion;
        String str5 = this.TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Queue entry updated - removed: ");
        sb5.append(remove);
        sb5.append(", added: ");
        sb5.append(offer);
        companion5.i(str5, sb5.toString());
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00e2  */
    public final void doWork() {
        Work work;
        String str;
        boolean z;
        Work work2;
        String str2;
        Work work3;
        String str3 = "Moving on to next task";
        boolean z2 = false;
        String str4 = null;
        if (this.currentWork != null) {
            Companion companion = CentralLog.Companion;
            String str5 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Already trying to connect to: ");
            Work work4 = this.currentWork;
            if (work4 != null) {
                BluetoothDevice device = work4.getDevice();
                if (device != null) {
                    str = device.getAddress();
                    sb.append(str);
                    companion.i(str5, sb.toString());
                    long currentTimeMillis = System.currentTimeMillis();
                    Work work5 = this.currentWork;
                    z = currentTimeMillis <= (work5 == null ? work5.getTimeout() : 0);
                    Work work6 = this.currentWork;
                    if ((work6 != null && work6.getFinished()) || z) {
                        Companion companion2 = CentralLog.Companion;
                        String str6 = this.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Handling erroneous current work for ");
                        work2 = this.currentWork;
                        if (work2 != null) {
                            BluetoothDevice device2 = work2.getDevice();
                            if (device2 != null) {
                                str2 = device2.getAddress();
                                sb2.append(str2);
                                sb2.append(" : - finished: ");
                                work3 = this.currentWork;
                                if (work3 != null) {
                                    z2 = work3.getFinished();
                                }
                                sb2.append(z2);
                                sb2.append(", timedout: ");
                                sb2.append(z);
                                companion2.w(str6, sb2.toString());
                                if (this.currentWork != null) {
                                    List connectedDevices = this.bluetoothManager.getConnectedDevices(7);
                                    Work work7 = this.currentWork;
                                    if (connectedDevices.contains(work7 != null ? work7.getDevice() : null)) {
                                        Companion companion3 = CentralLog.Companion;
                                        String str7 = this.TAG;
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("Disconnecting dangling connection to ");
                                        Work work8 = this.currentWork;
                                        if (work8 != null) {
                                            BluetoothDevice device3 = work8.getDevice();
                                            if (device3 != null) {
                                                str4 = device3.getAddress();
                                            }
                                        }
                                        sb3.append(str4);
                                        companion3.w(str7, sb3.toString());
                                        Work work9 = this.currentWork;
                                        if (work9 != null) {
                                            BluetoothGatt gatt = work9.getGatt();
                                            if (gatt != null) {
                                                gatt.disconnect();
                                            }
                                        }
                                    }
                                } else {
                                    doWork();
                                }
                            }
                        }
                        str2 = null;
                        sb2.append(str2);
                        sb2.append(" : - finished: ");
                        work3 = this.currentWork;
                        if (work3 != null) {
                        }
                        sb2.append(z2);
                        sb2.append(", timedout: ");
                        sb2.append(z);
                        companion2.w(str6, sb2.toString());
                        if (this.currentWork != null) {
                        }
                    }
                }
            }
            str = null;
            sb.append(str);
            companion.i(str5, sb.toString());
            long currentTimeMillis2 = System.currentTimeMillis();
            Work work52 = this.currentWork;
            if (currentTimeMillis2 <= (work52 == null ? work52.getTimeout() : 0)) {
            }
            Work work62 = this.currentWork;
            Companion companion22 = CentralLog.Companion;
            String str62 = this.TAG;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("Handling erroneous current work for ");
            work2 = this.currentWork;
            if (work2 != null) {
            }
            str2 = null;
            sb22.append(str2);
            sb22.append(" : - finished: ");
            work3 = this.currentWork;
            if (work3 != null) {
            }
            sb22.append(z2);
            sb22.append(", timedout: ");
            sb22.append(z);
            companion22.w(str62, sb22.toString());
            if (this.currentWork != null) {
            }
        } else if (this.workQueue.isEmpty()) {
            CentralLog.Companion.i(this.TAG, "Queue empty. Nothing to do.");
        } else {
            Companion companion4 = CentralLog.Companion;
            String str8 = this.TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Queue size: ");
            sb4.append(this.workQueue.size());
            companion4.i(str8, sb4.toString());
            Work work10 = null;
            long currentTimeMillis3 = System.currentTimeMillis();
            loop0:
            while (true) {
                work = work10;
                while (work == null && (!this.workQueue.isEmpty())) {
                    work = (Work) this.workQueue.poll();
                    if (work != null && currentTimeMillis3 - work.getTimeStamp() > BluetoothMonitoringService.Companion.getMaxQueueTime()) {
                        Companion companion5 = CentralLog.Companion;
                        String str9 = this.TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Work request for ");
                        sb5.append(work.getDevice().getAddress());
                        sb5.append(" too old. Not doing");
                        companion5.w(str9, sb5.toString());
                    }
                }
            }
            if (work != null) {
                BluetoothDevice device4 = work.getDevice();
                if (BluetoothMonitoringService.Companion.getUseBlacklist()) {
                    Iterable iterable = this.blacklist;
                    Collection arrayList = new ArrayList();
                    for (Object next : iterable) {
                        if (Intrinsics.areEqual((Object) ((BlacklistEntry) next).getUniqueIdentifier(), (Object) device4.getAddress())) {
                            arrayList.add(next);
                        }
                    }
                    if (!((List) arrayList).isEmpty()) {
                        Companion companion6 = CentralLog.Companion;
                        String str10 = this.TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("Already worked on ");
                        sb6.append(device4.getAddress());
                        sb6.append(". Skip.");
                        companion6.w(str10, sb6.toString());
                        doWork();
                        return;
                    }
                }
                boolean connectionStatus = getConnectionStatus(device4);
                Companion companion7 = CentralLog.Companion;
                String str11 = this.TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Already connected to ");
                sb7.append(device4.getAddress());
                sb7.append(" : ");
                sb7.append(connectionStatus);
                companion7.i(str11, sb7.toString());
                if (connectionStatus) {
                    work.getChecklist().getSkipped().setStatus(true);
                    work.getChecklist().getSkipped().setTimePerformed(System.currentTimeMillis());
                    finishWork(work);
                } else {
                    CentralGattCallback centralGattCallback = new CentralGattCallback(this, work);
                    Companion companion8 = CentralLog.Companion;
                    String str12 = this.TAG;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("Starting work - connecting to device: ");
                    sb8.append(device4.getAddress());
                    sb8.append(" @ ");
                    sb8.append(work.getConnectable().getRssi());
                    sb8.append(' ');
                    sb8.append(System.currentTimeMillis() - work.getTimeStamp());
                    sb8.append("ms ago");
                    companion8.i(str12, sb8.toString());
                    this.currentWork = work;
                    try {
                        work.getChecklist().getStarted().setStatus(true);
                        work.getChecklist().getStarted().setTimePerformed(System.currentTimeMillis());
                        work.startWork(this.context, centralGattCallback);
                        BluetoothGatt gatt2 = work.getGatt();
                        if (gatt2 != null) {
                            z2 = gatt2.connect();
                        }
                        if (!z2) {
                            Companion companion9 = CentralLog.Companion;
                            String str13 = this.TAG;
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append("Alamak! not connecting to ");
                            sb9.append(work.getDevice().getAddress());
                            sb9.append("??");
                            companion9.e(str13, sb9.toString());
                            CentralLog.Companion.e(this.TAG, str3);
                            this.currentWork = null;
                            doWork();
                            return;
                        }
                        Companion companion10 = CentralLog.Companion;
                        String str14 = this.TAG;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("Connection to ");
                        sb10.append(work.getDevice().getAddress());
                        sb10.append(" attempt in progress");
                        companion10.i(str14, sb10.toString());
                        Handler handler = this.timeoutHandler;
                        if (handler == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("timeoutHandler");
                        }
                        handler.postDelayed(work.getTimeoutRunnable(), BluetoothMonitoringService.Companion.getConnectionTimeout());
                        work.setTimeout(System.currentTimeMillis() + BluetoothMonitoringService.Companion.getConnectionTimeout());
                        Companion companion11 = CentralLog.Companion;
                        String str15 = this.TAG;
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("Timeout scheduled for ");
                        sb11.append(work.getDevice().getAddress());
                        companion11.i(str15, sb11.toString());
                    } catch (Throwable th) {
                        Companion companion12 = CentralLog.Companion;
                        String str16 = this.TAG;
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("Unexpected error while attempting to connect to ");
                        sb12.append(device4.getAddress());
                        sb12.append(": ");
                        sb12.append(th.getLocalizedMessage());
                        companion12.e(str16, sb12.toString());
                        CentralLog.Companion.e(this.TAG, str3);
                        this.currentWork = work10;
                        doWork();
                        return;
                    }
                }
            }
            if (work == null) {
                CentralLog.Companion.i(this.TAG, "No outstanding work");
            }
        }
    }

    private final boolean getConnectionStatus(BluetoothDevice bluetoothDevice) {
        return this.bluetoothManager.getDevicesMatchingConnectionStates(7, new int[]{2}).contains(bluetoothDevice);
    }

    public final void finishWork(Work work) {
        Intrinsics.checkParameterIsNotNull(work, "work");
        String str = "Work on ";
        if (work.getFinished()) {
            Companion companion = CentralLog.Companion;
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(work.getDevice().getAddress());
            sb.append(" already finished and closed");
            companion.i(str2, sb.toString());
            return;
        }
        if (work.isCriticalsCompleted()) {
            Utils utils = Utils.INSTANCE;
            Context context2 = this.context;
            String address = work.getDevice().getAddress();
            Intrinsics.checkExpressionValueIsNotNull(address, "work.device.address");
            utils.broadcastDeviceProcessed(context2, address);
        }
        Companion companion2 = CentralLog.Companion;
        String str3 = this.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(work.getDevice().getAddress());
        sb2.append(" stopped in: ");
        sb2.append(work.getChecklist().getDisconnected().getTimePerformed() - work.getChecklist().getStarted().getTimePerformed());
        companion2.i(str3, sb2.toString());
        Companion companion3 = CentralLog.Companion;
        String str4 = this.TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(work.getDevice().getAddress());
        sb3.append(" completed?: ");
        sb3.append(work.isCriticalsCompleted());
        sb3.append(". Connected in: ");
        sb3.append(work.getChecklist().getConnected().getTimePerformed() - work.getChecklist().getStarted().getTimePerformed());
        sb3.append(". connection lasted for: ");
        sb3.append(work.getChecklist().getDisconnected().getTimePerformed() - work.getChecklist().getConnected().getTimePerformed());
        sb3.append(". Status: ");
        sb3.append(work.getChecklist());
        companion3.i(str4, sb3.toString());
        Handler handler = this.timeoutHandler;
        if (handler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("timeoutHandler");
        }
        handler.removeCallbacks(work.getTimeoutRunnable());
        Companion companion4 = CentralLog.Companion;
        String str5 = this.TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Timeout removed for ");
        sb4.append(work.getDevice().getAddress());
        companion4.i(str5, sb4.toString());
        work.setFinished(true);
        doWork();
    }

    public final void terminateConnections() {
        CentralLog.Companion.d(this.TAG, "Cleaning up worker.");
        Work work = this.currentWork;
        if (work != null) {
            BluetoothGatt gatt = work.getGatt();
            if (gatt != null) {
                gatt.disconnect();
            }
        }
        this.currentWork = null;
        Handler handler = this.timeoutHandler;
        if (handler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("timeoutHandler");
        }
        handler.removeCallbacksAndMessages(null);
        Handler handler2 = this.queueHandler;
        if (handler2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("queueHandler");
        }
        handler2.removeCallbacksAndMessages(null);
        Handler handler3 = this.blacklistHandler;
        if (handler3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("blacklistHandler");
        }
        handler3.removeCallbacksAndMessages(null);
        this.workQueue.clear();
        this.blacklist.clear();
    }

    public final void unregisterReceivers() {
        String str = "Unable to close receivers: ";
        try {
            this.localBroadcastManager.unregisterReceiver(this.blacklistReceiver);
        } catch (Throwable th) {
            Companion companion = CentralLog.Companion;
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(th.getLocalizedMessage());
            companion.e(str2, sb.toString());
        }
        try {
            this.localBroadcastManager.unregisterReceiver(this.scannedDeviceReceiver);
        } catch (Throwable th2) {
            Companion companion2 = CentralLog.Companion;
            String str3 = this.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(th2.getLocalizedMessage());
            companion2.e(str3, sb2.toString());
        }
    }
}
