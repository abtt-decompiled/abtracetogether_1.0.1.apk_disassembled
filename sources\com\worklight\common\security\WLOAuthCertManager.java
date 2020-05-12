package com.worklight.common.security;

import android.content.pm.PackageManager.NameNotFoundException;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.wlclient.api.WLClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import org.json.JSONException;
import org.json.JSONObject;

public class WLOAuthCertManager extends WLCertManager {
    private static String KEYSTORE_FILENAME = ".oauthkeystore";
    private static final String PROVISIONING_ENTITY_FOR_KEYS = "WLAuthorizationManagerProvisioningEntity";
    private static final int RSA_KEY_SIZE = 512;
    private static WLOAuthCertManager instance;
    private static char[] keyStorePassword;

    public static synchronized WLOAuthCertManager getInstance() {
        WLOAuthCertManager wLOAuthCertManager;
        synchronized (WLOAuthCertManager.class) {
            if (instance == null) {
                WLOAuthCertManager wLOAuthCertManager2 = new WLOAuthCertManager();
                instance = wLOAuthCertManager2;
                wLOAuthCertManager2.init(WLClient.getInstance().getContext());
            }
            wLOAuthCertManager = instance;
        }
        return wLOAuthCertManager;
    }

    protected WLOAuthCertManager() {
        super(KEYSTORE_FILENAME, keyStorePassword);
    }

    /* access modifiers changed from: protected */
    public String getAlias(String str) {
        String str2 = "getAlias";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return PROVISIONING_ENTITY_FOR_KEYS;
    }

    public String signJWS(JSONObject jSONObject, KeyPair keyPair, String str) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, SignatureException, JSONException {
        String str2 = "signJWS";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return signJWS(jSONObject, (RSAPublicKey) keyPair.getPublic(), keyPair.getPrivate(), str);
    }

    public void generateKeyPair() throws NoSuchAlgorithmException, IOException, UnrecoverableEntryException, KeyStoreException, ClassNotFoundException, NoSuchProviderException {
        String str = "generateKeyPair";
        Logger.enter(getClass().getSimpleName(), str);
        if (getKeyPair() == null) {
            generateKeyPair(null, 512);
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public KeyPair getKeyPair() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException, IOException, ClassNotFoundException {
        String str = "getKeyPair";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return getKeyPair(PROVISIONING_ENTITY_FOR_KEYS);
    }

    public String signJWS(JSONObject jSONObject, String str) throws Exception {
        String str2 = "signJWS";
        Logger.enter(getClass().getSimpleName(), str2);
        KeyPair keyPair = getKeyPair();
        if (keyPair != null) {
            Logger.exit(getClass().getSimpleName(), str2);
            return signJWS(jSONObject, keyPair, str);
        }
        Logger.exit(getClass().getSimpleName(), str2);
        throw new Exception("Not found keypair in the keystore");
    }

    public void deleteKeyPair() {
        String str = "deleteKeyPair";
        Logger.enter(getClass().getSimpleName(), str);
        String str2 = PROVISIONING_ENTITY_FOR_KEYS;
        this.keyPairHash.remove(getAlias(str2));
        WLConfig.getInstance().writeSecurityPref(getAlias(str2), null);
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* JADX WARNING: type inference failed for: r4v5 */
    /* JADX WARNING: type inference failed for: r4v6, types: [java.security.KeyStore$PrivateKeyEntry] */
    /* JADX WARNING: type inference failed for: r4v7, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r3v3, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r7v11, types: [java.security.KeyStore$PrivateKeyEntry] */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v5
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], ?[OBJECT, ARRAY]]
  uses: [java.security.KeyStore$PrivateKeyEntry, ?[int, boolean, OBJECT, ARRAY, byte, short, char], java.io.FileInputStream]
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
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x008b  */
    /* JADX WARNING: Unknown variable types count: 4 */
    public PrivateKeyEntry getPrivateKeyEntry(String str) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, NameNotFoundException, UnrecoverableEntryException {
        ? r3;
        String str2 = "getPrivateKeyEntry";
        Logger.enter(getClass().getSimpleName(), str2);
        KeyStore instance2 = KeyStore.getInstance(KeyStore.getDefaultType());
        StringBuilder sb = new StringBuilder();
        sb.append(this.context.getFilesDir().getAbsolutePath());
        sb.append("/");
        sb.append(KEYSTORE_FILENAME);
        File file = new File(sb.toString());
        String alias = getAlias(str);
        ? r4 = 0;
        if (file.exists()) {
            try {
                ? fileInputStream = new FileInputStream(file);
                try {
                    instance2.load(fileInputStream, keyStorePassword);
                    ? r7 = (PrivateKeyEntry) instance2.getEntry(alias, new PasswordProtection(keyStorePassword));
                    fileInputStream.close();
                    r4 = r7;
                } catch (IOException e) {
                    e = e;
                    r3 = fileInputStream;
                    try {
                        Logger logger = logger;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to determine the existence of certificate for client registration with ");
                        sb2.append(e.getMessage());
                        logger.error(sb2.toString(), (Throwable) e);
                        if (r3 != 0) {
                        }
                        Logger.exit(getClass().getSimpleName(), str2);
                        return r4;
                    } catch (Throwable th) {
                        th = th;
                        r4 = r3;
                        if (r4 != 0) {
                            r4.close();
                        }
                        throw th;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                r3 = 0;
                Logger logger2 = logger;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("Failed to determine the existence of certificate for client registration with ");
                sb22.append(e.getMessage());
                logger2.error(sb22.toString(), (Throwable) e);
                if (r3 != 0) {
                    r3.close();
                }
                Logger.exit(getClass().getSimpleName(), str2);
                return r4;
            } catch (Throwable th2) {
                th = th2;
                if (r4 != 0) {
                }
                throw th;
            }
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return r4;
    }
}
