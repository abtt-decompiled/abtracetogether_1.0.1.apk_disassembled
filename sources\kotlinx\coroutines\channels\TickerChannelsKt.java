package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.EventLoop_commonKt;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.TimeSource;
import kotlinx.coroutines.TimeSourceKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a/\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a/\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a4\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, d2 = {"fixedDelayTicker", "", "delayMillis", "", "initialDelayMillis", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "(JJLkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fixedPeriodTicker", "ticker", "Lkotlinx/coroutines/channels/ReceiveChannel;", "context", "Lkotlin/coroutines/CoroutineContext;", "mode", "Lkotlinx/coroutines/channels/TickerMode;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 15})
/* compiled from: TickerChannels.kt */
public final class TickerChannelsKt {

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 15})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[TickerMode.values().length];
            $EnumSwitchMapping$0 = iArr;
            iArr[TickerMode.FIXED_PERIOD.ordinal()] = 1;
            $EnumSwitchMapping$0[TickerMode.FIXED_DELAY.ordinal()] = 2;
        }
    }

    public static /* synthetic */ ReceiveChannel ticker$default(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode, int i, Object obj) {
        if ((i & 2) != 0) {
            j2 = j;
        }
        if ((i & 4) != 0) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 8) != 0) {
            tickerMode = TickerMode.FIXED_PERIOD;
        }
        return ticker(j, j2, coroutineContext, tickerMode);
    }

    public static final ReceiveChannel<Unit> ticker(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode) {
        long j3 = j;
        long j4 = j2;
        CoroutineContext coroutineContext2 = coroutineContext;
        Intrinsics.checkParameterIsNotNull(coroutineContext2, "context");
        Intrinsics.checkParameterIsNotNull(tickerMode, "mode");
        boolean z = true;
        String str = " ms";
        if (j3 >= 0) {
            if (j4 < 0) {
                z = false;
            }
            if (z) {
                CoroutineScope coroutineScope = GlobalScope.INSTANCE;
                CoroutineContext plus = Dispatchers.getUnconfined().plus(coroutineContext2);
                TickerChannelsKt$ticker$3 tickerChannelsKt$ticker$3 = new TickerChannelsKt$ticker$3(tickerMode, j, j2, null);
                return ProduceKt.produce(coroutineScope, plus, 0, tickerChannelsKt$ticker$3);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Expected non-negative initial delay, but has ");
            sb.append(j2);
            sb.append(str);
            throw new IllegalArgumentException(sb.toString().toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Expected non-negative delay, but has ");
        sb2.append(j);
        sb2.append(str);
        throw new IllegalArgumentException(sb2.toString().toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00c3 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00df A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00ed  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0110  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0174 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0175  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002b  */
    static final /* synthetic */ Object fixedPeriodTicker(long j, long j2, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) {
        TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$1;
        Object coroutine_suspended;
        int i;
        long j3;
        long j4;
        long j5;
        SendChannel<? super Unit> sendChannel2;
        char c;
        long j6;
        long j7;
        long j8;
        SendChannel<? super Unit> sendChannel3;
        long j9;
        long j10;
        TimeSource timeSource;
        long j11;
        long coerceAtLeast;
        TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$12;
        long delayNanosToMillis;
        Unit unit;
        long j12;
        long j13 = j2;
        Continuation<? super Unit> continuation2 = continuation;
        if (continuation2 instanceof TickerChannelsKt$fixedPeriodTicker$1) {
            tickerChannelsKt$fixedPeriodTicker$1 = (TickerChannelsKt$fixedPeriodTicker$1) continuation2;
            if ((tickerChannelsKt$fixedPeriodTicker$1.label & Integer.MIN_VALUE) != 0) {
                tickerChannelsKt$fixedPeriodTicker$1.label -= Integer.MIN_VALUE;
                Object obj = tickerChannelsKt$fixedPeriodTicker$1.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = tickerChannelsKt$fixedPeriodTicker$1.label;
                int i2 = 2;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    TimeSource timeSource2 = TimeSourceKt.getTimeSource();
                    if (timeSource2 != null) {
                        Long boxLong = Boxing.boxLong(timeSource2.nanoTime());
                        if (boxLong != null) {
                            j12 = boxLong.longValue();
                            long delayToNanos = j12 + EventLoop_commonKt.delayToNanos(j2);
                            long j14 = j;
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = j14;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = j13;
                            sendChannel3 = sendChannel;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel3;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = delayToNanos;
                            tickerChannelsKt$fixedPeriodTicker$1.label = 1;
                            if (DelayKt.delay(j13, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            j5 = delayToNanos;
                            j7 = j14;
                        }
                    }
                    j12 = System.nanoTime();
                    long delayToNanos2 = j12 + EventLoop_commonKt.delayToNanos(j2);
                    long j142 = j;
                    tickerChannelsKt$fixedPeriodTicker$1.J$0 = j142;
                    tickerChannelsKt$fixedPeriodTicker$1.J$1 = j13;
                    sendChannel3 = sendChannel;
                    tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel3;
                    tickerChannelsKt$fixedPeriodTicker$1.J$2 = delayToNanos2;
                    tickerChannelsKt$fixedPeriodTicker$1.label = 1;
                    if (DelayKt.delay(j13, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                    }
                } else if (i == 1) {
                    long j15 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                    SendChannel<? super Unit> sendChannel4 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                    long j16 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                    j7 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                    ResultKt.throwOnFailure(obj);
                    sendChannel3 = sendChannel4;
                    long j17 = j15;
                    j13 = j16;
                    j5 = j17;
                } else if (i == 2) {
                    j6 = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                    j10 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                    sendChannel2 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                    j4 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                    j3 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                    ResultKt.throwOnFailure(obj);
                    timeSource = TimeSourceKt.getTimeSource();
                    if (timeSource != null) {
                    }
                    j11 = System.nanoTime();
                    long j18 = j11;
                    TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$13 = tickerChannelsKt$fixedPeriodTicker$1;
                    long j19 = j10;
                    coerceAtLeast = RangesKt.coerceAtLeast(j10 - j18, 0);
                    if (coerceAtLeast == 0) {
                    }
                    tickerChannelsKt$fixedPeriodTicker$12 = tickerChannelsKt$fixedPeriodTicker$13;
                    long j20 = coerceAtLeast;
                    long j21 = j18;
                    long j22 = j20;
                    delayNanosToMillis = EventLoop_commonKt.delayNanosToMillis(j22);
                    tickerChannelsKt$fixedPeriodTicker$12.J$0 = j3;
                    tickerChannelsKt$fixedPeriodTicker$12.J$1 = j4;
                    tickerChannelsKt$fixedPeriodTicker$12.L$0 = sendChannel2;
                    long j23 = j4;
                    long j24 = j19;
                    tickerChannelsKt$fixedPeriodTicker$12.J$2 = j24;
                    tickerChannelsKt$fixedPeriodTicker$12.J$3 = j6;
                    tickerChannelsKt$fixedPeriodTicker$12.J$4 = j21;
                    tickerChannelsKt$fixedPeriodTicker$12.J$5 = j22;
                    c = 4;
                    tickerChannelsKt$fixedPeriodTicker$12.label = 4;
                    if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$12) != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                } else if (i == 3) {
                    long j25 = tickerChannelsKt$fixedPeriodTicker$1.J$6;
                    long j26 = tickerChannelsKt$fixedPeriodTicker$1.J$5;
                    long j27 = tickerChannelsKt$fixedPeriodTicker$1.J$4;
                    j6 = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                    j9 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                    sendChannel2 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                    j4 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                    j3 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                    ResultKt.throwOnFailure(obj);
                    c = 4;
                    long j28 = j4;
                    j7 = j3;
                    j8 = j6;
                    j13 = j28;
                    char c2 = c;
                    sendChannel3 = sendChannel2;
                    i2 = 2;
                    j10 = j5 + j8;
                    unit = Unit.INSTANCE;
                    tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                    tickerChannelsKt$fixedPeriodTicker$1.J$1 = j13;
                    tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel3;
                    tickerChannelsKt$fixedPeriodTicker$1.J$2 = j10;
                    tickerChannelsKt$fixedPeriodTicker$1.J$3 = j8;
                    tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                    if (sendChannel3.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                } else if (i == 4) {
                    long j29 = tickerChannelsKt$fixedPeriodTicker$1.J$5;
                    long j30 = tickerChannelsKt$fixedPeriodTicker$1.J$4;
                    j6 = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                    j9 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                    sendChannel2 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                    j4 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                    j3 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                    ResultKt.throwOnFailure(obj);
                    c = 4;
                    long j282 = j4;
                    j7 = j3;
                    j8 = j6;
                    j13 = j282;
                    char c22 = c;
                    sendChannel3 = sendChannel2;
                    i2 = 2;
                    j10 = j5 + j8;
                    unit = Unit.INSTANCE;
                    tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                    tickerChannelsKt$fixedPeriodTicker$1.J$1 = j13;
                    tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel3;
                    tickerChannelsKt$fixedPeriodTicker$1.J$2 = j10;
                    tickerChannelsKt$fixedPeriodTicker$1.J$3 = j8;
                    tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                    if (sendChannel3.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    sendChannel2 = sendChannel3;
                    long j31 = j13;
                    j6 = j8;
                    j3 = j7;
                    j4 = j31;
                    timeSource = TimeSourceKt.getTimeSource();
                    if (timeSource != null) {
                        Long boxLong2 = Boxing.boxLong(timeSource.nanoTime());
                        if (boxLong2 != null) {
                            j11 = boxLong2.longValue();
                            long j182 = j11;
                            TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$132 = tickerChannelsKt$fixedPeriodTicker$1;
                            long j192 = j10;
                            coerceAtLeast = RangesKt.coerceAtLeast(j10 - j182, 0);
                            if (coerceAtLeast == 0 || j6 == 0) {
                                tickerChannelsKt$fixedPeriodTicker$12 = tickerChannelsKt$fixedPeriodTicker$132;
                                long j202 = coerceAtLeast;
                                long j212 = j182;
                                long j222 = j202;
                                delayNanosToMillis = EventLoop_commonKt.delayNanosToMillis(j222);
                                tickerChannelsKt$fixedPeriodTicker$12.J$0 = j3;
                                tickerChannelsKt$fixedPeriodTicker$12.J$1 = j4;
                                tickerChannelsKt$fixedPeriodTicker$12.L$0 = sendChannel2;
                                long j232 = j4;
                                long j242 = j192;
                                tickerChannelsKt$fixedPeriodTicker$12.J$2 = j242;
                                tickerChannelsKt$fixedPeriodTicker$12.J$3 = j6;
                                tickerChannelsKt$fixedPeriodTicker$12.J$4 = j212;
                                tickerChannelsKt$fixedPeriodTicker$12.J$5 = j222;
                                c = 4;
                                tickerChannelsKt$fixedPeriodTicker$12.label = 4;
                                if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$12) != coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                tickerChannelsKt$fixedPeriodTicker$1 = tickerChannelsKt$fixedPeriodTicker$12;
                                j9 = j242;
                                j4 = j232;
                                long j2822 = j4;
                                j7 = j3;
                                j8 = j6;
                                j13 = j2822;
                                char c222 = c;
                                sendChannel3 = sendChannel2;
                                i2 = 2;
                                j10 = j5 + j8;
                                unit = Unit.INSTANCE;
                                tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                                tickerChannelsKt$fixedPeriodTicker$1.J$1 = j13;
                                tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel3;
                                tickerChannelsKt$fixedPeriodTicker$1.J$2 = j10;
                                tickerChannelsKt$fixedPeriodTicker$1.J$3 = j8;
                                tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                                if (sendChannel3.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                                }
                                return coroutine_suspended;
                            }
                            long j32 = j6 - ((j182 - j192) % j6);
                            long j33 = coerceAtLeast;
                            long j34 = j182 + j32;
                            long j35 = j182;
                            long delayNanosToMillis2 = EventLoop_commonKt.delayNanosToMillis(j32);
                            TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$14 = tickerChannelsKt$fixedPeriodTicker$132;
                            tickerChannelsKt$fixedPeriodTicker$14.J$0 = j3;
                            tickerChannelsKt$fixedPeriodTicker$14.J$1 = j4;
                            tickerChannelsKt$fixedPeriodTicker$14.L$0 = sendChannel2;
                            tickerChannelsKt$fixedPeriodTicker$14.J$2 = j34;
                            tickerChannelsKt$fixedPeriodTicker$14.J$3 = j6;
                            long j36 = j34;
                            tickerChannelsKt$fixedPeriodTicker$14.J$4 = j35;
                            tickerChannelsKt$fixedPeriodTicker$14.J$5 = j33;
                            tickerChannelsKt$fixedPeriodTicker$14.J$6 = j32;
                            tickerChannelsKt$fixedPeriodTicker$14.label = 3;
                            if (DelayKt.delay(delayNanosToMillis2, tickerChannelsKt$fixedPeriodTicker$14) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            tickerChannelsKt$fixedPeriodTicker$1 = tickerChannelsKt$fixedPeriodTicker$14;
                            j9 = j36;
                            c = 4;
                            long j28222 = j4;
                            j7 = j3;
                            j8 = j6;
                            j13 = j28222;
                            char c2222 = c;
                            sendChannel3 = sendChannel2;
                            i2 = 2;
                            j10 = j5 + j8;
                            unit = Unit.INSTANCE;
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = j13;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel3;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = j10;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = j8;
                            tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                            if (sendChannel3.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                            }
                            return coroutine_suspended;
                            tickerChannelsKt$fixedPeriodTicker$12 = tickerChannelsKt$fixedPeriodTicker$132;
                            long j2022 = coerceAtLeast;
                            long j2122 = j182;
                            long j2222 = j2022;
                            delayNanosToMillis = EventLoop_commonKt.delayNanosToMillis(j2222);
                            tickerChannelsKt$fixedPeriodTicker$12.J$0 = j3;
                            tickerChannelsKt$fixedPeriodTicker$12.J$1 = j4;
                            tickerChannelsKt$fixedPeriodTicker$12.L$0 = sendChannel2;
                            long j2322 = j4;
                            long j2422 = j192;
                            tickerChannelsKt$fixedPeriodTicker$12.J$2 = j2422;
                            tickerChannelsKt$fixedPeriodTicker$12.J$3 = j6;
                            tickerChannelsKt$fixedPeriodTicker$12.J$4 = j2122;
                            tickerChannelsKt$fixedPeriodTicker$12.J$5 = j2222;
                            c = 4;
                            tickerChannelsKt$fixedPeriodTicker$12.label = 4;
                            if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$12) != coroutine_suspended) {
                            }
                            return coroutine_suspended;
                        }
                    }
                    j11 = System.nanoTime();
                    long j1822 = j11;
                    TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$1322 = tickerChannelsKt$fixedPeriodTicker$1;
                    long j1922 = j10;
                    coerceAtLeast = RangesKt.coerceAtLeast(j10 - j1822, 0);
                    if (coerceAtLeast == 0) {
                    }
                    tickerChannelsKt$fixedPeriodTicker$12 = tickerChannelsKt$fixedPeriodTicker$1322;
                    long j20222 = coerceAtLeast;
                    long j21222 = j1822;
                    long j22222 = j20222;
                    delayNanosToMillis = EventLoop_commonKt.delayNanosToMillis(j22222);
                    tickerChannelsKt$fixedPeriodTicker$12.J$0 = j3;
                    tickerChannelsKt$fixedPeriodTicker$12.J$1 = j4;
                    tickerChannelsKt$fixedPeriodTicker$12.L$0 = sendChannel2;
                    long j23222 = j4;
                    long j24222 = j1922;
                    tickerChannelsKt$fixedPeriodTicker$12.J$2 = j24222;
                    tickerChannelsKt$fixedPeriodTicker$12.J$3 = j6;
                    tickerChannelsKt$fixedPeriodTicker$12.J$4 = j21222;
                    tickerChannelsKt$fixedPeriodTicker$12.J$5 = j22222;
                    c = 4;
                    tickerChannelsKt$fixedPeriodTicker$12.label = 4;
                    if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$12) != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                    return coroutine_suspended;
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                j8 = EventLoop_commonKt.delayToNanos(j7);
                j10 = j5 + j8;
                unit = Unit.INSTANCE;
                tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                tickerChannelsKt$fixedPeriodTicker$1.J$1 = j13;
                tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel3;
                tickerChannelsKt$fixedPeriodTicker$1.J$2 = j10;
                tickerChannelsKt$fixedPeriodTicker$1.J$3 = j8;
                tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                if (sendChannel3.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                }
                return coroutine_suspended;
            }
        }
        tickerChannelsKt$fixedPeriodTicker$1 = new TickerChannelsKt$fixedPeriodTicker$1(continuation2);
        Object obj2 = tickerChannelsKt$fixedPeriodTicker$1.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = tickerChannelsKt$fixedPeriodTicker$1.label;
        int i22 = 2;
        if (i != 0) {
        }
        j8 = EventLoop_commonKt.delayToNanos(j7);
        j10 = j5 + j8;
        unit = Unit.INSTANCE;
        tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
        tickerChannelsKt$fixedPeriodTicker$1.J$1 = j13;
        tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel3;
        tickerChannelsKt$fixedPeriodTicker$1.J$2 = j10;
        tickerChannelsKt$fixedPeriodTicker$1.J$3 = j8;
        tickerChannelsKt$fixedPeriodTicker$1.label = i22;
        if (sendChannel3.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
        }
        return coroutine_suspended;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x007f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0092 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    static final /* synthetic */ Object fixedDelayTicker(long j, long j2, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) {
        TickerChannelsKt$fixedDelayTicker$1 tickerChannelsKt$fixedDelayTicker$1;
        Object coroutine_suspended;
        int i;
        long j3;
        long j4;
        SendChannel<? super Unit> sendChannel2;
        Unit unit;
        if (continuation instanceof TickerChannelsKt$fixedDelayTicker$1) {
            tickerChannelsKt$fixedDelayTicker$1 = (TickerChannelsKt$fixedDelayTicker$1) continuation;
            if ((tickerChannelsKt$fixedDelayTicker$1.label & Integer.MIN_VALUE) != 0) {
                tickerChannelsKt$fixedDelayTicker$1.label -= Integer.MIN_VALUE;
                Object obj = tickerChannelsKt$fixedDelayTicker$1.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = tickerChannelsKt$fixedDelayTicker$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
                    tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
                    tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
                    tickerChannelsKt$fixedDelayTicker$1.label = 1;
                    if (DelayKt.delay(j2, tickerChannelsKt$fixedDelayTicker$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    unit = Unit.INSTANCE;
                    tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
                    tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
                    tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
                    tickerChannelsKt$fixedDelayTicker$1.label = 2;
                    if (sendChannel.send(unit, tickerChannelsKt$fixedDelayTicker$1) != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                } else if (i == 1) {
                    sendChannel = (SendChannel) tickerChannelsKt$fixedDelayTicker$1.L$0;
                    j2 = tickerChannelsKt$fixedDelayTicker$1.J$1;
                    j = tickerChannelsKt$fixedDelayTicker$1.J$0;
                    ResultKt.throwOnFailure(obj);
                    unit = Unit.INSTANCE;
                    tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
                    tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
                    tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
                    tickerChannelsKt$fixedDelayTicker$1.label = 2;
                    if (sendChannel.send(unit, tickerChannelsKt$fixedDelayTicker$1) != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    long j5 = j;
                    sendChannel2 = sendChannel;
                    j4 = j2;
                    j3 = j5;
                    tickerChannelsKt$fixedDelayTicker$1.J$0 = j3;
                    tickerChannelsKt$fixedDelayTicker$1.J$1 = j4;
                    tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel2;
                    tickerChannelsKt$fixedDelayTicker$1.label = 3;
                    if (DelayKt.delay(j3, tickerChannelsKt$fixedDelayTicker$1) == coroutine_suspended) {
                    }
                    return coroutine_suspended;
                } else if (i == 2) {
                    sendChannel2 = (SendChannel) tickerChannelsKt$fixedDelayTicker$1.L$0;
                    j4 = tickerChannelsKt$fixedDelayTicker$1.J$1;
                    j3 = tickerChannelsKt$fixedDelayTicker$1.J$0;
                    ResultKt.throwOnFailure(obj);
                    tickerChannelsKt$fixedDelayTicker$1.J$0 = j3;
                    tickerChannelsKt$fixedDelayTicker$1.J$1 = j4;
                    tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel2;
                    tickerChannelsKt$fixedDelayTicker$1.label = 3;
                    if (DelayKt.delay(j3, tickerChannelsKt$fixedDelayTicker$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else if (i == 3) {
                    sendChannel2 = (SendChannel) tickerChannelsKt$fixedDelayTicker$1.L$0;
                    j4 = tickerChannelsKt$fixedDelayTicker$1.J$1;
                    j3 = tickerChannelsKt$fixedDelayTicker$1.J$0;
                    ResultKt.throwOnFailure(obj);
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                sendChannel = sendChannel2;
                j2 = j4;
                j = j3;
                unit = Unit.INSTANCE;
                tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
                tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
                tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
                tickerChannelsKt$fixedDelayTicker$1.label = 2;
                if (sendChannel.send(unit, tickerChannelsKt$fixedDelayTicker$1) != coroutine_suspended) {
                }
                return coroutine_suspended;
            }
        }
        tickerChannelsKt$fixedDelayTicker$1 = new TickerChannelsKt$fixedDelayTicker$1(continuation);
        Object obj2 = tickerChannelsKt$fixedDelayTicker$1.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = tickerChannelsKt$fixedDelayTicker$1.label;
        if (i != 0) {
        }
        sendChannel = sendChannel2;
        j2 = j4;
        j = j3;
        unit = Unit.INSTANCE;
        tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
        tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
        tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
        tickerChannelsKt$fixedDelayTicker$1.label = 2;
        if (sendChannel.send(unit, tickerChannelsKt$fixedDelayTicker$1) != coroutine_suspended) {
        }
        return coroutine_suspended;
    }
}
