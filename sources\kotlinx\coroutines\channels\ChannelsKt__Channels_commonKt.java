package kotlinx.coroutines.channels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.IndexedValue;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.BooleanRef;
import kotlin.jvm.internal.Ref.DoubleRef;
import kotlin.jvm.internal.Ref.IntRef;
import kotlin.jvm.internal.Ref.ObjectRef;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExceptionsKt;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls;
import kotlinx.coroutines.selects.SelectClause1;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Ø\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010%\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0011\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0010#\n\u0000\n\u0002\u0010\"\n\u0002\b\u0006\u001aJ\u0010\u0002\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0003j\u0002`\t2\u001a\u0010\n\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\f0\u000b\"\u0006\u0012\u0002\b\u00030\fH\u0007¢\u0006\u0002\u0010\r\u001a5\u0010\u000e\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a!\u0010\u0013\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a5\u0010\u0013\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001aY\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u0002H\u00180\u0016\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017\"\u0004\b\u0002\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00100\f2\u001e\u0010\u0019\u001a\u001a\u0012\u0004\u0012\u0002H\u0010\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u0002H\u00180\u001a0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001aG\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00170\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001aa\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u0002H\u00180\u0016\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017\"\u0004\b\u0002\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00170\u00032\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00180\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u001e\u001a]\u0010\u001f\u001a\u0002H \"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017\"\u0018\b\u0002\u0010 *\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0017\u0012\u0006\b\u0000\u0012\u0002H\u00100!*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002H 2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00170\u0003HHø\u0001\u0000¢\u0006\u0002\u0010#\u001aw\u0010\u001f\u001a\u0002H \"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017\"\u0004\b\u0002\u0010\u0018\"\u0018\b\u0003\u0010 *\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0017\u0012\u0006\b\u0000\u0012\u0002H\u00180!*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002H 2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00170\u00032\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00180\u0003HHø\u0001\u0000¢\u0006\u0002\u0010$\u001ao\u0010%\u001a\u0002H \"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017\"\u0004\b\u0002\u0010\u0018\"\u0018\b\u0003\u0010 *\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0017\u0012\u0006\b\u0000\u0012\u0002H\u00180!*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002H 2\u001e\u0010\u0019\u001a\u001a\u0012\u0004\u0012\u0002H\u0010\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u0002H\u00180\u001a0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010#\u001a\u001a\u0010&\u001a\u00020\b*\u0006\u0012\u0002\b\u00030\f2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0004H\u0001\u001aC\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H\u00100)2\u001d\u0010*\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\f\u0012\u0004\u0012\u0002H(0\u0003¢\u0006\u0002\b+H\b¢\u0006\u0002\u0010,\u001aC\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H\u00100\f2\u001d\u0010*\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\f\u0012\u0004\u0012\u0002H(0\u0003¢\u0006\u0002\b+H\b¢\u0006\u0002\u0010-\u001a5\u0010.\u001a\u00020\b\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100)2\u0012\u0010/\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\b0\u0003HHø\u0001\u0000¢\u0006\u0002\u00100\u001a5\u0010.\u001a\u00020\b\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010/\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\b0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a;\u00101\u001a\u00020\b\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0018\u0010/\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001002\u0012\u0004\u0012\u00020\b0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a1\u00103\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0003j\u0002`\t*\u0006\u0012\u0002\b\u00030\fH\u0007\u001a!\u00104\u001a\u000205\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a5\u00104\u001a\u000205\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a\u001e\u00106\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH\u0007\u001aZ\u00107\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u0002092\"\u0010:\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00170<\u0012\u0006\u0012\u0004\u0018\u00010=0;H\u0007ø\u0001\u0000¢\u0006\u0002\u0010>\u001a0\u0010?\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010@\u001a\u0002052\b\b\u0002\u00108\u001a\u000209H\u0007\u001aT\u0010A\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u0002092\"\u0010\u0011\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0<\u0012\u0006\u0012\u0004\u0018\u00010=0;H\u0007ø\u0001\u0000¢\u0006\u0002\u0010>\u001a)\u0010B\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010C\u001a\u000205H@ø\u0001\u0000¢\u0006\u0002\u0010D\u001a=\u0010E\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010C\u001a\u0002052\u0012\u0010F\u001a\u000e\u0012\u0004\u0012\u000205\u0012\u0004\u0012\u0002H\u00100\u0003HHø\u0001\u0000¢\u0006\u0002\u0010G\u001a+\u0010H\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010C\u001a\u000205H@ø\u0001\u0000¢\u0006\u0002\u0010D\u001aT\u0010I\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u0002092\"\u0010\u0011\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0<\u0012\u0006\u0012\u0004\u0018\u00010=0;H\u0007ø\u0001\u0000¢\u0006\u0002\u0010>\u001ai\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u00020927\u0010\u0011\u001a3\b\u0001\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0<\u0012\u0006\u0012\u0004\u0018\u00010=0KH\u0007ø\u0001\u0000¢\u0006\u0002\u0010L\u001ad\u0010M\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u0010\b\u0001\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100O*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2'\u0010\u0011\u001a#\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0;HHø\u0001\u0000¢\u0006\u0002\u0010P\u001ab\u0010M\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u000e\b\u0001\u0010N*\b\u0012\u0004\u0012\u0002H\u00100Q*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2'\u0010\u0011\u001a#\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0;HHø\u0001\u0000¢\u0006\u0002\u0010R\u001aT\u0010S\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u0002092\"\u0010\u0011\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0<\u0012\u0006\u0012\u0004\u0018\u00010=0;H\u0007ø\u0001\u0000¢\u0006\u0002\u0010>\u001a$\u0010T\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\b\b\u0000\u0010\u0010*\u00020=*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\fH\u0007\u001aA\u0010U\u001a\u0002HN\"\b\b\u0000\u0010\u0010*\u00020=\"\u0010\b\u0001\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100O*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\f2\u0006\u0010\"\u001a\u0002HNH@ø\u0001\u0000¢\u0006\u0002\u0010V\u001a?\u0010U\u001a\u0002HN\"\b\b\u0000\u0010\u0010*\u00020=\"\u000e\b\u0001\u0010N*\b\u0012\u0004\u0012\u0002H\u00100Q*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\f2\u0006\u0010\"\u001a\u0002HNH@ø\u0001\u0000¢\u0006\u0002\u0010W\u001aO\u0010X\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u0010\b\u0001\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100O*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010Y\u001aM\u0010X\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u000e\b\u0001\u0010N*\b\u0012\u0004\u0012\u0002H\u00100Q*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010Z\u001aO\u0010[\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u0010\b\u0001\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100O*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010Y\u001aM\u0010[\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u000e\b\u0001\u0010N*\b\u0012\u0004\u0012\u0002H\u00100Q*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010Z\u001a7\u0010\\\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a7\u0010]\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a!\u0010^\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a5\u0010^\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a#\u0010_\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a7\u0010_\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a`\u0010`\u001a\b\u0012\u0004\u0012\u0002H(0\f\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u0002092(\u0010\u0019\u001a$\b\u0001\u0012\u0004\u0012\u0002H\u0010\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H(0\f0<\u0012\u0006\u0012\u0004\u0018\u00010=0;H\u0007ø\u0001\u0000¢\u0006\u0002\u0010>\u001aX\u0010a\u001a\u0002H(\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010b\u001a\u0002H(2'\u0010c\u001a#\u0012\u0013\u0012\u0011H(¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(d\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0;HHø\u0001\u0000¢\u0006\u0002\u0010e\u001am\u0010f\u001a\u0002H(\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010b\u001a\u0002H(2<\u0010c\u001a8\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0013\u0012\u0011H(¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(d\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0KHHø\u0001\u0000¢\u0006\u0002\u0010g\u001aM\u0010h\u001a\u0014\u0012\u0004\u0012\u0002H\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100i0\u0016\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00170\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001ag\u0010h\u001a\u0014\u0012\u0004\u0012\u0002H\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180i0\u0016\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017\"\u0004\b\u0002\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00170\u00032\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00180\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u001e\u001aa\u0010j\u001a\u0002H \"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017\"\u001c\b\u0002\u0010 *\u0016\u0012\u0006\b\u0000\u0012\u0002H\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100k0!*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002H 2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00170\u0003HHø\u0001\u0000¢\u0006\u0002\u0010#\u001a{\u0010j\u001a\u0002H \"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010\u0017\"\u0004\b\u0002\u0010\u0018\"\u001c\b\u0003\u0010 *\u0016\u0012\u0006\b\u0000\u0012\u0002H\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180k0!*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002H 2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00170\u00032\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u00180\u0003HHø\u0001\u0000¢\u0006\u0002\u0010$\u001a)\u0010l\u001a\u000205\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010m\u001a\u0002H\u0010H@ø\u0001\u0000¢\u0006\u0002\u0010n\u001a5\u0010o\u001a\u000205\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a5\u0010p\u001a\u000205\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a!\u0010q\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a5\u0010q\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a)\u0010r\u001a\u000205\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010m\u001a\u0002H\u0010H@ø\u0001\u0000¢\u0006\u0002\u0010n\u001a#\u0010s\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a7\u0010s\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001aZ\u0010t\u001a\b\u0012\u0004\u0012\u0002H(0\f\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u0002092\"\u0010\u0019\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u0002H(0<\u0012\u0006\u0012\u0004\u0018\u00010=0;H\u0007ø\u0001\u0000¢\u0006\u0002\u0010>\u001ao\u0010u\u001a\b\u0012\u0004\u0012\u0002H(0\f\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u00020927\u0010\u0019\u001a3\b\u0001\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u0002H(0<\u0012\u0006\u0012\u0004\u0018\u00010=0KH\u0007ø\u0001\u0000¢\u0006\u0002\u0010L\u001au\u0010v\u001a\b\u0012\u0004\u0012\u0002H(0\f\"\u0004\b\u0000\u0010\u0010\"\b\b\u0001\u0010(*\u00020=*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u00020929\u0010\u0019\u001a5\b\u0001\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u0001H(0<\u0012\u0006\u0012\u0004\u0018\u00010=0KH\u0007ø\u0001\u0000¢\u0006\u0002\u0010L\u001ap\u0010w\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\b\b\u0001\u0010(*\u00020=\"\u0010\b\u0002\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H(0O*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2)\u0010\u0019\u001a%\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\u0006\u0012\u0004\u0018\u0001H(0;HHø\u0001\u0000¢\u0006\u0002\u0010P\u001an\u0010w\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\b\b\u0001\u0010(*\u00020=\"\u000e\b\u0002\u0010N*\b\u0012\u0004\u0012\u0002H(0Q*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2)\u0010\u0019\u001a%\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\u0006\u0012\u0004\u0018\u0001H(0;HHø\u0001\u0000¢\u0006\u0002\u0010R\u001aj\u0010x\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(\"\u0010\b\u0002\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H(0O*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2'\u0010\u0019\u001a#\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0;HHø\u0001\u0000¢\u0006\u0002\u0010P\u001ah\u0010x\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(\"\u000e\b\u0002\u0010N*\b\u0012\u0004\u0012\u0002H(0Q*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2'\u0010\u0019\u001a#\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0;HHø\u0001\u0000¢\u0006\u0002\u0010R\u001a`\u0010y\u001a\b\u0012\u0004\u0012\u0002H(0\f\"\u0004\b\u0000\u0010\u0010\"\b\b\u0001\u0010(*\u00020=*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u0002092$\u0010\u0019\u001a \b\u0001\u0012\u0004\u0012\u0002H\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u0001H(0<\u0012\u0006\u0012\u0004\u0018\u00010=0;H\u0007ø\u0001\u0000¢\u0006\u0002\u0010>\u001a[\u0010z\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\b\b\u0001\u0010(*\u00020=\"\u0010\b\u0002\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H(0O*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2\u0014\u0010\u0019\u001a\u0010\u0012\u0004\u0012\u0002H\u0010\u0012\u0006\u0012\u0004\u0018\u0001H(0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010Y\u001aY\u0010z\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\b\b\u0001\u0010(*\u00020=\"\u000e\b\u0002\u0010N*\b\u0012\u0004\u0012\u0002H(0Q*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2\u0014\u0010\u0019\u001a\u0010\u0012\u0004\u0012\u0002H\u0010\u0012\u0006\u0012\u0004\u0018\u0001H(0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010Z\u001aU\u0010{\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(\"\u0010\b\u0002\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H(0O*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010Y\u001aS\u0010{\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(\"\u000e\b\u0002\u0010N*\b\u0012\u0004\u0012\u0002H(0Q*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HN2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010Z\u001aG\u0010|\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010\"\u000e\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H(0}*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001aB\u0010~\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u001c\u0010\u001a\u0018\u0012\u0006\b\u0000\u0012\u0002H\u00100\u0001j\u000b\u0012\u0006\b\u0000\u0012\u0002H\u0010`\u0001H@ø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001aH\u0010\u0001\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010\"\u000e\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H(0}*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001aC\u0010\u0001\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u001c\u0010\u001a\u0018\u0012\u0006\b\u0000\u0012\u0002H\u00100\u0001j\u000b\u0012\u0006\b\u0000\u0012\u0002H\u0010`\u0001H@ø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001a\"\u0010\u0001\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a6\u0010\u0001\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a&\u0010\u0001\u001a\u000b\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0001\"\b\b\u0000\u0010\u0010*\u00020=*\b\u0012\u0004\u0012\u0002H\u00100\fH\u0007\u001aN\u0010\u0001\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100i\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100i0\u001a\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a(\u0010\u0001\u001a\u0004\u0018\u0001H\u0010\"\b\b\u0000\u0010\u0010*\u00020=*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a[\u0010\u0001\u001a\u0003H\u0001\"\u0005\b\u0000\u0010\u0001\"\t\b\u0001\u0010\u0010*\u0003H\u0001*\b\u0012\u0004\u0012\u0002H\u00100\f2)\u0010c\u001a%\u0012\u0014\u0012\u0012H\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(d\u0012\u0004\u0012\u0002H\u0010\u0012\u0005\u0012\u0003H\u00010;HHø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001ap\u0010\u0001\u001a\u0003H\u0001\"\u0005\b\u0000\u0010\u0001\"\t\b\u0001\u0010\u0010*\u0003H\u0001*\b\u0012\u0004\u0012\u0002H\u00100\f2>\u0010c\u001a:\u0012\u0013\u0012\u001105¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(C\u0012\u0014\u0012\u0012H\u0001¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(d\u0012\u0004\u0012\u0002H\u0010\u0012\u0005\u0012\u0003H\u00010KHHø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001a%\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\b\b\u0000\u0010\u0010*\u00020=*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\fH\u0007\u001a\"\u0010\u0001\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a6\u0010\u0001\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a$\u0010\u0001\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a8\u0010\u0001\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u000f0\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a6\u0010\u0001\u001a\u000205\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002050\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a8\u0010\u0001\u001a\u00030\u0001\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0013\u0010:\u001a\u000f\u0012\u0004\u0012\u0002H\u0010\u0012\u0005\u0012\u00030\u00010\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a1\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010@\u001a\u0002052\b\b\u0002\u00108\u001a\u000209H\u0007\u001aU\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00100\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u0002092\"\u0010\u0011\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0<\u0012\u0006\u0012\u0004\u0018\u00010=0;H\u0007ø\u0001\u0000¢\u0006\u0002\u0010>\u001a:\u0010\u0001\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u000e\b\u0001\u0010N*\b\u0012\u0004\u0012\u0002H\u00100Q*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HNH@ø\u0001\u0000¢\u0006\u0002\u0010W\u001a<\u0010\u0001\u001a\u0002HN\"\u0004\b\u0000\u0010\u0010\"\u0010\b\u0001\u0010N*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100O*\b\u0012\u0004\u0012\u0002H\u00100\f2\u0006\u0010\"\u001a\u0002HNH@ø\u0001\u0000¢\u0006\u0002\u0010V\u001a(\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00100i\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a@\u0010\u0001\u001a\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u0002H\u00180\u0016\"\u0004\b\u0000\u0010\u0017\"\u0004\b\u0001\u0010\u0018*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u0002H\u00180\u001a0\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001aW\u0010\u0001\u001a\u0002H \"\u0004\b\u0000\u0010\u0017\"\u0004\b\u0001\u0010\u0018\"\u0018\b\u0002\u0010 *\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0017\u0012\u0006\b\u0000\u0012\u0002H\u00180!*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u0002H\u00180\u001a0\f2\u0006\u0010\"\u001a\u0002H H@ø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001a(\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00100k\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a)\u0010\u0001\u001a\t\u0012\u0004\u0012\u0002H\u00100\u0001\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a)\u0010\u0001\u001a\t\u0012\u0004\u0012\u0002H\u00100 \u0001\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a/\u0010¡\u0001\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u0010020\f\"\u0004\b\u0000\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100\f2\b\b\u0002\u00108\u001a\u000209H\u0007\u001aA\u0010¢\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H(0\u001a0\f\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(*\b\u0012\u0004\u0012\u0002H\u00100\f2\r\u0010£\u0001\u001a\b\u0012\u0004\u0012\u0002H(0\fH\u0004\u001a~\u0010¢\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00180\f\"\u0004\b\u0000\u0010\u0010\"\u0004\b\u0001\u0010(\"\u0004\b\u0002\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00100\f2\r\u0010£\u0001\u001a\b\u0012\u0004\u0012\u0002H(0\f2\b\b\u0002\u00108\u001a\u00020928\u0010\u0019\u001a4\u0012\u0014\u0012\u0012H\u0010¢\u0006\r\b\u0005\u0012\t\b\u0006\u0012\u0005\b\b(¤\u0001\u0012\u0014\u0012\u0012H(¢\u0006\r\b\u0005\u0012\t\b\u0006\u0012\u0005\b\b(¥\u0001\u0012\u0004\u0012\u0002H\u00180;H\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006¦\u0001"}, d2 = {"DEFAULT_CLOSE_MESSAGE", "", "consumesAll", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "Lkotlinx/coroutines/CompletionHandler;", "channels", "", "Lkotlinx/coroutines/channels/ReceiveChannel;", "([Lkotlinx/coroutines/channels/ReceiveChannel;)Lkotlin/jvm/functions/Function1;", "all", "", "E", "predicate", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "any", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "associate", "", "K", "V", "transform", "Lkotlin/Pair;", "associateBy", "keySelector", "valueTransform", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "associateByTo", "M", "", "destination", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Map;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Map;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "associateTo", "cancelConsumed", "consume", "R", "Lkotlinx/coroutines/channels/BroadcastChannel;", "block", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/channels/BroadcastChannel;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "consumeEach", "action", "(Lkotlinx/coroutines/channels/BroadcastChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "consumeEachIndexed", "Lkotlin/collections/IndexedValue;", "consumes", "count", "", "distinct", "distinctBy", "context", "Lkotlin/coroutines/CoroutineContext;", "selector", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/channels/ReceiveChannel;", "drop", "n", "dropWhile", "elementAt", "index", "(Lkotlinx/coroutines/channels/ReceiveChannel;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "elementAtOrElse", "defaultValue", "(Lkotlinx/coroutines/channels/ReceiveChannel;ILkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "elementAtOrNull", "filter", "filterIndexed", "Lkotlin/Function3;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/channels/ReceiveChannel;", "filterIndexedTo", "C", "", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Collection;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Lkotlinx/coroutines/channels/SendChannel;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlinx/coroutines/channels/SendChannel;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "filterNot", "filterNotNull", "filterNotNullTo", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Collection;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "filterNotTo", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlinx/coroutines/channels/SendChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "filterTo", "find", "findLast", "first", "firstOrNull", "flatMap", "fold", "initial", "operation", "acc", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "foldIndexed", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "groupBy", "", "groupByTo", "", "indexOf", "element", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "indexOfFirst", "indexOfLast", "last", "lastIndexOf", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "mapIndexedNotNullTo", "mapIndexedTo", "mapNotNull", "mapNotNullTo", "mapTo", "maxBy", "", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Comparator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "minBy", "minWith", "none", "onReceiveOrNull", "Lkotlinx/coroutines/selects/SelectClause1;", "partition", "receiveOrNull", "reduce", "S", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reduceIndexed", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requireNoNulls", "single", "singleOrNull", "sumBy", "sumByDouble", "", "take", "takeWhile", "toChannel", "toCollection", "toList", "toMap", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toMutableList", "toMutableSet", "", "toSet", "", "withIndex", "zip", "other", "a", "b", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/channels/ChannelsKt")
/* compiled from: Channels.common.kt */
final /* synthetic */ class ChannelsKt__Channels_commonKt {
    public static final <E, R> R consume(BroadcastChannel<E> broadcastChannel, Function1<? super ReceiveChannel<? extends E>, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(broadcastChannel, "$this$consume");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        ReceiveChannel openSubscription = broadcastChannel.openSubscription();
        try {
            return function1.invoke(openSubscription);
        } finally {
            InlineMarker.finallyStart(1);
            DefaultImpls.cancel$default(openSubscription, (CancellationException) null, 1, (Object) null);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final <E> Object receiveOrNull(ReceiveChannel<? extends E> receiveChannel, Continuation<? super E> continuation) {
        if (receiveChannel != null) {
            return receiveChannel.receiveOrNull(continuation);
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.ReceiveChannel<E?>");
    }

    public static final <E> SelectClause1<E> onReceiveOrNull(ReceiveChannel<? extends E> receiveChannel) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$onReceiveOrNull");
        return receiveChannel.getOnReceiveOrNull();
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0087 A[Catch:{ all -> 0x009e }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    public static final <E> Object consumeEach(BroadcastChannel<E> broadcastChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) {
        ChannelsKt__Channels_commonKt$consumeEach$1 channelsKt__Channels_commonKt$consumeEach$1;
        int i;
        ReceiveChannel receiveChannel;
        BroadcastChannel<E> broadcastChannel2;
        ChannelIterator channelIterator;
        ReceiveChannel receiveChannel2;
        Object obj;
        BroadcastChannel<E> broadcastChannel3;
        ChannelsKt__Channels_commonKt$consumeEach$1 channelsKt__Channels_commonKt$consumeEach$12;
        Function1<? super E, Unit> function12;
        BroadcastChannel<E> broadcastChannel4;
        Object obj2;
        ReceiveChannel receiveChannel3;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$consumeEach$1) {
            channelsKt__Channels_commonKt$consumeEach$1 = (ChannelsKt__Channels_commonKt$consumeEach$1) continuation;
            if ((channelsKt__Channels_commonKt$consumeEach$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$consumeEach$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$consumeEach$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$consumeEach$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    receiveChannel = broadcastChannel.openSubscription();
                    obj2 = coroutine_suspended;
                    receiveChannel3 = receiveChannel;
                    channelsKt__Channels_commonKt$consumeEach$12 = channelsKt__Channels_commonKt$consumeEach$1;
                    function12 = function1;
                    broadcastChannel2 = broadcastChannel;
                    channelIterator = receiveChannel.iterator();
                    broadcastChannel4 = broadcastChannel2;
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$consumeEach$1.L$5;
                    ReceiveChannel receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEach$1.L$4;
                    receiveChannel = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEach$1.L$3;
                    broadcastChannel3 = (BroadcastChannel) channelsKt__Channels_commonKt$consumeEach$1.L$2;
                    Function1<? super E, Unit> function13 = (Function1) channelsKt__Channels_commonKt$consumeEach$1.L$1;
                    BroadcastChannel<E> broadcastChannel5 = (BroadcastChannel) channelsKt__Channels_commonKt$consumeEach$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ReceiveChannel receiveChannel5 = receiveChannel;
                        receiveChannel = receiveChannel4;
                        broadcastChannel2 = broadcastChannel5;
                        receiveChannel2 = receiveChannel5;
                        Object obj4 = coroutine_suspended;
                        channelsKt__Channels_commonKt$consumeEach$12 = channelsKt__Channels_commonKt$consumeEach$1;
                        function12 = function13;
                        obj = obj4;
                        try {
                            if (!((Boolean) obj3).booleanValue()) {
                                function12.invoke(channelIterator.next());
                                broadcastChannel4 = broadcastChannel3;
                                obj2 = obj;
                                receiveChannel3 = receiveChannel2;
                            }
                            Unit unit = Unit.INSTANCE;
                            InlineMarker.finallyStart(1);
                            DefaultImpls.cancel$default(receiveChannel2, (CancellationException) null, 1, (Object) null);
                            InlineMarker.finallyEnd(1);
                            return unit;
                        } catch (Throwable th) {
                            th = th;
                            receiveChannel = receiveChannel2;
                            InlineMarker.finallyStart(1);
                            DefaultImpls.cancel$default(receiveChannel, (CancellationException) null, 1, (Object) null);
                            InlineMarker.finallyEnd(1);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        InlineMarker.finallyStart(1);
                        DefaultImpls.cancel$default(receiveChannel, (CancellationException) null, 1, (Object) null);
                        InlineMarker.finallyEnd(1);
                        throw th;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$consumeEach$12.L$0 = broadcastChannel2;
                channelsKt__Channels_commonKt$consumeEach$12.L$1 = function12;
                channelsKt__Channels_commonKt$consumeEach$12.L$2 = broadcastChannel4;
                channelsKt__Channels_commonKt$consumeEach$12.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$consumeEach$12.L$4 = receiveChannel;
                channelsKt__Channels_commonKt$consumeEach$12.L$5 = channelIterator;
                channelsKt__Channels_commonKt$consumeEach$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$consumeEach$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                broadcastChannel3 = broadcastChannel4;
                obj3 = hasNext;
                receiveChannel2 = receiveChannel3;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                DefaultImpls.cancel$default(receiveChannel2, (CancellationException) null, 1, (Object) null);
                InlineMarker.finallyEnd(1);
                return unit22;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$consumeEach$1 = new ChannelsKt__Channels_commonKt$consumeEach$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$consumeEach$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$consumeEach$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$consumeEach$12.L$0 = broadcastChannel2;
            channelsKt__Channels_commonKt$consumeEach$12.L$1 = function12;
            channelsKt__Channels_commonKt$consumeEach$12.L$2 = broadcastChannel4;
            channelsKt__Channels_commonKt$consumeEach$12.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$consumeEach$12.L$4 = receiveChannel;
            channelsKt__Channels_commonKt$consumeEach$12.L$5 = channelIterator;
            channelsKt__Channels_commonKt$consumeEach$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$consumeEach$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th3) {
            th = th3;
            receiveChannel = receiveChannel3;
            InlineMarker.finallyStart(1);
            DefaultImpls.cancel$default(receiveChannel, (CancellationException) null, 1, (Object) null);
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final Function1<Throwable, Unit> consumes(ReceiveChannel<?> receiveChannel) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$consumes");
        return new ChannelsKt__Channels_commonKt$consumes$1<>(receiveChannel);
    }

    public static final void cancelConsumed(ReceiveChannel<?> receiveChannel, Throwable th) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$cancelConsumed");
        CancellationException cancellationException = null;
        if (th != 0) {
            if (th instanceof CancellationException) {
                cancellationException = th;
            }
            cancellationException = cancellationException;
            if (cancellationException == null) {
                cancellationException = ExceptionsKt.CancellationException("Channel was consumed, consumer had failed", th);
            }
        }
        receiveChannel.cancel(cancellationException);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final Function1<Throwable, Unit> consumesAll(ReceiveChannel<?>... receiveChannelArr) {
        Intrinsics.checkParameterIsNotNull(receiveChannelArr, "channels");
        return new ChannelsKt__Channels_commonKt$consumesAll$1<>(receiveChannelArr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0028, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001e, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001f, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r2, r3);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    public static final <E, R> R consume(ReceiveChannel<? extends E> receiveChannel, Function1<? super ReceiveChannel<? extends E>, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$consume");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        Throwable th = null;
        R invoke = function1.invoke(receiveChannel);
        InlineMarker.finallyStart(1);
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(1);
        return invoke;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x008a A[Catch:{ all -> 0x004a }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    public static final <E> Object consumeEach(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) {
        ChannelsKt__Channels_commonKt$consumeEach$3 channelsKt__Channels_commonKt$consumeEach$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$consumeEach$3 channelsKt__Channels_commonKt$consumeEach$32;
        Function1<? super E, Unit> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$consumeEach$3) {
            channelsKt__Channels_commonKt$consumeEach$3 = (ChannelsKt__Channels_commonKt$consumeEach$3) continuation;
            if ((channelsKt__Channels_commonKt$consumeEach$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$consumeEach$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$consumeEach$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$consumeEach$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        function12 = function1;
                        receiveChannel3 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$consumeEach$32 = channelsKt__Channels_commonKt$consumeEach$3;
                        receiveChannel5 = receiveChannel3;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$consumeEach$3.L$5;
                    ReceiveChannel<? extends E> receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEach$3.L$4;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$consumeEach$3.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEach$3.L$2;
                    Function1<? super E, Unit> function13 = (Function1) channelsKt__Channels_commonKt$consumeEach$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEach$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$consumeEach$3 channelsKt__Channels_commonKt$consumeEach$33 = channelsKt__Channels_commonKt$consumeEach$3;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel3 = receiveChannel7;
                        th = th4;
                        channelsKt__Channels_commonKt$consumeEach$32 = channelsKt__Channels_commonKt$consumeEach$33;
                        Function1<? super E, Unit> function14 = function13;
                        obj = coroutine_suspended;
                        function12 = function14;
                        if (!((Boolean) obj3).booleanValue()) {
                            function12.invoke(channelIterator.next());
                            receiveChannel2 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel4, th);
                        InlineMarker.finallyEnd(1);
                        return unit;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel4;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$consumeEach$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$consumeEach$32.L$1 = function12;
                channelsKt__Channels_commonKt$consumeEach$32.L$2 = receiveChannel2;
                channelsKt__Channels_commonKt$consumeEach$32.L$3 = th2;
                channelsKt__Channels_commonKt$consumeEach$32.L$4 = receiveChannel5;
                channelsKt__Channels_commonKt$consumeEach$32.L$5 = channelIterator;
                channelsKt__Channels_commonKt$consumeEach$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$consumeEach$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj4 = obj2;
                receiveChannel4 = receiveChannel2;
                obj3 = hasNext;
                th = th2;
                obj = obj4;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel4, th);
                InlineMarker.finallyEnd(1);
                return unit22;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$consumeEach$3 = new ChannelsKt__Channels_commonKt$consumeEach$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$consumeEach$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$consumeEach$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$consumeEach$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$consumeEach$32.L$1 = function12;
            channelsKt__Channels_commonKt$consumeEach$32.L$2 = receiveChannel2;
            channelsKt__Channels_commonKt$consumeEach$32.L$3 = th2;
            channelsKt__Channels_commonKt$consumeEach$32.L$4 = receiveChannel5;
            channelsKt__Channels_commonKt$consumeEach$32.L$5 = channelIterator;
            channelsKt__Channels_commonKt$consumeEach$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$consumeEach$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x009d A[Catch:{ all -> 0x00ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a8 A[Catch:{ all -> 0x00ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object consumeEachIndexed(ReceiveChannel<? extends E> receiveChannel, Function1<? super IndexedValue<? extends E>, Unit> function1, Continuation<? super Unit> continuation) {
        ChannelsKt__Channels_commonKt$consumeEachIndexed$1 channelsKt__Channels_commonKt$consumeEachIndexed$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        IntRef intRef;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$consumeEachIndexed$1 channelsKt__Channels_commonKt$consumeEachIndexed$12;
        Function1<? super IndexedValue<? extends E>, Unit> function12;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object hasNext;
        Continuation<? super Unit> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$consumeEachIndexed$1) {
            channelsKt__Channels_commonKt$consumeEachIndexed$1 = (ChannelsKt__Channels_commonKt$consumeEachIndexed$1) continuation2;
            if ((channelsKt__Channels_commonKt$consumeEachIndexed$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$consumeEachIndexed$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$consumeEachIndexed$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$consumeEachIndexed$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef2 = new IntRef();
                    intRef2.element = 0;
                    try {
                        intRef = intRef2;
                        channelsKt__Channels_commonKt$consumeEachIndexed$12 = channelsKt__Channels_commonKt$consumeEachIndexed$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                        function12 = function1;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$consumeEachIndexed$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEachIndexed$1.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$consumeEachIndexed$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEachIndexed$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEachIndexed$1.L$3;
                    IntRef intRef3 = (IntRef) channelsKt__Channels_commonKt$consumeEachIndexed$1.L$2;
                    Function1<? super IndexedValue<? extends E>, Unit> function13 = (Function1) channelsKt__Channels_commonKt$consumeEachIndexed$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$consumeEachIndexed$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        Throwable th5 = th4;
                        channelsKt__Channels_commonKt$consumeEachIndexed$12 = channelsKt__Channels_commonKt$consumeEachIndexed$1;
                        receiveChannel5 = receiveChannel9;
                        channelIterator = channelIterator2;
                        receiveChannel4 = receiveChannel7;
                        function12 = function13;
                        intRef = intRef3;
                        th2 = th5;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            int i2 = intRef.element;
                            intRef.element = i2 + 1;
                            function12.invoke(new IndexedValue(i2, next));
                            receiveChannel6 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return Unit.INSTANCE;
                    } catch (Throwable th6) {
                        th = th6;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th7) {
                            Throwable th8 = th7;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th8;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$consumeEachIndexed$12.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$consumeEachIndexed$12.L$1 = function12;
                channelsKt__Channels_commonKt$consumeEachIndexed$12.L$2 = intRef;
                channelsKt__Channels_commonKt$consumeEachIndexed$12.L$3 = receiveChannel5;
                channelsKt__Channels_commonKt$consumeEachIndexed$12.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$consumeEachIndexed$12.L$5 = th2;
                channelsKt__Channels_commonKt$consumeEachIndexed$12.L$6 = receiveChannel4;
                channelsKt__Channels_commonKt$consumeEachIndexed$12.L$7 = channelIterator;
                channelsKt__Channels_commonKt$consumeEachIndexed$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$consumeEachIndexed$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return Unit.INSTANCE;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$consumeEachIndexed$1 = new ChannelsKt__Channels_commonKt$consumeEachIndexed$1(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$consumeEachIndexed$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$consumeEachIndexed$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$consumeEachIndexed$12.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$consumeEachIndexed$12.L$1 = function12;
            channelsKt__Channels_commonKt$consumeEachIndexed$12.L$2 = intRef;
            channelsKt__Channels_commonKt$consumeEachIndexed$12.L$3 = receiveChannel5;
            channelsKt__Channels_commonKt$consumeEachIndexed$12.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$consumeEachIndexed$12.L$5 = th2;
            channelsKt__Channels_commonKt$consumeEachIndexed$12.L$6 = receiveChannel4;
            channelsKt__Channels_commonKt$consumeEachIndexed$12.L$7 = channelIterator;
            channelsKt__Channels_commonKt$consumeEachIndexed$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$consumeEachIndexed$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th9) {
            th = th9;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0063  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0094 A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a2 A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002c  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object elementAt(ReceiveChannel<? extends E> receiveChannel, int i, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$elementAt$1 channelsKt__Channels_commonKt$elementAt$1;
        int i2;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        Throwable th2;
        ReceiveChannel<? extends E> receiveChannel4;
        Object obj;
        ChannelsKt__Channels_commonKt$elementAt$1 channelsKt__Channels_commonKt$elementAt$12;
        ChannelIterator channelIterator;
        int i3;
        ReceiveChannel<? extends E> receiveChannel5;
        int i4;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        ChannelIterator channelIterator2;
        int i5;
        Object hasNext;
        int i6 = i;
        Continuation<? super E> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$elementAt$1) {
            channelsKt__Channels_commonKt$elementAt$1 = (ChannelsKt__Channels_commonKt$elementAt$1) continuation2;
            if ((channelsKt__Channels_commonKt$elementAt$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$elementAt$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$elementAt$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i2 = channelsKt__Channels_commonKt$elementAt$1.label;
                String str = "ReceiveChannel doesn't contain element at index ";
                if (i2 != 0) {
                    ResultKt.throwOnFailure(obj2);
                    Throwable th3 = null;
                    if (i6 >= 0) {
                        try {
                            th2 = th3;
                            channelsKt__Channels_commonKt$elementAt$12 = channelsKt__Channels_commonKt$elementAt$1;
                            obj = coroutine_suspended;
                            channelIterator2 = receiveChannel.iterator();
                            receiveChannel6 = receiveChannel;
                            receiveChannel2 = receiveChannel6;
                            receiveChannel5 = receiveChannel2;
                            i5 = 0;
                            i3 = i6;
                            receiveChannel7 = receiveChannel5;
                        } catch (Throwable th4) {
                            th = th4;
                            receiveChannel2 = receiveChannel;
                            th = th;
                            throw th;
                        }
                    } else {
                        Boxing.boxInt(i).intValue();
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(i6);
                        sb.append('.');
                        throw new IndexOutOfBoundsException(sb.toString());
                    }
                } else if (i2 == 1) {
                    i4 = channelsKt__Channels_commonKt$elementAt$1.I$1;
                    ChannelIterator channelIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$elementAt$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAt$1.L$4;
                    Throwable th5 = (Throwable) channelsKt__Channels_commonKt$elementAt$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAt$1.L$2;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAt$1.L$1;
                    int i7 = channelsKt__Channels_commonKt$elementAt$1.I$0;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAt$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        Throwable th6 = th5;
                        channelsKt__Channels_commonKt$elementAt$12 = channelsKt__Channels_commonKt$elementAt$1;
                        receiveChannel2 = receiveChannel9;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel8;
                        channelIterator = channelIterator3;
                        i3 = i7;
                        th2 = th6;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            int i8 = i4 + 1;
                            if (i3 == i4) {
                                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                            } else {
                                receiveChannel6 = receiveChannel4;
                                receiveChannel7 = receiveChannel3;
                                channelIterator2 = channelIterator;
                                i5 = i8;
                            }
                            ChannelsKt.cancelConsumed(receiveChannel2, th2);
                            return next;
                        }
                        Boxing.boxInt(i3).intValue();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(i3);
                        sb2.append('.');
                        throw new IndexOutOfBoundsException(sb2.toString());
                    } catch (Throwable th7) {
                        th = th7;
                        receiveChannel2 = receiveChannel9;
                        try {
                            throw th;
                        } catch (Throwable th8) {
                            Throwable th9 = th8;
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th9;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$elementAt$12.L$0 = receiveChannel7;
                channelsKt__Channels_commonKt$elementAt$12.I$0 = i3;
                channelsKt__Channels_commonKt$elementAt$12.L$1 = receiveChannel6;
                channelsKt__Channels_commonKt$elementAt$12.L$2 = receiveChannel2;
                channelsKt__Channels_commonKt$elementAt$12.L$3 = th2;
                channelsKt__Channels_commonKt$elementAt$12.L$4 = receiveChannel5;
                channelsKt__Channels_commonKt$elementAt$12.L$5 = channelIterator2;
                channelsKt__Channels_commonKt$elementAt$12.I$1 = i5;
                channelsKt__Channels_commonKt$elementAt$12.label = 1;
                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$elementAt$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel7;
                i4 = i5;
                channelIterator = channelIterator2;
                receiveChannel4 = receiveChannel6;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                }
                Boxing.boxInt(i3).intValue();
                StringBuilder sb22 = new StringBuilder();
                sb22.append(str);
                sb22.append(i3);
                sb22.append('.');
                throw new IndexOutOfBoundsException(sb22.toString());
                return obj;
            }
        }
        channelsKt__Channels_commonKt$elementAt$1 = new ChannelsKt__Channels_commonKt$elementAt$1(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$elementAt$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i2 = channelsKt__Channels_commonKt$elementAt$1.label;
        String str2 = "ReceiveChannel doesn't contain element at index ";
        if (i2 != 0) {
        }
        try {
            channelsKt__Channels_commonKt$elementAt$12.L$0 = receiveChannel7;
            channelsKt__Channels_commonKt$elementAt$12.I$0 = i3;
            channelsKt__Channels_commonKt$elementAt$12.L$1 = receiveChannel6;
            channelsKt__Channels_commonKt$elementAt$12.L$2 = receiveChannel2;
            channelsKt__Channels_commonKt$elementAt$12.L$3 = th2;
            channelsKt__Channels_commonKt$elementAt$12.L$4 = receiveChannel5;
            channelsKt__Channels_commonKt$elementAt$12.L$5 = channelIterator2;
            channelsKt__Channels_commonKt$elementAt$12.I$1 = i5;
            channelsKt__Channels_commonKt$elementAt$12.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$elementAt$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th10) {
            th = th10;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x009b A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00aa A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object elementAtOrElse(ReceiveChannel<? extends E> receiveChannel, int i, Function1<? super Integer, ? extends E> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$elementAtOrElse$1 channelsKt__Channels_commonKt$elementAtOrElse$1;
        int i2;
        ReceiveChannel<? extends E> receiveChannel2;
        int i3;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel3;
        int i4;
        Object obj;
        ChannelsKt__Channels_commonKt$elementAtOrElse$1 channelsKt__Channels_commonKt$elementAtOrElse$12;
        Throwable th;
        Function1<? super Integer, ? extends E> function12;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        int i5;
        ReceiveChannel<? extends E> receiveChannel6;
        Throwable th2;
        int i6;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$elementAtOrElse$1) {
            channelsKt__Channels_commonKt$elementAtOrElse$1 = (ChannelsKt__Channels_commonKt$elementAtOrElse$1) continuation;
            if ((channelsKt__Channels_commonKt$elementAtOrElse$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$elementAtOrElse$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$elementAtOrElse$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i2 = channelsKt__Channels_commonKt$elementAtOrElse$1.label;
                if (i2 != 0) {
                    ResultKt.throwOnFailure(obj2);
                    Throwable th3 = null;
                    if (i < 0) {
                        try {
                            Object invoke = function1.invoke(Boxing.boxInt(i));
                            InlineMarker.finallyStart(4);
                            ChannelsKt.cancelConsumed(receiveChannel, th3);
                            InlineMarker.finallyEnd(4);
                            return invoke;
                        } catch (Throwable th4) {
                            receiveChannel2 = receiveChannel;
                            th = th4;
                            throw th;
                        }
                    } else {
                        i6 = 0;
                        th2 = th3;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel;
                        function12 = function1;
                        receiveChannel2 = receiveChannel5;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$elementAtOrElse$12 = channelsKt__Channels_commonKt$elementAtOrElse$1;
                        i5 = i;
                        receiveChannel6 = receiveChannel2;
                    }
                } else if (i2 == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$elementAtOrElse$1.L$5;
                    i3 = channelsKt__Channels_commonKt$elementAtOrElse$1.I$1;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAtOrElse$1.L$4;
                    th = (Throwable) channelsKt__Channels_commonKt$elementAtOrElse$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAtOrElse$1.L$2;
                    Function1<? super Integer, ? extends E> function13 = (Function1) channelsKt__Channels_commonKt$elementAtOrElse$1.L$1;
                    i4 = channelsKt__Channels_commonKt$elementAtOrElse$1.I$0;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAtOrElse$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        ChannelsKt__Channels_commonKt$elementAtOrElse$1 channelsKt__Channels_commonKt$elementAtOrElse$13 = channelsKt__Channels_commonKt$elementAtOrElse$1;
                        receiveChannel4 = receiveChannel7;
                        receiveChannel2 = receiveChannel8;
                        channelsKt__Channels_commonKt$elementAtOrElse$12 = channelsKt__Channels_commonKt$elementAtOrElse$13;
                        Function1<? super Integer, ? extends E> function14 = function13;
                        obj = coroutine_suspended;
                        function12 = function14;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            int i7 = i3 + 1;
                            if (i4 == i3) {
                                InlineMarker.finallyStart(3);
                                ChannelsKt.cancelConsumed(receiveChannel2, th);
                                InlineMarker.finallyEnd(3);
                            } else {
                                receiveChannel5 = receiveChannel4;
                                i5 = i4;
                                receiveChannel6 = receiveChannel3;
                                th2 = th;
                                i6 = i7;
                            }
                            InlineMarker.finallyStart(3);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(3);
                            return next;
                        }
                        Object invoke2 = function12.invoke(Boxing.boxInt(i4));
                        InlineMarker.finallyStart(2);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(2);
                        return invoke2;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$elementAtOrElse$12.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$elementAtOrElse$12.I$0 = i5;
                channelsKt__Channels_commonKt$elementAtOrElse$12.L$1 = function12;
                channelsKt__Channels_commonKt$elementAtOrElse$12.L$2 = receiveChannel2;
                channelsKt__Channels_commonKt$elementAtOrElse$12.L$3 = th2;
                channelsKt__Channels_commonKt$elementAtOrElse$12.L$4 = receiveChannel5;
                channelsKt__Channels_commonKt$elementAtOrElse$12.I$1 = i6;
                channelsKt__Channels_commonKt$elementAtOrElse$12.L$5 = channelIterator;
                channelsKt__Channels_commonKt$elementAtOrElse$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$elementAtOrElse$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel6;
                i3 = i6;
                th = th2;
                i4 = i5;
                receiveChannel4 = receiveChannel5;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                }
                Object invoke22 = function12.invoke(Boxing.boxInt(i4));
                InlineMarker.finallyStart(2);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(2);
                return invoke22;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$elementAtOrElse$1 = new ChannelsKt__Channels_commonKt$elementAtOrElse$1(continuation);
        Object obj22 = channelsKt__Channels_commonKt$elementAtOrElse$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i2 = channelsKt__Channels_commonKt$elementAtOrElse$1.label;
        if (i2 != 0) {
        }
        try {
            channelsKt__Channels_commonKt$elementAtOrElse$12.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$elementAtOrElse$12.I$0 = i5;
            channelsKt__Channels_commonKt$elementAtOrElse$12.L$1 = function12;
            channelsKt__Channels_commonKt$elementAtOrElse$12.L$2 = receiveChannel2;
            channelsKt__Channels_commonKt$elementAtOrElse$12.L$3 = th2;
            channelsKt__Channels_commonKt$elementAtOrElse$12.L$4 = receiveChannel5;
            channelsKt__Channels_commonKt$elementAtOrElse$12.I$1 = i6;
            channelsKt__Channels_commonKt$elementAtOrElse$12.L$5 = channelIterator;
            channelsKt__Channels_commonKt$elementAtOrElse$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$elementAtOrElse$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0093 A[Catch:{ all -> 0x0049 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object elementAtOrNull(ReceiveChannel<? extends E> receiveChannel, int i, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$elementAtOrNull$1 channelsKt__Channels_commonKt$elementAtOrNull$1;
        int i2;
        ReceiveChannel<? extends E> receiveChannel2;
        int i3;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel3;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$elementAtOrNull$1 channelsKt__Channels_commonKt$elementAtOrNull$12;
        Object obj;
        int i4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        int i5;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$elementAtOrNull$1) {
            channelsKt__Channels_commonKt$elementAtOrNull$1 = (ChannelsKt__Channels_commonKt$elementAtOrNull$1) continuation;
            if ((channelsKt__Channels_commonKt$elementAtOrNull$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$elementAtOrNull$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$elementAtOrNull$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i2 = channelsKt__Channels_commonKt$elementAtOrNull$1.label;
                if (i2 != 0) {
                    ResultKt.throwOnFailure(obj3);
                    Throwable th2 = null;
                    if (i < 0) {
                        ChannelsKt.cancelConsumed(receiveChannel, th2);
                        return null;
                    }
                    i5 = 0;
                    try {
                        th = th2;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        i4 = i;
                        receiveChannel6 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$elementAtOrNull$12 = channelsKt__Channels_commonKt$elementAtOrNull$1;
                        receiveChannel5 = receiveChannel6;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i2 == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$elementAtOrNull$1.L$4;
                    i3 = channelsKt__Channels_commonKt$elementAtOrNull$1.I$1;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAtOrNull$1.L$3;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$elementAtOrNull$1.L$2;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAtOrNull$1.L$1;
                    int i6 = channelsKt__Channels_commonKt$elementAtOrNull$1.I$0;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$elementAtOrNull$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Throwable th5 = th4;
                        channelsKt__Channels_commonKt$elementAtOrNull$12 = channelsKt__Channels_commonKt$elementAtOrNull$1;
                        receiveChannel5 = receiveChannel7;
                        obj = coroutine_suspended;
                        i4 = i6;
                        th = th5;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            int i7 = i3 + 1;
                            if (i4 == i3) {
                                ChannelsKt.cancelConsumed(receiveChannel4, th);
                            } else {
                                receiveChannel2 = receiveChannel4;
                                receiveChannel6 = receiveChannel3;
                                obj2 = obj;
                                i5 = i7;
                            }
                            ChannelsKt.cancelConsumed(receiveChannel4, th);
                            return next;
                        }
                        ChannelsKt.cancelConsumed(receiveChannel4, th);
                        return null;
                    } catch (Throwable th6) {
                        th = th6;
                        receiveChannel2 = receiveChannel4;
                        try {
                            throw th;
                        } catch (Throwable th7) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$elementAtOrNull$12.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$elementAtOrNull$12.I$0 = i4;
                channelsKt__Channels_commonKt$elementAtOrNull$12.L$1 = receiveChannel2;
                channelsKt__Channels_commonKt$elementAtOrNull$12.L$2 = th;
                channelsKt__Channels_commonKt$elementAtOrNull$12.L$3 = receiveChannel5;
                channelsKt__Channels_commonKt$elementAtOrNull$12.I$1 = i5;
                channelsKt__Channels_commonKt$elementAtOrNull$12.L$4 = channelIterator;
                channelsKt__Channels_commonKt$elementAtOrNull$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$elementAtOrNull$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj4 = hasNext;
                receiveChannel3 = receiveChannel6;
                i3 = i5;
                obj = obj2;
                receiveChannel4 = receiveChannel2;
                obj3 = obj4;
                if (!((Boolean) obj3).booleanValue()) {
                    ChannelsKt.cancelConsumed(receiveChannel4, th);
                }
                ChannelsKt.cancelConsumed(receiveChannel4, th);
                return null;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$elementAtOrNull$1 = new ChannelsKt__Channels_commonKt$elementAtOrNull$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$elementAtOrNull$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i2 = channelsKt__Channels_commonKt$elementAtOrNull$1.label;
        if (i2 != 0) {
        }
        try {
            channelsKt__Channels_commonKt$elementAtOrNull$12.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$elementAtOrNull$12.I$0 = i4;
            channelsKt__Channels_commonKt$elementAtOrNull$12.L$1 = receiveChannel2;
            channelsKt__Channels_commonKt$elementAtOrNull$12.L$2 = th;
            channelsKt__Channels_commonKt$elementAtOrNull$12.L$3 = receiveChannel5;
            channelsKt__Channels_commonKt$elementAtOrNull$12.I$1 = i5;
            channelsKt__Channels_commonKt$elementAtOrNull$12.L$4 = channelIterator;
            channelsKt__Channels_commonKt$elementAtOrNull$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$elementAtOrNull$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th8) {
            th = th8;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0096 A[Catch:{ all -> 0x00cb }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a1 A[Catch:{ all -> 0x00cb }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00bf A[SYNTHETIC, Splitter:B:32:0x00bf] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0027  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object find(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$find$1 channelsKt__Channels_commonKt$find$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$find$1 channelsKt__Channels_commonKt$find$12;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Object hasNext;
        Continuation<? super E> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$find$1) {
            channelsKt__Channels_commonKt$find$1 = (ChannelsKt__Channels_commonKt$find$1) continuation2;
            if ((channelsKt__Channels_commonKt$find$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$find$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$find$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$find$1.label;
                Object obj3 = null;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        receiveChannel4 = receiveChannel;
                        function12 = function1;
                        th2 = null;
                        channelsKt__Channels_commonKt$find$12 = channelsKt__Channels_commonKt$find$1;
                        obj = coroutine_suspended;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel7 = receiveChannel4;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$find$1.L$7;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$find$1.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$find$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$find$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$find$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$find$1.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$find$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$find$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        ReceiveChannel<? extends E> receiveChannel11 = receiveChannel8;
                        channelsKt__Channels_commonKt$find$12 = channelsKt__Channels_commonKt$find$1;
                        receiveChannel6 = receiveChannel10;
                        channelIterator = channelIterator2;
                        receiveChannel2 = receiveChannel11;
                        ReceiveChannel<? extends E> receiveChannel12 = receiveChannel9;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel12;
                        Function1<? super E, Boolean> function14 = function13;
                        th2 = th4;
                        function12 = function14;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                InlineMarker.finallyStart(2);
                                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                                InlineMarker.finallyEnd(2);
                                obj3 = next;
                            } else {
                                receiveChannel7 = receiveChannel3;
                            }
                            InlineMarker.finallyStart(2);
                            ChannelsKt.cancelConsumed(receiveChannel2, th2);
                            InlineMarker.finallyEnd(2);
                            obj3 = next;
                        } else {
                            Unit unit = Unit.INSTANCE;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th2);
                            InlineMarker.finallyEnd(1);
                        }
                        return obj3;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$find$12.L$0 = receiveChannel7;
                channelsKt__Channels_commonKt$find$12.L$1 = function12;
                channelsKt__Channels_commonKt$find$12.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$find$12.L$3 = receiveChannel5;
                channelsKt__Channels_commonKt$find$12.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$find$12.L$5 = th2;
                channelsKt__Channels_commonKt$find$12.L$6 = receiveChannel4;
                channelsKt__Channels_commonKt$find$12.L$7 = channelIterator;
                channelsKt__Channels_commonKt$find$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$find$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj4 = hasNext;
                receiveChannel3 = receiveChannel7;
                obj2 = obj4;
                if (!((Boolean) obj2).booleanValue()) {
                }
                return obj3;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$find$1 = new ChannelsKt__Channels_commonKt$find$1(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$find$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$find$1.label;
        Object obj32 = null;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$find$12.L$0 = receiveChannel7;
            channelsKt__Channels_commonKt$find$12.L$1 = function12;
            channelsKt__Channels_commonKt$find$12.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$find$12.L$3 = receiveChannel5;
            channelsKt__Channels_commonKt$find$12.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$find$12.L$5 = th2;
            channelsKt__Channels_commonKt$find$12.L$6 = receiveChannel4;
            channelsKt__Channels_commonKt$find$12.L$7 = channelIterator;
            channelsKt__Channels_commonKt$find$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$find$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x009c A[Catch:{ all -> 0x00cd }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a9 A[Catch:{ all -> 0x00cd }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object findLast(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$findLast$1 channelsKt__Channels_commonKt$findLast$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ObjectRef objectRef;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel4;
        Object obj;
        ChannelsKt__Channels_commonKt$findLast$1 channelsKt__Channels_commonKt$findLast$12;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Throwable th2;
        ObjectRef objectRef2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$findLast$1) {
            channelsKt__Channels_commonKt$findLast$1 = (ChannelsKt__Channels_commonKt$findLast$1) continuation;
            if ((channelsKt__Channels_commonKt$findLast$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$findLast$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$findLast$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$findLast$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    ObjectRef objectRef3 = new ObjectRef();
                    objectRef3.element = null;
                    try {
                        objectRef2 = objectRef3;
                        channelsKt__Channels_commonKt$findLast$12 = channelsKt__Channels_commonKt$findLast$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$findLast$1.L$8;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$findLast$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$findLast$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$findLast$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$findLast$1.L$4;
                    ObjectRef objectRef4 = (ObjectRef) channelsKt__Channels_commonKt$findLast$1.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$findLast$1.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$findLast$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel11 = (ReceiveChannel) channelsKt__Channels_commonKt$findLast$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        Throwable th5 = th4;
                        receiveChannel5 = receiveChannel8;
                        receiveChannel3 = receiveChannel11;
                        objectRef = objectRef4;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel9;
                        function12 = function13;
                        th = th5;
                        ReceiveChannel<? extends E> receiveChannel12 = receiveChannel10;
                        channelsKt__Channels_commonKt$findLast$12 = channelsKt__Channels_commonKt$findLast$1;
                        receiveChannel6 = receiveChannel12;
                        if (!((Boolean) obj2).booleanValue()) {
                            T next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                objectRef.element = next;
                            }
                            receiveChannel7 = receiveChannel4;
                            th2 = th;
                            objectRef2 = objectRef;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return objectRef.element;
                    } catch (Throwable th6) {
                        th = th6;
                        receiveChannel2 = receiveChannel9;
                        try {
                            throw th;
                        } catch (Throwable th7) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$findLast$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$findLast$12.L$1 = function12;
                channelsKt__Channels_commonKt$findLast$12.L$2 = receiveChannel7;
                channelsKt__Channels_commonKt$findLast$12.L$3 = objectRef2;
                channelsKt__Channels_commonKt$findLast$12.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$findLast$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$findLast$12.L$6 = th2;
                channelsKt__Channels_commonKt$findLast$12.L$7 = receiveChannel5;
                channelsKt__Channels_commonKt$findLast$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$findLast$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$findLast$12);
                if (hasNext != obj) {
                    return obj;
                }
                Throwable th8 = th2;
                receiveChannel4 = receiveChannel7;
                obj2 = hasNext;
                objectRef = objectRef2;
                th = th8;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return objectRef.element;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$findLast$1 = new ChannelsKt__Channels_commonKt$findLast$1(continuation);
        Object obj22 = channelsKt__Channels_commonKt$findLast$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$findLast$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$findLast$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$findLast$12.L$1 = function12;
            channelsKt__Channels_commonKt$findLast$12.L$2 = receiveChannel7;
            channelsKt__Channels_commonKt$findLast$12.L$3 = objectRef2;
            channelsKt__Channels_commonKt$findLast$12.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$findLast$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$findLast$12.L$6 = th2;
            channelsKt__Channels_commonKt$findLast$12.L$7 = receiveChannel5;
            channelsKt__Channels_commonKt$findLast$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$findLast$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$findLast$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th9) {
            th = th9;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0072 A[Catch:{ all -> 0x003e }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007a A[SYNTHETIC, Splitter:B:27:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object first(ReceiveChannel<? extends E> receiveChannel, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$first$1 channelsKt__Channels_commonKt$first$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        ChannelIterator channelIterator;
        Throwable th2;
        if (continuation instanceof ChannelsKt__Channels_commonKt$first$1) {
            channelsKt__Channels_commonKt$first$1 = (ChannelsKt__Channels_commonKt$first$1) continuation;
            if ((channelsKt__Channels_commonKt$first$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$first$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$first$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$first$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Throwable th3 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$first$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$first$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$first$1.L$2 = th3;
                        channelsKt__Channels_commonKt$first$1.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$first$1.L$4 = it;
                        channelsKt__Channels_commonKt$first$1.label = 1;
                        Object hasNext = it.hasNext(channelsKt__Channels_commonKt$first$1);
                        if (hasNext == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        th2 = th3;
                        obj = hasNext;
                        ChannelIterator channelIterator2 = it;
                        receiveChannel2 = receiveChannel;
                        channelIterator = channelIterator2;
                    } catch (Throwable th4) {
                        receiveChannel2 = receiveChannel;
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th5;
                        }
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$first$1.L$4;
                    ReceiveChannel receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$first$1.L$3;
                    th2 = (Throwable) channelsKt__Channels_commonKt$first$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$first$1.L$1;
                    ReceiveChannel receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$first$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Throwable th6) {
                        th = th6;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (!((Boolean) obj).booleanValue()) {
                    Object next = channelIterator.next();
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    return next;
                }
                throw new NoSuchElementException("ReceiveChannel is empty.");
            }
        }
        channelsKt__Channels_commonKt$first$1 = new ChannelsKt__Channels_commonKt$first$1(continuation);
        obj = channelsKt__Channels_commonKt$first$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$first$1.label;
        if (i != 0) {
        }
        if (!((Boolean) obj).booleanValue()) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085 A[Catch:{ all -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0092 A[Catch:{ all -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object first(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$first$3 channelsKt__Channels_commonKt$first$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$first$3 channelsKt__Channels_commonKt$first$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$first$3) {
            channelsKt__Channels_commonKt$first$3 = (ChannelsKt__Channels_commonKt$first$3) continuation;
            if ((channelsKt__Channels_commonKt$first$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$first$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$first$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$first$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        channelsKt__Channels_commonKt$first$32 = channelsKt__Channels_commonKt$first$3;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$first$3.L$6;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$first$3.L$5;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$first$3.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$first$3.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$first$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$first$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$first$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        th = th4;
                        function12 = function13;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$first$32 = channelsKt__Channels_commonKt$first$3;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                InlineMarker.finallyStart(2);
                                ChannelsKt.cancelConsumed(receiveChannel2, th);
                                InlineMarker.finallyEnd(2);
                            } else {
                                receiveChannel6 = receiveChannel4;
                                obj2 = obj;
                                th2 = th;
                            }
                            InlineMarker.finallyStart(2);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(2);
                            return next;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        throw new NoSuchElementException("ReceiveChannel contains no element matching the predicate.");
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$first$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$first$32.L$1 = function12;
                channelsKt__Channels_commonKt$first$32.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$first$32.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$first$32.L$4 = th2;
                channelsKt__Channels_commonKt$first$32.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$first$32.L$6 = channelIterator;
                channelsKt__Channels_commonKt$first$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$first$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                }
                Unit unit2 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                throw new NoSuchElementException("ReceiveChannel contains no element matching the predicate.");
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$first$3 = new ChannelsKt__Channels_commonKt$first$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$first$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$first$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$first$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$first$32.L$1 = function12;
            channelsKt__Channels_commonKt$first$32.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$first$32.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$first$32.L$4 = th2;
            channelsKt__Channels_commonKt$first$32.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$first$32.L$6 = channelIterator;
            channelsKt__Channels_commonKt$first$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$first$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0077 A[SYNTHETIC, Splitter:B:26:0x0077] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object firstOrNull(ReceiveChannel<? extends E> receiveChannel, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$firstOrNull$1 channelsKt__Channels_commonKt$firstOrNull$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        ChannelIterator channelIterator;
        Throwable th2;
        if (continuation instanceof ChannelsKt__Channels_commonKt$firstOrNull$1) {
            channelsKt__Channels_commonKt$firstOrNull$1 = (ChannelsKt__Channels_commonKt$firstOrNull$1) continuation;
            if ((channelsKt__Channels_commonKt$firstOrNull$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$firstOrNull$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$firstOrNull$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$firstOrNull$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Throwable th3 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$firstOrNull$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$firstOrNull$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$firstOrNull$1.L$2 = th3;
                        channelsKt__Channels_commonKt$firstOrNull$1.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$firstOrNull$1.L$4 = it;
                        channelsKt__Channels_commonKt$firstOrNull$1.label = 1;
                        Object hasNext = it.hasNext(channelsKt__Channels_commonKt$firstOrNull$1);
                        if (hasNext == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        th2 = th3;
                        obj = hasNext;
                        ChannelIterator channelIterator2 = it;
                        receiveChannel2 = receiveChannel;
                        channelIterator = channelIterator2;
                    } catch (Throwable th4) {
                        receiveChannel2 = receiveChannel;
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th5;
                        }
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$firstOrNull$1.L$4;
                    ReceiveChannel receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$firstOrNull$1.L$3;
                    th2 = (Throwable) channelsKt__Channels_commonKt$firstOrNull$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$firstOrNull$1.L$1;
                    ReceiveChannel receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$firstOrNull$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Throwable th6) {
                        th = th6;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (((Boolean) obj).booleanValue()) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    return null;
                }
                Object next = channelIterator.next();
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                return next;
            }
        }
        channelsKt__Channels_commonKt$firstOrNull$1 = new ChannelsKt__Channels_commonKt$firstOrNull$1(continuation);
        obj = channelsKt__Channels_commonKt$firstOrNull$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$firstOrNull$1.label;
        if (i != 0) {
        }
        if (((Boolean) obj).booleanValue()) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0086 A[Catch:{ all -> 0x00be }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0093 A[Catch:{ all -> 0x00be }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object firstOrNull(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$firstOrNull$3 channelsKt__Channels_commonKt$firstOrNull$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$firstOrNull$3 channelsKt__Channels_commonKt$firstOrNull$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$firstOrNull$3) {
            channelsKt__Channels_commonKt$firstOrNull$3 = (ChannelsKt__Channels_commonKt$firstOrNull$3) continuation;
            if ((channelsKt__Channels_commonKt$firstOrNull$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$firstOrNull$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$firstOrNull$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$firstOrNull$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        channelsKt__Channels_commonKt$firstOrNull$32 = channelsKt__Channels_commonKt$firstOrNull$3;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$firstOrNull$3.L$6;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$firstOrNull$3.L$5;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$firstOrNull$3.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$firstOrNull$3.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$firstOrNull$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$firstOrNull$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$firstOrNull$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        th = th4;
                        function12 = function13;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$firstOrNull$32 = channelsKt__Channels_commonKt$firstOrNull$3;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                InlineMarker.finallyStart(2);
                                ChannelsKt.cancelConsumed(receiveChannel2, th);
                                InlineMarker.finallyEnd(2);
                            } else {
                                receiveChannel6 = receiveChannel4;
                                obj2 = obj;
                                th2 = th;
                            }
                            InlineMarker.finallyStart(2);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(2);
                            return next;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return null;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$firstOrNull$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$firstOrNull$32.L$1 = function12;
                channelsKt__Channels_commonKt$firstOrNull$32.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$firstOrNull$32.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$firstOrNull$32.L$4 = th2;
                channelsKt__Channels_commonKt$firstOrNull$32.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$firstOrNull$32.L$6 = channelIterator;
                channelsKt__Channels_commonKt$firstOrNull$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$firstOrNull$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                }
                Unit unit2 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return null;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$firstOrNull$3 = new ChannelsKt__Channels_commonKt$firstOrNull$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$firstOrNull$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$firstOrNull$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$firstOrNull$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$firstOrNull$32.L$1 = function12;
            channelsKt__Channels_commonKt$firstOrNull$32.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$firstOrNull$32.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$firstOrNull$32.L$4 = th2;
            channelsKt__Channels_commonKt$firstOrNull$32.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$firstOrNull$32.L$6 = channelIterator;
            channelsKt__Channels_commonKt$firstOrNull$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$firstOrNull$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0094 A[Catch:{ all -> 0x00cb }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a2 A[Catch:{ all -> 0x00cb }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00c0 A[Catch:{ all -> 0x00cb }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object indexOf(ReceiveChannel<? extends E> receiveChannel, E e, Continuation<? super Integer> continuation) {
        ChannelsKt__Channels_commonKt$indexOf$1 channelsKt__Channels_commonKt$indexOf$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        IntRef intRef;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$indexOf$1 channelsKt__Channels_commonKt$indexOf$12;
        E e2;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        IntRef intRef2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$indexOf$1) {
            channelsKt__Channels_commonKt$indexOf$1 = (ChannelsKt__Channels_commonKt$indexOf$1) continuation;
            if ((channelsKt__Channels_commonKt$indexOf$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$indexOf$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$indexOf$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$indexOf$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    IntRef intRef3 = new IntRef();
                    intRef3.element = 0;
                    try {
                        intRef2 = intRef3;
                        obj2 = coroutine_suspended;
                        th2 = null;
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        e2 = e;
                        receiveChannel3 = receiveChannel5;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$indexOf$12 = channelsKt__Channels_commonKt$indexOf$1;
                        receiveChannel2 = receiveChannel3;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$indexOf$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOf$1.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$indexOf$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOf$1.L$4;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOf$1.L$3;
                    IntRef intRef4 = (IntRef) channelsKt__Channels_commonKt$indexOf$1.L$2;
                    E e3 = channelsKt__Channels_commonKt$indexOf$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOf$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        intRef = intRef4;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$indexOf$12 = channelsKt__Channels_commonKt$indexOf$1;
                        receiveChannel2 = receiveChannel10;
                        E e4 = e3;
                        th = th4;
                        e2 = e4;
                        if (!((Boolean) obj3).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                        } else {
                            if (Intrinsics.areEqual((Object) e2, channelIterator.next())) {
                                Integer boxInt = Boxing.boxInt(intRef.element);
                            } else {
                                intRef.element++;
                                receiveChannel6 = receiveChannel4;
                                obj2 = obj;
                                th2 = th;
                                intRef2 = intRef;
                            }
                            Integer boxInt2 = Boxing.boxInt(intRef.element);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            return boxInt2;
                        }
                        Unit unit2 = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        return Boxing.boxInt(-1);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$indexOf$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$indexOf$12.L$1 = e2;
                channelsKt__Channels_commonKt$indexOf$12.L$2 = intRef2;
                channelsKt__Channels_commonKt$indexOf$12.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$indexOf$12.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$indexOf$12.L$5 = th2;
                channelsKt__Channels_commonKt$indexOf$12.L$6 = receiveChannel5;
                channelsKt__Channels_commonKt$indexOf$12.L$7 = channelIterator;
                channelsKt__Channels_commonKt$indexOf$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$indexOf$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                intRef = intRef2;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                }
                Unit unit22 = Unit.INSTANCE;
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                return Boxing.boxInt(-1);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$indexOf$1 = new ChannelsKt__Channels_commonKt$indexOf$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$indexOf$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$indexOf$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$indexOf$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$indexOf$12.L$1 = e2;
            channelsKt__Channels_commonKt$indexOf$12.L$2 = intRef2;
            channelsKt__Channels_commonKt$indexOf$12.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$indexOf$12.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$indexOf$12.L$5 = th2;
            channelsKt__Channels_commonKt$indexOf$12.L$6 = receiveChannel5;
            channelsKt__Channels_commonKt$indexOf$12.L$7 = channelIterator;
            channelsKt__Channels_commonKt$indexOf$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$indexOf$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0096 A[Catch:{ all -> 0x00e0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a4 A[Catch:{ all -> 0x00e0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00cf A[Catch:{ all -> 0x00e0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object indexOfFirst(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super Integer> continuation) {
        ChannelsKt__Channels_commonKt$indexOfFirst$1 channelsKt__Channels_commonKt$indexOfFirst$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        IntRef intRef;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$indexOfFirst$1 channelsKt__Channels_commonKt$indexOfFirst$12;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        IntRef intRef2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$indexOfFirst$1) {
            channelsKt__Channels_commonKt$indexOfFirst$1 = (ChannelsKt__Channels_commonKt$indexOfFirst$1) continuation;
            if ((channelsKt__Channels_commonKt$indexOfFirst$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$indexOfFirst$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$indexOfFirst$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$indexOfFirst$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    IntRef intRef3 = new IntRef();
                    intRef3.element = 0;
                    try {
                        intRef2 = intRef3;
                        obj2 = coroutine_suspended;
                        th2 = null;
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$indexOfFirst$12 = channelsKt__Channels_commonKt$indexOfFirst$1;
                        receiveChannel2 = receiveChannel3;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$indexOfFirst$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOfFirst$1.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$indexOfFirst$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOfFirst$1.L$4;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOfFirst$1.L$3;
                    IntRef intRef4 = (IntRef) channelsKt__Channels_commonKt$indexOfFirst$1.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$indexOfFirst$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOfFirst$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        intRef = intRef4;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$indexOfFirst$12 = channelsKt__Channels_commonKt$indexOfFirst$1;
                        receiveChannel2 = receiveChannel10;
                        Function1<? super E, Boolean> function14 = function13;
                        th = th4;
                        function12 = function14;
                        if (!((Boolean) obj3).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                        } else {
                            if (((Boolean) function12.invoke(channelIterator.next())).booleanValue()) {
                                Integer boxInt = Boxing.boxInt(intRef.element);
                            } else {
                                intRef.element++;
                                receiveChannel6 = receiveChannel4;
                                obj2 = obj;
                                th2 = th;
                                intRef2 = intRef;
                            }
                            Integer boxInt2 = Boxing.boxInt(intRef.element);
                            InlineMarker.finallyStart(2);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(2);
                            return boxInt2;
                        }
                        Unit unit2 = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return Boxing.boxInt(-1);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$indexOfFirst$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$indexOfFirst$12.L$1 = function12;
                channelsKt__Channels_commonKt$indexOfFirst$12.L$2 = intRef2;
                channelsKt__Channels_commonKt$indexOfFirst$12.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$indexOfFirst$12.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$indexOfFirst$12.L$5 = th2;
                channelsKt__Channels_commonKt$indexOfFirst$12.L$6 = receiveChannel5;
                channelsKt__Channels_commonKt$indexOfFirst$12.L$7 = channelIterator;
                channelsKt__Channels_commonKt$indexOfFirst$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$indexOfFirst$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                intRef = intRef2;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return Boxing.boxInt(-1);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$indexOfFirst$1 = new ChannelsKt__Channels_commonKt$indexOfFirst$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$indexOfFirst$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$indexOfFirst$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$indexOfFirst$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$indexOfFirst$12.L$1 = function12;
            channelsKt__Channels_commonKt$indexOfFirst$12.L$2 = intRef2;
            channelsKt__Channels_commonKt$indexOfFirst$12.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$indexOfFirst$12.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$indexOfFirst$12.L$5 = th2;
            channelsKt__Channels_commonKt$indexOfFirst$12.L$6 = receiveChannel5;
            channelsKt__Channels_commonKt$indexOfFirst$12.L$7 = channelIterator;
            channelsKt__Channels_commonKt$indexOfFirst$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$indexOfFirst$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a5 A[Catch:{ all -> 0x00e5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b4 A[Catch:{ all -> 0x00e5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00d3 A[Catch:{ all -> 0x00e5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object indexOfLast(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super Integer> continuation) {
        ChannelsKt__Channels_commonKt$indexOfLast$1 channelsKt__Channels_commonKt$indexOfLast$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        IntRef intRef;
        IntRef intRef2;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$indexOfLast$1 channelsKt__Channels_commonKt$indexOfLast$12;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        IntRef intRef3;
        IntRef intRef4;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$indexOfLast$1) {
            channelsKt__Channels_commonKt$indexOfLast$1 = (ChannelsKt__Channels_commonKt$indexOfLast$1) continuation;
            if ((channelsKt__Channels_commonKt$indexOfLast$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$indexOfLast$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$indexOfLast$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$indexOfLast$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    IntRef intRef5 = new IntRef();
                    intRef5.element = -1;
                    IntRef intRef6 = new IntRef();
                    intRef6.element = 0;
                    try {
                        intRef4 = intRef5;
                        intRef3 = intRef6;
                        th2 = null;
                        receiveChannel6 = receiveChannel;
                        function12 = function1;
                        channelsKt__Channels_commonKt$indexOfLast$12 = channelsKt__Channels_commonKt$indexOfLast$1;
                        receiveChannel3 = receiveChannel6;
                        receiveChannel2 = receiveChannel3;
                        channelIterator = receiveChannel.iterator();
                        obj2 = coroutine_suspended;
                        receiveChannel5 = receiveChannel2;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$indexOfLast$1.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOfLast$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$indexOfLast$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOfLast$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOfLast$1.L$4;
                    IntRef intRef7 = (IntRef) channelsKt__Channels_commonKt$indexOfLast$1.L$3;
                    IntRef intRef8 = (IntRef) channelsKt__Channels_commonKt$indexOfLast$1.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$indexOfLast$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$indexOfLast$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        intRef = intRef8;
                        th = th4;
                        function12 = function13;
                        intRef2 = intRef7;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$indexOfLast$12 = channelsKt__Channels_commonKt$indexOfLast$1;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                        } else {
                            if (((Boolean) function12.invoke(channelIterator.next())).booleanValue()) {
                                intRef.element = intRef2.element;
                            }
                            intRef2.element++;
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                            intRef3 = intRef2;
                            intRef4 = intRef;
                        }
                        Unit unit2 = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return Boxing.boxInt(intRef.element);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$indexOfLast$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$indexOfLast$12.L$1 = function12;
                channelsKt__Channels_commonKt$indexOfLast$12.L$2 = intRef4;
                channelsKt__Channels_commonKt$indexOfLast$12.L$3 = intRef3;
                channelsKt__Channels_commonKt$indexOfLast$12.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$indexOfLast$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$indexOfLast$12.L$6 = th2;
                channelsKt__Channels_commonKt$indexOfLast$12.L$7 = receiveChannel5;
                channelsKt__Channels_commonKt$indexOfLast$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$indexOfLast$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$indexOfLast$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                intRef = intRef4;
                intRef2 = intRef3;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return Boxing.boxInt(intRef.element);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$indexOfLast$1 = new ChannelsKt__Channels_commonKt$indexOfLast$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$indexOfLast$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$indexOfLast$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$indexOfLast$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$indexOfLast$12.L$1 = function12;
            channelsKt__Channels_commonKt$indexOfLast$12.L$2 = intRef4;
            channelsKt__Channels_commonKt$indexOfLast$12.L$3 = intRef3;
            channelsKt__Channels_commonKt$indexOfLast$12.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$indexOfLast$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$indexOfLast$12.L$6 = th2;
            channelsKt__Channels_commonKt$indexOfLast$12.L$7 = receiveChannel5;
            channelsKt__Channels_commonKt$indexOfLast$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$indexOfLast$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$indexOfLast$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0097 A[Catch:{ all -> 0x0068 }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c2 A[Catch:{ all -> 0x0044 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00cc A[SYNTHETIC, Splitter:B:44:0x00cc] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object last(ReceiveChannel<? extends E> receiveChannel, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$last$1 channelsKt__Channels_commonKt$last$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        Throwable th2;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object next;
        Object hasNext;
        ChannelIterator channelIterator2;
        ReceiveChannel<? extends E> receiveChannel5;
        Throwable th3;
        ReceiveChannel<? extends E> receiveChannel6;
        if (continuation instanceof ChannelsKt__Channels_commonKt$last$1) {
            channelsKt__Channels_commonKt$last$1 = (ChannelsKt__Channels_commonKt$last$1) continuation;
            if ((channelsKt__Channels_commonKt$last$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$last$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$last$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$last$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Throwable th4 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$last$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$last$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$last$1.L$2 = th4;
                        channelsKt__Channels_commonKt$last$1.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$last$1.L$4 = it;
                        channelsKt__Channels_commonKt$last$1.label = 1;
                        Object hasNext2 = it.hasNext(channelsKt__Channels_commonKt$last$1);
                        if (hasNext2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel2 = receiveChannel;
                        receiveChannel5 = receiveChannel2;
                        channelIterator2 = it;
                        receiveChannel6 = receiveChannel5;
                        Object obj2 = hasNext2;
                        th3 = th4;
                        obj = obj2;
                    } catch (Throwable th5) {
                        receiveChannel2 = receiveChannel;
                        th = th5;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else if (i == 1) {
                    channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$last$1.L$4;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$last$1.L$3;
                    th3 = (Throwable) channelsKt__Channels_commonKt$last$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$last$1.L$1;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$last$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Throwable th7) {
                        th = th7;
                    }
                } else if (i == 2) {
                    Object obj3 = channelsKt__Channels_commonKt$last$1.L$5;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$last$1.L$4;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$last$1.L$3;
                    th2 = (Throwable) channelsKt__Channels_commonKt$last$1.L$2;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$last$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$last$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        if (!((Boolean) obj).booleanValue()) {
                            next = channelIterator.next();
                            receiveChannel = receiveChannel7;
                            channelsKt__Channels_commonKt$last$1.L$0 = receiveChannel3;
                            channelsKt__Channels_commonKt$last$1.L$1 = receiveChannel;
                            channelsKt__Channels_commonKt$last$1.L$2 = th2;
                            channelsKt__Channels_commonKt$last$1.L$3 = receiveChannel4;
                            channelsKt__Channels_commonKt$last$1.L$4 = channelIterator;
                            channelsKt__Channels_commonKt$last$1.L$5 = next;
                            channelsKt__Channels_commonKt$last$1.label = 2;
                            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$last$1);
                            if (hasNext != coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Object obj4 = hasNext;
                            receiveChannel7 = receiveChannel;
                            obj3 = next;
                            obj = obj4;
                            if (!((Boolean) obj).booleanValue()) {
                                ChannelsKt.cancelConsumed(receiveChannel7, th2);
                            }
                            return coroutine_suspended;
                        }
                        ChannelsKt.cancelConsumed(receiveChannel7, th2);
                        return obj3;
                    } catch (Throwable th8) {
                        th = th8;
                        receiveChannel2 = receiveChannel7;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (!((Boolean) obj).booleanValue()) {
                    next = channelIterator2.next();
                    receiveChannel3 = receiveChannel5;
                    ReceiveChannel<? extends E> receiveChannel8 = receiveChannel6;
                    channelIterator = channelIterator2;
                    receiveChannel = receiveChannel2;
                    th2 = th3;
                    receiveChannel4 = receiveChannel8;
                    channelsKt__Channels_commonKt$last$1.L$0 = receiveChannel3;
                    channelsKt__Channels_commonKt$last$1.L$1 = receiveChannel;
                    channelsKt__Channels_commonKt$last$1.L$2 = th2;
                    channelsKt__Channels_commonKt$last$1.L$3 = receiveChannel4;
                    channelsKt__Channels_commonKt$last$1.L$4 = channelIterator;
                    channelsKt__Channels_commonKt$last$1.L$5 = next;
                    channelsKt__Channels_commonKt$last$1.label = 2;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$last$1);
                    if (hasNext != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                }
                throw new NoSuchElementException("ReceiveChannel is empty.");
            }
        }
        channelsKt__Channels_commonKt$last$1 = new ChannelsKt__Channels_commonKt$last$1(continuation);
        obj = channelsKt__Channels_commonKt$last$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$last$1.label;
        if (i != 0) {
        }
        if (!((Boolean) obj).booleanValue()) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a4 A[Catch:{ all -> 0x00e9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b3 A[Catch:{ all -> 0x00e9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object last(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$last$3 channelsKt__Channels_commonKt$last$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ObjectRef objectRef;
        BooleanRef booleanRef;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$last$3 channelsKt__Channels_commonKt$last$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        BooleanRef booleanRef2;
        ObjectRef objectRef2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$last$3) {
            channelsKt__Channels_commonKt$last$3 = (ChannelsKt__Channels_commonKt$last$3) continuation;
            if ((channelsKt__Channels_commonKt$last$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$last$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$last$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$last$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    ObjectRef objectRef3 = new ObjectRef();
                    objectRef3.element = null;
                    BooleanRef booleanRef3 = new BooleanRef();
                    booleanRef3.element = false;
                    try {
                        objectRef2 = objectRef3;
                        th2 = null;
                        booleanRef2 = booleanRef3;
                        receiveChannel6 = receiveChannel;
                        function12 = function1;
                        channelsKt__Channels_commonKt$last$32 = channelsKt__Channels_commonKt$last$3;
                        receiveChannel3 = receiveChannel6;
                        receiveChannel2 = receiveChannel3;
                        channelIterator = receiveChannel.iterator();
                        obj2 = coroutine_suspended;
                        receiveChannel5 = receiveChannel2;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$last$3.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$last$3.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$last$3.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$last$3.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$last$3.L$4;
                    BooleanRef booleanRef4 = (BooleanRef) channelsKt__Channels_commonKt$last$3.L$3;
                    ObjectRef objectRef4 = (ObjectRef) channelsKt__Channels_commonKt$last$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$last$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$last$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        objectRef = objectRef4;
                        th = th4;
                        function12 = function13;
                        booleanRef = booleanRef4;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$last$32 = channelsKt__Channels_commonKt$last$3;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            T next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                objectRef.element = next;
                                booleanRef.element = true;
                            }
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                            booleanRef2 = booleanRef;
                            objectRef2 = objectRef;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        if (!booleanRef.element) {
                            return objectRef.element;
                        }
                        throw new NoSuchElementException("ReceiveChannel contains no element matching the predicate.");
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$last$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$last$32.L$1 = function12;
                channelsKt__Channels_commonKt$last$32.L$2 = objectRef2;
                channelsKt__Channels_commonKt$last$32.L$3 = booleanRef2;
                channelsKt__Channels_commonKt$last$32.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$last$32.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$last$32.L$6 = th2;
                channelsKt__Channels_commonKt$last$32.L$7 = receiveChannel5;
                channelsKt__Channels_commonKt$last$32.L$8 = channelIterator;
                channelsKt__Channels_commonKt$last$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$last$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                objectRef = objectRef2;
                booleanRef = booleanRef2;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                if (!booleanRef.element) {
                }
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$last$3 = new ChannelsKt__Channels_commonKt$last$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$last$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$last$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$last$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$last$32.L$1 = function12;
            channelsKt__Channels_commonKt$last$32.L$2 = objectRef2;
            channelsKt__Channels_commonKt$last$32.L$3 = booleanRef2;
            channelsKt__Channels_commonKt$last$32.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$last$32.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$last$32.L$6 = th2;
            channelsKt__Channels_commonKt$last$32.L$7 = receiveChannel5;
            channelsKt__Channels_commonKt$last$32.L$8 = channelIterator;
            channelsKt__Channels_commonKt$last$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$last$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0063  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a3 A[Catch:{ all -> 0x00d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b2 A[Catch:{ all -> 0x00d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00cb A[Catch:{ all -> 0x00d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object lastIndexOf(ReceiveChannel<? extends E> receiveChannel, E e, Continuation<? super Integer> continuation) {
        ChannelsKt__Channels_commonKt$lastIndexOf$1 channelsKt__Channels_commonKt$lastIndexOf$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        IntRef intRef;
        IntRef intRef2;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$lastIndexOf$1 channelsKt__Channels_commonKt$lastIndexOf$12;
        E e2;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        IntRef intRef3;
        IntRef intRef4;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$lastIndexOf$1) {
            channelsKt__Channels_commonKt$lastIndexOf$1 = (ChannelsKt__Channels_commonKt$lastIndexOf$1) continuation;
            if ((channelsKt__Channels_commonKt$lastIndexOf$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$lastIndexOf$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$lastIndexOf$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$lastIndexOf$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    IntRef intRef5 = new IntRef();
                    intRef5.element = -1;
                    IntRef intRef6 = new IntRef();
                    intRef6.element = 0;
                    try {
                        intRef4 = intRef5;
                        intRef3 = intRef6;
                        th2 = null;
                        receiveChannel6 = receiveChannel;
                        e2 = e;
                        channelsKt__Channels_commonKt$lastIndexOf$12 = channelsKt__Channels_commonKt$lastIndexOf$1;
                        receiveChannel3 = receiveChannel6;
                        receiveChannel2 = receiveChannel3;
                        channelIterator = receiveChannel.iterator();
                        obj2 = coroutine_suspended;
                        receiveChannel5 = receiveChannel2;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$lastIndexOf$1.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$lastIndexOf$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$lastIndexOf$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$lastIndexOf$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$lastIndexOf$1.L$4;
                    IntRef intRef7 = (IntRef) channelsKt__Channels_commonKt$lastIndexOf$1.L$3;
                    IntRef intRef8 = (IntRef) channelsKt__Channels_commonKt$lastIndexOf$1.L$2;
                    E e3 = channelsKt__Channels_commonKt$lastIndexOf$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$lastIndexOf$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        intRef = intRef8;
                        th = th4;
                        e2 = e3;
                        intRef2 = intRef7;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$lastIndexOf$12 = channelsKt__Channels_commonKt$lastIndexOf$1;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                        } else {
                            if (Intrinsics.areEqual((Object) e2, channelIterator.next())) {
                                intRef.element = intRef2.element;
                            }
                            intRef2.element++;
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                            intRef3 = intRef2;
                            intRef4 = intRef;
                        }
                        Unit unit2 = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        return Boxing.boxInt(intRef.element);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$lastIndexOf$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$lastIndexOf$12.L$1 = e2;
                channelsKt__Channels_commonKt$lastIndexOf$12.L$2 = intRef4;
                channelsKt__Channels_commonKt$lastIndexOf$12.L$3 = intRef3;
                channelsKt__Channels_commonKt$lastIndexOf$12.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$lastIndexOf$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$lastIndexOf$12.L$6 = th2;
                channelsKt__Channels_commonKt$lastIndexOf$12.L$7 = receiveChannel5;
                channelsKt__Channels_commonKt$lastIndexOf$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$lastIndexOf$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$lastIndexOf$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                intRef = intRef4;
                intRef2 = intRef3;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                }
                Unit unit22 = Unit.INSTANCE;
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                return Boxing.boxInt(intRef.element);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$lastIndexOf$1 = new ChannelsKt__Channels_commonKt$lastIndexOf$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$lastIndexOf$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$lastIndexOf$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$lastIndexOf$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$lastIndexOf$12.L$1 = e2;
            channelsKt__Channels_commonKt$lastIndexOf$12.L$2 = intRef4;
            channelsKt__Channels_commonKt$lastIndexOf$12.L$3 = intRef3;
            channelsKt__Channels_commonKt$lastIndexOf$12.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$lastIndexOf$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$lastIndexOf$12.L$6 = th2;
            channelsKt__Channels_commonKt$lastIndexOf$12.L$7 = receiveChannel5;
            channelsKt__Channels_commonKt$lastIndexOf$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$lastIndexOf$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$lastIndexOf$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0098 A[SYNTHETIC, Splitter:B:30:0x0098] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00c0 A[Catch:{ all -> 0x0065 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object lastOrNull(ReceiveChannel<? extends E> receiveChannel, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$lastOrNull$1 channelsKt__Channels_commonKt$lastOrNull$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        Throwable th2;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object next;
        Object hasNext;
        ChannelIterator channelIterator2;
        ReceiveChannel<? extends E> receiveChannel5;
        if (continuation instanceof ChannelsKt__Channels_commonKt$lastOrNull$1) {
            channelsKt__Channels_commonKt$lastOrNull$1 = (ChannelsKt__Channels_commonKt$lastOrNull$1) continuation;
            if ((channelsKt__Channels_commonKt$lastOrNull$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$lastOrNull$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$lastOrNull$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$lastOrNull$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Throwable th3 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$lastOrNull$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$2 = th3;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$4 = it;
                        channelsKt__Channels_commonKt$lastOrNull$1.label = 1;
                        Object hasNext2 = it.hasNext(channelsKt__Channels_commonKt$lastOrNull$1);
                        if (hasNext2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel2 = receiveChannel;
                        receiveChannel3 = receiveChannel2;
                        channelIterator2 = it;
                        receiveChannel5 = receiveChannel3;
                        Object obj2 = hasNext2;
                        th2 = th3;
                        obj = obj2;
                    } catch (Throwable th4) {
                        receiveChannel2 = receiveChannel;
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th5;
                        }
                    }
                } else if (i == 1) {
                    channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$lastOrNull$1.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$1.L$3;
                    th2 = (Throwable) channelsKt__Channels_commonKt$lastOrNull$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$1.L$0;
                    ResultKt.throwOnFailure(obj);
                } else if (i == 2) {
                    Object obj3 = channelsKt__Channels_commonKt$lastOrNull$1.L$5;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$lastOrNull$1.L$4;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$1.L$3;
                    th2 = (Throwable) channelsKt__Channels_commonKt$lastOrNull$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$1.L$0;
                    ResultKt.throwOnFailure(obj);
                    if (!((Boolean) obj).booleanValue()) {
                        next = channelIterator.next();
                        receiveChannel = receiveChannel2;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$0 = receiveChannel3;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$2 = th2;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$3 = receiveChannel4;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$4 = channelIterator;
                        channelsKt__Channels_commonKt$lastOrNull$1.L$5 = next;
                        channelsKt__Channels_commonKt$lastOrNull$1.label = 2;
                        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$lastOrNull$1);
                        if (hasNext != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        Object obj4 = hasNext;
                        receiveChannel2 = receiveChannel;
                        obj3 = next;
                        obj = obj4;
                        if (!((Boolean) obj).booleanValue()) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        }
                        return coroutine_suspended;
                    }
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    return obj3;
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (((Boolean) obj).booleanValue()) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    return null;
                }
                next = channelIterator2.next();
                receiveChannel4 = receiveChannel5;
                channelIterator = channelIterator2;
                receiveChannel = receiveChannel2;
                channelsKt__Channels_commonKt$lastOrNull$1.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$lastOrNull$1.L$1 = receiveChannel;
                channelsKt__Channels_commonKt$lastOrNull$1.L$2 = th2;
                channelsKt__Channels_commonKt$lastOrNull$1.L$3 = receiveChannel4;
                channelsKt__Channels_commonKt$lastOrNull$1.L$4 = channelIterator;
                channelsKt__Channels_commonKt$lastOrNull$1.L$5 = next;
                channelsKt__Channels_commonKt$lastOrNull$1.label = 2;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$lastOrNull$1);
                if (hasNext != coroutine_suspended) {
                }
                return coroutine_suspended;
            }
        }
        channelsKt__Channels_commonKt$lastOrNull$1 = new ChannelsKt__Channels_commonKt$lastOrNull$1(continuation);
        obj = channelsKt__Channels_commonKt$lastOrNull$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$lastOrNull$1.label;
        if (i != 0) {
        }
        try {
            if (((Boolean) obj).booleanValue()) {
            }
        } catch (Throwable th6) {
            th = th6;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0095 A[Catch:{ all -> 0x00c8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a3 A[Catch:{ all -> 0x00c8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object lastOrNull(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$lastOrNull$3 channelsKt__Channels_commonKt$lastOrNull$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ObjectRef objectRef;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$lastOrNull$3 channelsKt__Channels_commonKt$lastOrNull$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        ObjectRef objectRef2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$lastOrNull$3) {
            channelsKt__Channels_commonKt$lastOrNull$3 = (ChannelsKt__Channels_commonKt$lastOrNull$3) continuation;
            if ((channelsKt__Channels_commonKt$lastOrNull$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$lastOrNull$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$lastOrNull$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$lastOrNull$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    ObjectRef objectRef3 = new ObjectRef();
                    objectRef3.element = null;
                    try {
                        objectRef2 = objectRef3;
                        obj2 = coroutine_suspended;
                        th2 = null;
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$lastOrNull$32 = channelsKt__Channels_commonKt$lastOrNull$3;
                        receiveChannel2 = receiveChannel3;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$lastOrNull$3.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$3.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$lastOrNull$3.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$3.L$4;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$3.L$3;
                    ObjectRef objectRef4 = (ObjectRef) channelsKt__Channels_commonKt$lastOrNull$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$lastOrNull$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$lastOrNull$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        objectRef = objectRef4;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$lastOrNull$32 = channelsKt__Channels_commonKt$lastOrNull$3;
                        receiveChannel2 = receiveChannel10;
                        Function1<? super E, Boolean> function14 = function13;
                        th = th4;
                        function12 = function14;
                        if (!((Boolean) obj3).booleanValue()) {
                            T next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                objectRef.element = next;
                            }
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                            objectRef2 = objectRef;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return objectRef.element;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$lastOrNull$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$lastOrNull$32.L$1 = function12;
                channelsKt__Channels_commonKt$lastOrNull$32.L$2 = objectRef2;
                channelsKt__Channels_commonKt$lastOrNull$32.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$lastOrNull$32.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$lastOrNull$32.L$5 = th2;
                channelsKt__Channels_commonKt$lastOrNull$32.L$6 = receiveChannel5;
                channelsKt__Channels_commonKt$lastOrNull$32.L$7 = channelIterator;
                channelsKt__Channels_commonKt$lastOrNull$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$lastOrNull$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                objectRef = objectRef2;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return objectRef.element;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$lastOrNull$3 = new ChannelsKt__Channels_commonKt$lastOrNull$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$lastOrNull$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$lastOrNull$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$lastOrNull$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$lastOrNull$32.L$1 = function12;
            channelsKt__Channels_commonKt$lastOrNull$32.L$2 = objectRef2;
            channelsKt__Channels_commonKt$lastOrNull$32.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$lastOrNull$32.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$lastOrNull$32.L$5 = th2;
            channelsKt__Channels_commonKt$lastOrNull$32.L$6 = receiveChannel5;
            channelsKt__Channels_commonKt$lastOrNull$32.L$7 = channelIterator;
            channelsKt__Channels_commonKt$lastOrNull$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$lastOrNull$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0097 A[Catch:{ all -> 0x0068 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00bd  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00c1 A[SYNTHETIC, Splitter:B:39:0x00c1] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00cb A[SYNTHETIC, Splitter:B:42:0x00cb] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object single(ReceiveChannel<? extends E> receiveChannel, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$single$1 channelsKt__Channels_commonKt$single$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        Object obj2;
        ReceiveChannel<? extends E> receiveChannel3;
        Throwable th2;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel4;
        Throwable th3;
        ReceiveChannel<? extends E> receiveChannel5;
        if (continuation instanceof ChannelsKt__Channels_commonKt$single$1) {
            channelsKt__Channels_commonKt$single$1 = (ChannelsKt__Channels_commonKt$single$1) continuation;
            if ((channelsKt__Channels_commonKt$single$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$single$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$single$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$single$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Throwable th4 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$single$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$single$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$single$1.L$2 = th4;
                        channelsKt__Channels_commonKt$single$1.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$single$1.L$4 = it;
                        channelsKt__Channels_commonKt$single$1.label = 1;
                        Object hasNext = it.hasNext(channelsKt__Channels_commonKt$single$1);
                        if (hasNext == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel2 = receiveChannel;
                        receiveChannel4 = receiveChannel2;
                        channelIterator = it;
                        receiveChannel5 = receiveChannel4;
                        Object obj3 = hasNext;
                        th3 = th4;
                        obj = obj3;
                    } catch (Throwable th5) {
                        receiveChannel2 = receiveChannel;
                        th = th5;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$single$1.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$single$1.L$3;
                    th3 = (Throwable) channelsKt__Channels_commonKt$single$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$single$1.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$single$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Throwable th7) {
                        th = th7;
                    }
                } else if (i == 2) {
                    obj2 = channelsKt__Channels_commonKt$single$1.L$5;
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$single$1.L$4;
                    ReceiveChannel receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$single$1.L$3;
                    th2 = (Throwable) channelsKt__Channels_commonKt$single$1.L$2;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$single$1.L$1;
                    ReceiveChannel receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$single$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        if (((Boolean) obj).booleanValue()) {
                            ChannelsKt.cancelConsumed(receiveChannel3, th2);
                            return obj2;
                        }
                        throw new IllegalArgumentException("ReceiveChannel has more than one element.");
                    } catch (Throwable th8) {
                        th = th8;
                        receiveChannel2 = receiveChannel3;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (!((Boolean) obj).booleanValue()) {
                    Object next = channelIterator.next();
                    channelsKt__Channels_commonKt$single$1.L$0 = receiveChannel4;
                    channelsKt__Channels_commonKt$single$1.L$1 = receiveChannel2;
                    channelsKt__Channels_commonKt$single$1.L$2 = th3;
                    channelsKt__Channels_commonKt$single$1.L$3 = receiveChannel5;
                    channelsKt__Channels_commonKt$single$1.L$4 = channelIterator;
                    channelsKt__Channels_commonKt$single$1.L$5 = next;
                    channelsKt__Channels_commonKt$single$1.label = 2;
                    Object hasNext2 = channelIterator.hasNext(channelsKt__Channels_commonKt$single$1);
                    if (hasNext2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    th2 = th3;
                    receiveChannel3 = receiveChannel2;
                    Object obj4 = next;
                    obj = hasNext2;
                    obj2 = obj4;
                    if (((Boolean) obj).booleanValue()) {
                    }
                } else {
                    throw new NoSuchElementException("ReceiveChannel is empty.");
                }
            }
        }
        channelsKt__Channels_commonKt$single$1 = new ChannelsKt__Channels_commonKt$single$1(continuation);
        obj = channelsKt__Channels_commonKt$single$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$single$1.label;
        if (i != 0) {
        }
        if (!((Boolean) obj).booleanValue()) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a4 A[Catch:{ all -> 0x00f8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b3 A[Catch:{ all -> 0x00f8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object single(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$single$3 channelsKt__Channels_commonKt$single$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ObjectRef objectRef;
        BooleanRef booleanRef;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$single$3 channelsKt__Channels_commonKt$single$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        BooleanRef booleanRef2;
        ObjectRef objectRef2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$single$3) {
            channelsKt__Channels_commonKt$single$3 = (ChannelsKt__Channels_commonKt$single$3) continuation;
            if ((channelsKt__Channels_commonKt$single$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$single$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$single$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$single$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    ObjectRef objectRef3 = new ObjectRef();
                    objectRef3.element = null;
                    BooleanRef booleanRef3 = new BooleanRef();
                    booleanRef3.element = false;
                    try {
                        objectRef2 = objectRef3;
                        th2 = null;
                        booleanRef2 = booleanRef3;
                        receiveChannel6 = receiveChannel;
                        function12 = function1;
                        channelsKt__Channels_commonKt$single$32 = channelsKt__Channels_commonKt$single$3;
                        receiveChannel3 = receiveChannel6;
                        receiveChannel2 = receiveChannel3;
                        channelIterator = receiveChannel.iterator();
                        obj2 = coroutine_suspended;
                        receiveChannel5 = receiveChannel2;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$single$3.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$single$3.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$single$3.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$single$3.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$single$3.L$4;
                    BooleanRef booleanRef4 = (BooleanRef) channelsKt__Channels_commonKt$single$3.L$3;
                    ObjectRef objectRef4 = (ObjectRef) channelsKt__Channels_commonKt$single$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$single$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$single$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        objectRef = objectRef4;
                        th = th4;
                        function12 = function13;
                        booleanRef = booleanRef4;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$single$32 = channelsKt__Channels_commonKt$single$3;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            T next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                if (!booleanRef.element) {
                                    objectRef.element = next;
                                    booleanRef.element = true;
                                }
                                throw new IllegalArgumentException("ReceiveChannel contains more than one matching element.");
                            }
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                            booleanRef2 = booleanRef;
                            objectRef2 = objectRef;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        if (!booleanRef.element) {
                            return objectRef.element;
                        }
                        throw new NoSuchElementException("ReceiveChannel contains no element matching the predicate.");
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$single$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$single$32.L$1 = function12;
                channelsKt__Channels_commonKt$single$32.L$2 = objectRef2;
                channelsKt__Channels_commonKt$single$32.L$3 = booleanRef2;
                channelsKt__Channels_commonKt$single$32.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$single$32.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$single$32.L$6 = th2;
                channelsKt__Channels_commonKt$single$32.L$7 = receiveChannel5;
                channelsKt__Channels_commonKt$single$32.L$8 = channelIterator;
                channelsKt__Channels_commonKt$single$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$single$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                objectRef = objectRef2;
                booleanRef = booleanRef2;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                if (!booleanRef.element) {
                }
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$single$3 = new ChannelsKt__Channels_commonKt$single$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$single$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$single$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$single$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$single$32.L$1 = function12;
            channelsKt__Channels_commonKt$single$32.L$2 = objectRef2;
            channelsKt__Channels_commonKt$single$32.L$3 = booleanRef2;
            channelsKt__Channels_commonKt$single$32.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$single$32.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$single$32.L$6 = th2;
            channelsKt__Channels_commonKt$single$32.L$7 = receiveChannel5;
            channelsKt__Channels_commonKt$single$32.L$8 = channelIterator;
            channelsKt__Channels_commonKt$single$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$single$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x009c A[SYNTHETIC, Splitter:B:32:0x009c] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00c6  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object singleOrNull(ReceiveChannel<? extends E> receiveChannel, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$singleOrNull$1 channelsKt__Channels_commonKt$singleOrNull$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        Object obj2;
        ReceiveChannel<? extends E> receiveChannel3;
        Throwable th2;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel4;
        Throwable th3;
        ReceiveChannel<? extends E> receiveChannel5;
        if (continuation instanceof ChannelsKt__Channels_commonKt$singleOrNull$1) {
            channelsKt__Channels_commonKt$singleOrNull$1 = (ChannelsKt__Channels_commonKt$singleOrNull$1) continuation;
            if ((channelsKt__Channels_commonKt$singleOrNull$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$singleOrNull$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$singleOrNull$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$singleOrNull$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Throwable th4 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$singleOrNull$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$singleOrNull$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$singleOrNull$1.L$2 = th4;
                        channelsKt__Channels_commonKt$singleOrNull$1.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$singleOrNull$1.L$4 = it;
                        channelsKt__Channels_commonKt$singleOrNull$1.label = 1;
                        Object hasNext = it.hasNext(channelsKt__Channels_commonKt$singleOrNull$1);
                        if (hasNext == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel2 = receiveChannel;
                        receiveChannel4 = receiveChannel2;
                        channelIterator = it;
                        receiveChannel5 = receiveChannel4;
                        Object obj3 = hasNext;
                        th3 = th4;
                        obj = obj3;
                    } catch (Throwable th5) {
                        receiveChannel2 = receiveChannel;
                        th = th5;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$singleOrNull$1.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$1.L$3;
                    th3 = (Throwable) channelsKt__Channels_commonKt$singleOrNull$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$1.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Throwable th7) {
                        th = th7;
                    }
                } else if (i == 2) {
                    obj2 = channelsKt__Channels_commonKt$singleOrNull$1.L$5;
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$singleOrNull$1.L$4;
                    ReceiveChannel receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$1.L$3;
                    th2 = (Throwable) channelsKt__Channels_commonKt$singleOrNull$1.L$2;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$1.L$1;
                    ReceiveChannel receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        if (!((Boolean) obj).booleanValue()) {
                            ChannelsKt.cancelConsumed(receiveChannel3, th2);
                            return null;
                        }
                        ChannelsKt.cancelConsumed(receiveChannel3, th2);
                        return obj2;
                    } catch (Throwable th8) {
                        th = th8;
                        receiveChannel2 = receiveChannel3;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (((Boolean) obj).booleanValue()) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th3);
                    return null;
                }
                Object next = channelIterator.next();
                channelsKt__Channels_commonKt$singleOrNull$1.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$singleOrNull$1.L$1 = receiveChannel2;
                channelsKt__Channels_commonKt$singleOrNull$1.L$2 = th3;
                channelsKt__Channels_commonKt$singleOrNull$1.L$3 = receiveChannel5;
                channelsKt__Channels_commonKt$singleOrNull$1.L$4 = channelIterator;
                channelsKt__Channels_commonKt$singleOrNull$1.L$5 = next;
                channelsKt__Channels_commonKt$singleOrNull$1.label = 2;
                Object hasNext2 = channelIterator.hasNext(channelsKt__Channels_commonKt$singleOrNull$1);
                if (hasNext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                th2 = th3;
                receiveChannel3 = receiveChannel2;
                Object obj4 = next;
                obj = hasNext2;
                obj2 = obj4;
                if (!((Boolean) obj).booleanValue()) {
                }
            }
        }
        channelsKt__Channels_commonKt$singleOrNull$1 = new ChannelsKt__Channels_commonKt$singleOrNull$1(continuation);
        obj = channelsKt__Channels_commonKt$singleOrNull$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$singleOrNull$1.label;
        if (i != 0) {
        }
        if (((Boolean) obj).booleanValue()) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00ad A[Catch:{ all -> 0x00f0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b8 A[Catch:{ all -> 0x00f0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00ec A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ed  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0027  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object singleOrNull(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$singleOrNull$3 channelsKt__Channels_commonKt$singleOrNull$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        ObjectRef objectRef;
        BooleanRef booleanRef;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$singleOrNull$3 channelsKt__Channels_commonKt$singleOrNull$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object hasNext;
        Continuation<? super E> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$singleOrNull$3) {
            channelsKt__Channels_commonKt$singleOrNull$3 = (ChannelsKt__Channels_commonKt$singleOrNull$3) continuation2;
            if ((channelsKt__Channels_commonKt$singleOrNull$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$singleOrNull$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$singleOrNull$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$singleOrNull$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    ObjectRef objectRef2 = new ObjectRef();
                    objectRef2.element = null;
                    BooleanRef booleanRef2 = new BooleanRef();
                    booleanRef2.element = false;
                    try {
                        objectRef = objectRef2;
                        obj = coroutine_suspended;
                        booleanRef = booleanRef2;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel4 = receiveChannel2;
                        function12 = function1;
                        channelsKt__Channels_commonKt$singleOrNull$32 = channelsKt__Channels_commonKt$singleOrNull$3;
                        receiveChannel5 = receiveChannel4;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$singleOrNull$3.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$3.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$singleOrNull$3.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$3.L$5;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$3.L$4;
                    BooleanRef booleanRef3 = (BooleanRef) channelsKt__Channels_commonKt$singleOrNull$3.L$3;
                    ObjectRef objectRef3 = (ObjectRef) channelsKt__Channels_commonKt$singleOrNull$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$singleOrNull$3.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$singleOrNull$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        Throwable th5 = th4;
                        channelsKt__Channels_commonKt$singleOrNull$32 = channelsKt__Channels_commonKt$singleOrNull$3;
                        receiveChannel5 = receiveChannel9;
                        channelIterator = channelIterator2;
                        receiveChannel4 = receiveChannel7;
                        function12 = function13;
                        objectRef = objectRef3;
                        booleanRef = booleanRef3;
                        th2 = th5;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj2).booleanValue()) {
                            T next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                if (booleanRef.element) {
                                    InlineMarker.finallyStart(2);
                                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                                    InlineMarker.finallyEnd(2);
                                } else {
                                    objectRef.element = next;
                                    booleanRef.element = true;
                                }
                                InlineMarker.finallyStart(2);
                                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                                InlineMarker.finallyEnd(2);
                                return null;
                            }
                            receiveChannel6 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        if (booleanRef.element) {
                            return null;
                        }
                        return objectRef.element;
                    } catch (Throwable th6) {
                        th = th6;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th7) {
                            Throwable th8 = th7;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th8;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$singleOrNull$32.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$singleOrNull$32.L$1 = function12;
                channelsKt__Channels_commonKt$singleOrNull$32.L$2 = objectRef;
                channelsKt__Channels_commonKt$singleOrNull$32.L$3 = booleanRef;
                channelsKt__Channels_commonKt$singleOrNull$32.L$4 = receiveChannel5;
                channelsKt__Channels_commonKt$singleOrNull$32.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$singleOrNull$32.L$6 = th2;
                channelsKt__Channels_commonKt$singleOrNull$32.L$7 = receiveChannel4;
                channelsKt__Channels_commonKt$singleOrNull$32.L$8 = channelIterator;
                channelsKt__Channels_commonKt$singleOrNull$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$singleOrNull$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                if (booleanRef.element) {
                }
                return obj;
            }
        }
        channelsKt__Channels_commonKt$singleOrNull$3 = new ChannelsKt__Channels_commonKt$singleOrNull$3(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$singleOrNull$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$singleOrNull$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$singleOrNull$32.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$singleOrNull$32.L$1 = function12;
            channelsKt__Channels_commonKt$singleOrNull$32.L$2 = objectRef;
            channelsKt__Channels_commonKt$singleOrNull$32.L$3 = booleanRef;
            channelsKt__Channels_commonKt$singleOrNull$32.L$4 = receiveChannel5;
            channelsKt__Channels_commonKt$singleOrNull$32.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$singleOrNull$32.L$6 = th2;
            channelsKt__Channels_commonKt$singleOrNull$32.L$7 = receiveChannel4;
            channelsKt__Channels_commonKt$singleOrNull$32.L$8 = channelIterator;
            channelsKt__Channels_commonKt$singleOrNull$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$singleOrNull$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th9) {
            th = th9;
            th = th;
            throw th;
        }
    }

    public static /* synthetic */ ReceiveChannel drop$default(ReceiveChannel receiveChannel, int i, CoroutineContext coroutineContext, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.drop(receiveChannel, i, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> drop(ReceiveChannel<? extends E> receiveChannel, int i, CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$drop");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$drop$1(receiveChannel, i, null), 2, null);
    }

    public static /* synthetic */ ReceiveChannel dropWhile$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.dropWhile(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> dropWhile(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$dropWhile");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$dropWhile$1(receiveChannel, function2, null), 2, null);
    }

    public static /* synthetic */ ReceiveChannel filter$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.filter(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> filter(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$filter");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$filter$1(receiveChannel, function2, null), 2, null);
    }

    public static /* synthetic */ ReceiveChannel filterIndexed$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.filterIndexed(receiveChannel, coroutineContext, function3);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> filterIndexed(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function3<? super Integer, ? super E, ? super Continuation<? super Boolean>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$filterIndexed");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "predicate");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$filterIndexed$1(receiveChannel, function3, null), 2, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00ac A[Catch:{ all -> 0x00f3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b9 A[Catch:{ all -> 0x00f3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends Collection<? super E>> Object filterIndexedTo(ReceiveChannel<? extends E> receiveChannel, C c, Function2<? super Integer, ? super E, Boolean> function2, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$filterIndexedTo$1 channelsKt__Channels_commonKt$filterIndexedTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        IntRef intRef;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$filterIndexedTo$1 channelsKt__Channels_commonKt$filterIndexedTo$12;
        Function2<? super Integer, ? super E, Boolean> function22;
        C c2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Object hasNext;
        Continuation<? super C> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$filterIndexedTo$1) {
            channelsKt__Channels_commonKt$filterIndexedTo$1 = (ChannelsKt__Channels_commonKt$filterIndexedTo$1) continuation2;
            if ((channelsKt__Channels_commonKt$filterIndexedTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$filterIndexedTo$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$filterIndexedTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$filterIndexedTo$1.label;
                int i2 = 1;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef2 = new IntRef();
                    intRef2.element = 0;
                    try {
                        c2 = c;
                        function22 = function2;
                        intRef = intRef2;
                        channelsKt__Channels_commonKt$filterIndexedTo$12 = channelsKt__Channels_commonKt$filterIndexedTo$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$filterIndexedTo$1.L$9;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$1.L$8;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$filterIndexedTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$1.L$5;
                    IntRef intRef3 = (IntRef) channelsKt__Channels_commonKt$filterIndexedTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$1.L$3;
                    Function2<? super Integer, ? super E, Boolean> function23 = (Function2) channelsKt__Channels_commonKt$filterIndexedTo$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$filterIndexedTo$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        ReceiveChannel<? extends E> receiveChannel11 = receiveChannel9;
                        channelsKt__Channels_commonKt$filterIndexedTo$12 = channelsKt__Channels_commonKt$filterIndexedTo$1;
                        receiveChannel6 = receiveChannel10;
                        channelIterator = channelIterator2;
                        receiveChannel2 = receiveChannel8;
                        function22 = function23;
                        th2 = th4;
                        c2 = c3;
                        intRef = intRef3;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel11;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            int i3 = intRef.element;
                            intRef.element = i3 + 1;
                            IndexedValue indexedValue = new IndexedValue(i3, next);
                            int component1 = indexedValue.component1();
                            Object component2 = indexedValue.component2();
                            if (((Boolean) function22.invoke(Boxing.boxInt(component1), component2)).booleanValue()) {
                                c2.add(component2);
                            }
                            receiveChannel7 = receiveChannel3;
                            i2 = 1;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return c2;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$0 = receiveChannel7;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$1 = c2;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$2 = function22;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$4 = intRef;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$6 = receiveChannel2;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$7 = th2;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$8 = receiveChannel4;
                channelsKt__Channels_commonKt$filterIndexedTo$12.L$9 = channelIterator;
                channelsKt__Channels_commonKt$filterIndexedTo$12.label = i2;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterIndexedTo$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel7;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$filterIndexedTo$1 = new ChannelsKt__Channels_commonKt$filterIndexedTo$1(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$filterIndexedTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$filterIndexedTo$1.label;
        int i22 = 1;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$0 = receiveChannel7;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$1 = c2;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$2 = function22;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$4 = intRef;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$6 = receiveChannel2;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$7 = th2;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$8 = receiveChannel4;
            channelsKt__Channels_commonKt$filterIndexedTo$12.L$9 = channelIterator;
            channelsKt__Channels_commonKt$filterIndexedTo$12.label = i22;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterIndexedTo$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00e3 A[Catch:{ all -> 0x0189 }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00fe A[Catch:{ all -> 0x0189 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0027  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends SendChannel<? super E>> Object filterIndexedTo(ReceiveChannel<? extends E> receiveChannel, C c, Function2<? super Integer, ? super E, Boolean> function2, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$filterIndexedTo$3 channelsKt__Channels_commonKt$filterIndexedTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th2;
        Object obj;
        ChannelsKt__Channels_commonKt$filterIndexedTo$3 channelsKt__Channels_commonKt$filterIndexedTo$32;
        IntRef intRef;
        Function2<? super Integer, ? super E, Boolean> function22;
        C c2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        char c3;
        ReceiveChannel<? extends E> receiveChannel7;
        C c4;
        Function2<? super Integer, ? super E, Boolean> function23;
        ReceiveChannel<? extends E> receiveChannel8;
        IntRef intRef2;
        ReceiveChannel<? extends E> receiveChannel9;
        ReceiveChannel<? extends E> receiveChannel10;
        Throwable th3;
        ChannelIterator channelIterator2;
        C c5;
        Object hasNext;
        Continuation<? super C> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$filterIndexedTo$3) {
            channelsKt__Channels_commonKt$filterIndexedTo$3 = (ChannelsKt__Channels_commonKt$filterIndexedTo$3) continuation2;
            if ((channelsKt__Channels_commonKt$filterIndexedTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$filterIndexedTo$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$filterIndexedTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$filterIndexedTo$3.label;
                int i2 = 1;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef3 = new IntRef();
                    intRef3.element = 0;
                    try {
                        c2 = c;
                        function22 = function2;
                        intRef = intRef3;
                        channelsKt__Channels_commonKt$filterIndexedTo$32 = channelsKt__Channels_commonKt$filterIndexedTo$3;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$filterIndexedTo$3.L$9;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$8;
                    th3 = (Throwable) channelsKt__Channels_commonKt$filterIndexedTo$3.L$7;
                    receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$6;
                    receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$5;
                    intRef2 = (IntRef) channelsKt__Channels_commonKt$filterIndexedTo$3.L$4;
                    receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$3;
                    function23 = (Function2) channelsKt__Channels_commonKt$filterIndexedTo$3.L$2;
                    C c6 = (SendChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$0;
                    ResultKt.throwOnFailure(obj2);
                    C c7 = c6;
                    channelIterator = channelIterator3;
                    receiveChannel2 = receiveChannel10;
                    c5 = c7;
                    if (!((Boolean) obj2).booleanValue()) {
                        Object next = channelIterator.next();
                        int i3 = intRef2.element;
                        intRef2.element = i3 + 1;
                        IndexedValue indexedValue = new IndexedValue(i3, next);
                        int component1 = indexedValue.component1();
                        Object component2 = indexedValue.component2();
                        Object obj3 = coroutine_suspended;
                        if (((Boolean) function23.invoke(Boxing.boxInt(component1), component2)).booleanValue()) {
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$0 = receiveChannel3;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$1 = c5;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$2 = function23;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$3 = receiveChannel8;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$4 = intRef2;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$5 = receiveChannel9;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$6 = receiveChannel2;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$7 = th3;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$8 = receiveChannel4;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$9 = channelIterator;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$10 = next;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$11 = next;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$12 = indexedValue;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.I$0 = component1;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.L$13 = component2;
                            c3 = 2;
                            channelsKt__Channels_commonKt$filterIndexedTo$3.label = 2;
                            Object obj4 = obj3;
                            if (c5.send(component2, channelsKt__Channels_commonKt$filterIndexedTo$3) == obj4) {
                                return obj4;
                            }
                            coroutine_suspended = obj4;
                            C c8 = c5;
                            receiveChannel10 = receiveChannel2;
                            channelIterator2 = channelIterator;
                            c4 = c8;
                            IntRef intRef4 = intRef2;
                            channelsKt__Channels_commonKt$filterIndexedTo$32 = channelsKt__Channels_commonKt$filterIndexedTo$3;
                            receiveChannel6 = receiveChannel8;
                            obj = coroutine_suspended;
                            receiveChannel5 = receiveChannel9;
                            intRef = intRef4;
                            C c9 = c4;
                            channelIterator = channelIterator2;
                            receiveChannel2 = receiveChannel10;
                            function22 = function23;
                            th2 = th3;
                            c2 = c9;
                            char c10 = c3;
                            receiveChannel7 = receiveChannel3;
                            i2 = 1;
                            return obj4;
                        }
                        c3 = 2;
                        receiveChannel5 = receiveChannel9;
                        intRef = intRef2;
                        channelsKt__Channels_commonKt$filterIndexedTo$32 = channelsKt__Channels_commonKt$filterIndexedTo$3;
                        receiveChannel6 = receiveChannel8;
                        obj = obj3;
                        Function2<? super Integer, ? super E, Boolean> function24 = function23;
                        th2 = th3;
                        c2 = c5;
                        function22 = function24;
                        char c102 = c3;
                        receiveChannel7 = receiveChannel3;
                        i2 = 1;
                    }
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel2, th3);
                    InlineMarker.finallyEnd(1);
                    return c5;
                } else if (i == 2) {
                    Object obj5 = channelsKt__Channels_commonKt$filterIndexedTo$3.L$13;
                    int i4 = channelsKt__Channels_commonKt$filterIndexedTo$3.I$0;
                    IndexedValue indexedValue2 = (IndexedValue) channelsKt__Channels_commonKt$filterIndexedTo$3.L$12;
                    Object obj6 = channelsKt__Channels_commonKt$filterIndexedTo$3.L$11;
                    Object obj7 = channelsKt__Channels_commonKt$filterIndexedTo$3.L$10;
                    channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$filterIndexedTo$3.L$9;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$8;
                    th3 = (Throwable) channelsKt__Channels_commonKt$filterIndexedTo$3.L$7;
                    receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$6;
                    receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$5;
                    intRef2 = (IntRef) channelsKt__Channels_commonKt$filterIndexedTo$3.L$4;
                    receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$3;
                    function23 = (Function2) channelsKt__Channels_commonKt$filterIndexedTo$3.L$2;
                    c4 = (SendChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterIndexedTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        c3 = 2;
                        IntRef intRef42 = intRef2;
                        channelsKt__Channels_commonKt$filterIndexedTo$32 = channelsKt__Channels_commonKt$filterIndexedTo$3;
                        receiveChannel6 = receiveChannel8;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel9;
                        intRef = intRef42;
                        C c92 = c4;
                        channelIterator = channelIterator2;
                        receiveChannel2 = receiveChannel10;
                        function22 = function23;
                        th2 = th3;
                        c2 = c92;
                        char c1022 = c3;
                        receiveChannel7 = receiveChannel3;
                        i2 = 1;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel10;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$0 = receiveChannel7;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$1 = c2;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$2 = function22;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$4 = intRef;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$6 = receiveChannel2;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$7 = th2;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$8 = receiveChannel4;
                channelsKt__Channels_commonKt$filterIndexedTo$32.L$9 = channelIterator;
                channelsKt__Channels_commonKt$filterIndexedTo$32.label = i2;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterIndexedTo$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj8 = hasNext;
                receiveChannel3 = receiveChannel7;
                obj2 = obj8;
                Object obj9 = obj;
                receiveChannel8 = receiveChannel6;
                channelsKt__Channels_commonKt$filterIndexedTo$3 = channelsKt__Channels_commonKt$filterIndexedTo$32;
                intRef2 = intRef;
                receiveChannel9 = receiveChannel5;
                coroutine_suspended = obj9;
                Function2<? super Integer, ? super E, Boolean> function25 = function22;
                c5 = c2;
                th3 = th2;
                function23 = function25;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th3);
                InlineMarker.finallyEnd(1);
                return c5;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$filterIndexedTo$3 = new ChannelsKt__Channels_commonKt$filterIndexedTo$3(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$filterIndexedTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$filterIndexedTo$3.label;
        int i22 = 1;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$0 = receiveChannel7;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$1 = c2;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$2 = function22;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$4 = intRef;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$6 = receiveChannel2;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$7 = th2;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$8 = receiveChannel4;
            channelsKt__Channels_commonKt$filterIndexedTo$32.L$9 = channelIterator;
            channelsKt__Channels_commonKt$filterIndexedTo$32.label = i22;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterIndexedTo$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    public static /* synthetic */ ReceiveChannel filterNot$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.filterNot(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> filterNot(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$filterNot");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        return ChannelsKt.filter(receiveChannel, coroutineContext, new ChannelsKt__Channels_commonKt$filterNot$1(function2, null));
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> filterNotNull(ReceiveChannel<? extends E> receiveChannel) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$filterNotNull");
        ReceiveChannel<E> filter$default = filter$default(receiveChannel, null, new ChannelsKt__Channels_commonKt$filterNotNull$1(null), 1, null);
        if (filter$default != null) {
            return filter$default;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.ReceiveChannel<E>");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085 A[Catch:{ all -> 0x00a5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0092 A[Catch:{ all -> 0x00a5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends Collection<? super E>> Object filterNotNullTo(ReceiveChannel<? extends E> receiveChannel, C c, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$filterNotNullTo$1 channelsKt__Channels_commonKt$filterNotNullTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$filterNotNullTo$1 channelsKt__Channels_commonKt$filterNotNullTo$12;
        C c2;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$filterNotNullTo$1) {
            channelsKt__Channels_commonKt$filterNotNullTo$1 = (ChannelsKt__Channels_commonKt$filterNotNullTo$1) continuation;
            if ((channelsKt__Channels_commonKt$filterNotNullTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$filterNotNullTo$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$filterNotNullTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$filterNotNullTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        channelsKt__Channels_commonKt$filterNotNullTo$12 = channelsKt__Channels_commonKt$filterNotNullTo$1;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        c2 = c;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterNotNullTo$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$1.L$5;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$filterNotNullTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$1.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$filterNotNullTo$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        th = th4;
                        c2 = c3;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$filterNotNullTo$12 = channelsKt__Channels_commonKt$filterNotNullTo$1;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            if (next != null) {
                                c2.add(next);
                            }
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                        }
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        return c2;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$filterNotNullTo$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$filterNotNullTo$12.L$1 = c2;
                channelsKt__Channels_commonKt$filterNotNullTo$12.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$filterNotNullTo$12.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$filterNotNullTo$12.L$4 = th2;
                channelsKt__Channels_commonKt$filterNotNullTo$12.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$filterNotNullTo$12.L$6 = channelIterator;
                channelsKt__Channels_commonKt$filterNotNullTo$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterNotNullTo$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                return c2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$filterNotNullTo$1 = new ChannelsKt__Channels_commonKt$filterNotNullTo$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$filterNotNullTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$filterNotNullTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$filterNotNullTo$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$filterNotNullTo$12.L$1 = c2;
            channelsKt__Channels_commonKt$filterNotNullTo$12.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$filterNotNullTo$12.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$filterNotNullTo$12.L$4 = th2;
            channelsKt__Channels_commonKt$filterNotNullTo$12.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$filterNotNullTo$12.L$6 = channelIterator;
            channelsKt__Channels_commonKt$filterNotNullTo$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterNotNullTo$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x007e  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a7 A[Catch:{ all -> 0x00f2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00b7 A[Catch:{ all -> 0x00f2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends SendChannel<? super E>> Object filterNotNullTo(ReceiveChannel<? extends E> receiveChannel, C c, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$filterNotNullTo$3 channelsKt__Channels_commonKt$filterNotNullTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel4;
        C c2;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Throwable th;
        ChannelsKt__Channels_commonKt$filterNotNullTo$3 channelsKt__Channels_commonKt$filterNotNullTo$32;
        ReceiveChannel<? extends E> receiveChannel7;
        ReceiveChannel<? extends E> receiveChannel8;
        Object obj;
        C c3;
        ChannelIterator channelIterator2;
        Object obj2;
        ReceiveChannel<? extends E> receiveChannel9;
        if (continuation instanceof ChannelsKt__Channels_commonKt$filterNotNullTo$3) {
            channelsKt__Channels_commonKt$filterNotNullTo$3 = (ChannelsKt__Channels_commonKt$filterNotNullTo$3) continuation;
            if ((channelsKt__Channels_commonKt$filterNotNullTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$filterNotNullTo$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$filterNotNullTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$filterNotNullTo$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        channelsKt__Channels_commonKt$filterNotNullTo$32 = channelsKt__Channels_commonKt$filterNotNullTo$3;
                        obj = coroutine_suspended;
                        channelIterator2 = receiveChannel.iterator();
                        receiveChannel7 = receiveChannel;
                        c3 = c;
                        th = null;
                        receiveChannel8 = receiveChannel7;
                        receiveChannel2 = receiveChannel8;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        try {
                            throw th;
                        } catch (Throwable th3) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th3;
                        }
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterNotNullTo$3.L$6;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$5;
                    th = (Throwable) channelsKt__Channels_commonKt$filterNotNullTo$3.L$4;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$3;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$0;
                    ResultKt.throwOnFailure(obj3);
                    ReceiveChannel<? extends E> receiveChannel11 = receiveChannel10;
                    obj2 = obj3;
                    receiveChannel2 = receiveChannel6;
                    receiveChannel9 = receiveChannel11;
                    if (!((Boolean) obj2).booleanValue()) {
                        Object next = channelIterator.next();
                        if (next != null) {
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$0 = receiveChannel9;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$1 = c2;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$2 = receiveChannel5;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$3 = receiveChannel2;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$4 = th;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$5 = receiveChannel3;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$6 = channelIterator;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$7 = next;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.L$8 = next;
                            channelsKt__Channels_commonKt$filterNotNullTo$3.label = 2;
                            if (c2.send(next, channelsKt__Channels_commonKt$filterNotNullTo$3) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            receiveChannel4 = receiveChannel9;
                            receiveChannel6 = receiveChannel2;
                            receiveChannel2 = receiveChannel6;
                            channelsKt__Channels_commonKt$filterNotNullTo$32 = channelsKt__Channels_commonKt$filterNotNullTo$3;
                            receiveChannel7 = receiveChannel3;
                            receiveChannel8 = receiveChannel5;
                            obj = coroutine_suspended;
                            c3 = c2;
                            channelIterator2 = channelIterator;
                            receiveChannel = receiveChannel4;
                            return coroutine_suspended;
                        }
                        C c4 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel9;
                        channelsKt__Channels_commonKt$filterNotNullTo$32 = channelsKt__Channels_commonKt$filterNotNullTo$3;
                        receiveChannel7 = receiveChannel3;
                        receiveChannel8 = receiveChannel5;
                        obj = coroutine_suspended;
                        c3 = c4;
                    }
                    Unit unit = Unit.INSTANCE;
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    return c2;
                } else if (i == 2) {
                    Object obj4 = channelsKt__Channels_commonKt$filterNotNullTo$3.L$8;
                    Object obj5 = channelsKt__Channels_commonKt$filterNotNullTo$3.L$7;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterNotNullTo$3.L$6;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$5;
                    th = (Throwable) channelsKt__Channels_commonKt$filterNotNullTo$3.L$4;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$3;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotNullTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        receiveChannel2 = receiveChannel6;
                        channelsKt__Channels_commonKt$filterNotNullTo$32 = channelsKt__Channels_commonKt$filterNotNullTo$3;
                        receiveChannel7 = receiveChannel3;
                        receiveChannel8 = receiveChannel5;
                        obj = coroutine_suspended;
                        c3 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel4;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel6;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$filterNotNullTo$32.L$0 = receiveChannel;
                channelsKt__Channels_commonKt$filterNotNullTo$32.L$1 = c3;
                channelsKt__Channels_commonKt$filterNotNullTo$32.L$2 = receiveChannel8;
                channelsKt__Channels_commonKt$filterNotNullTo$32.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$filterNotNullTo$32.L$4 = th;
                channelsKt__Channels_commonKt$filterNotNullTo$32.L$5 = receiveChannel7;
                channelsKt__Channels_commonKt$filterNotNullTo$32.L$6 = channelIterator2;
                channelsKt__Channels_commonKt$filterNotNullTo$32.label = 1;
                obj2 = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterNotNullTo$32);
                if (obj2 != obj) {
                    return obj;
                }
                ChannelsKt__Channels_commonKt$filterNotNullTo$3 channelsKt__Channels_commonKt$filterNotNullTo$33 = channelsKt__Channels_commonKt$filterNotNullTo$32;
                receiveChannel9 = receiveChannel;
                channelIterator = channelIterator2;
                c2 = c3;
                coroutine_suspended = obj;
                receiveChannel5 = receiveChannel8;
                receiveChannel3 = receiveChannel7;
                channelsKt__Channels_commonKt$filterNotNullTo$3 = channelsKt__Channels_commonKt$filterNotNullTo$33;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$filterNotNullTo$3 = new ChannelsKt__Channels_commonKt$filterNotNullTo$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$filterNotNullTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$filterNotNullTo$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$filterNotNullTo$32.L$0 = receiveChannel;
            channelsKt__Channels_commonKt$filterNotNullTo$32.L$1 = c3;
            channelsKt__Channels_commonKt$filterNotNullTo$32.L$2 = receiveChannel8;
            channelsKt__Channels_commonKt$filterNotNullTo$32.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$filterNotNullTo$32.L$4 = th;
            channelsKt__Channels_commonKt$filterNotNullTo$32.L$5 = receiveChannel7;
            channelsKt__Channels_commonKt$filterNotNullTo$32.L$6 = channelIterator2;
            channelsKt__Channels_commonKt$filterNotNullTo$32.label = 1;
            obj2 = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterNotNullTo$32);
            if (obj2 != obj) {
            }
            return obj;
        } catch (Throwable th5) {
            th = th5;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009a A[Catch:{ all -> 0x0053 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends Collection<? super E>> Object filterNotTo(ReceiveChannel<? extends E> receiveChannel, C c, Function1<? super E, Boolean> function1, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$filterNotTo$1 channelsKt__Channels_commonKt$filterNotTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$filterNotTo$1 channelsKt__Channels_commonKt$filterNotTo$12;
        Function1<? super E, Boolean> function12;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel5;
        C c2;
        ReceiveChannel<? extends E> receiveChannel6;
        Function1<? super E, Boolean> function13;
        ChannelsKt__Channels_commonKt$filterNotTo$1 channelsKt__Channels_commonKt$filterNotTo$13;
        Object obj2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$filterNotTo$1) {
            channelsKt__Channels_commonKt$filterNotTo$1 = (ChannelsKt__Channels_commonKt$filterNotTo$1) continuation;
            if ((channelsKt__Channels_commonKt$filterNotTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$filterNotTo$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$filterNotTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$filterNotTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th = null;
                        channelsKt__Channels_commonKt$filterNotTo$13 = channelsKt__Channels_commonKt$filterNotTo$1;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        receiveChannel6 = receiveChannel2;
                        c2 = c;
                        receiveChannel4 = receiveChannel6;
                        channelIterator = receiveChannel.iterator();
                        function13 = function1;
                        receiveChannel3 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterNotTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$1.L$6;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$filterNotTo$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$1.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$filterNotTo$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$filterNotTo$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$filterNotTo$1 channelsKt__Channels_commonKt$filterNotTo$14 = channelsKt__Channels_commonKt$filterNotTo$1;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel4 = receiveChannel9;
                        obj = coroutine_suspended;
                        c2 = c3;
                        channelsKt__Channels_commonKt$filterNotTo$12 = channelsKt__Channels_commonKt$filterNotTo$14;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        th = th3;
                        receiveChannel3 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            if (!((Boolean) function12.invoke(next)).booleanValue()) {
                                c2.add(next);
                            }
                            receiveChannel2 = receiveChannel5;
                            function13 = function12;
                            channelsKt__Channels_commonKt$filterNotTo$13 = channelsKt__Channels_commonKt$filterNotTo$12;
                            obj2 = obj;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel5, th);
                        InlineMarker.finallyEnd(1);
                        return c2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel5;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$filterNotTo$13.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$filterNotTo$13.L$1 = c2;
                channelsKt__Channels_commonKt$filterNotTo$13.L$2 = function13;
                channelsKt__Channels_commonKt$filterNotTo$13.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$filterNotTo$13.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$filterNotTo$13.L$5 = th;
                channelsKt__Channels_commonKt$filterNotTo$13.L$6 = receiveChannel6;
                channelsKt__Channels_commonKt$filterNotTo$13.L$7 = channelIterator;
                channelsKt__Channels_commonKt$filterNotTo$13.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterNotTo$13);
                if (hasNext != obj2) {
                    return obj2;
                }
                Function1<? super E, Boolean> function14 = function13;
                receiveChannel5 = receiveChannel2;
                obj3 = hasNext;
                obj = obj2;
                channelsKt__Channels_commonKt$filterNotTo$12 = channelsKt__Channels_commonKt$filterNotTo$13;
                function12 = function14;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel5, th);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$filterNotTo$1 = new ChannelsKt__Channels_commonKt$filterNotTo$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$filterNotTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$filterNotTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$filterNotTo$13.L$0 = receiveChannel4;
            channelsKt__Channels_commonKt$filterNotTo$13.L$1 = c2;
            channelsKt__Channels_commonKt$filterNotTo$13.L$2 = function13;
            channelsKt__Channels_commonKt$filterNotTo$13.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$filterNotTo$13.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$filterNotTo$13.L$5 = th;
            channelsKt__Channels_commonKt$filterNotTo$13.L$6 = receiveChannel6;
            channelsKt__Channels_commonKt$filterNotTo$13.L$7 = channelIterator;
            channelsKt__Channels_commonKt$filterNotTo$13.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterNotTo$13);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th6) {
            th = th6;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b1 A[Catch:{ all -> 0x0117 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00c4 A[Catch:{ all -> 0x0117 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends SendChannel<? super E>> Object filterNotTo(ReceiveChannel<? extends E> receiveChannel, C c, Function1<? super E, Boolean> function1, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$filterNotTo$3 channelsKt__Channels_commonKt$filterNotTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel4;
        C c2;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        ReceiveChannel<? extends E> receiveChannel8;
        ChannelsKt__Channels_commonKt$filterNotTo$3 channelsKt__Channels_commonKt$filterNotTo$32;
        C c3;
        ChannelIterator channelIterator2;
        Throwable th2;
        Object obj;
        Function1<? super E, Boolean> function13;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$filterNotTo$3) {
            channelsKt__Channels_commonKt$filterNotTo$3 = (ChannelsKt__Channels_commonKt$filterNotTo$3) continuation;
            if ((channelsKt__Channels_commonKt$filterNotTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$filterNotTo$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$filterNotTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$filterNotTo$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        channelsKt__Channels_commonKt$filterNotTo$32 = channelsKt__Channels_commonKt$filterNotTo$3;
                        obj = coroutine_suspended;
                        channelIterator2 = receiveChannel.iterator();
                        c3 = c;
                        function13 = function1;
                        th2 = null;
                        receiveChannel8 = receiveChannel;
                        receiveChannel2 = receiveChannel8;
                        receiveChannel7 = receiveChannel2;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th4;
                        }
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterNotTo$3.L$7;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$6;
                    Throwable th5 = (Throwable) channelsKt__Channels_commonKt$filterNotTo$3.L$5;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$filterNotTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$0;
                    ResultKt.throwOnFailure(obj2);
                    ReceiveChannel<? extends E> receiveChannel9 = receiveChannel6;
                    th2 = th5;
                    receiveChannel2 = receiveChannel9;
                    if (!((Boolean) obj2).booleanValue()) {
                        Object next = channelIterator.next();
                        if (!((Boolean) function12.invoke(next)).booleanValue()) {
                            channelsKt__Channels_commonKt$filterNotTo$3.L$0 = receiveChannel4;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$1 = c2;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$2 = function12;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$3 = receiveChannel5;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$4 = receiveChannel2;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$5 = th2;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$6 = receiveChannel3;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$7 = channelIterator;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$8 = next;
                            channelsKt__Channels_commonKt$filterNotTo$3.L$9 = next;
                            channelsKt__Channels_commonKt$filterNotTo$3.label = 2;
                            if (c2.send(next, channelsKt__Channels_commonKt$filterNotTo$3) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Throwable th6 = th2;
                            receiveChannel6 = receiveChannel2;
                            th = th6;
                            receiveChannel7 = receiveChannel3;
                            receiveChannel8 = receiveChannel5;
                            channelsKt__Channels_commonKt$filterNotTo$32 = channelsKt__Channels_commonKt$filterNotTo$3;
                            c3 = c2;
                            channelIterator2 = channelIterator;
                            receiveChannel = receiveChannel4;
                            ReceiveChannel<? extends E> receiveChannel10 = receiveChannel6;
                            th2 = th;
                            receiveChannel2 = receiveChannel10;
                            Function1<? super E, Boolean> function14 = function12;
                            obj = coroutine_suspended;
                            function13 = function14;
                            return coroutine_suspended;
                        }
                        receiveChannel7 = receiveChannel3;
                        receiveChannel8 = receiveChannel5;
                        channelsKt__Channels_commonKt$filterNotTo$32 = channelsKt__Channels_commonKt$filterNotTo$3;
                        c3 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel4;
                        Function1<? super E, Boolean> function15 = function12;
                        obj = coroutine_suspended;
                        function13 = function15;
                    }
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    InlineMarker.finallyEnd(1);
                    return c2;
                } else if (i == 2) {
                    Object obj3 = channelsKt__Channels_commonKt$filterNotTo$3.L$9;
                    Object obj4 = channelsKt__Channels_commonKt$filterNotTo$3.L$8;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterNotTo$3.L$7;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$6;
                    th = (Throwable) channelsKt__Channels_commonKt$filterNotTo$3.L$5;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$filterNotTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterNotTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        receiveChannel7 = receiveChannel3;
                        receiveChannel8 = receiveChannel5;
                        channelsKt__Channels_commonKt$filterNotTo$32 = channelsKt__Channels_commonKt$filterNotTo$3;
                        c3 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel4;
                        ReceiveChannel<? extends E> receiveChannel102 = receiveChannel6;
                        th2 = th;
                        receiveChannel2 = receiveChannel102;
                        Function1<? super E, Boolean> function142 = function12;
                        obj = coroutine_suspended;
                        function13 = function142;
                    } catch (Throwable th7) {
                        th = th7;
                        receiveChannel2 = receiveChannel6;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$filterNotTo$32.L$0 = receiveChannel;
                channelsKt__Channels_commonKt$filterNotTo$32.L$1 = c3;
                channelsKt__Channels_commonKt$filterNotTo$32.L$2 = function13;
                channelsKt__Channels_commonKt$filterNotTo$32.L$3 = receiveChannel8;
                channelsKt__Channels_commonKt$filterNotTo$32.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$filterNotTo$32.L$5 = th2;
                channelsKt__Channels_commonKt$filterNotTo$32.L$6 = receiveChannel7;
                channelsKt__Channels_commonKt$filterNotTo$32.L$7 = channelIterator2;
                channelsKt__Channels_commonKt$filterNotTo$32.label = 1;
                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterNotTo$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj5 = hasNext;
                receiveChannel4 = receiveChannel;
                channelIterator = channelIterator2;
                c2 = c3;
                channelsKt__Channels_commonKt$filterNotTo$3 = channelsKt__Channels_commonKt$filterNotTo$32;
                receiveChannel5 = receiveChannel8;
                receiveChannel3 = receiveChannel7;
                obj2 = obj5;
                Object obj6 = obj;
                function12 = function13;
                coroutine_suspended = obj6;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$filterNotTo$3 = new ChannelsKt__Channels_commonKt$filterNotTo$3(continuation);
        Object obj22 = channelsKt__Channels_commonKt$filterNotTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$filterNotTo$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$filterNotTo$32.L$0 = receiveChannel;
            channelsKt__Channels_commonKt$filterNotTo$32.L$1 = c3;
            channelsKt__Channels_commonKt$filterNotTo$32.L$2 = function13;
            channelsKt__Channels_commonKt$filterNotTo$32.L$3 = receiveChannel8;
            channelsKt__Channels_commonKt$filterNotTo$32.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$filterNotTo$32.L$5 = th2;
            channelsKt__Channels_commonKt$filterNotTo$32.L$6 = receiveChannel7;
            channelsKt__Channels_commonKt$filterNotTo$32.L$7 = channelIterator2;
            channelsKt__Channels_commonKt$filterNotTo$32.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterNotTo$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009a A[Catch:{ all -> 0x0053 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends Collection<? super E>> Object filterTo(ReceiveChannel<? extends E> receiveChannel, C c, Function1<? super E, Boolean> function1, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$filterTo$1 channelsKt__Channels_commonKt$filterTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$filterTo$1 channelsKt__Channels_commonKt$filterTo$12;
        Function1<? super E, Boolean> function12;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel5;
        C c2;
        ReceiveChannel<? extends E> receiveChannel6;
        Function1<? super E, Boolean> function13;
        ChannelsKt__Channels_commonKt$filterTo$1 channelsKt__Channels_commonKt$filterTo$13;
        Object obj2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$filterTo$1) {
            channelsKt__Channels_commonKt$filterTo$1 = (ChannelsKt__Channels_commonKt$filterTo$1) continuation;
            if ((channelsKt__Channels_commonKt$filterTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$filterTo$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$filterTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$filterTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th = null;
                        channelsKt__Channels_commonKt$filterTo$13 = channelsKt__Channels_commonKt$filterTo$1;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        receiveChannel6 = receiveChannel2;
                        c2 = c;
                        receiveChannel4 = receiveChannel6;
                        channelIterator = receiveChannel.iterator();
                        function13 = function1;
                        receiveChannel3 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$1.L$6;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$filterTo$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$1.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$filterTo$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$filterTo$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$filterTo$1 channelsKt__Channels_commonKt$filterTo$14 = channelsKt__Channels_commonKt$filterTo$1;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel4 = receiveChannel9;
                        obj = coroutine_suspended;
                        c2 = c3;
                        channelsKt__Channels_commonKt$filterTo$12 = channelsKt__Channels_commonKt$filterTo$14;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        th = th3;
                        receiveChannel3 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                c2.add(next);
                            }
                            receiveChannel2 = receiveChannel5;
                            function13 = function12;
                            channelsKt__Channels_commonKt$filterTo$13 = channelsKt__Channels_commonKt$filterTo$12;
                            obj2 = obj;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel5, th);
                        InlineMarker.finallyEnd(1);
                        return c2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel5;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$filterTo$13.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$filterTo$13.L$1 = c2;
                channelsKt__Channels_commonKt$filterTo$13.L$2 = function13;
                channelsKt__Channels_commonKt$filterTo$13.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$filterTo$13.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$filterTo$13.L$5 = th;
                channelsKt__Channels_commonKt$filterTo$13.L$6 = receiveChannel6;
                channelsKt__Channels_commonKt$filterTo$13.L$7 = channelIterator;
                channelsKt__Channels_commonKt$filterTo$13.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterTo$13);
                if (hasNext != obj2) {
                    return obj2;
                }
                Function1<? super E, Boolean> function14 = function13;
                receiveChannel5 = receiveChannel2;
                obj3 = hasNext;
                obj = obj2;
                channelsKt__Channels_commonKt$filterTo$12 = channelsKt__Channels_commonKt$filterTo$13;
                function12 = function14;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel5, th);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$filterTo$1 = new ChannelsKt__Channels_commonKt$filterTo$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$filterTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$filterTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$filterTo$13.L$0 = receiveChannel4;
            channelsKt__Channels_commonKt$filterTo$13.L$1 = c2;
            channelsKt__Channels_commonKt$filterTo$13.L$2 = function13;
            channelsKt__Channels_commonKt$filterTo$13.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$filterTo$13.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$filterTo$13.L$5 = th;
            channelsKt__Channels_commonKt$filterTo$13.L$6 = receiveChannel6;
            channelsKt__Channels_commonKt$filterTo$13.L$7 = channelIterator;
            channelsKt__Channels_commonKt$filterTo$13.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterTo$13);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th6) {
            th = th6;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b1 A[Catch:{ all -> 0x0117 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00c4 A[Catch:{ all -> 0x0117 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends SendChannel<? super E>> Object filterTo(ReceiveChannel<? extends E> receiveChannel, C c, Function1<? super E, Boolean> function1, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$filterTo$3 channelsKt__Channels_commonKt$filterTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel4;
        C c2;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        ReceiveChannel<? extends E> receiveChannel8;
        ChannelsKt__Channels_commonKt$filterTo$3 channelsKt__Channels_commonKt$filterTo$32;
        C c3;
        ChannelIterator channelIterator2;
        Throwable th2;
        Object obj;
        Function1<? super E, Boolean> function13;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$filterTo$3) {
            channelsKt__Channels_commonKt$filterTo$3 = (ChannelsKt__Channels_commonKt$filterTo$3) continuation;
            if ((channelsKt__Channels_commonKt$filterTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$filterTo$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$filterTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$filterTo$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        channelsKt__Channels_commonKt$filterTo$32 = channelsKt__Channels_commonKt$filterTo$3;
                        obj = coroutine_suspended;
                        channelIterator2 = receiveChannel.iterator();
                        c3 = c;
                        function13 = function1;
                        th2 = null;
                        receiveChannel8 = receiveChannel;
                        receiveChannel2 = receiveChannel8;
                        receiveChannel7 = receiveChannel2;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th4;
                        }
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterTo$3.L$7;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$3.L$6;
                    Throwable th5 = (Throwable) channelsKt__Channels_commonKt$filterTo$3.L$5;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$3.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$3.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$filterTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$filterTo$3.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$3.L$0;
                    ResultKt.throwOnFailure(obj2);
                    ReceiveChannel<? extends E> receiveChannel9 = receiveChannel6;
                    th2 = th5;
                    receiveChannel2 = receiveChannel9;
                    if (!((Boolean) obj2).booleanValue()) {
                        Object next = channelIterator.next();
                        if (((Boolean) function12.invoke(next)).booleanValue()) {
                            channelsKt__Channels_commonKt$filterTo$3.L$0 = receiveChannel4;
                            channelsKt__Channels_commonKt$filterTo$3.L$1 = c2;
                            channelsKt__Channels_commonKt$filterTo$3.L$2 = function12;
                            channelsKt__Channels_commonKt$filterTo$3.L$3 = receiveChannel5;
                            channelsKt__Channels_commonKt$filterTo$3.L$4 = receiveChannel2;
                            channelsKt__Channels_commonKt$filterTo$3.L$5 = th2;
                            channelsKt__Channels_commonKt$filterTo$3.L$6 = receiveChannel3;
                            channelsKt__Channels_commonKt$filterTo$3.L$7 = channelIterator;
                            channelsKt__Channels_commonKt$filterTo$3.L$8 = next;
                            channelsKt__Channels_commonKt$filterTo$3.L$9 = next;
                            channelsKt__Channels_commonKt$filterTo$3.label = 2;
                            if (c2.send(next, channelsKt__Channels_commonKt$filterTo$3) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Throwable th6 = th2;
                            receiveChannel6 = receiveChannel2;
                            th = th6;
                            receiveChannel7 = receiveChannel3;
                            receiveChannel8 = receiveChannel5;
                            channelsKt__Channels_commonKt$filterTo$32 = channelsKt__Channels_commonKt$filterTo$3;
                            c3 = c2;
                            channelIterator2 = channelIterator;
                            receiveChannel = receiveChannel4;
                            ReceiveChannel<? extends E> receiveChannel10 = receiveChannel6;
                            th2 = th;
                            receiveChannel2 = receiveChannel10;
                            Function1<? super E, Boolean> function14 = function12;
                            obj = coroutine_suspended;
                            function13 = function14;
                            return coroutine_suspended;
                        }
                        receiveChannel7 = receiveChannel3;
                        receiveChannel8 = receiveChannel5;
                        channelsKt__Channels_commonKt$filterTo$32 = channelsKt__Channels_commonKt$filterTo$3;
                        c3 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel4;
                        Function1<? super E, Boolean> function15 = function12;
                        obj = coroutine_suspended;
                        function13 = function15;
                    }
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    InlineMarker.finallyEnd(1);
                    return c2;
                } else if (i == 2) {
                    Object obj3 = channelsKt__Channels_commonKt$filterTo$3.L$9;
                    Object obj4 = channelsKt__Channels_commonKt$filterTo$3.L$8;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$filterTo$3.L$7;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$3.L$6;
                    th = (Throwable) channelsKt__Channels_commonKt$filterTo$3.L$5;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$3.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$3.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$filterTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$filterTo$3.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$filterTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        receiveChannel7 = receiveChannel3;
                        receiveChannel8 = receiveChannel5;
                        channelsKt__Channels_commonKt$filterTo$32 = channelsKt__Channels_commonKt$filterTo$3;
                        c3 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel4;
                        ReceiveChannel<? extends E> receiveChannel102 = receiveChannel6;
                        th2 = th;
                        receiveChannel2 = receiveChannel102;
                        Function1<? super E, Boolean> function142 = function12;
                        obj = coroutine_suspended;
                        function13 = function142;
                    } catch (Throwable th7) {
                        th = th7;
                        receiveChannel2 = receiveChannel6;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$filterTo$32.L$0 = receiveChannel;
                channelsKt__Channels_commonKt$filterTo$32.L$1 = c3;
                channelsKt__Channels_commonKt$filterTo$32.L$2 = function13;
                channelsKt__Channels_commonKt$filterTo$32.L$3 = receiveChannel8;
                channelsKt__Channels_commonKt$filterTo$32.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$filterTo$32.L$5 = th2;
                channelsKt__Channels_commonKt$filterTo$32.L$6 = receiveChannel7;
                channelsKt__Channels_commonKt$filterTo$32.L$7 = channelIterator2;
                channelsKt__Channels_commonKt$filterTo$32.label = 1;
                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterTo$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj5 = hasNext;
                receiveChannel4 = receiveChannel;
                channelIterator = channelIterator2;
                c2 = c3;
                channelsKt__Channels_commonKt$filterTo$3 = channelsKt__Channels_commonKt$filterTo$32;
                receiveChannel5 = receiveChannel8;
                receiveChannel3 = receiveChannel7;
                obj2 = obj5;
                Object obj6 = obj;
                function12 = function13;
                coroutine_suspended = obj6;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$filterTo$3 = new ChannelsKt__Channels_commonKt$filterTo$3(continuation);
        Object obj22 = channelsKt__Channels_commonKt$filterTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$filterTo$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$filterTo$32.L$0 = receiveChannel;
            channelsKt__Channels_commonKt$filterTo$32.L$1 = c3;
            channelsKt__Channels_commonKt$filterTo$32.L$2 = function13;
            channelsKt__Channels_commonKt$filterTo$32.L$3 = receiveChannel8;
            channelsKt__Channels_commonKt$filterTo$32.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$filterTo$32.L$5 = th2;
            channelsKt__Channels_commonKt$filterTo$32.L$6 = receiveChannel7;
            channelsKt__Channels_commonKt$filterTo$32.L$7 = channelIterator2;
            channelsKt__Channels_commonKt$filterTo$32.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$filterTo$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            throw th;
        }
    }

    public static /* synthetic */ ReceiveChannel take$default(ReceiveChannel receiveChannel, int i, CoroutineContext coroutineContext, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.take(receiveChannel, i, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> take(ReceiveChannel<? extends E> receiveChannel, int i, CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$take");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$take$1(receiveChannel, i, null), 2, null);
    }

    public static /* synthetic */ ReceiveChannel takeWhile$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.takeWhile(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> takeWhile(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$takeWhile");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$takeWhile$1(receiveChannel, function2, null), 2, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x009c A[Catch:{ all -> 0x00ce }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a9 A[Catch:{ all -> 0x00ce }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K, V> Object associate(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, ? extends Pair<? extends K, ? extends V>> function1, Continuation<? super Map<K, ? extends V>> continuation) {
        ChannelsKt__Channels_commonKt$associate$1 channelsKt__Channels_commonKt$associate$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Map map;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel4;
        Object obj;
        ChannelsKt__Channels_commonKt$associate$1 channelsKt__Channels_commonKt$associate$12;
        Function1<? super E, ? extends Pair<? extends K, ? extends V>> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Throwable th2;
        Map map2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$associate$1) {
            channelsKt__Channels_commonKt$associate$1 = (ChannelsKt__Channels_commonKt$associate$1) continuation;
            if ((channelsKt__Channels_commonKt$associate$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$associate$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$associate$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$associate$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        map2 = new LinkedHashMap();
                        channelsKt__Channels_commonKt$associate$12 = channelsKt__Channels_commonKt$associate$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$associate$1.L$8;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$associate$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$associate$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$associate$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$associate$1.L$4;
                    Map map3 = (Map) channelsKt__Channels_commonKt$associate$1.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$associate$1.L$2;
                    Function1<? super E, ? extends Pair<? extends K, ? extends V>> function13 = (Function1) channelsKt__Channels_commonKt$associate$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel11 = (ReceiveChannel) channelsKt__Channels_commonKt$associate$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        Throwable th5 = th4;
                        receiveChannel5 = receiveChannel8;
                        receiveChannel3 = receiveChannel11;
                        map = map3;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel9;
                        function12 = function13;
                        th = th5;
                        ReceiveChannel<? extends E> receiveChannel12 = receiveChannel10;
                        channelsKt__Channels_commonKt$associate$12 = channelsKt__Channels_commonKt$associate$1;
                        receiveChannel6 = receiveChannel12;
                        if (!((Boolean) obj2).booleanValue()) {
                            Pair pair = (Pair) function12.invoke(channelIterator.next());
                            map.put(pair.getFirst(), pair.getSecond());
                            receiveChannel7 = receiveChannel4;
                            th2 = th;
                            map2 = map;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return map;
                    } catch (Throwable th6) {
                        th = th6;
                        receiveChannel2 = receiveChannel9;
                        try {
                            throw th;
                        } catch (Throwable th7) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$associate$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$associate$12.L$1 = function12;
                channelsKt__Channels_commonKt$associate$12.L$2 = receiveChannel7;
                channelsKt__Channels_commonKt$associate$12.L$3 = map2;
                channelsKt__Channels_commonKt$associate$12.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$associate$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$associate$12.L$6 = th2;
                channelsKt__Channels_commonKt$associate$12.L$7 = receiveChannel5;
                channelsKt__Channels_commonKt$associate$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$associate$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associate$12);
                if (hasNext != obj) {
                    return obj;
                }
                Throwable th8 = th2;
                receiveChannel4 = receiveChannel7;
                obj2 = hasNext;
                map = map2;
                th = th8;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return map;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$associate$1 = new ChannelsKt__Channels_commonKt$associate$1(continuation);
        Object obj22 = channelsKt__Channels_commonKt$associate$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$associate$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$associate$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$associate$12.L$1 = function12;
            channelsKt__Channels_commonKt$associate$12.L$2 = receiveChannel7;
            channelsKt__Channels_commonKt$associate$12.L$3 = map2;
            channelsKt__Channels_commonKt$associate$12.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$associate$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$associate$12.L$6 = th2;
            channelsKt__Channels_commonKt$associate$12.L$7 = receiveChannel5;
            channelsKt__Channels_commonKt$associate$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$associate$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associate$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th9) {
            th = th9;
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0046, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0047, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0050, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object associate$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Map linkedHashMap = new LinkedHashMap();
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Pair pair = (Pair) function1.invoke(it.next());
                linkedHashMap.put(pair.getFirst(), pair.getSecond());
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return linkedHashMap;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x009c A[Catch:{ all -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a9 A[Catch:{ all -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K> Object associateBy(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, ? extends K> function1, Continuation<? super Map<K, ? extends E>> continuation) {
        ChannelsKt__Channels_commonKt$associateBy$1 channelsKt__Channels_commonKt$associateBy$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Map map;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel4;
        Object obj;
        ChannelsKt__Channels_commonKt$associateBy$1 channelsKt__Channels_commonKt$associateBy$12;
        Function1<? super E, ? extends K> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Throwable th2;
        Map map2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$associateBy$1) {
            channelsKt__Channels_commonKt$associateBy$1 = (ChannelsKt__Channels_commonKt$associateBy$1) continuation;
            if ((channelsKt__Channels_commonKt$associateBy$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$associateBy$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$associateBy$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$associateBy$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        map2 = new LinkedHashMap();
                        channelsKt__Channels_commonKt$associateBy$12 = channelsKt__Channels_commonKt$associateBy$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$associateBy$1.L$8;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$associateBy$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$1.L$4;
                    Map map3 = (Map) channelsKt__Channels_commonKt$associateBy$1.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$1.L$2;
                    Function1<? super E, ? extends K> function13 = (Function1) channelsKt__Channels_commonKt$associateBy$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel11 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        Throwable th5 = th4;
                        receiveChannel5 = receiveChannel8;
                        receiveChannel3 = receiveChannel11;
                        map = map3;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel9;
                        function12 = function13;
                        th = th5;
                        ReceiveChannel<? extends E> receiveChannel12 = receiveChannel10;
                        channelsKt__Channels_commonKt$associateBy$12 = channelsKt__Channels_commonKt$associateBy$1;
                        receiveChannel6 = receiveChannel12;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            map.put(function12.invoke(next), next);
                            receiveChannel7 = receiveChannel4;
                            th2 = th;
                            map2 = map;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return map;
                    } catch (Throwable th6) {
                        th = th6;
                        receiveChannel2 = receiveChannel9;
                        try {
                            throw th;
                        } catch (Throwable th7) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$associateBy$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$associateBy$12.L$1 = function12;
                channelsKt__Channels_commonKt$associateBy$12.L$2 = receiveChannel7;
                channelsKt__Channels_commonKt$associateBy$12.L$3 = map2;
                channelsKt__Channels_commonKt$associateBy$12.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$associateBy$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$associateBy$12.L$6 = th2;
                channelsKt__Channels_commonKt$associateBy$12.L$7 = receiveChannel5;
                channelsKt__Channels_commonKt$associateBy$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$associateBy$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateBy$12);
                if (hasNext != obj) {
                    return obj;
                }
                Throwable th8 = th2;
                receiveChannel4 = receiveChannel7;
                obj2 = hasNext;
                map = map2;
                th = th8;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return map;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$associateBy$1 = new ChannelsKt__Channels_commonKt$associateBy$1(continuation);
        Object obj22 = channelsKt__Channels_commonKt$associateBy$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$associateBy$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$associateBy$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$associateBy$12.L$1 = function12;
            channelsKt__Channels_commonKt$associateBy$12.L$2 = receiveChannel7;
            channelsKt__Channels_commonKt$associateBy$12.L$3 = map2;
            channelsKt__Channels_commonKt$associateBy$12.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$associateBy$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$associateBy$12.L$6 = th2;
            channelsKt__Channels_commonKt$associateBy$12.L$7 = receiveChannel5;
            channelsKt__Channels_commonKt$associateBy$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$associateBy$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateBy$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th9) {
            th = th9;
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003c, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object associateBy$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Map linkedHashMap = new LinkedHashMap();
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                linkedHashMap.put(function1.invoke(next), next);
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return linkedHashMap;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a2 A[Catch:{ all -> 0x00d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b0 A[Catch:{ all -> 0x00d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K, V> Object associateBy(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, ? extends K> function1, Function1<? super E, ? extends V> function12, Continuation<? super Map<K, ? extends V>> continuation) {
        ChannelsKt__Channels_commonKt$associateBy$2 channelsKt__Channels_commonKt$associateBy$2;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Map map;
        Object obj;
        Function1<? super E, ? extends V> function13;
        ChannelsKt__Channels_commonKt$associateBy$2 channelsKt__Channels_commonKt$associateBy$22;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel5;
        Function1<? super E, ? extends K> function14;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Function1<? super E, ? extends V> function15;
        Object obj2;
        Map map2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$associateBy$2) {
            channelsKt__Channels_commonKt$associateBy$2 = (ChannelsKt__Channels_commonKt$associateBy$2) continuation;
            if ((channelsKt__Channels_commonKt$associateBy$2.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$associateBy$2.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$associateBy$2.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$associateBy$2.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        map2 = new LinkedHashMap();
                        channelsKt__Channels_commonKt$associateBy$22 = channelsKt__Channels_commonKt$associateBy$2;
                        obj2 = coroutine_suspended;
                        th = null;
                        receiveChannel7 = receiveChannel;
                        receiveChannel2 = receiveChannel7;
                        receiveChannel6 = receiveChannel2;
                        function14 = function1;
                        receiveChannel4 = receiveChannel6;
                        channelIterator = receiveChannel.iterator();
                        function15 = function12;
                        receiveChannel3 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$associateBy$2.L$9;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$2.L$8;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$associateBy$2.L$7;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$2.L$6;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$2.L$5;
                    Map map3 = (Map) channelsKt__Channels_commonKt$associateBy$2.L$4;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$2.L$3;
                    function13 = (Function1) channelsKt__Channels_commonKt$associateBy$2.L$2;
                    Function1<? super E, ? extends K> function16 = (Function1) channelsKt__Channels_commonKt$associateBy$2.L$1;
                    ReceiveChannel<? extends E> receiveChannel11 = (ReceiveChannel) channelsKt__Channels_commonKt$associateBy$2.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel6 = receiveChannel8;
                        receiveChannel4 = receiveChannel11;
                        map = map3;
                        th = th3;
                        receiveChannel3 = receiveChannel10;
                        channelsKt__Channels_commonKt$associateBy$22 = channelsKt__Channels_commonKt$associateBy$2;
                        receiveChannel2 = receiveChannel9;
                        function14 = function16;
                        obj = obj4;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            map.put(function14.invoke(next), function13.invoke(next));
                            receiveChannel7 = receiveChannel5;
                            function15 = function13;
                            obj2 = obj;
                            map2 = map;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return map;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel9;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$associateBy$22.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$associateBy$22.L$1 = function14;
                channelsKt__Channels_commonKt$associateBy$22.L$2 = function15;
                channelsKt__Channels_commonKt$associateBy$22.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$associateBy$22.L$4 = map2;
                channelsKt__Channels_commonKt$associateBy$22.L$5 = receiveChannel7;
                channelsKt__Channels_commonKt$associateBy$22.L$6 = receiveChannel2;
                channelsKt__Channels_commonKt$associateBy$22.L$7 = th;
                channelsKt__Channels_commonKt$associateBy$22.L$8 = receiveChannel6;
                channelsKt__Channels_commonKt$associateBy$22.L$9 = channelIterator;
                channelsKt__Channels_commonKt$associateBy$22.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateBy$22);
                if (hasNext != obj2) {
                    return obj2;
                }
                Function1<? super E, ? extends V> function17 = function15;
                receiveChannel5 = receiveChannel7;
                obj3 = hasNext;
                map = map2;
                obj = obj2;
                function13 = function17;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return map;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$associateBy$2 = new ChannelsKt__Channels_commonKt$associateBy$2(continuation);
        Object obj32 = channelsKt__Channels_commonKt$associateBy$2.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$associateBy$2.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$associateBy$22.L$0 = receiveChannel4;
            channelsKt__Channels_commonKt$associateBy$22.L$1 = function14;
            channelsKt__Channels_commonKt$associateBy$22.L$2 = function15;
            channelsKt__Channels_commonKt$associateBy$22.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$associateBy$22.L$4 = map2;
            channelsKt__Channels_commonKt$associateBy$22.L$5 = receiveChannel7;
            channelsKt__Channels_commonKt$associateBy$22.L$6 = receiveChannel2;
            channelsKt__Channels_commonKt$associateBy$22.L$7 = th;
            channelsKt__Channels_commonKt$associateBy$22.L$8 = receiveChannel6;
            channelsKt__Channels_commonKt$associateBy$22.L$9 = channelIterator;
            channelsKt__Channels_commonKt$associateBy$22.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateBy$22);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th6) {
            th = th6;
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0040, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0041, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004a, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object associateBy$$forInline(ReceiveChannel receiveChannel, Function1 function1, Function1 function12, Continuation continuation) {
        Map linkedHashMap = new LinkedHashMap();
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                linkedHashMap.put(function1.invoke(next), function12.invoke(next));
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return linkedHashMap;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009a A[Catch:{ all -> 0x0053 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K, M extends Map<? super K, ? super E>> Object associateByTo(ReceiveChannel<? extends E> receiveChannel, M m, Function1<? super E, ? extends K> function1, Continuation<? super M> continuation) {
        ChannelsKt__Channels_commonKt$associateByTo$1 channelsKt__Channels_commonKt$associateByTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$associateByTo$1 channelsKt__Channels_commonKt$associateByTo$12;
        Function1<? super E, ? extends K> function12;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel5;
        M m2;
        ReceiveChannel<? extends E> receiveChannel6;
        Function1<? super E, ? extends K> function13;
        ChannelsKt__Channels_commonKt$associateByTo$1 channelsKt__Channels_commonKt$associateByTo$13;
        Object obj2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$associateByTo$1) {
            channelsKt__Channels_commonKt$associateByTo$1 = (ChannelsKt__Channels_commonKt$associateByTo$1) continuation;
            if ((channelsKt__Channels_commonKt$associateByTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$associateByTo$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$associateByTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$associateByTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th = null;
                        channelsKt__Channels_commonKt$associateByTo$13 = channelsKt__Channels_commonKt$associateByTo$1;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        receiveChannel6 = receiveChannel2;
                        m2 = m;
                        receiveChannel4 = receiveChannel6;
                        channelIterator = receiveChannel.iterator();
                        function13 = function1;
                        receiveChannel3 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$associateByTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$associateByTo$1.L$6;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$associateByTo$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$associateByTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$associateByTo$1.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$associateByTo$1.L$2;
                    M m3 = (Map) channelsKt__Channels_commonKt$associateByTo$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$associateByTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$associateByTo$1 channelsKt__Channels_commonKt$associateByTo$14 = channelsKt__Channels_commonKt$associateByTo$1;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel4 = receiveChannel9;
                        obj = coroutine_suspended;
                        m2 = m3;
                        channelsKt__Channels_commonKt$associateByTo$12 = channelsKt__Channels_commonKt$associateByTo$14;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        th = th3;
                        receiveChannel3 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            m2.put(function12.invoke(next), next);
                            receiveChannel2 = receiveChannel5;
                            function13 = function12;
                            channelsKt__Channels_commonKt$associateByTo$13 = channelsKt__Channels_commonKt$associateByTo$12;
                            obj2 = obj;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel5, th);
                        InlineMarker.finallyEnd(1);
                        return m2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel5;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$associateByTo$13.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$associateByTo$13.L$1 = m2;
                channelsKt__Channels_commonKt$associateByTo$13.L$2 = function13;
                channelsKt__Channels_commonKt$associateByTo$13.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$associateByTo$13.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$associateByTo$13.L$5 = th;
                channelsKt__Channels_commonKt$associateByTo$13.L$6 = receiveChannel6;
                channelsKt__Channels_commonKt$associateByTo$13.L$7 = channelIterator;
                channelsKt__Channels_commonKt$associateByTo$13.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateByTo$13);
                if (hasNext != obj2) {
                    return obj2;
                }
                Function1<? super E, ? extends K> function14 = function13;
                receiveChannel5 = receiveChannel2;
                obj3 = hasNext;
                obj = obj2;
                channelsKt__Channels_commonKt$associateByTo$12 = channelsKt__Channels_commonKt$associateByTo$13;
                function12 = function14;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel5, th);
                InlineMarker.finallyEnd(1);
                return m2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$associateByTo$1 = new ChannelsKt__Channels_commonKt$associateByTo$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$associateByTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$associateByTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$associateByTo$13.L$0 = receiveChannel4;
            channelsKt__Channels_commonKt$associateByTo$13.L$1 = m2;
            channelsKt__Channels_commonKt$associateByTo$13.L$2 = function13;
            channelsKt__Channels_commonKt$associateByTo$13.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$associateByTo$13.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$associateByTo$13.L$5 = th;
            channelsKt__Channels_commonKt$associateByTo$13.L$6 = receiveChannel6;
            channelsKt__Channels_commonKt$associateByTo$13.L$7 = channelIterator;
            channelsKt__Channels_commonKt$associateByTo$13.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateByTo$13);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th6) {
            th = th6;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0092 A[Catch:{ all -> 0x0057 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f A[Catch:{ all -> 0x0057 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K, V, M extends Map<? super K, ? super V>> Object associateByTo(ReceiveChannel<? extends E> receiveChannel, M m, Function1<? super E, ? extends K> function1, Function1<? super E, ? extends V> function12, Continuation<? super M> continuation) {
        ChannelsKt__Channels_commonKt$associateByTo$3 channelsKt__Channels_commonKt$associateByTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object obj;
        M m2;
        ChannelsKt__Channels_commonKt$associateByTo$3 channelsKt__Channels_commonKt$associateByTo$32;
        Function1<? super E, ? extends V> function13;
        Throwable th2;
        Function1<? super E, ? extends K> function14;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        M m3;
        Object obj2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$associateByTo$3) {
            channelsKt__Channels_commonKt$associateByTo$3 = (ChannelsKt__Channels_commonKt$associateByTo$3) continuation;
            if ((channelsKt__Channels_commonKt$associateByTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$associateByTo$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$associateByTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$associateByTo$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        function13 = function12;
                        channelsKt__Channels_commonKt$associateByTo$32 = channelsKt__Channels_commonKt$associateByTo$3;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        m3 = m;
                        function14 = function1;
                        receiveChannel4 = receiveChannel2;
                        receiveChannel3 = receiveChannel4;
                        channelIterator = receiveChannel.iterator();
                        th2 = null;
                        receiveChannel6 = receiveChannel3;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$associateByTo$3.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$associateByTo$3.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$associateByTo$3.L$6;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$associateByTo$3.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$associateByTo$3.L$4;
                    function13 = (Function1) channelsKt__Channels_commonKt$associateByTo$3.L$3;
                    Function1<? super E, ? extends K> function15 = (Function1) channelsKt__Channels_commonKt$associateByTo$3.L$2;
                    m2 = (Map) channelsKt__Channels_commonKt$associateByTo$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$associateByTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$associateByTo$3 channelsKt__Channels_commonKt$associateByTo$33 = channelsKt__Channels_commonKt$associateByTo$3;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel4 = receiveChannel9;
                        obj = coroutine_suspended;
                        function14 = function15;
                        channelsKt__Channels_commonKt$associateByTo$32 = channelsKt__Channels_commonKt$associateByTo$33;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        th2 = th4;
                        receiveChannel3 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            m2.put(function14.invoke(next), function13.invoke(next));
                            receiveChannel6 = receiveChannel5;
                            m3 = m2;
                            obj2 = obj;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return m2;
                    } catch (Throwable th5) {
                        th = th5;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$associateByTo$32.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$associateByTo$32.L$1 = m3;
                channelsKt__Channels_commonKt$associateByTo$32.L$2 = function14;
                channelsKt__Channels_commonKt$associateByTo$32.L$3 = function13;
                channelsKt__Channels_commonKt$associateByTo$32.L$4 = receiveChannel3;
                channelsKt__Channels_commonKt$associateByTo$32.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$associateByTo$32.L$6 = th2;
                channelsKt__Channels_commonKt$associateByTo$32.L$7 = receiveChannel6;
                channelsKt__Channels_commonKt$associateByTo$32.L$8 = channelIterator;
                channelsKt__Channels_commonKt$associateByTo$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateByTo$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                M m4 = m3;
                receiveChannel5 = receiveChannel6;
                obj3 = hasNext;
                obj = obj2;
                m2 = m4;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return m2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$associateByTo$3 = new ChannelsKt__Channels_commonKt$associateByTo$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$associateByTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$associateByTo$3.label;
        if (i != 0) {
        }
        channelsKt__Channels_commonKt$associateByTo$32.L$0 = receiveChannel4;
        channelsKt__Channels_commonKt$associateByTo$32.L$1 = m3;
        channelsKt__Channels_commonKt$associateByTo$32.L$2 = function14;
        channelsKt__Channels_commonKt$associateByTo$32.L$3 = function13;
        channelsKt__Channels_commonKt$associateByTo$32.L$4 = receiveChannel3;
        channelsKt__Channels_commonKt$associateByTo$32.L$5 = receiveChannel2;
        channelsKt__Channels_commonKt$associateByTo$32.L$6 = th2;
        channelsKt__Channels_commonKt$associateByTo$32.L$7 = receiveChannel6;
        channelsKt__Channels_commonKt$associateByTo$32.L$8 = channelIterator;
        channelsKt__Channels_commonKt$associateByTo$32.label = 1;
        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateByTo$32);
        if (hasNext != obj2) {
        }
        return obj2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009a A[Catch:{ all -> 0x0053 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K, V, M extends Map<? super K, ? super V>> Object associateTo(ReceiveChannel<? extends E> receiveChannel, M m, Function1<? super E, ? extends Pair<? extends K, ? extends V>> function1, Continuation<? super M> continuation) {
        ChannelsKt__Channels_commonKt$associateTo$1 channelsKt__Channels_commonKt$associateTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$associateTo$1 channelsKt__Channels_commonKt$associateTo$12;
        Function1<? super E, ? extends Pair<? extends K, ? extends V>> function12;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel5;
        M m2;
        ReceiveChannel<? extends E> receiveChannel6;
        Function1<? super E, ? extends Pair<? extends K, ? extends V>> function13;
        ChannelsKt__Channels_commonKt$associateTo$1 channelsKt__Channels_commonKt$associateTo$13;
        Object obj2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$associateTo$1) {
            channelsKt__Channels_commonKt$associateTo$1 = (ChannelsKt__Channels_commonKt$associateTo$1) continuation;
            if ((channelsKt__Channels_commonKt$associateTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$associateTo$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$associateTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$associateTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th = null;
                        channelsKt__Channels_commonKt$associateTo$13 = channelsKt__Channels_commonKt$associateTo$1;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        receiveChannel6 = receiveChannel2;
                        m2 = m;
                        receiveChannel4 = receiveChannel6;
                        channelIterator = receiveChannel.iterator();
                        function13 = function1;
                        receiveChannel3 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$associateTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$associateTo$1.L$6;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$associateTo$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$associateTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$associateTo$1.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$associateTo$1.L$2;
                    M m3 = (Map) channelsKt__Channels_commonKt$associateTo$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$associateTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$associateTo$1 channelsKt__Channels_commonKt$associateTo$14 = channelsKt__Channels_commonKt$associateTo$1;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel4 = receiveChannel9;
                        obj = coroutine_suspended;
                        m2 = m3;
                        channelsKt__Channels_commonKt$associateTo$12 = channelsKt__Channels_commonKt$associateTo$14;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        th = th3;
                        receiveChannel3 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Pair pair = (Pair) function12.invoke(channelIterator.next());
                            m2.put(pair.getFirst(), pair.getSecond());
                            receiveChannel2 = receiveChannel5;
                            function13 = function12;
                            channelsKt__Channels_commonKt$associateTo$13 = channelsKt__Channels_commonKt$associateTo$12;
                            obj2 = obj;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel5, th);
                        InlineMarker.finallyEnd(1);
                        return m2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel5;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$associateTo$13.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$associateTo$13.L$1 = m2;
                channelsKt__Channels_commonKt$associateTo$13.L$2 = function13;
                channelsKt__Channels_commonKt$associateTo$13.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$associateTo$13.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$associateTo$13.L$5 = th;
                channelsKt__Channels_commonKt$associateTo$13.L$6 = receiveChannel6;
                channelsKt__Channels_commonKt$associateTo$13.L$7 = channelIterator;
                channelsKt__Channels_commonKt$associateTo$13.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateTo$13);
                if (hasNext != obj2) {
                    return obj2;
                }
                Function1<? super E, ? extends Pair<? extends K, ? extends V>> function14 = function13;
                receiveChannel5 = receiveChannel2;
                obj3 = hasNext;
                obj = obj2;
                channelsKt__Channels_commonKt$associateTo$12 = channelsKt__Channels_commonKt$associateTo$13;
                function12 = function14;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel5, th);
                InlineMarker.finallyEnd(1);
                return m2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$associateTo$1 = new ChannelsKt__Channels_commonKt$associateTo$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$associateTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$associateTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$associateTo$13.L$0 = receiveChannel4;
            channelsKt__Channels_commonKt$associateTo$13.L$1 = m2;
            channelsKt__Channels_commonKt$associateTo$13.L$2 = function13;
            channelsKt__Channels_commonKt$associateTo$13.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$associateTo$13.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$associateTo$13.L$5 = th;
            channelsKt__Channels_commonKt$associateTo$13.L$6 = receiveChannel6;
            channelsKt__Channels_commonKt$associateTo$13.L$7 = channelIterator;
            channelsKt__Channels_commonKt$associateTo$13.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$associateTo$13);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th6) {
            th = th6;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ae A[Catch:{ all -> 0x00ec }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00be A[Catch:{ all -> 0x00ec }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends SendChannel<? super E>> Object toChannel(ReceiveChannel<? extends E> receiveChannel, C c, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$toChannel$1 channelsKt__Channels_commonKt$toChannel$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Object obj;
        C c2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        Throwable th;
        ChannelIterator channelIterator2;
        ChannelsKt__Channels_commonKt$toChannel$1 channelsKt__Channels_commonKt$toChannel$12;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Object obj2;
        C c3;
        ReceiveChannel<? extends E> receiveChannel8;
        if (continuation instanceof ChannelsKt__Channels_commonKt$toChannel$1) {
            channelsKt__Channels_commonKt$toChannel$1 = (ChannelsKt__Channels_commonKt$toChannel$1) continuation;
            if ((channelsKt__Channels_commonKt$toChannel$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$toChannel$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$toChannel$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$toChannel$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        channelsKt__Channels_commonKt$toChannel$12 = channelsKt__Channels_commonKt$toChannel$1;
                        obj2 = coroutine_suspended;
                        channelIterator2 = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        c3 = c;
                        th = null;
                        receiveChannel7 = receiveChannel6;
                        receiveChannel2 = receiveChannel7;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$toChannel$1.L$6;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$toChannel$1.L$5;
                    th = (Throwable) channelsKt__Channels_commonKt$toChannel$1.L$4;
                    receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$toChannel$1.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$toChannel$1.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$toChannel$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$toChannel$1.L$0;
                    ResultKt.throwOnFailure(obj3);
                    ReceiveChannel<? extends E> receiveChannel10 = receiveChannel9;
                    obj = obj3;
                    receiveChannel2 = receiveChannel8;
                    receiveChannel5 = receiveChannel10;
                    if (!((Boolean) obj).booleanValue()) {
                        Object next = channelIterator.next();
                        channelsKt__Channels_commonKt$toChannel$1.L$0 = receiveChannel5;
                        channelsKt__Channels_commonKt$toChannel$1.L$1 = c2;
                        channelsKt__Channels_commonKt$toChannel$1.L$2 = receiveChannel4;
                        channelsKt__Channels_commonKt$toChannel$1.L$3 = receiveChannel2;
                        channelsKt__Channels_commonKt$toChannel$1.L$4 = th;
                        channelsKt__Channels_commonKt$toChannel$1.L$5 = receiveChannel3;
                        channelsKt__Channels_commonKt$toChannel$1.L$6 = channelIterator;
                        channelsKt__Channels_commonKt$toChannel$1.L$7 = next;
                        channelsKt__Channels_commonKt$toChannel$1.L$8 = next;
                        channelsKt__Channels_commonKt$toChannel$1.label = 2;
                        if (c2.send(next, channelsKt__Channels_commonKt$toChannel$1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        C c4 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel5;
                        channelsKt__Channels_commonKt$toChannel$12 = channelsKt__Channels_commonKt$toChannel$1;
                        receiveChannel6 = receiveChannel3;
                        receiveChannel7 = receiveChannel4;
                        obj2 = coroutine_suspended;
                        c3 = c4;
                        return coroutine_suspended;
                    }
                    Unit unit = Unit.INSTANCE;
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    return c2;
                } else if (i == 2) {
                    Object obj4 = channelsKt__Channels_commonKt$toChannel$1.L$8;
                    Object obj5 = channelsKt__Channels_commonKt$toChannel$1.L$7;
                    ChannelIterator channelIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$toChannel$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel11 = (ReceiveChannel) channelsKt__Channels_commonKt$toChannel$1.L$5;
                    th = (Throwable) channelsKt__Channels_commonKt$toChannel$1.L$4;
                    receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$toChannel$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel12 = (ReceiveChannel) channelsKt__Channels_commonKt$toChannel$1.L$2;
                    C c5 = (SendChannel) channelsKt__Channels_commonKt$toChannel$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel13 = (ReceiveChannel) channelsKt__Channels_commonKt$toChannel$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        receiveChannel2 = receiveChannel8;
                        channelsKt__Channels_commonKt$toChannel$12 = channelsKt__Channels_commonKt$toChannel$1;
                        receiveChannel6 = receiveChannel11;
                        receiveChannel7 = receiveChannel12;
                        obj2 = coroutine_suspended;
                        c3 = c5;
                        channelIterator2 = channelIterator3;
                        receiveChannel = receiveChannel13;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th4;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$toChannel$12.L$0 = receiveChannel;
                channelsKt__Channels_commonKt$toChannel$12.L$1 = c3;
                channelsKt__Channels_commonKt$toChannel$12.L$2 = receiveChannel7;
                channelsKt__Channels_commonKt$toChannel$12.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$toChannel$12.L$4 = th;
                channelsKt__Channels_commonKt$toChannel$12.L$5 = receiveChannel6;
                channelsKt__Channels_commonKt$toChannel$12.L$6 = channelIterator2;
                channelsKt__Channels_commonKt$toChannel$12.label = 1;
                obj = channelIterator2.hasNext(channelsKt__Channels_commonKt$toChannel$12);
                if (obj != obj2) {
                    return obj2;
                }
                ChannelsKt__Channels_commonKt$toChannel$1 channelsKt__Channels_commonKt$toChannel$13 = channelsKt__Channels_commonKt$toChannel$12;
                receiveChannel5 = receiveChannel;
                channelIterator = channelIterator2;
                c2 = c3;
                coroutine_suspended = obj2;
                receiveChannel4 = receiveChannel7;
                receiveChannel3 = receiveChannel6;
                channelsKt__Channels_commonKt$toChannel$1 = channelsKt__Channels_commonKt$toChannel$13;
                if (!((Boolean) obj).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                return c2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$toChannel$1 = new ChannelsKt__Channels_commonKt$toChannel$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$toChannel$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$toChannel$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$toChannel$12.L$0 = receiveChannel;
            channelsKt__Channels_commonKt$toChannel$12.L$1 = c3;
            channelsKt__Channels_commonKt$toChannel$12.L$2 = receiveChannel7;
            channelsKt__Channels_commonKt$toChannel$12.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$toChannel$12.L$4 = th;
            channelsKt__Channels_commonKt$toChannel$12.L$5 = receiveChannel6;
            channelsKt__Channels_commonKt$toChannel$12.L$6 = channelIterator2;
            channelsKt__Channels_commonKt$toChannel$12.label = 1;
            obj = channelIterator2.hasNext(channelsKt__Channels_commonKt$toChannel$12);
            if (obj != obj2) {
            }
            return obj2;
        } catch (Throwable th5) {
            th = th5;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085 A[Catch:{ all -> 0x00a3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0092 A[Catch:{ all -> 0x00a3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, C extends Collection<? super E>> Object toCollection(ReceiveChannel<? extends E> receiveChannel, C c, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$toCollection$1 channelsKt__Channels_commonKt$toCollection$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$toCollection$1 channelsKt__Channels_commonKt$toCollection$12;
        C c2;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$toCollection$1) {
            channelsKt__Channels_commonKt$toCollection$1 = (ChannelsKt__Channels_commonKt$toCollection$1) continuation;
            if ((channelsKt__Channels_commonKt$toCollection$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$toCollection$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$toCollection$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$toCollection$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        channelsKt__Channels_commonKt$toCollection$12 = channelsKt__Channels_commonKt$toCollection$1;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        c2 = c;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$toCollection$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$toCollection$1.L$5;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$toCollection$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$toCollection$1.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$toCollection$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$toCollection$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$toCollection$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        th = th4;
                        c2 = c3;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$toCollection$12 = channelsKt__Channels_commonKt$toCollection$1;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            c2.add(channelIterator.next());
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                        }
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        return c2;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$toCollection$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$toCollection$12.L$1 = c2;
                channelsKt__Channels_commonKt$toCollection$12.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$toCollection$12.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$toCollection$12.L$4 = th2;
                channelsKt__Channels_commonKt$toCollection$12.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$toCollection$12.L$6 = channelIterator;
                channelsKt__Channels_commonKt$toCollection$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$toCollection$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                return c2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$toCollection$1 = new ChannelsKt__Channels_commonKt$toCollection$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$toCollection$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$toCollection$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$toCollection$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$toCollection$12.L$1 = c2;
            channelsKt__Channels_commonKt$toCollection$12.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$toCollection$12.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$toCollection$12.L$4 = th2;
            channelsKt__Channels_commonKt$toCollection$12.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$toCollection$12.L$6 = channelIterator;
            channelsKt__Channels_commonKt$toCollection$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$toCollection$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    public static final <E> Object toList(ReceiveChannel<? extends E> receiveChannel, Continuation<? super List<? extends E>> continuation) {
        return ChannelsKt.toMutableList(receiveChannel, continuation);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <K, V> Object toMap(ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel, Continuation<? super Map<K, ? extends V>> continuation) {
        return ChannelsKt.toMap(receiveChannel, new LinkedHashMap(), continuation);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085 A[Catch:{ all -> 0x00ad }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0092 A[Catch:{ all -> 0x00ad }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <K, V, M extends Map<? super K, ? super V>> Object toMap(ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel, M m, Continuation<? super M> continuation) {
        ChannelsKt__Channels_commonKt$toMap$2 channelsKt__Channels_commonKt$toMap$2;
        int i;
        ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel2;
        ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel4;
        ChannelsKt__Channels_commonKt$toMap$2 channelsKt__Channels_commonKt$toMap$22;
        M m2;
        ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel5;
        ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel6;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$toMap$2) {
            channelsKt__Channels_commonKt$toMap$2 = (ChannelsKt__Channels_commonKt$toMap$2) continuation;
            if ((channelsKt__Channels_commonKt$toMap$2.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$toMap$2.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$toMap$2.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$toMap$2.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        channelsKt__Channels_commonKt$toMap$22 = channelsKt__Channels_commonKt$toMap$2;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        m2 = m;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$toMap$2.L$6;
                    ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$toMap$2.L$5;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$toMap$2.L$4;
                    ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$toMap$2.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$toMap$2.L$2;
                    M m3 = (Map) channelsKt__Channels_commonKt$toMap$2.L$1;
                    ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$toMap$2.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        th = th4;
                        m2 = m3;
                        obj = obj4;
                        ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$toMap$22 = channelsKt__Channels_commonKt$toMap$2;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Pair pair = (Pair) channelIterator.next();
                            m2.put(pair.getFirst(), pair.getSecond());
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                        }
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        return m2;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$toMap$22.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$toMap$22.L$1 = m2;
                channelsKt__Channels_commonKt$toMap$22.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$toMap$22.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$toMap$22.L$4 = th2;
                channelsKt__Channels_commonKt$toMap$22.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$toMap$22.L$6 = channelIterator;
                channelsKt__Channels_commonKt$toMap$22.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$toMap$22);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                return m2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$toMap$2 = new ChannelsKt__Channels_commonKt$toMap$2(continuation);
        Object obj32 = channelsKt__Channels_commonKt$toMap$2.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$toMap$2.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$toMap$22.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$toMap$22.L$1 = m2;
            channelsKt__Channels_commonKt$toMap$22.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$toMap$22.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$toMap$22.L$4 = th2;
            channelsKt__Channels_commonKt$toMap$22.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$toMap$22.L$6 = channelIterator;
            channelsKt__Channels_commonKt$toMap$22.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$toMap$22);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object toMutableList(ReceiveChannel<? extends E> receiveChannel, Continuation<? super List<E>> continuation) {
        return ChannelsKt.toCollection(receiveChannel, new ArrayList(), continuation);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object toSet(ReceiveChannel<? extends E> receiveChannel, Continuation<? super Set<? extends E>> continuation) {
        return ChannelsKt.toMutableSet(receiveChannel, continuation);
    }

    public static /* synthetic */ ReceiveChannel flatMap$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.flatMap(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R> ReceiveChannel<R> flatMap(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super ReceiveChannel<? extends R>>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$flatMap");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$flatMap$1(receiveChannel, function2, null), 2, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a3 A[Catch:{ all -> 0x00d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ae A[Catch:{ all -> 0x00d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K> Object groupBy(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, ? extends K> function1, Continuation<? super Map<K, ? extends List<? extends E>>> continuation) {
        ChannelsKt__Channels_commonKt$groupBy$1 channelsKt__Channels_commonKt$groupBy$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        Map map;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$groupBy$1 channelsKt__Channels_commonKt$groupBy$12;
        Function1<? super E, ? extends K> function12;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Object hasNext;
        Continuation<? super Map<K, ? extends List<? extends E>>> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$groupBy$1) {
            channelsKt__Channels_commonKt$groupBy$1 = (ChannelsKt__Channels_commonKt$groupBy$1) continuation2;
            if ((channelsKt__Channels_commonKt$groupBy$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$groupBy$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$groupBy$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$groupBy$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        function12 = function1;
                        map = new LinkedHashMap();
                        channelsKt__Channels_commonKt$groupBy$12 = channelsKt__Channels_commonKt$groupBy$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$groupBy$1.L$8;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$groupBy$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$1.L$4;
                    Map map2 = (Map) channelsKt__Channels_commonKt$groupBy$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$1.L$2;
                    Function1<? super E, ? extends K> function13 = (Function1) channelsKt__Channels_commonKt$groupBy$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        ReceiveChannel<? extends E> receiveChannel11 = receiveChannel8;
                        channelsKt__Channels_commonKt$groupBy$12 = channelsKt__Channels_commonKt$groupBy$1;
                        receiveChannel6 = receiveChannel10;
                        th2 = th4;
                        function12 = function13;
                        map = map2;
                        channelIterator = channelIterator2;
                        receiveChannel2 = receiveChannel11;
                        ReceiveChannel<? extends E> receiveChannel12 = receiveChannel9;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel12;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            Object invoke = function12.invoke(next);
                            Object obj3 = map.get(invoke);
                            if (obj3 == null) {
                                obj3 = new ArrayList();
                                map.put(invoke, obj3);
                            }
                            ((List) obj3).add(next);
                            receiveChannel7 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return map;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$groupBy$12.L$0 = receiveChannel7;
                channelsKt__Channels_commonKt$groupBy$12.L$1 = function12;
                channelsKt__Channels_commonKt$groupBy$12.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$groupBy$12.L$3 = map;
                channelsKt__Channels_commonKt$groupBy$12.L$4 = receiveChannel5;
                channelsKt__Channels_commonKt$groupBy$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$groupBy$12.L$6 = th2;
                channelsKt__Channels_commonKt$groupBy$12.L$7 = receiveChannel4;
                channelsKt__Channels_commonKt$groupBy$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$groupBy$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$groupBy$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj4 = hasNext;
                receiveChannel3 = receiveChannel7;
                obj2 = obj4;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return map;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$groupBy$1 = new ChannelsKt__Channels_commonKt$groupBy$1(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$groupBy$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$groupBy$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$groupBy$12.L$0 = receiveChannel7;
            channelsKt__Channels_commonKt$groupBy$12.L$1 = function12;
            channelsKt__Channels_commonKt$groupBy$12.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$groupBy$12.L$3 = map;
            channelsKt__Channels_commonKt$groupBy$12.L$4 = receiveChannel5;
            channelsKt__Channels_commonKt$groupBy$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$groupBy$12.L$6 = th2;
            channelsKt__Channels_commonKt$groupBy$12.L$7 = receiveChannel4;
            channelsKt__Channels_commonKt$groupBy$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$groupBy$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$groupBy$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004c, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0056, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object groupBy$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Map linkedHashMap = new LinkedHashMap();
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                Object invoke = function1.invoke(next);
                Object obj = linkedHashMap.get(invoke);
                if (obj == null) {
                    obj = new ArrayList();
                    linkedHashMap.put(invoke, obj);
                }
                ((List) obj).add(next);
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return linkedHashMap;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00ab A[Catch:{ all -> 0x00e5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b8 A[Catch:{ all -> 0x00e5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K, V> Object groupBy(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, ? extends K> function1, Function1<? super E, ? extends V> function12, Continuation<? super Map<K, ? extends List<? extends V>>> continuation) {
        ChannelsKt__Channels_commonKt$groupBy$2 channelsKt__Channels_commonKt$groupBy$2;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        Map map;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$groupBy$2 channelsKt__Channels_commonKt$groupBy$22;
        Function1<? super E, ? extends V> function13;
        Function1<? super E, ? extends K> function14;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Object hasNext;
        Continuation<? super Map<K, ? extends List<? extends V>>> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$groupBy$2) {
            channelsKt__Channels_commonKt$groupBy$2 = (ChannelsKt__Channels_commonKt$groupBy$2) continuation2;
            if ((channelsKt__Channels_commonKt$groupBy$2.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$groupBy$2.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$groupBy$2.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$groupBy$2.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        function14 = function1;
                        function13 = function12;
                        map = new LinkedHashMap();
                        channelsKt__Channels_commonKt$groupBy$22 = channelsKt__Channels_commonKt$groupBy$2;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$groupBy$2.L$9;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$2.L$8;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$groupBy$2.L$7;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$2.L$6;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$2.L$5;
                    Map map2 = (Map) channelsKt__Channels_commonKt$groupBy$2.L$4;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$2.L$3;
                    Function1<? super E, ? extends V> function15 = (Function1) channelsKt__Channels_commonKt$groupBy$2.L$2;
                    Function1<? super E, ? extends K> function16 = (Function1) channelsKt__Channels_commonKt$groupBy$2.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$groupBy$2.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        ReceiveChannel<? extends E> receiveChannel11 = receiveChannel9;
                        channelsKt__Channels_commonKt$groupBy$22 = channelsKt__Channels_commonKt$groupBy$2;
                        receiveChannel6 = receiveChannel10;
                        channelIterator = channelIterator2;
                        receiveChannel2 = receiveChannel8;
                        function13 = function15;
                        th2 = th4;
                        function14 = function16;
                        map = map2;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel11;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            Object invoke = function14.invoke(next);
                            Object obj3 = map.get(invoke);
                            if (obj3 == null) {
                                obj3 = new ArrayList();
                                map.put(invoke, obj3);
                            }
                            ((List) obj3).add(function13.invoke(next));
                            receiveChannel7 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return map;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$groupBy$22.L$0 = receiveChannel7;
                channelsKt__Channels_commonKt$groupBy$22.L$1 = function14;
                channelsKt__Channels_commonKt$groupBy$22.L$2 = function13;
                channelsKt__Channels_commonKt$groupBy$22.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$groupBy$22.L$4 = map;
                channelsKt__Channels_commonKt$groupBy$22.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$groupBy$22.L$6 = receiveChannel2;
                channelsKt__Channels_commonKt$groupBy$22.L$7 = th2;
                channelsKt__Channels_commonKt$groupBy$22.L$8 = receiveChannel4;
                channelsKt__Channels_commonKt$groupBy$22.L$9 = channelIterator;
                channelsKt__Channels_commonKt$groupBy$22.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$groupBy$22);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj4 = hasNext;
                receiveChannel3 = receiveChannel7;
                obj2 = obj4;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return map;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$groupBy$2 = new ChannelsKt__Channels_commonKt$groupBy$2(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$groupBy$2.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$groupBy$2.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$groupBy$22.L$0 = receiveChannel7;
            channelsKt__Channels_commonKt$groupBy$22.L$1 = function14;
            channelsKt__Channels_commonKt$groupBy$22.L$2 = function13;
            channelsKt__Channels_commonKt$groupBy$22.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$groupBy$22.L$4 = map;
            channelsKt__Channels_commonKt$groupBy$22.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$groupBy$22.L$6 = receiveChannel2;
            channelsKt__Channels_commonKt$groupBy$22.L$7 = th2;
            channelsKt__Channels_commonKt$groupBy$22.L$8 = receiveChannel4;
            channelsKt__Channels_commonKt$groupBy$22.L$9 = channelIterator;
            channelsKt__Channels_commonKt$groupBy$22.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$groupBy$22);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0050, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0051, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005a, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object groupBy$$forInline(ReceiveChannel receiveChannel, Function1 function1, Function1 function12, Continuation continuation) {
        Map linkedHashMap = new LinkedHashMap();
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                Object invoke = function1.invoke(next);
                Object obj = linkedHashMap.get(invoke);
                if (obj == null) {
                    obj = new ArrayList();
                    linkedHashMap.put(invoke, obj);
                }
                ((List) obj).add(function12.invoke(next));
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return linkedHashMap;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0099 A[Catch:{ all -> 0x0053 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K, M extends Map<? super K, List<E>>> Object groupByTo(ReceiveChannel<? extends E> receiveChannel, M m, Function1<? super E, ? extends K> function1, Continuation<? super M> continuation) {
        ChannelsKt__Channels_commonKt$groupByTo$1 channelsKt__Channels_commonKt$groupByTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$groupByTo$1 channelsKt__Channels_commonKt$groupByTo$12;
        Function1<? super E, ? extends K> function12;
        Throwable th;
        M m2;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Function1<? super E, ? extends K> function13;
        ChannelsKt__Channels_commonKt$groupByTo$1 channelsKt__Channels_commonKt$groupByTo$13;
        Object obj2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$groupByTo$1) {
            channelsKt__Channels_commonKt$groupByTo$1 = (ChannelsKt__Channels_commonKt$groupByTo$1) continuation;
            if ((channelsKt__Channels_commonKt$groupByTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$groupByTo$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$groupByTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$groupByTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th = null;
                        channelsKt__Channels_commonKt$groupByTo$13 = channelsKt__Channels_commonKt$groupByTo$1;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        m2 = m;
                        receiveChannel4 = receiveChannel5;
                        channelIterator = receiveChannel.iterator();
                        function13 = function1;
                        receiveChannel3 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$groupByTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$groupByTo$1.L$6;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$groupByTo$1.L$5;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$groupByTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$groupByTo$1.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$groupByTo$1.L$2;
                    M m3 = (Map) channelsKt__Channels_commonKt$groupByTo$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$groupByTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$groupByTo$1 channelsKt__Channels_commonKt$groupByTo$14 = channelsKt__Channels_commonKt$groupByTo$1;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel4 = receiveChannel9;
                        obj = coroutine_suspended;
                        m2 = m3;
                        channelsKt__Channels_commonKt$groupByTo$12 = channelsKt__Channels_commonKt$groupByTo$14;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        th = th3;
                        receiveChannel3 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            Object invoke = function12.invoke(next);
                            Object obj4 = m2.get(invoke);
                            if (obj4 == null) {
                                obj4 = new ArrayList();
                                m2.put(invoke, obj4);
                            }
                            ((List) obj4).add(next);
                            receiveChannel6 = receiveChannel2;
                            function13 = function12;
                            channelsKt__Channels_commonKt$groupByTo$13 = channelsKt__Channels_commonKt$groupByTo$12;
                            obj2 = obj;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return m2;
                    } catch (Throwable th4) {
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$groupByTo$13.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$groupByTo$13.L$1 = m2;
                channelsKt__Channels_commonKt$groupByTo$13.L$2 = function13;
                channelsKt__Channels_commonKt$groupByTo$13.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$groupByTo$13.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$groupByTo$13.L$5 = th;
                channelsKt__Channels_commonKt$groupByTo$13.L$6 = receiveChannel5;
                channelsKt__Channels_commonKt$groupByTo$13.L$7 = channelIterator;
                channelsKt__Channels_commonKt$groupByTo$13.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$groupByTo$13);
                if (hasNext != obj2) {
                    return obj2;
                }
                Function1<? super E, ? extends K> function14 = function13;
                receiveChannel2 = receiveChannel6;
                obj3 = hasNext;
                obj = obj2;
                channelsKt__Channels_commonKt$groupByTo$12 = channelsKt__Channels_commonKt$groupByTo$13;
                function12 = function14;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return m2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$groupByTo$1 = new ChannelsKt__Channels_commonKt$groupByTo$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$groupByTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$groupByTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$groupByTo$13.L$0 = receiveChannel4;
            channelsKt__Channels_commonKt$groupByTo$13.L$1 = m2;
            channelsKt__Channels_commonKt$groupByTo$13.L$2 = function13;
            channelsKt__Channels_commonKt$groupByTo$13.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$groupByTo$13.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$groupByTo$13.L$5 = th;
            channelsKt__Channels_commonKt$groupByTo$13.L$6 = receiveChannel5;
            channelsKt__Channels_commonKt$groupByTo$13.L$7 = channelIterator;
            channelsKt__Channels_commonKt$groupByTo$13.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$groupByTo$13);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th6) {
            th = th6;
            receiveChannel2 = receiveChannel6;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a2 A[Catch:{ all -> 0x00da }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ad A[Catch:{ all -> 0x00da }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K, V, M extends Map<? super K, List<V>>> Object groupByTo(ReceiveChannel<? extends E> receiveChannel, M m, Function1<? super E, ? extends K> function1, Function1<? super E, ? extends V> function12, Continuation<? super M> continuation) {
        ChannelsKt__Channels_commonKt$groupByTo$3 channelsKt__Channels_commonKt$groupByTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$groupByTo$3 channelsKt__Channels_commonKt$groupByTo$32;
        Function1<? super E, ? extends V> function13;
        Function1<? super E, ? extends K> function14;
        M m2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object hasNext;
        Continuation<? super M> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$groupByTo$3) {
            channelsKt__Channels_commonKt$groupByTo$3 = (ChannelsKt__Channels_commonKt$groupByTo$3) continuation2;
            if ((channelsKt__Channels_commonKt$groupByTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$groupByTo$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$groupByTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$groupByTo$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        m2 = m;
                        function14 = function1;
                        function13 = function12;
                        th2 = null;
                        channelsKt__Channels_commonKt$groupByTo$32 = channelsKt__Channels_commonKt$groupByTo$3;
                        obj = coroutine_suspended;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$groupByTo$3.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$groupByTo$3.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$groupByTo$3.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$groupByTo$3.L$5;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$groupByTo$3.L$4;
                    Function1<? super E, ? extends V> function15 = (Function1) channelsKt__Channels_commonKt$groupByTo$3.L$3;
                    Function1<? super E, ? extends K> function16 = (Function1) channelsKt__Channels_commonKt$groupByTo$3.L$2;
                    M m3 = (Map) channelsKt__Channels_commonKt$groupByTo$3.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$groupByTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel9;
                        channelsKt__Channels_commonKt$groupByTo$32 = channelsKt__Channels_commonKt$groupByTo$3;
                        receiveChannel5 = receiveChannel10;
                        Function1<? super E, ? extends V> function17 = function15;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel8;
                        function13 = function17;
                        Function1<? super E, ? extends K> function18 = function16;
                        channelIterator = channelIterator2;
                        receiveChannel4 = receiveChannel7;
                        m2 = m3;
                        th2 = th4;
                        function14 = function18;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            Object invoke = function14.invoke(next);
                            Object obj3 = m2.get(invoke);
                            if (obj3 == null) {
                                obj3 = new ArrayList();
                                m2.put(invoke, obj3);
                            }
                            ((List) obj3).add(function13.invoke(next));
                            receiveChannel6 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return m2;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$groupByTo$32.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$groupByTo$32.L$1 = m2;
                channelsKt__Channels_commonKt$groupByTo$32.L$2 = function14;
                channelsKt__Channels_commonKt$groupByTo$32.L$3 = function13;
                channelsKt__Channels_commonKt$groupByTo$32.L$4 = receiveChannel5;
                channelsKt__Channels_commonKt$groupByTo$32.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$groupByTo$32.L$6 = th2;
                channelsKt__Channels_commonKt$groupByTo$32.L$7 = receiveChannel4;
                channelsKt__Channels_commonKt$groupByTo$32.L$8 = channelIterator;
                channelsKt__Channels_commonKt$groupByTo$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$groupByTo$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj4 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj2 = obj4;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return m2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$groupByTo$3 = new ChannelsKt__Channels_commonKt$groupByTo$3(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$groupByTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$groupByTo$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$groupByTo$32.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$groupByTo$32.L$1 = m2;
            channelsKt__Channels_commonKt$groupByTo$32.L$2 = function14;
            channelsKt__Channels_commonKt$groupByTo$32.L$3 = function13;
            channelsKt__Channels_commonKt$groupByTo$32.L$4 = receiveChannel5;
            channelsKt__Channels_commonKt$groupByTo$32.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$groupByTo$32.L$6 = th2;
            channelsKt__Channels_commonKt$groupByTo$32.L$7 = receiveChannel4;
            channelsKt__Channels_commonKt$groupByTo$32.L$8 = channelIterator;
            channelsKt__Channels_commonKt$groupByTo$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$groupByTo$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    public static /* synthetic */ ReceiveChannel map$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.map(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R> ReceiveChannel<R> map(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$map");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$map$1(receiveChannel, function2, null), 2, null);
    }

    public static /* synthetic */ ReceiveChannel mapIndexed$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.mapIndexed(receiveChannel, coroutineContext, function3);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R> ReceiveChannel<R> mapIndexed(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function3<? super Integer, ? super E, ? super Continuation<? super R>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$mapIndexed");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "transform");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$mapIndexed$1(receiveChannel, function3, null), 2, null);
    }

    public static /* synthetic */ ReceiveChannel mapIndexedNotNull$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.mapIndexedNotNull(receiveChannel, coroutineContext, function3);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R> ReceiveChannel<R> mapIndexedNotNull(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function3<? super Integer, ? super E, ? super Continuation<? super R>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$mapIndexedNotNull");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "transform");
        return ChannelsKt.filterNotNull(ChannelsKt.mapIndexed(receiveChannel, coroutineContext, function3));
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00ac A[Catch:{ all -> 0x00f1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b9 A[Catch:{ all -> 0x00f1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, C extends Collection<? super R>> Object mapIndexedNotNullTo(ReceiveChannel<? extends E> receiveChannel, C c, Function2<? super Integer, ? super E, ? extends R> function2, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$1 channelsKt__Channels_commonKt$mapIndexedNotNullTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        IntRef intRef;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$1 channelsKt__Channels_commonKt$mapIndexedNotNullTo$12;
        Function2<? super Integer, ? super E, ? extends R> function22;
        C c2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        Object hasNext;
        Continuation<? super C> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$1) {
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$1 = (ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$1) continuation2;
            if ((channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.label;
                int i2 = 1;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef2 = new IntRef();
                    intRef2.element = 0;
                    try {
                        c2 = c;
                        function22 = function2;
                        intRef = intRef2;
                        channelsKt__Channels_commonKt$mapIndexedNotNullTo$12 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$9;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$8;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$5;
                    IntRef intRef3 = (IntRef) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$3;
                    Function2<? super Integer, ? super E, ? extends R> function23 = (Function2) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        ReceiveChannel<? extends E> receiveChannel11 = receiveChannel9;
                        channelsKt__Channels_commonKt$mapIndexedNotNullTo$12 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$1;
                        receiveChannel6 = receiveChannel10;
                        channelIterator = channelIterator2;
                        receiveChannel2 = receiveChannel8;
                        function22 = function23;
                        th2 = th4;
                        c2 = c3;
                        intRef = intRef3;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel11;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            int i3 = intRef.element;
                            intRef.element = i3 + 1;
                            IndexedValue indexedValue = new IndexedValue(i3, next);
                            int component1 = indexedValue.component1();
                            Object invoke = function22.invoke(Boxing.boxInt(component1), indexedValue.component2());
                            if (invoke != null) {
                                Boxing.boxBoolean(c2.add(invoke));
                            }
                            receiveChannel7 = receiveChannel3;
                            i2 = 1;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return c2;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$0 = receiveChannel7;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$1 = c2;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$2 = function22;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$4 = intRef;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$6 = receiveChannel2;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$7 = th2;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$8 = receiveChannel4;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$9 = channelIterator;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.label = i2;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexedNotNullTo$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel7;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$mapIndexedNotNullTo$1 = new ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$1(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$mapIndexedNotNullTo$1.label;
        int i22 = 1;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$0 = receiveChannel7;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$1 = c2;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$2 = function22;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$4 = intRef;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$6 = receiveChannel2;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$7 = th2;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$8 = receiveChannel4;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.L$9 = channelIterator;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$12.label = i22;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexedNotNullTo$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00e5 A[Catch:{ all -> 0x0187 }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0100 A[Catch:{ all -> 0x0187 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0027  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, C extends SendChannel<? super R>> Object mapIndexedNotNullTo(ReceiveChannel<? extends E> receiveChannel, C c, Function2<? super Integer, ? super E, ? extends R> function2, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$3 channelsKt__Channels_commonKt$mapIndexedNotNullTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th2;
        Object obj;
        ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$3 channelsKt__Channels_commonKt$mapIndexedNotNullTo$32;
        IntRef intRef;
        Function2<? super Integer, ? super E, ? extends R> function22;
        C c2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        char c3;
        ReceiveChannel<? extends E> receiveChannel7;
        C c4;
        Function2<? super Integer, ? super E, ? extends R> function23;
        ReceiveChannel<? extends E> receiveChannel8;
        IntRef intRef2;
        ReceiveChannel<? extends E> receiveChannel9;
        ReceiveChannel<? extends E> receiveChannel10;
        Throwable th3;
        ChannelIterator channelIterator2;
        C c5;
        Object hasNext;
        Continuation<? super C> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$3) {
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3 = (ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$3) continuation2;
            if ((channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.label;
                int i2 = 1;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef3 = new IntRef();
                    intRef3.element = 0;
                    try {
                        c2 = c;
                        function22 = function2;
                        intRef = intRef3;
                        channelsKt__Channels_commonKt$mapIndexedNotNullTo$32 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel7 = receiveChannel;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$9;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$8;
                    th3 = (Throwable) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$7;
                    receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$6;
                    receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$5;
                    intRef2 = (IntRef) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$4;
                    receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$3;
                    function23 = (Function2) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$2;
                    C c6 = (SendChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$0;
                    ResultKt.throwOnFailure(obj2);
                    C c7 = c6;
                    channelIterator = channelIterator3;
                    receiveChannel2 = receiveChannel10;
                    c5 = c7;
                    if (!((Boolean) obj2).booleanValue()) {
                        Object next = channelIterator.next();
                        int i3 = intRef2.element;
                        intRef2.element = i3 + 1;
                        IndexedValue indexedValue = new IndexedValue(i3, next);
                        int component1 = indexedValue.component1();
                        Object component2 = indexedValue.component2();
                        Object obj3 = coroutine_suspended;
                        Object invoke = function23.invoke(Boxing.boxInt(component1), component2);
                        if (invoke != null) {
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$0 = receiveChannel3;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$1 = c5;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$2 = function23;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$3 = receiveChannel8;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$4 = intRef2;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$5 = receiveChannel9;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$6 = receiveChannel2;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$7 = th3;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$8 = receiveChannel4;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$9 = channelIterator;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$10 = next;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$11 = next;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$12 = indexedValue;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.I$0 = component1;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$13 = component2;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$14 = invoke;
                            c3 = 2;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.label = 2;
                            Object obj4 = obj3;
                            if (c5.send(invoke, channelsKt__Channels_commonKt$mapIndexedNotNullTo$3) == obj4) {
                                return obj4;
                            }
                            coroutine_suspended = obj4;
                            C c8 = c5;
                            receiveChannel10 = receiveChannel2;
                            channelIterator2 = channelIterator;
                            c4 = c8;
                            IntRef intRef4 = intRef2;
                            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3;
                            receiveChannel6 = receiveChannel8;
                            obj = coroutine_suspended;
                            receiveChannel5 = receiveChannel9;
                            intRef = intRef4;
                            C c9 = c4;
                            channelIterator = channelIterator2;
                            receiveChannel2 = receiveChannel10;
                            function22 = function23;
                            th2 = th3;
                            c2 = c9;
                            char c10 = c3;
                            receiveChannel7 = receiveChannel3;
                            i2 = 1;
                            return obj4;
                        }
                        c3 = 2;
                        receiveChannel5 = receiveChannel9;
                        intRef = intRef2;
                        channelsKt__Channels_commonKt$mapIndexedNotNullTo$32 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3;
                        receiveChannel6 = receiveChannel8;
                        obj = obj3;
                        Function2<? super Integer, ? super E, ? extends R> function24 = function23;
                        th2 = th3;
                        c2 = c5;
                        function22 = function24;
                        char c102 = c3;
                        receiveChannel7 = receiveChannel3;
                        i2 = 1;
                    }
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel2, th3);
                    InlineMarker.finallyEnd(1);
                    return c5;
                } else if (i == 2) {
                    Object obj5 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$14;
                    Object obj6 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$13;
                    int i4 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.I$0;
                    IndexedValue indexedValue2 = (IndexedValue) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$12;
                    Object obj7 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$11;
                    Object obj8 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$10;
                    channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$9;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$8;
                    th3 = (Throwable) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$7;
                    receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$6;
                    receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$5;
                    intRef2 = (IntRef) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$4;
                    receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$3;
                    function23 = (Function2) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$2;
                    c4 = (SendChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        c3 = 2;
                        IntRef intRef42 = intRef2;
                        channelsKt__Channels_commonKt$mapIndexedNotNullTo$32 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3;
                        receiveChannel6 = receiveChannel8;
                        obj = coroutine_suspended;
                        receiveChannel5 = receiveChannel9;
                        intRef = intRef42;
                        C c92 = c4;
                        channelIterator = channelIterator2;
                        receiveChannel2 = receiveChannel10;
                        function22 = function23;
                        th2 = th3;
                        c2 = c92;
                        char c1022 = c3;
                        receiveChannel7 = receiveChannel3;
                        i2 = 1;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel10;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$0 = receiveChannel7;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$1 = c2;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$2 = function22;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$4 = intRef;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$6 = receiveChannel2;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$7 = th2;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$8 = receiveChannel4;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$9 = channelIterator;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.label = i2;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexedNotNullTo$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj9 = hasNext;
                receiveChannel3 = receiveChannel7;
                obj2 = obj9;
                Object obj10 = obj;
                receiveChannel8 = receiveChannel6;
                channelsKt__Channels_commonKt$mapIndexedNotNullTo$3 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$32;
                intRef2 = intRef;
                receiveChannel9 = receiveChannel5;
                coroutine_suspended = obj10;
                Function2<? super Integer, ? super E, ? extends R> function25 = function22;
                c5 = c2;
                th3 = th2;
                function23 = function25;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th3);
                InlineMarker.finallyEnd(1);
                return c5;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$mapIndexedNotNullTo$3 = new ChannelsKt__Channels_commonKt$mapIndexedNotNullTo$3(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$mapIndexedNotNullTo$3.label;
        int i22 = 1;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$0 = receiveChannel7;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$1 = c2;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$2 = function22;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$4 = intRef;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$6 = receiveChannel2;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$7 = th2;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$8 = receiveChannel4;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.L$9 = channelIterator;
            channelsKt__Channels_commonKt$mapIndexedNotNullTo$32.label = i22;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexedNotNullTo$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a9 A[Catch:{ all -> 0x00d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b4 A[Catch:{ all -> 0x00d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, C extends Collection<? super R>> Object mapIndexedTo(ReceiveChannel<? extends E> receiveChannel, C c, Function2<? super Integer, ? super E, ? extends R> function2, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$mapIndexedTo$1 channelsKt__Channels_commonKt$mapIndexedTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        IntRef intRef;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$mapIndexedTo$1 channelsKt__Channels_commonKt$mapIndexedTo$12;
        Function2<? super Integer, ? super E, ? extends R> function22;
        C c2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object hasNext;
        Continuation<? super C> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$mapIndexedTo$1) {
            channelsKt__Channels_commonKt$mapIndexedTo$1 = (ChannelsKt__Channels_commonKt$mapIndexedTo$1) continuation2;
            if ((channelsKt__Channels_commonKt$mapIndexedTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$mapIndexedTo$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$mapIndexedTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$mapIndexedTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef2 = new IntRef();
                    intRef2.element = 0;
                    try {
                        function22 = function2;
                        intRef = intRef2;
                        channelsKt__Channels_commonKt$mapIndexedTo$12 = channelsKt__Channels_commonKt$mapIndexedTo$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                        c2 = c;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$mapIndexedTo$1.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$mapIndexedTo$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$1.L$4;
                    IntRef intRef3 = (IntRef) channelsKt__Channels_commonKt$mapIndexedTo$1.L$3;
                    Function2<? super Integer, ? super E, ? extends R> function23 = (Function2) channelsKt__Channels_commonKt$mapIndexedTo$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$mapIndexedTo$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$mapIndexedTo$12 = channelsKt__Channels_commonKt$mapIndexedTo$1;
                        receiveChannel5 = receiveChannel9;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel10;
                        IntRef intRef4 = intRef3;
                        channelIterator = channelIterator2;
                        receiveChannel4 = receiveChannel7;
                        c2 = c3;
                        intRef = intRef4;
                        Function2<? super Integer, ? super E, ? extends R> function24 = function23;
                        th2 = th4;
                        function22 = function24;
                        if (!((Boolean) obj2).booleanValue()) {
                            Object next = channelIterator.next();
                            int i2 = intRef.element;
                            intRef.element = i2 + 1;
                            c2.add(function22.invoke(Boxing.boxInt(i2), next));
                            receiveChannel6 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return c2;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$1 = c2;
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$2 = function22;
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$3 = intRef;
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$4 = receiveChannel5;
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$6 = th2;
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$7 = receiveChannel4;
                channelsKt__Channels_commonKt$mapIndexedTo$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$mapIndexedTo$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexedTo$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$mapIndexedTo$1 = new ChannelsKt__Channels_commonKt$mapIndexedTo$1(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$mapIndexedTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$mapIndexedTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$1 = c2;
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$2 = function22;
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$3 = intRef;
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$4 = receiveChannel5;
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$6 = th2;
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$7 = receiveChannel4;
            channelsKt__Channels_commonKt$mapIndexedTo$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$mapIndexedTo$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapIndexedTo$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00e5 A[Catch:{ all -> 0x0151 }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0102 A[Catch:{ all -> 0x0151 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0027  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, C extends SendChannel<? super R>> Object mapIndexedTo(ReceiveChannel<? extends E> receiveChannel, C c, Function2<? super Integer, ? super E, ? extends R> function2, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$mapIndexedTo$3 channelsKt__Channels_commonKt$mapIndexedTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        C c2;
        Function2<? super Integer, ? super E, ? extends R> function22;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        IntRef intRef;
        Throwable th2;
        ReceiveChannel<? extends E> receiveChannel5;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel6;
        ChannelsKt__Channels_commonKt$mapIndexedTo$3 channelsKt__Channels_commonKt$mapIndexedTo$32;
        ReceiveChannel<? extends E> receiveChannel7;
        ChannelIterator channelIterator2;
        ReceiveChannel<? extends E> receiveChannel8;
        C c3;
        Throwable th3;
        Function2<? super Integer, ? super E, ? extends R> function23;
        Object hasNext;
        ReceiveChannel<? extends E> receiveChannel9;
        Continuation<? super C> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$mapIndexedTo$3) {
            channelsKt__Channels_commonKt$mapIndexedTo$3 = (ChannelsKt__Channels_commonKt$mapIndexedTo$3) continuation2;
            if ((channelsKt__Channels_commonKt$mapIndexedTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$mapIndexedTo$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$mapIndexedTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$mapIndexedTo$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef2 = new IntRef();
                    intRef2.element = 0;
                    try {
                        function23 = function2;
                        intRef = intRef2;
                        channelsKt__Channels_commonKt$mapIndexedTo$32 = channelsKt__Channels_commonKt$mapIndexedTo$3;
                        obj = coroutine_suspended;
                        th3 = null;
                        channelIterator2 = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        receiveChannel7 = receiveChannel6;
                        receiveChannel2 = receiveChannel7;
                        receiveChannel8 = receiveChannel2;
                        c3 = c;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$mapIndexedTo$3.L$8;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$7;
                    th2 = (Throwable) channelsKt__Channels_commonKt$mapIndexedTo$3.L$6;
                    receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$4;
                    IntRef intRef3 = (IntRef) channelsKt__Channels_commonKt$mapIndexedTo$3.L$3;
                    function22 = (Function2) channelsKt__Channels_commonKt$mapIndexedTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$0;
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef4 = intRef3;
                    obj = coroutine_suspended;
                    receiveChannel2 = receiveChannel9;
                    intRef = intRef4;
                    if (!((Boolean) obj2).booleanValue()) {
                        Object next = channelIterator.next();
                        int i2 = intRef.element;
                        intRef.element = i2 + 1;
                        Object invoke = function22.invoke(Boxing.boxInt(i2), next);
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$0 = receiveChannel3;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$1 = c2;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$2 = function22;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$3 = intRef;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$4 = receiveChannel4;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$5 = receiveChannel2;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$6 = th2;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$7 = receiveChannel5;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$8 = channelIterator;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$9 = next;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.L$10 = next;
                        channelsKt__Channels_commonKt$mapIndexedTo$3.label = 2;
                        if (c2.send(invoke, channelsKt__Channels_commonKt$mapIndexedTo$3) == obj) {
                            return obj;
                        }
                        receiveChannel6 = receiveChannel3;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel4;
                        channelsKt__Channels_commonKt$mapIndexedTo$32 = channelsKt__Channels_commonKt$mapIndexedTo$3;
                        receiveChannel7 = receiveChannel10;
                        C c4 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel8 = receiveChannel5;
                        c3 = c4;
                        Function2<? super Integer, ? super E, ? extends R> function24 = function22;
                        th3 = th2;
                        function23 = function24;
                        return obj;
                    }
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    InlineMarker.finallyEnd(1);
                    return c2;
                } else if (i == 2) {
                    Object obj3 = channelsKt__Channels_commonKt$mapIndexedTo$3.L$10;
                    Object obj4 = channelsKt__Channels_commonKt$mapIndexedTo$3.L$9;
                    ChannelIterator channelIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$mapIndexedTo$3.L$8;
                    ReceiveChannel<? extends E> receiveChannel11 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$7;
                    Throwable th5 = (Throwable) channelsKt__Channels_commonKt$mapIndexedTo$3.L$6;
                    receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$5;
                    ReceiveChannel<? extends E> receiveChannel12 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$4;
                    IntRef intRef5 = (IntRef) channelsKt__Channels_commonKt$mapIndexedTo$3.L$3;
                    Function2<? super Integer, ? super E, ? extends R> function25 = (Function2) channelsKt__Channels_commonKt$mapIndexedTo$3.L$2;
                    C c5 = (SendChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel13 = (ReceiveChannel) channelsKt__Channels_commonKt$mapIndexedTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        receiveChannel6 = receiveChannel13;
                        ReceiveChannel<? extends E> receiveChannel14 = receiveChannel12;
                        channelsKt__Channels_commonKt$mapIndexedTo$32 = channelsKt__Channels_commonKt$mapIndexedTo$3;
                        receiveChannel7 = receiveChannel14;
                        IntRef intRef6 = intRef5;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel9;
                        intRef = intRef6;
                        C c6 = c5;
                        channelIterator2 = channelIterator3;
                        receiveChannel8 = receiveChannel11;
                        c3 = c6;
                        Function2<? super Integer, ? super E, ? extends R> function26 = function25;
                        th3 = th5;
                        function23 = function26;
                    } catch (Throwable th6) {
                        th = th6;
                        receiveChannel2 = receiveChannel9;
                        try {
                            throw th;
                        } catch (Throwable th7) {
                            Throwable th8 = th7;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th8;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$1 = c3;
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$2 = function23;
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$3 = intRef;
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$4 = receiveChannel7;
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$6 = th3;
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$7 = receiveChannel8;
                channelsKt__Channels_commonKt$mapIndexedTo$32.L$8 = channelIterator2;
                channelsKt__Channels_commonKt$mapIndexedTo$32.label = 1;
                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapIndexedTo$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj5 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj2 = obj5;
                ChannelsKt__Channels_commonKt$mapIndexedTo$3 channelsKt__Channels_commonKt$mapIndexedTo$33 = channelsKt__Channels_commonKt$mapIndexedTo$32;
                receiveChannel4 = receiveChannel7;
                channelsKt__Channels_commonKt$mapIndexedTo$3 = channelsKt__Channels_commonKt$mapIndexedTo$33;
                C c7 = c3;
                receiveChannel5 = receiveChannel8;
                channelIterator = channelIterator2;
                c2 = c7;
                Throwable th9 = th3;
                function22 = function23;
                th2 = th9;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$mapIndexedTo$3 = new ChannelsKt__Channels_commonKt$mapIndexedTo$3(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$mapIndexedTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$mapIndexedTo$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$1 = c3;
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$2 = function23;
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$3 = intRef;
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$4 = receiveChannel7;
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$6 = th3;
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$7 = receiveChannel8;
            channelsKt__Channels_commonKt$mapIndexedTo$32.L$8 = channelIterator2;
            channelsKt__Channels_commonKt$mapIndexedTo$32.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapIndexedTo$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th10) {
            th = th10;
            th = th;
            throw th;
        }
    }

    public static /* synthetic */ ReceiveChannel mapNotNull$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.mapNotNull(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R> ReceiveChannel<R> mapNotNull(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$mapNotNull");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        return ChannelsKt.filterNotNull(ChannelsKt.map(receiveChannel, coroutineContext, function2));
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009a A[Catch:{ all -> 0x0053 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, C extends Collection<? super R>> Object mapNotNullTo(ReceiveChannel<? extends E> receiveChannel, C c, Function1<? super E, ? extends R> function1, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$mapNotNullTo$1 channelsKt__Channels_commonKt$mapNotNullTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$mapNotNullTo$1 channelsKt__Channels_commonKt$mapNotNullTo$12;
        Function1<? super E, ? extends R> function12;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel5;
        C c2;
        ReceiveChannel<? extends E> receiveChannel6;
        Function1<? super E, ? extends R> function13;
        ChannelsKt__Channels_commonKt$mapNotNullTo$1 channelsKt__Channels_commonKt$mapNotNullTo$13;
        Object obj2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$mapNotNullTo$1) {
            channelsKt__Channels_commonKt$mapNotNullTo$1 = (ChannelsKt__Channels_commonKt$mapNotNullTo$1) continuation;
            if ((channelsKt__Channels_commonKt$mapNotNullTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$mapNotNullTo$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$mapNotNullTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$mapNotNullTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th = null;
                        channelsKt__Channels_commonKt$mapNotNullTo$13 = channelsKt__Channels_commonKt$mapNotNullTo$1;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        receiveChannel6 = receiveChannel2;
                        c2 = c;
                        receiveChannel4 = receiveChannel6;
                        channelIterator = receiveChannel.iterator();
                        function13 = function1;
                        receiveChannel3 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$mapNotNullTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$1.L$6;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$mapNotNullTo$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$1.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$mapNotNullTo$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$mapNotNullTo$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$mapNotNullTo$1 channelsKt__Channels_commonKt$mapNotNullTo$14 = channelsKt__Channels_commonKt$mapNotNullTo$1;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel4 = receiveChannel9;
                        obj = coroutine_suspended;
                        c2 = c3;
                        channelsKt__Channels_commonKt$mapNotNullTo$12 = channelsKt__Channels_commonKt$mapNotNullTo$14;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        th = th3;
                        receiveChannel3 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object invoke = function12.invoke(channelIterator.next());
                            if (invoke != null) {
                                Boxing.boxBoolean(c2.add(invoke));
                            }
                            receiveChannel2 = receiveChannel5;
                            function13 = function12;
                            channelsKt__Channels_commonKt$mapNotNullTo$13 = channelsKt__Channels_commonKt$mapNotNullTo$12;
                            obj2 = obj;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel5, th);
                        InlineMarker.finallyEnd(1);
                        return c2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel5;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$mapNotNullTo$13.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$mapNotNullTo$13.L$1 = c2;
                channelsKt__Channels_commonKt$mapNotNullTo$13.L$2 = function13;
                channelsKt__Channels_commonKt$mapNotNullTo$13.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$mapNotNullTo$13.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$mapNotNullTo$13.L$5 = th;
                channelsKt__Channels_commonKt$mapNotNullTo$13.L$6 = receiveChannel6;
                channelsKt__Channels_commonKt$mapNotNullTo$13.L$7 = channelIterator;
                channelsKt__Channels_commonKt$mapNotNullTo$13.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapNotNullTo$13);
                if (hasNext != obj2) {
                    return obj2;
                }
                Function1<? super E, ? extends R> function14 = function13;
                receiveChannel5 = receiveChannel2;
                obj3 = hasNext;
                obj = obj2;
                channelsKt__Channels_commonKt$mapNotNullTo$12 = channelsKt__Channels_commonKt$mapNotNullTo$13;
                function12 = function14;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel5, th);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$mapNotNullTo$1 = new ChannelsKt__Channels_commonKt$mapNotNullTo$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$mapNotNullTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$mapNotNullTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$mapNotNullTo$13.L$0 = receiveChannel4;
            channelsKt__Channels_commonKt$mapNotNullTo$13.L$1 = c2;
            channelsKt__Channels_commonKt$mapNotNullTo$13.L$2 = function13;
            channelsKt__Channels_commonKt$mapNotNullTo$13.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$mapNotNullTo$13.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$mapNotNullTo$13.L$5 = th;
            channelsKt__Channels_commonKt$mapNotNullTo$13.L$6 = receiveChannel6;
            channelsKt__Channels_commonKt$mapNotNullTo$13.L$7 = channelIterator;
            channelsKt__Channels_commonKt$mapNotNullTo$13.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapNotNullTo$13);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th6) {
            th = th6;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b3 A[Catch:{ all -> 0x0115 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00c6 A[Catch:{ all -> 0x0115 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, C extends SendChannel<? super R>> Object mapNotNullTo(ReceiveChannel<? extends E> receiveChannel, C c, Function1<? super E, ? extends R> function1, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$mapNotNullTo$3 channelsKt__Channels_commonKt$mapNotNullTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel4;
        C c2;
        Function1<? super E, ? extends R> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        ReceiveChannel<? extends E> receiveChannel8;
        ChannelsKt__Channels_commonKt$mapNotNullTo$3 channelsKt__Channels_commonKt$mapNotNullTo$32;
        C c3;
        ChannelIterator channelIterator2;
        Throwable th2;
        Object obj;
        Function1<? super E, ? extends R> function13;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$mapNotNullTo$3) {
            channelsKt__Channels_commonKt$mapNotNullTo$3 = (ChannelsKt__Channels_commonKt$mapNotNullTo$3) continuation;
            if ((channelsKt__Channels_commonKt$mapNotNullTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$mapNotNullTo$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$mapNotNullTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$mapNotNullTo$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        channelsKt__Channels_commonKt$mapNotNullTo$32 = channelsKt__Channels_commonKt$mapNotNullTo$3;
                        obj = coroutine_suspended;
                        channelIterator2 = receiveChannel.iterator();
                        c3 = c;
                        function13 = function1;
                        th2 = null;
                        receiveChannel8 = receiveChannel;
                        receiveChannel2 = receiveChannel8;
                        receiveChannel7 = receiveChannel2;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th4;
                        }
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$mapNotNullTo$3.L$7;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$6;
                    Throwable th5 = (Throwable) channelsKt__Channels_commonKt$mapNotNullTo$3.L$5;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$mapNotNullTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$0;
                    ResultKt.throwOnFailure(obj2);
                    ReceiveChannel<? extends E> receiveChannel9 = receiveChannel6;
                    th2 = th5;
                    receiveChannel2 = receiveChannel9;
                    if (!((Boolean) obj2).booleanValue()) {
                        Object next = channelIterator.next();
                        Object invoke = function12.invoke(next);
                        if (invoke != null) {
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$0 = receiveChannel4;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$1 = c2;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$2 = function12;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$3 = receiveChannel5;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$4 = receiveChannel2;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$5 = th2;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$6 = receiveChannel3;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$7 = channelIterator;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$8 = next;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$9 = next;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.L$10 = invoke;
                            channelsKt__Channels_commonKt$mapNotNullTo$3.label = 2;
                            if (c2.send(invoke, channelsKt__Channels_commonKt$mapNotNullTo$3) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Throwable th6 = th2;
                            receiveChannel6 = receiveChannel2;
                            th = th6;
                            receiveChannel7 = receiveChannel3;
                            receiveChannel8 = receiveChannel5;
                            channelsKt__Channels_commonKt$mapNotNullTo$32 = channelsKt__Channels_commonKt$mapNotNullTo$3;
                            c3 = c2;
                            channelIterator2 = channelIterator;
                            receiveChannel = receiveChannel4;
                            ReceiveChannel<? extends E> receiveChannel10 = receiveChannel6;
                            th2 = th;
                            receiveChannel2 = receiveChannel10;
                            Function1<? super E, ? extends R> function14 = function12;
                            obj = coroutine_suspended;
                            function13 = function14;
                            return coroutine_suspended;
                        }
                        receiveChannel7 = receiveChannel3;
                        receiveChannel8 = receiveChannel5;
                        channelsKt__Channels_commonKt$mapNotNullTo$32 = channelsKt__Channels_commonKt$mapNotNullTo$3;
                        c3 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel4;
                        Function1<? super E, ? extends R> function15 = function12;
                        obj = coroutine_suspended;
                        function13 = function15;
                    }
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    InlineMarker.finallyEnd(1);
                    return c2;
                } else if (i == 2) {
                    Object obj3 = channelsKt__Channels_commonKt$mapNotNullTo$3.L$10;
                    Object obj4 = channelsKt__Channels_commonKt$mapNotNullTo$3.L$9;
                    Object obj5 = channelsKt__Channels_commonKt$mapNotNullTo$3.L$8;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$mapNotNullTo$3.L$7;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$6;
                    th = (Throwable) channelsKt__Channels_commonKt$mapNotNullTo$3.L$5;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$mapNotNullTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$mapNotNullTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        receiveChannel7 = receiveChannel3;
                        receiveChannel8 = receiveChannel5;
                        channelsKt__Channels_commonKt$mapNotNullTo$32 = channelsKt__Channels_commonKt$mapNotNullTo$3;
                        c3 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel4;
                        ReceiveChannel<? extends E> receiveChannel102 = receiveChannel6;
                        th2 = th;
                        receiveChannel2 = receiveChannel102;
                        Function1<? super E, ? extends R> function142 = function12;
                        obj = coroutine_suspended;
                        function13 = function142;
                    } catch (Throwable th7) {
                        th = th7;
                        receiveChannel2 = receiveChannel6;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$mapNotNullTo$32.L$0 = receiveChannel;
                channelsKt__Channels_commonKt$mapNotNullTo$32.L$1 = c3;
                channelsKt__Channels_commonKt$mapNotNullTo$32.L$2 = function13;
                channelsKt__Channels_commonKt$mapNotNullTo$32.L$3 = receiveChannel8;
                channelsKt__Channels_commonKt$mapNotNullTo$32.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$mapNotNullTo$32.L$5 = th2;
                channelsKt__Channels_commonKt$mapNotNullTo$32.L$6 = receiveChannel7;
                channelsKt__Channels_commonKt$mapNotNullTo$32.L$7 = channelIterator2;
                channelsKt__Channels_commonKt$mapNotNullTo$32.label = 1;
                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapNotNullTo$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj6 = hasNext;
                receiveChannel4 = receiveChannel;
                channelIterator = channelIterator2;
                c2 = c3;
                channelsKt__Channels_commonKt$mapNotNullTo$3 = channelsKt__Channels_commonKt$mapNotNullTo$32;
                receiveChannel5 = receiveChannel8;
                receiveChannel3 = receiveChannel7;
                obj2 = obj6;
                Object obj7 = obj;
                function12 = function13;
                coroutine_suspended = obj7;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$mapNotNullTo$3 = new ChannelsKt__Channels_commonKt$mapNotNullTo$3(continuation);
        Object obj22 = channelsKt__Channels_commonKt$mapNotNullTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$mapNotNullTo$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$mapNotNullTo$32.L$0 = receiveChannel;
            channelsKt__Channels_commonKt$mapNotNullTo$32.L$1 = c3;
            channelsKt__Channels_commonKt$mapNotNullTo$32.L$2 = function13;
            channelsKt__Channels_commonKt$mapNotNullTo$32.L$3 = receiveChannel8;
            channelsKt__Channels_commonKt$mapNotNullTo$32.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$mapNotNullTo$32.L$5 = th2;
            channelsKt__Channels_commonKt$mapNotNullTo$32.L$6 = receiveChannel7;
            channelsKt__Channels_commonKt$mapNotNullTo$32.L$7 = channelIterator2;
            channelsKt__Channels_commonKt$mapNotNullTo$32.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapNotNullTo$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th8) {
            th = th8;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009a A[Catch:{ all -> 0x0053 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, C extends Collection<? super R>> Object mapTo(ReceiveChannel<? extends E> receiveChannel, C c, Function1<? super E, ? extends R> function1, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$mapTo$1 channelsKt__Channels_commonKt$mapTo$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$mapTo$1 channelsKt__Channels_commonKt$mapTo$12;
        Function1<? super E, ? extends R> function12;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel5;
        C c2;
        ReceiveChannel<? extends E> receiveChannel6;
        Function1<? super E, ? extends R> function13;
        ChannelsKt__Channels_commonKt$mapTo$1 channelsKt__Channels_commonKt$mapTo$13;
        Object obj2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$mapTo$1) {
            channelsKt__Channels_commonKt$mapTo$1 = (ChannelsKt__Channels_commonKt$mapTo$1) continuation;
            if ((channelsKt__Channels_commonKt$mapTo$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$mapTo$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$mapTo$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$mapTo$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th = null;
                        channelsKt__Channels_commonKt$mapTo$13 = channelsKt__Channels_commonKt$mapTo$1;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel;
                        receiveChannel6 = receiveChannel2;
                        c2 = c;
                        receiveChannel4 = receiveChannel6;
                        channelIterator = receiveChannel.iterator();
                        function13 = function1;
                        receiveChannel3 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$mapTo$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$1.L$6;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$mapTo$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$1.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$mapTo$1.L$2;
                    C c3 = (Collection) channelsKt__Channels_commonKt$mapTo$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        ChannelsKt__Channels_commonKt$mapTo$1 channelsKt__Channels_commonKt$mapTo$14 = channelsKt__Channels_commonKt$mapTo$1;
                        receiveChannel6 = receiveChannel7;
                        receiveChannel4 = receiveChannel9;
                        obj = coroutine_suspended;
                        c2 = c3;
                        channelsKt__Channels_commonKt$mapTo$12 = channelsKt__Channels_commonKt$mapTo$14;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        th = th3;
                        receiveChannel3 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            c2.add(function12.invoke(channelIterator.next()));
                            receiveChannel2 = receiveChannel5;
                            function13 = function12;
                            channelsKt__Channels_commonKt$mapTo$13 = channelsKt__Channels_commonKt$mapTo$12;
                            obj2 = obj;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel5, th);
                        InlineMarker.finallyEnd(1);
                        return c2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel2 = receiveChannel5;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$mapTo$13.L$0 = receiveChannel4;
                channelsKt__Channels_commonKt$mapTo$13.L$1 = c2;
                channelsKt__Channels_commonKt$mapTo$13.L$2 = function13;
                channelsKt__Channels_commonKt$mapTo$13.L$3 = receiveChannel3;
                channelsKt__Channels_commonKt$mapTo$13.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$mapTo$13.L$5 = th;
                channelsKt__Channels_commonKt$mapTo$13.L$6 = receiveChannel6;
                channelsKt__Channels_commonKt$mapTo$13.L$7 = channelIterator;
                channelsKt__Channels_commonKt$mapTo$13.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapTo$13);
                if (hasNext != obj2) {
                    return obj2;
                }
                Function1<? super E, ? extends R> function14 = function13;
                receiveChannel5 = receiveChannel2;
                obj3 = hasNext;
                obj = obj2;
                channelsKt__Channels_commonKt$mapTo$12 = channelsKt__Channels_commonKt$mapTo$13;
                function12 = function14;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel5, th);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$mapTo$1 = new ChannelsKt__Channels_commonKt$mapTo$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$mapTo$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$mapTo$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$mapTo$13.L$0 = receiveChannel4;
            channelsKt__Channels_commonKt$mapTo$13.L$1 = c2;
            channelsKt__Channels_commonKt$mapTo$13.L$2 = function13;
            channelsKt__Channels_commonKt$mapTo$13.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$mapTo$13.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$mapTo$13.L$5 = th;
            channelsKt__Channels_commonKt$mapTo$13.L$6 = receiveChannel6;
            channelsKt__Channels_commonKt$mapTo$13.L$7 = channelIterator;
            channelsKt__Channels_commonKt$mapTo$13.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$mapTo$13);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th6) {
            th = th6;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00bc A[Catch:{ all -> 0x010a }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00cf A[Catch:{ all -> 0x010a }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, C extends SendChannel<? super R>> Object mapTo(ReceiveChannel<? extends E> receiveChannel, C c, Function1<? super E, ? extends R> function1, Continuation<? super C> continuation) {
        ChannelsKt__Channels_commonKt$mapTo$3 channelsKt__Channels_commonKt$mapTo$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel4;
        C c2;
        Function1<? super E, ? extends R> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel6;
        ReceiveChannel<? extends E> receiveChannel7;
        ChannelsKt__Channels_commonKt$mapTo$3 channelsKt__Channels_commonKt$mapTo$32;
        C c3;
        ChannelIterator channelIterator2;
        Object obj;
        Function1<? super E, ? extends R> function13;
        Object hasNext;
        ReceiveChannel<? extends E> receiveChannel8;
        if (continuation instanceof ChannelsKt__Channels_commonKt$mapTo$3) {
            channelsKt__Channels_commonKt$mapTo$3 = (ChannelsKt__Channels_commonKt$mapTo$3) continuation;
            if ((channelsKt__Channels_commonKt$mapTo$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$mapTo$3.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$mapTo$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$mapTo$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    try {
                        channelsKt__Channels_commonKt$mapTo$32 = channelsKt__Channels_commonKt$mapTo$3;
                        obj = coroutine_suspended;
                        channelIterator2 = receiveChannel.iterator();
                        c3 = c;
                        function13 = function1;
                        th = null;
                        receiveChannel7 = receiveChannel;
                        receiveChannel2 = receiveChannel7;
                        receiveChannel6 = receiveChannel2;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$mapTo$3.L$7;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$3.L$6;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$mapTo$3.L$5;
                    receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$3.L$4;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$3.L$3;
                    function12 = (Function1) channelsKt__Channels_commonKt$mapTo$3.L$2;
                    c2 = (SendChannel) channelsKt__Channels_commonKt$mapTo$3.L$1;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$3.L$0;
                    ResultKt.throwOnFailure(obj2);
                    ReceiveChannel<? extends E> receiveChannel9 = receiveChannel8;
                    th = th3;
                    receiveChannel2 = receiveChannel9;
                    if (!((Boolean) obj2).booleanValue()) {
                        Object next = channelIterator.next();
                        Object invoke = function12.invoke(next);
                        channelsKt__Channels_commonKt$mapTo$3.L$0 = receiveChannel4;
                        channelsKt__Channels_commonKt$mapTo$3.L$1 = c2;
                        channelsKt__Channels_commonKt$mapTo$3.L$2 = function12;
                        channelsKt__Channels_commonKt$mapTo$3.L$3 = receiveChannel5;
                        channelsKt__Channels_commonKt$mapTo$3.L$4 = receiveChannel2;
                        channelsKt__Channels_commonKt$mapTo$3.L$5 = th;
                        channelsKt__Channels_commonKt$mapTo$3.L$6 = receiveChannel3;
                        channelsKt__Channels_commonKt$mapTo$3.L$7 = channelIterator;
                        channelsKt__Channels_commonKt$mapTo$3.L$8 = next;
                        channelsKt__Channels_commonKt$mapTo$3.L$9 = next;
                        channelsKt__Channels_commonKt$mapTo$3.label = 2;
                        if (c2.send(invoke, channelsKt__Channels_commonKt$mapTo$3) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel6 = receiveChannel3;
                        receiveChannel7 = receiveChannel5;
                        channelsKt__Channels_commonKt$mapTo$32 = channelsKt__Channels_commonKt$mapTo$3;
                        c3 = c2;
                        channelIterator2 = channelIterator;
                        receiveChannel = receiveChannel4;
                        Function1<? super E, ? extends R> function14 = function12;
                        obj = coroutine_suspended;
                        function13 = function14;
                        return coroutine_suspended;
                    }
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    InlineMarker.finallyEnd(1);
                    return c2;
                } else if (i == 2) {
                    Object obj3 = channelsKt__Channels_commonKt$mapTo$3.L$9;
                    Object obj4 = channelsKt__Channels_commonKt$mapTo$3.L$8;
                    ChannelIterator channelIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$mapTo$3.L$7;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$3.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$mapTo$3.L$5;
                    receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$3.L$4;
                    ReceiveChannel<? extends E> receiveChannel11 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$3.L$3;
                    Function1<? super E, ? extends R> function15 = (Function1) channelsKt__Channels_commonKt$mapTo$3.L$2;
                    C c4 = (SendChannel) channelsKt__Channels_commonKt$mapTo$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel12 = (ReceiveChannel) channelsKt__Channels_commonKt$mapTo$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        receiveChannel6 = receiveChannel10;
                        receiveChannel7 = receiveChannel11;
                        channelsKt__Channels_commonKt$mapTo$32 = channelsKt__Channels_commonKt$mapTo$3;
                        c3 = c4;
                        channelIterator2 = channelIterator3;
                        receiveChannel = receiveChannel12;
                        ReceiveChannel<? extends E> receiveChannel13 = receiveChannel8;
                        th = th4;
                        receiveChannel2 = receiveChannel13;
                        Function1<? super E, ? extends R> function16 = function15;
                        obj = coroutine_suspended;
                        function13 = function16;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$mapTo$32.L$0 = receiveChannel;
                channelsKt__Channels_commonKt$mapTo$32.L$1 = c3;
                channelsKt__Channels_commonKt$mapTo$32.L$2 = function13;
                channelsKt__Channels_commonKt$mapTo$32.L$3 = receiveChannel7;
                channelsKt__Channels_commonKt$mapTo$32.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$mapTo$32.L$5 = th;
                channelsKt__Channels_commonKt$mapTo$32.L$6 = receiveChannel6;
                channelsKt__Channels_commonKt$mapTo$32.L$7 = channelIterator2;
                channelsKt__Channels_commonKt$mapTo$32.label = 1;
                hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapTo$32);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj5 = hasNext;
                receiveChannel4 = receiveChannel;
                channelIterator = channelIterator2;
                c2 = c3;
                channelsKt__Channels_commonKt$mapTo$3 = channelsKt__Channels_commonKt$mapTo$32;
                receiveChannel5 = receiveChannel7;
                receiveChannel3 = receiveChannel6;
                obj2 = obj5;
                Object obj6 = obj;
                function12 = function13;
                coroutine_suspended = obj6;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return c2;
                return obj;
            }
        }
        channelsKt__Channels_commonKt$mapTo$3 = new ChannelsKt__Channels_commonKt$mapTo$3(continuation);
        Object obj22 = channelsKt__Channels_commonKt$mapTo$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$mapTo$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$mapTo$32.L$0 = receiveChannel;
            channelsKt__Channels_commonKt$mapTo$32.L$1 = c3;
            channelsKt__Channels_commonKt$mapTo$32.L$2 = function13;
            channelsKt__Channels_commonKt$mapTo$32.L$3 = receiveChannel7;
            channelsKt__Channels_commonKt$mapTo$32.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$mapTo$32.L$5 = th;
            channelsKt__Channels_commonKt$mapTo$32.L$6 = receiveChannel6;
            channelsKt__Channels_commonKt$mapTo$32.L$7 = channelIterator2;
            channelsKt__Channels_commonKt$mapTo$32.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapTo$32);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    public static /* synthetic */ ReceiveChannel withIndex$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.withIndex(receiveChannel, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<IndexedValue<E>> withIndex(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$withIndex");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$withIndex$1(receiveChannel, null), 2, null);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> distinct(ReceiveChannel<? extends E> receiveChannel) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$distinct");
        return distinctBy$default(receiveChannel, null, new ChannelsKt__Channels_commonKt$distinct$1(null), 1, null);
    }

    public static /* synthetic */ ReceiveChannel distinctBy$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.distinctBy(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, K> ReceiveChannel<E> distinctBy(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super K>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$distinctBy");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "selector");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumes(receiveChannel), new ChannelsKt__Channels_commonKt$distinctBy$1(receiveChannel, function2, null), 2, null);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object toMutableSet(ReceiveChannel<? extends E> receiveChannel, Continuation<? super Set<E>> continuation) {
        return ChannelsKt.toCollection(receiveChannel, new LinkedHashSet(), continuation);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085 A[Catch:{ all -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0092 A[Catch:{ all -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b6 A[SYNTHETIC, Splitter:B:34:0x00b6] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object all(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super Boolean> continuation) {
        ChannelsKt__Channels_commonKt$all$1 channelsKt__Channels_commonKt$all$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$all$1 channelsKt__Channels_commonKt$all$12;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$all$1) {
            channelsKt__Channels_commonKt$all$1 = (ChannelsKt__Channels_commonKt$all$1) continuation;
            if ((channelsKt__Channels_commonKt$all$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$all$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$all$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$all$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        channelsKt__Channels_commonKt$all$12 = channelsKt__Channels_commonKt$all$1;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$all$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$all$1.L$5;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$all$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$all$1.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$all$1.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$all$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$all$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        th = th4;
                        function12 = function13;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$all$12 = channelsKt__Channels_commonKt$all$1;
                        receiveChannel2 = receiveChannel10;
                        if (((Boolean) obj3).booleanValue()) {
                            if (!((Boolean) function12.invoke(channelIterator.next())).booleanValue()) {
                                Boolean boxBoolean = Boxing.boxBoolean(false);
                            } else {
                                receiveChannel6 = receiveChannel4;
                                obj2 = obj;
                                th2 = th;
                            }
                            Boolean boxBoolean2 = Boxing.boxBoolean(false);
                            InlineMarker.finallyStart(2);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(2);
                            return boxBoolean2;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return Boxing.boxBoolean(true);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$all$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$all$12.L$1 = function12;
                channelsKt__Channels_commonKt$all$12.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$all$12.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$all$12.L$4 = th2;
                channelsKt__Channels_commonKt$all$12.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$all$12.L$6 = channelIterator;
                channelsKt__Channels_commonKt$all$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$all$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                th = th2;
                obj = obj5;
                if (((Boolean) obj3).booleanValue()) {
                }
                Unit unit2 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return Boxing.boxBoolean(true);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$all$1 = new ChannelsKt__Channels_commonKt$all$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$all$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$all$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$all$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$all$12.L$1 = function12;
            channelsKt__Channels_commonKt$all$12.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$all$12.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$all$12.L$4 = th2;
            channelsKt__Channels_commonKt$all$12.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$all$12.L$6 = channelIterator;
            channelsKt__Channels_commonKt$all$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$all$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object any(ReceiveChannel<? extends E> receiveChannel, Continuation<? super Boolean> continuation) {
        ChannelsKt__Channels_commonKt$any$1 channelsKt__Channels_commonKt$any$1;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th2;
        Object obj;
        if (continuation instanceof ChannelsKt__Channels_commonKt$any$1) {
            channelsKt__Channels_commonKt$any$1 = (ChannelsKt__Channels_commonKt$any$1) continuation;
            if ((channelsKt__Channels_commonKt$any$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$any$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$any$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$any$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    th2 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$any$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$any$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$any$1.L$2 = th2;
                        channelsKt__Channels_commonKt$any$1.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$any$1.label = 1;
                        obj = it.hasNext(channelsKt__Channels_commonKt$any$1);
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    ReceiveChannel receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$any$1.L$3;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$any$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$any$1.L$1;
                    ReceiveChannel receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$any$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        obj = obj2;
                        th2 = th4;
                        receiveChannel = receiveChannel2;
                    } catch (Throwable th5) {
                        th = th5;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ChannelsKt.cancelConsumed(receiveChannel, th2);
                return obj;
            }
        }
        channelsKt__Channels_commonKt$any$1 = new ChannelsKt__Channels_commonKt$any$1(continuation);
        Object obj22 = channelsKt__Channels_commonKt$any$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$any$1.label;
        if (i != 0) {
        }
        ChannelsKt.cancelConsumed(receiveChannel, th2);
        return obj;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085 A[Catch:{ all -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0092 A[Catch:{ all -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b5 A[SYNTHETIC, Splitter:B:34:0x00b5] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object any(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super Boolean> continuation) {
        ChannelsKt__Channels_commonKt$any$3 channelsKt__Channels_commonKt$any$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$any$3 channelsKt__Channels_commonKt$any$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$any$3) {
            channelsKt__Channels_commonKt$any$3 = (ChannelsKt__Channels_commonKt$any$3) continuation;
            if ((channelsKt__Channels_commonKt$any$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$any$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$any$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$any$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        channelsKt__Channels_commonKt$any$32 = channelsKt__Channels_commonKt$any$3;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$any$3.L$6;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$any$3.L$5;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$any$3.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$any$3.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$any$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$any$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$any$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        th = th4;
                        function12 = function13;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$any$32 = channelsKt__Channels_commonKt$any$3;
                        receiveChannel2 = receiveChannel10;
                        if (((Boolean) obj3).booleanValue()) {
                            if (((Boolean) function12.invoke(channelIterator.next())).booleanValue()) {
                                Boolean boxBoolean = Boxing.boxBoolean(true);
                            } else {
                                receiveChannel6 = receiveChannel4;
                                obj2 = obj;
                                th2 = th;
                            }
                            Boolean boxBoolean2 = Boxing.boxBoolean(true);
                            InlineMarker.finallyStart(2);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(2);
                            return boxBoolean2;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return Boxing.boxBoolean(false);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$any$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$any$32.L$1 = function12;
                channelsKt__Channels_commonKt$any$32.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$any$32.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$any$32.L$4 = th2;
                channelsKt__Channels_commonKt$any$32.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$any$32.L$6 = channelIterator;
                channelsKt__Channels_commonKt$any$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$any$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                th = th2;
                obj = obj5;
                if (((Boolean) obj3).booleanValue()) {
                }
                Unit unit2 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return Boxing.boxBoolean(false);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$any$3 = new ChannelsKt__Channels_commonKt$any$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$any$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$any$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$any$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$any$32.L$1 = function12;
            channelsKt__Channels_commonKt$any$32.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$any$32.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$any$32.L$4 = th2;
            channelsKt__Channels_commonKt$any$32.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$any$32.L$6 = channelIterator;
            channelsKt__Channels_commonKt$any$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$any$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008b A[Catch:{ all -> 0x00ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0096 A[Catch:{ all -> 0x00ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object count(ReceiveChannel<? extends E> receiveChannel, Continuation<? super Integer> continuation) {
        ChannelsKt__Channels_commonKt$count$1 channelsKt__Channels_commonKt$count$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel3;
        IntRef intRef;
        Throwable th;
        Object obj;
        ChannelsKt__Channels_commonKt$count$1 channelsKt__Channels_commonKt$count$12;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$count$1) {
            channelsKt__Channels_commonKt$count$1 = (ChannelsKt__Channels_commonKt$count$1) continuation;
            if ((channelsKt__Channels_commonKt$count$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$count$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$count$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$count$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    IntRef intRef2 = new IntRef();
                    intRef2.element = 0;
                    try {
                        intRef = intRef2;
                        obj = coroutine_suspended;
                        th = null;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel4 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$count$12 = channelsKt__Channels_commonKt$count$1;
                        receiveChannel5 = receiveChannel4;
                    } catch (Throwable th2) {
                        receiveChannel2 = receiveChannel;
                        th = th2;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$count$1.L$6;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$count$1.L$5;
                    Throwable th3 = (Throwable) channelsKt__Channels_commonKt$count$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$count$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$count$1.L$2;
                    intRef = (IntRef) channelsKt__Channels_commonKt$count$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$count$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        Throwable th4 = th3;
                        channelsKt__Channels_commonKt$count$12 = channelsKt__Channels_commonKt$count$1;
                        receiveChannel5 = receiveChannel8;
                        th = th4;
                        ReceiveChannel<? extends E> receiveChannel9 = receiveChannel7;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel9;
                        if (!((Boolean) obj2).booleanValue()) {
                            channelIterator.next();
                            intRef.element++;
                            receiveChannel6 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        return Boxing.boxInt(intRef.element);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel7;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$count$12.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$count$12.L$1 = intRef;
                channelsKt__Channels_commonKt$count$12.L$2 = receiveChannel5;
                channelsKt__Channels_commonKt$count$12.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$count$12.L$4 = th;
                channelsKt__Channels_commonKt$count$12.L$5 = receiveChannel4;
                channelsKt__Channels_commonKt$count$12.L$6 = channelIterator;
                channelsKt__Channels_commonKt$count$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$count$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                return Boxing.boxInt(intRef.element);
                return obj;
            }
        }
        channelsKt__Channels_commonKt$count$1 = new ChannelsKt__Channels_commonKt$count$1(continuation);
        Object obj22 = channelsKt__Channels_commonKt$count$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$count$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$count$12.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$count$12.L$1 = intRef;
            channelsKt__Channels_commonKt$count$12.L$2 = receiveChannel5;
            channelsKt__Channels_commonKt$count$12.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$count$12.L$4 = th;
            channelsKt__Channels_commonKt$count$12.L$5 = receiveChannel4;
            channelsKt__Channels_commonKt$count$12.L$6 = channelIterator;
            channelsKt__Channels_commonKt$count$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$count$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0096 A[Catch:{ all -> 0x00d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a4 A[Catch:{ all -> 0x00d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00be A[Catch:{ all -> 0x00d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object count(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super Integer> continuation) {
        ChannelsKt__Channels_commonKt$count$3 channelsKt__Channels_commonKt$count$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        IntRef intRef;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$count$3 channelsKt__Channels_commonKt$count$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        IntRef intRef2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$count$3) {
            channelsKt__Channels_commonKt$count$3 = (ChannelsKt__Channels_commonKt$count$3) continuation;
            if ((channelsKt__Channels_commonKt$count$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$count$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$count$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$count$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    IntRef intRef3 = new IntRef();
                    intRef3.element = 0;
                    try {
                        intRef2 = intRef3;
                        obj2 = coroutine_suspended;
                        th2 = null;
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$count$32 = channelsKt__Channels_commonKt$count$3;
                        receiveChannel2 = receiveChannel3;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$count$3.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$count$3.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$count$3.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$count$3.L$4;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$count$3.L$3;
                    IntRef intRef4 = (IntRef) channelsKt__Channels_commonKt$count$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$count$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$count$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        intRef = intRef4;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$count$32 = channelsKt__Channels_commonKt$count$3;
                        receiveChannel2 = receiveChannel10;
                        Function1<? super E, Boolean> function14 = function13;
                        th = th4;
                        function12 = function14;
                        if (!((Boolean) obj3).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                        } else {
                            if (((Boolean) function12.invoke(channelIterator.next())).booleanValue()) {
                                intRef.element++;
                            }
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                            intRef2 = intRef;
                        }
                        Unit unit2 = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return Boxing.boxInt(intRef.element);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$count$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$count$32.L$1 = function12;
                channelsKt__Channels_commonKt$count$32.L$2 = intRef2;
                channelsKt__Channels_commonKt$count$32.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$count$32.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$count$32.L$5 = th2;
                channelsKt__Channels_commonKt$count$32.L$6 = receiveChannel5;
                channelsKt__Channels_commonKt$count$32.L$7 = channelIterator;
                channelsKt__Channels_commonKt$count$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$count$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                intRef = intRef2;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return Boxing.boxInt(intRef.element);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$count$3 = new ChannelsKt__Channels_commonKt$count$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$count$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$count$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$count$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$count$32.L$1 = function12;
            channelsKt__Channels_commonKt$count$32.L$2 = intRef2;
            channelsKt__Channels_commonKt$count$32.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$count$32.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$count$32.L$5 = th2;
            channelsKt__Channels_commonKt$count$32.L$6 = receiveChannel5;
            channelsKt__Channels_commonKt$count$32.L$7 = channelIterator;
            channelsKt__Channels_commonKt$count$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$count$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=R, code=java.lang.Object, for r18v0, types: [R, java.lang.Object] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a5 A[Catch:{ all -> 0x00cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b0 A[Catch:{ all -> 0x00cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R> Object fold(ReceiveChannel<? extends E> receiveChannel, Object obj, Function2<? super R, ? super E, ? extends R> function2, Continuation<? super R> continuation) {
        ChannelsKt__Channels_commonKt$fold$1 channelsKt__Channels_commonKt$fold$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        ObjectRef objectRef;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj2;
        ChannelsKt__Channels_commonKt$fold$1 channelsKt__Channels_commonKt$fold$12;
        Function2<? super R, ? super E, ? extends R> function22;
        Object obj3;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object hasNext;
        Continuation<? super R> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$fold$1) {
            channelsKt__Channels_commonKt$fold$1 = (ChannelsKt__Channels_commonKt$fold$1) continuation2;
            if ((channelsKt__Channels_commonKt$fold$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$fold$1.label -= Integer.MIN_VALUE;
                Object obj4 = channelsKt__Channels_commonKt$fold$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$fold$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj4);
                    ObjectRef objectRef2 = new ObjectRef();
                    Object obj5 = obj;
                    objectRef2.element = obj5;
                    try {
                        objectRef = objectRef2;
                        channelsKt__Channels_commonKt$fold$12 = channelsKt__Channels_commonKt$fold$1;
                        obj2 = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        function22 = function2;
                        obj3 = obj5;
                        receiveChannel4 = receiveChannel2;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$fold$1.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$fold$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$fold$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$fold$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$fold$1.L$4;
                    ObjectRef objectRef3 = (ObjectRef) channelsKt__Channels_commonKt$fold$1.L$3;
                    Function2<? super R, ? super E, ? extends R> function23 = (Function2) channelsKt__Channels_commonKt$fold$1.L$2;
                    Object obj6 = channelsKt__Channels_commonKt$fold$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$fold$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj4);
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$fold$12 = channelsKt__Channels_commonKt$fold$1;
                        receiveChannel5 = receiveChannel9;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel10;
                        ObjectRef objectRef4 = objectRef3;
                        channelIterator = channelIterator2;
                        receiveChannel4 = receiveChannel7;
                        obj3 = obj6;
                        objectRef = objectRef4;
                        Function2<? super R, ? super E, ? extends R> function24 = function23;
                        th2 = th4;
                        function22 = function24;
                        if (!((Boolean) obj4).booleanValue()) {
                            objectRef.element = function22.invoke(objectRef.element, channelIterator.next());
                            receiveChannel6 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return objectRef.element;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$fold$12.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$fold$12.L$1 = obj3;
                channelsKt__Channels_commonKt$fold$12.L$2 = function22;
                channelsKt__Channels_commonKt$fold$12.L$3 = objectRef;
                channelsKt__Channels_commonKt$fold$12.L$4 = receiveChannel5;
                channelsKt__Channels_commonKt$fold$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$fold$12.L$6 = th2;
                channelsKt__Channels_commonKt$fold$12.L$7 = receiveChannel4;
                channelsKt__Channels_commonKt$fold$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$fold$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$fold$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj7 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj4 = obj7;
                if (!((Boolean) obj4).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return objectRef.element;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$fold$1 = new ChannelsKt__Channels_commonKt$fold$1(continuation2);
        Object obj42 = channelsKt__Channels_commonKt$fold$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$fold$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$fold$12.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$fold$12.L$1 = obj3;
            channelsKt__Channels_commonKt$fold$12.L$2 = function22;
            channelsKt__Channels_commonKt$fold$12.L$3 = objectRef;
            channelsKt__Channels_commonKt$fold$12.L$4 = receiveChannel5;
            channelsKt__Channels_commonKt$fold$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$fold$12.L$6 = th2;
            channelsKt__Channels_commonKt$fold$12.L$7 = receiveChannel4;
            channelsKt__Channels_commonKt$fold$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$fold$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$fold$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=R, code=java.lang.Object, for r19v0, types: [R, java.lang.Object] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00b5 A[Catch:{ all -> 0x00e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00c2 A[Catch:{ all -> 0x00e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R> Object foldIndexed(ReceiveChannel<? extends E> receiveChannel, Object obj, Function3<? super Integer, ? super R, ? super E, ? extends R> function3, Continuation<? super R> continuation) {
        ChannelsKt__Channels_commonKt$foldIndexed$1 channelsKt__Channels_commonKt$foldIndexed$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        IntRef intRef;
        ObjectRef objectRef;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj2;
        ChannelsKt__Channels_commonKt$foldIndexed$1 channelsKt__Channels_commonKt$foldIndexed$12;
        Function3<? super Integer, ? super R, ? super E, ? extends R> function32;
        Object obj3;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object hasNext;
        Continuation<? super R> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$foldIndexed$1) {
            channelsKt__Channels_commonKt$foldIndexed$1 = (ChannelsKt__Channels_commonKt$foldIndexed$1) continuation2;
            if ((channelsKt__Channels_commonKt$foldIndexed$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$foldIndexed$1.label -= Integer.MIN_VALUE;
                Object obj4 = channelsKt__Channels_commonKt$foldIndexed$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$foldIndexed$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj4);
                    IntRef intRef2 = new IntRef();
                    intRef2.element = 0;
                    ObjectRef objectRef2 = new ObjectRef();
                    obj3 = obj;
                    objectRef2.element = obj3;
                    try {
                        intRef = intRef2;
                        obj2 = coroutine_suspended;
                        objectRef = objectRef2;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel4 = receiveChannel2;
                        function32 = function3;
                        channelsKt__Channels_commonKt$foldIndexed$12 = channelsKt__Channels_commonKt$foldIndexed$1;
                        receiveChannel5 = receiveChannel4;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$foldIndexed$1.L$9;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$foldIndexed$1.L$8;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$foldIndexed$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$foldIndexed$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$foldIndexed$1.L$5;
                    ObjectRef objectRef3 = (ObjectRef) channelsKt__Channels_commonKt$foldIndexed$1.L$4;
                    IntRef intRef3 = (IntRef) channelsKt__Channels_commonKt$foldIndexed$1.L$3;
                    Function3<? super Integer, ? super R, ? super E, ? extends R> function33 = (Function3) channelsKt__Channels_commonKt$foldIndexed$1.L$2;
                    Object obj5 = channelsKt__Channels_commonKt$foldIndexed$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$foldIndexed$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj4);
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$foldIndexed$12 = channelsKt__Channels_commonKt$foldIndexed$1;
                        receiveChannel5 = receiveChannel9;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel10;
                        ObjectRef objectRef4 = objectRef3;
                        channelIterator = channelIterator2;
                        receiveChannel4 = receiveChannel7;
                        obj3 = obj5;
                        intRef = intRef3;
                        th2 = th4;
                        function32 = function33;
                        objectRef = objectRef4;
                        if (!((Boolean) obj4).booleanValue()) {
                            Object next = channelIterator.next();
                            int i2 = intRef.element;
                            intRef.element = i2 + 1;
                            objectRef.element = function32.invoke(Boxing.boxInt(i2), objectRef.element, next);
                            receiveChannel6 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return objectRef.element;
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            Throwable th7 = th6;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th7;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$foldIndexed$12.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$foldIndexed$12.L$1 = obj3;
                channelsKt__Channels_commonKt$foldIndexed$12.L$2 = function32;
                channelsKt__Channels_commonKt$foldIndexed$12.L$3 = intRef;
                channelsKt__Channels_commonKt$foldIndexed$12.L$4 = objectRef;
                channelsKt__Channels_commonKt$foldIndexed$12.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$foldIndexed$12.L$6 = receiveChannel2;
                channelsKt__Channels_commonKt$foldIndexed$12.L$7 = th2;
                channelsKt__Channels_commonKt$foldIndexed$12.L$8 = receiveChannel4;
                channelsKt__Channels_commonKt$foldIndexed$12.L$9 = channelIterator;
                channelsKt__Channels_commonKt$foldIndexed$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$foldIndexed$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj6 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj4 = obj6;
                if (!((Boolean) obj4).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return objectRef.element;
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$foldIndexed$1 = new ChannelsKt__Channels_commonKt$foldIndexed$1(continuation2);
        Object obj42 = channelsKt__Channels_commonKt$foldIndexed$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$foldIndexed$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$foldIndexed$12.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$foldIndexed$12.L$1 = obj3;
            channelsKt__Channels_commonKt$foldIndexed$12.L$2 = function32;
            channelsKt__Channels_commonKt$foldIndexed$12.L$3 = intRef;
            channelsKt__Channels_commonKt$foldIndexed$12.L$4 = objectRef;
            channelsKt__Channels_commonKt$foldIndexed$12.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$foldIndexed$12.L$6 = receiveChannel2;
            channelsKt__Channels_commonKt$foldIndexed$12.L$7 = th2;
            channelsKt__Channels_commonKt$foldIndexed$12.L$8 = receiveChannel4;
            channelsKt__Channels_commonKt$foldIndexed$12.L$9 = channelIterator;
            channelsKt__Channels_commonKt$foldIndexed$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$foldIndexed$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th8) {
            th = th8;
            th = th;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c3 A[SYNTHETIC, Splitter:B:36:0x00c3] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00eb A[Catch:{ all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00f8 A[Catch:{ all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002a  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R extends Comparable<? super R>> Object maxBy(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, ? extends R> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$maxBy$1 channelsKt__Channels_commonKt$maxBy$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        Function1<? super E, ? extends R> function12;
        Throwable th2;
        ChannelIterator channelIterator;
        Comparable comparable;
        Object obj2;
        ReceiveChannel<? extends E> receiveChannel3;
        Object hasNext;
        ChannelIterator channelIterator2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        Function1<? super E, ? extends R> function13;
        ReceiveChannel<? extends E> receiveChannel6;
        Throwable th3;
        ReceiveChannel<? extends E> receiveChannel7 = receiveChannel;
        Continuation<? super E> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$maxBy$1) {
            channelsKt__Channels_commonKt$maxBy$1 = (ChannelsKt__Channels_commonKt$maxBy$1) continuation2;
            if ((channelsKt__Channels_commonKt$maxBy$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$maxBy$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$maxBy$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$maxBy$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Throwable th4 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$maxBy$1.L$0 = receiveChannel7;
                        Function1<? super E, ? extends R> function14 = function1;
                        channelsKt__Channels_commonKt$maxBy$1.L$1 = function14;
                        channelsKt__Channels_commonKt$maxBy$1.L$2 = receiveChannel7;
                        channelsKt__Channels_commonKt$maxBy$1.L$3 = th4;
                        channelsKt__Channels_commonKt$maxBy$1.L$4 = receiveChannel7;
                        channelsKt__Channels_commonKt$maxBy$1.L$5 = it;
                        channelsKt__Channels_commonKt$maxBy$1.label = 1;
                        Object hasNext2 = it.hasNext(channelsKt__Channels_commonKt$maxBy$1);
                        if (hasNext2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel5 = receiveChannel7;
                        function13 = function14;
                        receiveChannel6 = receiveChannel5;
                        th3 = th4;
                        channelIterator = it;
                        obj = hasNext2;
                        th2 = th3;
                        if (((Boolean) obj).booleanValue()) {
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$maxBy$1.L$5;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$maxBy$1.L$4;
                    Throwable th6 = (Throwable) channelsKt__Channels_commonKt$maxBy$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$maxBy$1.L$2;
                    function13 = (Function1) channelsKt__Channels_commonKt$maxBy$1.L$1;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$maxBy$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        th3 = th6;
                        channelIterator = channelIterator3;
                        receiveChannel7 = receiveChannel8;
                        th2 = th3;
                        if (((Boolean) obj).booleanValue()) {
                            InlineMarker.finallyStart(3);
                            ChannelsKt.cancelConsumed(receiveChannel7, th2);
                            InlineMarker.finallyEnd(3);
                            return null;
                        }
                        Object next = channelIterator.next();
                        comparable = (Comparable) function13.invoke(next);
                        receiveChannel2 = receiveChannel5;
                        function12 = function13;
                        ReceiveChannel<? extends E> receiveChannel9 = receiveChannel6;
                        obj2 = next;
                        receiveChannel3 = receiveChannel9;
                        channelsKt__Channels_commonKt$maxBy$1.L$0 = receiveChannel2;
                        channelsKt__Channels_commonKt$maxBy$1.L$1 = function12;
                        channelsKt__Channels_commonKt$maxBy$1.L$2 = receiveChannel7;
                        channelsKt__Channels_commonKt$maxBy$1.L$3 = th2;
                        channelsKt__Channels_commonKt$maxBy$1.L$4 = receiveChannel3;
                        channelsKt__Channels_commonKt$maxBy$1.L$5 = channelIterator;
                        channelsKt__Channels_commonKt$maxBy$1.L$6 = obj2;
                        channelsKt__Channels_commonKt$maxBy$1.L$7 = comparable;
                        channelsKt__Channels_commonKt$maxBy$1.label = 2;
                        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$maxBy$1);
                        if (hasNext != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        channelIterator2 = channelIterator;
                        receiveChannel4 = receiveChannel3;
                        obj = hasNext;
                        return coroutine_suspended;
                    } catch (Throwable th7) {
                        th = th7;
                        receiveChannel7 = receiveChannel8;
                        throw th;
                    }
                } else if (i == 2) {
                    Comparable comparable2 = (Comparable) channelsKt__Channels_commonKt$maxBy$1.L$7;
                    obj2 = channelsKt__Channels_commonKt$maxBy$1.L$6;
                    ChannelIterator channelIterator4 = (ChannelIterator) channelsKt__Channels_commonKt$maxBy$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$maxBy$1.L$4;
                    th2 = (Throwable) channelsKt__Channels_commonKt$maxBy$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$maxBy$1.L$2;
                    function12 = (Function1) channelsKt__Channels_commonKt$maxBy$1.L$1;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$maxBy$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        channelIterator2 = channelIterator4;
                        comparable = comparable2;
                        receiveChannel7 = receiveChannel10;
                    } catch (Throwable th8) {
                        th = th8;
                        receiveChannel7 = receiveChannel10;
                        try {
                            throw th;
                        } catch (Throwable th9) {
                            Throwable th10 = th9;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel7, th);
                            InlineMarker.finallyEnd(1);
                            throw th10;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                Throwable th11 = th2;
                ChannelIterator channelIterator5 = channelIterator2;
                if (!((Boolean) obj).booleanValue()) {
                    Object next2 = channelIterator5.next();
                    Comparable comparable3 = (Comparable) function12.invoke(next2);
                    if (comparable.compareTo(comparable3) < 0) {
                        obj2 = next2;
                        comparable = comparable3;
                    }
                    receiveChannel3 = receiveChannel4;
                    channelIterator = channelIterator5;
                    th2 = th11;
                    channelsKt__Channels_commonKt$maxBy$1.L$0 = receiveChannel2;
                    channelsKt__Channels_commonKt$maxBy$1.L$1 = function12;
                    channelsKt__Channels_commonKt$maxBy$1.L$2 = receiveChannel7;
                    channelsKt__Channels_commonKt$maxBy$1.L$3 = th2;
                    channelsKt__Channels_commonKt$maxBy$1.L$4 = receiveChannel3;
                    channelsKt__Channels_commonKt$maxBy$1.L$5 = channelIterator;
                    channelsKt__Channels_commonKt$maxBy$1.L$6 = obj2;
                    channelsKt__Channels_commonKt$maxBy$1.L$7 = comparable;
                    channelsKt__Channels_commonKt$maxBy$1.label = 2;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$maxBy$1);
                    if (hasNext != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                }
                InlineMarker.finallyStart(2);
                ChannelsKt.cancelConsumed(receiveChannel7, th11);
                InlineMarker.finallyEnd(2);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$maxBy$1 = new ChannelsKt__Channels_commonKt$maxBy$1(continuation2);
        obj = channelsKt__Channels_commonKt$maxBy$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$maxBy$1.label;
        if (i != 0) {
        }
        Throwable th112 = th2;
        ChannelIterator channelIterator52 = channelIterator2;
        if (!((Boolean) obj).booleanValue()) {
            InlineMarker.finallyStart(2);
            ChannelsKt.cancelConsumed(receiveChannel7, th112);
            InlineMarker.finallyEnd(2);
        }
        InlineMarker.finallyStart(2);
        ChannelsKt.cancelConsumed(receiveChannel7, th112);
        InlineMarker.finallyEnd(2);
        return obj2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a1 A[SYNTHETIC, Splitter:B:30:0x00a1] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00cc A[Catch:{ all -> 0x006d }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object maxWith(ReceiveChannel<? extends E> receiveChannel, Comparator<? super E> comparator, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$maxWith$1 channelsKt__Channels_commonKt$maxWith$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel3;
        Comparator<? super E> comparator2;
        Throwable th2;
        ReceiveChannel<? extends E> receiveChannel4;
        Object next;
        Object hasNext;
        ReceiveChannel<? extends E> receiveChannel5;
        ChannelIterator channelIterator2;
        Throwable th3;
        if (continuation instanceof ChannelsKt__Channels_commonKt$maxWith$1) {
            channelsKt__Channels_commonKt$maxWith$1 = (ChannelsKt__Channels_commonKt$maxWith$1) continuation;
            if ((channelsKt__Channels_commonKt$maxWith$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$maxWith$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$maxWith$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$maxWith$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    th3 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$maxWith$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$maxWith$1.L$1 = comparator;
                        channelsKt__Channels_commonKt$maxWith$1.L$2 = receiveChannel;
                        channelsKt__Channels_commonKt$maxWith$1.L$3 = th3;
                        channelsKt__Channels_commonKt$maxWith$1.L$4 = receiveChannel;
                        channelsKt__Channels_commonKt$maxWith$1.L$5 = it;
                        channelsKt__Channels_commonKt$maxWith$1.label = 1;
                        Object hasNext2 = it.hasNext(channelsKt__Channels_commonKt$maxWith$1);
                        if (hasNext2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel3 = receiveChannel;
                        comparator2 = comparator;
                        receiveChannel5 = receiveChannel3;
                        channelIterator2 = it;
                        obj = hasNext2;
                        receiveChannel2 = receiveChannel5;
                    } catch (Throwable th4) {
                        receiveChannel2 = receiveChannel;
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th5;
                        }
                    }
                } else if (i == 1) {
                    channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$maxWith$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$maxWith$1.L$4;
                    th3 = (Throwable) channelsKt__Channels_commonKt$maxWith$1.L$3;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$maxWith$1.L$2;
                    comparator2 = (Comparator) channelsKt__Channels_commonKt$maxWith$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$maxWith$1.L$0;
                    ResultKt.throwOnFailure(obj);
                } else if (i == 2) {
                    Object obj2 = channelsKt__Channels_commonKt$maxWith$1.L$6;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$maxWith$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$maxWith$1.L$4;
                    th2 = (Throwable) channelsKt__Channels_commonKt$maxWith$1.L$3;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$maxWith$1.L$2;
                    comparator2 = (Comparator) channelsKt__Channels_commonKt$maxWith$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$maxWith$1.L$0;
                    ResultKt.throwOnFailure(obj);
                    if (!((Boolean) obj).booleanValue()) {
                        next = channelIterator.next();
                        if (comparator2.compare(obj2, next) >= 0) {
                            next = obj2;
                        }
                        receiveChannel = receiveChannel2;
                        channelsKt__Channels_commonKt$maxWith$1.L$0 = receiveChannel3;
                        channelsKt__Channels_commonKt$maxWith$1.L$1 = comparator2;
                        channelsKt__Channels_commonKt$maxWith$1.L$2 = receiveChannel;
                        channelsKt__Channels_commonKt$maxWith$1.L$3 = th2;
                        channelsKt__Channels_commonKt$maxWith$1.L$4 = receiveChannel4;
                        channelsKt__Channels_commonKt$maxWith$1.L$5 = channelIterator;
                        channelsKt__Channels_commonKt$maxWith$1.L$6 = next;
                        channelsKt__Channels_commonKt$maxWith$1.label = 2;
                        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$maxWith$1);
                        if (hasNext != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        Object obj3 = hasNext;
                        receiveChannel2 = receiveChannel;
                        obj2 = next;
                        obj = obj3;
                        if (!((Boolean) obj).booleanValue()) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        }
                        return coroutine_suspended;
                    }
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    return obj2;
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (((Boolean) obj).booleanValue()) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th3);
                    return null;
                }
                next = channelIterator2.next();
                th2 = th3;
                receiveChannel4 = receiveChannel5;
                channelIterator = channelIterator2;
                receiveChannel = receiveChannel2;
                channelsKt__Channels_commonKt$maxWith$1.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$maxWith$1.L$1 = comparator2;
                channelsKt__Channels_commonKt$maxWith$1.L$2 = receiveChannel;
                channelsKt__Channels_commonKt$maxWith$1.L$3 = th2;
                channelsKt__Channels_commonKt$maxWith$1.L$4 = receiveChannel4;
                channelsKt__Channels_commonKt$maxWith$1.L$5 = channelIterator;
                channelsKt__Channels_commonKt$maxWith$1.L$6 = next;
                channelsKt__Channels_commonKt$maxWith$1.label = 2;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$maxWith$1);
                if (hasNext != coroutine_suspended) {
                }
                return coroutine_suspended;
            }
        }
        channelsKt__Channels_commonKt$maxWith$1 = new ChannelsKt__Channels_commonKt$maxWith$1(continuation);
        obj = channelsKt__Channels_commonKt$maxWith$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$maxWith$1.label;
        if (i != 0) {
        }
        try {
            if (((Boolean) obj).booleanValue()) {
            }
        } catch (Throwable th6) {
            th = th6;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c3 A[SYNTHETIC, Splitter:B:36:0x00c3] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00eb A[Catch:{ all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00f8 A[Catch:{ all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002a  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R extends Comparable<? super R>> Object minBy(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, ? extends R> function1, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$minBy$1 channelsKt__Channels_commonKt$minBy$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        Function1<? super E, ? extends R> function12;
        Throwable th2;
        ChannelIterator channelIterator;
        Comparable comparable;
        Object obj2;
        ReceiveChannel<? extends E> receiveChannel3;
        Object hasNext;
        ChannelIterator channelIterator2;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        Function1<? super E, ? extends R> function13;
        ReceiveChannel<? extends E> receiveChannel6;
        Throwable th3;
        ReceiveChannel<? extends E> receiveChannel7 = receiveChannel;
        Continuation<? super E> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$minBy$1) {
            channelsKt__Channels_commonKt$minBy$1 = (ChannelsKt__Channels_commonKt$minBy$1) continuation2;
            if ((channelsKt__Channels_commonKt$minBy$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$minBy$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$minBy$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$minBy$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    Throwable th4 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$minBy$1.L$0 = receiveChannel7;
                        Function1<? super E, ? extends R> function14 = function1;
                        channelsKt__Channels_commonKt$minBy$1.L$1 = function14;
                        channelsKt__Channels_commonKt$minBy$1.L$2 = receiveChannel7;
                        channelsKt__Channels_commonKt$minBy$1.L$3 = th4;
                        channelsKt__Channels_commonKt$minBy$1.L$4 = receiveChannel7;
                        channelsKt__Channels_commonKt$minBy$1.L$5 = it;
                        channelsKt__Channels_commonKt$minBy$1.label = 1;
                        Object hasNext2 = it.hasNext(channelsKt__Channels_commonKt$minBy$1);
                        if (hasNext2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel5 = receiveChannel7;
                        function13 = function14;
                        receiveChannel6 = receiveChannel5;
                        th3 = th4;
                        channelIterator = it;
                        obj = hasNext2;
                        th2 = th3;
                        if (((Boolean) obj).booleanValue()) {
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator3 = (ChannelIterator) channelsKt__Channels_commonKt$minBy$1.L$5;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$minBy$1.L$4;
                    Throwable th6 = (Throwable) channelsKt__Channels_commonKt$minBy$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$minBy$1.L$2;
                    function13 = (Function1) channelsKt__Channels_commonKt$minBy$1.L$1;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$minBy$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        th3 = th6;
                        channelIterator = channelIterator3;
                        receiveChannel7 = receiveChannel8;
                        th2 = th3;
                        if (((Boolean) obj).booleanValue()) {
                            InlineMarker.finallyStart(3);
                            ChannelsKt.cancelConsumed(receiveChannel7, th2);
                            InlineMarker.finallyEnd(3);
                            return null;
                        }
                        Object next = channelIterator.next();
                        comparable = (Comparable) function13.invoke(next);
                        receiveChannel2 = receiveChannel5;
                        function12 = function13;
                        ReceiveChannel<? extends E> receiveChannel9 = receiveChannel6;
                        obj2 = next;
                        receiveChannel3 = receiveChannel9;
                        channelsKt__Channels_commonKt$minBy$1.L$0 = receiveChannel2;
                        channelsKt__Channels_commonKt$minBy$1.L$1 = function12;
                        channelsKt__Channels_commonKt$minBy$1.L$2 = receiveChannel7;
                        channelsKt__Channels_commonKt$minBy$1.L$3 = th2;
                        channelsKt__Channels_commonKt$minBy$1.L$4 = receiveChannel3;
                        channelsKt__Channels_commonKt$minBy$1.L$5 = channelIterator;
                        channelsKt__Channels_commonKt$minBy$1.L$6 = obj2;
                        channelsKt__Channels_commonKt$minBy$1.L$7 = comparable;
                        channelsKt__Channels_commonKt$minBy$1.label = 2;
                        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$minBy$1);
                        if (hasNext != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        channelIterator2 = channelIterator;
                        receiveChannel4 = receiveChannel3;
                        obj = hasNext;
                        return coroutine_suspended;
                    } catch (Throwable th7) {
                        th = th7;
                        receiveChannel7 = receiveChannel8;
                        throw th;
                    }
                } else if (i == 2) {
                    Comparable comparable2 = (Comparable) channelsKt__Channels_commonKt$minBy$1.L$7;
                    obj2 = channelsKt__Channels_commonKt$minBy$1.L$6;
                    ChannelIterator channelIterator4 = (ChannelIterator) channelsKt__Channels_commonKt$minBy$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$minBy$1.L$4;
                    th2 = (Throwable) channelsKt__Channels_commonKt$minBy$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel10 = (ReceiveChannel) channelsKt__Channels_commonKt$minBy$1.L$2;
                    function12 = (Function1) channelsKt__Channels_commonKt$minBy$1.L$1;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$minBy$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        channelIterator2 = channelIterator4;
                        comparable = comparable2;
                        receiveChannel7 = receiveChannel10;
                    } catch (Throwable th8) {
                        th = th8;
                        receiveChannel7 = receiveChannel10;
                        try {
                            throw th;
                        } catch (Throwable th9) {
                            Throwable th10 = th9;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel7, th);
                            InlineMarker.finallyEnd(1);
                            throw th10;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                Throwable th11 = th2;
                ChannelIterator channelIterator5 = channelIterator2;
                if (!((Boolean) obj).booleanValue()) {
                    Object next2 = channelIterator5.next();
                    Comparable comparable3 = (Comparable) function12.invoke(next2);
                    if (comparable.compareTo(comparable3) > 0) {
                        obj2 = next2;
                        comparable = comparable3;
                    }
                    receiveChannel3 = receiveChannel4;
                    channelIterator = channelIterator5;
                    th2 = th11;
                    channelsKt__Channels_commonKt$minBy$1.L$0 = receiveChannel2;
                    channelsKt__Channels_commonKt$minBy$1.L$1 = function12;
                    channelsKt__Channels_commonKt$minBy$1.L$2 = receiveChannel7;
                    channelsKt__Channels_commonKt$minBy$1.L$3 = th2;
                    channelsKt__Channels_commonKt$minBy$1.L$4 = receiveChannel3;
                    channelsKt__Channels_commonKt$minBy$1.L$5 = channelIterator;
                    channelsKt__Channels_commonKt$minBy$1.L$6 = obj2;
                    channelsKt__Channels_commonKt$minBy$1.L$7 = comparable;
                    channelsKt__Channels_commonKt$minBy$1.label = 2;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$minBy$1);
                    if (hasNext != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                }
                InlineMarker.finallyStart(2);
                ChannelsKt.cancelConsumed(receiveChannel7, th11);
                InlineMarker.finallyEnd(2);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$minBy$1 = new ChannelsKt__Channels_commonKt$minBy$1(continuation2);
        obj = channelsKt__Channels_commonKt$minBy$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$minBy$1.label;
        if (i != 0) {
        }
        Throwable th112 = th2;
        ChannelIterator channelIterator52 = channelIterator2;
        if (!((Boolean) obj).booleanValue()) {
            InlineMarker.finallyStart(2);
            ChannelsKt.cancelConsumed(receiveChannel7, th112);
            InlineMarker.finallyEnd(2);
        }
        InlineMarker.finallyStart(2);
        ChannelsKt.cancelConsumed(receiveChannel7, th112);
        InlineMarker.finallyEnd(2);
        return obj2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a1 A[SYNTHETIC, Splitter:B:30:0x00a1] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00cc A[Catch:{ all -> 0x006d }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object minWith(ReceiveChannel<? extends E> receiveChannel, Comparator<? super E> comparator, Continuation<? super E> continuation) {
        ChannelsKt__Channels_commonKt$minWith$1 channelsKt__Channels_commonKt$minWith$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel3;
        Comparator<? super E> comparator2;
        Throwable th2;
        ReceiveChannel<? extends E> receiveChannel4;
        Object next;
        Object hasNext;
        ReceiveChannel<? extends E> receiveChannel5;
        ChannelIterator channelIterator2;
        Throwable th3;
        if (continuation instanceof ChannelsKt__Channels_commonKt$minWith$1) {
            channelsKt__Channels_commonKt$minWith$1 = (ChannelsKt__Channels_commonKt$minWith$1) continuation;
            if ((channelsKt__Channels_commonKt$minWith$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$minWith$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$minWith$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$minWith$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    th3 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$minWith$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$minWith$1.L$1 = comparator;
                        channelsKt__Channels_commonKt$minWith$1.L$2 = receiveChannel;
                        channelsKt__Channels_commonKt$minWith$1.L$3 = th3;
                        channelsKt__Channels_commonKt$minWith$1.L$4 = receiveChannel;
                        channelsKt__Channels_commonKt$minWith$1.L$5 = it;
                        channelsKt__Channels_commonKt$minWith$1.label = 1;
                        Object hasNext2 = it.hasNext(channelsKt__Channels_commonKt$minWith$1);
                        if (hasNext2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel3 = receiveChannel;
                        comparator2 = comparator;
                        receiveChannel5 = receiveChannel3;
                        channelIterator2 = it;
                        obj = hasNext2;
                        receiveChannel2 = receiveChannel5;
                    } catch (Throwable th4) {
                        receiveChannel2 = receiveChannel;
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th5;
                        }
                    }
                } else if (i == 1) {
                    channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$minWith$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$minWith$1.L$4;
                    th3 = (Throwable) channelsKt__Channels_commonKt$minWith$1.L$3;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$minWith$1.L$2;
                    comparator2 = (Comparator) channelsKt__Channels_commonKt$minWith$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$minWith$1.L$0;
                    ResultKt.throwOnFailure(obj);
                } else if (i == 2) {
                    Object obj2 = channelsKt__Channels_commonKt$minWith$1.L$6;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$minWith$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$minWith$1.L$4;
                    th2 = (Throwable) channelsKt__Channels_commonKt$minWith$1.L$3;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$minWith$1.L$2;
                    comparator2 = (Comparator) channelsKt__Channels_commonKt$minWith$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$minWith$1.L$0;
                    ResultKt.throwOnFailure(obj);
                    if (!((Boolean) obj).booleanValue()) {
                        next = channelIterator.next();
                        if (comparator2.compare(obj2, next) <= 0) {
                            next = obj2;
                        }
                        receiveChannel = receiveChannel2;
                        channelsKt__Channels_commonKt$minWith$1.L$0 = receiveChannel3;
                        channelsKt__Channels_commonKt$minWith$1.L$1 = comparator2;
                        channelsKt__Channels_commonKt$minWith$1.L$2 = receiveChannel;
                        channelsKt__Channels_commonKt$minWith$1.L$3 = th2;
                        channelsKt__Channels_commonKt$minWith$1.L$4 = receiveChannel4;
                        channelsKt__Channels_commonKt$minWith$1.L$5 = channelIterator;
                        channelsKt__Channels_commonKt$minWith$1.L$6 = next;
                        channelsKt__Channels_commonKt$minWith$1.label = 2;
                        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$minWith$1);
                        if (hasNext != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        Object obj3 = hasNext;
                        receiveChannel2 = receiveChannel;
                        obj2 = next;
                        obj = obj3;
                        if (!((Boolean) obj).booleanValue()) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        }
                        return coroutine_suspended;
                    }
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                    return obj2;
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (((Boolean) obj).booleanValue()) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th3);
                    return null;
                }
                next = channelIterator2.next();
                th2 = th3;
                receiveChannel4 = receiveChannel5;
                channelIterator = channelIterator2;
                receiveChannel = receiveChannel2;
                channelsKt__Channels_commonKt$minWith$1.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$minWith$1.L$1 = comparator2;
                channelsKt__Channels_commonKt$minWith$1.L$2 = receiveChannel;
                channelsKt__Channels_commonKt$minWith$1.L$3 = th2;
                channelsKt__Channels_commonKt$minWith$1.L$4 = receiveChannel4;
                channelsKt__Channels_commonKt$minWith$1.L$5 = channelIterator;
                channelsKt__Channels_commonKt$minWith$1.L$6 = next;
                channelsKt__Channels_commonKt$minWith$1.label = 2;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$minWith$1);
                if (hasNext != coroutine_suspended) {
                }
                return coroutine_suspended;
            }
        }
        channelsKt__Channels_commonKt$minWith$1 = new ChannelsKt__Channels_commonKt$minWith$1(continuation);
        obj = channelsKt__Channels_commonKt$minWith$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$minWith$1.label;
        if (i != 0) {
        }
        try {
            if (((Boolean) obj).booleanValue()) {
            }
        } catch (Throwable th6) {
            th = th6;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x006a A[Catch:{ all -> 0x0074 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006b A[Catch:{ all -> 0x0074 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object none(ReceiveChannel<? extends E> receiveChannel, Continuation<? super Boolean> continuation) {
        ChannelsKt__Channels_commonKt$none$1 channelsKt__Channels_commonKt$none$1;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th2;
        Object obj;
        if (continuation instanceof ChannelsKt__Channels_commonKt$none$1) {
            channelsKt__Channels_commonKt$none$1 = (ChannelsKt__Channels_commonKt$none$1) continuation;
            if ((channelsKt__Channels_commonKt$none$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$none$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$none$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$none$1.label;
                boolean z = true;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    th2 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$none$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$none$1.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$none$1.L$2 = th2;
                        channelsKt__Channels_commonKt$none$1.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$none$1.label = 1;
                        obj = it.hasNext(channelsKt__Channels_commonKt$none$1);
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    ReceiveChannel receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$none$1.L$3;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$none$1.L$2;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$none$1.L$1;
                    ReceiveChannel receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$none$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        obj = obj2;
                        th2 = th4;
                        receiveChannel = receiveChannel2;
                    } catch (Throwable th5) {
                        th = th5;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (!((Boolean) obj).booleanValue()) {
                    z = false;
                }
                Boolean boxBoolean = Boxing.boxBoolean(z);
                ChannelsKt.cancelConsumed(receiveChannel, th2);
                return boxBoolean;
            }
        }
        channelsKt__Channels_commonKt$none$1 = new ChannelsKt__Channels_commonKt$none$1(continuation);
        Object obj22 = channelsKt__Channels_commonKt$none$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$none$1.label;
        boolean z2 = true;
        if (i != 0) {
        }
        if (!((Boolean) obj).booleanValue()) {
        }
        Boolean boxBoolean2 = Boxing.boxBoolean(z2);
        ChannelsKt.cancelConsumed(receiveChannel, th2);
        return boxBoolean2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085 A[Catch:{ all -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0092 A[Catch:{ all -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b6 A[SYNTHETIC, Splitter:B:34:0x00b6] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object none(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super Boolean> continuation) {
        ChannelsKt__Channels_commonKt$none$3 channelsKt__Channels_commonKt$none$3;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$none$3 channelsKt__Channels_commonKt$none$32;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$none$3) {
            channelsKt__Channels_commonKt$none$3 = (ChannelsKt__Channels_commonKt$none$3) continuation;
            if ((channelsKt__Channels_commonKt$none$3.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$none$3.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$none$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$none$3.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    try {
                        th2 = null;
                        channelsKt__Channels_commonKt$none$32 = channelsKt__Channels_commonKt$none$3;
                        obj2 = coroutine_suspended;
                        receiveChannel6 = receiveChannel;
                        receiveChannel2 = receiveChannel6;
                        receiveChannel5 = receiveChannel2;
                        channelIterator = receiveChannel.iterator();
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$none$3.L$6;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$none$3.L$5;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$none$3.L$4;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$none$3.L$3;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$none$3.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$none$3.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$none$3.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        th = th4;
                        function12 = function13;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$none$32 = channelsKt__Channels_commonKt$none$3;
                        receiveChannel2 = receiveChannel10;
                        if (((Boolean) obj3).booleanValue()) {
                            if (((Boolean) function12.invoke(channelIterator.next())).booleanValue()) {
                                Boolean boxBoolean = Boxing.boxBoolean(false);
                            } else {
                                receiveChannel6 = receiveChannel4;
                                obj2 = obj;
                                th2 = th;
                            }
                            Boolean boxBoolean2 = Boxing.boxBoolean(false);
                            InlineMarker.finallyStart(2);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(2);
                            return boxBoolean2;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return Boxing.boxBoolean(true);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$none$32.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$none$32.L$1 = function12;
                channelsKt__Channels_commonKt$none$32.L$2 = receiveChannel6;
                channelsKt__Channels_commonKt$none$32.L$3 = receiveChannel2;
                channelsKt__Channels_commonKt$none$32.L$4 = th2;
                channelsKt__Channels_commonKt$none$32.L$5 = receiveChannel5;
                channelsKt__Channels_commonKt$none$32.L$6 = channelIterator;
                channelsKt__Channels_commonKt$none$32.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$none$32);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                th = th2;
                obj = obj5;
                if (((Boolean) obj3).booleanValue()) {
                }
                Unit unit2 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return Boxing.boxBoolean(true);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$none$3 = new ChannelsKt__Channels_commonKt$none$3(continuation);
        Object obj32 = channelsKt__Channels_commonKt$none$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$none$3.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$none$32.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$none$32.L$1 = function12;
            channelsKt__Channels_commonKt$none$32.L$2 = receiveChannel6;
            channelsKt__Channels_commonKt$none$32.L$3 = receiveChannel2;
            channelsKt__Channels_commonKt$none$32.L$4 = th2;
            channelsKt__Channels_commonKt$none$32.L$5 = receiveChannel5;
            channelsKt__Channels_commonKt$none$32.L$6 = channelIterator;
            channelsKt__Channels_commonKt$none$32.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$none$32);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a1 A[Catch:{ all -> 0x0070 }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00cf A[Catch:{ all -> 0x0048 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00e3 A[SYNTHETIC, Splitter:B:44:0x00e3] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <S, E extends S> Object reduce(ReceiveChannel<? extends E> receiveChannel, Function2<? super S, ? super E, ? extends S> function2, Continuation<? super S> continuation) {
        ChannelsKt__Channels_commonKt$reduce$1 channelsKt__Channels_commonKt$reduce$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        ChannelIterator channelIterator;
        ReceiveChannel<? extends E> receiveChannel3;
        Function2<? super S, ? super E, ? extends S> function22;
        Throwable th2;
        ReceiveChannel<? extends E> receiveChannel4;
        Object invoke;
        Object hasNext;
        ReceiveChannel<? extends E> receiveChannel5;
        ChannelIterator channelIterator2;
        ReceiveChannel<? extends E> receiveChannel6;
        Function2<? super S, ? super E, ? extends S> function23;
        Throwable th3;
        if (continuation instanceof ChannelsKt__Channels_commonKt$reduce$1) {
            channelsKt__Channels_commonKt$reduce$1 = (ChannelsKt__Channels_commonKt$reduce$1) continuation;
            if ((channelsKt__Channels_commonKt$reduce$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$reduce$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$reduce$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$reduce$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    th3 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$reduce$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$reduce$1.L$1 = function2;
                        channelsKt__Channels_commonKt$reduce$1.L$2 = receiveChannel;
                        channelsKt__Channels_commonKt$reduce$1.L$3 = th3;
                        channelsKt__Channels_commonKt$reduce$1.L$4 = receiveChannel;
                        channelsKt__Channels_commonKt$reduce$1.L$5 = it;
                        channelsKt__Channels_commonKt$reduce$1.label = 1;
                        Object hasNext2 = it.hasNext(channelsKt__Channels_commonKt$reduce$1);
                        if (hasNext2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel6 = receiveChannel;
                        function23 = function2;
                        receiveChannel5 = receiveChannel6;
                        channelIterator2 = it;
                        obj = hasNext2;
                        receiveChannel2 = receiveChannel5;
                    } catch (Throwable th4) {
                        receiveChannel2 = receiveChannel;
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else if (i == 1) {
                    channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$reduce$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$reduce$1.L$4;
                    th3 = (Throwable) channelsKt__Channels_commonKt$reduce$1.L$3;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$reduce$1.L$2;
                    function23 = (Function2) channelsKt__Channels_commonKt$reduce$1.L$1;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$reduce$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Throwable th6) {
                        th = th6;
                    }
                } else if (i == 2) {
                    Object obj2 = channelsKt__Channels_commonKt$reduce$1.L$6;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$reduce$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$reduce$1.L$4;
                    th2 = (Throwable) channelsKt__Channels_commonKt$reduce$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$reduce$1.L$2;
                    function22 = (Function2) channelsKt__Channels_commonKt$reduce$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$reduce$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        if (!((Boolean) obj).booleanValue()) {
                            invoke = function22.invoke(obj2, channelIterator.next());
                            receiveChannel = receiveChannel7;
                            channelsKt__Channels_commonKt$reduce$1.L$0 = receiveChannel3;
                            channelsKt__Channels_commonKt$reduce$1.L$1 = function22;
                            channelsKt__Channels_commonKt$reduce$1.L$2 = receiveChannel;
                            channelsKt__Channels_commonKt$reduce$1.L$3 = th2;
                            channelsKt__Channels_commonKt$reduce$1.L$4 = receiveChannel4;
                            channelsKt__Channels_commonKt$reduce$1.L$5 = channelIterator;
                            channelsKt__Channels_commonKt$reduce$1.L$6 = invoke;
                            channelsKt__Channels_commonKt$reduce$1.label = 2;
                            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$reduce$1);
                            if (hasNext != coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Object obj3 = hasNext;
                            receiveChannel7 = receiveChannel;
                            obj2 = invoke;
                            obj = obj3;
                            if (!((Boolean) obj).booleanValue()) {
                                InlineMarker.finallyStart(2);
                                ChannelsKt.cancelConsumed(receiveChannel7, th2);
                                InlineMarker.finallyEnd(2);
                            }
                            return coroutine_suspended;
                        }
                        InlineMarker.finallyStart(2);
                        ChannelsKt.cancelConsumed(receiveChannel7, th2);
                        InlineMarker.finallyEnd(2);
                        return obj2;
                    } catch (Throwable th7) {
                        th = th7;
                        receiveChannel2 = receiveChannel7;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (!((Boolean) obj).booleanValue()) {
                    invoke = channelIterator2.next();
                    receiveChannel3 = receiveChannel6;
                    function22 = function23;
                    ReceiveChannel<? extends E> receiveChannel8 = receiveChannel5;
                    channelIterator = channelIterator2;
                    receiveChannel = receiveChannel2;
                    th2 = th3;
                    receiveChannel4 = receiveChannel8;
                    channelsKt__Channels_commonKt$reduce$1.L$0 = receiveChannel3;
                    channelsKt__Channels_commonKt$reduce$1.L$1 = function22;
                    channelsKt__Channels_commonKt$reduce$1.L$2 = receiveChannel;
                    channelsKt__Channels_commonKt$reduce$1.L$3 = th2;
                    channelsKt__Channels_commonKt$reduce$1.L$4 = receiveChannel4;
                    channelsKt__Channels_commonKt$reduce$1.L$5 = channelIterator;
                    channelsKt__Channels_commonKt$reduce$1.L$6 = invoke;
                    channelsKt__Channels_commonKt$reduce$1.label = 2;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$reduce$1);
                    if (hasNext != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                }
                throw new UnsupportedOperationException("Empty channel can't be reduced.");
            }
        }
        channelsKt__Channels_commonKt$reduce$1 = new ChannelsKt__Channels_commonKt$reduce$1(continuation);
        obj = channelsKt__Channels_commonKt$reduce$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$reduce$1.label;
        if (i != 0) {
        }
        if (!((Boolean) obj).booleanValue()) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a3 A[Catch:{ all -> 0x0072 }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00d3 A[Catch:{ all -> 0x004a }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00ed A[SYNTHETIC, Splitter:B:44:0x00ed] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <S, E extends S> Object reduceIndexed(ReceiveChannel<? extends E> receiveChannel, Function3<? super Integer, ? super S, ? super E, ? extends S> function3, Continuation<? super S> continuation) {
        ChannelsKt__Channels_commonKt$reduceIndexed$1 channelsKt__Channels_commonKt$reduceIndexed$1;
        Object obj;
        int i;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel2;
        int i2;
        ReceiveChannel<? extends E> receiveChannel3;
        Function3<? super Integer, ? super S, ? super E, ? extends S> function32;
        Throwable th2;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelIterator channelIterator;
        Object invoke;
        Object hasNext;
        ReceiveChannel<? extends E> receiveChannel5;
        ChannelIterator channelIterator2;
        ReceiveChannel<? extends E> receiveChannel6;
        Function3<? super Integer, ? super S, ? super E, ? extends S> function33;
        Throwable th3;
        if (continuation instanceof ChannelsKt__Channels_commonKt$reduceIndexed$1) {
            channelsKt__Channels_commonKt$reduceIndexed$1 = (ChannelsKt__Channels_commonKt$reduceIndexed$1) continuation;
            if ((channelsKt__Channels_commonKt$reduceIndexed$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$reduceIndexed$1.label -= Integer.MIN_VALUE;
                obj = channelsKt__Channels_commonKt$reduceIndexed$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$reduceIndexed$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    th3 = null;
                    try {
                        ChannelIterator it = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$reduceIndexed$1.L$0 = receiveChannel;
                        channelsKt__Channels_commonKt$reduceIndexed$1.L$1 = function3;
                        channelsKt__Channels_commonKt$reduceIndexed$1.L$2 = receiveChannel;
                        channelsKt__Channels_commonKt$reduceIndexed$1.L$3 = th3;
                        channelsKt__Channels_commonKt$reduceIndexed$1.L$4 = receiveChannel;
                        channelsKt__Channels_commonKt$reduceIndexed$1.L$5 = it;
                        channelsKt__Channels_commonKt$reduceIndexed$1.label = 1;
                        Object hasNext2 = it.hasNext(channelsKt__Channels_commonKt$reduceIndexed$1);
                        if (hasNext2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel6 = receiveChannel;
                        function33 = function3;
                        receiveChannel5 = receiveChannel6;
                        channelIterator2 = it;
                        obj = hasNext2;
                        receiveChannel2 = receiveChannel5;
                    } catch (Throwable th4) {
                        receiveChannel2 = receiveChannel;
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th5;
                        }
                    }
                } else if (i == 1) {
                    channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$reduceIndexed$1.L$5;
                    receiveChannel5 = (ReceiveChannel) channelsKt__Channels_commonKt$reduceIndexed$1.L$4;
                    th3 = (Throwable) channelsKt__Channels_commonKt$reduceIndexed$1.L$3;
                    receiveChannel2 = (ReceiveChannel) channelsKt__Channels_commonKt$reduceIndexed$1.L$2;
                    function33 = (Function3) channelsKt__Channels_commonKt$reduceIndexed$1.L$1;
                    receiveChannel6 = (ReceiveChannel) channelsKt__Channels_commonKt$reduceIndexed$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Throwable th6) {
                        th = th6;
                    }
                } else if (i == 2) {
                    Object obj2 = channelsKt__Channels_commonKt$reduceIndexed$1.L$6;
                    i2 = channelsKt__Channels_commonKt$reduceIndexed$1.I$0;
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$reduceIndexed$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$reduceIndexed$1.L$4;
                    th2 = (Throwable) channelsKt__Channels_commonKt$reduceIndexed$1.L$3;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$reduceIndexed$1.L$2;
                    function32 = (Function3) channelsKt__Channels_commonKt$reduceIndexed$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$reduceIndexed$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        if (!((Boolean) obj).booleanValue()) {
                            Integer boxInt = Boxing.boxInt(i2);
                            i2++;
                            invoke = function32.invoke(boxInt, obj2, channelIterator.next());
                            receiveChannel = receiveChannel7;
                            channelsKt__Channels_commonKt$reduceIndexed$1.L$0 = receiveChannel3;
                            channelsKt__Channels_commonKt$reduceIndexed$1.L$1 = function32;
                            channelsKt__Channels_commonKt$reduceIndexed$1.L$2 = receiveChannel;
                            channelsKt__Channels_commonKt$reduceIndexed$1.L$3 = th2;
                            channelsKt__Channels_commonKt$reduceIndexed$1.L$4 = receiveChannel4;
                            channelsKt__Channels_commonKt$reduceIndexed$1.L$5 = channelIterator;
                            channelsKt__Channels_commonKt$reduceIndexed$1.I$0 = i2;
                            channelsKt__Channels_commonKt$reduceIndexed$1.L$6 = invoke;
                            channelsKt__Channels_commonKt$reduceIndexed$1.label = 2;
                            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$reduceIndexed$1);
                            if (hasNext != coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Object obj3 = hasNext;
                            receiveChannel7 = receiveChannel;
                            obj2 = invoke;
                            obj = obj3;
                            if (!((Boolean) obj).booleanValue()) {
                                InlineMarker.finallyStart(2);
                                ChannelsKt.cancelConsumed(receiveChannel7, th2);
                                InlineMarker.finallyEnd(2);
                            }
                            return coroutine_suspended;
                        }
                        InlineMarker.finallyStart(2);
                        ChannelsKt.cancelConsumed(receiveChannel7, th2);
                        InlineMarker.finallyEnd(2);
                        return obj2;
                    } catch (Throwable th7) {
                        th = th7;
                        receiveChannel2 = receiveChannel7;
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                if (!((Boolean) obj).booleanValue()) {
                    invoke = channelIterator2.next();
                    function32 = function33;
                    receiveChannel3 = receiveChannel6;
                    th2 = th3;
                    channelIterator = channelIterator2;
                    receiveChannel = receiveChannel2;
                    receiveChannel4 = receiveChannel5;
                    i2 = 1;
                    channelsKt__Channels_commonKt$reduceIndexed$1.L$0 = receiveChannel3;
                    channelsKt__Channels_commonKt$reduceIndexed$1.L$1 = function32;
                    channelsKt__Channels_commonKt$reduceIndexed$1.L$2 = receiveChannel;
                    channelsKt__Channels_commonKt$reduceIndexed$1.L$3 = th2;
                    channelsKt__Channels_commonKt$reduceIndexed$1.L$4 = receiveChannel4;
                    channelsKt__Channels_commonKt$reduceIndexed$1.L$5 = channelIterator;
                    channelsKt__Channels_commonKt$reduceIndexed$1.I$0 = i2;
                    channelsKt__Channels_commonKt$reduceIndexed$1.L$6 = invoke;
                    channelsKt__Channels_commonKt$reduceIndexed$1.label = 2;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$reduceIndexed$1);
                    if (hasNext != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                }
                throw new UnsupportedOperationException("Empty channel can't be reduced.");
            }
        }
        channelsKt__Channels_commonKt$reduceIndexed$1 = new ChannelsKt__Channels_commonKt$reduceIndexed$1(continuation);
        obj = channelsKt__Channels_commonKt$reduceIndexed$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$reduceIndexed$1.label;
        if (i != 0) {
        }
        if (!((Boolean) obj).booleanValue()) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0096 A[Catch:{ all -> 0x00ce }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a4 A[Catch:{ all -> 0x00ce }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object sumBy(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Integer> function1, Continuation<? super Integer> continuation) {
        ChannelsKt__Channels_commonKt$sumBy$1 channelsKt__Channels_commonKt$sumBy$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        IntRef intRef;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$sumBy$1 channelsKt__Channels_commonKt$sumBy$12;
        Function1<? super E, Integer> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        IntRef intRef2;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$sumBy$1) {
            channelsKt__Channels_commonKt$sumBy$1 = (ChannelsKt__Channels_commonKt$sumBy$1) continuation;
            if ((channelsKt__Channels_commonKt$sumBy$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$sumBy$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$sumBy$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$sumBy$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    IntRef intRef3 = new IntRef();
                    intRef3.element = 0;
                    try {
                        intRef2 = intRef3;
                        obj2 = coroutine_suspended;
                        th2 = null;
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        function12 = function1;
                        receiveChannel3 = receiveChannel5;
                        channelIterator = receiveChannel.iterator();
                        channelsKt__Channels_commonKt$sumBy$12 = channelsKt__Channels_commonKt$sumBy$1;
                        receiveChannel2 = receiveChannel3;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$sumBy$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$sumBy$1.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$sumBy$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$sumBy$1.L$4;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$sumBy$1.L$3;
                    IntRef intRef4 = (IntRef) channelsKt__Channels_commonKt$sumBy$1.L$2;
                    Function1<? super E, Integer> function13 = (Function1) channelsKt__Channels_commonKt$sumBy$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$sumBy$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        intRef = intRef4;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$sumBy$12 = channelsKt__Channels_commonKt$sumBy$1;
                        receiveChannel2 = receiveChannel10;
                        Function1<? super E, Integer> function14 = function13;
                        th = th4;
                        function12 = function14;
                        if (!((Boolean) obj3).booleanValue()) {
                            intRef.element += ((Number) function12.invoke(channelIterator.next())).intValue();
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                            intRef2 = intRef;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return Boxing.boxInt(intRef.element);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$sumBy$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$sumBy$12.L$1 = function12;
                channelsKt__Channels_commonKt$sumBy$12.L$2 = intRef2;
                channelsKt__Channels_commonKt$sumBy$12.L$3 = receiveChannel6;
                channelsKt__Channels_commonKt$sumBy$12.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$sumBy$12.L$5 = th2;
                channelsKt__Channels_commonKt$sumBy$12.L$6 = receiveChannel5;
                channelsKt__Channels_commonKt$sumBy$12.L$7 = channelIterator;
                channelsKt__Channels_commonKt$sumBy$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$sumBy$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                intRef = intRef2;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return Boxing.boxInt(intRef.element);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$sumBy$1 = new ChannelsKt__Channels_commonKt$sumBy$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$sumBy$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$sumBy$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$sumBy$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$sumBy$12.L$1 = function12;
            channelsKt__Channels_commonKt$sumBy$12.L$2 = intRef2;
            channelsKt__Channels_commonKt$sumBy$12.L$3 = receiveChannel6;
            channelsKt__Channels_commonKt$sumBy$12.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$sumBy$12.L$5 = th2;
            channelsKt__Channels_commonKt$sumBy$12.L$6 = receiveChannel5;
            channelsKt__Channels_commonKt$sumBy$12.L$7 = channelIterator;
            channelsKt__Channels_commonKt$sumBy$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$sumBy$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a0 A[Catch:{ all -> 0x00d4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ad A[Catch:{ all -> 0x00d4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object sumByDouble(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Double> function1, Continuation<? super Double> continuation) {
        ChannelsKt__Channels_commonKt$sumByDouble$1 channelsKt__Channels_commonKt$sumByDouble$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ReceiveChannel<? extends E> receiveChannel3;
        DoubleRef doubleRef;
        Throwable th2;
        ChannelIterator channelIterator;
        Object obj;
        ChannelsKt__Channels_commonKt$sumByDouble$1 channelsKt__Channels_commonKt$sumByDouble$12;
        Function1<? super E, Double> function12;
        ReceiveChannel<? extends E> receiveChannel4;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object hasNext;
        Continuation<? super Double> continuation2 = continuation;
        if (continuation2 instanceof ChannelsKt__Channels_commonKt$sumByDouble$1) {
            channelsKt__Channels_commonKt$sumByDouble$1 = (ChannelsKt__Channels_commonKt$sumByDouble$1) continuation2;
            if ((channelsKt__Channels_commonKt$sumByDouble$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$sumByDouble$1.label -= Integer.MIN_VALUE;
                Object obj2 = channelsKt__Channels_commonKt$sumByDouble$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$sumByDouble$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj2);
                    DoubleRef doubleRef2 = new DoubleRef();
                    doubleRef2.element = 0.0d;
                    try {
                        doubleRef = doubleRef2;
                        channelsKt__Channels_commonKt$sumByDouble$12 = channelsKt__Channels_commonKt$sumByDouble$1;
                        obj = coroutine_suspended;
                        th2 = null;
                        channelIterator = receiveChannel.iterator();
                        receiveChannel6 = receiveChannel;
                        receiveChannel5 = receiveChannel6;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel4 = receiveChannel2;
                        function12 = function1;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel;
                        th = th;
                        throw th;
                    }
                } else if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) channelsKt__Channels_commonKt$sumByDouble$1.L$7;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$sumByDouble$1.L$6;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$sumByDouble$1.L$5;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$sumByDouble$1.L$4;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$sumByDouble$1.L$3;
                    DoubleRef doubleRef3 = (DoubleRef) channelsKt__Channels_commonKt$sumByDouble$1.L$2;
                    Function1<? super E, Double> function13 = (Function1) channelsKt__Channels_commonKt$sumByDouble$1.L$1;
                    receiveChannel3 = (ReceiveChannel) channelsKt__Channels_commonKt$sumByDouble$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj2);
                        Throwable th5 = th4;
                        channelsKt__Channels_commonKt$sumByDouble$12 = channelsKt__Channels_commonKt$sumByDouble$1;
                        receiveChannel5 = receiveChannel9;
                        channelIterator = channelIterator2;
                        receiveChannel4 = receiveChannel7;
                        function12 = function13;
                        doubleRef = doubleRef3;
                        th2 = th5;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        obj = coroutine_suspended;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj2).booleanValue()) {
                            doubleRef.element += ((Number) function12.invoke(channelIterator.next())).doubleValue();
                            receiveChannel6 = receiveChannel3;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        InlineMarker.finallyEnd(1);
                        return Boxing.boxDouble(doubleRef.element);
                    } catch (Throwable th6) {
                        th = th6;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th7) {
                            Throwable th8 = th7;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th8;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$sumByDouble$12.L$0 = receiveChannel6;
                channelsKt__Channels_commonKt$sumByDouble$12.L$1 = function12;
                channelsKt__Channels_commonKt$sumByDouble$12.L$2 = doubleRef;
                channelsKt__Channels_commonKt$sumByDouble$12.L$3 = receiveChannel5;
                channelsKt__Channels_commonKt$sumByDouble$12.L$4 = receiveChannel2;
                channelsKt__Channels_commonKt$sumByDouble$12.L$5 = th2;
                channelsKt__Channels_commonKt$sumByDouble$12.L$6 = receiveChannel4;
                channelsKt__Channels_commonKt$sumByDouble$12.L$7 = channelIterator;
                channelsKt__Channels_commonKt$sumByDouble$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$sumByDouble$12);
                if (hasNext != obj) {
                    return obj;
                }
                Object obj3 = hasNext;
                receiveChannel3 = receiveChannel6;
                obj2 = obj3;
                if (!((Boolean) obj2).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th2);
                InlineMarker.finallyEnd(1);
                return Boxing.boxDouble(doubleRef.element);
                return obj;
            }
        }
        channelsKt__Channels_commonKt$sumByDouble$1 = new ChannelsKt__Channels_commonKt$sumByDouble$1(continuation2);
        Object obj22 = channelsKt__Channels_commonKt$sumByDouble$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$sumByDouble$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$sumByDouble$12.L$0 = receiveChannel6;
            channelsKt__Channels_commonKt$sumByDouble$12.L$1 = function12;
            channelsKt__Channels_commonKt$sumByDouble$12.L$2 = doubleRef;
            channelsKt__Channels_commonKt$sumByDouble$12.L$3 = receiveChannel5;
            channelsKt__Channels_commonKt$sumByDouble$12.L$4 = receiveChannel2;
            channelsKt__Channels_commonKt$sumByDouble$12.L$5 = th2;
            channelsKt__Channels_commonKt$sumByDouble$12.L$6 = receiveChannel4;
            channelsKt__Channels_commonKt$sumByDouble$12.L$7 = channelIterator;
            channelsKt__Channels_commonKt$sumByDouble$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$sumByDouble$12);
            if (hasNext != obj) {
            }
            return obj;
        } catch (Throwable th9) {
            th = th9;
            th = th;
            throw th;
        }
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> ReceiveChannel<E> requireNoNulls(ReceiveChannel<? extends E> receiveChannel) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$requireNoNulls");
        return map$default(receiveChannel, null, new ChannelsKt__Channels_commonKt$requireNoNulls$1(receiveChannel, null), 1, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x009f A[Catch:{ all -> 0x00dc }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ae A[Catch:{ all -> 0x00dc }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E> Object partition(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Boolean> function1, Continuation<? super Pair<? extends List<? extends E>, ? extends List<? extends E>>> continuation) {
        ChannelsKt__Channels_commonKt$partition$1 channelsKt__Channels_commonKt$partition$1;
        int i;
        ReceiveChannel<? extends E> receiveChannel2;
        ReceiveChannel<? extends E> receiveChannel3;
        ChannelIterator channelIterator;
        ArrayList arrayList;
        ArrayList arrayList2;
        Throwable th;
        Object obj;
        ReceiveChannel<? extends E> receiveChannel4;
        ChannelsKt__Channels_commonKt$partition$1 channelsKt__Channels_commonKt$partition$12;
        Function1<? super E, Boolean> function12;
        ReceiveChannel<? extends E> receiveChannel5;
        ReceiveChannel<? extends E> receiveChannel6;
        Object obj2;
        Throwable th2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        Object hasNext;
        if (continuation instanceof ChannelsKt__Channels_commonKt$partition$1) {
            channelsKt__Channels_commonKt$partition$1 = (ChannelsKt__Channels_commonKt$partition$1) continuation;
            if ((channelsKt__Channels_commonKt$partition$1.label & Integer.MIN_VALUE) != 0) {
                channelsKt__Channels_commonKt$partition$1.label -= Integer.MIN_VALUE;
                Object obj3 = channelsKt__Channels_commonKt$partition$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = channelsKt__Channels_commonKt$partition$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj3);
                    ArrayList arrayList5 = new ArrayList();
                    try {
                        arrayList4 = arrayList5;
                        arrayList3 = new ArrayList();
                        th2 = null;
                        receiveChannel6 = receiveChannel;
                        function12 = function1;
                        channelsKt__Channels_commonKt$partition$12 = channelsKt__Channels_commonKt$partition$1;
                        receiveChannel3 = receiveChannel6;
                        receiveChannel2 = receiveChannel3;
                        channelIterator = receiveChannel.iterator();
                        obj2 = coroutine_suspended;
                        receiveChannel5 = receiveChannel2;
                    } catch (Throwable th3) {
                        receiveChannel2 = receiveChannel;
                        th = th3;
                        throw th;
                    }
                } else if (i == 1) {
                    channelIterator = (ChannelIterator) channelsKt__Channels_commonKt$partition$1.L$8;
                    ReceiveChannel<? extends E> receiveChannel7 = (ReceiveChannel) channelsKt__Channels_commonKt$partition$1.L$7;
                    Throwable th4 = (Throwable) channelsKt__Channels_commonKt$partition$1.L$6;
                    ReceiveChannel<? extends E> receiveChannel8 = (ReceiveChannel) channelsKt__Channels_commonKt$partition$1.L$5;
                    receiveChannel4 = (ReceiveChannel) channelsKt__Channels_commonKt$partition$1.L$4;
                    ArrayList arrayList6 = (ArrayList) channelsKt__Channels_commonKt$partition$1.L$3;
                    ArrayList arrayList7 = (ArrayList) channelsKt__Channels_commonKt$partition$1.L$2;
                    Function1<? super E, Boolean> function13 = (Function1) channelsKt__Channels_commonKt$partition$1.L$1;
                    ReceiveChannel<? extends E> receiveChannel9 = (ReceiveChannel) channelsKt__Channels_commonKt$partition$1.L$0;
                    try {
                        ResultKt.throwOnFailure(obj3);
                        Object obj4 = coroutine_suspended;
                        receiveChannel5 = receiveChannel7;
                        receiveChannel3 = receiveChannel9;
                        arrayList = arrayList7;
                        th = th4;
                        function12 = function13;
                        arrayList2 = arrayList6;
                        obj = obj4;
                        ReceiveChannel<? extends E> receiveChannel10 = receiveChannel8;
                        channelsKt__Channels_commonKt$partition$12 = channelsKt__Channels_commonKt$partition$1;
                        receiveChannel2 = receiveChannel10;
                        if (!((Boolean) obj3).booleanValue()) {
                            Object next = channelIterator.next();
                            if (((Boolean) function12.invoke(next)).booleanValue()) {
                                arrayList.add(next);
                            } else {
                                arrayList2.add(next);
                            }
                            receiveChannel6 = receiveChannel4;
                            obj2 = obj;
                            th2 = th;
                            arrayList3 = arrayList2;
                            arrayList4 = arrayList;
                        }
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        InlineMarker.finallyEnd(1);
                        return new Pair(arrayList, arrayList2);
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel2 = receiveChannel8;
                        try {
                            throw th;
                        } catch (Throwable th6) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            InlineMarker.finallyEnd(1);
                            throw th6;
                        }
                    }
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelsKt__Channels_commonKt$partition$12.L$0 = receiveChannel3;
                channelsKt__Channels_commonKt$partition$12.L$1 = function12;
                channelsKt__Channels_commonKt$partition$12.L$2 = arrayList4;
                channelsKt__Channels_commonKt$partition$12.L$3 = arrayList3;
                channelsKt__Channels_commonKt$partition$12.L$4 = receiveChannel6;
                channelsKt__Channels_commonKt$partition$12.L$5 = receiveChannel2;
                channelsKt__Channels_commonKt$partition$12.L$6 = th2;
                channelsKt__Channels_commonKt$partition$12.L$7 = receiveChannel5;
                channelsKt__Channels_commonKt$partition$12.L$8 = channelIterator;
                channelsKt__Channels_commonKt$partition$12.label = 1;
                hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$partition$12);
                if (hasNext != obj2) {
                    return obj2;
                }
                Object obj5 = obj2;
                receiveChannel4 = receiveChannel6;
                obj3 = hasNext;
                arrayList = arrayList4;
                arrayList2 = arrayList3;
                th = th2;
                obj = obj5;
                if (!((Boolean) obj3).booleanValue()) {
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit22 = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel2, th);
                InlineMarker.finallyEnd(1);
                return new Pair(arrayList, arrayList2);
                return obj2;
            }
        }
        channelsKt__Channels_commonKt$partition$1 = new ChannelsKt__Channels_commonKt$partition$1(continuation);
        Object obj32 = channelsKt__Channels_commonKt$partition$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = channelsKt__Channels_commonKt$partition$1.label;
        if (i != 0) {
        }
        try {
            channelsKt__Channels_commonKt$partition$12.L$0 = receiveChannel3;
            channelsKt__Channels_commonKt$partition$12.L$1 = function12;
            channelsKt__Channels_commonKt$partition$12.L$2 = arrayList4;
            channelsKt__Channels_commonKt$partition$12.L$3 = arrayList3;
            channelsKt__Channels_commonKt$partition$12.L$4 = receiveChannel6;
            channelsKt__Channels_commonKt$partition$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$partition$12.L$6 = th2;
            channelsKt__Channels_commonKt$partition$12.L$7 = receiveChannel5;
            channelsKt__Channels_commonKt$partition$12.L$8 = channelIterator;
            channelsKt__Channels_commonKt$partition$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$partition$12);
            if (hasNext != obj2) {
            }
            return obj2;
        } catch (Throwable th7) {
            th = th7;
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0050, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0051, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005a, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object partition$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                if (((Boolean) function1.invoke(next)).booleanValue()) {
                    arrayList.add(next);
                } else {
                    arrayList2.add(next);
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return new Pair(arrayList, arrayList2);
            }
        }
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R> ReceiveChannel<Pair<E, R>> zip(ReceiveChannel<? extends E> receiveChannel, ReceiveChannel<? extends R> receiveChannel2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$zip");
        Intrinsics.checkParameterIsNotNull(receiveChannel2, "other");
        return zip$default(receiveChannel, receiveChannel2, null, ChannelsKt__Channels_commonKt$zip$1.INSTANCE, 2, null);
    }

    public static /* synthetic */ ReceiveChannel zip$default(ReceiveChannel receiveChannel, ReceiveChannel receiveChannel2, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.zip(receiveChannel, receiveChannel2, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    public static final <E, R, V> ReceiveChannel<V> zip(ReceiveChannel<? extends E> receiveChannel, ReceiveChannel<? extends R> receiveChannel2, CoroutineContext coroutineContext, Function2<? super E, ? super R, ? extends V> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$this$zip");
        Intrinsics.checkParameterIsNotNull(receiveChannel2, "other");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, ChannelsKt.consumesAll(receiveChannel, receiveChannel2), new ChannelsKt__Channels_commonKt$zip$2(receiveChannel, receiveChannel2, function2, null), 2, null);
    }

    private static final Object consumeEach$$forInline(BroadcastChannel broadcastChannel, Function1 function1, Continuation continuation) {
        ReceiveChannel openSubscription = broadcastChannel.openSubscription();
        try {
            ChannelIterator it = openSubscription.iterator();
            while (true) {
                InlineMarker.mark(0);
                Object hasNext = it.hasNext(continuation);
                InlineMarker.mark(1);
                if (!((Boolean) hasNext).booleanValue()) {
                    return Unit.INSTANCE;
                }
                function1.invoke(it.next());
            }
        } finally {
            InlineMarker.finallyStart(1);
            DefaultImpls.cancel$default(openSubscription, (CancellationException) null, 1, (Object) null);
            InlineMarker.finallyEnd(1);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0031, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r4, r5);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003b, code lost:
        throw r6;
     */
    private static final Object consumeEach$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                function1.invoke(it.next());
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return unit;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003c, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r9);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        throw r10;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object consumeEachIndexed$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                int i2 = i + 1;
                function1.invoke(new IndexedValue(i, it.next()));
                i = i2;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Unit.INSTANCE;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r8 = r9.invoke(java.lang.Integer.valueOf(r8));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004e, code lost:
        r9 = 2;
        kotlin.jvm.internal.InlineMarker.finallyStart(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0055, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0056, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005f, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object elementAtOrElse$$forInline(ReceiveChannel receiveChannel, int i, Function1 function1, Continuation continuation) {
        Object invoke;
        int i2;
        Throwable th = null;
        if (i >= 0) {
            ChannelIterator it = receiveChannel.iterator();
            int i3 = 0;
            while (true) {
                InlineMarker.mark(0);
                Object hasNext = it.hasNext(continuation);
                InlineMarker.mark(1);
                if (!((Boolean) hasNext).booleanValue()) {
                    break;
                }
                Object next = it.next();
                int i4 = i3 + 1;
                if (i == i3) {
                    InlineMarker.finallyStart(3);
                    ChannelsKt.cancelConsumed(receiveChannel, th);
                    InlineMarker.finallyEnd(3);
                    return next;
                }
                i3 = i4;
            }
        } else {
            invoke = function1.invoke(Integer.valueOf(i));
            i2 = 4;
            InlineMarker.finallyStart(4);
        }
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(i2);
        return invoke;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0047, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0050, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object find$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Object next;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        do {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                next = it.next();
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return null;
            }
        } while (!((Boolean) function1.invoke(next)).booleanValue());
        InlineMarker.finallyStart(2);
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(2);
        return next;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003c, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0046, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object findLast$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Object obj = null;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                if (((Boolean) function1.invoke(next)).booleanValue()) {
                    obj = next;
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return obj;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004e, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0057, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object first$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Object next;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        do {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                next = it.next();
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                throw new NoSuchElementException("ReceiveChannel contains no element matching the predicate.");
            }
        } while (!((Boolean) function1.invoke(next)).booleanValue());
        InlineMarker.finallyStart(2);
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(2);
        return next;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0045, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0046, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object firstOrNull$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Object next;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        do {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                next = it.next();
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return null;
            }
        } while (!((Boolean) function1.invoke(next)).booleanValue());
        InlineMarker.finallyStart(2);
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(2);
        return next;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0051, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005b, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object indexOfFirst$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (!((Boolean) hasNext).booleanValue()) {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Integer.valueOf(-1);
            } else if (((Boolean) function1.invoke(it.next())).booleanValue()) {
                Integer valueOf = Integer.valueOf(i);
                InlineMarker.finallyStart(2);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(2);
                return valueOf;
            } else {
                i++;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0043, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0044, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004d, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object indexOfLast$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = -1;
        int i2 = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                if (((Boolean) function1.invoke(it.next())).booleanValue()) {
                    i = i2;
                }
                i2++;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Integer.valueOf(i);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004a, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004b, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r9);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0054, code lost:
        throw r10;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object last$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Object obj = null;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        boolean z = false;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (!((Boolean) hasNext).booleanValue()) {
                break;
            }
            Object next = it.next();
            if (((Boolean) function1.invoke(next)).booleanValue()) {
                z = true;
                obj = next;
            }
        }
        Unit unit = Unit.INSTANCE;
        InlineMarker.finallyStart(1);
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(1);
        if (z) {
            return obj;
        }
        throw new NoSuchElementException("ReceiveChannel contains no element matching the predicate.");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003c, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0046, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object lastOrNull$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Object obj = null;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                if (((Boolean) function1.invoke(next)).booleanValue()) {
                    obj = next;
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return obj;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0056, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0057, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r9);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0060, code lost:
        throw r10;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object single$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Object obj = null;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        boolean z = false;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                if (((Boolean) function1.invoke(next)).booleanValue()) {
                    if (!z) {
                        z = true;
                        obj = next;
                    } else {
                        throw new IllegalArgumentException("ReceiveChannel contains more than one matching element.");
                    }
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                if (z) {
                    return obj;
                }
                throw new NoSuchElementException("ReceiveChannel contains no element matching the predicate.");
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r10 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0040, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r9, r1);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0049, code lost:
        if (r5 != false) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004b, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004c, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004f, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0050, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r9, r10);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0059, code lost:
        throw r11;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object singleOrNull$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        Object obj = null;
        boolean z = false;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (!((Boolean) hasNext).booleanValue()) {
                break;
            }
            Object next = it.next();
            if (((Boolean) function1.invoke(next)).booleanValue()) {
                if (z) {
                    InlineMarker.finallyStart(2);
                    ChannelsKt.cancelConsumed(receiveChannel, th);
                    InlineMarker.finallyEnd(2);
                    return null;
                }
                z = true;
                obj = next;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0052, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0053, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r9);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005c, code lost:
        throw r10;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object filterIndexedTo$$forInline(ReceiveChannel receiveChannel, Collection collection, Function2 function2, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                int i2 = i + 1;
                IndexedValue indexedValue = new IndexedValue(i, it.next());
                int component1 = indexedValue.component1();
                Object component2 = indexedValue.component2();
                if (((Boolean) function2.invoke(Integer.valueOf(component1), component2)).booleanValue()) {
                    collection.add(component2);
                }
                i = i2;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return collection;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x005c, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r9);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0066, code lost:
        throw r10;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object filterIndexedTo$$forInline(ReceiveChannel receiveChannel, SendChannel sendChannel, Function2 function2, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                int i2 = i + 1;
                IndexedValue indexedValue = new IndexedValue(i, it.next());
                int component1 = indexedValue.component1();
                Object component2 = indexedValue.component2();
                if (((Boolean) function2.invoke(Integer.valueOf(component1), component2)).booleanValue()) {
                    InlineMarker.mark(0);
                    sendChannel.send(component2, continuation);
                    InlineMarker.mark(2);
                    InlineMarker.mark(1);
                }
                i = i2;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return sendChannel;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003d, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003e, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0047, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object filterNotTo$$forInline(ReceiveChannel receiveChannel, Collection collection, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                if (!((Boolean) function1.invoke(next)).booleanValue()) {
                    collection.add(next);
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return collection;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0051, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object filterNotTo$$forInline(ReceiveChannel receiveChannel, SendChannel sendChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                if (!((Boolean) function1.invoke(next)).booleanValue()) {
                    InlineMarker.mark(0);
                    sendChannel.send(next, continuation);
                    InlineMarker.mark(2);
                    InlineMarker.mark(1);
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return sendChannel;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003d, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003e, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0047, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object filterTo$$forInline(ReceiveChannel receiveChannel, Collection collection, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                if (((Boolean) function1.invoke(next)).booleanValue()) {
                    collection.add(next);
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return collection;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0051, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object filterTo$$forInline(ReceiveChannel receiveChannel, SendChannel sendChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                if (((Boolean) function1.invoke(next)).booleanValue()) {
                    InlineMarker.mark(0);
                    sendChannel.send(next, continuation);
                    InlineMarker.mark(2);
                    InlineMarker.mark(1);
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return sendChannel;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0035, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0036, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003f, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object associateByTo$$forInline(ReceiveChannel receiveChannel, Map map, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                map.put(function1.invoke(next), next);
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return map;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0039, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003a, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0043, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object associateByTo$$forInline(ReceiveChannel receiveChannel, Map map, Function1 function1, Function1 function12, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                map.put(function1.invoke(next), function12.invoke(next));
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return map;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003f, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0040, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0049, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object associateTo$$forInline(ReceiveChannel receiveChannel, Map map, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Pair pair = (Pair) function1.invoke(it.next());
                map.put(pair.getFirst(), pair.getSecond());
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return map;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0045, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0046, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004f, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object groupByTo$$forInline(ReceiveChannel receiveChannel, Map map, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                Object invoke = function1.invoke(next);
                Object obj = map.get(invoke);
                if (obj == null) {
                    obj = new ArrayList();
                    map.put(invoke, obj);
                }
                ((List) obj).add(next);
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return map;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0049, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0053, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object groupByTo$$forInline(ReceiveChannel receiveChannel, Map map, Function1 function1, Function1 function12, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object next = it.next();
                Object invoke = function1.invoke(next);
                Object obj = map.get(invoke);
                if (obj == null) {
                    obj = new ArrayList();
                    map.put(invoke, obj);
                }
                ((List) obj).add(function12.invoke(next));
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return map;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004c, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r9);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0056, code lost:
        throw r10;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object mapIndexedNotNullTo$$forInline(ReceiveChannel receiveChannel, Collection collection, Function2 function2, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                int i2 = i + 1;
                IndexedValue indexedValue = new IndexedValue(i, it.next());
                int component1 = indexedValue.component1();
                Object invoke = function2.invoke(Integer.valueOf(component1), indexedValue.component2());
                if (invoke != null) {
                    collection.add(invoke);
                }
                i = i2;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return collection;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0056, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0057, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r9);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0060, code lost:
        throw r10;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object mapIndexedNotNullTo$$forInline(ReceiveChannel receiveChannel, SendChannel sendChannel, Function2 function2, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                int i2 = i + 1;
                IndexedValue indexedValue = new IndexedValue(i, it.next());
                int component1 = indexedValue.component1();
                Object invoke = function2.invoke(Integer.valueOf(component1), indexedValue.component2());
                if (invoke != null) {
                    InlineMarker.mark(0);
                    sendChannel.send(invoke, continuation);
                    InlineMarker.mark(2);
                    InlineMarker.mark(1);
                }
                i = i2;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return sendChannel;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003d, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003e, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object mapIndexedTo$$forInline(ReceiveChannel receiveChannel, Collection collection, Function2 function2, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                int i2 = i + 1;
                collection.add(function2.invoke(Integer.valueOf(i), it.next()));
                i = i2;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return collection;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0047, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0048, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0051, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object mapIndexedTo$$forInline(ReceiveChannel receiveChannel, SendChannel sendChannel, Function2 function2, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                int i2 = i + 1;
                Object invoke = function2.invoke(Integer.valueOf(i), it.next());
                InlineMarker.mark(0);
                sendChannel.send(invoke, continuation);
                InlineMarker.mark(2);
                InlineMarker.mark(1);
                i = i2;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return sendChannel;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0037, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0038, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r4, r5);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0041, code lost:
        throw r6;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object mapNotNullTo$$forInline(ReceiveChannel receiveChannel, Collection collection, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object invoke = function1.invoke(it.next());
                if (invoke != null) {
                    collection.add(invoke);
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return collection;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0041, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0042, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object mapNotNullTo$$forInline(ReceiveChannel receiveChannel, SendChannel sendChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object invoke = function1.invoke(it.next());
                if (invoke != null) {
                    InlineMarker.mark(0);
                    sendChannel.send(invoke, continuation);
                    InlineMarker.mark(2);
                    InlineMarker.mark(1);
                }
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return sendChannel;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0035, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0036, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r4, r5);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003f, code lost:
        throw r6;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object mapTo$$forInline(ReceiveChannel receiveChannel, Collection collection, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                collection.add(function1.invoke(it.next()));
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return collection;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003f, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0040, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0049, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object mapTo$$forInline(ReceiveChannel receiveChannel, SendChannel sendChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                Object invoke = function1.invoke(it.next());
                InlineMarker.mark(0);
                sendChannel.send(invoke, continuation);
                InlineMarker.mark(2);
                InlineMarker.mark(1);
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return sendChannel;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004c, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0056, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object all$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        do {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (!((Boolean) hasNext).booleanValue()) {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Boolean.valueOf(true);
            }
        } while (((Boolean) function1.invoke(it.next())).booleanValue());
        Boolean valueOf = Boolean.valueOf(false);
        InlineMarker.finallyStart(2);
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(2);
        return valueOf;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004c, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0056, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object any$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        do {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (!((Boolean) hasNext).booleanValue()) {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Boolean.valueOf(false);
            }
        } while (!((Boolean) function1.invoke(it.next())).booleanValue());
        Boolean valueOf = Boolean.valueOf(true);
        InlineMarker.finallyStart(2);
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(2);
        return valueOf;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0041, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0042, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object count$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (!((Boolean) hasNext).booleanValue()) {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Integer.valueOf(i);
            } else if (((Boolean) function1.invoke(it.next())).booleanValue()) {
                i++;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0032, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0033, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r4, r5);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003c, code lost:
        throw r6;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object fold$$forInline(ReceiveChannel receiveChannel, Object obj, Function2 function2, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                obj = function2.invoke(obj, it.next());
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return obj;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003a, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003b, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0044, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object foldIndexed$$forInline(ReceiveChannel receiveChannel, Object obj, Function3 function3, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                int i2 = i + 1;
                obj = function3.invoke(Integer.valueOf(i), obj, it.next());
                i = i2;
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return obj;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005e, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r9, r10);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0067, code lost:
        throw r11;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object maxBy$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        int i;
        Object obj = null;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        InlineMarker.mark(0);
        Object hasNext = it.hasNext(continuation);
        InlineMarker.mark(1);
        if (!((Boolean) hasNext).booleanValue()) {
            i = 3;
            InlineMarker.finallyStart(3);
        } else {
            obj = it.next();
            Comparable comparable = (Comparable) function1.invoke(obj);
            while (true) {
                InlineMarker.mark(0);
                Object hasNext2 = it.hasNext(continuation);
                InlineMarker.mark(1);
                if (!((Boolean) hasNext2).booleanValue()) {
                    break;
                }
                Object next = it.next();
                Comparable comparable2 = (Comparable) function1.invoke(next);
                if (comparable.compareTo(comparable2) < 0) {
                    obj = next;
                    comparable = comparable2;
                }
            }
            i = 2;
            InlineMarker.finallyStart(2);
        }
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(i);
        return obj;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005e, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r9, r10);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0067, code lost:
        throw r11;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object minBy$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        int i;
        Object obj = null;
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        InlineMarker.mark(0);
        Object hasNext = it.hasNext(continuation);
        InlineMarker.mark(1);
        if (!((Boolean) hasNext).booleanValue()) {
            i = 3;
            InlineMarker.finallyStart(3);
        } else {
            obj = it.next();
            Comparable comparable = (Comparable) function1.invoke(obj);
            while (true) {
                InlineMarker.mark(0);
                Object hasNext2 = it.hasNext(continuation);
                InlineMarker.mark(1);
                if (!((Boolean) hasNext2).booleanValue()) {
                    break;
                }
                Object next = it.next();
                Comparable comparable2 = (Comparable) function1.invoke(next);
                if (comparable.compareTo(comparable2) > 0) {
                    obj = next;
                    comparable = comparable2;
                }
            }
            i = 2;
            InlineMarker.finallyStart(2);
        }
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(i);
        return obj;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004c, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004d, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r5, r6);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0056, code lost:
        throw r7;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object none$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        do {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (!((Boolean) hasNext).booleanValue()) {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Boolean.valueOf(true);
            }
        } while (!((Boolean) function1.invoke(it.next())).booleanValue());
        Boolean valueOf = Boolean.valueOf(false);
        InlineMarker.finallyStart(2);
        ChannelsKt.cancelConsumed(receiveChannel, th);
        InlineMarker.finallyEnd(2);
        return valueOf;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0051, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0052, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005b, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object reduce$$forInline(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        InlineMarker.mark(0);
        Object hasNext = it.hasNext(continuation);
        InlineMarker.mark(1);
        if (((Boolean) hasNext).booleanValue()) {
            Object next = it.next();
            while (true) {
                InlineMarker.mark(0);
                Object hasNext2 = it.hasNext(continuation);
                InlineMarker.mark(1);
                if (((Boolean) hasNext2).booleanValue()) {
                    next = function2.invoke(next, it.next());
                } else {
                    InlineMarker.finallyStart(2);
                    ChannelsKt.cancelConsumed(receiveChannel, th);
                    InlineMarker.finallyEnd(2);
                    return next;
                }
            }
        } else {
            throw new UnsupportedOperationException("Empty channel can't be reduced.");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0058, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0059, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r8, r9);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0062, code lost:
        throw r10;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object reduceIndexed$$forInline(ReceiveChannel receiveChannel, Function3 function3, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        InlineMarker.mark(0);
        Object hasNext = it.hasNext(continuation);
        InlineMarker.mark(1);
        if (((Boolean) hasNext).booleanValue()) {
            Object next = it.next();
            int i = 1;
            while (true) {
                InlineMarker.mark(0);
                Object hasNext2 = it.hasNext(continuation);
                InlineMarker.mark(1);
                if (((Boolean) hasNext2).booleanValue()) {
                    Integer valueOf = Integer.valueOf(i);
                    i++;
                    next = function3.invoke(valueOf, next, it.next());
                } else {
                    InlineMarker.finallyStart(2);
                    ChannelsKt.cancelConsumed(receiveChannel, th);
                    InlineMarker.finallyEnd(2);
                    return next;
                }
            }
        } else {
            throw new UnsupportedOperationException("Empty channel can't be reduced.");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003e, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003f, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r7);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0048, code lost:
        throw r8;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object sumBy$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        int i = 0;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                i += ((Number) function1.invoke(it.next())).intValue();
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Integer.valueOf(i);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003f, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0040, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0049, code lost:
        throw r9;
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Channel operators are deprecated in favour of Flow and will be removed in 1.4")
    private static final Object sumByDouble$$forInline(ReceiveChannel receiveChannel, Function1 function1, Continuation continuation) {
        Throwable th = null;
        ChannelIterator it = receiveChannel.iterator();
        double d = 0.0d;
        while (true) {
            InlineMarker.mark(0);
            Object hasNext = it.hasNext(continuation);
            InlineMarker.mark(1);
            if (((Boolean) hasNext).booleanValue()) {
                d += ((Number) function1.invoke(it.next())).doubleValue();
            } else {
                Unit unit = Unit.INSTANCE;
                InlineMarker.finallyStart(1);
                ChannelsKt.cancelConsumed(receiveChannel, th);
                InlineMarker.finallyEnd(1);
                return Double.valueOf(d);
            }
        }
    }
}
