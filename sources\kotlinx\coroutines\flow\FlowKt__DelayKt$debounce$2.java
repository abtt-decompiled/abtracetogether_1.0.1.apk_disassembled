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
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$debounce$2", f = "Delay.kt", i = {0, 0, 0, 0}, l = {137}, m = "invokeSuspend", n = {"$this$scopedFlow", "downstream", "values", "lastValue"}, s = {"L$0", "L$1", "L$2", "L$3"})
/* compiled from: Delay.kt */
final class FlowKt__DelayKt$debounce$2 extends SuspendLambda implements Function3<CoroutineScope, FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Flow $this_debounce;
    final /* synthetic */ long $timeoutMillis;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    private CoroutineScope p$;
    private FlowCollector p$0;

    FlowKt__DelayKt$debounce$2(Flow flow, long j, Continuation continuation) {
        this.$this_debounce = flow;
        this.$timeoutMillis = j;
        super(3, continuation);
    }

    public final Continuation<Unit> create(CoroutineScope coroutineScope, FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineScope, "$this$create");
        Intrinsics.checkParameterIsNotNull(flowCollector, "downstream");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        FlowKt__DelayKt$debounce$2 flowKt__DelayKt$debounce$2 = new FlowKt__DelayKt$debounce$2(this.$this_debounce, this.$timeoutMillis, continuation);
        flowKt__DelayKt$debounce$2.p$ = coroutineScope;
        flowKt__DelayKt$debounce$2.p$0 = flowCollector;
        return flowKt__DelayKt$debounce$2;
    }

    public final Object invoke(Object obj, Object obj2, Object obj3) {
        return ((FlowKt__DelayKt$debounce$2) create((CoroutineScope) obj, (FlowCollector) obj2, (Continuation) obj3)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:9|(2:10|11)|12|13|14|(4:16|17|18|19)(1:21)|27|(1:29)|(1:31)(4:32|33|7|(1:34)(0))|31) */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:16|17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00b0, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00b7, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00bb, code lost:
        r16 = r10;
        r19 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00bf, code lost:
        r1.handleBuilderException(r0);
     */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0060  */
    public final Object invokeSuspend(Object obj) {
        CoroutineScope coroutineScope;
        FlowCollector flowCollector;
        ReceiveChannel receiveChannel;
        ObjectRef objectRef;
        FlowKt__DelayKt$debounce$2 flowKt__DelayKt$debounce$2;
        Object obj2;
        FlowKt__DelayKt$debounce$2 flowKt__DelayKt$debounce$22;
        Continuation continuation;
        SelectBuilderImpl selectBuilderImpl;
        SelectBuilder selectBuilder;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        int i2 = 1;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope2 = this.p$;
            FlowCollector flowCollector2 = this.p$0;
            ReceiveChannel produce$default = ProduceKt.produce$default(coroutineScope2, null, -1, new FlowKt__DelayKt$debounce$2$values$1(this, null), 1, null);
            ObjectRef objectRef2 = new ObjectRef();
            objectRef2.element = null;
            flowKt__DelayKt$debounce$2 = this;
            coroutineScope = coroutineScope2;
            receiveChannel = produce$default;
            objectRef = objectRef2;
            flowCollector = flowCollector2;
            obj2 = coroutine_suspended;
        } else if (i == 1) {
            FlowKt__DelayKt$debounce$2 flowKt__DelayKt$debounce$23 = (FlowKt__DelayKt$debounce$2) this.L$4;
            ObjectRef objectRef3 = (ObjectRef) this.L$3;
            ReceiveChannel receiveChannel2 = (ReceiveChannel) this.L$2;
            FlowCollector flowCollector3 = (FlowCollector) this.L$1;
            CoroutineScope coroutineScope3 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            flowKt__DelayKt$debounce$2 = this;
            objectRef = objectRef3;
            receiveChannel = receiveChannel2;
            flowCollector = flowCollector3;
            coroutineScope = coroutineScope3;
            obj2 = coroutine_suspended;
            i2 = 1;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (objectRef.element == NullSurrogateKt.DONE) {
            flowKt__DelayKt$debounce$2.L$0 = coroutineScope;
            flowKt__DelayKt$debounce$2.L$1 = flowCollector;
            flowKt__DelayKt$debounce$2.L$2 = receiveChannel;
            flowKt__DelayKt$debounce$2.L$3 = objectRef;
            flowKt__DelayKt$debounce$2.L$4 = flowKt__DelayKt$debounce$2;
            flowKt__DelayKt$debounce$2.label = i2;
            Continuation continuation2 = flowKt__DelayKt$debounce$2;
            SelectBuilderImpl selectBuilderImpl2 = new SelectBuilderImpl(continuation2);
            try {
                selectBuilder = selectBuilderImpl2;
            } catch (Throwable th) {
                th = th;
                selectBuilderImpl = selectBuilderImpl2;
            }
            r4 = r4;
            FlowKt__DelayKt$debounce$2 flowKt__DelayKt$debounce$24 = flowKt__DelayKt$debounce$2;
            ReceiveChannel receiveChannel3 = receiveChannel;
            SelectClause1 onReceiveOrNull = receiveChannel.getOnReceiveOrNull();
            selectBuilderImpl = selectBuilderImpl2;
            FlowKt__DelayKt$debounce$2$invokeSuspend$$inlined$select$lambda$1 flowKt__DelayKt$debounce$2$invokeSuspend$$inlined$select$lambda$1 = new FlowKt__DelayKt$debounce$2$invokeSuspend$$inlined$select$lambda$1(null, flowKt__DelayKt$debounce$24, receiveChannel3, objectRef, flowCollector);
            selectBuilder.invoke(onReceiveOrNull, (Function2<? super Q, ? super Continuation<? super R>, ? extends Object>) flowKt__DelayKt$debounce$2$invokeSuspend$$inlined$select$lambda$1);
            T t = objectRef.element;
            if (t != null) {
                r4 = r4;
                SelectBuilder selectBuilder2 = selectBuilder;
                long j = flowKt__DelayKt$debounce$2.$timeoutMillis;
                continuation = continuation2;
                flowKt__DelayKt$debounce$22 = flowKt__DelayKt$debounce$2;
                FlowKt__DelayKt$debounce$2$invokeSuspend$$inlined$select$lambda$2 flowKt__DelayKt$debounce$2$invokeSuspend$$inlined$select$lambda$2 = new FlowKt__DelayKt$debounce$2$invokeSuspend$$inlined$select$lambda$2(t, null, selectBuilder2, flowKt__DelayKt$debounce$2, receiveChannel, objectRef, flowCollector);
                selectBuilder.onTimeout(j, flowKt__DelayKt$debounce$2$invokeSuspend$$inlined$select$lambda$2);
            } else {
                continuation = continuation2;
                flowKt__DelayKt$debounce$22 = flowKt__DelayKt$debounce$2;
            }
            Object result = selectBuilderImpl.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            if (result != obj2) {
                return obj2;
            }
            flowKt__DelayKt$debounce$2 = flowKt__DelayKt$debounce$22;
            i2 = 1;
            if (objectRef.element == NullSurrogateKt.DONE) {
            }
            return obj2;
        }
        return Unit.INSTANCE;
    }
}
