package com.worklight.wlclient;

import com.worklight.common.Logger;
import java.io.IOException;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Response;

class CertificatePinningInterceptor implements Interceptor {
    CertificatePinningInterceptor() {
    }

    public Response intercept(Chain chain) throws IOException {
        String str = "intercept";
        Logger.enter(getClass().getSimpleName(), str);
        Response proceed = chain.proceed(chain.request());
        CertificatePinner certificatePinner = HttpClientManager.getInstance().getOkHttpClient().certificatePinner();
        if (!(certificatePinner == null || proceed == null || proceed.handshake() == null)) {
            certificatePinner.check(HttpClientManager.DUMMY_PINNING_HOST_NAME, proceed.handshake().peerCertificates());
        }
        Logger.exit(getClass().getSimpleName(), str);
        return proceed;
    }
}
