package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.channels.BroadcastChannel;
import kotlinx.coroutines.channels.ChannelsKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.channels.ValueOrClosed;
import kotlinx.coroutines.flow.internal.ChannelFlowKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u001a0\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007\u001a\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\nH\u0007\u001a/\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a&\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011"}, d2 = {"asFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "Lkotlinx/coroutines/channels/BroadcastChannel;", "broadcastIn", "scope", "Lkotlinx/coroutines/CoroutineScope;", "start", "Lkotlinx/coroutines/CoroutineStart;", "consumeAsFlow", "Lkotlinx/coroutines/channels/ReceiveChannel;", "emitAll", "", "Lkotlinx/coroutines/flow/FlowCollector;", "channel", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "produceIn", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* compiled from: Channels.kt */
final /* synthetic */ class FlowKt__ChannelsKt {
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a5, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00a6, code lost:
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00a9, code lost:
        throw r9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0071 A[Catch:{ all -> 0x00a5 }, RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0072 A[Catch:{ all -> 0x00a5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x008e A[SYNTHETIC, Splitter:B:32:0x008e] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008f A[Catch:{ all -> 0x00a5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    public static final <T> Object emitAll(FlowCollector<? super T> flowCollector, ReceiveChannel<? extends T> receiveChannel, Continuation<? super Unit> continuation) {
        FlowKt__ChannelsKt$emitAll$1 flowKt__ChannelsKt$emitAll$1;
        Object coroutine_suspended;
        int i;
        Throwable th;
        FlowCollector<? super T> flowCollector2;
        Object r9;
        Throwable r92;
        Throwable th2;
        Object receiveOrClosed;
        if (continuation instanceof FlowKt__ChannelsKt$emitAll$1) {
            flowKt__ChannelsKt$emitAll$1 = (FlowKt__ChannelsKt$emitAll$1) continuation;
            if ((flowKt__ChannelsKt$emitAll$1.label & Integer.MIN_VALUE) != 0) {
                flowKt__ChannelsKt$emitAll$1.label -= Integer.MIN_VALUE;
                Object obj = flowKt__ChannelsKt$emitAll$1.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = flowKt__ChannelsKt$emitAll$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    th2 = null;
                    flowKt__ChannelsKt$emitAll$1.L$0 = flowCollector;
                    flowKt__ChannelsKt$emitAll$1.L$1 = receiveChannel;
                    flowKt__ChannelsKt$emitAll$1.L$2 = th2;
                    flowKt__ChannelsKt$emitAll$1.L$3 = flowCollector;
                    flowKt__ChannelsKt$emitAll$1.label = 1;
                    receiveOrClosed = receiveChannel.receiveOrClosed(flowKt__ChannelsKt$emitAll$1);
                    if (receiveOrClosed == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Object obj2 = receiveOrClosed;
                    flowCollector2 = flowCollector;
                    th = th2;
                    obj = obj2;
                    r9 = ((ValueOrClosed) obj).m989unboximpl();
                    if (!ValueOrClosed.m987isClosedimpl(r9)) {
                    }
                    r92 = ValueOrClosed.m983getCloseCauseimpl(r9);
                    if (r92 != null) {
                    }
                    return coroutine_suspended;
                } else if (i == 1) {
                    FlowCollector flowCollector3 = (FlowCollector) flowKt__ChannelsKt$emitAll$1.L$3;
                    th = (Throwable) flowKt__ChannelsKt$emitAll$1.L$2;
                    receiveChannel = (ReceiveChannel) flowKt__ChannelsKt$emitAll$1.L$1;
                    flowCollector2 = (FlowCollector) flowKt__ChannelsKt$emitAll$1.L$0;
                    ResultKt.throwOnFailure(obj);
                    r9 = ((ValueOrClosed) obj).m989unboximpl();
                    if (!ValueOrClosed.m987isClosedimpl(r9)) {
                        r92 = ValueOrClosed.m983getCloseCauseimpl(r9);
                    } else {
                        Object r5 = ValueOrClosed.m984getValueimpl(r9);
                        flowKt__ChannelsKt$emitAll$1.L$0 = flowCollector2;
                        flowKt__ChannelsKt$emitAll$1.L$1 = receiveChannel;
                        flowKt__ChannelsKt$emitAll$1.L$2 = th;
                        flowKt__ChannelsKt$emitAll$1.L$3 = r9;
                        flowKt__ChannelsKt$emitAll$1.label = 2;
                        if (flowCollector2.emit(r5, flowKt__ChannelsKt$emitAll$1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    r92 = ValueOrClosed.m983getCloseCauseimpl(r9);
                    if (r92 != null) {
                        ChannelsKt.cancelConsumed(receiveChannel, th);
                        return Unit.INSTANCE;
                    }
                    throw r92;
                } else if (i == 2) {
                    Object obj3 = flowKt__ChannelsKt$emitAll$1.L$3;
                    th = (Throwable) flowKt__ChannelsKt$emitAll$1.L$2;
                    receiveChannel = (ReceiveChannel) flowKt__ChannelsKt$emitAll$1.L$1;
                    flowCollector2 = (FlowCollector) flowKt__ChannelsKt$emitAll$1.L$0;
                    ResultKt.throwOnFailure(obj);
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                th2 = th;
                flowCollector = flowCollector2;
                flowKt__ChannelsKt$emitAll$1.L$0 = flowCollector;
                flowKt__ChannelsKt$emitAll$1.L$1 = receiveChannel;
                flowKt__ChannelsKt$emitAll$1.L$2 = th2;
                flowKt__ChannelsKt$emitAll$1.L$3 = flowCollector;
                flowKt__ChannelsKt$emitAll$1.label = 1;
                receiveOrClosed = receiveChannel.receiveOrClosed(flowKt__ChannelsKt$emitAll$1);
                if (receiveOrClosed == coroutine_suspended) {
                }
                return coroutine_suspended;
            }
        }
        flowKt__ChannelsKt$emitAll$1 = new FlowKt__ChannelsKt$emitAll$1(continuation);
        Object obj4 = flowKt__ChannelsKt$emitAll$1.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = flowKt__ChannelsKt$emitAll$1.label;
        if (i != 0) {
        }
        th2 = th;
        flowCollector = flowCollector2;
        flowKt__ChannelsKt$emitAll$1.L$0 = flowCollector;
        flowKt__ChannelsKt$emitAll$1.L$1 = receiveChannel;
        flowKt__ChannelsKt$emitAll$1.L$2 = th2;
        flowKt__ChannelsKt$emitAll$1.L$3 = flowCollector;
        flowKt__ChannelsKt$emitAll$1.label = 1;
        receiveOrClosed = receiveChannel.receiveOrClosed(flowKt__ChannelsKt$emitAll$1);
        if (receiveOrClosed == coroutine_suspended) {
        }
        return coroutine_suspended;
    }

    public static final <T> Flow<T> consumeAsFlow(ReceiveChannel<? extends T> receiveChannel) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$consumeAsFlow");
        ConsumeAsFlow consumeAsFlow = new ConsumeAsFlow(receiveChannel, null, 0, 6, null);
        return consumeAsFlow;
    }

    public static /* synthetic */ BroadcastChannel broadcastIn$default(Flow flow, CoroutineScope coroutineScope, CoroutineStart coroutineStart, int i, Object obj) {
        if ((i & 2) != 0) {
            coroutineStart = CoroutineStart.LAZY;
        }
        return FlowKt.broadcastIn(flow, coroutineScope, coroutineStart);
    }

    public static final <T> BroadcastChannel<T> broadcastIn(Flow<? extends T> flow, CoroutineScope coroutineScope, CoroutineStart coroutineStart) {
        Intrinsics.checkParameterIsNotNull(flow, "$this$broadcastIn");
        Intrinsics.checkParameterIsNotNull(coroutineScope, "scope");
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        return ChannelFlowKt.asChannelFlow(flow).broadcastImpl(coroutineScope, coroutineStart);
    }

    public static final <T> ReceiveChannel<T> produceIn(Flow<? extends T> flow, CoroutineScope coroutineScope) {
        Intrinsics.checkParameterIsNotNull(flow, "$this$produceIn");
        Intrinsics.checkParameterIsNotNull(coroutineScope, "scope");
        return ChannelFlowKt.asChannelFlow(flow).produceImpl(coroutineScope);
    }

    public static final <T> Flow<T> asFlow(BroadcastChannel<T> broadcastChannel) {
        Intrinsics.checkParameterIsNotNull(broadcastChannel, "$this$asFlow");
        return new FlowKt__ChannelsKt$asFlow$$inlined$unsafeFlow$1<>(broadcastChannel);
    }
}
