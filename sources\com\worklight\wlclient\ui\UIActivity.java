package com.worklight.wlclient.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import com.worklight.common.Logger;
import com.worklight.common.security.PRNGFixes;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLConstants;
import com.worklight.wlclient.api.challengehandler.SecurityCheckChallengeHandler;
import com.worklight.wlclient.challengehandler.RemoteDisableChallengeHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class UIActivity extends Activity {
    /* access modifiers changed from: private */
    public static Logger logger = Logger.getInstance("UIActivity");

    public void onCreate(Bundle bundle) {
        String str = "onCreate";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.setContext(this);
        super.onCreate(bundle);
        try {
            PRNGFixes.apply();
        } catch (Exception unused) {
            logger.error("Failed to apply Android PRNG secure random fixes.");
        }
        handleDialogue(getIntent().getStringExtra(WLConstants.ACTION_ID));
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        String str = "onCreateOptionsMenu";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return true;
    }

    private void handleDialogue(String str) {
        String str2 = "handleDialogue";
        Logger.enter(getClass().getSimpleName(), str2);
        Intent intent = getIntent();
        if (str.equalsIgnoreCase(WLConstants.REMOTE_DISABLE_ACTION)) {
            handleRemoteDisableDialogue(intent);
        } else if (str.equalsIgnoreCase(WLConstants.NOTIFY_ACTION)) {
            handleNotifyDialogue(intent);
        } else if (str.equalsIgnoreCase(WLConstants.EXIT_ACTION)) {
            handleExitDialogue(intent);
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void handleExitDialogue(Intent intent) {
        String str = "handleExitDialogue";
        Logger.enter(getClass().getSimpleName(), str);
        createAndShowDialogue(intent.getStringExtra(WLConstants.DIALOGUE_TITLE), intent.getStringExtra(WLConstants.DIALOGUE_MESSAGE), intent.getStringExtra(WLConstants.POSITIVE_BUTTON_TEXT), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String str = "onClick";
                Logger.enter(getClass().getSimpleName(), str);
                UIActivity.this.finish();
                Logger.exit(getClass().getSimpleName(), str);
            }
        }, null, null);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void handleNotifyDialogue(Intent intent) {
        String str = "handleNotifyDialogue";
        Logger.enter(getClass().getSimpleName(), str);
        String stringExtra = intent.getStringExtra(WLConstants.DIALOGUE_TITLE);
        String stringExtra2 = intent.getStringExtra(WLConstants.DIALOGUE_MESSAGE);
        final String stringExtra3 = intent.getStringExtra(WLConstants.DIALOGUE_MESSAGE_ID);
        createAndShowDialogue(stringExtra, stringExtra2, intent.getStringExtra(WLConstants.POSITIVE_BUTTON_TEXT), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String str = "onClick";
                Logger.enter(getClass().getSimpleName(), str);
                SecurityCheckChallengeHandler securityCheckChallengeHandler = WLClient.getInstance().getSecurityCheckChallengeHandler(WLConstants.REMOTE_DISABLE_ACTION);
                if (securityCheckChallengeHandler != null) {
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put(RemoteDisableChallengeHandler.PROTOCOL_MESSAGE_ID, stringExtra3);
                        securityCheckChallengeHandler.submitChallengeAnswer(jSONObject);
                    } catch (JSONException e) {
                        Logger access$000 = UIActivity.logger;
                        String str2 = RemoteDisableChallengeHandler.PROTOCOL_ERROR_MESSAGE;
                        access$000.error(str2, (Throwable) e);
                        throw new RuntimeException(str2);
                    }
                }
                UIActivity.this.finish();
                Logger.exit(getClass().getSimpleName(), str);
            }
        }, null, null);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void handleRemoteDisableDialogue(Intent intent) {
        OnClickListener onClickListener;
        String str;
        String str2 = "handleRemoteDisableDialogue";
        Logger.enter(getClass().getSimpleName(), str2);
        String stringExtra = intent.getStringExtra(WLConstants.DIALOGUE_TITLE);
        String stringExtra2 = intent.getStringExtra(WLConstants.DIALOGUE_MESSAGE);
        String stringExtra3 = intent.getStringExtra(WLConstants.POSITIVE_BUTTON_TEXT);
        AnonymousClass3 r6 = new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String str = "onClick";
                Logger.enter(getClass().getSimpleName(), str);
                UIActivity.this.finish();
                Logger.exit(getClass().getSimpleName(), str);
            }
        };
        final String stringExtra4 = intent.getStringExtra(WLConstants.DOWNLOAD_LINK);
        if (stringExtra4 != null) {
            str = intent.getStringExtra(WLConstants.NEUTRAL_BUTTON_TEXT);
            onClickListener = new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String str = "onClick";
                    Logger.enter(getClass().getSimpleName(), str);
                    UIActivity.this.openURL(stringExtra4);
                    UIActivity.this.finish();
                    Logger.exit(getClass().getSimpleName(), str);
                }
            };
        } else {
            str = null;
            onClickListener = null;
        }
        createAndShowDialogue(stringExtra, stringExtra2, stringExtra3, r6, str, onClickListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: private */
    public void openURL(String str) {
        String str2 = "openURL";
        Logger.enter(getClass().getSimpleName(), str2);
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private void exitApplication() {
        String str = "exitApplication";
        Logger.enter(getClass().getSimpleName(), str);
        finish();
        moveTaskToBack(true);
        Logger.exit(getClass().getSimpleName(), str);
    }

    private void createAndShowDialogue(String str, String str2, String str3, OnClickListener onClickListener, String str4, OnClickListener onClickListener2) {
        String str5 = "createAndShowDialogue";
        Logger.enter(getClass().getSimpleName(), str5);
        Builder builder = new Builder(this);
        builder.setTitle(str);
        builder.setMessage(str2);
        if (!(str3 == null || onClickListener == null)) {
            builder.setPositiveButton(str3, onClickListener);
        }
        if (!(str4 == null || onClickListener2 == null)) {
            builder.setNeutralButton(str4, onClickListener2);
        }
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        create.setCancelable(false);
        create.show();
        Logger.exit(getClass().getSimpleName(), str5);
    }
}
