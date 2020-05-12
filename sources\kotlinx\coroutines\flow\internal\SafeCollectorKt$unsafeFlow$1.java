package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001f\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H@ø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollectorKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* compiled from: SafeCollector.kt */
public final class SafeCollectorKt$unsafeFlow$1 implements Flow<T> {
    final /* synthetic */ Function2 $block;

    public SafeCollectorKt$unsafeFlow$1(Function2 function2) {
        this.$block = function2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    public Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        SafeCollectorKt$unsafeFlow$1$collect$1 safeCollectorKt$unsafeFlow$1$collect$1;
        int i;
        if (continuation instanceof SafeCollectorKt$unsafeFlow$1$collect$1) {
            safeCollectorKt$unsafeFlow$1$collect$1 = (SafeCollectorKt$unsafeFlow$1$collect$1) continuation;
            if ((safeCollectorKt$unsafeFlow$1$collect$1.label & Integer.MIN_VALUE) != 0) {
                safeCollectorKt$unsafeFlow$1$collect$1.label -= Integer.MIN_VALUE;
                Object obj = safeCollectorKt$unsafeFlow$1$collect$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = safeCollectorKt$unsafeFlow$1$collect$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Function2 function2 = this.$block;
                    safeCollectorKt$unsafeFlow$1$collect$1.L$0 = this;
                    safeCollectorKt$unsafeFlow$1$collect$1.L$1 = flowCollector;
                    safeCollectorKt$unsafeFlow$1$collect$1.label = 1;
                    if (function2.invoke(flowCollector, safeCollectorKt$unsafeFlow$1$collect$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else if (i == 1) {
                    FlowCollector flowCollector2 = (FlowCollector) safeCollectorKt$unsafeFlow$1$collect$1.L$1;
                    SafeCollectorKt$unsafeFlow$1 safeCollectorKt$unsafeFlow$1 = (SafeCollectorKt$unsafeFlow$1) safeCollectorKt$unsafeFlow$1$collect$1.L$0;
                    ResultKt.throwOnFailure(obj);
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return Unit.INSTANCE;
            }
        }
        safeCollectorKt$unsafeFlow$1$collect$1 = new SafeCollectorKt$unsafeFlow$1$collect$1(this, continuation);
        Object obj2 = safeCollectorKt$unsafeFlow$1$collect$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = safeCollectorKt$unsafeFlow$1$collect$1.label;
        if (i != 0) {
        }
        return Unit.INSTANCE;
    }

    public Object collect$$forInline(FlowCollector flowCollector, Continuation continuation) {
        InlineMarker.mark(4);
        new SafeCollectorKt$unsafeFlow$1$collect$1(this, continuation);
        InlineMarker.mark(5);
        this.$block.invoke(flowCollector, continuation);
        return Unit.INSTANCE;
    }
}
