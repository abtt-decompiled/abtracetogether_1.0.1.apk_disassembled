package com.worklight.wlclient.certificatepinning;

import com.worklight.common.Logger;
import com.worklight.nativeandroid.common.WLUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;

public class PublicKeyCertificateDigest implements CertificateDigester {
    public String getDigest(Certificate certificate) {
        String str = "getDigest";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            Logger.exit(getClass().getSimpleName(), str);
            return WLUtils.byteArrayToHexString(instance.digest(certificate.getPublicKey().getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Logger.exit(getClass().getSimpleName(), str);
            return null;
        }
    }
}
