package ca.albertahealthservices.contacttracing.fragment;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.api.Request;
import ca.albertahealthservices.contacttracing.api.Response;
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
@DebugMetadata(c = "ca.albertahealthservices.contacttracing.fragment.EnterPinFragment$uploadData$1", f = "EnterPinFragment.kt", i = {0, 0, 1, 1, 1, 1}, l = {141, 150}, m = "invokeSuspend", n = {"$this$launch", "myParentFragment", "$this$launch", "myParentFragment", "jsonData", "queryParams"}, s = {"L$0", "L$1", "L$0", "L$1", "L$2", "L$3"})
/* compiled from: EnterPinFragment.kt */
final class EnterPinFragment$uploadData$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private CoroutineScope p$;
    final /* synthetic */ EnterPinFragment this$0;

    EnterPinFragment$uploadData$1(EnterPinFragment enterPinFragment, Continuation continuation) {
        this.this$0 = enterPinFragment;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        EnterPinFragment$uploadData$1 enterPinFragment$uploadData$1 = new EnterPinFragment$uploadData$1(this.this$0, continuation);
        enterPinFragment$uploadData$1.p$ = (CoroutineScope) obj;
        return enterPinFragment$uploadData$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((EnterPinFragment$uploadData$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0108  */
    public final Object invokeSuspend(Object obj) {
        UploadPageFragment uploadPageFragment;
        Object obj2;
        Response response;
        Object obj3;
        CoroutineScope coroutineScope;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        String str = "enterPinFragmentErrorMessage";
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            coroutineScope = this.p$;
            Fragment parentFragment = this.this$0.getParentFragment();
            if (parentFragment != null) {
                UploadPageFragment uploadPageFragment2 = (UploadPageFragment) parentFragment;
                uploadPageFragment2.turnOnLoadingProgress();
                EnterPinFragment enterPinFragment = this.this$0;
                this.L$0 = coroutineScope;
                this.L$1 = uploadPageFragment2;
                this.label = 1;
                obj3 = enterPinFragment.getEncounterJSON(this);
                if (obj3 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                uploadPageFragment = uploadPageFragment2;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.fragment.UploadPageFragment");
            }
        } else if (i == 1) {
            UploadPageFragment uploadPageFragment3 = (UploadPageFragment) this.L$1;
            CoroutineScope coroutineScope2 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            uploadPageFragment = uploadPageFragment3;
            coroutineScope = coroutineScope2;
            obj3 = obj;
        } else if (i == 2) {
            HashMap hashMap = (HashMap) this.L$3;
            JSONObject jSONObject = (JSONObject) this.L$2;
            UploadPageFragment uploadPageFragment4 = (UploadPageFragment) this.L$1;
            CoroutineScope coroutineScope3 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            uploadPageFragment = uploadPageFragment4;
            obj2 = obj;
            response = (Response) obj2;
            uploadPageFragment.turnOffLoadingProgress();
            if (response.isSuccess()) {
                AppCompatTextView appCompatTextView = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
                Intrinsics.checkExpressionValueIsNotNull(appCompatTextView, str);
                appCompatTextView.setText(this.this$0.getString(R.string.failed_to_upload_data));
                AppCompatTextView appCompatTextView2 = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
                Intrinsics.checkExpressionValueIsNotNull(appCompatTextView2, str);
                appCompatTextView2.setVisibility(0);
            } else {
                uploadPageFragment.navigateToUploadComplete();
            }
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        JSONObject jSONObject2 = (JSONObject) obj3;
        if (jSONObject2.length() == 0) {
            AppCompatTextView appCompatTextView3 = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView3, str);
            appCompatTextView3.setText(this.this$0.getString(R.string.no_encounter_data_available));
            AppCompatTextView appCompatTextView4 = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView4, str);
            appCompatTextView4.setVisibility(0);
            return Unit.INSTANCE;
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put("userId", Preference.INSTANCE.getUUID(this.this$0.getContext()));
        Request request = Request.INSTANCE;
        this.L$0 = coroutineScope;
        this.L$1 = uploadPageFragment;
        this.L$2 = jSONObject2;
        this.L$3 = hashMap2;
        this.label = 2;
        obj2 = Request.runRequest$default(request, "/adapters/uploadData/uploadData", "POST", 0, null, jSONObject2, hashMap2, this, 12, null);
        if (obj2 == coroutine_suspended) {
            return coroutine_suspended;
        }
        response = (Response) obj2;
        uploadPageFragment.turnOffLoadingProgress();
        if (response.isSuccess()) {
        }
        return Unit.INSTANCE;
    }
}
