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

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$dropWhile$1", f = "Channels.common.kt", i = {0, 1, 1, 2, 2, 3, 4, 4}, l = {725, 726, 727, 731, 732}, m = "invokeSuspend", n = {"$this$produce", "$this$produce", "e", "$this$produce", "e", "$this$produce", "$this$produce", "e"}, s = {"L$0", "L$0", "L$1", "L$0", "L$1", "L$0", "L$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$dropWhile$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $predicate;
    final /* synthetic */ ReceiveChannel $this_dropWhile;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$dropWhile$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_dropWhile = receiveChannel;
        this.$predicate = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$1 = new ChannelsKt__Channels_commonKt$dropWhile$1(this.$this_dropWhile, this.$predicate, continuation);
        channelsKt__Channels_commonKt$dropWhile$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$dropWhile$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$dropWhile$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00c3 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00db A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00e7  */
    public final Object invokeSuspend(Object obj) {
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        Object obj2;
        ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$1;
        ChannelIterator channelIterator2;
        Object hasNext;
        Object obj3;
        ChannelIterator channelIterator3;
        ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$12;
        ChannelIterator channelIterator4;
        ProducerScope producerScope2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            ProducerScope producerScope3 = this.p$;
            channelIterator4 = this.$this_dropWhile.iterator();
            producerScope2 = producerScope3;
            channelsKt__Channels_commonKt$dropWhile$12 = this;
        } else if (i == 1) {
            ChannelIterator channelIterator5 = (ChannelIterator) this.L$1;
            ProducerScope producerScope4 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            producerScope = producerScope4;
            channelIterator3 = channelIterator5;
            obj2 = coroutine_suspended;
            channelsKt__Channels_commonKt$dropWhile$1 = this;
            if (((Boolean) obj).booleanValue()) {
                Object next = channelIterator3.next();
                Function2 function2 = channelsKt__Channels_commonKt$dropWhile$1.$predicate;
                channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
                channelsKt__Channels_commonKt$dropWhile$1.L$1 = next;
                channelsKt__Channels_commonKt$dropWhile$1.L$2 = channelIterator3;
                channelsKt__Channels_commonKt$dropWhile$1.label = 2;
                Object invoke = function2.invoke(next, channelsKt__Channels_commonKt$dropWhile$1);
                if (invoke == obj2) {
                    return obj2;
                }
                Object obj4 = invoke;
                obj3 = next;
                obj = obj4;
                if (((Boolean) obj).booleanValue()) {
                }
                channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
                channelsKt__Channels_commonKt$dropWhile$1.L$1 = obj3;
                channelsKt__Channels_commonKt$dropWhile$1.label = 3;
                if (producerScope.send(obj3, channelsKt__Channels_commonKt$dropWhile$1) == obj2) {
                }
                return obj2;
            }
            channelIterator2 = channelsKt__Channels_commonKt$dropWhile$1.$this_dropWhile.iterator();
            channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
            channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$dropWhile$1.label = 4;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
            if (hasNext != obj2) {
            }
            return obj2;
        } else if (i == 2) {
            ChannelIterator channelIterator6 = (ChannelIterator) this.L$2;
            Object obj5 = this.L$1;
            producerScope = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            obj3 = obj5;
            channelIterator3 = channelIterator6;
            obj2 = coroutine_suspended;
            channelsKt__Channels_commonKt$dropWhile$1 = this;
            if (((Boolean) obj).booleanValue()) {
                channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
                channelsKt__Channels_commonKt$dropWhile$1.L$1 = obj3;
                channelsKt__Channels_commonKt$dropWhile$1.label = 3;
            } else {
                channelsKt__Channels_commonKt$dropWhile$12 = channelsKt__Channels_commonKt$dropWhile$1;
                coroutine_suspended = obj2;
                channelIterator4 = channelIterator3;
                producerScope2 = producerScope;
            }
            channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
            channelsKt__Channels_commonKt$dropWhile$1.L$1 = obj3;
            channelsKt__Channels_commonKt$dropWhile$1.label = 3;
            if (producerScope.send(obj3, channelsKt__Channels_commonKt$dropWhile$1) == obj2) {
                return obj2;
            }
            channelIterator2 = channelsKt__Channels_commonKt$dropWhile$1.$this_dropWhile.iterator();
            channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
            channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$dropWhile$1.label = 4;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
            if (hasNext != obj2) {
            }
            return obj2;
        } else if (i == 3) {
            ProducerScope producerScope5 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            producerScope = producerScope5;
            obj2 = coroutine_suspended;
            channelsKt__Channels_commonKt$dropWhile$1 = this;
            channelIterator2 = channelsKt__Channels_commonKt$dropWhile$1.$this_dropWhile.iterator();
            channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
            channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$dropWhile$1.label = 4;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
            if (hasNext != obj2) {
            }
            return obj2;
        } else if (i == 4) {
            ChannelIterator channelIterator7 = (ChannelIterator) this.L$1;
            ProducerScope producerScope6 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            producerScope = producerScope6;
            channelIterator = channelIterator7;
            obj2 = coroutine_suspended;
            channelsKt__Channels_commonKt$dropWhile$1 = this;
            if (!((Boolean) obj).booleanValue()) {
            }
            return Unit.INSTANCE;
        } else if (i == 5) {
            ChannelIterator channelIterator8 = (ChannelIterator) this.L$2;
            ProducerScope producerScope7 = (ProducerScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            channelIterator2 = channelIterator8;
            producerScope = producerScope7;
            obj2 = coroutine_suspended;
            channelsKt__Channels_commonKt$dropWhile$1 = this;
            channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
            channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$dropWhile$1.label = 4;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
            if (hasNext != obj2) {
                return obj2;
            }
            Object obj6 = hasNext;
            channelIterator = channelIterator2;
            obj = obj6;
            if (!((Boolean) obj).booleanValue()) {
                Object next2 = channelIterator.next();
                channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
                channelsKt__Channels_commonKt$dropWhile$1.L$1 = next2;
                channelsKt__Channels_commonKt$dropWhile$1.L$2 = channelIterator;
                channelsKt__Channels_commonKt$dropWhile$1.label = 5;
                if (producerScope.send(next2, channelsKt__Channels_commonKt$dropWhile$1) == obj2) {
                    return obj2;
                }
                channelIterator2 = channelIterator;
                channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
                channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
                channelsKt__Channels_commonKt$dropWhile$1.label = 4;
                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
                if (hasNext != obj2) {
                }
                return obj2;
            }
            return Unit.INSTANCE;
            return obj2;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        channelsKt__Channels_commonKt$dropWhile$12.L$0 = producerScope2;
        channelsKt__Channels_commonKt$dropWhile$12.L$1 = channelIterator4;
        channelsKt__Channels_commonKt$dropWhile$12.label = 1;
        Object hasNext2 = channelIterator4.hasNext(channelsKt__Channels_commonKt$dropWhile$12);
        if (hasNext2 == coroutine_suspended) {
            return coroutine_suspended;
        }
        Object obj7 = coroutine_suspended;
        channelsKt__Channels_commonKt$dropWhile$1 = channelsKt__Channels_commonKt$dropWhile$12;
        obj = hasNext2;
        producerScope = producerScope2;
        channelIterator3 = channelIterator4;
        obj2 = obj7;
        if (((Boolean) obj).booleanValue()) {
        }
        channelIterator2 = channelsKt__Channels_commonKt$dropWhile$1.$this_dropWhile.iterator();
        channelsKt__Channels_commonKt$dropWhile$1.L$0 = producerScope;
        channelsKt__Channels_commonKt$dropWhile$1.L$1 = channelIterator2;
        channelsKt__Channels_commonKt$dropWhile$1.label = 4;
        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$dropWhile$1);
        if (hasNext != obj2) {
        }
        return obj2;
        return coroutine_suspended;
    }
}
