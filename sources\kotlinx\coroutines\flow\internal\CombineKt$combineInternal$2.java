package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.selects.SelectBuilder;
import kotlinx.coroutines.selects.SelectBuilderImpl;
import kotlinx.coroutines.selects.SelectClause1;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "R", "T", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2", f = "Combine.kt", i = {0, 0, 0, 0, 0}, l = {146}, m = "invokeSuspend", n = {"$this$coroutineScope", "size", "channels", "latestValues", "isClosed"}, s = {"L$0", "I$0", "L$1", "L$2", "L$3"})
/* compiled from: Combine.kt */
final class CombineKt$combineInternal$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function0 $arrayFactory;
    final /* synthetic */ Flow[] $flows;
    final /* synthetic */ FlowCollector $this_combineInternal;
    final /* synthetic */ Function3 $transform;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    private CoroutineScope p$;

    CombineKt$combineInternal$2(FlowCollector flowCollector, Flow[] flowArr, Function0 function0, Function3 function3, Continuation continuation) {
        this.$this_combineInternal = flowCollector;
        this.$flows = flowArr;
        this.$arrayFactory = function0;
        this.$transform = function3;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        CombineKt$combineInternal$2 combineKt$combineInternal$2 = new CombineKt$combineInternal$2(this.$this_combineInternal, this.$flows, this.$arrayFactory, this.$transform, continuation);
        combineKt$combineInternal$2.p$ = (CoroutineScope) obj;
        return combineKt$combineInternal$2;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((CombineKt$combineInternal$2) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x013f  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0095 A[SYNTHETIC] */
    public final Object invokeSuspend(Object obj) {
        Object[] objArr;
        Boolean[] boolArr;
        ReceiveChannel[] receiveChannelArr;
        int i;
        CoroutineScope coroutineScope;
        CombineKt$combineInternal$2 combineKt$combineInternal$2;
        Object obj2;
        int i2;
        int i3;
        CoroutineScope coroutineScope2;
        CombineKt$combineInternal$2 combineKt$combineInternal$22;
        Continuation continuation;
        ReceiveChannel[] receiveChannelArr2;
        SelectBuilderImpl selectBuilderImpl;
        Object result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i4 = this.label;
        int i5 = 0;
        int i6 = 1;
        if (i4 == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope3 = this.p$;
            int length = this.$flows.length;
            ReceiveChannel[] receiveChannelArr3 = new ReceiveChannel[length];
            for (int i7 = 0; i7 < length; i7++) {
                receiveChannelArr3[i7] = CombineKt.asFairChannel(coroutineScope3, this.$flows[Boxing.boxInt(i7).intValue()]);
            }
            Object[] objArr2 = new Object[length];
            Boolean[] boolArr2 = new Boolean[length];
            for (int i8 = 0; i8 < length; i8++) {
                Boxing.boxInt(i8).intValue();
                boolArr2[i8] = Boxing.boxBoolean(false);
            }
            combineKt$combineInternal$2 = this;
            coroutineScope = coroutineScope3;
            i = length;
            receiveChannelArr = receiveChannelArr3;
            objArr = objArr2;
            boolArr = boolArr2;
            obj2 = coroutine_suspended;
        } else if (i4 == 1) {
            CombineKt$combineInternal$2 combineKt$combineInternal$23 = (CombineKt$combineInternal$2) this.L$4;
            Boolean[] boolArr3 = (Boolean[]) this.L$3;
            Object[] objArr3 = (Object[]) this.L$2;
            ReceiveChannel[] receiveChannelArr4 = (ReceiveChannel[]) this.L$1;
            int i9 = this.I$0;
            CoroutineScope coroutineScope4 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            combineKt$combineInternal$2 = this;
            boolArr = boolArr3;
            objArr = objArr3;
            receiveChannelArr = receiveChannelArr4;
            i = i9;
            coroutineScope = coroutineScope4;
            obj2 = coroutine_suspended;
            i5 = 0;
            i6 = 1;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        int length2 = boolArr.length;
        int i10 = i5;
        while (true) {
            if (i10 >= length2) {
                i2 = i6;
                break;
            }
            if (!Boxing.boxBoolean(boolArr[i10].booleanValue()).booleanValue()) {
                i2 = i5;
                break;
            }
            i10++;
        }
        if (i2 != 0) {
            combineKt$combineInternal$2.L$0 = coroutineScope;
            combineKt$combineInternal$2.I$0 = i;
            combineKt$combineInternal$2.L$1 = receiveChannelArr;
            combineKt$combineInternal$2.L$2 = objArr;
            combineKt$combineInternal$2.L$3 = boolArr;
            combineKt$combineInternal$2.L$4 = combineKt$combineInternal$2;
            combineKt$combineInternal$2.label = i6;
            Continuation continuation2 = combineKt$combineInternal$2;
            SelectBuilderImpl selectBuilderImpl2 = new SelectBuilderImpl(continuation2);
            try {
            } catch (Throwable th) {
                th = th;
                selectBuilderImpl = selectBuilderImpl2;
                continuation = continuation2;
                combineKt$combineInternal$22 = combineKt$combineInternal$2;
                coroutineScope2 = coroutineScope;
                i3 = i;
            }
            SelectBuilder selectBuilder = selectBuilderImpl2;
            int i11 = i5;
            while (i11 < i) {
                boolean booleanValue = boolArr[i11].booleanValue();
                ReceiveChannel receiveChannel = receiveChannelArr[i11];
                r5 = r5;
                int i12 = i11;
                selectBuilderImpl = selectBuilderImpl2;
                continuation = continuation2;
                combineKt$combineInternal$22 = combineKt$combineInternal$2;
                coroutineScope2 = coroutineScope;
                i3 = i;
                try {
                } catch (Throwable th2) {
                    th = th2;
                    receiveChannelArr2 = receiveChannelArr;
                    selectBuilderImpl.handleBuilderException(th);
                    result = selectBuilderImpl.getResult();
                    if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    }
                    if (result != obj2) {
                    }
                    return obj2;
                }
                CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1 combineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1 = new CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1(i11, null, combineKt$combineInternal$2, i, boolArr, receiveChannelArr, objArr);
                Function2 function2 = combineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1;
                if (booleanValue) {
                    receiveChannelArr2 = receiveChannelArr;
                } else {
                    r5 = r5;
                    int i13 = i12;
                    CombineKt$combineInternal$2 combineKt$combineInternal$24 = combineKt$combineInternal$22;
                    int i14 = i3;
                    Boolean[] boolArr4 = boolArr;
                    SelectClause1 onReceiveOrNull = receiveChannel.getOnReceiveOrNull();
                    receiveChannelArr2 = receiveChannelArr;
                    try {
                        CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$2 combineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$2 = new CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$2(function2, null, i13, combineKt$combineInternal$24, i14, boolArr4, receiveChannelArr, objArr);
                        selectBuilder.invoke(onReceiveOrNull, (Function2<? super Q, ? super Continuation<? super R>, ? extends Object>) combineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$2);
                    } catch (Throwable th3) {
                        th = th3;
                    }
                }
                i11 = i12 + 1;
                selectBuilderImpl2 = selectBuilderImpl;
                receiveChannelArr = receiveChannelArr2;
                continuation2 = continuation;
                combineKt$combineInternal$2 = combineKt$combineInternal$22;
                coroutineScope = coroutineScope2;
                i = i3;
            }
            selectBuilderImpl = selectBuilderImpl2;
            continuation = continuation2;
            combineKt$combineInternal$22 = combineKt$combineInternal$2;
            coroutineScope2 = coroutineScope;
            i3 = i;
            receiveChannelArr2 = receiveChannelArr;
            result = selectBuilderImpl.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            if (result != obj2) {
                return obj2;
            }
            receiveChannelArr = receiveChannelArr2;
            combineKt$combineInternal$2 = combineKt$combineInternal$22;
            coroutineScope = coroutineScope2;
            i = i3;
            i5 = 0;
            i6 = 1;
            int length22 = boolArr.length;
            int i102 = i5;
            while (true) {
                if (i102 >= length22) {
                }
                i102++;
            }
            if (i2 != 0) {
            }
            return obj2;
        }
        return Unit.INSTANCE;
    }
}
