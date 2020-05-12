package ca.albertahealthservices.contacttracing.fragment;

import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.status.persistence.StatusRecord;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord;
import com.google.gson.Gson;
import com.worklight.wlclient.WLRequest.RequestPaths;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "exportedData", "Lca/albertahealthservices/contacttracing/fragment/ExportData;", "kotlin.jvm.PlatformType", "accept", "ca/albertahealthservices/contacttracing/fragment/EnterPinFragment$getEncounterJSON$2$2"}, k = 3, mv = {1, 1, 16})
/* compiled from: EnterPinFragment.kt */
final class EnterPinFragment$getEncounterJSON$$inlined$suspendCoroutine$lambda$1<T> implements Consumer<ExportData> {
    final /* synthetic */ Continuation $cont;
    final /* synthetic */ EnterPinFragment this$0;

    EnterPinFragment$getEncounterJSON$$inlined$suspendCoroutine$lambda$1(Continuation continuation, EnterPinFragment enterPinFragment) {
        this.$cont = continuation;
        this.this$0 = enterPinFragment;
    }

    public final void accept(ExportData exportData) {
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("records: ");
        sb.append(exportData.getRecordList());
        String sb2 = sb.toString();
        String str = EnterPinFragment.TAG;
        companion.d(str, sb2);
        Companion companion2 = CentralLog.Companion;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("status: ");
        sb3.append(exportData.getStatusList());
        companion2.d(str, sb3.toString());
        Gson gson = new Gson();
        Iterable<StreetPassRecord> recordList = exportData.getRecordList();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(recordList, 10));
        for (StreetPassRecord streetPassRecord : recordList) {
            streetPassRecord.setTimestamp(streetPassRecord.getTimestamp() / ((long) 1000));
            arrayList.add(streetPassRecord);
        }
        List list = (List) arrayList;
        Iterable<StatusRecord> statusList = exportData.getStatusList();
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(statusList, 10));
        for (StatusRecord statusRecord : statusList) {
            statusRecord.setTimestamp(statusRecord.getTimestamp() / ((long) 1000));
            arrayList2.add(statusRecord);
        }
        List list2 = (List) arrayList2;
        Map hashMap = new HashMap();
        String access$getUploadToken$p = this.this$0.uploadToken;
        if (access$getUploadToken$p != null) {
            hashMap.put("token", access$getUploadToken$p);
            hashMap.put("records", list);
            hashMap.put(RequestPaths.EVENTS, list2);
            String json = gson.toJson((Object) hashMap);
            Continuation continuation = this.$cont;
            JSONObject jSONObject = new JSONObject(json);
            Result.Companion companion3 = Result.Companion;
            continuation.resumeWith(Result.m3constructorimpl(jSONObject));
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Any");
    }
}
