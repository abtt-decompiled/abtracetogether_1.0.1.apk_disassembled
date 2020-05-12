package com.worklight.wlclient.challengehandler;

import android.content.Context;
import android.content.Intent;
import com.worklight.common.Logger;
import com.worklight.common.WLConfig;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLConstants;
import com.worklight.wlclient.api.challengehandler.SecurityCheckChallengeHandler;
import com.worklight.wlclient.ui.UIActivity;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;

public class RemoteDisableChallengeHandler extends SecurityCheckChallengeHandler {
    private static final String APPLICATION_DISABLED_ID = "WLClient.applicationDenied";
    private static final String CLOSE_ID = "WLClient.close";
    private static final String NOTIFICATION_TITLE_ID = "WLClient.notificationTitle";
    private static final String NOTIFY_MESAGE = "NOTIFY";
    private static final String PROTOCOL_DOWNLOAD_LINK = "downloadLink";
    public static final String PROTOCOL_ERROR_MESSAGE = "Protocol Error - could not parse JSON object";
    private static final String PROTOCOL_MESSAGE = "message";
    public static final String PROTOCOL_MESSAGE_ID = "messageId";
    private static final String PROTOCOL_MESSAGE_TYPE = "messageType";
    private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";
    private static final String UPGRADE_ID = "WLClient.upgrade";
    private static Logger logger = Logger.getInstance("RemoteDisableChallengeHandler");

    public RemoteDisableChallengeHandler(String str) {
        super(str);
    }

    public void handleSuccess(JSONObject jSONObject) {
        String str = "handleSuccess";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void handleFailure(JSONObject jSONObject) {
        String str = PROTOCOL_DOWNLOAD_LINK;
        try {
            String string = jSONObject.getString(PROTOCOL_MESSAGE);
            String str2 = null;
            if (jSONObject.has(str)) {
                str2 = jSONObject.getString(str);
            }
            createAndShowDisableDialogue(string, str2);
        } catch (JSONException e) {
            Logger logger2 = logger;
            String str3 = PROTOCOL_ERROR_MESSAGE;
            logger2.error(str3, (Throwable) e);
            throw new RuntimeException(str3);
        }
    }

    private void createAndShowDisableDialogue(String str, String str2) {
        String str3 = "createAndShowDisableDialogue";
        Logger.enter(getClass().getSimpleName(), str3);
        ResourceBundle messagesBundle = WLUtils.getMessagesBundle();
        Context context = WLClient.getInstance().getContext();
        Intent intent = new Intent(context, UIActivity.class);
        intent.putExtra(WLConstants.ACTION_ID, WLConstants.REMOTE_DISABLE_ACTION);
        intent.putExtra(WLConstants.DIALOGUE_MESSAGE, str);
        intent.putExtra(WLConstants.DIALOGUE_TITLE, messagesBundle.getString(APPLICATION_DISABLED_ID));
        intent.putExtra(WLConstants.POSITIVE_BUTTON_TEXT, messagesBundle.getString(CLOSE_ID));
        if (!(str2 == null || str2.length() == 0 || str2.equalsIgnoreCase("null"))) {
            intent.putExtra(WLConstants.DOWNLOAD_LINK, str2);
            intent.putExtra(WLConstants.NEUTRAL_BUTTON_TEXT, messagesBundle.getString(UPGRADE_ID));
        }
        intent.addFlags(268435456);
        context.startActivity(intent);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public void handleChallenge(JSONObject jSONObject) {
        String str = PROTOCOL_MESSAGE_ID;
        String str2 = "handleChallenge";
        Logger.enter(getClass().getSimpleName(), str2);
        try {
            String string = jSONObject.getString(PROTOCOL_MESSAGE);
            String string2 = jSONObject.getString(str);
            if (isDisplayMessageDialogue(WLConfig.getInstance().readWLPref(str), string2, jSONObject.getString(PROTOCOL_MESSAGE_TYPE))) {
                createAndShowMessageDialogue(string, string2);
                WLConfig.getInstance().writeWLPref(str, string2);
            } else {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(str, string2);
                submitChallengeAnswer(jSONObject2);
            }
            Logger.exit(getClass().getSimpleName(), str2);
        } catch (JSONException e) {
            Logger logger2 = logger;
            String str3 = PROTOCOL_ERROR_MESSAGE;
            logger2.error(str3, (Throwable) e);
            Logger.exit(getClass().getSimpleName(), str2);
            throw new RuntimeException(str3);
        }
    }

    private void createAndShowMessageDialogue(String str, String str2) {
        String str3 = "createAndShowMessageDialogue";
        Logger.enter(getClass().getSimpleName(), str3);
        ResourceBundle messagesBundle = WLUtils.getMessagesBundle();
        Context context = WLClient.getInstance().getContext();
        Intent intent = new Intent(context, UIActivity.class);
        intent.putExtra(WLConstants.ACTION_ID, WLConstants.NOTIFY_ACTION);
        intent.putExtra(WLConstants.DIALOGUE_MESSAGE, str);
        intent.putExtra(WLConstants.DIALOGUE_TITLE, messagesBundle.getString(NOTIFICATION_TITLE_ID));
        intent.putExtra(WLConstants.POSITIVE_BUTTON_TEXT, messagesBundle.getString(CLOSE_ID));
        intent.putExtra(WLConstants.DIALOGUE_MESSAGE_ID, str2);
        intent.addFlags(268435456);
        context.startActivity(intent);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    private boolean isDisplayMessageDialogue(String str, String str2, String str3) {
        String str4 = "isDisplayMessageDialogue";
        Logger.enter(getClass().getSimpleName(), str4);
        if (str3 == null || !str3.equalsIgnoreCase(NOTIFY_MESAGE)) {
            Logger.exit(getClass().getSimpleName(), str4);
            return true;
        } else if (str == null || !str.equalsIgnoreCase(str2)) {
            Logger.exit(getClass().getSimpleName(), str4);
            return true;
        } else {
            Logger.exit(getClass().getSimpleName(), str4);
            return false;
        }
    }
}
