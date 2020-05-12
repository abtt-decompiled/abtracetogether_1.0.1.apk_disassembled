package com.worklight.utils;

import android.content.Context;
import com.worklight.common.Logger;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONException;

public class SecurityUtils {
    public static final String HASH_ALGORITH_MD5 = "MD5";
    public static final String HASH_ALGORITH_SHA1 = "SHA-1";
    private static Logger logger = Logger.getInstance("SecurityUtils");

    public static class PBKDF2 {
        private static byte[] encodedInt(int i) {
            return new byte[]{(byte) ((-16777216 & i) >>> 24), (byte) ((16711680 & i) >>> 16), (byte) ((65280 & i) >>> 8), (byte) (i & 255)};
        }

        public static Key genKey(char[] cArr, byte[] bArr, int i, int i2) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
            Mac instance = Mac.getInstance("HMACSHA1");
            int macLength = instance.getMacLength();
            int ceil = ceil(i2, macLength);
            int i3 = i2 - ((ceil - 1) * macLength);
            byte[] bArr2 = new byte[i2];
            instance.init(new SecretKeySpec(new String(cArr).getBytes(), "HmacSHA1"));
            int i4 = 1;
            while (i4 <= ceil) {
                System.arraycopy(f(instance, bArr, i, i4), 0, bArr2, (i4 - 1) * macLength, i4 == ceil ? i3 : macLength);
                i4++;
            }
            return new SecretKeySpec(bArr2, "AES");
        }

        private static byte[] f(Mac mac, byte[] bArr, int i, int i2) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
            byte[] bArr2 = new byte[mac.getMacLength()];
            byte[] concat = concat(bArr, encodedInt(i2));
            for (int i3 = 1; i3 <= i; i3++) {
                concat = mac.doFinal(concat);
                bArr2 = xor(bArr2, concat);
            }
            return bArr2;
        }

        private static byte[] xor(byte[] bArr, byte[] bArr2) {
            for (int i = 0; i < bArr.length; i++) {
                bArr[i] = (byte) ((bArr[i] ^ bArr2[i]) & 255);
            }
            return bArr;
        }

        private static byte[] concat(byte[] bArr, byte[] bArr2) {
            byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
            System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
            System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
            return bArr3;
        }

        private static int ceil(int i, int i2) {
            int i3 = i / i2;
            return i % i2 != 0 ? i3 + 1 : i3;
        }
    }

    public static String hashData(String str, String str2) {
        byte[] hashData = hashData(str.getBytes(), str2);
        StringBuilder sb = new StringBuilder();
        for (byte b : hashData) {
            sb.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    public static byte[] hashData(byte[] bArr, String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(str);
            instance.reset();
            instance.update(bArr);
            return instance.digest();
        } catch (NoSuchAlgorithmException e) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" is not supported on this device");
            logger2.error(sb.toString(), (Throwable) e);
            return null;
        }
    }

    public static InputStream decryptData(InputStream inputStream, byte[] bArr) throws Exception {
        byte[] bArr2 = {109, 76, 99, 107, 50, 70, 56, 89, 73, 115, 116, 111, 114, 97, 101, 80};
        SecretKeySpec secretKeySpec = new SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(bArr), "AES");
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(2, secretKeySpec, new IvParameterSpec(bArr2));
        return new CipherInputStream(inputStream, instance);
    }

    public static byte[] kpg(Context context, String[] strArr) {
        try {
            return PaidSecurityUtils.kpg(context, strArr);
        } catch (Throwable unused) {
            return new byte[0];
        }
    }

    public static String hashDataFromJSON(Context context, JSONArray jSONArray) throws JSONException, UnsupportedEncodingException {
        String str = ((String) jSONArray.get(0)).split(",")[0];
        JSONArray jSONArray2 = (JSONArray) jSONArray.get(1);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray2.length(); i++) {
            arrayList.add(jSONArray2.getString(i));
        }
        String replaceAll = WLBase64.encode(kpg(context, (String[]) arrayList.toArray(new String[0])), "UTF-8").replaceAll("\n", "");
        StringBuilder sb = new StringBuilder();
        sb.append(str.trim());
        sb.append(replaceAll);
        return hashData(sb.toString(), HASH_ALGORITH_SHA1);
    }
}
