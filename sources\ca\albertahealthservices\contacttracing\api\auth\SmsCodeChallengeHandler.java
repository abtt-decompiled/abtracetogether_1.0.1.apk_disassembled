package ca.albertahealthservices.contacttracing.api.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.challengehandler.SecurityCheckChallengeHandler;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONException;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\nJ\u0010\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u0010\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u0010H\u0016J\u0010\u0010\u0015\u001a\u00020\f2\b\u0010\u0016\u001a\u0004\u0018\u00010\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lca/albertahealthservices/contacttracing/api/auth/SmsCodeChallengeHandler;", "Lcom/worklight/wlclient/api/challengehandler/SecurityCheckChallengeHandler;", "()V", "broadcastManager", "Landroidx/localbroadcastmanager/content/LocalBroadcastManager;", "context", "Landroid/content/Context;", "errorMsg", "", "isChallenged", "", "cancelChallenge", "", "skipCancelledBroadcast", "handleChallenge", "jsonObject", "Lorg/json/JSONObject;", "handleFailure", "error", "handleSuccess", "identity", "verifySmsCode", "otp", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: SmsCodeChallengeHandler.kt */
public final class SmsCodeChallengeHandler extends SecurityCheckChallengeHandler {
    public static final String ACTION_CANCEL_CHALLENGE = "ACTION_CANCEL_CHALLENGE";
    public static final String ACTION_CHALLENGE_CANCELLED = "ACTION_CHALLENGE_CANCELLED";
    public static final String ACTION_VERIFY_SMS_CODE = "ACTION_VERIFY_SMS_CODE";
    public static final String ACTION_VERIFY_SMS_CODE_FAIL = "ACTION_VERIFY_SMS_CODE_FAIL";
    public static final String ACTION_VERIFY_SMS_CODE_REQUIRED = "ACTION_VERIFY_SMS_CODE_REQUIRED";
    public static final String ACTION_VERIFY_SMS_CODE_SUCCESS = "ACTION_VERIFY_SMS_CODE_SUCCESS";
    public static final Companion Companion = new Companion(null);
    public static final String EXTRA_SKIP_CANCELLED_BROADCAST = "EXTRA_SKIP_CANCELLED_BROADCAST";
    private final LocalBroadcastManager broadcastManager;
    private final Context context;
    private String errorMsg = "";
    private boolean isChallenged;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lca/albertahealthservices/contacttracing/api/auth/SmsCodeChallengeHandler$Companion;", "", "()V", "ACTION_CANCEL_CHALLENGE", "", "ACTION_CHALLENGE_CANCELLED", "ACTION_VERIFY_SMS_CODE", "ACTION_VERIFY_SMS_CODE_FAIL", "ACTION_VERIFY_SMS_CODE_REQUIRED", "ACTION_VERIFY_SMS_CODE_SUCCESS", "EXTRA_SKIP_CANCELLED_BROADCAST", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: SmsCodeChallengeHandler.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public SmsCodeChallengeHandler() {
        super("smsOtpService");
        WLClient instance = WLClient.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(instance, "WLClient.getInstance()");
        Context context2 = instance.getContext();
        Intrinsics.checkExpressionValueIsNotNull(context2, "WLClient.getInstance().context");
        this.context = context2;
        LocalBroadcastManager instance2 = LocalBroadcastManager.getInstance(context2);
        Intrinsics.checkExpressionValueIsNotNull(instance2, "LocalBroadcastManager.getInstance(context)");
        this.broadcastManager = instance2;
        instance2.registerReceiver(new BroadcastReceiver(this) {
            final /* synthetic */ SmsCodeChallengeHandler this$0;

            {
                this.this$0 = r1;
            }

            public void onReceive(Context context, Intent intent) {
                Intrinsics.checkParameterIsNotNull(intent, "intent");
                try {
                    this.this$0.verifySmsCode(intent.getStringExtra("otp"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new IntentFilter(ACTION_VERIFY_SMS_CODE));
        this.broadcastManager.registerReceiver(new BroadcastReceiver(this) {
            final /* synthetic */ SmsCodeChallengeHandler this$0;

            {
                this.this$0 = r1;
            }

            public void onReceive(Context context, Intent intent) {
                Intrinsics.checkParameterIsNotNull(intent, "intent");
                this.this$0.cancelChallenge(intent.getBooleanExtra(SmsCodeChallengeHandler.EXTRA_SKIP_CANCELLED_BROADCAST, false));
            }
        }, new IntentFilter(ACTION_CANCEL_CHALLENGE));
    }

    public void handleChallenge(JSONObject jSONObject) {
        String str;
        String str2 = "errorMsg";
        Intrinsics.checkParameterIsNotNull(jSONObject, "jsonObject");
        ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("Challenge Received - ");
        sb.append(jSONObject);
        companion.d("smsOTP", sb.toString());
        this.isChallenged = true;
        try {
            if (jSONObject.isNull(str2)) {
                str = "";
            } else {
                str = jSONObject.getString(str2);
                Intrinsics.checkExpressionValueIsNotNull(str, "jsonObject.getString(\"errorMsg\")");
            }
            this.errorMsg = str;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_VERIFY_SMS_CODE_REQUIRED);
        intent.putExtra(str2, this.errorMsg);
        this.broadcastManager.sendBroadcast(intent);
    }

    public void handleFailure(JSONObject jSONObject) {
        Intrinsics.checkParameterIsNotNull(jSONObject, "error");
        super.handleFailure(jSONObject);
        ca.albertahealthservices.contacttracing.logging.CentralLog.Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("handleFailure - ");
        sb.append(jSONObject);
        companion.d("smsOTP", sb.toString());
        this.isChallenged = false;
        String str = "failure";
        if (jSONObject.isNull(str)) {
            this.errorMsg = "Failed to veify . Please try again later.";
        } else {
            try {
                String string = jSONObject.getString(str);
                Intrinsics.checkExpressionValueIsNotNull(string, "error.getString(\"failure\")");
                this.errorMsg = string;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_VERIFY_SMS_CODE_FAIL);
        intent.putExtra("errorMsg", this.errorMsg);
        this.broadcastManager.sendBroadcast(intent);
    }

    public void handleSuccess(JSONObject jSONObject) {
        Intrinsics.checkParameterIsNotNull(jSONObject, "identity");
        super.handleSuccess(jSONObject);
        this.isChallenged = false;
        Intent intent = new Intent();
        intent.setAction(ACTION_VERIFY_SMS_CODE_SUCCESS);
        this.broadcastManager.sendBroadcast(intent);
        CentralLog.Companion.d("smsOTP", "handleSuccess");
    }

    public final void verifySmsCode(String str) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("code", str);
        submitChallengeAnswer(jSONObject);
    }

    public static /* synthetic */ void cancelChallenge$default(SmsCodeChallengeHandler smsCodeChallengeHandler, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        smsCodeChallengeHandler.cancelChallenge(z);
    }

    public final void cancelChallenge(boolean z) {
        String str = "smsOTP";
        try {
            cancel();
            if (!z) {
                Intent intent = new Intent();
                intent.setAction(ACTION_CHALLENGE_CANCELLED);
                this.broadcastManager.sendBroadcast(intent);
            }
            CentralLog.Companion.d(str, "canceledChallenge");
        } catch (NullPointerException unused) {
            CentralLog.Companion.d(str, "no challenge to cancel");
        }
    }
}
