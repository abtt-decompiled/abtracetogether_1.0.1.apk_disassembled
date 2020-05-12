package com.worklight.common.security;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.util.Base64;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.utils.WLBase64;
import com.worklight.wlclient.WLRequest.RequestPaths;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class WLCertManager {
    protected static final String ALG = "alg";
    private static final String APPLICATION = "application";
    protected static final String E = "e";
    protected static final String JWK = "jwk";
    protected static final String KTY = "kty";
    protected static final String N = "n";
    protected static final String RSA = "RSA";
    protected static final String X5C = "x5c";
    private static boolean first = true;
    protected static Logger logger = Logger.getInstance("wl.certManager");
    private String KEYSTORE_FILENAME;
    protected Context context;
    protected HashMap<String, KeyPair> keyPairHash = new HashMap<>();
    private char[] keyStorePassword;

    /* access modifiers changed from: protected */
    public abstract String getAlias(String str);

    protected WLCertManager(String str, char[] cArr) {
        this.KEYSTORE_FILENAME = str;
        this.keyStorePassword = cArr;
    }

    public void init(Context context2) {
        String simpleName = getClass().getSimpleName();
        String str = RequestPaths.INIT;
        Logger.enter(simpleName, str);
        this.context = context2;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public KeyPair generateKeyPair(String str, int i) throws NoSuchAlgorithmException, IOException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator;
        String str2 = "generateKeyPair";
        Logger.enter(getClass().getSimpleName(), str2);
        int i2 = VERSION.SDK_INT;
        String str3 = RSA;
        if (i2 <= 17) {
            keyPairGenerator = KeyPairGenerator.getInstance(str3, "BC");
        } else {
            keyPairGenerator = KeyPairGenerator.getInstance(str3);
        }
        keyPairGenerator.initialize(i);
        KeyPair genKeyPair = keyPairGenerator.genKeyPair();
        if (genKeyPair != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(genKeyPair);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            int length = byteArray.length;
            WLConfig.getInstance().writeSecurityPref(getAlias(str), Base64.encodeToString(byteArray, 0));
            objectOutputStream.close();
            byteArrayOutputStream.close();
            this.keyPairHash.put(getAlias(str), genKeyPair);
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return genKeyPair;
    }

    private byte[] signCsrData(String str, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String str2 = "signCsrData";
        Logger.enter(getClass().getSimpleName(), str2);
        Signature instance = Signature.getInstance("SHA256withRSA");
        instance.initSign(privateKey);
        instance.update(str.getBytes());
        Logger.exit(getClass().getSimpleName(), str2);
        return instance.sign();
    }

    public String signCsr(JSONObject jSONObject, String str) throws Exception {
        String str2 = "signCsr";
        Logger.enter(getClass().getSimpleName(), str2);
        KeyPair keyPair = (KeyPair) this.keyPairHash.get(getAlias(str));
        RSAPublicKey rSAPublicKey = (RSAPublicKey) keyPair.getPublic();
        Logger.exit(getClass().getSimpleName(), str2);
        return signJWS(jSONObject, rSAPublicKey, keyPair.getPrivate(), null);
    }

    public String signJWS(JSONObject jSONObject, RSAPublicKey rSAPublicKey, PrivateKey privateKey, String str) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String str2 = "signJWS";
        Logger.enter(getClass().getSimpleName(), str2);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(ALG, "RS256");
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put(KTY, RSA);
        String str3 = "UTF-8";
        jSONObject3.put(N, WLBase64.encodeUrlSafe(rSAPublicKey.getModulus().toByteArray(), str3));
        jSONObject3.put(E, WLBase64.encodeUrlSafe(rSAPublicKey.getPublicExponent().toByteArray(), str3));
        if (str != null) {
            jSONObject3.put("kid", str);
        }
        jSONObject2.put(JWK, jSONObject3);
        String jSONObject4 = jSONObject2.toString();
        String jSONObject5 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(WLBase64.encodeUrlSafe(jSONObject4.getBytes(), str3));
        String str4 = ".";
        sb.append(str4);
        sb.append(WLBase64.encodeUrlSafe(jSONObject5.getBytes(), str3));
        String sb2 = sb.toString();
        String encodeUrlSafe = WLBase64.encodeUrlSafe(signCsrData(sb2, privateKey), str3);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(str4);
        sb3.append(encodeUrlSafe);
        String sb4 = sb3.toString();
        Logger.exit(getClass().getSimpleName(), str2);
        return sb4;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00f1 A[SYNTHETIC, Splitter:B:42:0x00f1] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0117 A[SYNTHETIC, Splitter:B:47:0x0117] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0133 A[SYNTHETIC, Splitter:B:55:0x0133] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0159 A[SYNTHETIC, Splitter:B:60:0x0159] */
    public void clearKeystore(String str) throws KeyStoreException {
        FileInputStream fileInputStream;
        Logger logger2;
        StringBuilder sb;
        String str2 = ": ";
        String str3 = "clearKeystore";
        Logger.enter(getClass().getSimpleName(), str3);
        KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.context.getFilesDir().getAbsolutePath());
        sb2.append("/");
        sb2.append(this.KEYSTORE_FILENAME);
        File file = new File(sb2.toString());
        FileOutputStream fileOutputStream = null;
        try {
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                instance.load(fileInputStream, this.keyStorePassword);
                fileInputStream.close();
            } else {
                instance.load(null, this.keyStorePassword);
                fileInputStream = null;
            }
            try {
                String alias = getAlias(str);
                if (instance.containsAlias(alias)) {
                    instance.deleteEntry(alias);
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                    try {
                        instance.store(fileOutputStream2, this.keyStorePassword);
                        fileOutputStream = fileOutputStream2;
                    } catch (Exception e) {
                        fileOutputStream = fileOutputStream2;
                        e = e;
                        try {
                            Logger logger3 = logger;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Error deleting the keystore containing the certificate ");
                            sb3.append(e.getMessage());
                            logger3.error(sb3.toString(), (Throwable) e);
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e2) {
                                    Logger logger4 = logger;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append(e2.getClass());
                                    sb4.append(str2);
                                    sb4.append(e2.getMessage());
                                    logger4.error(sb4.toString(), (Throwable) e2);
                                }
                            }
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e3) {
                                    e = e3;
                                    logger2 = logger;
                                    sb = new StringBuilder();
                                }
                            }
                            Logger.exit(getClass().getSimpleName(), str3);
                        } catch (Throwable th) {
                            th = th;
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e4) {
                                    Logger logger5 = logger;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append(e4.getClass());
                                    sb5.append(str2);
                                    sb5.append(e4.getMessage());
                                    logger5.error(sb5.toString(), (Throwable) e4);
                                }
                            }
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e5) {
                                    Logger logger6 = logger;
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(e5.getClass());
                                    sb6.append(str2);
                                    sb6.append(e5.getMessage());
                                    logger6.error(sb6.toString(), (Throwable) e5);
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        fileOutputStream = fileOutputStream2;
                        th = th2;
                        if (fileOutputStream != null) {
                        }
                        if (fileInputStream != null) {
                        }
                        throw th;
                    }
                }
                logger.debug("Key store cleared.");
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e6) {
                        Logger logger7 = logger;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(e6.getClass());
                        sb7.append(str2);
                        sb7.append(e6.getMessage());
                        logger7.error(sb7.toString(), (Throwable) e6);
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e7) {
                        e = e7;
                        logger2 = logger;
                        sb = new StringBuilder();
                    }
                }
            } catch (Exception e8) {
                e = e8;
            }
        } catch (Exception e9) {
            e = e9;
            fileInputStream = null;
            Logger logger32 = logger;
            StringBuilder sb32 = new StringBuilder();
            sb32.append("Error deleting the keystore containing the certificate ");
            sb32.append(e.getMessage());
            logger32.error(sb32.toString(), (Throwable) e);
            if (fileOutputStream != null) {
            }
            if (fileInputStream != null) {
            }
            Logger.exit(getClass().getSimpleName(), str3);
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            if (fileOutputStream != null) {
            }
            if (fileInputStream != null) {
            }
            throw th;
        }
        Logger.exit(getClass().getSimpleName(), str3);
        sb.append(e.getClass());
        sb.append(str2);
        sb.append(e.getMessage());
        logger2.error(sb.toString(), (Throwable) e);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0151, code lost:
        if (r8 != null) goto L_0x0112;
     */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x01b5 A[SYNTHETIC, Splitter:B:101:0x01b5] */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01c6 A[SYNTHETIC, Splitter:B:106:0x01c6] */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x01e5 A[SYNTHETIC, Splitter:B:115:0x01e5] */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x01f6 A[SYNTHETIC, Splitter:B:120:0x01f6] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0122 A[SYNTHETIC, Splitter:B:55:0x0122] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0135 A[Catch:{ Exception -> 0x012d, all -> 0x0126 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x013d A[SYNTHETIC, Splitter:B:70:0x013d] */
    public void removeEntityKeyStoreValues(String str) throws KeyStoreException {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        Throwable th;
        FileOutputStream fileOutputStream2;
        String[] packagesForUid;
        FileOutputStream fileOutputStream3;
        FileInputStream fileInputStream2;
        FileOutputStream fileOutputStream4;
        String str2 = str;
        String str3 = "removeEntityKeyStoreValues";
        Logger.enter(getClass().getSimpleName(), str3);
        KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
        StringBuilder sb = new StringBuilder();
        sb.append(this.context.getFilesDir().getAbsolutePath());
        String str4 = "/";
        sb.append(str4);
        sb.append(this.KEYSTORE_FILENAME);
        File file = new File(sb.toString());
        try {
            String alias = getAlias(str);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                try {
                    instance.load(fileInputStream, this.keyStorePassword);
                    if (instance.containsAlias(alias)) {
                        instance.deleteEntry(alias);
                        fileOutputStream2 = new FileOutputStream(file);
                        try {
                            instance.store(fileOutputStream2, this.keyStorePassword);
                        } catch (Exception e) {
                            e = e;
                            fileOutputStream = fileOutputStream2;
                            try {
                                Logger logger2 = logger;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Error deleting the keystore containing the certificate ");
                                sb2.append(e.getMessage());
                                logger2.error(sb2.toString(), (Throwable) e);
                                if (fileOutputStream != null) {
                                }
                                if (fileInputStream != null) {
                                }
                                Logger.exit(getClass().getSimpleName(), str3);
                            } catch (Throwable th2) {
                                th = th2;
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e2) {
                                        IOException iOException = e2;
                                        logger.error(iOException.getMessage(), (Throwable) iOException);
                                    }
                                }
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (IOException e3) {
                                        IOException iOException2 = e3;
                                        logger.error(iOException2.getMessage(), (Throwable) iOException2);
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fileOutputStream = fileOutputStream2;
                            if (fileOutputStream != null) {
                            }
                            if (fileInputStream != null) {
                            }
                            throw th;
                        }
                    } else {
                        fileOutputStream2 = null;
                    }
                    fileInputStream.close();
                } catch (Exception e4) {
                    e = e4;
                    fileOutputStream = null;
                    Logger logger22 = logger;
                    StringBuilder sb22 = new StringBuilder();
                    sb22.append("Error deleting the keystore containing the certificate ");
                    sb22.append(e.getMessage());
                    logger22.error(sb22.toString(), (Throwable) e);
                    if (fileOutputStream != null) {
                    }
                    if (fileInputStream != null) {
                    }
                    Logger.exit(getClass().getSimpleName(), str3);
                } catch (Throwable th4) {
                    th = th4;
                    fileOutputStream = null;
                    if (fileOutputStream != null) {
                    }
                    if (fileInputStream != null) {
                    }
                    throw th;
                }
            } else {
                fileInputStream = null;
                fileOutputStream2 = null;
            }
            if (str2.equals(APPLICATION)) {
                Logger.exit(getClass().getSimpleName(), str3);
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e5) {
                        IOException iOException3 = e5;
                        logger.error(iOException3.getMessage(), (Throwable) iOException3);
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e6) {
                        IOException iOException4 = e6;
                        logger.error(iOException4.getMessage(), (Throwable) iOException4);
                    }
                }
                return;
            }
            for (String str5 : this.context.getPackageManager().getPackagesForUid(this.context.getApplicationInfo().uid)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(this.context.createPackageContext(str5, 0).getFilesDir().getAbsolutePath());
                sb3.append(str4);
                sb3.append(this.KEYSTORE_FILENAME);
                File file2 = new File(sb3.toString());
                if (file2.exists()) {
                    try {
                        fileInputStream2 = new FileInputStream(file2);
                        try {
                            instance.load(fileInputStream2, this.keyStorePassword);
                            instance.deleteEntry(alias);
                            fileOutputStream3 = new FileOutputStream(file2);
                        } catch (IOException unused) {
                            fileOutputStream3 = null;
                            if (fileInputStream2 != null) {
                                fileInputStream2.close();
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            fileOutputStream4 = null;
                            if (fileInputStream2 != null) {
                                try {
                                    fileInputStream2.close();
                                } catch (Exception e7) {
                                    e = e7;
                                    fileInputStream = fileInputStream2;
                                    fileOutputStream = fileOutputStream4;
                                    Logger logger222 = logger;
                                    StringBuilder sb222 = new StringBuilder();
                                    sb222.append("Error deleting the keystore containing the certificate ");
                                    sb222.append(e.getMessage());
                                    logger222.error(sb222.toString(), (Throwable) e);
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                        } catch (IOException e8) {
                                            IOException iOException5 = e8;
                                            logger.error(iOException5.getMessage(), (Throwable) iOException5);
                                        }
                                    }
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    Logger.exit(getClass().getSimpleName(), str3);
                                } catch (Throwable th6) {
                                    th = th6;
                                    fileInputStream = fileInputStream2;
                                    fileOutputStream = fileOutputStream4;
                                    if (fileOutputStream != null) {
                                    }
                                    if (fileInputStream != null) {
                                    }
                                    throw th;
                                }
                            }
                            if (fileOutputStream4 != null) {
                                fileOutputStream4.close();
                            }
                            throw th;
                        }
                        try {
                            instance.store(fileOutputStream3, this.keyStorePassword);
                            fileOutputStream3.close();
                            fileInputStream2.close();
                            try {
                                fileInputStream2.close();
                            } catch (Exception e9) {
                                e = e9;
                                FileOutputStream fileOutputStream5 = fileOutputStream3;
                                fileInputStream = fileInputStream2;
                                fileOutputStream = fileOutputStream5;
                            } catch (Throwable th7) {
                                th = th7;
                                FileOutputStream fileOutputStream6 = fileOutputStream3;
                                fileInputStream = fileInputStream2;
                                fileOutputStream = fileOutputStream6;
                                if (fileOutputStream != null) {
                                }
                                if (fileInputStream != null) {
                                }
                                throw th;
                            }
                        } catch (IOException unused2) {
                            if (fileInputStream2 != null) {
                            }
                        } catch (Throwable th8) {
                            th = th8;
                            fileOutputStream4 = fileOutputStream3;
                            if (fileInputStream2 != null) {
                            }
                            if (fileOutputStream4 != null) {
                            }
                            throw th;
                        }
                    } catch (IOException unused3) {
                        fileInputStream2 = null;
                        fileOutputStream3 = null;
                        if (fileInputStream2 != null) {
                        }
                    } catch (Throwable th9) {
                        th = th9;
                        fileInputStream2 = null;
                        fileOutputStream4 = null;
                        if (fileInputStream2 != null) {
                        }
                        if (fileOutputStream4 != null) {
                        }
                        throw th;
                    }
                    fileOutputStream3.close();
                    fileOutputStream2 = fileOutputStream3;
                    fileInputStream = fileInputStream2;
                } else {
                    fileInputStream = null;
                    fileOutputStream2 = null;
                }
            }
            Logger logger3 = logger;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("certificate cleared for entity: ");
            sb4.append(str2);
            logger3.debug(sb4.toString());
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (IOException e10) {
                    IOException iOException6 = e10;
                    logger.error(iOException6.getMessage(), (Throwable) iOException6);
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e11) {
                    IOException iOException7 = e11;
                    logger.error(iOException7.getMessage(), (Throwable) iOException7);
                }
            }
            Logger.exit(getClass().getSimpleName(), str3);
        } catch (Exception e12) {
            e = e12;
            fileOutputStream = null;
            fileInputStream = null;
            Logger logger2222 = logger;
            StringBuilder sb2222 = new StringBuilder();
            sb2222.append("Error deleting the keystore containing the certificate ");
            sb2222.append(e.getMessage());
            logger2222.error(sb2222.toString(), (Throwable) e);
            if (fileOutputStream != null) {
            }
            if (fileInputStream != null) {
            }
            Logger.exit(getClass().getSimpleName(), str3);
        } catch (Throwable th10) {
            th = th10;
            fileOutputStream = null;
            fileInputStream = null;
            if (fileOutputStream != null) {
            }
            if (fileInputStream != null) {
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public byte[] signData(String str, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        String str2 = "signData";
        Logger.enter(getClass().getSimpleName(), str2);
        Signature instance = Signature.getInstance("SHA256withRSA");
        instance.initSign(privateKey);
        instance.update(str.getBytes());
        Logger.exit(getClass().getSimpleName(), str2);
        return instance.sign();
    }

    public KeyPair getKeyPair(String str) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException, IOException, ClassNotFoundException {
        String str2 = "getKeyPair";
        Logger.enter(getClass().getSimpleName(), str2);
        String alias = getAlias(str);
        if (((KeyPair) this.keyPairHash.get(alias)) == null) {
            String readSecurityPref = WLConfig.getInstance().readSecurityPref(getAlias(str));
            if (readSecurityPref == null) {
                logger.debug("There is no KeyPair in memory-getKeyPair");
                Logger.exit(getClass().getSimpleName(), str2);
                return null;
            }
            byte[] decode = Base64.decode(readSecurityPref, 0);
            int length = decode.length;
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object readObject = objectInputStream.readObject();
                if (readObject instanceof KeyPair) {
                    this.keyPairHash.put(alias, (KeyPair) readObject);
                }
                objectInputStream.close();
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.exit(getClass().getSimpleName(), str2);
                return null;
            }
        }
        Logger.exit(getClass().getSimpleName(), str2);
        return (KeyPair) this.keyPairHash.get(alias);
    }

    private KeyStore loadKeystore() throws KeyStoreException {
        String str = "loadKeystore";
        Logger.enter(getClass().getSimpleName(), str);
        KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
        StringBuilder sb = new StringBuilder();
        sb.append(this.context.getFilesDir().getAbsolutePath());
        sb.append("/");
        sb.append(this.KEYSTORE_FILENAME);
        File file = new File(sb.toString());
        if (file.exists()) {
            try {
                instance.load(new FileInputStream(file), this.keyStorePassword);
            } catch (IOException e) {
                Logger.exit(getClass().getSimpleName(), str);
                throw new Error(e);
            } catch (NoSuchAlgorithmException e2) {
                Logger.exit(getClass().getSimpleName(), str);
                throw new Error(e2);
            } catch (CertificateException e3) {
                Logger.exit(getClass().getSimpleName(), str);
                throw new Error(e3);
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return instance;
    }

    /* JADX WARNING: type inference failed for: r9v0 */
    /* JADX WARNING: type inference failed for: r9v1 */
    /* JADX WARNING: type inference failed for: r9v2 */
    /* JADX WARNING: type inference failed for: r13v1, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r13v2 */
    /* JADX WARNING: type inference failed for: r13v3, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r13v4 */
    /* JADX WARNING: type inference failed for: r13v5 */
    /* JADX WARNING: type inference failed for: r13v6 */
    /* JADX WARNING: type inference failed for: r13v8, types: [java.io.OutputStream, java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r13v9 */
    /* JADX WARNING: type inference failed for: r13v10 */
    /* JADX WARNING: type inference failed for: r9v13, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r7v6, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r9v14 */
    /* JADX WARNING: type inference failed for: r7v8 */
    /* JADX WARNING: type inference failed for: r7v9, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r9v15 */
    /* JADX WARNING: type inference failed for: r9v16 */
    /* JADX WARNING: type inference failed for: r9v17 */
    /* JADX WARNING: type inference failed for: r13v11 */
    /* JADX WARNING: type inference failed for: r13v12 */
    /* JADX WARNING: type inference failed for: r13v13 */
    /* JADX WARNING: type inference failed for: r13v14 */
    /* JADX WARNING: type inference failed for: r13v15 */
    /* JADX WARNING: type inference failed for: r13v16 */
    /* JADX WARNING: type inference failed for: r7v10 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00a8, code lost:
        if (r7 != 0) goto L_0x00aa;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r13v2
  assigns: []
  uses: []
  mth insns count: 175
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
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01a2  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01a7  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x01af  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x01b8 A[SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 12 */
    public PrivateKeyEntry getPrivateKeyEntry(String str) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, NameNotFoundException, UnrecoverableEntryException {
        ? r13;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2;
        ? r132;
        ? r133;
        ? r9;
        ? r7;
        String str2 = str;
        String str3 = "getPrivateKeyEntry";
        Logger.enter(getClass().getSimpleName(), str3);
        KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
        StringBuilder sb = new StringBuilder();
        sb.append(this.context.getFilesDir().getAbsolutePath());
        String str4 = "/";
        sb.append(str4);
        sb.append(this.KEYSTORE_FILENAME);
        File file = new File(sb.toString());
        String alias = getAlias(str);
        boolean exists = file.exists();
        String str5 = APPLICATION;
        ? r92 = 0;
        if (exists) {
            try {
                r7 = new FileInputStream(file);
                try {
                    instance.load(r7, this.keyStorePassword);
                    PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) instance.getEntry(alias, new PasswordProtection(this.keyStorePassword));
                    if (privateKeyEntry != null) {
                        Logger.exit(getClass().getSimpleName(), str3);
                        r7.close();
                        return privateKeyEntry;
                    }
                    if (str5.equalsIgnoreCase(str2)) {
                        Logger.exit(getClass().getSimpleName(), str3);
                        r7.close();
                        return null;
                    }
                    r7.close();
                } catch (IOException e) {
                    e = e;
                    r7 = r7;
                    try {
                        Logger logger2 = logger;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to determine the existence of certificate for device authentication with ");
                        sb2.append(e.getMessage());
                        logger2.error(sb2.toString(), (Throwable) e);
                    } catch (Throwable th) {
                        th = th;
                        r9 = r7;
                        if (r9 != 0) {
                        }
                        throw th;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                r7 = 0;
                Logger logger22 = logger;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("Failed to determine the existence of certificate for device authentication with ");
                sb22.append(e.getMessage());
                logger22.error(sb22.toString(), (Throwable) e);
            } catch (Throwable th2) {
                th = th2;
                r9 = r92;
                if (r9 != 0) {
                    r9.close();
                }
                throw th;
            }
        } else if (str5.equalsIgnoreCase(str2)) {
            Logger.exit(getClass().getSimpleName(), str3);
            return null;
        } else {
            instance.load(null, this.keyStorePassword);
        }
        String[] packagesForUid = this.context.getPackageManager().getPackagesForUid(this.context.getApplicationInfo().uid);
        KeyStore instance2 = KeyStore.getInstance(KeyStore.getDefaultType());
        int length = packagesForUid.length;
        int i = 0;
        int i2 = 0;
        ? r93 = r92;
        while (i2 < length) {
            String str6 = packagesForUid[i2];
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.context.createPackageContext(str6, i).getFilesDir().getAbsolutePath());
            sb3.append(str4);
            sb3.append(this.KEYSTORE_FILENAME);
            File file2 = new File(sb3.toString());
            if (file2.exists()) {
                try {
                    fileInputStream2 = new FileInputStream(file2);
                    try {
                        instance2.load(fileInputStream2, this.keyStorePassword);
                        PrivateKeyEntry privateKeyEntry2 = (PrivateKeyEntry) instance2.getEntry(alias, new PasswordProtection(this.keyStorePassword));
                        if (privateKeyEntry2 == null) {
                            try {
                                fileInputStream2.close();
                                fileInputStream2.close();
                            } catch (IOException e3) {
                                e = e3;
                                r133 = r93;
                                try {
                                    Logger logger3 = logger;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Failed copying certificate for device authentication with ");
                                    sb4.append(e.getMessage());
                                    sb4.append(" , device authentication certificate will recreate.");
                                    logger3.error(sb4.toString(), (Throwable) e);
                                    if (fileInputStream2 != null) {
                                    }
                                    if (r133 != 0) {
                                    }
                                    i2++;
                                    i = 0;
                                    r93 = 0;
                                } catch (Throwable th3) {
                                    th = th3;
                                    r132 = r133;
                                    fileInputStream = fileInputStream2;
                                    r13 = r132;
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    if (r13 != 0) {
                                        r13.close();
                                    }
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                r132 = r93;
                                fileInputStream = fileInputStream2;
                                r13 = r132;
                                if (fileInputStream != null) {
                                }
                                if (r13 != 0) {
                                }
                                throw th;
                            }
                        } else {
                            ? fileOutputStream = new FileOutputStream(file);
                            try {
                                instance.setKeyEntry(alias, privateKeyEntry2.getPrivateKey(), this.keyStorePassword, privateKeyEntry2.getCertificateChain());
                                instance.store(fileOutputStream, this.keyStorePassword);
                                fileOutputStream.close();
                                fileInputStream2.close();
                                Logger.exit(getClass().getSimpleName(), str3);
                                fileInputStream2.close();
                                fileOutputStream.close();
                                return privateKeyEntry2;
                            } catch (IOException e4) {
                                e = e4;
                                r133 = fileOutputStream;
                            }
                        }
                    } catch (IOException e5) {
                        e = e5;
                        r133 = 0;
                        Logger logger32 = logger;
                        StringBuilder sb42 = new StringBuilder();
                        sb42.append("Failed copying certificate for device authentication with ");
                        sb42.append(e.getMessage());
                        sb42.append(" , device authentication certificate will recreate.");
                        logger32.error(sb42.toString(), (Throwable) e);
                        if (fileInputStream2 != null) {
                        }
                        if (r133 != 0) {
                        }
                        i2++;
                        i = 0;
                        r93 = 0;
                    } catch (Throwable th5) {
                        th = th5;
                        fileInputStream = fileInputStream2;
                        r13 = 0;
                        if (fileInputStream != null) {
                        }
                        if (r13 != 0) {
                        }
                        throw th;
                    }
                } catch (IOException e6) {
                    e = e6;
                    fileInputStream2 = null;
                    r133 = 0;
                    Logger logger322 = logger;
                    StringBuilder sb422 = new StringBuilder();
                    sb422.append("Failed copying certificate for device authentication with ");
                    sb422.append(e.getMessage());
                    sb422.append(" , device authentication certificate will recreate.");
                    logger322.error(sb422.toString(), (Throwable) e);
                    if (fileInputStream2 != null) {
                        fileInputStream2.close();
                    }
                    if (r133 != 0) {
                        r133.close();
                    }
                    i2++;
                    i = 0;
                    r93 = 0;
                } catch (Throwable th6) {
                    th = th6;
                    fileInputStream = null;
                    r13 = 0;
                    if (fileInputStream != null) {
                    }
                    if (r13 != 0) {
                    }
                    throw th;
                }
            }
            i2++;
            i = 0;
            r93 = 0;
        }
        Logger.exit(getClass().getSimpleName(), str3);
        return null;
    }
}
