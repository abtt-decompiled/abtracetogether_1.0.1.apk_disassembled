package com.worklight.utils;

import android.util.Base64;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.wlclient.api.SecurityUtils;
import java.security.Key;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESStringEncryption implements StringEncryption {
    static final String Algorithm = "AES";
    private static final String CLIENT_ID_OAUTH_LABEL = "com.worklight.oauth.clientid";
    private static final int IV_BYTE_SIZE = 16;
    private static final String IV_PARAMETER_SPEC = "IvParameterSpec";
    private static final String SECURITY_PREFS = "SecurityPrefs";
    static final String hashStr = "zDfb2E9yZartghdY";
    static final int minKeySize = 16;
    String iv;
    IvParameterSpec ivspec;
    Key key;

    public AESStringEncryption(String str) {
        this.key = new SecretKeySpec(hash(str.getBytes()), Algorithm);
    }

    public String encrypt(String str) {
        String str2 = "encrypt";
        Logger.enter(getClass().getSimpleName(), str2);
        byte[] doFinalWithMode = doFinalWithMode(1, str.getBytes());
        Logger.exit(getClass().getSimpleName(), str2);
        return Base64.encodeToString(doFinalWithMode, 2);
    }

    public String decrypt(String str) {
        String str2 = "decrypt";
        Logger.enter(getClass().getSimpleName(), str2);
        byte[] doFinalWithMode = doFinalWithMode(2, Base64.decode(str.getBytes(), 2));
        Logger.exit(getClass().getSimpleName(), str2);
        return new String(doFinalWithMode);
    }

    private byte[] doFinalWithMode(int i, byte[] bArr) {
        Cipher cipher;
        String str = "doFinalWithMode";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            String readPref = WLConfig.getInstance().readPref(SECURITY_PREFS, CLIENT_ID_OAUTH_LABEL);
            String str2 = IV_PARAMETER_SPEC;
            if (readPref == null) {
                if (WLConfig.getInstance().readWLPref(str2) == null) {
                    this.iv = SecurityUtils.getRandomString(16);
                    WLConfig.getInstance().writeWLPref(str2, this.iv);
                }
            }
            if (WLConfig.getInstance().readWLPref(str2) != null) {
                this.iv = WLConfig.getInstance().readWLPref(str2);
                this.ivspec = new IvParameterSpec(WLUtils.hexStringToByteArray(this.iv));
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(i, this.key, this.ivspec);
            } else {
                cipher = Cipher.getInstance(Algorithm);
                cipher.init(i, this.key);
            }
            Logger.exit(getClass().getSimpleName(), str);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.exit(getClass().getSimpleName(), str);
            return new byte[0];
        }
    }

    private byte[] hash(byte[] bArr) {
        String str = "hash";
        Logger.enter(getClass().getSimpleName(), str);
        byte[] copyOf = Arrays.copyOf(bArr, 16);
        byte[] bytes = hashStr.getBytes();
        for (int i = 0; i < 16; i++) {
            copyOf[i] = (byte) (copyOf[i] ^ bytes[i]);
        }
        Logger.exit(getClass().getSimpleName(), str);
        return copyOf;
    }
}
