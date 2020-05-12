package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;
import kotlinx.coroutines.flow.internal.NullSurrogateKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001f\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H@ø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007¸\u0006\b"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollectorKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core", "kotlinx/coroutines/flow/FlowKt__DistinctKt$distinctUntilChangedBy$$inlined$unsafeFlow$2"}, k = 1, mv = {1, 1, 15})
/* compiled from: SafeCollector.kt */
public final class FlowKt__DistinctKt$distinctUntilChangedBy$$inlined$distinctUntilChangedBy$FlowKt__DistinctKt$1 implements Flow<T> {
    final /* synthetic */ Function1 $keySelector$inlined;
    final /* synthetic */ Flow $this_distinctUntilChangedBy$inlined;

    public FlowKt__DistinctKt$distinctUntilChangedBy$$inlined$distinctUntilChangedBy$FlowKt__DistinctKt$1(Flow flow, Function1 function1) {
        this.$this_distinctUntilChangedBy$inlined = flow;
        this.$keySelector$inlined = function1;
    }

    public Object collect(final FlowCollector flowCollector, Continuation continuation) {
        final ObjectRef objectRef = new ObjectRef();
        objectRef.element = NullSurrogateKt.NULL;
        return this.$this_distinctUntilChangedBy$inlined.collect(new FlowCollector<T>() {
            /* JADX WARNING: Removed duplicated region for block: B:12:0x0040  */
            /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
            public Object emit(Object obj, Continuation continuation) {
                AnonymousClass1 r0;
                int i;
                if (continuation instanceof AnonymousClass1) {
                    r0 = (AnonymousClass1) continuation;
                    if ((r0.label & Integer.MIN_VALUE) != 0) {
                        r0.label -= Integer.MIN_VALUE;
                        Object obj2 = r0.result;
                        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        i = r0.label;
                        if (i != 0) {
                            ResultKt.throwOnFailure(obj2);
                            T invoke = this.$keySelector$inlined.invoke(obj);
                            if (objectRef.element == NullSurrogateKt.NULL || !Boxing.boxBoolean(Intrinsics.areEqual((Object) objectRef.element, (Object) invoke)).booleanValue()) {
                                objectRef.element = invoke;
                                FlowCollector flowCollector = flowCollector;
                                r0.L$0 = this;
                                r0.L$1 = obj;
                                r0.L$2 = r0;
                                r0.L$3 = obj;
                                r0.L$4 = invoke;
                                r0.label = 1;
                                obj2 = flowCollector.emit(obj, r0);
                                if (obj2 == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                            }
                            return Unit.INSTANCE;
                        } else if (i == 1) {
                            Object obj3 = r0.L$4;
                            Object obj4 = r0.L$3;
                            AnonymousClass1 r6 = (AnonymousClass1) r0.L$2;
                            Object obj5 = r0.L$1;
                            AnonymousClass2 r62 = (AnonymousClass2) r0.L$0;
                            ResultKt.throwOnFailure(obj2);
                        } else {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        Unit unit = (Unit) obj2;
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
                    final /* synthetic */ AnonymousClass2 this$0;

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
                Unit unit2 = (Unit) obj22;
                return Unit.INSTANCE;
            }
        }, continuation);
    }
}
