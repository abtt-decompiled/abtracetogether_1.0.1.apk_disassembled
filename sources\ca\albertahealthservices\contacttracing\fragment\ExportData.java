package ca.albertahealthservices.contacttracing.fragment;

import ca.albertahealthservices.contacttracing.status.persistence.StatusRecord;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B!\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003¢\u0006\u0002\u0010\u0007J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003HÆ\u0003J)\u0010\r\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\t¨\u0006\u0015"}, d2 = {"Lca/albertahealthservices/contacttracing/fragment/ExportData;", "", "recordList", "", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecord;", "statusList", "Lca/albertahealthservices/contacttracing/status/persistence/StatusRecord;", "(Ljava/util/List;Ljava/util/List;)V", "getRecordList", "()Ljava/util/List;", "getStatusList", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: UploadPageFragment.kt */
public final class ExportData {
    private final List<StreetPassRecord> recordList;
    private final List<StatusRecord> statusList;

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<ca.albertahealthservices.contacttracing.status.persistence.StatusRecord>, for r2v0, types: [java.util.List] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord>, for r1v0, types: [java.util.List] */
    public static /* synthetic */ ExportData copy$default(ExportData exportData, List<StreetPassRecord> list, List<StatusRecord> list2, int i, Object obj) {
        if ((i & 1) != 0) {
            list = exportData.recordList;
        }
        if ((i & 2) != 0) {
            list2 = exportData.statusList;
        }
        return exportData.copy(list, list2);
    }

    public final List<StreetPassRecord> component1() {
        return this.recordList;
    }

    public final List<StatusRecord> component2() {
        return this.statusList;
    }

    public final ExportData copy(List<StreetPassRecord> list, List<StatusRecord> list2) {
        Intrinsics.checkParameterIsNotNull(list, "recordList");
        Intrinsics.checkParameterIsNotNull(list2, "statusList");
        return new ExportData(list, list2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.statusList, (java.lang.Object) r3.statusList) != false) goto L_0x001f;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof ExportData) {
                ExportData exportData = (ExportData) obj;
                if (Intrinsics.areEqual((Object) this.recordList, (Object) exportData.recordList)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        List<StreetPassRecord> list = this.recordList;
        int i = 0;
        int hashCode = (list != null ? list.hashCode() : 0) * 31;
        List<StatusRecord> list2 = this.statusList;
        if (list2 != null) {
            i = list2.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExportData(recordList=");
        sb.append(this.recordList);
        sb.append(", statusList=");
        sb.append(this.statusList);
        sb.append(")");
        return sb.toString();
    }

    public ExportData(List<StreetPassRecord> list, List<StatusRecord> list2) {
        Intrinsics.checkParameterIsNotNull(list, "recordList");
        Intrinsics.checkParameterIsNotNull(list2, "statusList");
        this.recordList = list;
        this.statusList = list2;
    }

    public final List<StreetPassRecord> getRecordList() {
        return this.recordList;
    }

    public final List<StatusRecord> getStatusList() {
        return this.statusList;
    }
}
