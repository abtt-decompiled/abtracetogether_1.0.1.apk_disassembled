package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref.ObjectRef;
import kotlinx.coroutines.flow.internal.AbortFlowException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u0002\u0004\n\u0002\b\u0019¨\u0006\u0006¸\u0006\u0000"}, d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* compiled from: Collect.kt */
public final class FlowKt__ReduceKt$first$$inlined$collect$2 implements FlowCollector<T> {
    final /* synthetic */ Function2 $predicate$inlined;
    final /* synthetic */ ObjectRef $result$inlined;

    public FlowKt__ReduceKt$first$$inlined$collect$2(Function2 function2, ObjectRef objectRef) {
        this.$predicate$inlined = function2;
        this.$result$inlined = objectRef;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Object, code=T, for r6v0, types: [java.lang.Object] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0077 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    public Object emit(T t, Continuation continuation) {
        AnonymousClass1 r0;
        Object obj;
        Object coroutine_suspended;
        int i;
        FlowKt__ReduceKt$first$$inlined$collect$2 flowKt__ReduceKt$first$$inlined$collect$2;
        if (continuation instanceof AnonymousClass1) {
            r0 = (AnonymousClass1) continuation;
            if ((r0.label & Integer.MIN_VALUE) != 0) {
                r0.label -= Integer.MIN_VALUE;
                obj = r0.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = r0.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Continuation continuation2 = r0;
                    Function2 function2 = this.$predicate$inlined;
                    r0.L$0 = this;
                    r0.L$1 = t;
                    r0.L$2 = continuation2;
                    r0.L$3 = t;
                    r0.label = 2;
                    r0.L$0 = this;
                    r0.L$1 = t;
                    r0.L$2 = continuation2;
                    r0.L$3 = t;
                    r0.label = 1;
                    obj = function2.invoke(t, r0);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    flowKt__ReduceKt$first$$inlined$collect$2 = this;
                } else if (i == 1) {
                    t = r0.L$3;
                    Continuation continuation3 = (Continuation) r0.L$2;
                    Object obj2 = r0.L$1;
                    flowKt__ReduceKt$first$$inlined$collect$2 = (FlowKt__ReduceKt$first$$inlined$collect$2) r0.L$0;
                    ResultKt.throwOnFailure(obj);
                } else if (i == 2) {
                    t = r0.L$3;
                    Continuation continuation4 = (Continuation) r0.L$2;
                    Object obj3 = r0.L$1;
                    flowKt__ReduceKt$first$$inlined$collect$2 = (FlowKt__ReduceKt$first$$inlined$collect$2) r0.L$0;
                    ResultKt.throwOnFailure(obj);
                    if (!((Boolean) obj).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    flowKt__ReduceKt$first$$inlined$collect$2.$result$inlined.element = t;
                    throw new AbortFlowException();
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (!((Boolean) obj).booleanValue()) {
                }
            }
        }
        r0 = new ContinuationImpl(this, continuation) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            int label;
            /* synthetic */ Object result;
            final /* synthetic */ FlowKt__ReduceKt$first$$inlined$collect$2 this$0;

            {
                this.this$0 = r1;
            }

            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return this.this$0.emit(null, this);
            }
        };
        obj = r0.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = r0.label;
        if (i != 0) {
        }
        if (obj == coroutine_suspended) {
        }
        if (!((Boolean) obj).booleanValue()) {
        }
    }
}
