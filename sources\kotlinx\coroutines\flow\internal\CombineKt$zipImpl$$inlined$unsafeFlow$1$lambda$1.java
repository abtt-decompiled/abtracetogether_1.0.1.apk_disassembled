package kotlinx.coroutines.flow.internal;

import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.ChannelIterator;
import kotlinx.coroutines.channels.ChannelsKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.channels.SendChannel;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.internal.Symbol;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0004*\u00020\u0005H@¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"<anonymous>", "", "T1", "T2", "R", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx/coroutines/flow/internal/CombineKt$zipImpl$1$1"}, k = 3, mv = {1, 1, 15})
/* compiled from: Combine.kt */
final class CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ FlowCollector $this_unsafeFlow;
    Object L$0;
    Object L$1;
    Object L$10;
    Object L$11;
    Object L$12;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    Object L$9;
    int label;
    private CoroutineScope p$;
    final /* synthetic */ CombineKt$zipImpl$$inlined$unsafeFlow$1 this$0;

    CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1(FlowCollector flowCollector, Continuation continuation, CombineKt$zipImpl$$inlined$unsafeFlow$1 combineKt$zipImpl$$inlined$unsafeFlow$1) {
        this.$this_unsafeFlow = flowCollector;
        this.this$0 = combineKt$zipImpl$$inlined$unsafeFlow$1;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 = new CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1(this.$this_unsafeFlow, continuation, this.this$0);
        combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.p$ = (CoroutineScope) obj;
        return combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x0186 A[Catch:{ all -> 0x0119 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01d0 A[Catch:{ all -> 0x0257 }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x01d3 A[Catch:{ all -> 0x0257 }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x01df A[Catch:{ all -> 0x0257 }] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x020e  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x023e  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0266  */
    public final Object invokeSuspend(Object obj) {
        ReceiveChannel receiveChannel;
        AbortFlowException abortFlowException;
        final ReceiveChannel receiveChannel2;
        Throwable th;
        CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1;
        CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12;
        ReceiveChannel receiveChannel3;
        ReceiveChannel receiveChannel4;
        ReceiveChannel receiveChannel5;
        Throwable th2;
        ChannelIterator channelIterator;
        CoroutineScope coroutineScope;
        ChannelIterator channelIterator2;
        ChannelIterator channelIterator3;
        ReceiveChannel receiveChannel6;
        ReceiveChannel receiveChannel7;
        ChannelIterator channelIterator4;
        Throwable th3;
        ReceiveChannel receiveChannel8;
        ReceiveChannel receiveChannel9;
        CoroutineScope coroutineScope2;
        ChannelIterator channelIterator5;
        Object obj2;
        ReceiveChannel receiveChannel10;
        Object obj3;
        ChannelIterator channelIterator6;
        FlowCollector flowCollector;
        Object obj4;
        Object obj5;
        CoroutineScope coroutineScope3;
        ReceiveChannel receiveChannel11;
        ChannelIterator channelIterator7;
        ReceiveChannel receiveChannel12;
        CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13;
        Object obj6;
        Throwable th4;
        ReceiveChannel receiveChannel13;
        Object obj7;
        ReceiveChannel receiveChannel14;
        Symbol symbol;
        Object next;
        Object obj8;
        CoroutineScope coroutineScope4;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            coroutineScope = this.p$;
            receiveChannel2 = CombineKt.asChannel(coroutineScope, this.this$0.$flow$inlined);
            receiveChannel = CombineKt.asChannel(coroutineScope, this.this$0.$flow2$inlined);
            if (receiveChannel != null) {
                ((SendChannel) receiveChannel).invokeOnClose(new Function1<Throwable, Unit>() {
                    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                        invoke((Throwable) obj);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(Throwable th) {
                        if (!receiveChannel2.isClosedForReceive()) {
                            receiveChannel2.cancel((CancellationException) new AbortFlowException());
                        }
                    }
                });
                channelIterator = receiveChannel.iterator();
                try {
                    th2 = null;
                    channelIterator3 = receiveChannel2.iterator();
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12 = this;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12;
                    receiveChannel5 = receiveChannel2;
                    receiveChannel4 = receiveChannel5;
                    receiveChannel3 = receiveChannel4;
                } catch (AbortFlowException unused) {
                    if (!receiveChannel.isClosedForReceive()) {
                        abortFlowException = new AbortFlowException();
                    }
                } catch (Throwable th5) {
                    if (!receiveChannel.isClosedForReceive()) {
                        receiveChannel.cancel((CancellationException) new AbortFlowException());
                    }
                    throw th5;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.SendChannel<*>");
            }
        } else if (i == 1) {
            ChannelIterator channelIterator8 = (ChannelIterator) this.L$9;
            ReceiveChannel receiveChannel15 = (ReceiveChannel) this.L$8;
            Throwable th6 = (Throwable) this.L$7;
            receiveChannel2 = (ReceiveChannel) this.L$6;
            CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$14 = (CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1) this.L$5;
            ReceiveChannel receiveChannel16 = (ReceiveChannel) this.L$4;
            ChannelIterator channelIterator9 = (ChannelIterator) this.L$3;
            receiveChannel = (ReceiveChannel) this.L$2;
            ReceiveChannel receiveChannel17 = (ReceiveChannel) this.L$1;
            CoroutineScope coroutineScope5 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            obj8 = obj;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 = this;
            Throwable th7 = th6;
            channelIterator6 = channelIterator8;
            receiveChannel14 = receiveChannel17;
            receiveChannel12 = receiveChannel16;
            th4 = th7;
            CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$15 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$14;
            receiveChannel13 = receiveChannel15;
            coroutineScope4 = coroutineScope5;
            channelIterator7 = channelIterator9;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$15;
            if (!((Boolean) obj8).booleanValue()) {
                obj2 = channelIterator6.next();
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$0 = coroutineScope4;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$1 = receiveChannel14;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$2 = receiveChannel;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$3 = channelIterator7;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$4 = receiveChannel12;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$5 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$6 = receiveChannel2;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$7 = th4;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$8 = receiveChannel13;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$9 = channelIterator6;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$10 = obj2;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$11 = obj2;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.label = 2;
                obj6 = channelIterator7.hasNext(combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1);
                if (obj6 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel11 = receiveChannel;
                coroutineScope3 = coroutineScope4;
                obj7 = obj2;
                if (((Boolean) obj6).booleanValue()) {
                    FlowCollector flowCollector2 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.$this_unsafeFlow;
                    Function3 function3 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.this$0.$transform$inlined;
                    Object obj9 = coroutine_suspended;
                    if (obj7 != NullSurrogateKt.NULL) {
                    }
                    symbol = NullSurrogateKt.NULL;
                    Function3 function32 = function3;
                    next = channelIterator7.next();
                    if (next == symbol) {
                    }
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$0 = coroutineScope3;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$1 = receiveChannel14;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$2 = receiveChannel11;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$3 = channelIterator7;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$4 = receiveChannel12;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$5 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$6 = receiveChannel2;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$7 = th4;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$8 = receiveChannel13;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$9 = channelIterator6;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$10 = obj2;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$11 = obj7;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$12 = flowCollector2;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.label = 3;
                    Object obj10 = r17;
                    ReceiveChannel receiveChannel18 = receiveChannel14;
                    obj5 = function32.invoke(obj10, next, combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1);
                    obj4 = obj9;
                    if (obj5 != obj4) {
                    }
                    return obj4;
                }
                channelIterator2 = channelIterator6;
                th2 = th4;
                receiveChannel5 = receiveChannel12;
                channelIterator = channelIterator7;
                receiveChannel3 = receiveChannel13;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13;
                receiveChannel4 = receiveChannel2;
                receiveChannel2 = receiveChannel14;
                coroutineScope = coroutineScope3;
                receiveChannel = receiveChannel11;
                channelIterator3 = channelIterator2;
                return coroutine_suspended;
            }
            Unit unit = Unit.INSTANCE;
            ChannelsKt.cancelConsumed(receiveChannel2, th4);
            if (!receiveChannel.isClosedForReceive()) {
                abortFlowException = new AbortFlowException();
                receiveChannel.cancel((CancellationException) abortFlowException);
            }
            return Unit.INSTANCE;
        } else if (i == 2) {
            Object obj11 = this.L$11;
            Object obj12 = this.L$10;
            channelIterator6 = (ChannelIterator) this.L$9;
            receiveChannel13 = (ReceiveChannel) this.L$8;
            th4 = (Throwable) this.L$7;
            receiveChannel2 = (ReceiveChannel) this.L$6;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13 = (CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1) this.L$5;
            receiveChannel12 = (ReceiveChannel) this.L$4;
            channelIterator7 = (ChannelIterator) this.L$3;
            receiveChannel = (ReceiveChannel) this.L$2;
            ReceiveChannel receiveChannel19 = (ReceiveChannel) this.L$1;
            CoroutineScope coroutineScope6 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            obj6 = obj;
            obj2 = obj12;
            obj7 = obj11;
            receiveChannel14 = receiveChannel19;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 = this;
            ReceiveChannel receiveChannel20 = receiveChannel;
            coroutineScope3 = coroutineScope6;
            receiveChannel11 = receiveChannel20;
            try {
                if (((Boolean) obj6).booleanValue()) {
                    channelIterator2 = channelIterator6;
                    th2 = th4;
                    receiveChannel5 = receiveChannel12;
                    channelIterator = channelIterator7;
                    receiveChannel3 = receiveChannel13;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13;
                    receiveChannel4 = receiveChannel2;
                    receiveChannel2 = receiveChannel14;
                    coroutineScope = coroutineScope3;
                    receiveChannel = receiveChannel11;
                    channelIterator3 = channelIterator2;
                }
                FlowCollector flowCollector22 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.$this_unsafeFlow;
                Function3 function33 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.this$0.$transform$inlined;
                Object obj92 = coroutine_suspended;
                Object obj13 = obj7 != NullSurrogateKt.NULL ? null : obj7;
                symbol = NullSurrogateKt.NULL;
                Function3 function322 = function33;
                next = channelIterator7.next();
                if (next == symbol) {
                    next = null;
                }
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$0 = coroutineScope3;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$1 = receiveChannel14;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$2 = receiveChannel11;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$3 = channelIterator7;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$4 = receiveChannel12;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$5 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$6 = receiveChannel2;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$7 = th4;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$8 = receiveChannel13;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$9 = channelIterator6;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$10 = obj2;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$11 = obj7;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$12 = flowCollector22;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.label = 3;
                Object obj102 = obj13;
                ReceiveChannel receiveChannel182 = receiveChannel14;
                obj5 = function322.invoke(obj102, next, combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1);
                obj4 = obj92;
                if (obj5 != obj4) {
                    return obj4;
                }
                flowCollector = flowCollector22;
                receiveChannel10 = receiveChannel182;
                ReceiveChannel receiveChannel21 = receiveChannel13;
                obj3 = obj7;
                coroutineScope2 = coroutineScope3;
                receiveChannel7 = receiveChannel12;
                th3 = th4;
                receiveChannel9 = receiveChannel11;
                channelIterator4 = channelIterator7;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13;
                receiveChannel8 = receiveChannel21;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$0 = coroutineScope2;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$1 = receiveChannel10;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$2 = receiveChannel9;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$3 = channelIterator4;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$4 = receiveChannel7;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$5 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$6 = receiveChannel2;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$7 = th3;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$8 = receiveChannel8;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$9 = channelIterator6;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$10 = obj2;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$11 = obj3;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.label = 4;
                if (flowCollector.emit(obj5, combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1) != obj4) {
                }
                return obj4;
                return obj4;
            } catch (Throwable th8) {
                th = th8;
                receiveChannel = receiveChannel11;
                throw th;
            }
        } else if (i == 3) {
            flowCollector = (FlowCollector) this.L$12;
            Object obj14 = this.L$11;
            Object obj15 = this.L$10;
            ChannelIterator channelIterator10 = (ChannelIterator) this.L$9;
            ReceiveChannel receiveChannel22 = (ReceiveChannel) this.L$8;
            Throwable th9 = (Throwable) this.L$7;
            ReceiveChannel receiveChannel23 = (ReceiveChannel) this.L$6;
            CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$16 = (CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1) this.L$5;
            receiveChannel7 = (ReceiveChannel) this.L$4;
            ChannelIterator channelIterator11 = (ChannelIterator) this.L$3;
            ReceiveChannel receiveChannel24 = (ReceiveChannel) this.L$2;
            ReceiveChannel receiveChannel25 = (ReceiveChannel) this.L$1;
            CoroutineScope coroutineScope7 = (CoroutineScope) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                receiveChannel10 = receiveChannel25;
                obj3 = obj14;
                coroutineScope2 = coroutineScope7;
                receiveChannel9 = receiveChannel24;
                channelIterator6 = channelIterator10;
                th3 = th9;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$16;
                channelIterator4 = channelIterator11;
                obj2 = obj15;
                receiveChannel8 = receiveChannel22;
                receiveChannel2 = receiveChannel23;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 = this;
                obj4 = coroutine_suspended;
                obj5 = obj;
                try {
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$0 = coroutineScope2;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$1 = receiveChannel10;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$2 = receiveChannel9;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$3 = channelIterator4;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$4 = receiveChannel7;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$5 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$6 = receiveChannel2;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$7 = th3;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$8 = receiveChannel8;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$9 = channelIterator6;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$10 = obj2;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$11 = obj3;
                    combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.label = 4;
                    if (flowCollector.emit(obj5, combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1) != obj4) {
                        return obj4;
                    }
                    coroutine_suspended = obj4;
                    channelIterator5 = channelIterator6;
                    receiveChannel6 = receiveChannel10;
                    channelIterator2 = channelIterator5;
                    coroutineScope = coroutineScope2;
                    th2 = th3;
                    channelIterator = channelIterator4;
                    receiveChannel3 = receiveChannel8;
                    receiveChannel4 = receiveChannel2;
                    receiveChannel2 = receiveChannel6;
                    ReceiveChannel receiveChannel26 = receiveChannel7;
                    receiveChannel = receiveChannel9;
                    receiveChannel5 = receiveChannel26;
                    channelIterator3 = channelIterator2;
                    return obj4;
                } catch (Throwable th10) {
                    th = th10;
                    receiveChannel = receiveChannel9;
                    throw th;
                }
            } catch (Throwable th11) {
                th = th11;
                receiveChannel = receiveChannel24;
                receiveChannel2 = receiveChannel23;
                throw th;
            }
        } else if (i == 4) {
            channelIterator5 = (ChannelIterator) this.L$9;
            receiveChannel8 = (ReceiveChannel) this.L$8;
            th3 = (Throwable) this.L$7;
            receiveChannel2 = (ReceiveChannel) this.L$6;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12 = (CombineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1) this.L$5;
            ReceiveChannel receiveChannel27 = (ReceiveChannel) this.L$4;
            channelIterator4 = (ChannelIterator) this.L$3;
            receiveChannel = (ReceiveChannel) this.L$2;
            receiveChannel6 = (ReceiveChannel) this.L$1;
            coroutineScope2 = (CoroutineScope) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                receiveChannel9 = receiveChannel;
                receiveChannel7 = receiveChannel27;
                combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1 = this;
                channelIterator2 = channelIterator5;
                coroutineScope = coroutineScope2;
                th2 = th3;
                channelIterator = channelIterator4;
                receiveChannel3 = receiveChannel8;
                receiveChannel4 = receiveChannel2;
                receiveChannel2 = receiveChannel6;
                ReceiveChannel receiveChannel262 = receiveChannel7;
                receiveChannel = receiveChannel9;
                receiveChannel5 = receiveChannel262;
                channelIterator3 = channelIterator2;
            } catch (Throwable th12) {
                th = th12;
                throw th;
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        try {
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$0 = coroutineScope;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$1 = receiveChannel2;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$2 = receiveChannel;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$3 = channelIterator;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$4 = receiveChannel5;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$5 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$6 = receiveChannel4;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$7 = th2;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$8 = receiveChannel3;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.L$9 = channelIterator3;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$1.label = 1;
            obj8 = channelIterator3.hasNext(combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12);
            if (obj8 == coroutine_suspended) {
                return coroutine_suspended;
            }
            ChannelIterator channelIterator12 = channelIterator;
            coroutineScope4 = coroutineScope;
            receiveChannel14 = receiveChannel2;
            receiveChannel2 = receiveChannel4;
            combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$13 = combineKt$zipImpl$$inlined$unsafeFlow$1$lambda$12;
            channelIterator7 = channelIterator12;
            ReceiveChannel receiveChannel28 = receiveChannel5;
            th4 = th2;
            channelIterator6 = channelIterator3;
            receiveChannel13 = receiveChannel3;
            receiveChannel12 = receiveChannel28;
            if (!((Boolean) obj8).booleanValue()) {
            }
            Unit unit2 = Unit.INSTANCE;
            ChannelsKt.cancelConsumed(receiveChannel2, th4);
            if (!receiveChannel.isClosedForReceive()) {
            }
            return Unit.INSTANCE;
            return coroutine_suspended;
        } catch (Throwable th13) {
            th = th13;
            receiveChannel2 = receiveChannel4;
            throw th;
        }
    }
}
