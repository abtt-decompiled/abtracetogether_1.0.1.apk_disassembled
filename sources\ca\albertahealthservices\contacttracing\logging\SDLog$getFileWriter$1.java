package ca.albertahealthservices.contacttracing.logging;

import java.io.BufferedWriter;
import kotlin.Metadata;
import kotlin.jvm.internal.MutablePropertyReference0;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;

@Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 16})
/* compiled from: SDLog.kt */
final /* synthetic */ class SDLog$getFileWriter$1 extends MutablePropertyReference0 {
    SDLog$getFileWriter$1(SDLog sDLog) {
        super(sDLog);
    }

    public String getName() {
        return "cachedFileWriter";
    }

    public KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinClass(SDLog.class);
    }

    public String getSignature() {
        return "getCachedFileWriter()Ljava/io/BufferedWriter;";
    }

    public Object get() {
        return SDLog.access$getCachedFileWriter$p((SDLog) this.receiver);
    }

    public void set(Object obj) {
        SDLog.cachedFileWriter = (BufferedWriter) obj;
    }
}
