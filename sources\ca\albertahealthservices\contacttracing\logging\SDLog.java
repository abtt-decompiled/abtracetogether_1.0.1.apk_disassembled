package ca.albertahealthservices.contacttracing.logging;

import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0018\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\t\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u0010\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\u0004H\u0002J\u001f\u0010\u0017\u001a\u00020\u00182\u0012\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u001a\"\u00020\u0004¢\u0006\u0002\u0010\u001bJ\u001f\u0010\u001c\u001a\u00020\u00182\u0012\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u001a\"\u00020\u0004¢\u0006\u0002\u0010\u001bJ\b\u0010\u001d\u001a\u00020\nH\u0002J\u001f\u0010\u001e\u001a\u00020\u00182\u0012\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u001a\"\u00020\u0004¢\u0006\u0002\u0010\u001bJ%\u0010\u001f\u001a\u00020\u00182\u0006\u0010 \u001a\u00020\u00042\u000e\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u001aH\u0002¢\u0006\u0002\u0010!J\u001f\u0010\"\u001a\u00020\u00182\u0012\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u001a\"\u00020\u0004¢\u0006\u0002\u0010\u001bR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000e8BX\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lca/albertahealthservices/contacttracing/logging/SDLog;", "", "()V", "APP_NAME", "", "FOLDER", "buffer", "Ljava/lang/StringBuffer;", "cachedDateStamp", "cachedFileWriter", "Ljava/io/BufferedWriter;", "dateFormat", "Ljava/text/SimpleDateFormat;", "isWritable", "", "()Z", "lastWrite", "", "timestampFormat", "checkSDState", "", "createFileWriter", "dateStamp", "d", "", "message", "", "([Ljava/lang/String;)V", "e", "getFileWriter", "i", "log", "label", "(Ljava/lang/String;[Ljava/lang/String;)V", "w", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: SDLog.kt */
public final class SDLog {
    private static final String APP_NAME = "ContactTracing";
    private static final String FOLDER = "SDLogging";
    public static final SDLog INSTANCE = new SDLog();
    private static StringBuffer buffer = new StringBuffer();
    private static String cachedDateStamp = "";
    /* access modifiers changed from: private */
    public static BufferedWriter cachedFileWriter;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static long lastWrite;
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private SDLog() {
    }

    public static final /* synthetic */ BufferedWriter access$getCachedFileWriter$p(SDLog sDLog) {
        BufferedWriter bufferedWriter = cachedFileWriter;
        if (bufferedWriter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("cachedFileWriter");
        }
        return bufferedWriter;
    }

    private final boolean isWritable() {
        boolean[] checkSDState = checkSDState();
        return checkSDState[1] & checkSDState[0];
    }

    private final boolean[] checkSDState() {
        boolean z;
        boolean z2;
        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState != null) {
            int hashCode = externalStorageState.hashCode();
            if (hashCode != 1242932856) {
                if (hashCode == 1299749220 && externalStorageState.equals("mounted_ro")) {
                    z2 = true;
                    z = false;
                    return new boolean[]{z2, z};
                }
            } else if (externalStorageState.equals("mounted")) {
                z2 = true;
                z = z2;
                return new boolean[]{z2, z};
            }
        }
        z2 = false;
        z = z2;
        return new boolean[]{z2, z};
    }

    public final void i(String... strArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "message");
        log("INFO", strArr);
    }

    public final void w(String... strArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "message");
        log("WARN", strArr);
    }

    public final void d(String... strArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "message");
        log("DEBUG", strArr);
    }

    public final void e(String... strArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "message");
        log("ERROR", strArr);
    }

    private final BufferedWriter createFileWriter(String str) {
        StringBuilder sb = new StringBuilder();
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        Intrinsics.checkExpressionValueIsNotNull(externalStorageDirectory, "Environment.getExternalStorageDirectory()");
        sb.append(externalStorageDirectory.getAbsolutePath());
        sb.append("/");
        sb.append(FOLDER);
        File file = new File(sb.toString());
        file.mkdirs();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ContactTracing_");
        sb2.append(str);
        sb2.append(".txt");
        Writer fileWriter = new FileWriter(new File(file, sb2.toString()), true);
        return fileWriter instanceof BufferedWriter ? (BufferedWriter) fileWriter : new BufferedWriter(fileWriter, 8192);
    }

    private final BufferedWriter getFileWriter() {
        String format = dateFormat.format(new Date());
        String str = "cachedFileWriter";
        if (Intrinsics.areEqual((Object) format, (Object) cachedDateStamp)) {
            BufferedWriter bufferedWriter = cachedFileWriter;
            if (bufferedWriter != null) {
                return bufferedWriter;
            }
            Intrinsics.throwUninitializedPropertyAccessException(str);
            return bufferedWriter;
        }
        BufferedWriter bufferedWriter2 = cachedFileWriter;
        if (bufferedWriter2 != null) {
            if (bufferedWriter2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            bufferedWriter2.flush();
            BufferedWriter bufferedWriter3 = cachedFileWriter;
            if (bufferedWriter3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            bufferedWriter3.close();
        }
        Intrinsics.checkExpressionValueIsNotNull(format, "dateStamp");
        BufferedWriter createFileWriter = createFileWriter(format);
        cachedFileWriter = createFileWriter;
        cachedDateStamp = format;
        if (createFileWriter == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        return createFileWriter;
    }

    private final void log(String str, String[] strArr) {
        if (isWritable() && strArr != null) {
            String format = timestampFormat.format(new Date());
            String joinToString$default = ArraysKt.joinToString$default((Object[]) strArr, (CharSequence) " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
            StringBuffer stringBuffer = buffer;
            StringBuilder sb = new StringBuilder();
            sb.append(format);
            sb.append(' ');
            sb.append(str);
            sb.append(' ');
            sb.append(joinToString$default);
            sb.append(10);
            stringBuffer.append(sb.toString());
            try {
                BufferedWriter fileWriter = getFileWriter();
                fileWriter.write(buffer.toString());
                buffer = new StringBuffer();
                if (System.currentTimeMillis() - lastWrite > ((long) 10000)) {
                    fileWriter.flush();
                    lastWrite = System.currentTimeMillis();
                }
            } catch (IOException e) {
                StringBuffer stringBuffer2 = buffer;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(format);
                sb2.append(" ERROR SDLog ??? IOException while writing to SDLog: ");
                sb2.append(e.getMessage());
                sb2.append(10);
                stringBuffer2.append(sb2.toString());
            }
        }
    }
}
