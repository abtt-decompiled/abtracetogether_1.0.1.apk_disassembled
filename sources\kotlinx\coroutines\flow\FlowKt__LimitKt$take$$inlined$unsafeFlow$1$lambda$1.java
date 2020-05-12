package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.internal.Ref.IntRef;
import kotlinx.coroutines.flow.internal.AbortFlowException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u0002\u0004\n\u0002\b\u0019¨\u0006\u0006¸\u0006\u0007"}, d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core", "kotlinx/coroutines/flow/FlowKt__LimitKt$$special$$inlined$collect$3"}, k = 1, mv = {1, 1, 15})
/* compiled from: Collect.kt */
public final class FlowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1 implements FlowCollector<T> {
    final /* synthetic */ IntRef $consumed$inlined;
    final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;
    final /* synthetic */ FlowKt__LimitKt$take$$inlined$unsafeFlow$1 this$0;

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    public Object emit(Object obj, Continuation continuation) {
        AnonymousClass1 r0;
        int i;
        FlowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1 flowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1;
        IntRef intRef;
        if (continuation instanceof AnonymousClass1) {
            r0 = (AnonymousClass1) continuation;
            if ((r0.label & Integer.MIN_VALUE) != 0) {
                r0.label -= Integer.MIN_VALUE;
                Object obj2 = r0.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = r0.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    Continuation continuation2 = r0;
                    FlowCollector flowCollector = this.$this_unsafeFlow$inlined;
                    r0.L$0 = this;
                    r0.L$1 = obj;
                    r0.L$2 = continuation2;
                    r0.L$3 = obj;
                    r0.label = 1;
                    if (flowCollector.emit(obj, r0) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    flowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1 = this;
                } else if (i == 1) {
                    Object obj3 = r0.L$3;
                    Continuation continuation3 = (Continuation) r0.L$2;
                    Object obj4 = r0.L$1;
                    flowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1 = (FlowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1) r0.L$0;
                    ResultKt.throwOnFailure(obj2);
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                intRef = flowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1.$consumed$inlined;
                intRef.element++;
                if (intRef.element == flowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1.this$0.$count$inlined) {
                    return Unit.INSTANCE;
                }
                throw new AbortFlowException();
            }
        }
        r0 = new ContinuationImpl(this, continuation) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            int label;
            /* synthetic */ Object result;
            final /* synthetic */ FlowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1 this$0;

            {
                this.this$0 = r1;
            }

            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return this.this$0.emit(null, this);
            }
        };
        Object obj22 = r0.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = r0.label;
        if (i != 0) {
        }
        intRef = flowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1.$consumed$inlined;
        intRef.element++;
        if (intRef.element == flowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1.this$0.$count$inlined) {
        }
    }

    public FlowKt__LimitKt$take$$inlined$unsafeFlow$1$lambda$1(FlowCollector flowCollector, IntRef intRef, FlowKt__LimitKt$take$$inlined$unsafeFlow$1 flowKt__LimitKt$take$$inlined$unsafeFlow$1) {
        this.$this_unsafeFlow$inlined = flowCollector;
        this.$consumed$inlined = intRef;
        this.this$0 = flowKt__LimitKt$take$$inlined$unsafeFlow$1;
    }
}
