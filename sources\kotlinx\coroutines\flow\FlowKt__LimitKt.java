package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a&\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001aJ\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\"\u0010\u0006\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0007H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u001a&\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001aJ\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\"\u0010\u0006\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0007H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u0002\u0004\n\u0002\b\u0019¨\u0006\u000e"}, d2 = {"drop", "Lkotlinx/coroutines/flow/Flow;", "T", "count", "", "dropWhile", "predicate", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "take", "takeWhile", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* compiled from: Limit.kt */
final /* synthetic */ class FlowKt__LimitKt {
    public static final <T> Flow<T> drop(Flow<? extends T> flow, int i) {
        Intrinsics.checkParameterIsNotNull(flow, "$this$drop");
        if (i >= 0) {
            return new FlowKt__LimitKt$drop$$inlined$unsafeFlow$1<>(flow, i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Drop count should be non-negative, but had ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString().toString());
    }

    public static final <T> Flow<T> dropWhile(Flow<? extends T> flow, Function2<? super T, ? super Continuation<? super Boolean>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(flow, "$this$dropWhile");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        return new FlowKt__LimitKt$dropWhile$$inlined$unsafeFlow$1<>(flow, function2);
    }

    public static final <T> Flow<T> take(Flow<? extends T> flow, int i) {
        Intrinsics.checkParameterIsNotNull(flow, "$this$take");
        if (i > 0) {
            return new FlowKt__LimitKt$take$$inlined$unsafeFlow$1<>(flow, i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Requested element count ");
        sb.append(i);
        sb.append(" should be positive");
        throw new IllegalArgumentException(sb.toString().toString());
    }

    public static final <T> Flow<T> takeWhile(Flow<? extends T> flow, Function2<? super T, ? super Continuation<? super Boolean>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(flow, "$this$takeWhile");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        return new FlowKt__LimitKt$takeWhile$$inlined$unsafeFlow$1<>(flow, function2);
    }
}
