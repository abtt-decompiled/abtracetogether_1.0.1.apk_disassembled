package okhttp3.internal.huc;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import okhttp3.Request;
import okio.Buffer;
import okio.BufferedSink;

final class BufferedRequestBody extends OutputStreamRequestBody {
    final Buffer buffer;
    long contentLength = -1;

    BufferedRequestBody(long j) {
        Buffer buffer2 = new Buffer();
        this.buffer = buffer2;
        initOutputStream(buffer2, j);
    }

    public long contentLength() throws IOException {
        return this.contentLength;
    }

    public Request prepareToSendRequest(Request request) throws IOException {
        String str = HttpHeaders.CONTENT_LENGTH;
        if (request.header(str) != null) {
            return request;
        }
        outputStream().close();
        this.contentLength = this.buffer.size();
        return request.newBuilder().removeHeader(HttpHeaders.TRANSFER_ENCODING).header(str, Long.toString(this.buffer.size())).build();
    }

    public void writeTo(BufferedSink bufferedSink) throws IOException {
        this.buffer.copyTo(bufferedSink.buffer(), 0, this.buffer.size());
    }
}
