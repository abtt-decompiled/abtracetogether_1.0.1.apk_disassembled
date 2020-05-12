package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function3;
import kotlinx.coroutines.flow.internal.SafeCollector;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001f\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H@ø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007¸\u0006\u0000"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollectorKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* compiled from: SafeCollector.kt */
public final class FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 implements Flow<T> {
    final /* synthetic */ Function3 $action$inlined;
    final /* synthetic */ Flow $this_onCompletion$inlined;

    public FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1(Flow flow, Function3 function3) {
        this.$this_onCompletion$inlined = flow;
        this.$action$inlined = function3;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00cf A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d6  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0105 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0109  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x010a  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    public Object collect(FlowCollector flowCollector, Continuation continuation) {
        AnonymousClass1 r0;
        Object coroutine_suspended;
        int i;
        Throwable th;
        Throwable th2;
        Throwable th3;
        FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1;
        FlowCollector flowCollector2;
        Continuation continuation2;
        FlowCollector flowCollector3;
        FlowCollector safeCollector;
        Function3 function3;
        Throwable th4;
        Throwable th5;
        Throwable th6;
        FlowCollector safeCollector2;
        Function3 function32;
        if (continuation instanceof AnonymousClass1) {
            r0 = (AnonymousClass1) continuation;
            if ((r0.label & Integer.MIN_VALUE) != 0) {
                r0.label -= Integer.MIN_VALUE;
                Object obj = r0.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = r0.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Continuation continuation3 = r0;
                    th5 = null;
                    try {
                        Flow flow = this.$this_onCompletion$inlined;
                        r0.L$0 = this;
                        r0.L$1 = flowCollector;
                        r0.L$2 = continuation3;
                        r0.L$3 = flowCollector;
                        r0.L$4 = th5;
                        r0.label = 1;
                        Object catchImpl = FlowKt.catchImpl(flow, flowCollector, r0);
                        if (catchImpl == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 = this;
                        flowCollector2 = flowCollector;
                        Object obj2 = catchImpl;
                        continuation2 = continuation3;
                        obj = obj2;
                    } catch (Throwable th7) {
                        flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 = this;
                        flowCollector2 = flowCollector;
                        continuation2 = continuation3;
                        th3 = th5;
                        flowCollector3 = flowCollector2;
                        th = th7;
                        safeCollector = new SafeCollector(flowCollector3, r0.getContext());
                        function3 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$action$inlined;
                        r0.L$0 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1;
                        r0.L$1 = flowCollector2;
                        r0.L$2 = continuation2;
                        r0.L$3 = flowCollector3;
                        r0.L$4 = th3;
                        r0.L$5 = th;
                        r0.label = 3;
                        if (FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(safeCollector, function3, th3, r0) == coroutine_suspended) {
                        }
                    }
                } else if (i == 1) {
                    Throwable th8 = (Throwable) r0.L$4;
                    flowCollector3 = (FlowCollector) r0.L$3;
                    continuation2 = (Continuation) r0.L$2;
                    flowCollector2 = (FlowCollector) r0.L$1;
                    flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 = (FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1) r0.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        FlowCollector flowCollector4 = flowCollector3;
                        th5 = th8;
                        flowCollector = flowCollector4;
                    } catch (Throwable th9) {
                        Throwable th10 = th9;
                        th3 = th8;
                        th = th10;
                        safeCollector = new SafeCollector(flowCollector3, r0.getContext());
                        function3 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$action$inlined;
                        r0.L$0 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1;
                        r0.L$1 = flowCollector2;
                        r0.L$2 = continuation2;
                        r0.L$3 = flowCollector3;
                        r0.L$4 = th3;
                        r0.L$5 = th;
                        r0.label = 3;
                        if (FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(safeCollector, function3, th3, r0) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        th2 = th3;
                        if (th2 == null) {
                        }
                    }
                } else if (i == 2) {
                    th4 = (Throwable) r0.L$4;
                    FlowCollector flowCollector5 = (FlowCollector) r0.L$3;
                    Continuation continuation4 = (Continuation) r0.L$2;
                    FlowCollector flowCollector6 = (FlowCollector) r0.L$1;
                    FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$12 = (FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1) r0.L$0;
                    ResultKt.throwOnFailure(obj);
                    if (th4 != null) {
                        return Unit.INSTANCE;
                    }
                    throw th4;
                } else if (i == 3) {
                    th = (Throwable) r0.L$5;
                    th2 = (Throwable) r0.L$4;
                    FlowCollector flowCollector7 = (FlowCollector) r0.L$3;
                    Continuation continuation5 = (Continuation) r0.L$2;
                    FlowCollector flowCollector8 = (FlowCollector) r0.L$1;
                    FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$13 = (FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1) r0.L$0;
                    ResultKt.throwOnFailure(obj);
                    if (th2 == null) {
                        throw th2;
                    }
                    throw th;
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                th6 = (Throwable) obj;
                safeCollector2 = new SafeCollector(flowCollector, r0.getContext());
                function32 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$action$inlined;
                r0.L$0 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1;
                r0.L$1 = flowCollector2;
                r0.L$2 = continuation2;
                r0.L$3 = flowCollector;
                r0.L$4 = th6;
                r0.label = 2;
                if (FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(safeCollector2, function32, th6, r0) != coroutine_suspended) {
                    return coroutine_suspended;
                }
                th4 = th6;
                if (th4 != null) {
                }
            }
        }
        r0 = new ContinuationImpl(this, continuation) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
            int label;
            /* synthetic */ Object result;
            final /* synthetic */ FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 this$0;

            {
                this.this$0 = r1;
            }

            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return this.this$0.collect(null, this);
            }
        };
        Object obj3 = r0.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = r0.label;
        if (i != 0) {
        }
        try {
            th6 = (Throwable) obj3;
            safeCollector2 = new SafeCollector(flowCollector, r0.getContext());
            function32 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$action$inlined;
            r0.L$0 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1;
            r0.L$1 = flowCollector2;
            r0.L$2 = continuation2;
            r0.L$3 = flowCollector;
            r0.L$4 = th6;
            r0.label = 2;
            if (FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(safeCollector2, function32, th6, r0) != coroutine_suspended) {
            }
        } catch (Throwable th11) {
            Throwable th12 = th5;
            flowCollector3 = flowCollector;
            th = th11;
            th3 = th12;
            safeCollector = new SafeCollector(flowCollector3, r0.getContext());
            function3 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$action$inlined;
            r0.L$0 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1;
            r0.L$1 = flowCollector2;
            r0.L$2 = continuation2;
            r0.L$3 = flowCollector3;
            r0.L$4 = th3;
            r0.L$5 = th;
            r0.label = 3;
            if (FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(safeCollector, function3, th3, r0) == coroutine_suspended) {
            }
        }
    }
}
