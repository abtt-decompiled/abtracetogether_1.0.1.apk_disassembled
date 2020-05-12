package com.worklight.wlclient;

import android.content.Context;
import com.worklight.common.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.Request;

public class AsynchronousRequestSender {
    private static final ExecutorService pool = Executors.newFixedThreadPool(6);
    private static AsynchronousRequestSender sender = new AsynchronousRequestSender();
    private Map<String, String> globalHeaders = new HashMap();

    private AsynchronousRequestSender() {
    }

    public static AsynchronousRequestSender getInstance() {
        return sender;
    }

    public static synchronized void reset(Context context) {
        synchronized (AsynchronousRequestSender.class) {
            sender.globalHeaders.clear();
            HttpClientManager.resetInstance(context);
        }
    }

    public void sendWLRequestAsync(WLRequest wLRequest) {
        String str = "sendWLRequestAsync";
        Logger.enter(getClass().getSimpleName(), str);
        addGlobalHeadersToRequest(wLRequest.getOkRequest());
        pool.execute(new WLRequestSender(wLRequest));
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void addGlobalHeader(String str, String str2) {
        String str3 = "addGlobalHeader";
        Logger.enter(getClass().getSimpleName(), str3);
        this.globalHeaders.put(str, str2);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public void removeGlobalHeader(String str) {
        String str2 = "removeGlobalHeader";
        Logger.enter(getClass().getSimpleName(), str2);
        this.globalHeaders.remove(str);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public Map<String, String> getGlobalHeaders() {
        return this.globalHeaders;
    }

    public Request addGlobalHeadersToRequest(Request request) {
        String str = "addGlobalHeadersToRequest";
        Logger.enter(getClass().getSimpleName(), str);
        for (Entry entry : this.globalHeaders.entrySet()) {
            String header = request.header((String) entry.getKey());
            if (header == null || !((String) entry.getValue()).equals(header)) {
                request = request.newBuilder().addHeader((String) entry.getKey(), (String) entry.getValue()).build();
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return request;
    }
}
