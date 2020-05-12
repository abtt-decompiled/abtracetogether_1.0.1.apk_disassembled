package com.worklight.wlclient.api.challengehandler;

import com.worklight.common.Logger;
import org.json.JSONObject;

public abstract class SecurityCheckChallengeHandler extends BaseChallengeHandler<JSONObject> {
    private Logger logger = Logger.getInstance(SecurityCheckChallengeHandler.class.getSimpleName());

    public SecurityCheckChallengeHandler(String str) {
        super(str);
    }

    public void submitChallengeAnswer(JSONObject jSONObject) {
        String str = "submitChallengeAnswer";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.activeRequest == null) {
            this.logger.error("submitAnswer has been called for unknown request");
        } else if (jSONObject == null) {
            this.activeRequest.removeExpectedAnswer(getHandlerName());
        } else {
            this.activeRequest.submitAnswer(getHandlerName(), jSONObject);
        }
        this.activeRequest = null;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void handleSuccess(JSONObject jSONObject) {
        String str = "handleSuccess";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void handleFailure(JSONObject jSONObject) {
        String str = "handleFailure";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
    }
}
