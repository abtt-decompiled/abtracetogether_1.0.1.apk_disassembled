package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Connection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.StreamAllocation;

public final class RealInterceptorChain implements Chain {
    private int calls;
    private final Connection connection;
    private final HttpStream httpStream;
    private final int index;
    private final List<Interceptor> interceptors;
    private final Request request;
    private final StreamAllocation streamAllocation;

    public RealInterceptorChain(List<Interceptor> list, StreamAllocation streamAllocation2, HttpStream httpStream2, Connection connection2, int i, Request request2) {
        this.interceptors = list;
        this.connection = connection2;
        this.streamAllocation = streamAllocation2;
        this.httpStream = httpStream2;
        this.index = i;
        this.request = request2;
    }

    public Connection connection() {
        return this.connection;
    }

    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }

    public HttpStream httpStream() {
        return this.httpStream;
    }

    public Request request() {
        return this.request;
    }

    public Response proceed(Request request2) throws IOException {
        return proceed(request2, this.streamAllocation, this.httpStream, this.connection);
    }

    public Response proceed(Request request2, StreamAllocation streamAllocation2, HttpStream httpStream2, Connection connection2) throws IOException {
        if (this.index < this.interceptors.size()) {
            this.calls++;
            String str = "network interceptor ";
            if (this.httpStream == null || sameConnection(request2.url())) {
                String str2 = " must call proceed() exactly once";
                if (this.httpStream == null || this.calls <= 1) {
                    RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, streamAllocation2, httpStream2, connection2, this.index + 1, request2);
                    Interceptor interceptor = (Interceptor) this.interceptors.get(this.index);
                    Response intercept = interceptor.intercept(realInterceptorChain);
                    if (httpStream2 != null && this.index + 1 < this.interceptors.size() && realInterceptorChain.calls != 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(interceptor);
                        sb.append(str2);
                        throw new IllegalStateException(sb.toString());
                    } else if (intercept != null) {
                        return intercept;
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("interceptor ");
                        sb2.append(interceptor);
                        sb2.append(" returned null");
                        throw new NullPointerException(sb2.toString());
                    }
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(this.interceptors.get(this.index - 1));
                    sb3.append(str2);
                    throw new IllegalStateException(sb3.toString());
                }
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(this.interceptors.get(this.index - 1));
                sb4.append(" must retain the same host and port");
                throw new IllegalStateException(sb4.toString());
            }
        } else {
            throw new AssertionError();
        }
    }

    private boolean sameConnection(HttpUrl httpUrl) {
        return httpUrl.host().equals(this.connection.route().address().url().host()) && httpUrl.port() == this.connection.route().address().url().port();
    }
}
