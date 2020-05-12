package com.worklight.common.security;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.wlclient.WLRequestListener;
import com.worklight.wlclient.api.DeviceDisplayNameListener;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.auth.WLAuthorizationManagerInternal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONException;
import org.json.JSONObject;

public class WLDeviceAuthManager extends WLCertManager {
    private static final String CLIENT_REGISTRATION_DATA_KEY = "com.worklight.oauth.application.data";
    private static String DEVICE_ID_ALIAS = "WLDeviceID";
    private static String KEYSTORE_FILENAME = ".keystore";
    private static String MARSHMALLOW_CONSTANT_MAC_ID = "02:00:00:00:00:00";
    private static WLDeviceAuthManager instance;
    private static char[] keyStorePassword;
    private String deviceDisplayName;
    private String deviceUuid;

    public static synchronized WLDeviceAuthManager getInstance() {
        WLDeviceAuthManager wLDeviceAuthManager;
        synchronized (WLDeviceAuthManager.class) {
            if (instance == null) {
                instance = new WLDeviceAuthManager();
            }
            wLDeviceAuthManager = instance;
        }
        return wLDeviceAuthManager;
    }

    protected WLDeviceAuthManager() {
        super(KEYSTORE_FILENAME, keyStorePassword);
    }

    /* access modifiers changed from: protected */
    public String getAlias(String str) {
        String str2 = "getAlias";
        Logger.enter(getClass().getSimpleName(), str2);
        if (str.equals("application")) {
            Logger.exit(getClass().getSimpleName(), str2);
            StringBuilder sb = new StringBuilder();
            sb.append("app:");
            sb.append(WLConfig.getInstance().getAppId());
            return sb.toString();
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return str;
    }

    private String generateDeviceID() {
        String str = "generateDeviceID";
        Logger.enter(getClass().getSimpleName(), str);
        String macAddress = this.context.getPackageManager().hasSystemFeature("android.hardware.wifi") ? ((WifiManager) this.context.getSystemService("wifi")).getConnectionInfo().getMacAddress() : null;
        String string = Secure.getString(this.context.getContentResolver(), "android_id");
        if ("strong".equalsIgnoreCase(WLConfig.getInstance().getWlGenerateDeviceIdStrong())) {
            if (macAddress == null || MARSHMALLOW_CONSTANT_MAC_ID.equals(macAddress)) {
                Log.d("<#<#>#>", "=============");
                if (VERSION.SDK_INT >= 9) {
                    String str2 = Build.SERIAL;
                    if (str2 != null) {
                        Log.d("<~<~>~>", "=~=~=~=~=~=~");
                        StringBuilder sb = new StringBuilder();
                        sb.append(string);
                        sb.append(str2);
                        string = sb.toString();
                    } else {
                        Log.d("<!<!>!>", "=!=!=!=!=!=!=!=");
                        try {
                            String deviceId = ((TelephonyManager) this.context.getSystemService("phone")).getDeviceId();
                            if (deviceId != null) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(string);
                                sb2.append(deviceId);
                                string = sb2.toString();
                            }
                        } catch (Exception e) {
                            Log.e("<o<o>o>", "o!o!o!o!o!o!o!o");
                            Logger logger = logger;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Device UUID could not be generated since IMEI access requires permissions in the AndroidManifest.xml ");
                            sb3.append(e.getMessage());
                            logger.error(sb3.toString(), (Throwable) e);
                        }
                    }
                }
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(string);
                sb4.append(macAddress);
                string = sb4.toString();
            }
        } else if (macAddress != null) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(string);
            sb5.append(macAddress);
            string = sb5.toString();
        }
        String uuid = UUID.nameUUIDFromBytes(string.getBytes()).toString();
        this.deviceUuid = uuid;
        saveDeviceUUID(DEVICE_ID_ALIAS, uuid);
        Logger.exit(getClass().getSimpleName(), str);
        return this.deviceUuid;
    }

    public String getDeviceUUID(Context context) {
        SecretKeyEntry secretKeyEntry;
        String str = "getDeviceUUID";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.context == null) {
            this.context = context;
        }
        if (this.deviceUuid == null) {
            try {
                secretKeyEntry = (SecretKeyEntry) getSecretKeyEntry(DEVICE_ID_ALIAS);
            } catch (Exception unused) {
                secretKeyEntry = null;
            }
            if (secretKeyEntry == null) {
                this.deviceUuid = generateDeviceID();
            } else {
                this.deviceUuid = new String(secretKeyEntry.getSecretKey().getEncoded());
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.deviceUuid;
    }

    public String regenerateDeviceID(Context context) {
        String str = "regenerateDeviceID";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.context == null) {
            this.context = context;
        }
        this.deviceUuid = generateDeviceID();
        WLConfig.getInstance().writeSecurityPref(CLIENT_REGISTRATION_DATA_KEY, "");
        Logger.exit(getClass().getSimpleName(), str);
        return this.deviceUuid;
    }

    public void overrideDeviceID(String str, Context context) {
        String str2 = "overrideDeviceID";
        Logger.enter(getClass().getSimpleName(), str2);
        if (this.context == null) {
            this.context = context;
        }
        saveDeviceUUID(DEVICE_ID_ALIAS, str);
        WLConfig.getInstance().writeSecurityPref(CLIENT_REGISTRATION_DATA_KEY, "");
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public String signJWS(JSONObject jSONObject, KeyPair keyPair, String str) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, SignatureException, JSONException {
        String str2 = "signJWS";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return signJWS(jSONObject, (RSAPublicKey) keyPair.getPublic(), keyPair.getPrivate(), str);
    }

    public JSONObject getDeviceData() throws JSONException {
        String str = "getDeviceData";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(WLConfig.APP_ID, instance.getDeviceUUID(WLClient.getInstance().getContext()));
        jSONObject.put("hardware", Build.MODEL);
        StringBuilder sb = new StringBuilder();
        sb.append("android ");
        sb.append(VERSION.RELEASE);
        jSONObject.put("platform", sb.toString());
        String str2 = this.deviceDisplayName;
        if (str2 != null) {
            jSONObject.put("deviceDisplayName", str2);
        }
        Logger.exit(getClass().getSimpleName(), str);
        return jSONObject;
    }

    public void getDeviceDisplayName(final DeviceDisplayNameListener deviceDisplayNameListener) {
        String str = "getDeviceDisplayName";
        Logger.enter(getClass().getSimpleName(), str);
        WLAuthorizationManagerInternal.getInstance().invokeGetRegistrationDataRequest(new WLRequestListener() {
            public void onSuccess(WLResponse wLResponse) {
                String str = "onSuccess";
                Logger.enter(getClass().getSimpleName(), str);
                try {
                    deviceDisplayNameListener.onSuccess(wLResponse.getResponseJSON().getJSONObject("registration").getJSONObject("device").getString("deviceDisplayName"));
                } catch (Exception e) {
                    deviceDisplayNameListener.onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, e.getMessage(), wLResponse.getOptions()));
                }
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onFailure(WLFailResponse wLFailResponse) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                deviceDisplayNameListener.onFailure(wLFailResponse);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void setDeviceDisplayName(String str, WLRequestListener wLRequestListener) {
        String str2 = "setDeviceDisplayName";
        Logger.enter(getClass().getSimpleName(), str2);
        if (WLAuthorizationManagerInternal.getInstance().getClientId() != null) {
            this.deviceDisplayName = str;
            WLAuthorizationManagerInternal.getInstance().invokeRegistrationRequest(wLRequestListener);
            this.deviceDisplayName = null;
        } else {
            wLRequestListener.onFailure(new WLFailResponse(WLErrorCode.APPLICATION_NOT_REGISTERED, "set device friendly name is allowed only after regisration", null));
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x007c, code lost:
        if (r4 == null) goto L_0x00b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b5, code lost:
        if (r4 == null) goto L_0x00b8;
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b2 A[SYNTHETIC, Splitter:B:36:0x00b2] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00c7 A[SYNTHETIC, Splitter:B:45:0x00c7] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00cc A[SYNTHETIC, Splitter:B:49:0x00cc] */
    private SecretKeyEntry saveDeviceUUID(String str, String str2) {
        FileInputStream fileInputStream;
        SecretKeyEntry secretKeyEntry;
        FileOutputStream fileOutputStream;
        String str3 = "saveDeviceUUID";
        Logger.enter(getClass().getSimpleName(), str3);
        FileOutputStream fileOutputStream2 = null;
        try {
            KeyStore instance2 = KeyStore.getInstance(KeyStore.getDefaultType());
            StringBuilder sb = new StringBuilder();
            sb.append(this.context.getFilesDir().getAbsolutePath());
            sb.append("/");
            sb.append(KEYSTORE_FILENAME);
            File file = new File(sb.toString());
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                try {
                    instance2.load(fileInputStream, keyStorePassword);
                    fileInputStream.close();
                } catch (Exception e) {
                    e = e;
                    secretKeyEntry = null;
                }
            } else {
                instance2.load(null, keyStorePassword);
                fileInputStream = null;
            }
            secretKeyEntry = new SecretKeyEntry(new SecretKeySpec(str2.getBytes(), "DES"));
            try {
                instance2.setEntry(str, secretKeyEntry, new PasswordProtection(keyStorePassword));
                fileOutputStream = new FileOutputStream(file);
            } catch (Exception e2) {
                e = e2;
                try {
                    Logger logger = logger;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Device UUID could not be saved for authentication with ");
                    sb2.append(e.getMessage());
                    logger.error(sb2.toString(), (Throwable) e);
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (IOException unused) {
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (IOException unused2) {
                        }
                    }
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException unused3) {
                        }
                    }
                    throw th;
                }
            }
            try {
                instance2.store(fileOutputStream, keyStorePassword);
                try {
                    fileOutputStream.close();
                } catch (IOException unused4) {
                }
            } catch (Exception e3) {
                Exception exc = e3;
                fileOutputStream2 = fileOutputStream;
                e = exc;
                Logger logger2 = logger;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("Device UUID could not be saved for authentication with ");
                sb22.append(e.getMessage());
                logger2.error(sb22.toString(), (Throwable) e);
                if (fileOutputStream2 != null) {
                }
            } catch (Throwable th2) {
                fileOutputStream2 = fileOutputStream;
                th = th2;
                if (fileOutputStream2 != null) {
                }
                if (fileInputStream != null) {
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            secretKeyEntry = null;
            fileInputStream = null;
            Logger logger22 = logger;
            StringBuilder sb222 = new StringBuilder();
            sb222.append("Device UUID could not be saved for authentication with ");
            sb222.append(e.getMessage());
            logger22.error(sb222.toString(), (Throwable) e);
            if (fileOutputStream2 != null) {
            }
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            if (fileOutputStream2 != null) {
            }
            if (fileInputStream != null) {
            }
            throw th;
        }
        Logger.exit(getClass().getSimpleName(), str3);
        return secretKeyEntry;
    }

    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r7v2 */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r13v1, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r7v4, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r14v1 */
    /* JADX WARNING: type inference failed for: r13v2 */
    /* JADX WARNING: type inference failed for: r7v5 */
    /* JADX WARNING: type inference failed for: r14v2, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r13v3, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r13v4 */
    /* JADX WARNING: type inference failed for: r14v3 */
    /* JADX WARNING: type inference failed for: r13v5 */
    /* JADX WARNING: type inference failed for: r14v4, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r13v6 */
    /* JADX WARNING: type inference failed for: r13v7 */
    /* JADX WARNING: type inference failed for: r13v9, types: [java.io.OutputStream, java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r7v9, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r8v4 */
    /* JADX WARNING: type inference failed for: r7v10 */
    /* JADX WARNING: type inference failed for: r8v5 */
    /* JADX WARNING: type inference failed for: r8v6, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r8v7, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r13v10 */
    /* JADX WARNING: type inference failed for: r14v5 */
    /* JADX WARNING: type inference failed for: r13v11 */
    /* JADX WARNING: type inference failed for: r13v12 */
    /* JADX WARNING: type inference failed for: r14v6 */
    /* JADX WARNING: type inference failed for: r14v7 */
    /* JADX WARNING: type inference failed for: r14v8 */
    /* JADX WARNING: type inference failed for: r13v13 */
    /* JADX WARNING: type inference failed for: r8v8 */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0091, code lost:
        if (r8 != 0) goto L_0x006d;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r13v2
  assigns: []
  uses: []
  mth insns count: 154
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
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0164  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0169  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0171  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x017a A[SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 14 */
    private Entry getSecretKeyEntry(String str) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, NameNotFoundException, UnrecoverableEntryException {
        ? r13;
        ? r7;
        ? r14;
        ? r132;
        ? r142;
        ? r133;
        ? r72;
        ? r8;
        String str2 = str;
        String str3 = "getSecretKeyEntry";
        Logger.enter(getClass().getSimpleName(), str3);
        KeyStore instance2 = KeyStore.getInstance(KeyStore.getDefaultType());
        StringBuilder sb = new StringBuilder();
        sb.append(this.context.getFilesDir().getAbsolutePath());
        String str4 = "/";
        sb.append(str4);
        sb.append(KEYSTORE_FILENAME);
        File file = new File(sb.toString());
        ? r73 = 0;
        if (file.exists()) {
            try {
                r8 = new FileInputStream(file);
                try {
                    instance2.load(r8, keyStorePassword);
                    SecretKeyEntry secretKeyEntry = (SecretKeyEntry) instance2.getEntry(str2, new PasswordProtection(keyStorePassword));
                    if (secretKeyEntry != null) {
                        Logger.exit(getClass().getSimpleName(), str3);
                        r8.close();
                        return secretKeyEntry;
                    }
                } catch (IOException e) {
                    e = e;
                    r8 = r8;
                    try {
                        Logger logger = logger;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to get device UUID from local keystore file with ");
                        sb2.append(e.getMessage());
                        logger.debug(sb2.toString(), (Throwable) e);
                    } catch (Throwable th) {
                        th = th;
                        r72 = r8;
                        if (r72 != 0) {
                            r72.close();
                        }
                        throw th;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                r8 = 0;
                Logger logger2 = logger;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("Failed to get device UUID from local keystore file with ");
                sb22.append(e.getMessage());
                logger2.debug(sb22.toString(), (Throwable) e);
            } catch (Throwable th2) {
                th = th2;
                r72 = r73;
                if (r72 != 0) {
                }
                throw th;
            }
            r8.close();
        } else {
            instance2.load(null, keyStorePassword);
        }
        String[] packagesForUid = this.context.getPackageManager().getPackagesForUid(this.context.getApplicationInfo().uid);
        KeyStore instance3 = KeyStore.getInstance(KeyStore.getDefaultType());
        int length = packagesForUid.length;
        int i = 0;
        int i2 = 0;
        ? r74 = r73;
        while (i2 < length) {
            String str5 = packagesForUid[i2];
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.context.createPackageContext(str5, i).getFilesDir().getAbsolutePath());
            sb3.append(str4);
            sb3.append(KEYSTORE_FILENAME);
            File file2 = new File(sb3.toString());
            if (file2.exists()) {
                try {
                    ? fileInputStream = new FileInputStream(file2);
                    try {
                        instance3.load(fileInputStream, keyStorePassword);
                        Entry entry = instance3.getEntry(str2, new PasswordProtection(keyStorePassword));
                        if (entry == null) {
                            fileInputStream.close();
                            fileInputStream.close();
                        } else {
                            ? fileOutputStream = new FileOutputStream(file);
                            try {
                                instance2.setEntry(str2, entry, new PasswordProtection(keyStorePassword));
                                instance2.store(fileOutputStream, keyStorePassword);
                                fileOutputStream.close();
                                fileInputStream.close();
                                Logger.exit(getClass().getSimpleName(), str3);
                                fileInputStream.close();
                                fileOutputStream.close();
                                return entry;
                            } catch (IOException e3) {
                                e = e3;
                                r142 = fileInputStream;
                                r133 = fileOutputStream;
                            }
                        }
                    } catch (IOException e4) {
                        e = e4;
                        r133 = r74;
                        r142 = fileInputStream;
                        try {
                            Logger logger3 = logger;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("Failed copying Device UUID from same sharedUserId applications file to local application keystore with ");
                            sb4.append(e.getMessage());
                            logger3.debug(sb4.toString(), (Throwable) e);
                            if (r142 != 0) {
                            }
                            if (r133 == 0) {
                            }
                            i2++;
                            i = 0;
                            r74 = 0;
                        } catch (Throwable th3) {
                            th = th3;
                            r14 = r142;
                            r132 = r133;
                            r7 = r14;
                            r13 = r132;
                            if (r7 != 0) {
                                r7.close();
                            }
                            if (r13 != 0) {
                                r13.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        r132 = r74;
                        r14 = fileInputStream;
                        r7 = r14;
                        r13 = r132;
                        if (r7 != 0) {
                        }
                        if (r13 != 0) {
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    e = e5;
                    ? r134 = r74;
                    r142 = r134;
                    r133 = r134;
                    Logger logger32 = logger;
                    StringBuilder sb42 = new StringBuilder();
                    sb42.append("Failed copying Device UUID from same sharedUserId applications file to local application keystore with ");
                    sb42.append(e.getMessage());
                    logger32.debug(sb42.toString(), (Throwable) e);
                    if (r142 != 0) {
                        r142.close();
                    }
                    if (r133 == 0) {
                        r133.close();
                    }
                    i2++;
                    i = 0;
                    r74 = 0;
                } catch (Throwable th5) {
                    th = th5;
                    r13 = r74;
                    r7 = r74;
                    if (r7 != 0) {
                    }
                    if (r13 != 0) {
                    }
                    throw th;
                }
            }
            i2++;
            i = 0;
            r74 = 0;
        }
        Logger.exit(getClass().getSimpleName(), str3);
        return null;
    }
}
