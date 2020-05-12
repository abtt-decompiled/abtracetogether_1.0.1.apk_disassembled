package ca.albertahealthservices.contacttracing.services;

import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.services.BluetoothMonitoringService.StreetPassReceiver;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord;
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
@DebugMetadata(c = "ca.albertahealthservices.contacttracing.services.BluetoothMonitoringService$StreetPassReceiver$onReceive$1", f = "BluetoothMonitoringService.kt", i = {0}, l = {646}, m = "invokeSuspend", n = {"$this$launch"}, s = {"L$0"})
/* compiled from: BluetoothMonitoringService.kt */
final class BluetoothMonitoringService$StreetPassReceiver$onReceive$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ StreetPassRecord $record;
    Object L$0;
    int label;
    private CoroutineScope p$;
    final /* synthetic */ StreetPassReceiver this$0;

    BluetoothMonitoringService$StreetPassReceiver$onReceive$1(StreetPassReceiver streetPassReceiver, StreetPassRecord streetPassRecord, Continuation continuation) {
        this.this$0 = streetPassReceiver;
        this.$record = streetPassRecord;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        BluetoothMonitoringService$StreetPassReceiver$onReceive$1 bluetoothMonitoringService$StreetPassReceiver$onReceive$1 = new BluetoothMonitoringService$StreetPassReceiver$onReceive$1(this.this$0, this.$record, continuation);
        bluetoothMonitoringService$StreetPassReceiver$onReceive$1.p$ = (CoroutineScope) obj;
        return bluetoothMonitoringService$StreetPassReceiver$onReceive$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((BluetoothMonitoringService$StreetPassReceiver$onReceive$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope = this.p$;
            Companion companion = CentralLog.Companion;
            String access$getTAG$p = this.this$0.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Coroutine - Saving StreetPassRecord: ");
            sb.append(Utils.INSTANCE.getDate(this.$record.getTimestamp()));
            companion.d(access$getTAG$p, sb.toString());
            StreetPassRecordStorage access$getStreetPassRecordStorage$p = BluetoothMonitoringService.access$getStreetPassRecordStorage$p(BluetoothMonitoringService.this);
            StreetPassRecord streetPassRecord = this.$record;
            this.L$0 = coroutineScope;
            this.label = 1;
            if (access$getStreetPassRecordStorage$p.saveRecord(streetPassRecord, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
            CoroutineScope coroutineScope2 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
