package com.worklight.wlclient.challengehandler;

import android.content.Context;
import android.content.Intent;
import com.worklight.common.Logger;
import com.worklight.common.security.AppAuthenticityToken;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLConstants;
import com.worklight.wlclient.api.challengehandler.SecurityCheckChallengeHandler;
import com.worklight.wlclient.ui.UIActivity;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticityChallengeHandler extends SecurityCheckChallengeHandler {
    private static final String APP_AUTHENTICITY_RESPONSE = "appAuthenticityResponse";
    private static final String AUTH_FAIL_ID = "WLClient.authFailure";
    private static final String CLOSE_ID = "WLClient.close";
    private static final String INIT_FAILURE_TITLE_ID = "WLClient.wlclientInitFailure";
    private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";
    private static Logger logger = Logger.getInstance("AuthenticityChallengeHandler");

    public AuthenticityChallengeHandler(String str) {
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
        ResourceBundle messagesBundle = WLUtils.getMessagesBundle();
        Context context = WLClient.getInstance().getContext();
        Intent intent = new Intent(context, UIActivity.class);
        intent.putExtra(WLConstants.ACTION_ID, WLConstants.EXIT_ACTION);
        intent.putExtra(WLConstants.DIALOGUE_MESSAGE, messagesBundle.getString(AUTH_FAIL_ID));
        intent.putExtra(WLConstants.DIALOGUE_TITLE, messagesBundle.getString(INIT_FAILURE_TITLE_ID));
        intent.putExtra(WLConstants.POSITIVE_BUTTON_TEXT, messagesBundle.getString(CLOSE_ID));
        intent.addFlags(268435456);
        context.startActivity(intent);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void handleChallenge(JSONObject jSONObject) {
        String str = "handleChallenge";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            String string = jSONObject.getString("challenge");
            AppAuthenticityToken appAuthenticityToken = new AppAuthenticityToken();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(APP_AUTHENTICITY_RESPONSE, appAuthenticityToken.a1(WLClient.getInstance().getContext(), string.substring(0, string.indexOf(43))));
            submitChallengeAnswer(jSONObject2);
        } catch (JSONException e) {
            logger.debug(e.getMessage());
        }
        Logger.exit(getClass().getSimpleName(), str);
    }
}
