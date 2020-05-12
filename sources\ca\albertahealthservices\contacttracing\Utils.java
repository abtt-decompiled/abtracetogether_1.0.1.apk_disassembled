package ca.albertahealthservices.contacttracing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ca.albertahealthservices.contacttracing.bluetooth.gatt.GATTKt;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.scheduler.Scheduler;
import ca.albertahealthservices.contacttracing.services.BluetoothMonitoringService;
import ca.albertahealthservices.contacttracing.services.BluetoothMonitoringService.Command;
import ca.albertahealthservices.contacttracing.status.Status;
import ca.albertahealthservices.contacttracing.streetpass.ConnectablePeripheral;
import ca.albertahealthservices.contacttracing.streetpass.ConnectionRecord;
import ca.albertahealthservices.contacttracing.streetpass.StreetPassKt;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0011\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u0004J\u001e\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0016\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015J\u0018\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bJ\u000e\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010 \u001a\u00020\u00192\u0006\u0010!\u001a\u00020\u0004J\u000e\u0010\"\u001a\u00020\u00042\u0006\u0010#\u001a\u00020$J\u0010\u0010%\u001a\u0004\u0018\u00010\u00042\u0006\u0010&\u001a\u00020$J\u0011\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00040(¢\u0006\u0002\u0010)J\u000e\u0010*\u001a\u00020\u00042\u0006\u0010#\u001a\u00020$J\u0016\u0010+\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010,\u001a\u00020-J\u0006\u0010.\u001a\u00020\u0017J\u0016\u0010/\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u00100\u001a\u00020\u0004J\u0016\u00101\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u00102\u001a\u00020$J\u0016\u00103\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u00104\u001a\u00020$J\u0016\u00105\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u00106\u001a\u00020$J\u0016\u00107\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u00106\u001a\u00020$J\u0016\u00108\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u00109\u001a\u00020$J\u0016\u0010:\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u00106\u001a\u00020$J\u0018\u0010;\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010,\u001a\u0004\u0018\u00010-J\u000e\u0010<\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010=\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006>"}, d2 = {"Lca/albertahealthservices/contacttracing/Utils;", "", "()V", "TAG", "", "broadcastDeviceDisconnected", "", "context", "Landroid/content/Context;", "device", "Landroid/bluetooth/BluetoothDevice;", "broadcastDeviceProcessed", "deviceAddress", "broadcastDeviceScanned", "connectableBleDevice", "Lca/albertahealthservices/contacttracing/streetpass/ConnectablePeripheral;", "broadcastStatusReceived", "statusRecord", "Lca/albertahealthservices/contacttracing/status/Status;", "broadcastStreetPassReceived", "streetpass", "Lca/albertahealthservices/contacttracing/streetpass/ConnectionRecord;", "canHandleIntent", "", "batteryExemptionIntent", "Landroid/content/Intent;", "packageManager", "Landroid/content/pm/PackageManager;", "cancelBMUpdateCheck", "cancelNextAdvertise", "cancelNextHealthCheck", "cancelNextScan", "getBatteryOptimizerExemptionIntent", "packageName", "getDate", "milliSeconds", "", "getDateFromUnix", "unix_timestamp", "getRequiredPermissions", "", "()[Ljava/lang/String;", "getTime", "hideKeyboardFrom", "view", "Landroid/view/View;", "isBluetoothAvailable", "readFromInternalStorage", "fileName", "scheduleBMUpdateCheck", "bmCheckInterval", "scheduleNextAdvertise", "timeToNextAdvertise", "scheduleNextHealthCheck", "timeInMillis", "scheduleNextScan", "scheduleRepeatingPurge", "intervalMillis", "scheduleStartMonitoringService", "showKeyboardFrom", "startBluetoothMonitoringService", "stopBluetoothMonitoringService", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: Utils.kt */
public final class Utils {
    public static final Utils INSTANCE = new Utils();
    private static final String TAG = "Utils";

    private Utils() {
    }

    public final String[] getRequiredPermissions() {
        return new String[]{"android.permission.ACCESS_FINE_LOCATION"};
    }

    public final Intent getBatteryOptimizerExemptionIntent(String str) {
        Intrinsics.checkParameterIsNotNull(str, "packageName");
        Intent intent = new Intent();
        intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(str);
        intent.setData(Uri.parse(sb.toString()));
        return intent;
    }

    public final boolean canHandleIntent(Intent intent, PackageManager packageManager) {
        Intrinsics.checkParameterIsNotNull(intent, "batteryExemptionIntent");
        return (packageManager == null || intent.resolveActivity(packageManager) == null) ? false : true;
    }

    public final String getDate(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Calendar instance = Calendar.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(instance, "calendar");
        instance.setTimeInMillis(j);
        String format = simpleDateFormat.format(instance.getTime());
        Intrinsics.checkExpressionValueIsNotNull(format, "formatter.format(calendar.time)");
        return format;
    }

    public final String getTime(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        Calendar instance = Calendar.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(instance, "calendar");
        instance.setTimeInMillis(j);
        String format = simpleDateFormat.format(instance.getTime());
        Intrinsics.checkExpressionValueIsNotNull(format, "formatter.format(calendar.time)");
        return format;
    }

    public final void startBluetoothMonitoringService(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_START.getIndex());
        context.startService(intent);
    }

    public final void scheduleStartMonitoringService(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_START.getIndex());
        Scheduler.INSTANCE.scheduleServiceIntent(BluetoothMonitoringService.Companion.getPENDING_START(), context, intent, j);
    }

    public final void scheduleBMUpdateCheck(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        cancelBMUpdateCheck(context);
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_UPDATE_BM.getIndex());
        Scheduler.INSTANCE.scheduleServiceIntent(BluetoothMonitoringService.Companion.getPENDING_BM_UPDATE(), context, intent, j);
    }

    public final void cancelBMUpdateCheck(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_UPDATE_BM.getIndex());
        Scheduler.INSTANCE.cancelServiceIntent(BluetoothMonitoringService.Companion.getPENDING_BM_UPDATE(), context, intent);
    }

    public final void stopBluetoothMonitoringService(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_STOP.getIndex());
        cancelNextScan(context);
        cancelNextHealthCheck(context);
        context.stopService(intent);
    }

    public final void scheduleNextScan(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        cancelNextScan(context);
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_SCAN.getIndex());
        Scheduler.INSTANCE.scheduleServiceIntent(BluetoothMonitoringService.Companion.getPENDING_SCAN_REQ_CODE(), context, intent, j);
    }

    public final void cancelNextScan(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_SCAN.getIndex());
        Scheduler.INSTANCE.cancelServiceIntent(BluetoothMonitoringService.Companion.getPENDING_SCAN_REQ_CODE(), context, intent);
    }

    public final void scheduleNextAdvertise(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        cancelNextAdvertise(context);
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_ADVERTISE.getIndex());
        Scheduler.INSTANCE.scheduleServiceIntent(BluetoothMonitoringService.Companion.getPENDING_ADVERTISE_REQ_CODE(), context, intent, j);
    }

    public final void cancelNextAdvertise(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_ADVERTISE.getIndex());
        Scheduler.INSTANCE.cancelServiceIntent(BluetoothMonitoringService.Companion.getPENDING_ADVERTISE_REQ_CODE(), context, intent);
    }

    public final void scheduleNextHealthCheck(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        cancelNextHealthCheck(context);
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_SELF_CHECK.getIndex());
        Scheduler.INSTANCE.scheduleServiceIntent(BluetoothMonitoringService.Companion.getPENDING_HEALTH_CHECK_CODE(), context, intent, j);
    }

    public final void cancelNextHealthCheck(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_SELF_CHECK.getIndex());
        Scheduler.INSTANCE.cancelServiceIntent(BluetoothMonitoringService.Companion.getPENDING_HEALTH_CHECK_CODE(), context, intent);
    }

    public final void scheduleRepeatingPurge(Context context, long j) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, BluetoothMonitoringService.class);
        intent.putExtra(BluetoothMonitoringService.Companion.getCOMMAND_KEY(), Command.ACTION_PURGE.getIndex());
        Scheduler.INSTANCE.scheduleRepeatingServiceIntent(BluetoothMonitoringService.Companion.getPENDING_PURGE_CODE(), context, intent, j);
    }

    public final void broadcastDeviceScanned(Context context, BluetoothDevice bluetoothDevice, ConnectablePeripheral connectablePeripheral) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(bluetoothDevice, "device");
        Intrinsics.checkParameterIsNotNull(connectablePeripheral, "connectableBleDevice");
        Intent intent = new Intent(StreetPassKt.ACTION_DEVICE_SCANNED);
        intent.putExtra("android.bluetooth.device.extra.DEVICE", bluetoothDevice);
        intent.putExtra(GATTKt.CONNECTION_DATA, connectablePeripheral);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public final void broadcastDeviceProcessed(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "deviceAddress");
        Intent intent = new Intent(GATTKt.ACTION_DEVICE_PROCESSED);
        intent.putExtra(GATTKt.DEVICE_ADDRESS, str);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public final void broadcastStreetPassReceived(Context context, ConnectionRecord connectionRecord) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(connectionRecord, "streetpass");
        Intent intent = new Intent(GATTKt.ACTION_RECEIVED_STREETPASS);
        intent.putExtra(GATTKt.STREET_PASS, connectionRecord);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public final void broadcastStatusReceived(Context context, Status status) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(status, "statusRecord");
        Intent intent = new Intent(GATTKt.ACTION_RECEIVED_STATUS);
        intent.putExtra(GATTKt.STATUS, status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public final void broadcastDeviceDisconnected(Context context, BluetoothDevice bluetoothDevice) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(bluetoothDevice, "device");
        Intent intent = new Intent(GATTKt.ACTION_GATT_DISCONNECTED);
        intent.putExtra("android.bluetooth.device.extra.DEVICE", bluetoothDevice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public final String readFromInternalStorage(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "fileName");
        Companion companion = CentralLog.Companion;
        String str2 = TAG;
        companion.d(str2, "Reading from internal storage");
        ObjectRef objectRef = new ObjectRef();
        objectRef.element = (String) null;
        StringBuilder sb = new StringBuilder();
        FileInputStream openFileInput = context.openFileInput(str);
        Intrinsics.checkExpressionValueIsNotNull(openFileInput, "context.openFileInput(fileName)");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput));
        while (new Utils$readFromInternalStorage$1(objectRef, bufferedReader).invoke() != null) {
            try {
                Companion companion2 = CentralLog.Companion;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Text: ");
                sb2.append((String) objectRef.element);
                companion2.d(str2, sb2.toString());
                sb.append((String) objectRef.element);
            } catch (Throwable th) {
                Companion companion3 = CentralLog.Companion;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Failed to readFromInternalStorage: ");
                sb3.append(th.getMessage());
                companion3.e(str2, sb3.toString());
            }
        }
        bufferedReader.close();
        String sb4 = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(sb4, "stringBuilder.toString()");
        return sb4;
    }

    public final String getDateFromUnix(long j) {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.ENGLISH).format(Long.valueOf(j)).toString();
    }

    public final void hideKeyboardFrom(Context context, View view) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(view, "view");
        Object systemService = context.getSystemService("input_method");
        if (systemService != null) {
            ((InputMethodManager) systemService).hideSoftInputFromWindow(view.getWindowToken(), 0);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
    }

    public final void showKeyboardFrom(Context context, View view) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Object systemService = context.getSystemService("input_method");
        if (systemService != null) {
            ((InputMethodManager) systemService).showSoftInput(view, 2);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
    }

    public final boolean isBluetoothAvailable() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        return defaultAdapter != null && defaultAdapter.isEnabled() && defaultAdapter.getState() == 12;
    }
}
