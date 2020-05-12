package kotlin.coroutines;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.coroutines.CoroutineContext.Element;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bg\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fJ(\u0010\u0002\u001a\u0004\u0018\u0001H\u0003\"\b\b\u0000\u0010\u0003*\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0005H\u0002¢\u0006\u0002\u0010\u0006J\"\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\bH&J\u0014\u0010\u000b\u001a\u00020\f2\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0014\u0010\r\u001a\u00020\u000e2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\bH\u0016¨\u0006\u0010"}, d2 = {"Lkotlin/coroutines/ContinuationInterceptor;", "Lkotlin/coroutines/CoroutineContext$Element;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "interceptContinuation", "Lkotlin/coroutines/Continuation;", "T", "continuation", "minusKey", "Lkotlin/coroutines/CoroutineContext;", "releaseInterceptedContinuation", "", "Key", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: ContinuationInterceptor.kt */
public interface ContinuationInterceptor extends Element {
    public static final Key Key = Key.$$INSTANCE;

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 15})
    /* compiled from: ContinuationInterceptor.kt */
    public static final class DefaultImpls {
        public static <R> R fold(ContinuationInterceptor continuationInterceptor, R r, Function2<? super R, ? super Element, ? extends R> function2) {
            Intrinsics.checkParameterIsNotNull(function2, "operation");
            return kotlin.coroutines.CoroutineContext.Element.DefaultImpls.fold(continuationInterceptor, r, function2);
        }

        public static CoroutineContext plus(ContinuationInterceptor continuationInterceptor, CoroutineContext coroutineContext) {
            Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
            return kotlin.coroutines.CoroutineContext.Element.DefaultImpls.plus(continuationInterceptor, coroutineContext);
        }

        public static void releaseInterceptedContinuation(ContinuationInterceptor continuationInterceptor, Continuation<?> continuation) {
            Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        }

        public static <E extends Element> E get(ContinuationInterceptor continuationInterceptor, kotlin.coroutines.CoroutineContext.Key<E> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            if (key != ContinuationInterceptor.Key) {
                return null;
            }
            if (continuationInterceptor != null) {
                return continuationInterceptor;
            }
            throw new TypeCastException("null cannot be cast to non-null type E");
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=kotlin.coroutines.ContinuationInterceptor, code=kotlin.coroutines.CoroutineContext, for r1v0, types: [kotlin.coroutines.ContinuationInterceptor, kotlin.coroutines.CoroutineContext] */
        public static CoroutineContext minusKey(CoroutineContext coroutineContext, kotlin.coroutines.CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            if (key == ContinuationInterceptor.Key) {
                coroutineContext = EmptyCoroutineContext.INSTANCE;
            }
            return coroutineContext;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lkotlin/coroutines/ContinuationInterceptor$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlin/coroutines/ContinuationInterceptor;", "()V", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    /* compiled from: ContinuationInterceptor.kt */
    public static final class Key implements kotlin.coroutines.CoroutineContext.Key<ContinuationInterceptor> {
        static final /* synthetic */ Key $$INSTANCE = new Key();

        private Key() {
        }
    }

    <E extends Element> E get(kotlin.coroutines.CoroutineContext.Key<E> key);

    <T> Continuation<T> interceptContinuation(Continuation<? super T> continuation);

    CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Key<?> key);

    void releaseInterceptedContinuation(Continuation<?> continuation);
}
