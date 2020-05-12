package ca.albertahealthservices.contacttracing.streetpass.persistence;

import android.content.Context;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\u0006\u0010\u000e\u001a\u00020\u000fJ\u0019\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H@ø\u0001\u0000¢\u0006\u0002\u0010\u0013J\u0019\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\rH@ø\u0001\u0000¢\u0006\u0002\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u0002\u0004\n\u0002\b\u0019¨\u0006\u0017"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordStorage;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getContext", "()Landroid/content/Context;", "recordDao", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDao;", "getRecordDao", "()Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDao;", "getAllRecords", "", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecord;", "nukeDb", "", "purgeOldRecords", "before", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveRecord", "record", "(Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecord;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: StreetPassRecordStorage.kt */
public final class StreetPassRecordStorage {
    private final Context context;
    private final StreetPassRecordDao recordDao = StreetPassRecordDatabase.Companion.getDatabase(this.context).recordDao();

    public StreetPassRecordStorage(Context context2) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        this.context = context2;
    }

    public final Context getContext() {
        return this.context;
    }

    public final StreetPassRecordDao getRecordDao() {
        return this.recordDao;
    }

    public final Object saveRecord(StreetPassRecord streetPassRecord, Continuation<? super Unit> continuation) {
        Object insert = this.recordDao.insert(streetPassRecord, continuation);
        if (insert == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return insert;
        }
        return Unit.INSTANCE;
    }

    public final void nukeDb() {
        this.recordDao.nukeDb();
    }

    public final List<StreetPassRecord> getAllRecords() {
        return this.recordDao.getCurrentRecords();
    }

    public final Object purgeOldRecords(long j, Continuation<? super Unit> continuation) {
        Object purgeOldRecords = this.recordDao.purgeOldRecords(j, continuation);
        if (purgeOldRecords == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return purgeOldRecords;
        }
        return Unit.INSTANCE;
    }
}
