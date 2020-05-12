package ca.albertahealthservices.contacttracing.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ca.albertahealthservices.contacttracing.BuildConfig;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.RestartActivity;
import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.bluetooth.BLEAdvertiser;
import ca.albertahealthservices.contacttracing.bluetooth.gatt.GATTKt;
import ca.albertahealthservices.contacttracing.idmanager.TempIDManager;
import ca.albertahealthservices.contacttracing.idmanager.TemporaryID;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.WFLog;
import ca.albertahealthservices.contacttracing.notifications.NotificationTemplates;
import ca.albertahealthservices.contacttracing.permissions.RequestFileWritePermission;
import ca.albertahealthservices.contacttracing.status.Status;
import ca.albertahealthservices.contacttracing.status.persistence.StatusRecord;
import ca.albertahealthservices.contacttracing.status.persistence.StatusRecordStorage;
import ca.albertahealthservices.contacttracing.streetpass.ConnectionRecord;
import ca.albertahealthservices.contacttracing.streetpass.StreetPassScanner;
import ca.albertahealthservices.contacttracing.streetpass.StreetPassServer;
import ca.albertahealthservices.contacttracing.streetpass.StreetPassWorker;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecordStorage;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.collections.MapsKt;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference0Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.ranges.RangesKt;
import kotlin.reflect.KProperty;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.Job.DefaultImpls;
import kotlinx.coroutines.JobKt;
import pub.devrel.easypermissions.EasyPermissions;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000®\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\u0018\u0000 `2\u00020\u00012\u00020\u0002:\u0006^_`abcB\u0005¢\u0006\u0002\u0010\u0003J\b\u0010*\u001a\u00020+H\u0002J\b\u0010,\u001a\u00020+H\u0002J\b\u0010-\u001a\u00020+H\u0002J\b\u0010.\u001a\u00020+H\u0002J\b\u0010/\u001a\u00020+H\u0002J\b\u00100\u001a\u00020+H\u0002J\b\u00101\u001a\u00020+H\u0002J\u0006\u00102\u001a\u00020+J\u0016\u00103\u001a\u0002042\u0006\u00105\u001a\u0002042\u0006\u00106\u001a\u000204J\b\u00107\u001a\u000208H\u0002J\b\u00109\u001a\u000208H\u0002J\b\u0010:\u001a\u000208H\u0002J\u0012\u0010;\u001a\u00020+2\b\b\u0002\u0010<\u001a\u000208H\u0002J\u0012\u0010=\u001a\u00020+2\b\b\u0002\u0010<\u001a\u000208H\u0002J\u0014\u0010>\u001a\u0004\u0018\u00010?2\b\u0010@\u001a\u0004\u0018\u00010AH\u0016J\b\u0010B\u001a\u00020+H\u0016J\b\u0010C\u001a\u00020+H\u0016J\"\u0010D\u001a\u00020E2\b\u0010@\u001a\u0004\u0018\u00010A2\u0006\u0010F\u001a\u00020E2\u0006\u0010G\u001a\u00020EH\u0016J\b\u0010H\u001a\u00020+H\u0002J\b\u0010I\u001a\u00020+H\u0002J\b\u0010J\u001a\u00020+H\u0002J\b\u0010K\u001a\u00020+H\u0002J\b\u0010L\u001a\u00020+H\u0002J\u0010\u0010M\u001a\u00020+2\b\u0010N\u001a\u0004\u0018\u00010OJ\b\u0010P\u001a\u00020+H\u0002J\b\u0010Q\u001a\u00020+H\u0002J\b\u0010R\u001a\u00020+H\u0002J\b\u0010S\u001a\u00020+H\u0002J\b\u0010T\u001a\u00020+H\u0002J\b\u0010U\u001a\u00020+H\u0002J\b\u0010V\u001a\u00020+H\u0002J\b\u0010W\u001a\u00020+H\u0002J\b\u0010X\u001a\u00020+H\u0002J\b\u0010Y\u001a\u00020+H\u0002J\b\u0010Z\u001a\u00020+H\u0002J\b\u0010[\u001a\u00020+H\u0002J\u0006\u0010\\\u001a\u00020+J\b\u0010]\u001a\u00020+H\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u00060\u0007R\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX.¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000b8VX\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X.¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X.¢\u0006\u0002\n\u0000R\u0012\u0010\u0018\u001a\u00060\u0019R\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX.¢\u0006\u0002\n\u0000R\u0012\u0010\u001c\u001a\u00060\u001dR\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX.¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u000e¢\u0006\u0002\n\u0000R\u001c\u0010$\u001a\u0004\u0018\u00010%X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)¨\u0006d²\u0006\f\u0010e\u001a\u0004\u0018\u00010fX\u0002"}, d2 = {"Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService;", "Landroid/app/Service;", "Lkotlinx/coroutines/CoroutineScope;", "()V", "advertiser", "Lca/albertahealthservices/contacttracing/bluetooth/BLEAdvertiser;", "bluetoothStatusReceiver", "Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$BluetoothStatusReceiver;", "commandHandler", "Lca/albertahealthservices/contacttracing/services/CommandHandler;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "job", "Lkotlinx/coroutines/Job;", "localBroadcastManager", "Landroidx/localbroadcastmanager/content/LocalBroadcastManager;", "mNotificationManager", "Landroid/app/NotificationManager;", "notificationShown", "Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$NOTIFICATION_STATE;", "serviceUUID", "", "statusReceiver", "Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$StatusReceiver;", "statusRecordStorage", "Lca/albertahealthservices/contacttracing/status/persistence/StatusRecordStorage;", "streetPassReceiver", "Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$StreetPassReceiver;", "streetPassRecordStorage", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordStorage;", "streetPassScanner", "Lca/albertahealthservices/contacttracing/streetpass/StreetPassScanner;", "streetPassServer", "Lca/albertahealthservices/contacttracing/streetpass/StreetPassServer;", "worker", "Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker;", "getWorker", "()Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker;", "setWorker", "(Lca/albertahealthservices/contacttracing/streetpass/StreetPassWorker;)V", "acquireWritePermission", "", "actionAdvertise", "actionHealthCheck", "actionPurge", "actionScan", "actionStart", "actionStop", "actionUpdateBm", "calcPhaseShift", "", "min", "max", "hasLocationPermissions", "", "hasWritePermissions", "isBluetoothEnabled", "notifyLackingThings", "override", "notifyRunning", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "performHealthCheck", "performPurge", "performScan", "performUserLoginCheck", "registerReceivers", "runService", "cmd", "Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$Command;", "scheduleAdvertisement", "scheduleScan", "setup", "setupAdvertiser", "setupAdvertisingCycles", "setupCycles", "setupNotifications", "setupScanCycles", "setupScanner", "setupService", "startScan", "stopService", "teardown", "unregisterReceivers", "BluetoothStatusReceiver", "Command", "Companion", "NOTIFICATION_STATE", "StatusReceiver", "StreetPassReceiver", "app_release", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;"}, k = 1, mv = {1, 1, 16})
/* compiled from: BluetoothMonitoringService.kt */
public final class BluetoothMonitoringService extends Service implements CoroutineScope {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property0(new PropertyReference0Impl(Reflection.getOrCreateKotlinClass(BluetoothMonitoringService.class), "bluetoothAdapter", "<v#0>"))};
    private static final String CHANNEL_ID = "ACT Updates";
    /* access modifiers changed from: private */
    public static final String CHANNEL_SERVICE = "ACT Foreground Service";
    /* access modifiers changed from: private */
    public static final String COMMAND_KEY = COMMAND_KEY;
    public static final Companion Companion = new Companion(null);
    private static final int NOTIFICATION_ID = 771579;
    /* access modifiers changed from: private */
    public static final int PENDING_ACTIVITY = 5;
    /* access modifiers changed from: private */
    public static final int PENDING_ADVERTISE_REQ_CODE = 8;
    /* access modifiers changed from: private */
    public static final int PENDING_BM_UPDATE = 11;
    /* access modifiers changed from: private */
    public static final int PENDING_HEALTH_CHECK_CODE = 9;
    /* access modifiers changed from: private */
    public static final int PENDING_PURGE_CODE = 12;
    /* access modifiers changed from: private */
    public static final int PENDING_SCAN_REQ_CODE = 7;
    /* access modifiers changed from: private */
    public static final int PENDING_START = 6;
    /* access modifiers changed from: private */
    public static final int PENDING_WIZARD_REQ_CODE = 10;
    /* access modifiers changed from: private */
    public static final int PUSH_NOTIFICATION_ID = 771578;
    /* access modifiers changed from: private */
    public static final String TAG = TAG;
    /* access modifiers changed from: private */
    public static final long advertisingDuration = 180000;
    /* access modifiers changed from: private */
    public static final long advertisingGap = 5000;
    /* access modifiers changed from: private */
    public static final long blacklistDuration = 100000;
    /* access modifiers changed from: private */
    public static final long bmCheckInterval = 540000;
    /* access modifiers changed from: private */
    public static TemporaryID broadcastMessage = null;
    /* access modifiers changed from: private */
    public static final long connectionTimeout = 6000;
    /* access modifiers changed from: private */
    public static final long healthCheckInterval = 900000;
    /* access modifiers changed from: private */
    public static final boolean infiniteAdvertising = false;
    /* access modifiers changed from: private */
    public static final boolean infiniteScanning = false;
    /* access modifiers changed from: private */
    public static final long maxQueueTime = 7000;
    /* access modifiers changed from: private */
    public static final long maxScanInterval = 60000;
    /* access modifiers changed from: private */
    public static final long minScanInterval = 35000;
    /* access modifiers changed from: private */
    public static final long purgeInterval = 86400000;
    /* access modifiers changed from: private */
    public static final long purgeTTL = 1814400000;
    /* access modifiers changed from: private */
    public static final long scanDuration = 10000;
    /* access modifiers changed from: private */
    public static final boolean useBlacklist = true;
    private BLEAdvertiser advertiser;
    private final BluetoothStatusReceiver bluetoothStatusReceiver = new BluetoothStatusReceiver();
    private CommandHandler commandHandler;
    private Job job = JobKt.Job$default((Job) null, 1, (Object) null);
    private LocalBroadcastManager localBroadcastManager;
    private NotificationManager mNotificationManager;
    private NOTIFICATION_STATE notificationShown;
    private String serviceUUID;
    private final StatusReceiver statusReceiver = new StatusReceiver();
    /* access modifiers changed from: private */
    public StatusRecordStorage statusRecordStorage;
    private final StreetPassReceiver streetPassReceiver = new StreetPassReceiver();
    /* access modifiers changed from: private */
    public StreetPassRecordStorage streetPassRecordStorage;
    private StreetPassScanner streetPassScanner;
    private StreetPassServer streetPassServer;
    private StreetPassWorker worker;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016¨\u0006\t"}, d2 = {"Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$BluetoothStatusReceiver;", "Landroid/content/BroadcastReceiver;", "(Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService;)V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: BluetoothMonitoringService.kt */
    public final class BluetoothStatusReceiver extends BroadcastReceiver {
        public BluetoothStatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && Intrinsics.areEqual((Object) intent.getAction(), (Object) "android.bluetooth.adapter.action.STATE_CHANGED")) {
                switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1)) {
                    case 10:
                        CentralLog.Companion.d(BluetoothMonitoringService.TAG, "BluetoothAdapter.STATE_OFF");
                        return;
                    case 11:
                        CentralLog.Companion.d(BluetoothMonitoringService.TAG, "BluetoothAdapter.STATE_TURNING_ON");
                        return;
                    case 12:
                        CentralLog.Companion.d(BluetoothMonitoringService.TAG, "BluetoothAdapter.STATE_ON");
                        Utils utils = Utils.INSTANCE;
                        Context applicationContext = BluetoothMonitoringService.this.getApplicationContext();
                        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "this@BluetoothMonitoringService.applicationContext");
                        utils.startBluetoothMonitoringService(applicationContext);
                        return;
                    case 13:
                        CentralLog.Companion.d(BluetoothMonitoringService.TAG, "BluetoothAdapter.STATE_TURNING_OFF");
                        BluetoothMonitoringService.notifyLackingThings$default(BluetoothMonitoringService.this, false, 1, null);
                        BluetoothMonitoringService.this.teardown();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\b\u0001\u0018\u0000 \u00132\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0013B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012¨\u0006\u0014"}, d2 = {"Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$Command;", "", "index", "", "string", "", "(Ljava/lang/String;IILjava/lang/String;)V", "getIndex", "()I", "getString", "()Ljava/lang/String;", "INVALID", "ACTION_START", "ACTION_SCAN", "ACTION_STOP", "ACTION_ADVERTISE", "ACTION_SELF_CHECK", "ACTION_UPDATE_BM", "ACTION_PURGE", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: BluetoothMonitoringService.kt */
    public enum Command {
        INVALID(-1, r2),
        ACTION_START(0, "START"),
        ACTION_SCAN(1, "SCAN"),
        ACTION_STOP(2, "STOP"),
        ACTION_ADVERTISE(3, "ADVERTISE"),
        ACTION_SELF_CHECK(4, "SELF_CHECK"),
        ACTION_UPDATE_BM(5, "UPDATE_BM"),
        ACTION_PURGE(6, "PURGE");
        
        public static final Companion Companion = null;
        /* access modifiers changed from: private */
        public static final Map<Integer, Command> types = null;
        private final int index;
        private final String string;

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u00062\u0006\u0010\b\u001a\u00020\u0005R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$Command$Companion;", "", "()V", "types", "", "", "Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$Command;", "findByValue", "value", "app_release"}, k = 1, mv = {1, 1, 16})
        /* compiled from: BluetoothMonitoringService.kt */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final Command findByValue(int i) {
                return (Command) Command.types.get(Integer.valueOf(i));
            }
        }

        private Command(int i, String str) {
            this.index = i;
            this.string = str;
        }

        public final int getIndex() {
            return this.index;
        }

        public final String getString() {
            return this.string;
        }

        static {
            int i;
            Companion = new Companion(null);
            Command[] values = values();
            Map<Integer, Command> linkedHashMap = new LinkedHashMap<>(RangesKt.coerceAtLeast(MapsKt.mapCapacity(values.length), 16));
            for (Command command : values) {
                Pair pair = TuplesKt.to(Integer.valueOf(command.index), command);
                linkedHashMap.put(pair.getFirst(), pair.getSecond());
            }
            types = linkedHashMap;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0015\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0013\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u000e\u0010\n\u001a\u00020\u000bXD¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0014\u0010\u0011\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0014\u0010\u0013\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0014\u0010\u0015\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000eR\u0014\u0010\u0017\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000eR\u0014\u0010\u0019\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u000eR\u0014\u0010\u001b\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u000eR\u0014\u0010\u001d\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u000eR\u000e\u0010\u001f\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u0014\u0010 \u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0014\u0010$\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b%\u0010#R\u0014\u0010&\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b'\u0010#R\u0014\u0010(\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b)\u0010#R\u001c\u0010*\u001a\u0004\u0018\u00010+X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\u0014\u00100\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b1\u0010#R\u0014\u00102\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b3\u0010#R\u0014\u00104\u001a\u000205XD¢\u0006\b\n\u0000\u001a\u0004\b6\u00107R\u0014\u00108\u001a\u000205XD¢\u0006\b\n\u0000\u001a\u0004\b9\u00107R\u0014\u0010:\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b;\u0010#R\u0014\u0010<\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b=\u0010#R\u0014\u0010>\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\b?\u0010#R\u0014\u0010@\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\bA\u0010#R\u0014\u0010B\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\bC\u0010#R\u0014\u0010D\u001a\u00020!XD¢\u0006\b\n\u0000\u001a\u0004\bE\u0010#R\u0014\u0010F\u001a\u000205XD¢\u0006\b\n\u0000\u001a\u0004\bG\u00107¨\u0006H"}, d2 = {"Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$Companion;", "", "()V", "CHANNEL_ID", "", "CHANNEL_SERVICE", "getCHANNEL_SERVICE", "()Ljava/lang/String;", "COMMAND_KEY", "getCOMMAND_KEY", "NOTIFICATION_ID", "", "PENDING_ACTIVITY", "getPENDING_ACTIVITY", "()I", "PENDING_ADVERTISE_REQ_CODE", "getPENDING_ADVERTISE_REQ_CODE", "PENDING_BM_UPDATE", "getPENDING_BM_UPDATE", "PENDING_HEALTH_CHECK_CODE", "getPENDING_HEALTH_CHECK_CODE", "PENDING_PURGE_CODE", "getPENDING_PURGE_CODE", "PENDING_SCAN_REQ_CODE", "getPENDING_SCAN_REQ_CODE", "PENDING_START", "getPENDING_START", "PENDING_WIZARD_REQ_CODE", "getPENDING_WIZARD_REQ_CODE", "PUSH_NOTIFICATION_ID", "getPUSH_NOTIFICATION_ID", "TAG", "advertisingDuration", "", "getAdvertisingDuration", "()J", "advertisingGap", "getAdvertisingGap", "blacklistDuration", "getBlacklistDuration", "bmCheckInterval", "getBmCheckInterval", "broadcastMessage", "Lca/albertahealthservices/contacttracing/idmanager/TemporaryID;", "getBroadcastMessage", "()Lca/albertahealthservices/contacttracing/idmanager/TemporaryID;", "setBroadcastMessage", "(Lca/albertahealthservices/contacttracing/idmanager/TemporaryID;)V", "connectionTimeout", "getConnectionTimeout", "healthCheckInterval", "getHealthCheckInterval", "infiniteAdvertising", "", "getInfiniteAdvertising", "()Z", "infiniteScanning", "getInfiniteScanning", "maxQueueTime", "getMaxQueueTime", "maxScanInterval", "getMaxScanInterval", "minScanInterval", "getMinScanInterval", "purgeInterval", "getPurgeInterval", "purgeTTL", "getPurgeTTL", "scanDuration", "getScanDuration", "useBlacklist", "getUseBlacklist", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: BluetoothMonitoringService.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getCHANNEL_SERVICE() {
            return BluetoothMonitoringService.CHANNEL_SERVICE;
        }

        public final int getPUSH_NOTIFICATION_ID() {
            return BluetoothMonitoringService.PUSH_NOTIFICATION_ID;
        }

        public final String getCOMMAND_KEY() {
            return BluetoothMonitoringService.COMMAND_KEY;
        }

        public final int getPENDING_ACTIVITY() {
            return BluetoothMonitoringService.PENDING_ACTIVITY;
        }

        public final int getPENDING_START() {
            return BluetoothMonitoringService.PENDING_START;
        }

        public final int getPENDING_SCAN_REQ_CODE() {
            return BluetoothMonitoringService.PENDING_SCAN_REQ_CODE;
        }

        public final int getPENDING_ADVERTISE_REQ_CODE() {
            return BluetoothMonitoringService.PENDING_ADVERTISE_REQ_CODE;
        }

        public final int getPENDING_HEALTH_CHECK_CODE() {
            return BluetoothMonitoringService.PENDING_HEALTH_CHECK_CODE;
        }

        public final int getPENDING_WIZARD_REQ_CODE() {
            return BluetoothMonitoringService.PENDING_WIZARD_REQ_CODE;
        }

        public final int getPENDING_BM_UPDATE() {
            return BluetoothMonitoringService.PENDING_BM_UPDATE;
        }

        public final int getPENDING_PURGE_CODE() {
            return BluetoothMonitoringService.PENDING_PURGE_CODE;
        }

        public final TemporaryID getBroadcastMessage() {
            return BluetoothMonitoringService.broadcastMessage;
        }

        public final void setBroadcastMessage(TemporaryID temporaryID) {
            BluetoothMonitoringService.broadcastMessage = temporaryID;
        }

        public final long getScanDuration() {
            return BluetoothMonitoringService.scanDuration;
        }

        public final long getMinScanInterval() {
            return BluetoothMonitoringService.minScanInterval;
        }

        public final long getMaxScanInterval() {
            return BluetoothMonitoringService.maxScanInterval;
        }

        public final long getAdvertisingDuration() {
            return BluetoothMonitoringService.advertisingDuration;
        }

        public final long getAdvertisingGap() {
            return BluetoothMonitoringService.advertisingGap;
        }

        public final long getMaxQueueTime() {
            return BluetoothMonitoringService.maxQueueTime;
        }

        public final long getBmCheckInterval() {
            return BluetoothMonitoringService.bmCheckInterval;
        }

        public final long getHealthCheckInterval() {
            return BluetoothMonitoringService.healthCheckInterval;
        }

        public final long getPurgeInterval() {
            return BluetoothMonitoringService.purgeInterval;
        }

        public final long getPurgeTTL() {
            return BluetoothMonitoringService.purgeTTL;
        }

        public final long getConnectionTimeout() {
            return BluetoothMonitoringService.connectionTimeout;
        }

        public final long getBlacklistDuration() {
            return BluetoothMonitoringService.blacklistDuration;
        }

        public final boolean getInfiniteScanning() {
            return BluetoothMonitoringService.infiniteScanning;
        }

        public final boolean getInfiniteAdvertising() {
            return BluetoothMonitoringService.infiniteAdvertising;
        }

        public final boolean getUseBlacklist() {
            return BluetoothMonitoringService.useBlacklist;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, d2 = {"Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$NOTIFICATION_STATE;", "", "(Ljava/lang/String;I)V", "RUNNING", "LACKING_THINGS", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: BluetoothMonitoringService.kt */
    public enum NOTIFICATION_STATE {
        RUNNING,
        LACKING_THINGS
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$StatusReceiver;", "Landroid/content/BroadcastReceiver;", "(Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService;)V", "TAG", "", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: BluetoothMonitoringService.kt */
    public final class StatusReceiver extends BroadcastReceiver {
        private final String TAG = "StatusReceiver";

        public StatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(intent, "intent");
            if (Intrinsics.areEqual((Object) GATTKt.ACTION_RECEIVED_STATUS, (Object) intent.getAction())) {
                Parcelable parcelableExtra = intent.getParcelableExtra(GATTKt.STATUS);
                Intrinsics.checkExpressionValueIsNotNull(parcelableExtra, "intent.getParcelableExtra(STATUS)");
                Status status = (Status) parcelableExtra;
                ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion = CentralLog.Companion;
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Status received: ");
                sb.append(status.getMsg());
                companion.d(str, sb.toString());
                if (status.getMsg().length() > 0) {
                    BuildersKt__Builders_commonKt.launch$default(BluetoothMonitoringService.this, null, null, new BluetoothMonitoringService$StatusReceiver$onReceive$1(this, new StatusRecord(status.getMsg()), null), 3, null);
                }
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService$StreetPassReceiver;", "Landroid/content/BroadcastReceiver;", "(Lca/albertahealthservices/contacttracing/services/BluetoothMonitoringService;)V", "TAG", "", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: BluetoothMonitoringService.kt */
    public final class StreetPassReceiver extends BroadcastReceiver {
        /* access modifiers changed from: private */
        public final String TAG = "StreetPassReceiver";

        public StreetPassReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Intent intent2 = intent;
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(intent2, "intent");
            if (Intrinsics.areEqual((Object) GATTKt.ACTION_RECEIVED_STREETPASS, (Object) intent.getAction())) {
                Parcelable parcelableExtra = intent2.getParcelableExtra(GATTKt.STREET_PASS);
                Intrinsics.checkExpressionValueIsNotNull(parcelableExtra, "intent.getParcelableExtra(STREET_PASS)");
                ConnectionRecord connectionRecord = (ConnectionRecord) parcelableExtra;
                ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion = CentralLog.Companion;
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("StreetPass received: ");
                sb.append(connectionRecord);
                companion.d(str, sb.toString());
                if (connectionRecord.getMsg().length() > 0) {
                    StreetPassRecord streetPassRecord = new StreetPassRecord(connectionRecord.getVersion(), connectionRecord.getMsg(), connectionRecord.getOrg(), connectionRecord.getPeripheral().getModelP(), connectionRecord.getCentral().getModelC(), connectionRecord.getRssi(), connectionRecord.getTxPower());
                    BuildersKt__Builders_commonKt.launch$default(BluetoothMonitoringService.this, null, null, new BluetoothMonitoringService$StreetPassReceiver$onReceive$1(this, streetPassRecord, null), 3, null);
                }
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 16})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Command.values().length];
            $EnumSwitchMapping$0 = iArr;
            iArr[Command.ACTION_START.ordinal()] = 1;
            $EnumSwitchMapping$0[Command.ACTION_SCAN.ordinal()] = 2;
            $EnumSwitchMapping$0[Command.ACTION_ADVERTISE.ordinal()] = 3;
            $EnumSwitchMapping$0[Command.ACTION_UPDATE_BM.ordinal()] = 4;
            $EnumSwitchMapping$0[Command.ACTION_STOP.ordinal()] = 5;
            $EnumSwitchMapping$0[Command.ACTION_SELF_CHECK.ordinal()] = 6;
            $EnumSwitchMapping$0[Command.ACTION_PURGE.ordinal()] = 7;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final /* synthetic */ StatusRecordStorage access$getStatusRecordStorage$p(BluetoothMonitoringService bluetoothMonitoringService) {
        StatusRecordStorage statusRecordStorage2 = bluetoothMonitoringService.statusRecordStorage;
        if (statusRecordStorage2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("statusRecordStorage");
        }
        return statusRecordStorage2;
    }

    public static final /* synthetic */ StreetPassRecordStorage access$getStreetPassRecordStorage$p(BluetoothMonitoringService bluetoothMonitoringService) {
        StreetPassRecordStorage streetPassRecordStorage2 = bluetoothMonitoringService.streetPassRecordStorage;
        if (streetPassRecordStorage2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("streetPassRecordStorage");
        }
        return streetPassRecordStorage2;
    }

    public final StreetPassWorker getWorker() {
        return this.worker;
    }

    public final void setWorker(StreetPassWorker streetPassWorker) {
        this.worker = streetPassWorker;
    }

    public CoroutineContext getCoroutineContext() {
        return Dispatchers.getMain().plus(this.job);
    }

    public void onCreate() {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
        Intrinsics.checkExpressionValueIsNotNull(instance, "LocalBroadcastManager.getInstance(this)");
        this.localBroadcastManager = instance;
        setup();
    }

    private final void setup() {
        Object systemService = getSystemService("power");
        if (systemService != null) {
            CentralLog.Companion.setPowerManager((PowerManager) systemService);
            this.commandHandler = new CommandHandler(new WeakReference(this));
            CentralLog.Companion.d(TAG, "Creating service - BluetoothMonitoringService");
            this.serviceUUID = BuildConfig.BLE_SSID;
            Context applicationContext = getApplicationContext();
            String str = "this.applicationContext";
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, str);
            this.worker = new StreetPassWorker(applicationContext);
            unregisterReceivers();
            registerReceivers();
            Context applicationContext2 = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext2, str);
            this.streetPassRecordStorage = new StreetPassRecordStorage(applicationContext2);
            Context applicationContext3 = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext3, str);
            this.statusRecordStorage = new StatusRecordStorage(applicationContext3);
            setupNotifications();
            TempIDManager tempIDManager = TempIDManager.INSTANCE;
            Context applicationContext4 = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext4, str);
            broadcastMessage = tempIDManager.retrieveTemporaryID(applicationContext4);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.os.PowerManager");
    }

    public final void teardown() {
        StreetPassServer streetPassServer2 = this.streetPassServer;
        if (streetPassServer2 != null) {
            streetPassServer2.tearDown();
        }
        this.streetPassServer = null;
        StreetPassScanner streetPassScanner2 = this.streetPassScanner;
        if (streetPassScanner2 != null) {
            streetPassScanner2.stopScan();
        }
        this.streetPassScanner = null;
        CommandHandler commandHandler2 = this.commandHandler;
        if (commandHandler2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("commandHandler");
        }
        commandHandler2.removeCallbacksAndMessages(null);
        Utils utils = Utils.INSTANCE;
        Context applicationContext = getApplicationContext();
        String str = "this.applicationContext";
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, str);
        utils.cancelBMUpdateCheck(applicationContext);
        Utils utils2 = Utils.INSTANCE;
        Context applicationContext2 = getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext2, str);
        utils2.cancelNextScan(applicationContext2);
        Utils utils3 = Utils.INSTANCE;
        Context applicationContext3 = getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext3, str);
        utils3.cancelNextAdvertise(applicationContext3);
    }

    private final void setupNotifications() {
        Object systemService = getSystemService("notification");
        if (systemService != null) {
            this.mNotificationManager = (NotificationManager) systemService;
            if (VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_SERVICE, 2);
                notificationChannel.enableLights(false);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{0});
                notificationChannel.setSound(null, null);
                notificationChannel.setShowBadge(false);
                NotificationManager notificationManager = this.mNotificationManager;
                if (notificationManager == null) {
                    Intrinsics.throwNpe();
                }
                notificationManager.createNotificationChannel(notificationChannel);
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.app.NotificationManager");
    }

    static /* synthetic */ void notifyLackingThings$default(BluetoothMonitoringService bluetoothMonitoringService, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        bluetoothMonitoringService.notifyLackingThings(z);
    }

    private final void notifyLackingThings(boolean z) {
        if (this.notificationShown != NOTIFICATION_STATE.LACKING_THINGS || z) {
            ca.albertahealthservices.contacttracing.notifications.NotificationTemplates.Companion companion = NotificationTemplates.Companion;
            Context applicationContext = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, "this.applicationContext");
            startForeground(NOTIFICATION_ID, companion.lackingThingsNotification(applicationContext, CHANNEL_ID));
            this.notificationShown = NOTIFICATION_STATE.LACKING_THINGS;
        }
    }

    static /* synthetic */ void notifyRunning$default(BluetoothMonitoringService bluetoothMonitoringService, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        bluetoothMonitoringService.notifyRunning(z);
    }

    private final void notifyRunning(boolean z) {
        if (this.notificationShown != NOTIFICATION_STATE.RUNNING || z) {
            ca.albertahealthservices.contacttracing.notifications.NotificationTemplates.Companion companion = NotificationTemplates.Companion;
            Context applicationContext = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, "this.applicationContext");
            startForeground(NOTIFICATION_ID, companion.getRunningNotification(applicationContext, CHANNEL_ID));
            this.notificationShown = NOTIFICATION_STATE.RUNNING;
        }
    }

    private final boolean hasLocationPermissions() {
        String[] requiredPermissions = Utils.INSTANCE.getRequiredPermissions();
        return EasyPermissions.hasPermissions(getApplicationContext(), (String[]) Arrays.copyOf(requiredPermissions, requiredPermissions.length));
    }

    private final boolean hasWritePermissions() {
        return EasyPermissions.hasPermissions(getApplicationContext(), (String[]) Arrays.copyOf(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1));
    }

    private final void acquireWritePermission() {
        Intent intent = new Intent(getApplicationContext(), RequestFileWritePermission.class);
        intent.setFlags(268435456);
        startActivity(intent);
    }

    private final boolean isBluetoothEnabled() {
        Lazy lazy = LazyKt.lazy(LazyThreadSafetyMode.NONE, (Function0) new BluetoothMonitoringService$isBluetoothEnabled$bluetoothAdapter$2(this));
        KProperty kProperty = $$delegatedProperties[0];
        BluetoothAdapter bluetoothAdapter = (BluetoothAdapter) lazy.getValue();
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        CentralLog.Companion.i(TAG, "Service onStartCommand");
        if (!hasLocationPermissions() || !isBluetoothEnabled()) {
            ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion = CentralLog.Companion;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("location permission: ");
            sb.append(hasLocationPermissions());
            sb.append(" bluetooth: ");
            sb.append(isBluetoothEnabled());
            companion.i(str, sb.toString());
            notifyLackingThings$default(this, false, 1, null);
            return 1;
        } else if (intent != null) {
            runService(Command.Companion.findByValue(intent.getIntExtra(COMMAND_KEY, Command.INVALID.getIndex())));
            return 1;
        } else {
            CentralLog.Companion.e(TAG, "WTF? Nothing in intent @ onStartCommand");
            CommandHandler commandHandler2 = this.commandHandler;
            if (commandHandler2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("commandHandler");
            }
            commandHandler2.startBluetoothMonitoringService();
            return 1;
        }
    }

    public final void runService(Command command) {
        ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion = CentralLog.Companion;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Command is:");
        sb.append(command != null ? command.getString() : null);
        companion.i(str, sb.toString());
        if (!hasLocationPermissions() || !isBluetoothEnabled()) {
            ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion2 = CentralLog.Companion;
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("location permission: ");
            sb2.append(hasLocationPermissions());
            sb2.append(" bluetooth: ");
            sb2.append(isBluetoothEnabled());
            companion2.i(str2, sb2.toString());
            notifyLackingThings$default(this, false, 1, null);
            return;
        }
        notifyRunning$default(this, false, 1, null);
        if (command != null) {
            String str3 = "this.applicationContext";
            switch (WhenMappings.$EnumSwitchMapping$0[command.ordinal()]) {
                case 1:
                    setupService();
                    Utils utils = Utils.INSTANCE;
                    Context applicationContext = getApplicationContext();
                    Intrinsics.checkExpressionValueIsNotNull(applicationContext, str3);
                    utils.scheduleNextHealthCheck(applicationContext, healthCheckInterval);
                    Utils utils2 = Utils.INSTANCE;
                    Context applicationContext2 = getApplicationContext();
                    Intrinsics.checkExpressionValueIsNotNull(applicationContext2, str3);
                    utils2.scheduleRepeatingPurge(applicationContext2, purgeInterval);
                    Utils utils3 = Utils.INSTANCE;
                    Context applicationContext3 = getApplicationContext();
                    Intrinsics.checkExpressionValueIsNotNull(applicationContext3, str3);
                    utils3.scheduleBMUpdateCheck(applicationContext3, bmCheckInterval);
                    actionStart();
                    break;
                case 2:
                    scheduleScan();
                    actionScan();
                    break;
                case 3:
                    scheduleAdvertisement();
                    actionAdvertise();
                    break;
                case 4:
                    Utils utils4 = Utils.INSTANCE;
                    Context applicationContext4 = getApplicationContext();
                    Intrinsics.checkExpressionValueIsNotNull(applicationContext4, str3);
                    utils4.scheduleBMUpdateCheck(applicationContext4, bmCheckInterval);
                    actionUpdateBm();
                    break;
                case 5:
                    actionStop();
                    break;
                case 6:
                    Utils utils5 = Utils.INSTANCE;
                    Context applicationContext5 = getApplicationContext();
                    Intrinsics.checkExpressionValueIsNotNull(applicationContext5, str3);
                    utils5.scheduleNextHealthCheck(applicationContext5, healthCheckInterval);
                    actionHealthCheck();
                    break;
                case 7:
                    actionPurge();
                    break;
            }
        }
        ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion3 = CentralLog.Companion;
        String str4 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Invalid / ignored command: ");
        sb3.append(command);
        sb3.append(". Nothing to do");
        companion3.i(str4, sb3.toString());
    }

    private final void actionStop() {
        stopForeground(true);
        stopSelf();
        CentralLog.Companion.w(TAG, "Service Stopping");
    }

    private final void actionHealthCheck() {
        performUserLoginCheck();
        performHealthCheck();
        Utils utils = Utils.INSTANCE;
        Context applicationContext = getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "this.applicationContext");
        utils.scheduleRepeatingPurge(applicationContext, purgeInterval);
    }

    private final void actionPurge() {
        performPurge();
    }

    private final void actionStart() {
        CentralLog.Companion.d(TAG, "Action Start");
        TempIDManager tempIDManager = TempIDManager.INSTANCE;
        Context applicationContext = getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "applicationContext");
        broadcastMessage = tempIDManager.retrieveTemporaryID(applicationContext);
        setupCycles();
        TempIDManager.INSTANCE.getTemporaryIDs(this);
        CentralLog.Companion.d(TAG, "Get TemporaryIDs completed");
        TempIDManager tempIDManager2 = TempIDManager.INSTANCE;
        Context applicationContext2 = getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext2, "this.applicationContext");
        TemporaryID retrieveTemporaryID = tempIDManager2.retrieveTemporaryID(applicationContext2);
        if (retrieveTemporaryID != null) {
            broadcastMessage = retrieveTemporaryID;
            setupCycles();
        }
    }

    public final void actionUpdateBm() {
        TempIDManager tempIDManager = TempIDManager.INSTANCE;
        Context applicationContext = getApplicationContext();
        String str = "this.applicationContext";
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, str);
        if (tempIDManager.needToUpdate(applicationContext) || broadcastMessage == null) {
            CentralLog.Companion.i(TAG, "[TempID] Need to update TemporaryID in actionUpdateBM");
            TempIDManager.INSTANCE.getTemporaryIDs(this);
            TempIDManager tempIDManager2 = TempIDManager.INSTANCE;
            Context applicationContext2 = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext2, str);
            TemporaryID retrieveTemporaryID = tempIDManager2.retrieveTemporaryID(applicationContext2);
            if (retrieveTemporaryID != null) {
                CentralLog.Companion.i(TAG, "[TempID] Updated Temp ID");
                broadcastMessage = retrieveTemporaryID;
            }
            if (retrieveTemporaryID == null) {
                CentralLog.Companion.e(TAG, "[TempID] Failed to fetch new Temp ID");
                return;
            }
            return;
        }
        CentralLog.Companion.i(TAG, "[TempID] Don't need to update Temp ID in actionUpdateBM");
    }

    public final long calcPhaseShift(long j, long j2) {
        return (long) (((double) j) + (Math.random() * ((double) (j2 - j))));
    }

    private final void actionScan() {
        TempIDManager tempIDManager = TempIDManager.INSTANCE;
        Context applicationContext = getApplicationContext();
        String str = "this.applicationContext";
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, str);
        if (tempIDManager.needToUpdate(applicationContext) || broadcastMessage == null) {
            CentralLog.Companion.i(TAG, "[TempID] Need to update TemporaryID in actionScan");
            TempIDManager tempIDManager2 = TempIDManager.INSTANCE;
            Context applicationContext2 = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext2, str);
            tempIDManager2.getTemporaryIDs(applicationContext2);
            TempIDManager tempIDManager3 = TempIDManager.INSTANCE;
            Context applicationContext3 = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext3, str);
            TemporaryID retrieveTemporaryID = tempIDManager3.retrieveTemporaryID(applicationContext3);
            if (retrieveTemporaryID != null) {
                broadcastMessage = retrieveTemporaryID;
                performScan();
            }
        } else {
            CentralLog.Companion.i(TAG, "[TempID] Don't need to update Temp ID in actionScan");
            performScan();
        }
        performScan();
    }

    private final void actionAdvertise() {
        setupAdvertiser();
        if (isBluetoothEnabled()) {
            BLEAdvertiser bLEAdvertiser = this.advertiser;
            if (bLEAdvertiser != null) {
                bLEAdvertiser.startAdvertising(advertisingDuration);
                return;
            }
            return;
        }
        CentralLog.Companion.w(TAG, "Unable to start advertising, bluetooth is off");
    }

    private final void setupService() {
        StreetPassServer streetPassServer2 = this.streetPassServer;
        if (streetPassServer2 == null) {
            Context applicationContext = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, "this.applicationContext");
            String str = this.serviceUUID;
            if (str == null) {
                Intrinsics.throwUninitializedPropertyAccessException("serviceUUID");
            }
            streetPassServer2 = new StreetPassServer(applicationContext, str);
        }
        this.streetPassServer = streetPassServer2;
        setupScanner();
        setupAdvertiser();
    }

    private final void setupScanner() {
        StreetPassScanner streetPassScanner2 = this.streetPassScanner;
        if (streetPassScanner2 == null) {
            Context context = this;
            String str = this.serviceUUID;
            if (str == null) {
                Intrinsics.throwUninitializedPropertyAccessException("serviceUUID");
            }
            streetPassScanner2 = new StreetPassScanner(context, str, scanDuration);
        }
        this.streetPassScanner = streetPassScanner2;
    }

    private final void setupAdvertiser() {
        BLEAdvertiser bLEAdvertiser = this.advertiser;
        if (bLEAdvertiser == null) {
            String str = this.serviceUUID;
            if (str == null) {
                Intrinsics.throwUninitializedPropertyAccessException("serviceUUID");
            }
            bLEAdvertiser = new BLEAdvertiser(str);
        }
        this.advertiser = bLEAdvertiser;
    }

    private final void setupCycles() {
        setupScanCycles();
        setupAdvertisingCycles();
    }

    private final void setupScanCycles() {
        CommandHandler commandHandler2 = this.commandHandler;
        if (commandHandler2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("commandHandler");
        }
        commandHandler2.scheduleNextScan(0);
    }

    private final void setupAdvertisingCycles() {
        CommandHandler commandHandler2 = this.commandHandler;
        if (commandHandler2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("commandHandler");
        }
        commandHandler2.scheduleNextAdvertise(0);
    }

    private final void performScan() {
        setupScanner();
        startScan();
    }

    private final void scheduleScan() {
        if (!infiniteScanning) {
            CommandHandler commandHandler2 = this.commandHandler;
            if (commandHandler2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("commandHandler");
            }
            commandHandler2.scheduleNextScan(scanDuration + calcPhaseShift(minScanInterval, maxScanInterval));
        }
    }

    private final void scheduleAdvertisement() {
        if (!infiniteAdvertising) {
            CommandHandler commandHandler2 = this.commandHandler;
            if (commandHandler2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("commandHandler");
            }
            commandHandler2.scheduleNextAdvertise(advertisingDuration + advertisingGap);
        }
    }

    private final void startScan() {
        if (isBluetoothEnabled()) {
            StreetPassScanner streetPassScanner2 = this.streetPassScanner;
            if (streetPassScanner2 == null) {
                return;
            }
            if (!streetPassScanner2.isScanning()) {
                streetPassScanner2.startScan();
            } else {
                CentralLog.Companion.e(TAG, "Already scanning!");
            }
        } else {
            CentralLog.Companion.w(TAG, "Unable to start scan - bluetooth is off");
        }
    }

    private final void performUserLoginCheck() {
        if (Intrinsics.areEqual((Object) Preference.INSTANCE.getUUID(getApplicationContext()), (Object) "")) {
            Preference preference = Preference.INSTANCE;
            Context applicationContext = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, "applicationContext");
            if (preference.isOnBoarded(applicationContext)) {
                String str = "User is not logged in but has completed onboarding";
                CentralLog.Companion.d(TAG, str);
                WFLog.Companion.logError(str);
                Intent intent = new Intent(getApplicationContext(), RestartActivity.class);
                intent.addFlags(268435456);
                getApplicationContext().startActivity(intent);
            }
        }
    }

    private final void performHealthCheck() {
        CentralLog.Companion.i(TAG, "Performing self diagnosis");
        if (!hasLocationPermissions() || !isBluetoothEnabled()) {
            CentralLog.Companion.i(TAG, "no location permission");
            notifyLackingThings(true);
            return;
        }
        notifyRunning(true);
        setupService();
        String str = "commandHandler";
        if (!infiniteScanning) {
            CommandHandler commandHandler2 = this.commandHandler;
            if (commandHandler2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            if (!commandHandler2.hasScanScheduled()) {
                CentralLog.Companion.w(TAG, "Missing Scan Schedule - rectifying");
                CommandHandler commandHandler3 = this.commandHandler;
                if (commandHandler3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str);
                }
                commandHandler3.scheduleNextScan(100);
            } else {
                CentralLog.Companion.w(TAG, "Scan Schedule present");
            }
        } else {
            CentralLog.Companion.w(TAG, "Should be operating under infinite scan mode");
        }
        if (!infiniteAdvertising) {
            CommandHandler commandHandler4 = this.commandHandler;
            if (commandHandler4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            if (!commandHandler4.hasAdvertiseScheduled()) {
                CentralLog.Companion.w(TAG, "Missing Advertise Schedule - rectifying");
                CommandHandler commandHandler5 = this.commandHandler;
                if (commandHandler5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str);
                }
                commandHandler5.scheduleNextAdvertise(100);
            } else {
                ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion = CentralLog.Companion;
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Advertise Schedule present. Should be advertising?:  ");
                BLEAdvertiser bLEAdvertiser = this.advertiser;
                boolean z = false;
                sb.append(bLEAdvertiser != null ? bLEAdvertiser.getShouldBeAdvertising() : false);
                sb.append(". Is Advertising?: ");
                BLEAdvertiser bLEAdvertiser2 = this.advertiser;
                if (bLEAdvertiser2 != null) {
                    z = bLEAdvertiser2.isAdvertising();
                }
                sb.append(z);
                companion.w(str2, sb.toString());
            }
        } else {
            CentralLog.Companion.w(TAG, "Should be operating under infinite advertise mode");
        }
    }

    private final void performPurge() {
        BuildersKt__Builders_commonKt.launch$default(this, null, null, new BluetoothMonitoringService$performPurge$1(this, this, null), 3, null);
    }

    public void onDestroy() {
        super.onDestroy();
        CentralLog.Companion.i(TAG, "BluetoothMonitoringService destroyed - tearing down");
        stopService();
        CentralLog.Companion.i(TAG, "BluetoothMonitoringService destroyed");
    }

    private final void stopService() {
        teardown();
        unregisterReceivers();
        StreetPassWorker streetPassWorker = this.worker;
        if (streetPassWorker != null) {
            streetPassWorker.terminateConnections();
        }
        StreetPassWorker streetPassWorker2 = this.worker;
        if (streetPassWorker2 != null) {
            streetPassWorker2.unregisterReceivers();
        }
        DefaultImpls.cancel$default(this.job, (CancellationException) null, 1, (Object) null);
    }

    private final void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter(GATTKt.ACTION_RECEIVED_STREETPASS);
        LocalBroadcastManager localBroadcastManager2 = this.localBroadcastManager;
        String str = "localBroadcastManager";
        if (localBroadcastManager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        localBroadcastManager2.registerReceiver(this.streetPassReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter(GATTKt.ACTION_RECEIVED_STATUS);
        LocalBroadcastManager localBroadcastManager3 = this.localBroadcastManager;
        if (localBroadcastManager3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        localBroadcastManager3.registerReceiver(this.statusReceiver, intentFilter2);
        registerReceiver(this.bluetoothStatusReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        CentralLog.Companion.i(TAG, "Receivers registered");
    }

    private final void unregisterReceivers() {
        String str = "localBroadcastManager";
        try {
            LocalBroadcastManager localBroadcastManager2 = this.localBroadcastManager;
            if (localBroadcastManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            localBroadcastManager2.unregisterReceiver(this.streetPassReceiver);
        } catch (Throwable unused) {
            CentralLog.Companion.w(TAG, "streetPassReceiver is not registered?");
        }
        try {
            LocalBroadcastManager localBroadcastManager3 = this.localBroadcastManager;
            if (localBroadcastManager3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            localBroadcastManager3.unregisterReceiver(this.statusReceiver);
        } catch (Throwable unused2) {
            CentralLog.Companion.w(TAG, "statusReceiver is not registered?");
        }
        try {
            unregisterReceiver(this.bluetoothStatusReceiver);
        } catch (Throwable unused3) {
            CentralLog.Companion.w(TAG, "bluetoothStatusReceiver is not registered?");
        }
    }
}
