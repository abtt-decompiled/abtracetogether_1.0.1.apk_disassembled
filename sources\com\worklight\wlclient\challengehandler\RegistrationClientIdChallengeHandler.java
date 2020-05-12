package com.worklight.wlclient.challengehandler;

import com.worklight.common.Logger;
import com.worklight.wlclient.api.challengehandler.SecurityCheckChallengeHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationClientIdChallengeHandler extends SecurityCheckChallengeHandler {
    public RegistrationClientIdChallengeHandler(String str) {
        super(str);
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

    public void handleChallenge(JSONObject jSONObject) {
        String str = "handleChallenge";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("response", jSONObject.getString("clientID"));
            submitChallengeAnswer(jSONObject2);
            Logger.exit(getClass().getSimpleName(), str);
        } catch (JSONException e) {
            Logger.exit(getClass().getSimpleName(), str);
            throw new RuntimeException(String.format("Failed to create json object: %S", new Object[]{e.getMessage()}));
        }
    }
}
