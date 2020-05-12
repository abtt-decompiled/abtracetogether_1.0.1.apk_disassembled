package ca.albertahealthservices.contacttracing.fragment;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.api.Request;
import ca.albertahealthservices.contacttracing.api.Response;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.TypeCastException;
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
@DebugMetadata(c = "ca.albertahealthservices.contacttracing.fragment.EnterPinFragment$getUploadToken$1", f = "EnterPinFragment.kt", i = {0, 0, 0}, l = {125}, m = "invokeSuspend", n = {"$this$launch", "myParentFragment", "queryParams"}, s = {"L$0", "L$1", "L$2"})
/* compiled from: EnterPinFragment.kt */
final class EnterPinFragment$getUploadToken$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private CoroutineScope p$;
    final /* synthetic */ EnterPinFragment this$0;

    EnterPinFragment$getUploadToken$1(EnterPinFragment enterPinFragment, Continuation continuation) {
        this.this$0 = enterPinFragment;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        EnterPinFragment$getUploadToken$1 enterPinFragment$getUploadToken$1 = new EnterPinFragment$getUploadToken$1(this.this$0, continuation);
        enterPinFragment$getUploadToken$1.p$ = (CoroutineScope) obj;
        return enterPinFragment$getUploadToken$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((EnterPinFragment$getUploadToken$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        UploadPageFragment uploadPageFragment;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope = this.p$;
            CentralLog.Companion.d("EnterPinFragment", "Fetching upload token");
            Fragment parentFragment = this.this$0.getParentFragment();
            if (parentFragment != null) {
                UploadPageFragment uploadPageFragment2 = (UploadPageFragment) parentFragment;
                uploadPageFragment2.turnOnLoadingProgress();
                HashMap hashMap = new HashMap();
                hashMap.put("userId", Preference.INSTANCE.getUUID(this.this$0.getContext()));
                Request request = Request.INSTANCE;
                this.L$0 = coroutineScope;
                this.L$1 = uploadPageFragment2;
                this.L$2 = hashMap;
                this.label = 1;
                obj = Request.runRequest$default(request, "/adapters/getUploadTokenAdapter/getUploadToken", "GET", 0, null, null, hashMap, this, 28, null);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
                uploadPageFragment = uploadPageFragment2;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.fragment.UploadPageFragment");
            }
        } else if (i == 1) {
            HashMap hashMap2 = (HashMap) this.L$2;
            uploadPageFragment = (UploadPageFragment) this.L$1;
            CoroutineScope coroutineScope2 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Response response = (Response) obj;
        JSONObject data = response.getData();
        Object obj2 = data != null ? data.get("token") : null;
        if (response.isSuccess()) {
            Integer status = response.getStatus();
            if (status != null && status.intValue() == 200 && !(obj2 instanceof JSONObject)) {
                EnterPinFragment enterPinFragment = this.this$0;
                if (obj2 != null) {
                    enterPinFragment.uploadToken = (String) obj2;
                    uploadPageFragment.turnOffLoadingProgress();
                    return Unit.INSTANCE;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
            }
        }
        AppCompatTextView appCompatTextView = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
        String str = "enterPinFragmentErrorMessage";
        Intrinsics.checkExpressionValueIsNotNull(appCompatTextView, str);
        appCompatTextView.setText(this.this$0.getString(R.string.failed_to_send_pin));
        AppCompatTextView appCompatTextView2 = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
        Intrinsics.checkExpressionValueIsNotNull(appCompatTextView2, str);
        appCompatTextView2.setVisibility(0);
        uploadPageFragment.turnOffLoadingProgress();
        return Unit.INSTANCE;
    }
}
