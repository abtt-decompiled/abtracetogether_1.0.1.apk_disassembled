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
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$map$1", f = "Channels.common.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2}, l = {2201, 1400, 1400}, m = "invokeSuspend", n = {"$this$produce", "$this$consumeEach$iv", "$this$consume$iv$iv", "cause$iv$iv", "$this$consume$iv", "$this$produce", "$this$consumeEach$iv", "$this$consume$iv$iv", "cause$iv$iv", "$this$consume$iv", "e$iv", "it", "$this$produce", "$this$consumeEach$iv", "$this$consume$iv$iv", "cause$iv$iv", "$this$consume$iv", "e$iv", "it"}, s = {"L$0", "L$1", "L$3", "L$4", "L$5", "L$0", "L$1", "L$3", "L$4", "L$5", "L$7", "L$8", "L$0", "L$1", "L$3", "L$4", "L$5", "L$7", "L$8"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$map$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_map;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    Object L$9;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$map$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_map = receiveChannel;
        this.$transform = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$1 = new ChannelsKt__Channels_commonKt$map$1(this.$this_map, this.$transform, continuation);
        channelsKt__Channels_commonKt$map$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$map$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$map$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00e2 A[Catch:{ all -> 0x0138 }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0128  */
    public final Object invokeSuspend(Object obj) {
        ReceiveChannel receiveChannel;
        Throwable th;
        ReceiveChannel receiveChannel2;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$1;
        ReceiveChannel receiveChannel3;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$12;
        Throwable th2;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        ProducerScope producerScope2;
        Object obj2;
        Object obj3;
        ReceiveChannel receiveChannel4;
        ReceiveChannel receiveChannel5;
        Object obj4;
        ChannelIterator channelIterator2;
        ReceiveChannel receiveChannel6;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$13;
        ProducerScope producerScope3;
        Object obj5;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        int i2 = 2;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            ProducerScope producerScope4 = this.p$;
            receiveChannel = this.$this_map;
            channelsKt__Channels_commonKt$map$12 = this;
            channelsKt__Channels_commonKt$map$1 = channelsKt__Channels_commonKt$map$12;
            receiveChannel2 = receiveChannel;
            receiveChannel3 = receiveChannel2;
            Throwable th3 = null;
            producerScope = producerScope4;
            channelIterator = receiveChannel.iterator();
            th2 = th3;
        } else if (i == 1) {
            channelIterator = (ChannelIterator) this.L$6;
            ReceiveChannel receiveChannel7 = (ReceiveChannel) this.L$5;
            th2 = (Throwable) this.L$4;
            receiveChannel = (ReceiveChannel) this.L$3;
            ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$14 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
            ReceiveChannel receiveChannel8 = (ReceiveChannel) this.L$1;
            ProducerScope producerScope5 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            obj5 = obj;
            channelsKt__Channels_commonKt$map$1 = this;
            ReceiveChannel receiveChannel9 = receiveChannel;
            receiveChannel = receiveChannel7;
            producerScope = producerScope5;
            receiveChannel3 = receiveChannel8;
            channelsKt__Channels_commonKt$map$12 = channelsKt__Channels_commonKt$map$14;
            receiveChannel2 = receiveChannel9;
            if (!((Boolean) obj5).booleanValue()) {
                obj3 = channelIterator.next();
                Function2 function2 = channelsKt__Channels_commonKt$map$1.$transform;
                channelsKt__Channels_commonKt$map$1.L$0 = producerScope;
                channelsKt__Channels_commonKt$map$1.L$1 = receiveChannel3;
                channelsKt__Channels_commonKt$map$1.L$2 = channelsKt__Channels_commonKt$map$12;
                channelsKt__Channels_commonKt$map$1.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$map$1.L$4 = th2;
                channelsKt__Channels_commonKt$map$1.L$5 = receiveChannel;
                channelsKt__Channels_commonKt$map$1.L$6 = channelIterator;
                channelsKt__Channels_commonKt$map$1.L$7 = obj3;
                channelsKt__Channels_commonKt$map$1.L$8 = obj3;
                channelsKt__Channels_commonKt$map$1.L$9 = producerScope;
                channelsKt__Channels_commonKt$map$1.label = i2;
                obj2 = function2.invoke(obj3, channelsKt__Channels_commonKt$map$1);
                if (obj2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                producerScope2 = producerScope;
                channelsKt__Channels_commonKt$map$13 = channelsKt__Channels_commonKt$map$1;
                receiveChannel6 = receiveChannel;
                receiveChannel4 = receiveChannel3;
                channelIterator2 = channelIterator;
                producerScope3 = producerScope2;
                receiveChannel5 = receiveChannel2;
                obj4 = obj3;
                channelsKt__Channels_commonKt$map$13.L$0 = producerScope2;
                channelsKt__Channels_commonKt$map$13.L$1 = receiveChannel4;
                channelsKt__Channels_commonKt$map$13.L$2 = channelsKt__Channels_commonKt$map$12;
                channelsKt__Channels_commonKt$map$13.L$3 = receiveChannel5;
                channelsKt__Channels_commonKt$map$13.L$4 = th2;
                channelsKt__Channels_commonKt$map$13.L$5 = receiveChannel6;
                channelsKt__Channels_commonKt$map$13.L$6 = channelIterator2;
                channelsKt__Channels_commonKt$map$13.L$7 = obj4;
                channelsKt__Channels_commonKt$map$13.L$8 = obj3;
                channelsKt__Channels_commonKt$map$13.label = 3;
                if (producerScope3.send(obj2, channelsKt__Channels_commonKt$map$13) != coroutine_suspended) {
                }
                return coroutine_suspended;
                return coroutine_suspended;
            }
            Unit unit = Unit.INSTANCE;
            ChannelsKt.cancelConsumed(receiveChannel2, th2);
            return Unit.INSTANCE;
        } else if (i == 2) {
            producerScope3 = (ProducerScope) this.L$9;
            Object obj6 = this.L$8;
            Object obj7 = this.L$7;
            channelIterator2 = (ChannelIterator) this.L$6;
            ReceiveChannel receiveChannel10 = (ReceiveChannel) this.L$5;
            Throwable th4 = (Throwable) this.L$4;
            receiveChannel5 = (ReceiveChannel) this.L$3;
            ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$15 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
            ReceiveChannel receiveChannel11 = (ReceiveChannel) this.L$1;
            ProducerScope producerScope6 = (ProducerScope) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                channelsKt__Channels_commonKt$map$13 = this;
                producerScope2 = producerScope6;
                obj2 = obj;
                ReceiveChannel receiveChannel12 = receiveChannel11;
                obj3 = obj6;
                receiveChannel6 = receiveChannel10;
                obj4 = obj7;
                th2 = th4;
                channelsKt__Channels_commonKt$map$12 = channelsKt__Channels_commonKt$map$15;
                receiveChannel4 = receiveChannel12;
                channelsKt__Channels_commonKt$map$13.L$0 = producerScope2;
                channelsKt__Channels_commonKt$map$13.L$1 = receiveChannel4;
                channelsKt__Channels_commonKt$map$13.L$2 = channelsKt__Channels_commonKt$map$12;
                channelsKt__Channels_commonKt$map$13.L$3 = receiveChannel5;
                channelsKt__Channels_commonKt$map$13.L$4 = th2;
                channelsKt__Channels_commonKt$map$13.L$5 = receiveChannel6;
                channelsKt__Channels_commonKt$map$13.L$6 = channelIterator2;
                channelsKt__Channels_commonKt$map$13.L$7 = obj4;
                channelsKt__Channels_commonKt$map$13.L$8 = obj3;
                channelsKt__Channels_commonKt$map$13.label = 3;
                if (producerScope3.send(obj2, channelsKt__Channels_commonKt$map$13) != coroutine_suspended) {
                    return coroutine_suspended;
                }
                channelIterator = channelIterator2;
                receiveChannel2 = receiveChannel5;
                receiveChannel3 = receiveChannel4;
                channelsKt__Channels_commonKt$map$1 = channelsKt__Channels_commonKt$map$13;
                receiveChannel = receiveChannel6;
                producerScope = producerScope2;
                i2 = 2;
                return coroutine_suspended;
            } catch (Throwable th5) {
                th = th5;
                receiveChannel = receiveChannel5;
                throw th;
            }
        } else if (i == 3) {
            channelIterator = (ChannelIterator) this.L$6;
            ReceiveChannel receiveChannel13 = (ReceiveChannel) this.L$5;
            th2 = (Throwable) this.L$4;
            receiveChannel = (ReceiveChannel) this.L$3;
            ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$16 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
            ReceiveChannel receiveChannel14 = (ReceiveChannel) this.L$1;
            ProducerScope producerScope7 = (ProducerScope) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                channelsKt__Channels_commonKt$map$1 = this;
                ReceiveChannel receiveChannel15 = receiveChannel;
                receiveChannel = receiveChannel13;
                producerScope = producerScope7;
                receiveChannel3 = receiveChannel14;
                channelsKt__Channels_commonKt$map$12 = channelsKt__Channels_commonKt$map$16;
                receiveChannel2 = receiveChannel15;
                i2 = 2;
            } catch (Throwable th6) {
                th = th6;
                try {
                    throw th;
                } catch (Throwable th7) {
                    Throwable th8 = th7;
                    ChannelsKt.cancelConsumed(receiveChannel, th);
                    throw th8;
                }
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        try {
            channelsKt__Channels_commonKt$map$1.L$0 = producerScope;
            channelsKt__Channels_commonKt$map$1.L$1 = receiveChannel3;
            channelsKt__Channels_commonKt$map$1.L$2 = channelsKt__Channels_commonKt$map$12;
            channelsKt__Channels_commonKt$map$1.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$map$1.L$4 = th2;
            channelsKt__Channels_commonKt$map$1.L$5 = receiveChannel;
            channelsKt__Channels_commonKt$map$1.L$6 = channelIterator;
            channelsKt__Channels_commonKt$map$1.label = 1;
            obj5 = channelIterator.hasNext(channelsKt__Channels_commonKt$map$12);
            if (obj5 == coroutine_suspended) {
                return coroutine_suspended;
            }
            if (!((Boolean) obj5).booleanValue()) {
            }
            Unit unit2 = Unit.INSTANCE;
            ChannelsKt.cancelConsumed(receiveChannel2, th2);
            return Unit.INSTANCE;
        } catch (Throwable th9) {
            th = th9;
            receiveChannel = receiveChannel2;
            throw th;
        }
    }
}
