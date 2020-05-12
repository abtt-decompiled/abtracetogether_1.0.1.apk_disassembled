package ca.albertahealthservices.contacttracing.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import java.util.UUID;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000=\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0018\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010!\u001a\u00020\"J\u0006\u0010#\u001a\u00020\"R\u000e\u0010\u0007\u001a\u00020\u0005XD¢\u0006\u0002\n\u0000R\u001d\u0010\b\u001a\u0004\u0018\u00010\t8BX\u0002¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000bR+\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u00038B@BX\u0002¢\u0006\u0012\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0004\n\u0002\u0010\u0019R+\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u000e\u001a\u00020\u001a8B@BX\u0002¢\u0006\u0012\n\u0004\b \u0010\u0014\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001f¨\u0006$"}, d2 = {"Lca/albertahealthservices/contacttracing/bluetooth/BLEDiscoverer;", "", "context", "Landroid/content/Context;", "serviceUUIDString", "", "(Landroid/content/Context;Ljava/lang/String;)V", "TAG", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;", "getBluetoothAdapter", "()Landroid/bluetooth/BluetoothAdapter;", "bluetoothAdapter$delegate", "Lkotlin/Lazy;", "<set-?>", "getContext", "()Landroid/content/Context;", "setContext", "(Landroid/content/Context;)V", "context$delegate", "Lkotlin/properties/ReadWriteProperty;", "localBroadcastManager", "Landroidx/localbroadcastmanager/content/LocalBroadcastManager;", "mDiscoveryReceiver", "ca/albertahealthservices/contacttracing/bluetooth/BLEDiscoverer$mDiscoveryReceiver$1", "Lca/albertahealthservices/contacttracing/bluetooth/BLEDiscoverer$mDiscoveryReceiver$1;", "Landroid/os/ParcelUuid;", "serviceUUID", "getServiceUUID", "()Landroid/os/ParcelUuid;", "setServiceUUID", "(Landroid/os/ParcelUuid;)V", "serviceUUID$delegate", "cancelDiscovery", "", "startDiscovery", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BLEDiscoverer.kt */
public final class BLEDiscoverer {
    static final /* synthetic */ KProperty[] $$delegatedProperties;
    /* access modifiers changed from: private */
    public final String TAG = "BLEDiscoverer";
    private final Lazy bluetoothAdapter$delegate;
    private final ReadWriteProperty context$delegate = Delegates.INSTANCE.notNull();
    private LocalBroadcastManager localBroadcastManager;
    private final BLEDiscoverer$mDiscoveryReceiver$1 mDiscoveryReceiver;
    private final ReadWriteProperty serviceUUID$delegate = Delegates.INSTANCE.notNull();

    static {
        Class<BLEDiscoverer> cls = BLEDiscoverer.class;
        $$delegatedProperties = new KProperty[]{Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "serviceUUID", "getServiceUUID()Landroid/os/ParcelUuid;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "context", "getContext()Landroid/content/Context;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "bluetoothAdapter", "getBluetoothAdapter()Landroid/bluetooth/BluetoothAdapter;"))};
    }

    private final BluetoothAdapter getBluetoothAdapter() {
        Lazy lazy = this.bluetoothAdapter$delegate;
        KProperty kProperty = $$delegatedProperties[2];
        return (BluetoothAdapter) lazy.getValue();
    }

    private final Context getContext() {
        return (Context) this.context$delegate.getValue(this, $$delegatedProperties[1]);
    }

    private final ParcelUuid getServiceUUID() {
        return (ParcelUuid) this.serviceUUID$delegate.getValue(this, $$delegatedProperties[0]);
    }

    private final void setContext(Context context) {
        this.context$delegate.setValue(this, $$delegatedProperties[1], context);
    }

    private final void setServiceUUID(ParcelUuid parcelUuid) {
        this.serviceUUID$delegate.setValue(this, $$delegatedProperties[0], parcelUuid);
    }

    public BLEDiscoverer(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "serviceUUIDString");
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(context);
        Intrinsics.checkExpressionValueIsNotNull(instance, "LocalBroadcastManager.getInstance(context)");
        this.localBroadcastManager = instance;
        setContext(context);
        setServiceUUID(new ParcelUuid(UUID.fromString(str)));
        this.bluetoothAdapter$delegate = LazyKt.lazy(LazyThreadSafetyMode.NONE, (Function0) new BLEDiscoverer$bluetoothAdapter$2(context));
        this.mDiscoveryReceiver = new BLEDiscoverer$mDiscoveryReceiver$1(this);
    }

    public final void startDiscovery() {
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
        intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        this.localBroadcastManager.registerReceiver(this.mDiscoveryReceiver, intentFilter);
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            Intrinsics.throwNpe();
        }
        bluetoothAdapter.startDiscovery();
    }

    public final void cancelDiscovery() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            Intrinsics.throwNpe();
        }
        bluetoothAdapter.cancelDiscovery();
        try {
            this.localBroadcastManager.unregisterReceiver(this.mDiscoveryReceiver);
        } catch (Throwable th) {
            Companion companion = CentralLog.Companion;
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Already unregistered workReceiver? ");
            sb.append(th.getMessage());
            companion.e(str, sb.toString());
        }
    }
}
