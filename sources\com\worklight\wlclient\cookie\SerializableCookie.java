package com.worklight.wlclient.cookie;

import android.util.Log;
import com.worklight.common.Logger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import okhttp3.Cookie;
import okhttp3.Cookie.Builder;

class SerializableCookie implements Serializable {
    private static long NON_VALID_EXPIRES_AT = -1;
    private static final String TAG = SerializableCookie.class.getSimpleName();
    private static final long serialVersionUID = -8594045714036645534L;
    private transient Cookie cookie;

    SerializableCookie() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0057 A[SYNTHETIC, Splitter:B:18:0x0057] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0066 A[SYNTHETIC, Splitter:B:26:0x0066] */
    public String encode(Cookie cookie2) {
        ObjectOutputStream objectOutputStream;
        String str = "Stream not closed in encodeCookie";
        String str2 = "encode";
        Logger.enter(getClass().getSimpleName(), str2);
        this.cookie = cookie2;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream2 = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            try {
                objectOutputStream.writeObject(this);
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    Log.d(TAG, str, e);
                }
                Logger.exit(getClass().getSimpleName(), str2);
                return byteArrayToHexString(byteArrayOutputStream.toByteArray());
            } catch (IOException e2) {
                e = e2;
                try {
                    Log.d(TAG, "IOException in encodeCookie", e);
                    Logger.exit(getClass().getSimpleName(), str2);
                    if (objectOutputStream != null) {
                    }
                    return null;
                } catch (Throwable th) {
                    th = th;
                    objectOutputStream2 = objectOutputStream;
                    if (objectOutputStream2 != null) {
                        try {
                            objectOutputStream2.close();
                        } catch (IOException e3) {
                            Log.d(TAG, str, e3);
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e4) {
            e = e4;
            objectOutputStream = null;
            Log.d(TAG, "IOException in encodeCookie", e);
            Logger.exit(getClass().getSimpleName(), str2);
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e5) {
                    Log.d(TAG, str, e5);
                }
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            if (objectOutputStream2 != null) {
            }
            throw th;
        }
    }

    private static String byteArrayToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            byte b2 = b & 255;
            if (b2 < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(b2));
        }
        return sb.toString();
    }

    /* JADX WARNING: type inference failed for: r7v2 */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.io.ObjectInputStream] */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r7v5, types: [okhttp3.Cookie] */
    /* JADX WARNING: type inference failed for: r3v2, types: [java.io.ObjectInputStream] */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r3v4, types: [java.io.ObjectInputStream] */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v8, types: [java.io.ObjectInputStream] */
    /* JADX WARNING: type inference failed for: r7v12, types: [okhttp3.Cookie] */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: type inference failed for: r3v13 */
    /* JADX WARNING: type inference failed for: r3v14 */
    /* JADX WARNING: type inference failed for: r3v15 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v1
  assigns: []
  uses: []
  mth insns count: 54
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0044 A[SYNTHETIC, Splitter:B:18:0x0044] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0053 A[SYNTHETIC, Splitter:B:25:0x0053] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0065 A[SYNTHETIC, Splitter:B:31:0x0065] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x004a=Splitter:B:22:0x004a, B:15:0x003b=Splitter:B:15:0x003b} */
    /* JADX WARNING: Unknown variable types count: 7 */
    public Cookie decode(String str) {
        ? r3;
        ? r32;
        ? r33;
        ? r34;
        String str2 = "Stream not closed in decodeCookie";
        String str3 = "decode";
        Logger.enter(getClass().getSimpleName(), str3);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(hexStringToByteArray(str));
        ? r7 = 0;
        try {
            ? objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                ? r72 = ((SerializableCookie) objectInputStream.readObject()).cookie;
                try {
                    objectInputStream.close();
                    r7 = r72;
                } catch (IOException e) {
                    Log.d(TAG, str2, e);
                }
            } catch (IOException e2) {
                e = e2;
                r33 = objectInputStream;
                r32 = r33;
                Log.d(TAG, "IOException in decodeCookie", e);
                r32 = r33;
                if (r33 != 0) {
                    r33.close();
                }
                Logger.exit(getClass().getSimpleName(), str3);
                return r7;
            } catch (ClassNotFoundException e3) {
                e = e3;
                r34 = objectInputStream;
                try {
                    r32 = r34;
                    Log.d(TAG, "ClassNotFoundException in decodeCookie", e);
                    r32 = r34;
                    if (r34 != 0) {
                        r34.close();
                    }
                    Logger.exit(getClass().getSimpleName(), str3);
                    return r7;
                } catch (Throwable th) {
                    th = th;
                    r3 = r32;
                    if (r3 != 0) {
                        try {
                            r3.close();
                        } catch (IOException e4) {
                            Log.d(TAG, str2, e4);
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e5) {
            e = e5;
            r33 = r7;
            r32 = r33;
            Log.d(TAG, "IOException in decodeCookie", e);
            r32 = r33;
            if (r33 != 0) {
            }
            Logger.exit(getClass().getSimpleName(), str3);
            return r7;
        } catch (ClassNotFoundException e6) {
            e = e6;
            r34 = r7;
            r32 = r34;
            Log.d(TAG, "ClassNotFoundException in decodeCookie", e);
            r32 = r34;
            if (r34 != 0) {
            }
            Logger.exit(getClass().getSimpleName(), str3);
            return r7;
        } catch (Throwable th2) {
            r3 = r7;
            th = th2;
            if (r3 != 0) {
            }
            throw th;
        }
        Logger.exit(getClass().getSimpleName(), str3);
        return r7;
    }

    private static byte[] hexStringToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        String str = "writeObject";
        Logger.enter(getClass().getSimpleName(), str);
        objectOutputStream.writeObject(this.cookie.name());
        objectOutputStream.writeObject(this.cookie.value());
        objectOutputStream.writeLong(this.cookie.persistent() ? this.cookie.expiresAt() : NON_VALID_EXPIRES_AT);
        objectOutputStream.writeObject(this.cookie.domain());
        objectOutputStream.writeObject(this.cookie.path());
        objectOutputStream.writeBoolean(this.cookie.secure());
        objectOutputStream.writeBoolean(this.cookie.httpOnly());
        objectOutputStream.writeBoolean(this.cookie.hostOnly());
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String str = "readObject";
        Logger.enter(getClass().getSimpleName(), str);
        Builder builder = new Builder();
        builder.name((String) objectInputStream.readObject());
        builder.value((String) objectInputStream.readObject());
        long readLong = objectInputStream.readLong();
        if (readLong != NON_VALID_EXPIRES_AT) {
            builder.expiresAt(readLong);
        }
        String str2 = (String) objectInputStream.readObject();
        builder.domain(str2);
        builder.path((String) objectInputStream.readObject());
        if (objectInputStream.readBoolean()) {
            builder.secure();
        }
        if (objectInputStream.readBoolean()) {
            builder.httpOnly();
        }
        if (objectInputStream.readBoolean()) {
            builder.hostOnlyDomain(str2);
        }
        this.cookie = builder.build();
        Logger.exit(getClass().getSimpleName(), str);
    }
}
