package okhttp3.internal.http;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

public final class BridgeInterceptor implements Interceptor {
    private final CookieJar cookieJar;

    public BridgeInterceptor(CookieJar cookieJar2) {
        this.cookieJar = cookieJar2;
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Builder newBuilder = request.newBuilder();
        RequestBody body = request.body();
        String str = HttpHeaders.CONTENT_LENGTH;
        if (body != null) {
            MediaType contentType = body.contentType();
            if (contentType != null) {
                newBuilder.header(HttpHeaders.CONTENT_TYPE, contentType.toString());
            }
            long contentLength = body.contentLength();
            int i = (contentLength > -1 ? 1 : (contentLength == -1 ? 0 : -1));
            String str2 = HttpHeaders.TRANSFER_ENCODING;
            if (i != 0) {
                newBuilder.header(str, Long.toString(contentLength));
                newBuilder.removeHeader(str2);
            } else {
                newBuilder.header(str2, "chunked");
                newBuilder.removeHeader(str);
            }
        }
        String str3 = HttpHeaders.HOST;
        boolean z = false;
        if (request.header(str3) == null) {
            newBuilder.header(str3, Util.hostHeader(request.url(), false));
        }
        String str4 = HttpHeaders.CONNECTION;
        if (request.header(str4) == null) {
            newBuilder.header(str4, "Keep-Alive");
        }
        String str5 = HttpHeaders.ACCEPT_ENCODING;
        String str6 = "gzip";
        if (request.header(str5) == null) {
            z = true;
            newBuilder.header(str5, str6);
        }
        List loadForRequest = this.cookieJar.loadForRequest(request.url());
        if (!loadForRequest.isEmpty()) {
            newBuilder.header(HttpHeaders.COOKIE, cookieHeader(loadForRequest));
        }
        String str7 = HttpHeaders.USER_AGENT;
        if (request.header(str7) == null) {
            newBuilder.header(str7, Version.userAgent());
        }
        Response proceed = chain.proceed(newBuilder.build());
        HttpHeaders.receiveHeaders(this.cookieJar, request.url(), proceed.headers());
        Response.Builder request2 = proceed.newBuilder().request(request);
        if (z) {
            String str8 = HttpHeaders.CONTENT_ENCODING;
            if (str6.equalsIgnoreCase(proceed.header(str8)) && HttpHeaders.hasBody(proceed)) {
                GzipSource gzipSource = new GzipSource(proceed.body().source());
                Headers build = proceed.headers().newBuilder().removeAll(str8).removeAll(str).build();
                request2.headers(build);
                request2.body(new RealResponseBody(build, Okio.buffer((Source) gzipSource)));
            }
        }
        return request2.build();
    }

    private String cookieHeader(List<Cookie> list) {
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append("; ");
            }
            Cookie cookie = (Cookie) list.get(i);
            sb.append(cookie.name());
            sb.append('=');
            sb.append(cookie.value());
        }
        return sb.toString();
    }
}
