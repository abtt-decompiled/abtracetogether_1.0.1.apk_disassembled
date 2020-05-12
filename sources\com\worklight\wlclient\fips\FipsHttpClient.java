package com.worklight.wlclient.fips;

import android.content.Context;
import android.util.Log;
import com.worklight.common.Logger;
import com.worklight.nativeandroid.common.WLUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FipsHttpClient {
    public static String CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT";
    private static final String LOG_TAG;
    public static String REDIRECT = "REDIRECT";
    public static String SO_BUFFER_SIZE = "SO_BUFFER_SIZE";
    public static String SO_TIMEOUT = "SO_TIMEOUT";
    public static String STALE_CHECKING = "STALE_CHECKING";
    private static final String UNIMPLEMENTED_METHOD_MSG = "FipsHttpClient does not implement this method";
    private static Context ctx = null;
    private static final Logger logger;
    private Map mParams;
    private MicroVPNLib nativeLib;
    private long share;
    private int uvpn;

    static {
        Class<FipsHttpClient> cls = FipsHttpClient.class;
        logger = Logger.getInstance(cls.getName());
        LOG_TAG = cls.getSimpleName();
    }

    public static synchronized void setCtx(Context context) {
        synchronized (FipsHttpClient.class) {
            ctx = context;
        }
    }

    public void resetCookies() {
        String str = "resetCookies";
        Logger.enter(getClass().getSimpleName(), str);
        this.nativeLib.resetCookies();
        Logger.exit(getClass().getSimpleName(), str);
    }

    protected FipsHttpClient() {
        if (Thread.currentThread().getStackTrace()[2].toString().contains("FipsHttpClientTest")) {
            this.mParams = new HashMap();
            return;
        }
        throw new RuntimeException("This constructor is for unit tests only, and should not be called in production code!");
    }

    public FipsHttpClient(Map map) {
        WLUtils.loadLib(ctx, "libcrypto.so.1.0.0");
        WLUtils.loadLib(ctx, "libssl.so.1.0.0");
        MicroVPNLib microVPNLib = new MicroVPNLib();
        this.nativeLib = microVPNLib;
        if (microVPNLib.FIPSInit() == 1) {
            long curlInit = this.nativeLib.curlInit();
            this.share = curlInit;
            if (curlInit != -1) {
                if (map == null) {
                    map = new HashMap();
                    map.put(STALE_CHECKING, Boolean.valueOf(false));
                    map.put(CONNECTION_TIMEOUT, Integer.valueOf(60000));
                    map.put(SO_TIMEOUT, Integer.valueOf(60000));
                    map.put(SO_BUFFER_SIZE, Integer.valueOf(8192));
                    map.put(REDIRECT, Boolean.valueOf(false));
                }
                this.mParams = map;
                this.uvpn = 0;
                return;
            }
            throw new RuntimeException("HTTP client initialization failed.");
        }
        throw new RuntimeException("FIPS initialization failed.");
    }

    public String executeRaw(String str, String str2, String str3, String[] strArr) throws IOException {
        Class<FipsHttpClient> cls = FipsHttpClient.class;
        String str4 = "executeRaw";
        Logger.enter(getClass().getSimpleName(), str4);
        String name = cls.getName();
        StringBuilder sb = new StringBuilder();
        sb.append("entering executeRaw(),  url = ");
        sb.append(str);
        Log.d(name, sb.toString());
        String name2 = cls.getName();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("entering executeRaw(),  httpMethod = ");
        sb2.append(str2);
        Log.d(name2, sb2.toString());
        String name3 = cls.getName();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("entering executeRaw(),  headers = ");
        sb3.append(strArr);
        Log.d(name3, sb3.toString());
        String name4 = cls.getName();
        StringBuilder sb4 = new StringBuilder();
        sb4.append("entering executeRaw(),  body = ");
        sb4.append(str3);
        Log.d(name4, sb4.toString());
        logger.debug("entering executeRaw(), ");
        String[] strArr2 = new String[5];
        StringBuilder sb5 = new StringBuilder();
        sb5.append("redirect: ");
        sb5.append(this.mParams.get(REDIRECT));
        strArr2[0] = sb5.toString();
        if (((Integer) this.mParams.get(CONNECTION_TIMEOUT)).intValue() > 0) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("connectTO: ");
            sb6.append(this.mParams.get(CONNECTION_TIMEOUT));
            strArr2[1] = sb6.toString();
        } else {
            strArr2[1] = "connectTO: 30";
        }
        if (((Integer) this.mParams.get(SO_TIMEOUT)).intValue() > 0) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("socketTO: ");
            sb7.append(this.mParams.get(SO_TIMEOUT));
            strArr2[2] = sb7.toString();
        } else {
            strArr2[2] = "socketTO: 30";
        }
        if (((Integer) this.mParams.get(SO_BUFFER_SIZE)).intValue() > 0) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append("socketSize: ");
            sb8.append(this.mParams.get(SO_BUFFER_SIZE));
            strArr2[3] = sb8.toString();
        } else {
            strArr2[3] = "socketSize: 0";
        }
        StringBuilder sb9 = new StringBuilder();
        sb9.append("CAPath: ");
        sb9.append(WLUtils.getNoBackupFilesDir(ctx));
        strArr2[4] = sb9.toString();
        byte[] curlExecute = this.nativeLib.curlExecute(this.share, (long) this.uvpn, str, str2, str3, strArr, strArr2);
        Logger.exit(getClass().getSimpleName(), str4);
        return new String(curlExecute);
    }

    public String execute(HttpURLConnection httpURLConnection, String str) throws IOException {
        String str2 = "execute";
        Logger.enter(getClass().getSimpleName(), str2);
        String requestMethod = httpURLConnection.getRequestMethod();
        String url = httpURLConnection.getURL().toString();
        Map headerFields = httpURLConnection.getHeaderFields();
        String[] strArr = new String[headerFields.size()];
        int i = 0;
        for (String str3 : headerFields.keySet()) {
            List list = (List) headerFields.get(str3);
            for (int i2 = 0; i2 < list.size(); i2++) {
                Object obj = list.get(i2);
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(": ");
                sb.append(obj);
                strArr[i] = sb.toString();
                i++;
            }
        }
        String[] strArr2 = new String[5];
        StringBuilder sb2 = new StringBuilder();
        sb2.append("redirect: ");
        sb2.append(this.mParams.get(REDIRECT));
        strArr2[0] = sb2.toString();
        if (((Integer) this.mParams.get(CONNECTION_TIMEOUT)).intValue() > 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("connectTO: ");
            sb3.append(this.mParams.get(CONNECTION_TIMEOUT));
            strArr2[1] = sb3.toString();
        } else {
            strArr2[1] = "connectTO: 30";
        }
        if (((Integer) this.mParams.get(SO_TIMEOUT)).intValue() > 0) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("socketTO: ");
            sb4.append(this.mParams.get(SO_TIMEOUT));
            strArr2[2] = sb4.toString();
        } else {
            strArr2[2] = "socketTO: 30";
        }
        if (((Integer) this.mParams.get(SO_BUFFER_SIZE)).intValue() > 0) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("socketSize: ");
            sb5.append(this.mParams.get(SO_BUFFER_SIZE));
            strArr2[3] = sb5.toString();
        } else {
            strArr2[3] = "socketSize: 0";
        }
        StringBuilder sb6 = new StringBuilder();
        sb6.append("CAPath: ");
        sb6.append(WLUtils.getNoBackupFilesDir(ctx));
        strArr2[4] = sb6.toString();
        byte[] curlExecute = this.nativeLib.curlExecute(this.share, (long) this.uvpn, url, requestMethod, str, strArr, strArr2);
        Logger.exit(getClass().getSimpleName(), str2);
        return parseCurlResponse(curlExecute);
    }

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r2v2, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r3v2, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r3v3, types: [java.io.ByteArrayInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r2v5, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003d, code lost:
        if (r3 != 0) goto L_0x0030;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], java.lang.String, ?[OBJECT, ARRAY]]
  uses: [java.lang.String, ?[int, boolean, OBJECT, ARRAY, byte, short, char], java.io.InputStream]
  mth insns count: 36
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
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0050 A[SYNTHETIC, Splitter:B:24:0x0050] */
    /* JADX WARNING: Unknown variable types count: 4 */
    public String parseCurlResponse(byte[] bArr) throws IOException {
        ? r3;
        ? r32;
        String str = "parseCurlResponse";
        Logger.enter(getClass().getSimpleName(), str);
        StringBuffer stringBuffer = new StringBuffer();
        ? r2 = 0;
        try {
            r32 = new ByteArrayInputStream(bArr);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(r32));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                }
                r3 = r32;
                r2 = stringBuffer.toString();
            } catch (IOException e) {
                e = e;
                r32 = r32;
                try {
                    e.printStackTrace();
                } catch (Throwable th) {
                    th = th;
                    r2 = r3;
                    if (r2 != 0) {
                    }
                    throw th;
                }
            }
        } catch (IOException e2) {
            e = e2;
            r32 = 0;
            e.printStackTrace();
        } catch (Throwable th2) {
            th = th2;
            if (r2 != 0) {
                try {
                    r2.close();
                } catch (Exception unused) {
                }
            }
            throw th;
        }
        try {
            r3.close();
            r2 = r2;
        } catch (Exception unused2) {
            r2 = r2;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return r2;
    }
}
