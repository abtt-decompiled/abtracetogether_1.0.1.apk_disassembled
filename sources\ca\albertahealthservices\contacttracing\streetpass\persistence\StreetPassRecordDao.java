package ca.albertahealthservices.contacttracing.streetpass.persistence;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H'J\u0010\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0006H'J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u0006H'J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\t\u001a\u00020\nH'J\u0019\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004H§@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\fH'J\u0019\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0012H§@ø\u0001\u0000¢\u0006\u0002\u0010\u0013\u0002\u0004\n\u0002\b\u0019¨\u0006\u0014"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDao;", "", "getCurrentRecords", "", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecord;", "getMostRecentRecord", "Landroidx/lifecycle/LiveData;", "getRecords", "getRecordsViaQuery", "query", "Landroidx/sqlite/db/SupportSQLiteQuery;", "insert", "", "record", "(Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecord;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "nukeDb", "purgeOldRecords", "before", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: StreetPassRecordDao.kt */
public interface StreetPassRecordDao {
    List<StreetPassRecord> getCurrentRecords();

    LiveData<StreetPassRecord> getMostRecentRecord();

    LiveData<List<StreetPassRecord>> getRecords();

    List<StreetPassRecord> getRecordsViaQuery(SupportSQLiteQuery supportSQLiteQuery);

    Object insert(StreetPassRecord streetPassRecord, Continuation<? super Unit> continuation);

    void nukeDb();

    Object purgeOldRecords(long j, Continuation<? super Unit> continuation);
}
