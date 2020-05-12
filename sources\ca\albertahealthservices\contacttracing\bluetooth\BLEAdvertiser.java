package ca.albertahealthservices.contacttracing.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertiseSettings.Builder;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.os.Handler;
import android.os.ParcelUuid;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.services.BluetoothMonitoringService;
import java.nio.charset.Charset;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u00101\u001a\u0002022\u0006\u00103\u001a\u000204J\u000e\u00105\u001a\u0002022\u0006\u00103\u001a\u000204J\u0006\u00106\u001a\u000202R\u000e\u0010\u0005\u001a\u00020\u0003XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0019X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0011\u0010\u001d\u001a\u00020\u001e¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0019\u0010#\u001a\n %*\u0004\u0018\u00010$0$¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u001a\u0010(\u001a\u00020\u0019X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001a\"\u0004\b*\u0010\u001cR\u001a\u0010+\u001a\u00020,X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100¨\u00067"}, d2 = {"Lca/albertahealthservices/contacttracing/bluetooth/BLEAdvertiser;", "", "serviceUUID", "", "(Ljava/lang/String;)V", "TAG", "advertiser", "Landroid/bluetooth/le/BluetoothLeAdvertiser;", "callback", "Landroid/bluetooth/le/AdvertiseCallback;", "charLength", "", "data", "Landroid/bluetooth/le/AdvertiseData;", "getData", "()Landroid/bluetooth/le/AdvertiseData;", "setData", "(Landroid/bluetooth/le/AdvertiseData;)V", "handler", "Landroid/os/Handler;", "getHandler", "()Landroid/os/Handler;", "setHandler", "(Landroid/os/Handler;)V", "isAdvertising", "", "()Z", "setAdvertising", "(Z)V", "pUuid", "Landroid/os/ParcelUuid;", "getPUuid", "()Landroid/os/ParcelUuid;", "getServiceUUID", "()Ljava/lang/String;", "settings", "Landroid/bluetooth/le/AdvertiseSettings;", "kotlin.jvm.PlatformType", "getSettings", "()Landroid/bluetooth/le/AdvertiseSettings;", "shouldBeAdvertising", "getShouldBeAdvertising", "setShouldBeAdvertising", "stopRunnable", "Ljava/lang/Runnable;", "getStopRunnable", "()Ljava/lang/Runnable;", "setStopRunnable", "(Ljava/lang/Runnable;)V", "startAdvertising", "", "timeoutInMillis", "", "startAdvertisingLegacy", "stopAdvertising", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BLEAdvertiser.kt */
public final class BLEAdvertiser {
    /* access modifiers changed from: private */
    public final String TAG = "BLEAdvertiser";
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseCallback callback = new BLEAdvertiser$callback$1(this);
    /* access modifiers changed from: private */
    public int charLength = 3;
    private AdvertiseData data;
    private Handler handler = new Handler();
    private boolean isAdvertising;
    private final ParcelUuid pUuid = new ParcelUuid(UUID.fromString(this.serviceUUID));
    private final String serviceUUID;
    private final AdvertiseSettings settings = new Builder().setTxPowerLevel(3).setAdvertiseMode(0).setConnectable(true).setTimeout(0).build();
    private boolean shouldBeAdvertising;
    private Runnable stopRunnable = new BLEAdvertiser$stopRunnable$1(this);

    public BLEAdvertiser(String str) {
        Intrinsics.checkParameterIsNotNull(str, "serviceUUID");
        this.serviceUUID = str;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        Intrinsics.checkExpressionValueIsNotNull(defaultAdapter, "BluetoothAdapter.getDefaultAdapter()");
        this.advertiser = defaultAdapter.getBluetoothLeAdvertiser();
    }

    public final String getServiceUUID() {
        return this.serviceUUID;
    }

    public final ParcelUuid getPUuid() {
        return this.pUuid;
    }

    public final AdvertiseSettings getSettings() {
        return this.settings;
    }

