package kotlin.time;

import com.worklight.wlclient.api.WLConstants;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b&\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0012\b@\u0018\u0000 s2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001sB\u0014\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010%\u001a\u00020\t2\u0006\u0010&\u001a\u00020\u0000H\u0002ø\u0001\u0000¢\u0006\u0004\b'\u0010(J\u001b\u0010)\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\u0003H\u0002ø\u0001\u0000¢\u0006\u0004\b+\u0010,J\u001b\u0010)\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\tH\u0002ø\u0001\u0000¢\u0006\u0004\b+\u0010-J\u001b\u0010)\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u0000H\u0002ø\u0001\u0000¢\u0006\u0004\b.\u0010,J\u0013\u0010/\u001a\u0002002\b\u0010&\u001a\u0004\u0018\u000101HÖ\u0003J\t\u00102\u001a\u00020\tHÖ\u0001J\r\u00103\u001a\u000200¢\u0006\u0004\b4\u00105J\r\u00106\u001a\u000200¢\u0006\u0004\b7\u00105J\r\u00108\u001a\u000200¢\u0006\u0004\b9\u00105J\r\u0010:\u001a\u000200¢\u0006\u0004\b;\u00105J\u001b\u0010<\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0000H\u0002ø\u0001\u0000¢\u0006\u0004\b=\u0010,J\u001b\u0010>\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0000H\u0002ø\u0001\u0000¢\u0006\u0004\b?\u0010,J\u0017\u0010@\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0004\bA\u0010(J\u001b\u0010B\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\u0003H\u0002ø\u0001\u0000¢\u0006\u0004\bC\u0010,J\u001b\u0010B\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\tH\u0002ø\u0001\u0000¢\u0006\u0004\bC\u0010-J\u0001\u0010D\u001a\u0002HE\"\u0004\b\u0000\u0010E2u\u0010F\u001aq\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(J\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(K\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(L\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(N\u0012\u0004\u0012\u0002HE0GH\b¢\u0006\u0004\bO\u0010PJx\u0010D\u001a\u0002HE\"\u0004\b\u0000\u0010E2`\u0010F\u001a\\\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(K\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(L\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(N\u0012\u0004\u0012\u0002HE0QH\b¢\u0006\u0004\bO\u0010RJc\u0010D\u001a\u0002HE\"\u0004\b\u0000\u0010E2K\u0010F\u001aG\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(L\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(N\u0012\u0004\u0012\u0002HE0SH\b¢\u0006\u0004\bO\u0010TJN\u0010D\u001a\u0002HE\"\u0004\b\u0000\u0010E26\u0010F\u001a2\u0012\u0013\u0012\u00110V¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t¢\u0006\f\bH\u0012\b\bI\u0012\u0004\b\b(N\u0012\u0004\u0012\u0002HE0UH\b¢\u0006\u0004\bO\u0010WJ\u0019\u0010X\u001a\u00020\u00032\n\u0010Y\u001a\u00060Zj\u0002`[¢\u0006\u0004\b\\\u0010]J\u0019\u0010^\u001a\u00020\t2\n\u0010Y\u001a\u00060Zj\u0002`[¢\u0006\u0004\b_\u0010`J\r\u0010a\u001a\u00020b¢\u0006\u0004\bc\u0010dJ\u0019\u0010e\u001a\u00020V2\n\u0010Y\u001a\u00060Zj\u0002`[¢\u0006\u0004\bf\u0010gJ\r\u0010h\u001a\u00020V¢\u0006\u0004\bi\u0010jJ\r\u0010k\u001a\u00020V¢\u0006\u0004\bl\u0010jJ\u000f\u0010m\u001a\u00020bH\u0016¢\u0006\u0004\bn\u0010dJ#\u0010m\u001a\u00020b2\n\u0010Y\u001a\u00060Zj\u0002`[2\b\b\u0002\u0010o\u001a\u00020\t¢\u0006\u0004\bn\u0010pJ\u0013\u0010q\u001a\u00020\u0000H\u0002ø\u0001\u0000¢\u0006\u0004\br\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00008Fø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u001a\u0010\b\u001a\u00020\t8@X\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005R\u0011\u0010\u0010\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0005R\u0011\u0010\u0012\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005R\u0011\u0010\u0014\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0005R\u0011\u0010\u0016\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0005R\u0011\u0010\u0018\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005R\u0011\u0010\u001a\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0005R\u001a\u0010\u001c\u001a\u00020\t8@X\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u000b\u001a\u0004\b\u001e\u0010\rR\u001a\u0010\u001f\u001a\u00020\t8@X\u0004¢\u0006\f\u0012\u0004\b \u0010\u000b\u001a\u0004\b!\u0010\rR\u001a\u0010\"\u001a\u00020\t8@X\u0004¢\u0006\f\u0012\u0004\b#\u0010\u000b\u001a\u0004\b$\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000ø\u0001\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006t"}, d2 = {"Lkotlin/time/Duration;", "", "value", "", "constructor-impl", "(D)D", "absoluteValue", "getAbsoluteValue-impl", "hoursComponent", "", "hoursComponent$annotations", "()V", "getHoursComponent-impl", "(D)I", "inDays", "getInDays-impl", "inHours", "getInHours-impl", "inMicroseconds", "getInMicroseconds-impl", "inMilliseconds", "getInMilliseconds-impl", "inMinutes", "getInMinutes-impl", "inNanoseconds", "getInNanoseconds-impl", "inSeconds", "getInSeconds-impl", "minutesComponent", "minutesComponent$annotations", "getMinutesComponent-impl", "nanosecondsComponent", "nanosecondsComponent$annotations", "getNanosecondsComponent-impl", "secondsComponent", "secondsComponent$annotations", "getSecondsComponent-impl", "compareTo", "other", "compareTo-LRDsOJo", "(DD)I", "div", "scale", "div-impl", "(DD)D", "(DI)D", "div-LRDsOJo", "equals", "", "", "hashCode", "isFinite", "isFinite-impl", "(D)Z", "isInfinite", "isInfinite-impl", "isNegative", "isNegative-impl", "isPositive", "isPositive-impl", "minus", "minus-LRDsOJo", "plus", "plus-LRDsOJo", "precision", "precision-impl", "times", "times-impl", "toComponents", "T", "action", "Lkotlin/Function5;", "Lkotlin/ParameterName;", "name", "days", "hours", "minutes", "seconds", "nanoseconds", "toComponents-impl", "(DLkotlin/jvm/functions/Function5;)Ljava/lang/Object;", "Lkotlin/Function4;", "(DLkotlin/jvm/functions/Function4;)Ljava/lang/Object;", "Lkotlin/Function3;", "(DLkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "Lkotlin/Function2;", "", "(DLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "toDouble", "unit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "toDouble-impl", "(DLjava/util/concurrent/TimeUnit;)D", "toInt", "toInt-impl", "(DLjava/util/concurrent/TimeUnit;)I", "toIsoString", "", "toIsoString-impl", "(D)Ljava/lang/String;", "toLong", "toLong-impl", "(DLjava/util/concurrent/TimeUnit;)J", "toLongMilliseconds", "toLongMilliseconds-impl", "(D)J", "toLongNanoseconds", "toLongNanoseconds-impl", "toString", "toString-impl", "decimals", "(DLjava/util/concurrent/TimeUnit;I)Ljava/lang/String;", "unaryMinus", "unaryMinus-impl", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: Duration.kt */
public final class Duration implements Comparable<Duration> {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static final double INFINITE = m928constructorimpl(DoubleCompanionObject.INSTANCE.getPOSITIVE_INFINITY());
    /* access modifiers changed from: private */
    public static final double ZERO = m928constructorimpl(0.0d);
    private final double value;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J&\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\n\u0010\u0010\u001a\u00060\u000ej\u0002`\u000fR\u0016\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006R\u0016\u0010\b\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\t\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011"}, d2 = {"Lkotlin/time/Duration$Companion;", "", "()V", "INFINITE", "Lkotlin/time/Duration;", "getINFINITE", "()D", "D", "ZERO", "getZERO", "convert", "", "value", "sourceUnit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "targetUnit", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    /* compiled from: Duration.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final double getZERO() {
            return Duration.ZERO;
        }

        public final double getINFINITE() {
            return Duration.INFINITE;
        }

        public final double convert(double d, TimeUnit timeUnit, TimeUnit timeUnit2) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "sourceUnit");
            Intrinsics.checkParameterIsNotNull(timeUnit2, "targetUnit");
            return DurationUnitKt.convertDurationUnit(d, timeUnit, timeUnit2);
        }
    }

    /* renamed from: box-impl reason: not valid java name */
    public static final /* synthetic */ Duration m926boximpl(double d) {
        return new Duration(d);
    }

    /* renamed from: constructor-impl reason: not valid java name */
    public static double m928constructorimpl(double d) {
        return d;
    }

    /* renamed from: div-LRDsOJo reason: not valid java name */
    public static final double m929divLRDsOJo(double d, double d2) {
        return d / d2;
    }

    /* renamed from: equals-impl reason: not valid java name */
    public static boolean m932equalsimpl(double d, Object obj) {
        return (obj instanceof Duration) && Double.compare(d, ((Duration) obj).m971unboximpl()) == 0;
    }

    /* renamed from: equals-impl0 reason: not valid java name */
    public static final boolean m933equalsimpl0(double d, double d2) {
        throw null;
    }

    /* renamed from: hashCode-impl reason: not valid java name */
    public static int m946hashCodeimpl(double d) {
        long doubleToLongBits = Double.doubleToLongBits(d);
        return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
    }

    public static /* synthetic */ void hoursComponent$annotations() {
    }

    /* renamed from: isNegative-impl reason: not valid java name */
    public static final boolean m949isNegativeimpl(double d) {
        return d < ((double) 0);
    }

    /* renamed from: isPositive-impl reason: not valid java name */
    public static final boolean m950isPositiveimpl(double d) {
        return d > ((double) 0);
    }

    public static /* synthetic */ void minutesComponent$annotations() {
    }

    public static /* synthetic */ void nanosecondsComponent$annotations() {
    }

    /* renamed from: precision-impl reason: not valid java name */
    private static final int m953precisionimpl(double d, double d2) {
        if (d2 < ((double) 1)) {
            return 3;
        }
        if (d2 < ((double) 10)) {
            return 2;
        }
        return d2 < ((double) 100) ? 1 : 0;
    }

    public static /* synthetic */ void secondsComponent$annotations() {
    }

    /* renamed from: compareTo-LRDsOJo reason: not valid java name */
    public int m970compareToLRDsOJo(double d) {
        return m927compareToLRDsOJo(this.value, d);
    }

    public boolean equals(Object obj) {
        return m932equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m946hashCodeimpl(this.value);
    }

    public String toString() {
        return m966toStringimpl(this.value);
    }

    /* renamed from: unbox-impl reason: not valid java name */
    public final /* synthetic */ double m971unboximpl() {
        return this.value;
    }

    private /* synthetic */ Duration(double d) {
        this.value = d;
    }

    public /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return m970compareToLRDsOJo(((Duration) obj).m971unboximpl());
    }

    /* renamed from: unaryMinus-impl reason: not valid java name */
    public static final double m969unaryMinusimpl(double d) {
        return m928constructorimpl(-d);
    }

    /* renamed from: plus-LRDsOJo reason: not valid java name */
    public static final double m952plusLRDsOJo(double d, double d2) {
        return m928constructorimpl(d + d2);
    }

    /* renamed from: minus-LRDsOJo reason: not valid java name */
    public static final double m951minusLRDsOJo(double d, double d2) {
        return m928constructorimpl(d - d2);
    }

    /* renamed from: times-impl reason: not valid java name */
    public static final double m955timesimpl(double d, int i) {
        return m928constructorimpl(d * ((double) i));
    }

    /* renamed from: times-impl reason: not valid java name */
    public static final double m954timesimpl(double d, double d2) {
        return m928constructorimpl(d * d2);
    }

    /* renamed from: div-impl reason: not valid java name */
    public static final double m931divimpl(double d, int i) {
        return m928constructorimpl(d / ((double) i));
    }

    /* renamed from: div-impl reason: not valid java name */
    public static final double m930divimpl(double d, double d2) {
        return m928constructorimpl(d / d2);
    }

    /* renamed from: isInfinite-impl reason: not valid java name */
    public static final boolean m948isInfiniteimpl(double d) {
        return Double.isInfinite(d);
    }

    /* renamed from: isFinite-impl reason: not valid java name */
    public static final boolean m947isFiniteimpl(double d) {
        return !Double.isInfinite(d) && !Double.isNaN(d);
    }

    /* renamed from: getAbsoluteValue-impl reason: not valid java name */
    public static final double m934getAbsoluteValueimpl(double d) {
        return m949isNegativeimpl(d) ? m969unaryMinusimpl(d) : d;
    }

    /* renamed from: compareTo-LRDsOJo reason: not valid java name */
    public static int m927compareToLRDsOJo(double d, double d2) {
        return Double.compare(d, d2);
    }

    /* renamed from: toComponents-impl reason: not valid java name */
    public static final <T> T m959toComponentsimpl(double d, Function5<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends T> function5) {
        Intrinsics.checkParameterIsNotNull(function5, WLConstants.ACTION_ID);
        return function5.invoke(Integer.valueOf((int) m936getInDaysimpl(d)), Integer.valueOf(m935getHoursComponentimpl(d)), Integer.valueOf(m943getMinutesComponentimpl(d)), Integer.valueOf(m945getSecondsComponentimpl(d)), Integer.valueOf(m944getNanosecondsComponentimpl(d)));
    }

    /* renamed from: toComponents-impl reason: not valid java name */
    public static final <T> T m958toComponentsimpl(double d, Function4<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends T> function4) {
        Intrinsics.checkParameterIsNotNull(function4, WLConstants.ACTION_ID);
        return function4.invoke(Integer.valueOf((int) m937getInHoursimpl(d)), Integer.valueOf(m943getMinutesComponentimpl(d)), Integer.valueOf(m945getSecondsComponentimpl(d)), Integer.valueOf(m944getNanosecondsComponentimpl(d)));
    }

    /* renamed from: toComponents-impl reason: not valid java name */
    public static final <T> T m957toComponentsimpl(double d, Function3<? super Integer, ? super Integer, ? super Integer, ? extends T> function3) {
        Intrinsics.checkParameterIsNotNull(function3, WLConstants.ACTION_ID);
        return function3.invoke(Integer.valueOf((int) m940getInMinutesimpl(d)), Integer.valueOf(m945getSecondsComponentimpl(d)), Integer.valueOf(m944getNanosecondsComponentimpl(d)));
    }

    /* renamed from: toComponents-impl reason: not valid java name */
    public static final <T> T m956toComponentsimpl(double d, Function2<? super Long, ? super Integer, ? extends T> function2) {
        Intrinsics.checkParameterIsNotNull(function2, WLConstants.ACTION_ID);
        return function2.invoke(Long.valueOf((long) m942getInSecondsimpl(d)), Integer.valueOf(m944getNanosecondsComponentimpl(d)));
    }

    /* renamed from: getHoursComponent-impl reason: not valid java name */
    public static final int m935getHoursComponentimpl(double d) {
        return (int) (m937getInHoursimpl(d) % ((double) 24));
    }

    /* renamed from: getMinutesComponent-impl reason: not valid java name */
    public static final int m943getMinutesComponentimpl(double d) {
        return (int) (m940getInMinutesimpl(d) % ((double) 60));
    }

    /* renamed from: getSecondsComponent-impl reason: not valid java name */
    public static final int m945getSecondsComponentimpl(double d) {
        return (int) (m942getInSecondsimpl(d) % ((double) 60));
    }

    /* renamed from: getNanosecondsComponent-impl reason: not valid java name */
    public static final int m944getNanosecondsComponentimpl(double d) {
        return (int) (m941getInNanosecondsimpl(d) % 1.0E9d);
    }

    /* renamed from: toDouble-impl reason: not valid java name */
    public static final double m960toDoubleimpl(double d, TimeUnit timeUnit) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        return DurationUnitKt.convertDurationUnit(d, DurationKt.getStorageUnit(), timeUnit);
    }

    /* renamed from: toLong-impl reason: not valid java name */
    public static final long m963toLongimpl(double d, TimeUnit timeUnit) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        return (long) m960toDoubleimpl(d, timeUnit);
    }

    /* renamed from: toInt-impl reason: not valid java name */
    public static final int m961toIntimpl(double d, TimeUnit timeUnit) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        return (int) m960toDoubleimpl(d, timeUnit);
    }

    /* renamed from: getInDays-impl reason: not valid java name */
    public static final double m936getInDaysimpl(double d) {
        return m960toDoubleimpl(d, TimeUnit.DAYS);
    }

    /* renamed from: getInHours-impl reason: not valid java name */
    public static final double m937getInHoursimpl(double d) {
        return m960toDoubleimpl(d, TimeUnit.HOURS);
    }

    /* renamed from: getInMinutes-impl reason: not valid java name */
    public static final double m940getInMinutesimpl(double d) {
        return m960toDoubleimpl(d, TimeUnit.MINUTES);
    }

    /* renamed from: getInSeconds-impl reason: not valid java name */
    public static final double m942getInSecondsimpl(double d) {
        return m960toDoubleimpl(d, TimeUnit.SECONDS);
    }

    /* renamed from: getInMilliseconds-impl reason: not valid java name */
    public static final double m939getInMillisecondsimpl(double d) {
        return m960toDoubleimpl(d, TimeUnit.MILLISECONDS);
    }

    /* renamed from: getInMicroseconds-impl reason: not valid java name */
    public static final double m938getInMicrosecondsimpl(double d) {
        return m960toDoubleimpl(d, TimeUnit.MICROSECONDS);
    }

    /* renamed from: getInNanoseconds-impl reason: not valid java name */
    public static final double m941getInNanosecondsimpl(double d) {
        return m960toDoubleimpl(d, TimeUnit.NANOSECONDS);
    }

    /* renamed from: toLongNanoseconds-impl reason: not valid java name */
    public static final long m965toLongNanosecondsimpl(double d) {
        return m963toLongimpl(d, TimeUnit.NANOSECONDS);
    }

    /* renamed from: toLongMilliseconds-impl reason: not valid java name */
    public static final long m964toLongMillisecondsimpl(double d) {
        return m963toLongimpl(d, TimeUnit.MILLISECONDS);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a0  */
    /* renamed from: toString-impl reason: not valid java name */
    public static String m966toStringimpl(double d) {
        int i;
        TimeUnit timeUnit;
        String str;
        TimeUnit timeUnit2;
        if (m948isInfiniteimpl(d)) {
            return String.valueOf(d);
        }
        if (d == 0.0d) {
            return "0s";
        }
        double r0 = m941getInNanosecondsimpl(m934getAbsoluteValueimpl(d));
        boolean z = false;
        if (r0 < 1.0E-6d) {
            timeUnit2 = TimeUnit.SECONDS;
        } else {
            if (r0 < ((double) 1)) {
                timeUnit = TimeUnit.NANOSECONDS;
                i = 7;
            } else {
                if (r0 < 1000.0d) {
                    timeUnit = TimeUnit.NANOSECONDS;
                } else if (r0 < 1000000.0d) {
                    timeUnit = TimeUnit.MICROSECONDS;
                } else if (r0 < 1.0E9d) {
                    timeUnit = TimeUnit.MILLISECONDS;
                } else if (r0 < 1.0E12d) {
                    timeUnit = TimeUnit.SECONDS;
                } else if (r0 < 6.0E13d) {
                    timeUnit = TimeUnit.MINUTES;
                } else if (r0 < 3.6E15d) {
                    timeUnit = TimeUnit.HOURS;
                } else if (r0 < 8.64E20d) {
                    timeUnit = TimeUnit.DAYS;
                } else {
                    timeUnit2 = TimeUnit.DAYS;
                }
                i = 0;
            }
            double r4 = m960toDoubleimpl(d, timeUnit);
            StringBuilder sb = new StringBuilder();
            if (!z) {
                str = FormatToDecimalsKt.formatScientific(r4);
            } else if (i > 0) {
                str = FormatToDecimalsKt.formatUpToDecimals(r4, i);
            } else {
                str = FormatToDecimalsKt.formatToExactDecimals(r4, m953precisionimpl(d, Math.abs(r4)));
            }
            sb.append(str);
            sb.append(DurationUnitKt.shortName(timeUnit));
            return sb.toString();
        }
        i = 0;
        z = true;
        double r42 = m960toDoubleimpl(d, timeUnit);
        StringBuilder sb2 = new StringBuilder();
        if (!z) {
        }
        sb2.append(str);
        sb2.append(DurationUnitKt.shortName(timeUnit));
        return sb2.toString();
    }

    /* renamed from: toString-impl$default reason: not valid java name */
    public static /* synthetic */ String m968toStringimpl$default(double d, TimeUnit timeUnit, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return m967toStringimpl(d, timeUnit, i);
    }

    /* renamed from: toString-impl reason: not valid java name */
    public static final String m967toStringimpl(double d, TimeUnit timeUnit, int i) {
        String str;
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        if (!(i >= 0)) {
            StringBuilder sb = new StringBuilder();
            sb.append("decimals must be not negative, but was ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString().toString());
        } else if (m948isInfiniteimpl(d)) {
            return String.valueOf(d);
        } else {
            double r5 = m960toDoubleimpl(d, timeUnit);
            StringBuilder sb2 = new StringBuilder();
            if (Math.abs(r5) < 1.0E14d) {
                str = FormatToDecimalsKt.formatToExactDecimals(r5, RangesKt.coerceAtMost(i, 12));
            } else {
                str = FormatToDecimalsKt.formatScientific(r5);
            }
            sb2.append(str);
            sb2.append(DurationUnitKt.shortName(timeUnit));
            return sb2.toString();
        }
    }

    /* renamed from: toIsoString-impl reason: not valid java name */
    public static final String m962toIsoStringimpl(double d) {
        StringBuilder sb = new StringBuilder();
        if (m949isNegativeimpl(d)) {
            sb.append('-');
        }
        sb.append("PT");
        double r7 = m934getAbsoluteValueimpl(d);
        int r1 = (int) m937getInHoursimpl(r7);
        int r2 = m943getMinutesComponentimpl(r7);
        int r3 = m945getSecondsComponentimpl(r7);
        int r72 = m944getNanosecondsComponentimpl(r7);
        boolean z = true;
        boolean z2 = r1 != 0;
        boolean z3 = (r3 == 0 && r72 == 0) ? false : true;
        if (r2 == 0 && (!z3 || !z2)) {
            z = false;
        }
        if (z2) {
            sb.append(r1);
            sb.append('H');
        }
        if (z) {
            sb.append(r2);
            sb.append('M');
        }
        if (z3 || (!z2 && !z)) {
            sb.append(r3);
            if (r72 != 0) {
                sb.append('.');
                String padStart = StringsKt.padStart(String.valueOf(r72), 9, '0');
                if (r72 % 1000000 == 0) {
                    sb.append(padStart, 0, 3);
                } else if (r72 % 1000 == 0) {
                    sb.append(padStart, 0, 6);
                } else {
                    sb.append(padStart);
                }
            }
            sb.append('S');
        }
        String sb2 = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(sb2, "StringBuilder().apply(builderAction).toString()");
        return sb2;
    }
}
