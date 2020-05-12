package com.worklight.wlclient.api;

import android.content.Context;
import android.os.Build.VERSION;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.wlclient.WLRequestListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONException;
import org.json.JSONObject;

public class SecurityUtils {
    private static final int BYTES_TO_BITS = 8;
    private static final int CURRENT_VERSION = 1;
    public static final String CYPHER_TEXT_LABEL = "ct";
    private static final String ENCRYPTION_SOURCE = "java";
    public static final String ENCRYPTION_SOURCE_LABEL = "src";
    private static final int IV_BYTE_SIZE = 16;
    public static final String IV_LABEL = "iv";
    private static final int PBKDF2_LENGTH_BYTE_SIZE = 32;
    private static final String UTF_8 = "UTF-8";
    public static final String VERSION_LABEL = "v";

    public static String generateKey(String str, String str2, int i) throws SecurityUtilsException {
        return generateKey(str, str2, i, 32);
    }

    public static SecretKey generateSecretKey(String str, String str2, int i, int i2) throws Exception {
        SecretKeyFactory secretKeyFactory;
        if (VERSION.SDK_INT >= 19) {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1And8bit");
        } else {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        }
        return secretKeyFactory.generateSecret(new PBEKeySpec(str.toCharArray(), str2.getBytes(UTF_8), i, i2 * 8));
    }

    public static String generateKey(String str, String str2, int i, int i2) throws SecurityUtilsException {
        if (str == null || str.length() <= 0) {
            throw new SecurityUtilsException("Password cannot be null or empty.");
        } else if (str2 == null || str2.length() <= 0) {
            throw new SecurityUtilsException("Salt cannot be null or empty.");
        } else if (i <= 0) {
            throw new SecurityUtilsException("Iterations must be greater than zero.");
        } else if (i2 > 0) {
            try {
                return WLUtils.byteArrayToHexString(generateSecretKey(str, str2, i, i2).getEncoded());
            } catch (Exception unused) {
                throw new SecurityUtilsException("Problem occured while encrypting. Make sure the given key is valid.");
            }
        } else {
            throw new SecurityUtilsException("Key length must be greater than zero.");
        }
    }

    public static JSONObject encrypt(String str, String str2) throws SecurityUtilsException {
        String str3 = "Problem occured while encrypting. Make sure the given key is valid.";
        if (str == null || str.length() <= 0) {
            throw new SecurityUtilsException("Key cannot be null or empty.");
        } else if (str2 == null || str2.length() <= 0) {
            throw new SecurityUtilsException("Plain text cannot be null or empty.");
        } else {
            String randomString = getRandomString(16);
            try {
                try {
                    String byteArrayToHexString = WLUtils.byteArrayToHexString(initCipher(1, WLUtils.hexStringToByteArray(str), WLUtils.hexStringToByteArray(randomString)).doFinal(str2.getBytes()));
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put(CYPHER_TEXT_LABEL, byteArrayToHexString);
                        jSONObject.put(IV_LABEL, randomString);
                        jSONObject.put(ENCRYPTION_SOURCE_LABEL, ENCRYPTION_SOURCE);
                        jSONObject.put(VERSION_LABEL, 1);
                        return jSONObject;
                    } catch (JSONException e) {
                        throw new SecurityUtilsException("There was a problem while adding properties to the returned JSONObject.", e);
                    }
                } catch (IllegalBlockSizeException e2) {
                    throw new SecurityUtilsException(str3, e2);
                } catch (BadPaddingException e3) {
                    throw new SecurityUtilsException(str3, e3);
                }
            } catch (InvalidKeyException e4) {
                throw new SecurityUtilsException(str3, e4);
            } catch (NoSuchAlgorithmException e5) {
                throw new SecurityUtilsException(str3, e5);
            } catch (NoSuchPaddingException e6) {
                throw new SecurityUtilsException(str3, e6);
            } catch (InvalidAlgorithmParameterException e7) {
                throw new SecurityUtilsException(str3, e7);
            }
        }
    }

    public static String decrypt(String str, JSONObject jSONObject) throws SecurityUtilsException {
        if (str == null || str.length() <= 0) {
            throw new SecurityUtilsException("Key cannot be null or empty.");
        }
        if (jSONObject != null && jSONObject.length() > 0) {
            String str2 = CYPHER_TEXT_LABEL;
            if (jSONObject.has(str2)) {
                String str3 = IV_LABEL;
                if (jSONObject.has(str3)) {
                    String str4 = ENCRYPTION_SOURCE_LABEL;
                    if (jSONObject.has(str4) && jSONObject.has(VERSION_LABEL)) {
                        try {
                            if (jSONObject.getString(str4).equals(ENCRYPTION_SOURCE)) {
                                String string = jSONObject.getString(str2);
                                String string2 = jSONObject.getString(str3);
                                return new String(initCipher(2, WLUtils.hexStringToByteArray(str), WLUtils.hexStringToByteArray(string2)).doFinal(WLUtils.hexStringToByteArray(string)), UTF_8);
                            }
                            throw new SecurityUtilsException("The encrypted object was generated in another environment. Cannot decrypt in this environment.");
                        } catch (JSONException e) {
                            throw new SecurityUtilsException("There was a problem while decrypting. Make sure the given encryptedObject is valid.", e);
                        } catch (Exception e2) {
                            throw new SecurityUtilsException("There was a problem while adding properties to the returned JSONObject.", e2);
                        }
                    }
                }
            }
        }
        throw new SecurityUtilsException("The given encrypted object is invalid or null.");
    }

    public static byte[] generateLocalKey(int i) {
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    public static String getRandomString(int i) {
        return encodeBytesAsHexString(generateLocalKey(i));
    }

    public static String encodeBytesAsHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        if (bArr != null) {
            for (byte valueOf : bArr) {
                sb.append(String.format("%02X", new Object[]{Byte.valueOf(valueOf)}));
            }
        }
        return sb.toString();
    }

    public static void getRandomStringFromServer(final int i, final Context context, final WLRequestListener wLRequestListener) {
        new Runnable() {
            public void run() {
                HttpURLConnection httpURLConnection;
                String str = "run";
                Logger.enter(getClass().getSimpleName(), str);
                try {
                    httpURLConnection = (HttpURLConnection) SecurityUtils.buildURL(i, SecurityUtils.getConfig(context)).openConnection();
                    wLRequestListener.onSuccess(new WLResponse(Callback.DEFAULT_DRAG_ANIMATION_DURATION, SecurityUtils.readRandomStringFromInputStream(httpURLConnection), null));
                    httpURLConnection.disconnect();
                } catch (Throwable th) {
                    wLRequestListener.onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, th.getMessage(), null));
                }
                Logger.exit(getClass().getSimpleName(), str);
            }
        }.run();
    }

    static String readRandomStringFromInputStream(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(httpURLConnection.getInputStream())));
        String str = "";
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return str;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(readLine);
                str = sb.toString();
            } finally {
                bufferedReader.close();
            }
        }
    }

    static URL buildURL(int i, WLConfig wLConfig) throws MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append(wLConfig.getRootURL());
        sb.append("/");
        sb.append("apps/services/random");
        sb.append("?bytes=");
        sb.append(i);
        return new URL(sb.toString());
    }

    /* access modifiers changed from: private */
    public static WLConfig getConfig(Context context) {
        try {
            return WLConfig.getInstance();
        } catch (IllegalStateException unused) {
            WLConfig.createInstance(context);
            return WLConfig.getInstance();
        }
    }

    static Cipher initCipher(int i, byte[] bArr, byte[] bArr2) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(i, secretKeySpec, ivParameterSpec);
        return instance;
    }
}
