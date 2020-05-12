package ca.albertahealthservices.contacttracing.services;

import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.status.persistence.StatusRecordStorage;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecordStorage;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 16})
@DebugMetadata(c = "ca.albertahealthservices.contacttracing.services.BluetoothMonitoringService$performPurge$1", f = "BluetoothMonitoringService.kt", i = {0, 0, 1, 1}, l = {526, 527}, m = "invokeSuspend", n = {"$this$launch", "before", "$this$launch", "before"}, s = {"L$0", "J$0", "L$0", "J$0"})
/* compiled from: BluetoothMonitoringService.kt */
final class BluetoothMonitoringService$performPurge$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ BluetoothMonitoringService $context;
    long J$0;
    Object L$0;
    int label;
    private CoroutineScope p$;
    final /* synthetic */ BluetoothMonitoringService this$0;

    BluetoothMonitoringService$performPurge$1(BluetoothMonitoringService bluetoothMonitoringService, BluetoothMonitoringService bluetoothMonitoringService2, Continuation continuation) {
        this.this$0 = bluetoothMonitoringService;
        this.$context = bluetoothMonitoringService2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        BluetoothMonitoringService$performPurge$1 bluetoothMonitoringService$performPurge$1 = new BluetoothMonitoringService$performPurge$1(this.this$0, this.$context, continuation);
        bluetoothMonitoringService$performPurge$1.p$ = (CoroutineScope) obj;
        return bluetoothMonitoringService$performPurge$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((BluetoothMonitoringService$performPurge$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        long j;
        CoroutineScope coroutineScope;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            coroutineScope = this.p$;
            long currentTimeMillis = System.currentTimeMillis() - BluetoothMonitoringService.Companion.getPurgeTTL();
            Companion companion = CentralLog.Companion;
            String access$getTAG$cp = BluetoothMonitoringService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Coroutine - Purging of data before epoch time ");
            sb.append(currentTimeMillis);
            companion.i(access$getTAG$cp, sb.toString());
            StreetPassRecordStorage access$getStreetPassRecordStorage$p = BluetoothMonitoringService.access$getStreetPassRecordStorage$p(this.this$0);
            this.L$0 = coroutineScope;
            this.J$0 = currentTimeMillis;
            this.label = 1;
            if (access$getStreetPassRecordStorage$p.purgeOldRecords(currentTimeMillis, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
            j = currentTimeMillis;
        } else if (i == 1) {
            j = this.J$0;
            coroutineScope = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
        } else if (i == 2) {
            CoroutineScope coroutineScope2 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            Preference.INSTANCE.putLastPurgeTime(this.$context, System.currentTimeMillis());
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        StatusRecordStorage access$getStatusRecordStorage$p = BluetoothMonitoringService.access$getStatusRecordStorage$p(this.this$0);
        this.L$0 = coroutineScope;
        this.J$0 = j;
        this.label = 2;
        if (access$getStatusRecordStorage$p.purgeOldRecords(j, this) == coroutine_suspended) {
            return coroutine_suspended;
        }
        Preference.INSTANCE.putLastPurgeTime(this.$context, System.currentTimeMillis());
        return Unit.INSTANCE;
    }
}
