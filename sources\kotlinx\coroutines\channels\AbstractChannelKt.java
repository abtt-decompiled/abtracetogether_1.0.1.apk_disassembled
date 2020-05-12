package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlinx.coroutines.channels.ValueOrClosed.Closed;
import kotlinx.coroutines.channels.ValueOrClosed.Companion;
import kotlinx.coroutines.internal.Symbol;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0000\u001a#\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u001a\"\u0004\b\u0000\u0010\u001b*\u0004\u0018\u00010\u0001H\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a%\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u001a\"\u0004\b\u0000\u0010\u001b*\u0006\u0012\u0002\b\u00030\u001dH\bø\u0001\u0000¢\u0006\u0002\u0010\u001e\"\u0016\u0010\u0000\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003\"\u0016\u0010\u0004\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0003\"\u0016\u0010\u0006\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0007\u0010\u0003\"\u0016\u0010\b\u001a\u00020\t8\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\n\u0010\u0003\"\u0016\u0010\u000b\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\u0003\"\u0016\u0010\r\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u000e\u0010\u0003\"\u0016\u0010\u000f\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0010\u0010\u0003\"\u000e\u0010\u0011\u001a\u00020\u0012XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0013\u001a\u00020\u0012XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0014\u001a\u00020\u0012XT¢\u0006\u0002\n\u0000\"\u0016\u0010\u0015\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0016\u0010\u0003\"\u0016\u0010\u0017\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0018\u0010\u0003*(\b\u0000\u0010\u001f\"\u0010\u0012\u0006\u0012\u0004\u0018\u00010!\u0012\u0004\u0012\u00020\"0 2\u0010\u0012\u0006\u0012\u0004\u0018\u00010!\u0012\u0004\u0012\u00020\"0 \u0002\u0004\n\u0002\b\u0019¨\u0006#"}, d2 = {"CLOSE_RESUMED", "", "CLOSE_RESUMED$annotations", "()V", "ENQUEUE_FAILED", "ENQUEUE_FAILED$annotations", "HANDLER_INVOKED", "HANDLER_INVOKED$annotations", "NULL_VALUE", "Lkotlinx/coroutines/internal/Symbol;", "NULL_VALUE$annotations", "OFFER_FAILED", "OFFER_FAILED$annotations", "OFFER_SUCCESS", "OFFER_SUCCESS$annotations", "POLL_FAILED", "POLL_FAILED$annotations", "RECEIVE_NULL_ON_CLOSE", "", "RECEIVE_RESULT", "RECEIVE_THROWS_ON_CLOSE", "SELECT_STARTED", "SELECT_STARTED$annotations", "SEND_RESUMED", "SEND_RESUMED$annotations", "toResult", "Lkotlinx/coroutines/channels/ValueOrClosed;", "E", "(Ljava/lang/Object;)Ljava/lang/Object;", "Lkotlinx/coroutines/channels/Closed;", "(Lkotlinx/coroutines/channels/Closed;)Ljava/lang/Object;", "Handler", "Lkotlin/Function1;", "", "", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 15})
/* compiled from: AbstractChannel.kt */
public final class AbstractChannelKt {
    public static final Object CLOSE_RESUMED = new Symbol("CLOSE_RESUMED");
    public static final Object ENQUEUE_FAILED = new Symbol("ENQUEUE_FAILED");
    public static final Object HANDLER_INVOKED = new Symbol("ON_CLOSE_HANDLER_INVOKED");
    public static final Symbol NULL_VALUE = new Symbol("NULL_VALUE");
    public static final Object OFFER_FAILED = new Symbol("OFFER_FAILED");
    public static final Object OFFER_SUCCESS = new Symbol("OFFER_SUCCESS");
    public static final Object POLL_FAILED = new Symbol("POLL_FAILED");
    public static final int RECEIVE_NULL_ON_CLOSE = 1;
    public static final int RECEIVE_RESULT = 2;
    public static final int RECEIVE_THROWS_ON_CLOSE = 0;
    public static final Object SELECT_STARTED = new Symbol("SELECT_STARTED");
    public static final Object SEND_RESUMED = new Symbol("SEND_RESUMED");

    public static /* synthetic */ void CLOSE_RESUMED$annotations() {
    }

    public static /* synthetic */ void ENQUEUE_FAILED$annotations() {
    }

    public static /* synthetic */ void HANDLER_INVOKED$annotations() {
    }

    public static /* synthetic */ void NULL_VALUE$annotations() {
    }

    public static /* synthetic */ void OFFER_FAILED$annotations() {
    }

    public static /* synthetic */ void OFFER_SUCCESS$annotations() {
    }

    public static /* synthetic */ void POLL_FAILED$annotations() {
    }

    public static /* synthetic */ void SELECT_STARTED$annotations() {
    }

    public static /* synthetic */ void SEND_RESUMED$annotations() {
    }

    /* access modifiers changed from: private */
    public static final <E> Object toResult(Object obj) {
        if (obj instanceof Closed) {
            Companion companion = ValueOrClosed.Companion;
            return ValueOrClosed.m980constructorimpl(new Closed(((Closed) obj).closeCause));
        }
        Companion companion2 = ValueOrClosed.Companion;
        return ValueOrClosed.m980constructorimpl(obj);
    }

    /* access modifiers changed from: private */
    public static final <E> Object toResult(Closed<?> closed) {
        Companion companion = ValueOrClosed.Companion;
        return ValueOrClosed.m980constructorimpl(new Closed(closed.closeCause));
    }
}
