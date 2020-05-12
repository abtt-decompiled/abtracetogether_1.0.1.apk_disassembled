package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.StreamResetException;
import okhttp3.internal.http.Http1xStream;
import okhttp3.internal.http.Http2xStream;
import okhttp3.internal.http.HttpStream;

public final class StreamAllocation {
    public final Address address;
    private boolean canceled;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    private int refusedStreamCount;
    private boolean released;
    private Route route;
    private final RouteSelector routeSelector;
    private HttpStream stream;

    public StreamAllocation(ConnectionPool connectionPool2, Address address2) {
        this.connectionPool = connectionPool2;
        this.address = address2;
        this.routeSelector = new RouteSelector(address2, routeDatabase());
    }

    public HttpStream newStream(OkHttpClient okHttpClient, boolean z) {
        HttpStream httpStream;
        int connectTimeoutMillis = okHttpClient.connectTimeoutMillis();
        int readTimeoutMillis = okHttpClient.readTimeoutMillis();
        int writeTimeoutMillis = okHttpClient.writeTimeoutMillis();
        try {
            RealConnection findHealthyConnection = findHealthyConnection(connectTimeoutMillis, readTimeoutMillis, writeTimeoutMillis, okHttpClient.retryOnConnectionFailure(), z);
            if (findHealthyConnection.framedConnection != null) {
                httpStream = new Http2xStream(okHttpClient, this, findHealthyConnection.framedConnection);
            } else {
                findHealthyConnection.socket().setSoTimeout(readTimeoutMillis);
                findHealthyConnection.source.timeout().timeout((long) readTimeoutMillis, TimeUnit.MILLISECONDS);
                findHealthyConnection.sink.timeout().timeout((long) writeTimeoutMillis, TimeUnit.MILLISECONDS);
                httpStream = new Http1xStream(okHttpClient, this, findHealthyConnection.source, findHealthyConnection.sink);
            }
            synchronized (this.connectionPool) {
                this.stream = httpStream;
            }
            return httpStream;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0018, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        if (r0.isHealthy(r8) != false) goto L_0x0018;
     */
    private RealConnection findHealthyConnection(int i, int i2, int i3, boolean z, boolean z2) throws IOException {
        while (true) {
            RealConnection findConnection = findConnection(i, i2, i3, z);
            synchronized (this.connectionPool) {
                if (findConnection.successCount == 0) {
                    return findConnection;
                }
            }
            noNewStreams();
        }
        while (true) {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002c, code lost:
        if (r1 != null) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002e, code lost:
        r1 = r8.routeSelector.next();
        r0 = r8.connectionPool;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0036, code lost:
        monitor-enter(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r8.route = r1;
        r8.refusedStreamCount = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x003c, code lost:
        monitor-exit(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0041, code lost:
        r0 = new okhttp3.internal.connection.RealConnection(r1);
        acquire(r0);
        r1 = r8.connectionPool;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x004b, code lost:
        monitor-enter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        okhttp3.internal.Internal.instance.put(r8.connectionPool, r0);
        r8.connection = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0057, code lost:
        if (r8.canceled != false) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0059, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x005a, code lost:
        r0.connect(r9, r10, r11, r8.address.connectionSpecs(), r12);
        routeDatabase().connected(r0.route());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0073, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007b, code lost:
        throw new java.io.IOException("Canceled");
     */
    private RealConnection findConnection(int i, int i2, int i3, boolean z) throws IOException {
        synchronized (this.connectionPool) {
            if (this.released) {
                throw new IllegalStateException("released");
            } else if (this.stream != null) {
                throw new IllegalStateException("stream != null");
            } else if (!this.canceled) {
                RealConnection realConnection = this.connection;
                if (realConnection != null && !realConnection.noNewStreams) {
                    return realConnection;
                }
                RealConnection realConnection2 = Internal.instance.get(this.connectionPool, this.address, this);
                if (realConnection2 != null) {
                    this.connection = realConnection2;
                    return realConnection2;
                }
                Route route2 = this.route;
            } else {
                throw new IOException("Canceled");
            }
        }
    }

    public void streamFinished(boolean z, HttpStream httpStream) {
        synchronized (this.connectionPool) {
            if (httpStream != null) {
                if (httpStream == this.stream) {
                    if (!z) {
                        this.connection.successCount++;
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("expected ");
            sb.append(this.stream);
            sb.append(" but was ");
            sb.append(httpStream);
            throw new IllegalStateException(sb.toString());
        }
        deallocate(z, false, true);
    }

    public HttpStream stream() {
        HttpStream httpStream;
        synchronized (this.connectionPool) {
            httpStream = this.stream;
        }
        return httpStream;
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }

    public synchronized RealConnection connection() {
        return this.connection;
    }

    public void release() {
        deallocate(false, true, false);
    }

    public void noNewStreams() {
        deallocate(true, false, false);
    }

    private void deallocate(boolean z, boolean z2, boolean z3) {
        RealConnection realConnection;
        RealConnection realConnection2;
        synchronized (this.connectionPool) {
            realConnection = null;
            if (z3) {
                try {
                    this.stream = null;
                } catch (Throwable th) {
                    while (true) {
                        throw th;
                    }
                }
            }
            if (z2) {
                this.released = true;
            }
            if (this.connection != null) {
                if (z) {
                    this.connection.noNewStreams = true;
                }
                if (this.stream == null && (this.released || this.connection.noNewStreams)) {
                    release(this.connection);
                    if (this.connection.allocations.isEmpty()) {
                        this.connection.idleAtNanos = System.nanoTime();
                        if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                            realConnection2 = this.connection;
                            this.connection = null;
                            realConnection = realConnection2;
                        }
                    }
                    realConnection2 = null;
                    this.connection = null;
                    realConnection = realConnection2;
                }
            }
        }
        if (realConnection != null) {
            Util.closeQuietly(realConnection.socket());
        }
    }

    public void cancel() {
        HttpStream httpStream;
        RealConnection realConnection;
        synchronized (this.connectionPool) {
            this.canceled = true;
            httpStream = this.stream;
            realConnection = this.connection;
        }
        if (httpStream != null) {
            httpStream.cancel();
        } else if (realConnection != null) {
            realConnection.cancel();
        }
    }

    public void streamFailed(IOException iOException) {
        boolean z;
        synchronized (this.connectionPool) {
            if (iOException instanceof StreamResetException) {
                StreamResetException streamResetException = (StreamResetException) iOException;
                if (streamResetException.errorCode == ErrorCode.REFUSED_STREAM) {
                    this.refusedStreamCount++;
                }
                if (streamResetException.errorCode != ErrorCode.REFUSED_STREAM || this.refusedStreamCount > 1) {
                    this.route = null;
                }
                z = false;
            } else {
                if (this.connection != null && !this.connection.isMultiplexed()) {
                    if (this.connection.successCount == 0) {
                        if (!(this.route == null || iOException == null)) {
                            this.routeSelector.connectFailed(this.route, iOException);
                        }
                        this.route = null;
                    }
                }
                z = false;
            }
            z = true;
        }
        deallocate(z, false, true);
    }

    public void acquire(RealConnection realConnection) {
        realConnection.allocations.add(new WeakReference(this));
    }

    private void release(RealConnection realConnection) {
        int size = realConnection.allocations.size();
        for (int i = 0; i < size; i++) {
            if (((Reference) realConnection.allocations.get(i)).get() == this) {
                realConnection.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }

    public boolean hasMoreRoutes() {
        return this.route != null || this.routeSelector.hasNext();
    }

    public String toString() {
        return this.address.toString();
    }
}
