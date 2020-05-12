package ca.albertahealthservices.contacttracing.bluetooth.gatt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.content.Context;
import androidx.core.view.InputDeviceCompat;
import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.idmanager.TempIDManager;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.protocol.BlueTrace;
import ca.albertahealthservices.contacttracing.protocol.BlueTraceProtocol;
import ca.albertahealthservices.contacttracing.protocol.PeripheralInterface;
import ca.albertahealthservices.contacttracing.streetpass.ConnectionRecord;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000G\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J,\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016JD\u0010\u0016\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0013\u001a\u00020\u00122\b\u0010\u001a\u001a\u0004\u0018\u00010\tH\u0016J\"\u0010\u001b\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u0012H\u0016J \u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0018H\u0016J\u000e\u0010 \u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010R\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\t0\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0007R\u001d\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\t0\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0007¨\u0006!"}, d2 = {"ca/albertahealthservices/contacttracing/bluetooth/gatt/GattServer$gattServerCallback$1", "Landroid/bluetooth/BluetoothGattServerCallback;", "deviceCharacteristicMap", "", "", "Ljava/util/UUID;", "getDeviceCharacteristicMap", "()Ljava/util/Map;", "readPayloadMap", "", "getReadPayloadMap", "writeDataPayload", "getWriteDataPayload", "onCharacteristicReadRequest", "", "device", "Landroid/bluetooth/BluetoothDevice;", "requestId", "", "offset", "characteristic", "Landroid/bluetooth/BluetoothGattCharacteristic;", "onCharacteristicWriteRequest", "preparedWrite", "", "responseNeeded", "value", "onConnectionStateChange", "status", "newState", "onExecuteWrite", "execute", "saveDataReceived", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: GattServer.kt */
public final class GattServer$gattServerCallback$1 extends BluetoothGattServerCallback {
    private final Map<String, UUID> deviceCharacteristicMap = new HashMap();
    private final Map<String, byte[]> readPayloadMap = new HashMap();
    final /* synthetic */ GattServer this$0;
    private final Map<String, byte[]> writeDataPayload = new HashMap();

    GattServer$gattServerCallback$1(GattServer gattServer) {
        this.this$0 = gattServer;
    }

    public final Map<String, byte[]> getWriteDataPayload() {
        return this.writeDataPayload;
    }

    public final Map<String, byte[]> getReadPayloadMap() {
        return this.readPayloadMap;
    }

    public final Map<String, UUID> getDeviceCharacteristicMap() {
        return this.deviceCharacteristicMap;
    }

    public void onConnectionStateChange(BluetoothDevice bluetoothDevice, int i, int i2) {
        String str = null;
        if (i2 == 0) {
            Companion companion = CentralLog.Companion;
            String access$getTAG$p = this.this$0.TAG;
            StringBuilder sb = new StringBuilder();
            if (bluetoothDevice != null) {
                str = bluetoothDevice.getAddress();
            }
            sb.append(str);
            sb.append(" Disconnected from local GATT server.");
            companion.i(access$getTAG$p, sb.toString());
        } else if (i2 != 2) {
            Companion companion2 = CentralLog.Companion;
            String access$getTAG$p2 = this.this$0.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Connection status: ");
            sb2.append(i2);
            sb2.append(" - ");
            if (bluetoothDevice != null) {
                str = bluetoothDevice.getAddress();
            }
            sb2.append(str);
            companion2.i(access$getTAG$p2, sb2.toString());
        } else {
            Companion companion3 = CentralLog.Companion;
            String access$getTAG$p3 = this.this$0.TAG;
            StringBuilder sb3 = new StringBuilder();
            if (bluetoothDevice != null) {
                str = bluetoothDevice.getAddress();
            }
            sb3.append(str);
            sb3.append(" Connected to local GATT server");
            companion3.i(access$getTAG$p3, sb3.toString());
        }
    }

    public void onCharacteristicReadRequest(BluetoothDevice bluetoothDevice, int i, int i2, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothDevice == null) {
            CentralLog.Companion.w(this.this$0.TAG, "No device");
        }
        if (bluetoothDevice != null) {
            Companion companion = CentralLog.Companion;
            String access$getTAG$p = this.this$0.TAG;
            StringBuilder sb = new StringBuilder();
            String str = "onCharacteristicReadRequest from ";
            sb.append(str);
            sb.append(bluetoothDevice.getAddress());
            companion.i(access$getTAG$p, sb.toString());
            if (!BlueTrace.INSTANCE.supportsCharUUID(bluetoothGattCharacteristic != null ? bluetoothGattCharacteristic.getUuid() : null)) {
                Companion companion2 = CentralLog.Companion;
                String access$getTAG$p2 = this.this$0.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("unsupported characteristic UUID from ");
                sb2.append(bluetoothDevice.getAddress());
                companion2.i(access$getTAG$p2, sb2.toString());
                BluetoothGattServer bluetoothGattServer = this.this$0.getBluetoothGattServer();
                if (bluetoothGattServer != null) {
                    bluetoothGattServer.sendResponse(bluetoothDevice, i, InputDeviceCompat.SOURCE_KEYBOARD, 0, null);
                }
            } else if (bluetoothGattCharacteristic != null) {
                UUID uuid = bluetoothGattCharacteristic.getUuid();
                if (uuid != null) {
                    BlueTraceProtocol implementation = BlueTrace.INSTANCE.getImplementation(uuid);
                    String str2 = "- ";
                    String str3 = " - ";
                    if (TempIDManager.INSTANCE.bmValid(this.this$0.getContext())) {
                        Map<String, byte[]> map = this.readPayloadMap;
                        String address = bluetoothDevice.getAddress();
                        Intrinsics.checkExpressionValueIsNotNull(address, "device.address");
                        Object obj = map.get(address);
                        if (obj == null) {
                            obj = implementation.getPeripheral().prepareReadRequestData(implementation.getVersionInt());
                            map.put(address, obj);
                        }
                        byte[] bArr = (byte[]) obj;
                        byte[] copyOfRange = ArraysKt.copyOfRange(bArr, i2, bArr.length);
                        Companion companion3 = CentralLog.Companion;
                        String access$getTAG$p3 = this.this$0.TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str);
                        sb3.append(bluetoothDevice.getAddress());
                        sb3.append(str3);
                        sb3.append(i);
                        sb3.append(str2);
                        sb3.append(i2);
                        sb3.append(str3);
                        sb3.append(new String(copyOfRange, Charsets.UTF_8));
                        companion3.i(access$getTAG$p3, sb3.toString());
                        BluetoothGattServer bluetoothGattServer2 = this.this$0.getBluetoothGattServer();
                        if (bluetoothGattServer2 != null) {
                            bluetoothGattServer2.sendResponse(bluetoothDevice, i, 0, 0, copyOfRange);
                            return;
                        }
                        return;
                    }
                    Companion companion4 = CentralLog.Companion;
                    String access$getTAG$p4 = this.this$0.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str);
                    sb4.append(bluetoothDevice.getAddress());
                    sb4.append(str3);
                    sb4.append(i);
                    sb4.append(str2);
                    sb4.append(i2);
                    sb4.append(" - BM Expired");
                    companion4.i(access$getTAG$p4, sb4.toString());
                    BluetoothGattServer bluetoothGattServer3 = this.this$0.getBluetoothGattServer();
                    if (bluetoothGattServer3 != null) {
                        bluetoothGattServer3.sendResponse(bluetoothDevice, i, InputDeviceCompat.SOURCE_KEYBOARD, 0, new byte[0]);
                    }
                }
            }
        }
    }

    public void onCharacteristicWriteRequest(BluetoothDevice bluetoothDevice, int i, BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z, boolean z2, int i2, byte[] bArr) {
        boolean z3 = z;
        byte[] bArr2 = bArr;
        Intrinsics.checkParameterIsNotNull(bluetoothGattCharacteristic, "characteristic");
        if (bluetoothDevice == null) {
            CentralLog.Companion.e(this.this$0.TAG, "Write stopped - no device");
        }
        if (bluetoothDevice != null) {
            Companion companion = CentralLog.Companion;
            String access$getTAG$p = this.this$0.TAG;
            StringBuilder sb = new StringBuilder();
            String str = "onCharacteristicWriteRequest - ";
            sb.append(str);
            sb.append(bluetoothDevice.getAddress());
            String str2 = " - preparedWrite: ";
            sb.append(str2);
            sb.append(z3);
            companion.i(access$getTAG$p, sb.toString());
            Companion companion2 = CentralLog.Companion;
            String access$getTAG$p2 = this.this$0.TAG;
            StringBuilder sb2 = new StringBuilder();
            String str3 = "onCharacteristicWriteRequest from ";
            sb2.append(str3);
            sb2.append(bluetoothDevice.getAddress());
            String str4 = " - ";
            sb2.append(str4);
            int i3 = i;
            sb2.append(i);
            sb2.append(str4);
            sb2.append(i2);
            companion2.i(access$getTAG$p2, sb2.toString());
            if (BlueTrace.INSTANCE.supportsCharUUID(bluetoothGattCharacteristic.getUuid())) {
                Map<String, UUID> map = this.deviceCharacteristicMap;
                String address = bluetoothDevice.getAddress();
                String str5 = "device.address";
                Intrinsics.checkExpressionValueIsNotNull(address, str5);
                UUID uuid = bluetoothGattCharacteristic.getUuid();
                Intrinsics.checkExpressionValueIsNotNull(uuid, "characteristic.uuid");
                map.put(address, uuid);
                String str6 = bArr2 != null ? new String(bArr2, Charsets.UTF_8) : "";
                Companion companion3 = CentralLog.Companion;
                String access$getTAG$p3 = this.this$0.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(bluetoothDevice.getAddress());
                sb3.append(str4);
                sb3.append(str6);
                companion3.i(access$getTAG$p3, sb3.toString());
                if (bArr2 != null) {
                    byte[] bArr3 = (byte[]) this.writeDataPayload.get(bluetoothDevice.getAddress());
                    if (bArr3 == null) {
                        bArr3 = new byte[0];
                    }
                    byte[] plus = ArraysKt.plus(bArr3, bArr2);
                    Map<String, byte[]> map2 = this.writeDataPayload;
                    String address2 = bluetoothDevice.getAddress();
                    Intrinsics.checkExpressionValueIsNotNull(address2, str5);
                    map2.put(address2, plus);
                    Companion companion4 = CentralLog.Companion;
                    String access$getTAG$p4 = this.this$0.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Accumulated characteristic: ");
                    sb4.append(new String(plus, Charsets.UTF_8));
                    companion4.i(access$getTAG$p4, sb4.toString());
                    if (z3 && z2) {
                        Companion companion5 = CentralLog.Companion;
                        String access$getTAG$p5 = this.this$0.TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Sending response offset: ");
                        sb5.append(plus.length);
                        companion5.i(access$getTAG$p5, sb5.toString());
                        BluetoothGattServer bluetoothGattServer = this.this$0.getBluetoothGattServer();
                        if (bluetoothGattServer != null) {
                            bluetoothGattServer.sendResponse(bluetoothDevice, i, 0, plus.length, bArr);
                        }
                    }
                    if (!z3) {
                        Companion companion6 = CentralLog.Companion;
                        String access$getTAG$p6 = this.this$0.TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str);
                        sb6.append(bluetoothDevice.getAddress());
                        sb6.append(str2);
                        sb6.append(z3);
                        companion6.i(access$getTAG$p6, sb6.toString());
                        saveDataReceived(bluetoothDevice);
                        if (z2) {
                            BluetoothGattServer bluetoothGattServer2 = this.this$0.getBluetoothGattServer();
                            if (bluetoothGattServer2 != null) {
                                bluetoothGattServer2.sendResponse(bluetoothDevice, i, 0, 0, null);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            Companion companion7 = CentralLog.Companion;
            String access$getTAG$p7 = this.this$0.TAG;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("unsupported characteristic UUID from ");
            sb7.append(bluetoothDevice.getAddress());
            companion7.i(access$getTAG$p7, sb7.toString());
            if (z2) {
                BluetoothGattServer bluetoothGattServer3 = this.this$0.getBluetoothGattServer();
                if (bluetoothGattServer3 != null) {
                    bluetoothGattServer3.sendResponse(bluetoothDevice, i, InputDeviceCompat.SOURCE_KEYBOARD, 0, null);
                }
            }
        }
    }

    public void onExecuteWrite(BluetoothDevice bluetoothDevice, int i, boolean z) {
        Intrinsics.checkParameterIsNotNull(bluetoothDevice, "device");
        super.onExecuteWrite(bluetoothDevice, i, z);
        byte[] bArr = (byte[]) this.writeDataPayload.get(bluetoothDevice.getAddress());
        if (bArr != null) {
            Companion companion = CentralLog.Companion;
            String access$getTAG$p = this.this$0.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onExecuteWrite - ");
            sb.append(i);
            sb.append("- ");
            sb.append(bluetoothDevice.getAddress());
            sb.append(" - ");
            sb.append(new String(bArr, Charsets.UTF_8));
            companion.i(access$getTAG$p, sb.toString());
            saveDataReceived(bluetoothDevice);
            BluetoothGattServer bluetoothGattServer = this.this$0.getBluetoothGattServer();
            if (bluetoothGattServer != null) {
                bluetoothGattServer.sendResponse(bluetoothDevice, i, 0, 0, null);
                return;
            }
            return;
        }
        BluetoothGattServer bluetoothGattServer2 = this.this$0.getBluetoothGattServer();
        if (bluetoothGattServer2 != null) {
            bluetoothGattServer2.sendResponse(bluetoothDevice, i, InputDeviceCompat.SOURCE_KEYBOARD, 0, null);
        }
    }

    public final void saveDataReceived(BluetoothDevice bluetoothDevice) {
        String str = "device.address";
        Intrinsics.checkParameterIsNotNull(bluetoothDevice, "device");
        byte[] bArr = (byte[]) this.writeDataPayload.get(bluetoothDevice.getAddress());
        UUID uuid = (UUID) this.deviceCharacteristicMap.get(bluetoothDevice.getAddress());
        if (uuid != null && bArr != null) {
            try {
                PeripheralInterface peripheral = BlueTrace.INSTANCE.getImplementation(uuid).getPeripheral();
                String address = bluetoothDevice.getAddress();
                Intrinsics.checkExpressionValueIsNotNull(address, str);
                ConnectionRecord processWriteRequestDataReceived = peripheral.processWriteRequestDataReceived(bArr, address);
                if (processWriteRequestDataReceived != null) {
                    Utils.INSTANCE.broadcastStreetPassReceived(this.this$0.getContext(), processWriteRequestDataReceived);
                }
            } catch (Throwable th) {
                Companion companion = CentralLog.Companion;
                String access$getTAG$p = this.this$0.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to process write payload - ");
                sb.append(th.getMessage());
                companion.e(access$getTAG$p, sb.toString());
            }
            Utils utils = Utils.INSTANCE;
            Context context = this.this$0.getContext();
            String address2 = bluetoothDevice.getAddress();
            Intrinsics.checkExpressionValueIsNotNull(address2, str);
            utils.broadcastDeviceProcessed(context, address2);
            this.writeDataPayload.remove(bluetoothDevice.getAddress());
            this.readPayloadMap.remove(bluetoothDevice.getAddress());
            UUID uuid2 = (UUID) this.deviceCharacteristicMap.remove(bluetoothDevice.getAddress());
        }
    }
}
