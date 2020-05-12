package ca.albertahealthservices.contacttracing.streetpass;

import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import ca.albertahealthservices.contacttracing.bluetooth.gatt.GattServer;
import ca.albertahealthservices.contacttracing.bluetooth.gatt.GattService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\u001a\u0010\u0010\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0002J\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\u0007\u001a\u00020\u0005XD¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0013"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/StreetPassServer;", "", "context", "Landroid/content/Context;", "serviceUUIDString", "", "(Landroid/content/Context;Ljava/lang/String;)V", "TAG", "getContext", "()Landroid/content/Context;", "gattServer", "Lca/albertahealthservices/contacttracing/bluetooth/gatt/GattServer;", "getServiceUUIDString", "()Ljava/lang/String;", "checkServiceAvailable", "", "setupGattServer", "tearDown", "", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: StreetPassServer.kt */
public final class StreetPassServer {
    private final String TAG = "StreetPassServer";
    private final Context context;
    private GattServer gattServer;
    private final String serviceUUIDString;

    public StreetPassServer(Context context2, String str) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        Intrinsics.checkParameterIsNotNull(str, "serviceUUIDString");
        this.context = context2;
        this.serviceUUIDString = str;
        this.gattServer = setupGattServer(context2, str);
    }

    public final Context getContext() {
        return this.context;
    }

    public final String getServiceUUIDString() {
        return this.serviceUUIDString;
    }

    private final GattServer setupGattServer(Context context2, String str) {
        GattServer gattServer2 = new GattServer(context2, str);
        if (!gattServer2.startServer()) {
            return null;
        }
        gattServer2.addService(new GattService(context2, str));
        return gattServer2;
    }

    public final void tearDown() {
        GattServer gattServer2 = this.gattServer;
        if (gattServer2 != null) {
            gattServer2.stop();
        }
    }

    public final boolean checkServiceAvailable() {
        GattServer gattServer2 = this.gattServer;
        if (gattServer2 != null) {
            BluetoothGattServer bluetoothGattServer = gattServer2.getBluetoothGattServer();
            if (bluetoothGattServer != null) {
                List services = bluetoothGattServer.getServices();
                if (services != null) {
                    Iterable iterable = services;
                    Collection arrayList = new ArrayList();
                    for (Object next : iterable) {
                        BluetoothGattService bluetoothGattService = (BluetoothGattService) next;
                        Intrinsics.checkExpressionValueIsNotNull(bluetoothGattService, "it");
                        if (bluetoothGattService.getUuid().toString().equals(this.serviceUUIDString)) {
                            arrayList.add(next);
                        }
                    }
                    return !((List) arrayList).isEmpty();
                }
            }
        }
        return false;
    }
}
