package com.worklight.wlclient.auth;

import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.wlclient.api.challengehandler.SecurityCheckChallengeHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class ClockSyncChallengeHandler extends SecurityCheckChallengeHandler {
    public static final String SERVER_TIME_STAMP = "serverTimeStamp";
    public static final int WARNING_TIME = 30000;
    private static Logger logger;

    public ClockSyncChallengeHandler(String str) {
        super(str);
        logger = Logger.getInstance(ClockSyncChallengeHandler.class.getSimpleName());
    }

    public void handleSuccess(JSONObject jSONObject) {
        String str = "handleSuccess";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            long j = jSONObject.getLong(SERVER_TIME_STAMP) - System.currentTimeMillis();
            WLConfig.getInstance().setServerRelativeTime(j);
            long abs = Math.abs(j);
            if (abs > 30000) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("The different between client time and server time is ");
                sb.append(abs / 1000);
                sb.append(" seconds");
                logger2.warn(sb.toString());
            }
            Logger.exit(getClass().getSimpleName(), str);
        } catch (JSONException e) {
            Logger.exit(getClass().getSimpleName(), str);
            throw new RuntimeException(String.format("Failed to get the server time stamp from JSON: %S", new Object[]{e.getMessage()}));
        }
    }

    public void handleFailure(JSONObject jSONObject) {
        String str = "handleFailure";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void handleChallenge(JSONObject jSONObject) {
        String str = "handleChallenge";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
    }
}
