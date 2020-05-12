package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.ProduceKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.internal.NullSurrogateKt;
import kotlinx.coroutines.selects.SelectBuilder;
import kotlinx.coroutines.selects.SelectBuilderImpl;
import kotlinx.coroutines.selects.SelectClause1;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H@¢\u0006\u0004\b\u0006\u0010\u0007"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;", "downstream", "Lkotlinx/coroutines/flow/FlowCollector;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2", f = "Delay.kt", i = {0, 0, 0, 0, 0}, l = {137}, m = "invokeSuspend", n = {"$this$scopedFlow", "downstream", "values", "lastValue", "ticker"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4"})
/* compiled from: Delay.kt */
final class FlowKt__DelayKt$sample$2 extends SuspendLambda implements Function3<CoroutineScope, FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ long $periodMillis;
    final /* synthetic */ Flow $this_sample;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    int label;
    private CoroutineScope p$;
    private FlowCollector p$0;

    FlowKt__DelayKt$sample$2(Flow flow, long j, Continuation continuation) {
        this.$this_sample = flow;
        this.$periodMillis = j;
        super(3, continuation);
    }

    public final Continuation<Unit> create(CoroutineScope coroutineScope, FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineScope, "$this$create");
        Intrinsics.checkParameterIsNotNull(flowCollector, "downstream");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        FlowKt__DelayKt$sample$2 flowKt__DelayKt$sample$2 = new FlowKt__DelayKt$sample$2(this.$this_sample, this.$periodMillis, continuation);
        flowKt__DelayKt$sample$2.p$ = coroutineScope;
        flowKt__DelayKt$sample$2.p$0 = flowCollector;
        return flowKt__DelayKt$sample$2;
    }

    public final Object invoke(Object obj, Object obj2, Object obj3) {
        return ((FlowKt__DelayKt$sample$2) create((CoroutineScope) obj, (FlowCollector) obj2, (Continuation) obj3)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(13:9|10|11|12|13|14|15|21|(1:23)|(1:25)|26|7|(1:27)(0)) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x00bb, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x00bc, code lost:
        r3 = r17;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00c3, code lost:
        r3.handleBuilderException(r0);
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00d5 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x006c  */
    public final Object invokeSuspend(Object obj) {
        FlowKt__DelayKt$sample$2 flowKt__DelayKt$sample$2;
        CoroutineScope coroutineScope;
        ObjectRef objectRef;
        ReceiveChannel receiveChannel;
        FlowCollector flowCollector;
        ReceiveChannel receiveChannel2;
        Object obj2;
        Continuation continuation;
        SelectBuilderImpl selectBuilderImpl;
        SelectBuilder selectBuilder;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        int i2 = 1;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope2 = this.p$;
            flowCollector = this.p$0;
            CoroutineScope coroutineScope3 = coroutineScope2;
            receiveChannel = ProduceKt.produce$default(coroutineScope3, null, -1, new FlowKt__DelayKt$sample$2$values$1(this, null), 1, null);
            objectRef = new ObjectRef();
            objectRef.element = null;
            flowKt__DelayKt$sample$2 = this;
            coroutineScope = coroutineScope2;
            receiveChannel2 = FlowKt__DelayKt.fixedPeriodTicker$default(coroutineScope3, this.$periodMillis, 0, 2, null);
            obj2 = coroutine_suspended;
        } else if (i == 1) {
            FlowKt__DelayKt$sample$2 flowKt__DelayKt$sample$22 = (FlowKt__DelayKt$sample$2) this.L$5;
            ReceiveChannel receiveChannel3 = (ReceiveChannel) this.L$4;
            ObjectRef objectRef2 = (ObjectRef) this.L$3;
            ReceiveChannel receiveChannel4 = (ReceiveChannel) this.L$2;
            FlowCollector flowCollector2 = (FlowCollector) this.L$1;
            CoroutineScope coroutineScope4 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            flowKt__DelayKt$sample$2 = this;
            receiveChannel2 = receiveChannel3;
            objectRef = objectRef2;
            receiveChannel = receiveChannel4;
            flowCollector = flowCollector2;
            coroutineScope = coroutineScope4;
            obj2 = coroutine_suspended;
            i2 = 1;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (objectRef.element != NullSurrogateKt.DONE) {
            flowKt__DelayKt$sample$2.L$0 = coroutineScope;
            flowKt__DelayKt$sample$2.L$1 = flowCollector;
            flowKt__DelayKt$sample$2.L$2 = receiveChannel;
            flowKt__DelayKt$sample$2.L$3 = objectRef;
            flowKt__DelayKt$sample$2.L$4 = receiveChannel2;
            flowKt__DelayKt$sample$2.L$5 = flowKt__DelayKt$sample$2;
            flowKt__DelayKt$sample$2.label = i2;
            Continuation continuation2 = flowKt__DelayKt$sample$2;
            SelectBuilderImpl selectBuilderImpl2 = new SelectBuilderImpl(continuation2);
            try {
                selectBuilder = selectBuilderImpl2;
            } catch (Throwable th) {
                th = th;
                continuation = continuation2;
                selectBuilderImpl = selectBuilderImpl2;
            }
            r4 = r4;
            ReceiveChannel receiveChannel5 = receiveChannel;
            SelectClause1 onReceiveOrNull = receiveChannel.getOnReceiveOrNull();
            SelectBuilderImpl selectBuilderImpl3 = selectBuilderImpl2;
            continuation = continuation2;
            FlowKt__DelayKt$sample$2$invokeSuspend$$inlined$select$lambda$1 flowKt__DelayKt$sample$2$invokeSuspend$$inlined$select$lambda$1 = new FlowKt__DelayKt$sample$2$invokeSuspend$$inlined$select$lambda$1(null, receiveChannel5, receiveChannel2, objectRef, flowCollector);
            selectBuilder.invoke(onReceiveOrNull, (Function2<? super Q, ? super Continuation<? super R>, ? extends Object>) flowKt__DelayKt$sample$2$invokeSuspend$$inlined$select$lambda$1);
            SelectClause1 onReceive = receiveChannel2.getOnReceive();
            FlowKt__DelayKt$sample$2$invokeSuspend$$inlined$select$lambda$2 flowKt__DelayKt$sample$2$invokeSuspend$$inlined$select$lambda$2 = new FlowKt__DelayKt$sample$2$invokeSuspend$$inlined$select$lambda$2(null, receiveChannel, receiveChannel2, objectRef, flowCollector);
            selectBuilder.invoke(onReceive, (Function2<? super Q, ? super Continuation<? super R>, ? extends Object>) flowKt__DelayKt$sample$2$invokeSuspend$$inlined$select$lambda$2);
            selectBuilderImpl = selectBuilderImpl3;
            Object result = selectBuilderImpl.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            if (result == obj2) {
                return obj2;
            }
            i2 = 1;
            if (objectRef.element != NullSurrogateKt.DONE) {
            }
        }
        return Unit.INSTANCE;
    }
}
