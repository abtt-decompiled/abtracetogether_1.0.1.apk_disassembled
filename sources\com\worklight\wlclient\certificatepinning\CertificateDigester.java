package com.worklight.wlclient.certificatepinning;

import java.security.cert.Certificate;

public interface CertificateDigester {
    String getDigest(Certificate certificate);
}
