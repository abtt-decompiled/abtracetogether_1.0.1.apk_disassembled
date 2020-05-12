package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$mapIndexed$1", f = "Channels.common.kt", i = {0, 0, 1, 1, 1, 2, 2, 2}, l = {1423, 1424, 1424}, m = "invokeSuspend", n = {"$this$produce", "index", "$this$produce", "index", "e", "$this$produce", "index", "e"}, s = {"L$0", "I$0", "L$0", "I$0", "L$1", "L$0", "I$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$mapIndexed$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_mapIndexed;
    final /* synthetic */ Function3 $transform;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$mapIndexed$1(ReceiveChannel receiveChannel, Function3 function3, Continuation continuation) {
        this.$this_mapIndexed = receiveChannel;
        this.$transform = function3;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$1 = new ChannelsKt__Channels_commonKt$mapIndexed$1(this.$this_mapIndexed, this.$transform, continuation);
        channelsKt__Channels_commonKt$mapIndexed$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$mapIndexed$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$mapIndexed$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00b2  */
    public final Object invokeSuspend(Object obj) {
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$1;
        Object obj2;
        int i;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        ProducerScope producerScope3;
        int i2;
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$12;
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$13;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = this.label;
        if (i3 == 0) {
            ResultKt.throwOnFailure(obj);
            producerScope3 = this.p$;
            channelsKt__Channels_commonKt$mapIndexed$12 = this;
            i2 = 0;
            channelIterator2 = this.$this_mapIndexed.iterator();
        } else if (i3 == 1) {
            channelIterator2 = (ChannelIterator) this.L$1;
            i2 = this.I$0;
            producerScope = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            channelsKt__Channels_commonKt$mapIndexed$13 = this;
            if (!((Boolean) obj).booleanValue()) {
                Object next = channelIterator2.next();
                Function3 function3 = channelsKt__Channels_commonKt$mapIndexed$13.$transform;
                Integer boxInt = Boxing.boxInt(i2);
                int i4 = i2 + 1;
                channelsKt__Channels_commonKt$mapIndexed$13.L$0 = producerScope;
                channelsKt__Channels_commonKt$mapIndexed$13.I$0 = i4;
                channelsKt__Channels_commonKt$mapIndexed$13.L$1 = next;
                channelsKt__Channels_commonKt$mapIndexed$13.L$2 = channelIterator2;
                channelsKt__Channels_commonKt$mapIndexed$13.L$3 = producerScope;
                channelsKt__Channels_commonKt$mapIndexed$13.label = 2;
                Object invoke = function3.invoke(boxInt, next, channelsKt__Channels_commonKt$mapIndexed$13);
                if (invoke == coroutine_suspended) {
                    return coroutine_suspended;
                }
                channelsKt__Channels_commonKt$mapIndexed$1 = channelsKt__Channels_commonKt$mapIndexed$13;
                i = i4;
                channelIterator = channelIterator2;
                producerScope2 = producerScope;
                Object obj3 = invoke;
                obj2 = next;
                obj = obj3;
                channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope;
                channelsKt__Channels_commonKt$mapIndexed$1.I$0 = i;
                channelsKt__Channels_commonKt$mapIndexed$1.L$1 = obj2;
                channelsKt__Channels_commonKt$mapIndexed$1.L$2 = channelIterator;
                channelsKt__Channels_commonKt$mapIndexed$1.label = 3;
                if (producerScope2.send(obj, channelsKt__Channels_commonKt$mapIndexed$1) != coroutine_suspended) {
                }
                return coroutine_suspended;
                return coroutine_suspended;
            }
            return Unit.INSTANCE;
        } else if (i3 == 2) {
            producerScope2 = (ProducerScope) this.L$3;
            channelIterator = (ChannelIterator) this.L$2;
            Object obj4 = this.L$1;
            i = this.I$0;
            ProducerScope producerScope4 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            channelsKt__Channels_commonKt$mapIndexed$1 = this;
            ProducerScope producerScope5 = producerScope4;
            obj2 = obj4;
            producerScope = producerScope5;
            channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope;
            channelsKt__Channels_commonKt$mapIndexed$1.I$0 = i;
            channelsKt__Channels_commonKt$mapIndexed$1.L$1 = obj2;
            channelsKt__Channels_commonKt$mapIndexed$1.L$2 = channelIterator;
            channelsKt__Channels_commonKt$mapIndexed$1.label = 3;
            if (producerScope2.send(obj, channelsKt__Channels_commonKt$mapIndexed$1) != coroutine_suspended) {
                return coroutine_suspended;
            }
            channelIterator2 = channelIterator;
            producerScope3 = producerScope;
            i2 = i;
            channelsKt__Channels_commonKt$mapIndexed$12 = channelsKt__Channels_commonKt$mapIndexed$1;
            return coroutine_suspended;
        } else if (i3 == 3) {
            channelIterator2 = (ChannelIterator) this.L$2;
            i2 = this.I$0;
            ProducerScope producerScope6 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            producerScope3 = producerScope6;
            channelsKt__Channels_commonKt$mapIndexed$12 = this;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope3;
        channelsKt__Channels_commonKt$mapIndexed$12.I$0 = i2;
        channelsKt__Channels_commonKt$mapIndexed$12.L$1 = channelIterator2;
        channelsKt__Channels_commonKt$mapIndexed$12.label = 1;
        Object hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapIndexed$12);
        if (hasNext == coroutine_suspended) {
            return coroutine_suspended;
        }
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$14 = channelsKt__Channels_commonKt$mapIndexed$12;
        producerScope = producerScope3;
        obj = hasNext;
        channelsKt__Channels_commonKt$mapIndexed$13 = channelsKt__Channels_commonKt$mapIndexed$14;
        if (!((Boolean) obj).booleanValue()) {
        }
        return Unit.INSTANCE;
        return coroutine_suspended;
    }
}
