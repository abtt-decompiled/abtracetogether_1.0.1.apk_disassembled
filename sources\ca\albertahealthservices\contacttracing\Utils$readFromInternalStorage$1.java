package ca.albertahealthservices.contacttracing;

import java.io.BufferedReader;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref.ObjectRef;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "invoke"}, k = 3, mv = {1, 1, 16})
/* compiled from: Utils.kt */
final class Utils$readFromInternalStorage$1 extends Lambda implements Function0<String> {
    final /* synthetic */ BufferedReader $bufferedReader;
    final /* synthetic */ ObjectRef $text;

    Utils$readFromInternalStorage$1(ObjectRef objectRef, BufferedReader bufferedReader) {
        this.$text = objectRef;
        this.$bufferedReader = bufferedReader;
        super(0);
    }

    public final String invoke() {
        this.$text.element = this.$bufferedReader.readLine();
        return (String) this.$text.element;
    }
}
