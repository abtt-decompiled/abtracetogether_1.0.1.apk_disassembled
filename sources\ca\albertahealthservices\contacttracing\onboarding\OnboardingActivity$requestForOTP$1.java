package ca.albertahealthservices.contacttracing.onboarding;

import android.content.Context;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.api.ErrorCode;
import ca.albertahealthservices.contacttracing.api.Request;
import ca.albertahealthservices.contacttracing.api.Response;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 16})
@DebugMetadata(c = "ca.albertahealthservices.contacttracing.onboarding.OnboardingActivity$requestForOTP$1", f = "OnboardingActivity.kt", i = {0, 0, 1}, l = {466, 479}, m = "invokeSuspend", n = {"$this$launch", "continueToOtp", "$this$launch"}, s = {"L$0", "I$0", "L$0"})
/* compiled from: OnboardingActivity.kt */
final class OnboardingActivity$requestForOTP$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $phoneNumber;
    final /* synthetic */ boolean $skipRegister;
    int I$0;
    Object L$0;
    int label;
    private CoroutineScope p$;
    final /* synthetic */ OnboardingActivity this$0;

    OnboardingActivity$requestForOTP$1(OnboardingActivity onboardingActivity, boolean z, String str, Continuation continuation) {
        this.this$0 = onboardingActivity;
        this.$skipRegister = z;
        this.$phoneNumber = str;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        OnboardingActivity$requestForOTP$1 onboardingActivity$requestForOTP$1 = new OnboardingActivity$requestForOTP$1(this.this$0, this.$skipRegister, this.$phoneNumber, continuation);
        onboardingActivity$requestForOTP$1.p$ = (CoroutineScope) obj;
        return onboardingActivity$requestForOTP$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((OnboardingActivity$requestForOTP$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0113  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        Response response;
        Object obj3;
        Object obj4;
        int i;
        Object obj5;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = this.label;
        int i3 = 0;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            obj3 = this.p$;
            this.this$0.resendingCode = false;
            if (!this.$skipRegister) {
                Request request = Request.INSTANCE;
                StringBuilder sb = new StringBuilder();
                sb.append("/adapters/smsOtpService/phone/register/");
                sb.append(this.$phoneNumber);
                String sb2 = sb.toString();
                this.L$0 = obj3;
                this.I$0 = 1;
                this.label = 1;
                obj4 = obj3;
                obj5 = Request.runRequest$default(request, sb2, "POST", 0, null, null, null, this, 60, null);
                if (obj5 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                i = 1;
            } else {
                Object obj6 = obj3;
                i3 = 1;
                if (i3 != 0) {
                    Request request2 = Request.INSTANCE;
                    JSONObject jSONObject = new JSONObject();
                    this.L$0 = obj3;
                    this.label = 2;
                    obj2 = Request.runRequest$default(request2, "/adapters/smsOtpService/phone/verifySmsOTP", "POST", 0, null, jSONObject, null, this, 44, null);
                    if (obj2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    response = (Response) obj2;
                    if (response.isSuccess()) {
                    }
                    if (!Intrinsics.areEqual((Object) response.getErrorCode(), (Object) ErrorCode.INSTANCE.getCHALLENGE_HANDLING_CANCELED())) {
                    }
                    this.this$0.enableFragmentbutton();
                }
                return Unit.INSTANCE;
            }
        } else if (i2 == 1) {
            int i4 = this.I$0;
            CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            obj4 = coroutineScope;
            i = i4;
            obj5 = obj;
        } else if (i2 == 2) {
            CoroutineScope coroutineScope2 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            obj2 = obj;
            response = (Response) obj2;
            if (response.isSuccess()) {
                Integer status = response.getStatus();
                if (!(status == null || status.intValue() != 200 || response.getData() == null)) {
                    try {
                        Preference preference = Preference.INSTANCE;
                        Context applicationContext = this.this$0.getApplicationContext();
                        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "applicationContext");
                        String string = response.getData().getString("userId");
                        Intrinsics.checkExpressionValueIsNotNull(string, "triggerSmsOTPResponse.data.getString(\"userId\")");
                        preference.putUUID(applicationContext, string);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.this$0.requestTempIdsIfNeeded();
                    return Unit.INSTANCE;
                }
            }
            if (!Intrinsics.areEqual((Object) response.getErrorCode(), (Object) ErrorCode.INSTANCE.getCHALLENGE_HANDLING_CANCELED())) {
                OnboardingActivity onboardingActivity = this.this$0;
                String string2 = onboardingActivity.getString(R.string.invalid_otp);
                Intrinsics.checkExpressionValueIsNotNull(string2, "getString(R.string.invalid_otp)");
                onboardingActivity.updateOTPError(string2);
            }
            this.this$0.enableFragmentbutton();
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Response response2 = (Response) obj5;
        Integer status2 = response2.getStatus();
        if (status2 != null && status2.intValue() == 200) {
            i3 = i;
        } else {
            this.this$0.updatePhoneNumberError(response2.getError());
            this.this$0.enableFragmentbutton();
        }
        obj3 = obj4;
        if (i3 != 0) {
        }
        return Unit.INSTANCE;
    }
}
