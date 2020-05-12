package ca.albertahealthservices.contacttracing.bluetooth.gatt;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import ca.albertahealthservices.contacttracing.BuildConfig;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR+\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8F@FX\u0002¢\u0006\u0012\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0014\u001a\n \u0016*\u0004\u0018\u00010\u00150\u0015X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lca/albertahealthservices/contacttracing/bluetooth/gatt/GattService;", "", "context", "Landroid/content/Context;", "serviceUUIDString", "", "(Landroid/content/Context;Ljava/lang/String;)V", "characteristicV2", "Landroid/bluetooth/BluetoothGattCharacteristic;", "getContext", "()Landroid/content/Context;", "<set-?>", "Landroid/bluetooth/BluetoothGattService;", "gattService", "getGattService", "()Landroid/bluetooth/BluetoothGattService;", "setGattService", "(Landroid/bluetooth/BluetoothGattService;)V", "gattService$delegate", "Lkotlin/properties/ReadWriteProperty;", "serviceUUID", "Ljava/util/UUID;", "kotlin.jvm.PlatformType", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: GattService.kt */
public final class GattService {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(GattService.class), "gattService", "getGattService()Landroid/bluetooth/BluetoothGattService;"))};
    private BluetoothGattCharacteristic characteristicV2;
    private final Context context;
    private final ReadWriteProperty gattService$delegate = Delegates.INSTANCE.notNull();
    private UUID serviceUUID;

    public final BluetoothGattService getGattService() {
        return (BluetoothGattService) this.gattService$delegate.getValue(this, $$delegatedProperties[0]);
    }

    public final void setGattService(BluetoothGattService bluetoothGattService) {
        Intrinsics.checkParameterIsNotNull(bluetoothGattService, "<set-?>");
        this.gattService$delegate.setValue(this, $$delegatedProperties[0], bluetoothGattService);
    }

    public GattService(Context context2, String str) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        Intrinsics.checkParameterIsNotNull(str, "serviceUUIDString");
        this.context = context2;
        this.serviceUUID = UUID.fromString(str);
        setGattService(new BluetoothGattService(this.serviceUUID, 0));
        this.characteristicV2 = new BluetoothGattCharacteristic(UUID.fromString(BuildConfig.V2_CHARACTERISTIC_ID), 10, 17);
        getGattService().addCharacteristic(this.characteristicV2);
    }

    public final Context getContext() {
        return this.context;
    }
}
