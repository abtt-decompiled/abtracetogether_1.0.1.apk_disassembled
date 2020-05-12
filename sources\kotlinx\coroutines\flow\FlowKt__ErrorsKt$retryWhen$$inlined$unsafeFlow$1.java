package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function4;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001f\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H@ø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007¸\u0006\u0000"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollectorKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* compiled from: SafeCollector.kt */
public final class FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 implements Flow<T> {
    final /* synthetic */ Function4 $predicate$inlined;
    final /* synthetic */ Flow $this_retryWhen$inlined;

    public FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1(Flow flow, Function4 function4) {
        this.$this_retryWhen$inlined = flow;
        this.$predicate$inlined = function4;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00af  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ec A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002c  */
    public Object collect(FlowCollector flowCollector, Continuation continuation) {
        FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1;
        AnonymousClass1 r1;
        int i;
        FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
        FlowCollector flowCollector2;
        Continuation continuation2;
        FlowCollector flowCollector3;
        long j;
        int i2;
        FlowCollector flowCollector4;
        AnonymousClass1 r3;
        FlowCollector flowCollector5;
        Object obj;
        Throwable th;
        Throwable th2;
        Object catchImpl;
        Continuation continuation3 = continuation;
        if (continuation3 instanceof AnonymousClass1) {
            r1 = (AnonymousClass1) continuation3;
            if ((r1.label & Integer.MIN_VALUE) != 0) {
                r1.label -= Integer.MIN_VALUE;
                flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 = this;
                Object obj2 = r1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = r1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    j = 0;
                    continuation2 = r1;
                    flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1;
                    obj = coroutine_suspended;
                    flowCollector4 = flowCollector;
                    r3 = r1;
                    flowCollector5 = flowCollector4;
                } else if (i == 1) {
                    i2 = r1.I$0;
                    j = r1.J$0;
                    flowCollector3 = (FlowCollector) r1.L$3;
                    continuation2 = (Continuation) r1.L$2;
                    flowCollector2 = (FlowCollector) r1.L$1;
                    flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12 = (FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1) r1.L$0;
                    ResultKt.throwOnFailure(obj2);
                    th2 = (Throwable) obj2;
                    if (th2 != null) {
                        Function4 function4 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12.$predicate$inlined;
                        Long boxLong = Boxing.boxLong(j);
                        r1.L$0 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
                        r1.L$1 = flowCollector2;
                        r1.L$2 = continuation2;
                        r1.L$3 = flowCollector3;
                        r1.J$0 = j;
                        r1.L$4 = th2;
                        r1.label = 3;
                        r1.L$0 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
                        r1.L$1 = flowCollector2;
                        r1.L$2 = continuation2;
                        r1.L$3 = flowCollector3;
                        r1.J$0 = j;
                        r1.L$4 = th2;
                        r1.label = 2;
                        Object invoke = function4.invoke(flowCollector3, th2, boxLong, r1);
                        if (invoke == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        Object obj3 = invoke;
                        th = th2;
                        obj2 = obj3;
                        if (obj2 == coroutine_suspended) {
                        }
                        if (((Boolean) obj2).booleanValue()) {
                        }
                        throw th;
                        return coroutine_suspended;
                    }
                    flowCollector4 = flowCollector3;
                    long j2 = j;
                    Object obj4 = coroutine_suspended;
                    r3 = r1;
                    flowCollector5 = flowCollector2;
                    if (i2 != 0) {
                    }
                    return Unit.INSTANCE;
                } else if (i == 2) {
                    th = (Throwable) r1.L$4;
                    j = r1.J$0;
                    flowCollector3 = (FlowCollector) r1.L$3;
                    continuation2 = (Continuation) r1.L$2;
                    flowCollector2 = (FlowCollector) r1.L$1;
                    flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12 = (FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1) r1.L$0;
                    ResultKt.throwOnFailure(obj2);
                    if (obj2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    if (((Boolean) obj2).booleanValue()) {
                        throw th;
                    }
                    throw th;
                } else if (i == 3) {
                    th = (Throwable) r1.L$4;
                    j = r1.J$0;
                    flowCollector3 = (FlowCollector) r1.L$3;
                    continuation2 = (Continuation) r1.L$2;
                    flowCollector2 = (FlowCollector) r1.L$1;
                    flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12 = (FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1) r1.L$0;
                    ResultKt.throwOnFailure(obj2);
                    if (((Boolean) obj2).booleanValue()) {
                        j++;
                        i2 = 1;
                        flowCollector4 = flowCollector3;
                        long j22 = j;
                        Object obj42 = coroutine_suspended;
                        r3 = r1;
                        flowCollector5 = flowCollector2;
                        if (i2 != 0) {
                            obj = obj42;
                            j = j22;
                        }
                        return Unit.INSTANCE;
                    }
                    throw th;
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                Flow flow = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12.$this_retryWhen$inlined;
                r3.L$0 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
                r3.L$1 = flowCollector5;
                r3.L$2 = continuation2;
                r3.L$3 = flowCollector4;
                r3.J$0 = j;
                r3.I$0 = 0;
                r3.label = 1;
                catchImpl = FlowKt.catchImpl(flow, flowCollector4, r3);
                if (catchImpl != obj) {
                    return obj;
                }
                flowCollector3 = flowCollector4;
                obj2 = catchImpl;
                flowCollector2 = flowCollector5;
                r1 = r3;
                coroutine_suspended = obj;
                i2 = 0;
                th2 = (Throwable) obj2;
                if (th2 != null) {
                }
                flowCollector4 = flowCollector3;
                long j222 = j;
                Object obj422 = coroutine_suspended;
                r3 = r1;
                flowCollector5 = flowCollector2;
                if (i2 != 0) {
                }
                return Unit.INSTANCE;
                return obj;
            }
        }
        flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 = this;
        r1 = new ContinuationImpl(flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1, continuation3) {
            int I$0;
            long J$0;
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            int label;
            /* synthetic */ Object result;
            final /* synthetic */ FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 this$0;

            {
                this.this$0 = r1;
            }

            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return this.this$0.collect(null, this);
            }
        };
        Object obj22 = r1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = r1.label;
        if (i != 0) {
        }
        Flow flow2 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12.$this_retryWhen$inlined;
        r3.L$0 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
        r3.L$1 = flowCollector5;
        r3.L$2 = continuation2;
        r3.L$3 = flowCollector4;
        r3.J$0 = j;
        r3.I$0 = 0;
        r3.label = 1;
        catchImpl = FlowKt.catchImpl(flow2, flowCollector4, r3);
        if (catchImpl != obj) {
        }
        return obj;
    }
}
