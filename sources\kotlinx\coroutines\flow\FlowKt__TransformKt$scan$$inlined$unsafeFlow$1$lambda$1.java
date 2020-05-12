package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Ref.ObjectRef;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u0002\u0004\n\u0002\b\u0019¨\u0006\u0006¸\u0006\u0007"}, d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core", "kotlinx/coroutines/flow/FlowKt__TransformKt$$special$$inlined$collect$9"}, k = 1, mv = {1, 1, 15})
/* compiled from: Collect.kt */
public final class FlowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1 implements FlowCollector<T> {
    final /* synthetic */ ObjectRef $accumulator$inlined;
    final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;
    final /* synthetic */ FlowKt__TransformKt$scan$$inlined$unsafeFlow$1 this$0;

    public FlowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1(FlowCollector flowCollector, ObjectRef objectRef, FlowKt__TransformKt$scan$$inlined$unsafeFlow$1 flowKt__TransformKt$scan$$inlined$unsafeFlow$1) {
        this.$this_unsafeFlow$inlined = flowCollector;
        this.$accumulator$inlined = objectRef;
        this.this$0 = flowKt__TransformKt$scan$$inlined$unsafeFlow$1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0097 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    public Object emit(Object obj, Continuation continuation) {
        AnonymousClass1 r0;
        T coroutine_suspended;
        int i;
        ObjectRef objectRef;
        FlowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1 flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1;
        Object obj2;
        Continuation continuation2;
        Object obj3;
        FlowCollector flowCollector;
        T t;
        if (continuation instanceof AnonymousClass1) {
            r0 = (AnonymousClass1) continuation;
            if ((r0.label & Integer.MIN_VALUE) != 0) {
                r0.label -= Integer.MIN_VALUE;
                T t2 = r0.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = r0.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(t2);
                    Continuation continuation3 = r0;
                    ObjectRef objectRef2 = this.$accumulator$inlined;
                    Function3 function3 = this.this$0.$operation$inlined;
                    T t3 = this.$accumulator$inlined.element;
                    r0.L$0 = this;
                    r0.L$1 = obj;
                    r0.L$2 = continuation3;
                    r0.L$3 = obj;
                    r0.L$4 = objectRef2;
                    r0.label = 1;
                    T invoke = function3.invoke(t3, obj, r0);
                    if (invoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1 = this;
                    obj2 = obj;
                    objectRef = objectRef2;
                    obj3 = obj2;
                    T t4 = invoke;
                    continuation2 = continuation3;
                    t2 = t4;
                } else if (i == 1) {
                    objectRef = (ObjectRef) r0.L$4;
                    obj3 = r0.L$3;
                    continuation2 = (Continuation) r0.L$2;
                    obj2 = r0.L$1;
                    flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1 = (FlowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1) r0.L$0;
                    ResultKt.throwOnFailure(t2);
                } else if (i == 2) {
                    Object obj4 = r0.L$3;
                    Continuation continuation4 = (Continuation) r0.L$2;
                    Object obj5 = r0.L$1;
                    FlowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1 flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$12 = (FlowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1) r0.L$0;
                    ResultKt.throwOnFailure(t2);
                    return Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                objectRef.element = t2;
                flowCollector = flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1.$this_unsafeFlow$inlined;
                t = flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1.$accumulator$inlined.element;
                r0.L$0 = flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1;
                r0.L$1 = obj2;
                r0.L$2 = continuation2;
                r0.L$3 = obj3;
                r0.label = 2;
                if (flowCollector.emit(t, r0) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            }
        }
        r0 = new ContinuationImpl(this, continuation) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            int label;
            /* synthetic */ Object result;
            final /* synthetic */ FlowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1 this$0;

            {
                this.this$0 = r1;
            }

            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return this.this$0.emit(null, this);
            }
        };
        T t22 = r0.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = r0.label;
        if (i != 0) {
        }
        objectRef.element = t22;
        flowCollector = flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1.$this_unsafeFlow$inlined;
        t = flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1.$accumulator$inlined.element;
        r0.L$0 = flowKt__TransformKt$scan$$inlined$unsafeFlow$1$lambda$1;
        r0.L$1 = obj2;
        r0.L$2 = continuation2;
        r0.L$3 = obj3;
        r0.label = 2;
        if (flowCollector.emit(t, r0) == coroutine_suspended) {
        }
        return Unit.INSTANCE;
    }
}
