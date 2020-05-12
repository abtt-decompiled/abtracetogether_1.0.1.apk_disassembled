package ca.albertahealthservices.contacttracing.protocol;

import ca.albertahealthservices.contacttracing.BuildConfig;
import ca.albertahealthservices.contacttracing.protocol.v2.BlueTraceV2;
import java.util.Map;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u0005J\u000e\u0010\f\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u0006J\u0010\u0010\u000f\u001a\u00020\u00102\b\u0010\r\u001a\u0004\u0018\u00010\u0005R\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001d\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\b¨\u0006\u0011"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/BlueTrace;", "", "()V", "characteristicToProtocolVersionMap", "", "Ljava/util/UUID;", "", "getCharacteristicToProtocolVersionMap", "()Ljava/util/Map;", "implementations", "Lca/albertahealthservices/contacttracing/protocol/BlueTraceProtocol;", "getImplementations", "getImplementation", "charUUID", "protocolVersion", "supportsCharUUID", "", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BlueTrace.kt */
public final class BlueTrace {
    public static final BlueTrace INSTANCE = new BlueTrace();
    private static final Map<UUID, Integer> characteristicToProtocolVersionMap;
    private static final Map<Integer, BlueTraceProtocol> implementations;

    static {
        UUID fromString = UUID.fromString(BuildConfig.V2_CHARACTERISTIC_ID);
        Integer valueOf = Integer.valueOf(2);
        characteristicToProtocolVersionMap = MapsKt.mapOf(TuplesKt.to(fromString, valueOf));
        implementations = MapsKt.mapOf(TuplesKt.to(valueOf, new BlueTraceV2()));
    }

    private BlueTrace() {
    }

    public final Map<UUID, Integer> getCharacteristicToProtocolVersionMap() {
        return characteristicToProtocolVersionMap;
    }

    public final Map<Integer, BlueTraceProtocol> getImplementations() {
        return implementations;
    }

    public final boolean supportsCharUUID(UUID uuid) {
        boolean z = false;
        if (uuid == null) {
            return false;
        }
        Integer num = (Integer) characteristicToProtocolVersionMap.get(uuid);
        if (num != null) {
            if (implementations.get(Integer.valueOf(num.intValue())) != null) {
                z = true;
            }
        }
        return z;
    }

    public final BlueTraceProtocol getImplementation(UUID uuid) {
        Intrinsics.checkParameterIsNotNull(uuid, "charUUID");
        Integer num = (Integer) characteristicToProtocolVersionMap.get(uuid);
        return getImplementation(num != null ? num.intValue() : 1);
    }

    public final BlueTraceProtocol getImplementation(int i) {
        BlueTraceProtocol blueTraceProtocol = (BlueTraceProtocol) implementations.get(Integer.valueOf(i));
        return blueTraceProtocol != null ? blueTraceProtocol : new BlueTraceV2();
    }
}
