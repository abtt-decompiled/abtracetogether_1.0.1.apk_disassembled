package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.Result.Companion;
import kotlin.ResultKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "e", "invoke"}, k = 3, mv = {1, 1, 15})
/* compiled from: ExceptionsConstuctor.kt */
public final class ExceptionsConstuctorKt$safeCtor$1 extends Lambda implements Function1<Throwable, Throwable> {
    final /* synthetic */ Function1 $block;

    public ExceptionsConstuctorKt$safeCtor$1(Function1 function1) {
        this.$block = function1;
        super(1);
    }

    public final Throwable invoke(Throwable th) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(th, "e");
        try {
            Companion companion = Result.Companion;
            obj = Result.m3constructorimpl((Throwable) this.$block.invoke(th));
        } catch (Throwable th2) {
            Companion companion2 = Result.Companion;
            obj = Result.m3constructorimpl(ResultKt.createFailure(th2));
        }
        if (Result.m9isFailureimpl(obj)) {
            obj = null;
        }
        return (Throwable) obj;
    }
}
