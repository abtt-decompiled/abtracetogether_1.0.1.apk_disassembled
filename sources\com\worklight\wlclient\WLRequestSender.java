package com.worklight.wlclient;

import com.worklight.common.Logger;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponse;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;

class WLRequestSender implements Runnable {
    private static final Logger logger = Logger.getInstance("wl.request");
    WLRequest request;

    protected WLRequestSender(WLRequest wLRequest) {
        this.request = wLRequest;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:68:0x01fc */
    public void run() {
        Logger.enter(getClass().getSimpleName(), "run");
        try {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Sending request ");
            sb.append(this.request.getOkRequest().url().toString());
            logger2.debug(sb.toString());
            Request userAgentHeader = WLNativeAPIUtils.setUserAgentHeader(this.request.getOkRequest());
            Builder newBuilder = HttpClientManager.getInstance().getOkHttpClient().newBuilder();
            newBuilder.readTimeout((long) this.request.getOptions().getTimeout(), TimeUnit.MILLISECONDS);
            newBuilder.connectTimeout((long) this.request.getOptions().getTimeout(), TimeUnit.MILLISECONDS);
            newBuilder.writeTimeout((long) this.request.getOptions().getTimeout(), TimeUnit.MILLISECONDS);
            WLResponse wLResponse = new WLResponse(newBuilder.build().newCall(userAgentHeader).execute());
            wLResponse.setOptions(this.request.getOptions());
            this.request.requestFinished(wLResponse);
            if (!(this.request.getOptions() == null || this.request.getOptions().getResponseListener() == null)) {
                synchronized (this.request.getOptions().getResponseListener()) {
                    this.request.getOptions().getResponseListener().notifyAll();
                }
            }
        } catch (SocketTimeoutException ) {
            this.request.getRequestListener().onFailure(new WLFailResponse(WLErrorCode.REQUEST_TIMEOUT, WLErrorCode.REQUEST_TIMEOUT.getDescription(), this.request.getOptions()));
            Logger.exit(getClass().getSimpleName(), "run");
            if (!(this.request.getOptions() == null || this.request.getOptions().getResponseListener() == null)) {
                synchronized (this.request.getOptions().getResponseListener()) {
                    this.request.getOptions().getResponseListener().notifyAll();
                }
            }
            return;
        } catch (IllegalArgumentException e) {
            WLRequestListener requestListener = this.request.getRequestListener();
            WLErrorCode wLErrorCode = WLErrorCode.ILLEGAL_ARGUMENT_EXCEPTION;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(WLErrorCode.ILLEGAL_ARGUMENT_EXCEPTION.getDescription());
            sb2.append(" ");
            sb2.append(e.getMessage());
            requestListener.onFailure(new WLFailResponse(wLErrorCode, sb2.toString(), this.request.getOptions()));
            if (!(this.request.getOptions() == null || this.request.getOptions().getResponseListener() == null)) {
                synchronized (this.request.getOptions().getResponseListener()) {
                    this.request.getOptions().getResponseListener().notifyAll();
                }
            }
        } catch (ConnectException e2) {
            e2.printStackTrace();
            WLRequestListener requestListener2 = this.request.getRequestListener();
            WLErrorCode wLErrorCode2 = WLErrorCode.UNEXPECTED_ERROR;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(e2.toString());
            sb3.append(" Please check the network connection");
            requestListener2.onFailure(new WLFailResponse(wLErrorCode2, sb3.toString(), this.request.getOptions()));
            if (!(this.request.getOptions() == null || this.request.getOptions().getResponseListener() == null)) {
                synchronized (this.request.getOptions().getResponseListener()) {
                    this.request.getOptions().getResponseListener().notifyAll();
                }
            }
        } catch (Exception e3) {
            try {
                e3.printStackTrace();
                this.request.getRequestListener().onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, e3.toString(), this.request.getOptions()));
                Logger.exit(getClass().getSimpleName(), "run");
                if (!(this.request.getOptions() == null || this.request.getOptions().getResponseListener() == null)) {
                    synchronized (this.request.getOptions().getResponseListener()) {
                        this.request.getOptions().getResponseListener().notifyAll();
                    }
                }
                return;
            } catch (Throwable th) {
                if (!(this.request.getOptions() == null || this.request.getOptions().getResponseListener() == null)) {
                    synchronized (this.request.getOptions().getResponseListener()) {
                        this.request.getOptions().getResponseListener().notifyAll();
                    }
                }
                throw th;
            }
        }
        Logger.exit(getClass().getSimpleName(), "run");
    }
}