    public final AdvertiseData getData() {
        return this.data;
    }

    public final void setData(AdvertiseData advertiseData) {
        this.data = advertiseData;
    }

    public final Handler getHandler() {
        return this.handler;
    }

    public final void setHandler(Handler handler2) {
        Intrinsics.checkParameterIsNotNull(handler2, "<set-?>");
        this.handler = handler2;
    }

    public final Runnable getStopRunnable() {
        return this.stopRunnable;
    }

    public final void setStopRunnable(Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(runnable, "<set-?>");
        this.stopRunnable = runnable;
    }

    public final boolean isAdvertising() {
        return this.isAdvertising;
    }

    public final void setAdvertising(boolean z) {
        this.isAdvertising = z;
    }

    public final boolean getShouldBeAdvertising() {
        return this.shouldBeAdvertising;
    }

    public final void setShouldBeAdvertising(boolean z) {
        this.shouldBeAdvertising = z;
    }

    public final void startAdvertisingLegacy(long j) {
        String uuid = UUID.randomUUID().toString();
        Intrinsics.checkExpressionValueIsNotNull(uuid, "UUID.randomUUID().toString()");
        int length = uuid.length() - this.charLength;
        int length2 = uuid.length();
        String str = "null cannot be cast to non-null type java.lang.String";
        if (uuid != null) {
            String substring = uuid.substring(length, length2);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            Companion companion = CentralLog.Companion;
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Unique string: ");
            sb.append(substring);
            companion.d(str2, sb.toString());
            Charset charset = Charsets.UTF_8;
            if (substring != null) {
                byte[] bytes = substring.getBytes(charset);
                Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
                this.data = new AdvertiseData.Builder().setIncludeDeviceName(false).setIncludeTxPowerLevel(true).addServiceUuid(this.pUuid).addManufacturerData(1023, bytes).build();
                try {
                    CentralLog.Companion.d(this.TAG, "Start advertising");
                    BluetoothLeAdvertiser bluetoothLeAdvertiser = this.advertiser;
                    if (bluetoothLeAdvertiser == null) {
                        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                        Intrinsics.checkExpressionValueIsNotNull(defaultAdapter, "BluetoothAdapter.getDefaultAdapter()");
                        bluetoothLeAdvertiser = defaultAdapter.getBluetoothLeAdvertiser();
                    }
                    this.advertiser = bluetoothLeAdvertiser;
                    if (bluetoothLeAdvertiser != null) {
                        bluetoothLeAdvertiser.startAdvertising(this.settings, this.data, this.callback);
                    }
                } catch (Throwable th) {
                    Companion companion2 = CentralLog.Companion;
                    String str3 = this.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to start advertising legacy: ");
                    sb2.append(th.getMessage());
                    companion2.e(str3, sb2.toString());
                }
                if (!BluetoothMonitoringService.Companion.getInfiniteAdvertising()) {
                    this.handler.removeCallbacksAndMessages(this.stopRunnable);
                    this.handler.postDelayed(this.stopRunnable, j);
                    return;
                }
                return;
            }
            throw new TypeCastException(str);
        }
        throw new TypeCastException(str);
    }

    public final void startAdvertising(long j) {
        startAdvertisingLegacy(j);
        this.shouldBeAdvertising = true;
    }

    public final void stopAdvertising() {
        try {
            CentralLog.Companion.d(this.TAG, "stop advertising");
            BluetoothLeAdvertiser bluetoothLeAdvertiser = this.advertiser;
            if (bluetoothLeAdvertiser != null) {
                bluetoothLeAdvertiser.stopAdvertising(this.callback);
            }
        } catch (Throwable th) {
            Companion companion = CentralLog.Companion;
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to stop advertising: ");
            sb.append(th.getMessage());
            companion.e(str, sb.toString());
        }
        this.shouldBeAdvertising = false;
        this.handler.removeCallbacksAndMessages(null);
    }
}
