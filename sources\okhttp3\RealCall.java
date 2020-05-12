package okhttp3;

import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;

final class RealCall implements Call {
    /* access modifiers changed from: private */
    public final OkHttpClient client;
    private boolean executed;
    Request originalRequest;
    /* access modifiers changed from: private */
    public final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;

    final class AsyncCall extends NamedRunnable {
        private final Callback responseCallback;

        private AsyncCall(Callback callback) {
            super("OkHttp %s", RealCall.this.redactedUrl().toString());
            this.responseCallback = callback;
        }

        /* access modifiers changed from: 0000 */
        public String host() {
            return RealCall.this.originalRequest.url().host();
        }

        /* access modifiers changed from: 0000 */
        public Request request() {
            return RealCall.this.originalRequest;
        }

        /* access modifiers changed from: 0000 */
        public RealCall get() {
            return RealCall.this;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0035 A[SYNTHETIC, Splitter:B:12:0x0035] */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0055 A[Catch:{ all -> 0x002d }] */
        public void execute() {
            IOException e;
            boolean z = true;
            try {
                Response access$100 = RealCall.this.getResponseWithInterceptorChain();
                if (RealCall.this.retryAndFollowUpInterceptor.isCanceled()) {
                    try {
                        this.responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
                    } catch (IOException e2) {
                        e = e2;
                        if (!z) {
                            try {
                                Platform platform = Platform.get();
                                StringBuilder sb = new StringBuilder();
                                sb.append("Callback failure for ");
                                sb.append(RealCall.this.toLoggableString());
                                platform.log(4, sb.toString(), e);
                            } catch (Throwable th) {
                                RealCall.this.client.dispatcher().finished(this);
                                throw th;
                            }
                        } else {
                            this.responseCallback.onFailure(RealCall.this, e);
                        }
                        RealCall.this.client.dispatcher().finished(this);
                    }
                } else {
                    this.responseCallback.onResponse(RealCall.this, access$100);
                }
            } catch (IOException e3) {
                e = e3;
                z = false;
                if (!z) {
                }
                RealCall.this.client.dispatcher().finished(this);
            }
            RealCall.this.client.dispatcher().finished(this);
        }
    }

    protected RealCall(OkHttpClient okHttpClient, Request request) {
        this.client = okHttpClient;
        this.originalRequest = request;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(okHttpClient);
    }

    public Request request() {
        return this.originalRequest;
    }

    public Response execute() throws IOException {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
            } else {
                throw new IllegalStateException("Already Executed");
            }
        }
        try {
            this.client.dispatcher().executed(this);
            Response responseWithInterceptorChain = getResponseWithInterceptorChain();
            if (responseWithInterceptorChain != null) {
                return responseWithInterceptorChain;
            }
            throw new IOException("Canceled");
        } finally {
            this.client.dispatcher().finished(this);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void setForWebSocket() {
        if (!this.executed) {
            this.retryAndFollowUpInterceptor.setForWebSocket(true);
        } else {
            throw new IllegalStateException("Already Executed");
        }
    }

    public void enqueue(Callback callback) {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
            } else {
                throw new IllegalStateException("Already Executed");
            }
        }
        this.client.dispatcher().enqueue(new AsyncCall(callback));
    }

    public void cancel() {
        this.retryAndFollowUpInterceptor.cancel();
    }

    public synchronized boolean isExecuted() {
        return this.executed;
    }

    public boolean isCanceled() {
        return this.retryAndFollowUpInterceptor.isCanceled();
    }

    /* access modifiers changed from: 0000 */
    public StreamAllocation streamAllocation() {
        return this.retryAndFollowUpInterceptor.streamAllocation();
    }

    /* access modifiers changed from: private */
    public String toLoggableString() {
        String str = this.retryAndFollowUpInterceptor.isCanceled() ? "canceled call" : NotificationCompat.CATEGORY_CALL;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" to ");
        sb.append(redactedUrl());
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public HttpUrl redactedUrl() {
        return this.originalRequest.url().resolve("/...");
    }

    /* access modifiers changed from: private */
    public Response getResponseWithInterceptorChain() throws IOException {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.client.interceptors());
        arrayList.add(this.retryAndFollowUpInterceptor);
        arrayList.add(new BridgeInterceptor(this.client.cookieJar()));
        arrayList.add(new CacheInterceptor(this.client.internalCache()));
        arrayList.add(new ConnectInterceptor(this.client));
        if (!this.retryAndFollowUpInterceptor.isForWebSocket()) {
            arrayList.addAll(this.client.networkInterceptors());
        }
        arrayList.add(new CallServerInterceptor(this.retryAndFollowUpInterceptor.isForWebSocket()));
        RealInterceptorChain realInterceptorChain = new RealInterceptorChain(arrayList, null, null, null, 0, this.originalRequest);
        return realInterceptorChain.proceed(this.originalRequest);
    }
}
