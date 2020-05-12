package com.worklight.wlclient;

import android.content.Context;
import android.os.Build.VERSION;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.utils.SecurityUtils;
import com.worklight.wlclient.cookie.WLPersistentCookie;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Response;
import okio.ByteString;

public class HttpClientManager {
    public static final String DUMMY_PINNING_HOST_NAME = "*";
    private static final String LOCATION_HEADER = "Location";
    private static final String OAUTH_PREVENT_REDIRECT = "wl-oauth-prevent-redirect";
    private static final int OAUTH_REDIRECT_STATUS = 222;
    public static final String PARAM_MFPREDIRECTURI = "://mfpredirecturi";
    private static final int SOCKET_OPERATION_TIMEOUT = 60;
    private static HttpClientManager instance;
    private static Logger logger = Logger.getInstance(HttpClientManager.class.getName());
    private Builder builder;
    private CertificatePinner certificatePinningManager;
    private OkHttpClient httpClient;
    private String webViewUserAgent;

    private HttpClientManager(Context context) {
        String str = "Unable to create socket";
        Builder builder2 = new Builder();
        this.builder = builder2;
        builder2.connectTimeout(60, TimeUnit.SECONDS);
        this.builder.readTimeout(60, TimeUnit.SECONDS);
        if (VERSION.SDK_INT <= 19) {
            try {
                this.builder.sslSocketFactory(new TLSEnabledSSLSocketFactory()).build();
            } catch (KeyManagementException e) {
                logger.error(str, (Throwable) e);
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e2) {
                logger.error(str, (Throwable) e2);
                e2.printStackTrace();
            }
        }
        String protocol = WLConfig.getInstance().getProtocol();
        if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https")) {
            this.builder.cookieJar(WLPersistentCookie.generate(context));
            this.builder.addNetworkInterceptor(new Interceptor() {
                public Response intercept(Chain chain) throws IOException {
                    String str = "intercept";
                    Logger.enter(getClass().getSimpleName(), str);
                    Response proceed = chain.proceed(chain.request());
                    if (proceed.isRedirect()) {
                        String header = proceed.header("Location");
                        if (header != null && header.contains(HttpClientManager.PARAM_MFPREDIRECTURI)) {
                            Logger.exit(getClass().getSimpleName(), str);
                            return proceed.newBuilder().code(HttpClientManager.OAUTH_REDIRECT_STATUS).message(HttpClientManager.OAUTH_PREVENT_REDIRECT).build();
                        }
                    }
                    Logger.exit(getClass().getSimpleName(), str);
                    return proceed;
                }
            });
            String property = System.getProperty("http.agent");
            this.webViewUserAgent = property;
            if (!property.contains("Worklight")) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.webViewUserAgent);
                sb.append("/Worklight/");
                sb.append(WLConfig.getInstance().getPlatformVersion());
                this.webViewUserAgent = sb.toString();
            }
            OkHttpInterceptor okHttpInterceptor = new OkHttpInterceptor(WLConfig.getInstance(), context);
            CertificatePinningInterceptor certificatePinningInterceptor = new CertificatePinningInterceptor();
            this.builder.addInterceptor(okHttpInterceptor);
            this.builder.addInterceptor(certificatePinningInterceptor);
            this.httpClient = getOkHttpClient();
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("HttpClientFactory: Can't create HttpClient with protocol ");
        sb2.append(protocol);
        throw new RuntimeException(sb2.toString());
    }

    public static boolean setSSLSocketFactory(KeyStore keyStore, char[] cArr) {
        try {
            if (getAndroidCATrustStore() == null) {
                return false;
            }
            if (!(instance == null || instance.httpClient == null)) {
                SSLContext instance2 = SSLContext.getInstance("TLS");
                TrustManagerFactory instance3 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                instance3.init(keyStore);
                KeyManagerFactory instance4 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                instance4.init(keyStore, cArr);
                instance2.init(instance4.getKeyManagers(), instance3.getTrustManagers(), null);
                instance.builder.sslSocketFactory(instance2.getSocketFactory());
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public void pinTrustedCertificatePublicKey(Certificate certificate) {
        String str = "pinTrustedCertificatePublicKey";
        Logger.enter(getClass().getSimpleName(), str);
        if (certificate == null) {
            pinEmptyCertificate();
        }
        if (certificate instanceof X509Certificate) {
            CertificatePinner.Builder builder2 = new CertificatePinner.Builder();
            StringBuilder sb = new StringBuilder();
            sb.append("sha1/");
            sb.append(sha1(ByteString.of(certificate.getPublicKey().getEncoded())).base64());
            CertificatePinner build = builder2.add(DUMMY_PINNING_HOST_NAME, sb.toString()).build();
            this.certificatePinningManager = build;
            this.builder.certificatePinner(build);
            this.httpClient = this.builder.build();
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void pinMultipleTrustedCertificatePublicKey(Certificate[] certificateArr) {
        String str = "pinMultipleTrustedCertificatePublicKey";
        Logger.enter(getClass().getSimpleName(), str);
        String[] strArr = new String[certificateArr.length];
        for (int i = 0; i < certificateArr.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("sha1/");
            sb.append(sha1(ByteString.of(certificateArr[i].getPublicKey().getEncoded())).base64());
            strArr[i] = sb.toString();
        }
        CertificatePinner.Builder builder2 = new CertificatePinner.Builder();
        builder2.add(DUMMY_PINNING_HOST_NAME, strArr);
        CertificatePinner build = builder2.build();
        this.certificatePinningManager = build;
        this.builder.certificatePinner(build);
        this.httpClient = this.builder.build();
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void pinEmptyCertificate() {
        String str = "pinEmptyCertificate";
        Logger.enter(getClass().getSimpleName(), str);
        CertificatePinner build = new CertificatePinner.Builder().add(DUMMY_PINNING_HOST_NAME, "sha1/ ").build();
        this.certificatePinningManager = build;
        this.builder.certificatePinner(build);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private ByteString sha1(ByteString byteString) {
        String str = "sha1";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            byte[] digest = MessageDigest.getInstance(SecurityUtils.HASH_ALGORITH_SHA1).digest(byteString.toByteArray());
            Logger.exit(getClass().getSimpleName(), str);
            return ByteString.of(digest);
        } catch (NoSuchAlgorithmException e) {
            Logger.exit(getClass().getSimpleName(), str);
            throw new RuntimeException(e);
        }
    }

    public void pinTrustedCertificatePublicKey(Certificate certificate, String str) {
        String str2 = "pinTrustedCertificatePublicKey";
        Logger.enter(getClass().getSimpleName(), str2);
        if (certificate instanceof X509Certificate) {
            CertificatePinner.Builder builder2 = new CertificatePinner.Builder();
            StringBuilder sb = new StringBuilder();
            sb.append("sha1/");
            sb.append(sha1(ByteString.of(certificate.getPublicKey().getEncoded())).base64());
            CertificatePinner build = builder2.add(str, sb.toString()).build();
            this.certificatePinningManager = build;
            this.builder.certificatePinner(build);
            this.httpClient = this.builder.build();
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public static HttpClientManager getInstance() {
        HttpClientManager httpClientManager = instance;
        if (httpClientManager != null) {
            return httpClientManager;
        }
        throw new IllegalStateException("HttpClientManager should be created first (before calling getInstance");
    }

    public OkHttpClient getOkHttpClient() {
        String str = "getOkHttpClient";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.httpClient == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return this.builder.build();
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.httpClient;
    }

    public String getWebViewUserAgent() {
        String str = "getWebViewUserAgent";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.webViewUserAgent;
    }

    public static synchronized void createInstance(Context context) {
        synchronized (HttpClientManager.class) {
            if (instance == null) {
                instance = new HttpClientManager(context);
            }
        }
    }

    static synchronized void resetInstance(Context context) {
        synchronized (HttpClientManager.class) {
            instance = new HttpClientManager(context);
        }
    }

    private static KeyStore getAndroidCATrustStore() {
        try {
            KeyStore instance2 = KeyStore.getInstance("AndroidCAStore");
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).init(instance2);
            return instance2;
        } catch (Throwable th) {
            logger.error("Failed to access AndroidCAStore", th);
            return null;
        }
    }
}
