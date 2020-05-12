package com.worklight.wlclient.api.challengehandler;

import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.wlclient.HttpClientManager;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody.Builder;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class GatewayChallengeHandler extends BaseChallengeHandler<WLResponse> implements WLResponseListener {
    public static int DEFAULT_TIMEOUT_IN_MILLISECONDS = 10000;
    private static Logger logger = Logger.getInstance("GatewayChallengeHandler");

    public abstract boolean canHandleResponse(WLResponse wLResponse);

    public GatewayChallengeHandler(String str) {
        super(str);
    }

    /* access modifiers changed from: protected */
    public void submitSuccess(WLResponse wLResponse) {
        Logger.enter(getClass().getSimpleName(), "submitSuccess");
        synchronized (this) {
            this.activeRequest.removeExpectedAnswer(getHandlerName());
            this.activeRequest = null;
            releaseWaitingList();
        }
        Logger.exit(getClass().getSimpleName(), "submitSuccess");
    }

    /* access modifiers changed from: protected */
    public void submitLoginForm(String str, Map<String, String> map, Map<String, String> map2, int i, String str2) {
        Request request;
        String str3 = "submitLoginForm";
        Logger.enter(getClass().getSimpleName(), str3);
        logger.debug("Request [login]");
        if (str.indexOf("http") != 0 || str.indexOf(":") <= 0) {
            String externalForm = WLConfig.getInstance().getAppURL().toExternalForm();
            if (externalForm.charAt(externalForm.length() - 1) == '/' && str.length() > 0 && str.charAt(0) == '/') {
                str = str.substring(1);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(externalForm);
            sb.append(str);
            str = sb.toString();
        }
        boolean equalsIgnoreCase = str2.equalsIgnoreCase("post");
        String str4 = WLConfig.WL_X_VERSION_HEADER;
        if (equalsIgnoreCase) {
            Builder builder = new Builder();
            for (Entry entry : map.entrySet()) {
                builder.add((String) entry.getKey(), (String) entry.getValue());
            }
            request = new Request.Builder().url(str).addHeader(str4, WLConfig.getInstance().getApplicationVersion()).post(builder.build()).build();
        } else if (str2.equalsIgnoreCase("get")) {
            HttpUrl.Builder newBuilder = HttpUrl.parse(str).newBuilder();
            for (String str5 : map.keySet()) {
                newBuilder.addQueryParameter(str5, (String) map.get(str5));
            }
            try {
                String url = newBuilder.build().url().toString();
                Logger logger2 = logger;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("final url ");
                sb2.append(url);
                logger2.info(sb2.toString());
                request = new Request.Builder().url(url).addHeader(str4, WLConfig.getInstance().getApplicationVersion()).build();
            } catch (Exception e) {
                Logger logger3 = logger;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("unexpected error: failed to  addQueryParamsToUrl ");
                sb3.append(e.getLocalizedMessage());
                logger3.error(sb3.toString());
                Logger.exit(getClass().getSimpleName(), str3);
                throw new Error(e);
            }
        } else {
            Logger.exit(getClass().getSimpleName(), str3);
            throw new RuntimeException("CustomChallengeHandler.submitLoginForm: invalid request method.");
        }
        if (map2 != null) {
            for (String str6 : map2.keySet()) {
                request = request.newBuilder().addHeader(str6, (String) map2.get(str6)).build();
            }
        }
        if (i < 0) {
            i = DEFAULT_TIMEOUT_IN_MILLISECONDS;
        }
        OkHttpClient.Builder newBuilder2 = HttpClientManager.getInstance().getOkHttpClient().newBuilder();
        long j = (long) i;
        newBuilder2.readTimeout(j, TimeUnit.MILLISECONDS);
        newBuilder2.connectTimeout(j, TimeUnit.MILLISECONDS);
        newBuilder2.writeTimeout(j, TimeUnit.MILLISECONDS);
        newBuilder2.build().newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                String str = "onFailure";
                Logger.enter(getClass().getSimpleName(), str);
                GatewayChallengeHandler.this.handleOnFailure(iOException);
                Logger.exit(getClass().getSimpleName(), str);
            }

            public void onResponse(Call call, Response response) throws IOException {
                String str = "onResponse";
                Logger.enter(getClass().getSimpleName(), str);
                GatewayChallengeHandler.this.handleOnSuccess(response);
                Logger.exit(getClass().getSimpleName(), str);
            }
        });
        Logger.exit(getClass().getSimpleName(), str3);
    }

    /* access modifiers changed from: private */
    public void handleOnSuccess(Response response) {
        String str = "handleOnSuccess";
        Logger.enter(getClass().getSimpleName(), str);
        if (response.isSuccessful()) {
            onSuccess(new WLResponse(response));
        } else {
            onFailure(new WLFailResponse(response));
        }
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* access modifiers changed from: private */
    public void handleOnFailure(IOException iOException) {
        String str = "handleOnFailure";
        Logger.enter(getClass().getSimpleName(), str);
        if (iOException instanceof SocketTimeoutException) {
            onFailure(new WLFailResponse(WLErrorCode.REQUEST_TIMEOUT, WLErrorCode.REQUEST_TIMEOUT.getDescription(), null));
        } else {
            onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, iOException.getMessage(), null));
        }
        Logger.exit(getClass().getSimpleName(), str);
    }
}
