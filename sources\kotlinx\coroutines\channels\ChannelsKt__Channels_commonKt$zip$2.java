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

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0005H@¢\u0006\u0004\b\u0006\u0010\u0007"}, d2 = {"<anonymous>", "", "E", "R", "V", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$zip$2", f = "Channels.common.kt", i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2}, l = {2201, 2191, 2193}, m = "invokeSuspend", n = {"$this$produce", "otherIterator", "$this$consumeEach$iv", "$this$consume$iv$iv", "cause$iv$iv", "$this$consume$iv", "$this$produce", "otherIterator", "$this$consumeEach$iv", "$this$consume$iv$iv", "cause$iv$iv", "$this$consume$iv", "e$iv", "element1", "$this$produce", "otherIterator", "$this$consumeEach$iv", "$this$consume$iv$iv", "cause$iv$iv", "$this$consume$iv", "e$iv", "element1", "element2"}, s = {"L$0", "L$1", "L$2", "L$4", "L$5", "L$6", "L$0", "L$1", "L$2", "L$4", "L$5", "L$6", "L$8", "L$9", "L$0", "L$1", "L$2", "L$4", "L$5", "L$6", "L$8", "L$9", "L$10"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$zip$2 extends SuspendLambda implements Function2<ProducerScope<? super V>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $other;
    final /* synthetic */ ReceiveChannel $this_zip;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$10;
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

    ChannelsKt__Channels_commonKt$zip$2(ReceiveChannel receiveChannel, ReceiveChannel receiveChannel2, Function2 function2, Continuation continuation) {
        this.$this_zip = receiveChannel;
        this.$other = receiveChannel2;
        this.$transform = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$2 = new ChannelsKt__Channels_commonKt$zip$2(this.$this_zip, this.$other, this.$transform, continuation);
        channelsKt__Channels_commonKt$zip$2.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$zip$2;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$zip$2) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00df A[Catch:{ all -> 0x0146 }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0111 A[Catch:{ all -> 0x0146 }] */
    public final Object invokeSuspend(Object obj) {
        ReceiveChannel receiveChannel;
        Throwable th;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        ReceiveChannel receiveChannel2;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$2;
        Throwable th2;
        ReceiveChannel receiveChannel3;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$22;
        ChannelIterator channelIterator2;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$23;
        Object obj2;
        Object obj3;
        ChannelIterator channelIterator3;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$24;
        Throwable th3;
        Object obj4;
        Object obj5;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        int i2 = 2;
        int i3 = 1;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            ProducerScope producerScope2 = this.p$;
            ChannelIterator it = this.$other.iterator();
            receiveChannel = this.$this_zip;
            th2 = null;
            channelsKt__Channels_commonKt$zip$23 = this;
            producerScope = producerScope2;
            channelIterator = it;
            receiveChannel3 = receiveChannel;
            receiveChannel2 = receiveChannel3;
            channelIterator2 = receiveChannel.iterator();
            channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$23;
        } else if (i == 1) {
            channelIterator2 = (ChannelIterator) this.L$7;
            receiveChannel3 = (ReceiveChannel) this.L$6;
            th2 = (Throwable) this.L$5;
            receiveChannel = (ReceiveChannel) this.L$4;
            channelsKt__Channels_commonKt$zip$2 = (ChannelsKt__Channels_commonKt$zip$2) this.L$3;
            receiveChannel2 = (ReceiveChannel) this.L$2;
            channelIterator = (ChannelIterator) this.L$1;
            producerScope = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            obj5 = obj;
            channelsKt__Channels_commonKt$zip$23 = this;
            if (!((Boolean) obj5).booleanValue()) {
                obj3 = channelIterator2.next();
                channelsKt__Channels_commonKt$zip$23.L$0 = producerScope;
                channelsKt__Channels_commonKt$zip$23.L$1 = channelIterator;
                channelsKt__Channels_commonKt$zip$23.L$2 = receiveChannel2;
                channelsKt__Channels_commonKt$zip$23.L$3 = channelsKt__Channels_commonKt$zip$2;
                channelsKt__Channels_commonKt$zip$23.L$4 = receiveChannel;
                channelsKt__Channels_commonKt$zip$23.L$5 = th2;
                channelsKt__Channels_commonKt$zip$23.L$6 = receiveChannel3;
                channelsKt__Channels_commonKt$zip$23.L$7 = channelIterator2;
                channelsKt__Channels_commonKt$zip$23.L$8 = obj3;
                channelsKt__Channels_commonKt$zip$23.L$9 = obj3;
                channelsKt__Channels_commonKt$zip$23.label = i2;
                obj2 = channelIterator.hasNext(channelsKt__Channels_commonKt$zip$23);
                if (obj2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$23;
                channelIterator3 = channelIterator;
                channelsKt__Channels_commonKt$zip$24 = channelsKt__Channels_commonKt$zip$2;
                th3 = th2;
                obj4 = obj3;
                if (((Boolean) obj2).booleanValue()) {
                }
                th2 = th3;
                channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$24;
                channelIterator = channelIterator3;
                channelsKt__Channels_commonKt$zip$23 = channelsKt__Channels_commonKt$zip$22;
                i2 = 2;
                i3 = 1;
                return coroutine_suspended;
            }
            Unit unit = Unit.INSTANCE;
            ChannelsKt.cancelConsumed(receiveChannel, th2);
            return Unit.INSTANCE;
        } else if (i == 2) {
            Object obj6 = this.L$9;
            Object obj7 = this.L$8;
            ChannelIterator channelIterator4 = (ChannelIterator) this.L$7;
            ReceiveChannel receiveChannel4 = (ReceiveChannel) this.L$6;
            th3 = (Throwable) this.L$5;
            ReceiveChannel receiveChannel5 = (ReceiveChannel) this.L$4;
            channelsKt__Channels_commonKt$zip$24 = (ChannelsKt__Channels_commonKt$zip$2) this.L$3;
            ReceiveChannel receiveChannel6 = (ReceiveChannel) this.L$2;
            channelIterator3 = (ChannelIterator) this.L$1;
            ProducerScope producerScope3 = (ProducerScope) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                obj2 = obj;
                channelsKt__Channels_commonKt$zip$22 = this;
                ProducerScope producerScope4 = producerScope3;
                obj3 = obj6;
                channelIterator2 = channelIterator4;
                obj4 = obj7;
                receiveChannel3 = receiveChannel4;
                receiveChannel = receiveChannel5;
                receiveChannel2 = receiveChannel6;
                producerScope = producerScope4;
                if (((Boolean) obj2).booleanValue()) {
                    Object next = channelIterator3.next();
                    Object invoke = channelsKt__Channels_commonKt$zip$22.$transform.invoke(obj3, next);
                    channelsKt__Channels_commonKt$zip$22.L$0 = producerScope;
                    channelsKt__Channels_commonKt$zip$22.L$1 = channelIterator3;
                    channelsKt__Channels_commonKt$zip$22.L$2 = receiveChannel2;
                    channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$24;
                    channelsKt__Channels_commonKt$zip$22.L$4 = receiveChannel;
                    channelsKt__Channels_commonKt$zip$22.L$5 = th3;
                    channelsKt__Channels_commonKt$zip$22.L$6 = receiveChannel3;
                    channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator2;
                    channelsKt__Channels_commonKt$zip$22.L$8 = obj4;
                    channelsKt__Channels_commonKt$zip$22.L$9 = obj3;
                    channelsKt__Channels_commonKt$zip$22.L$10 = next;
                    channelsKt__Channels_commonKt$zip$22.label = 3;
                    if (producerScope.send(invoke, channelsKt__Channels_commonKt$zip$22) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                th2 = th3;
                channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$24;
                channelIterator = channelIterator3;
                channelsKt__Channels_commonKt$zip$23 = channelsKt__Channels_commonKt$zip$22;
                i2 = 2;
                i3 = 1;
            } catch (Throwable th4) {
                th = th4;
                receiveChannel = receiveChannel5;
                throw th;
            }
        } else if (i == 3) {
            channelIterator2 = (ChannelIterator) this.L$7;
            receiveChannel3 = (ReceiveChannel) this.L$6;
            th2 = (Throwable) this.L$5;
            receiveChannel = (ReceiveChannel) this.L$4;
            channelsKt__Channels_commonKt$zip$2 = (ChannelsKt__Channels_commonKt$zip$2) this.L$3;
            receiveChannel2 = (ReceiveChannel) this.L$2;
            channelIterator = (ChannelIterator) this.L$1;
            producerScope = (ProducerScope) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                channelsKt__Channels_commonKt$zip$22 = this;
                channelsKt__Channels_commonKt$zip$23 = channelsKt__Channels_commonKt$zip$22;
                i2 = 2;
                i3 = 1;
            } catch (Throwable th5) {
                th = th5;
                try {
                    throw th;
                } catch (Throwable th6) {
                    Throwable th7 = th6;
                    ChannelsKt.cancelConsumed(receiveChannel, th);
                    throw th7;
                }
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        channelsKt__Channels_commonKt$zip$23.L$0 = producerScope;
        channelsKt__Channels_commonKt$zip$23.L$1 = channelIterator;
        channelsKt__Channels_commonKt$zip$23.L$2 = receiveChannel2;
        channelsKt__Channels_commonKt$zip$23.L$3 = channelsKt__Channels_commonKt$zip$2;
        channelsKt__Channels_commonKt$zip$23.L$4 = receiveChannel;
        channelsKt__Channels_commonKt$zip$23.L$5 = th2;
        channelsKt__Channels_commonKt$zip$23.L$6 = receiveChannel3;
        channelsKt__Channels_commonKt$zip$23.L$7 = channelIterator2;
        channelsKt__Channels_commonKt$zip$23.label = i3;
        obj5 = channelIterator2.hasNext(channelsKt__Channels_commonKt$zip$2);
        if (obj5 == coroutine_suspended) {
            return coroutine_suspended;
        }
        if (!((Boolean) obj5).booleanValue()) {
            Unit unit2 = Unit.INSTANCE;
        }
        Unit unit22 = Unit.INSTANCE;
        ChannelsKt.cancelConsumed(receiveChannel, th2);
        return Unit.INSTANCE;
    }
}
