package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$flatMap$1", f = "Channels.common.kt", i = {0, 1, 1, 2, 2}, l = {1291, 1292, 1292}, m = "invokeSuspend", n = {"$this$produce", "$this$produce", "e", "$this$produce", "e"}, s = {"L$0", "L$0", "L$1", "L$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$flatMap$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_flatMap;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$flatMap$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_flatMap = receiveChannel;
        this.$transform = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$1 = new ChannelsKt__Channels_commonKt$flatMap$1(this.$this_flatMap, this.$transform, continuation);
        channelsKt__Channels_commonKt$flatMap$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$flatMap$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$flatMap$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x009b  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ChannelIterator channelIterator;
        Object obj3;
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$1;
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$12;
        ChannelIterator channelIterator2;
        Object obj4;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            ProducerScope producerScope = this.p$;
            channelIterator2 = this.$this_flatMap.iterator();
            obj4 = producerScope;
        } else if (i == 1) {
            ChannelIterator channelIterator3 = (ChannelIterator) this.L$1;
            ProducerScope producerScope2 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            obj2 = producerScope2;
            channelIterator = channelIterator3;
            obj3 = coroutine_suspended;
            channelsKt__Channels_commonKt$flatMap$1 = this;
            if (!((Boolean) obj).booleanValue()) {
            }
            return Unit.INSTANCE;
        } else if (i == 2) {
            ChannelIterator channelIterator4 = (ChannelIterator) this.L$2;
            Object obj5 = this.L$1;
            obj2 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            Object obj6 = obj5;
            channelIterator = channelIterator4;
            obj3 = coroutine_suspended;
            channelsKt__Channels_commonKt$flatMap$1 = this;
            ReceiveChannel receiveChannel = (ReceiveChannel) obj;
            SendChannel sendChannel = (SendChannel) obj2;
            channelsKt__Channels_commonKt$flatMap$1.L$0 = obj2;
            channelsKt__Channels_commonKt$flatMap$1.L$1 = obj6;
            channelsKt__Channels_commonKt$flatMap$1.L$2 = channelIterator;
            channelsKt__Channels_commonKt$flatMap$1.label = 3;
            if (ChannelsKt.toChannel(receiveChannel, sendChannel, channelsKt__Channels_commonKt$flatMap$1) != obj3) {
                return obj3;
            }
            channelsKt__Channels_commonKt$flatMap$12 = channelsKt__Channels_commonKt$flatMap$1;
            coroutine_suspended = obj3;
            channelIterator2 = channelIterator;
            obj4 = obj2;
            channelsKt__Channels_commonKt$flatMap$12.L$0 = obj4;
            channelsKt__Channels_commonKt$flatMap$12.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$flatMap$12.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$flatMap$12);
            if (hasNext != coroutine_suspended) {
                return coroutine_suspended;
            }
            Object obj7 = coroutine_suspended;
            channelsKt__Channels_commonKt$flatMap$1 = channelsKt__Channels_commonKt$flatMap$12;
            obj = hasNext;
            obj2 = obj4;
            channelIterator = channelIterator2;
            obj3 = obj7;
            if (!((Boolean) obj).booleanValue()) {
                Object next = channelIterator.next();
                Function2 function2 = channelsKt__Channels_commonKt$flatMap$1.$transform;
                channelsKt__Channels_commonKt$flatMap$1.L$0 = obj2;
                channelsKt__Channels_commonKt$flatMap$1.L$1 = next;
                channelsKt__Channels_commonKt$flatMap$1.L$2 = channelIterator;
                channelsKt__Channels_commonKt$flatMap$1.label = 2;
                Object invoke = function2.invoke(next, channelsKt__Channels_commonKt$flatMap$1);
                if (invoke == obj3) {
                    return obj3;
                }
                Object obj8 = invoke;
                obj6 = next;
                obj = obj8;
                ReceiveChannel receiveChannel2 = (ReceiveChannel) obj;
                SendChannel sendChannel2 = (SendChannel) obj2;
                channelsKt__Channels_commonKt$flatMap$1.L$0 = obj2;
                channelsKt__Channels_commonKt$flatMap$1.L$1 = obj6;
                channelsKt__Channels_commonKt$flatMap$1.L$2 = channelIterator;
                channelsKt__Channels_commonKt$flatMap$1.label = 3;
                if (ChannelsKt.toChannel(receiveChannel2, sendChannel2, channelsKt__Channels_commonKt$flatMap$1) != obj3) {
                }
                return obj3;
            }
            return Unit.INSTANCE;
            return coroutine_suspended;
            return obj3;
        } else if (i == 3) {
            channelIterator2 = (ChannelIterator) this.L$2;
            obj4 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        channelsKt__Channels_commonKt$flatMap$12 = this;
        channelsKt__Channels_commonKt$flatMap$12.L$0 = obj4;
        channelsKt__Channels_commonKt$flatMap$12.L$1 = channelIterator2;
        channelsKt__Channels_commonKt$flatMap$12.label = 1;
        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$flatMap$12);
        if (hasNext != coroutine_suspended) {
        }
        return coroutine_suspended;
    }
}
