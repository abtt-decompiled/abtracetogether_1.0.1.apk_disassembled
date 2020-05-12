package ca.albertahealthservices.contacttracing.streetpass.persistence;

import androidx.lifecycle.LiveData;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0019\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH@ø\u0001\u0000¢\u0006\u0002\u0010\u000eR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordRepository;", "", "recordDao", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDao;", "(Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDao;)V", "allRecords", "Landroidx/lifecycle/LiveData;", "", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecord;", "getAllRecords", "()Landroidx/lifecycle/LiveData;", "insert", "", "word", "(Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecord;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: StreetPassRecordRepository.kt */
public final class StreetPassRecordRepository {
    private final LiveData<List<StreetPassRecord>> allRecords;
    private final StreetPassRecordDao recordDao;

    public StreetPassRecordRepository(StreetPassRecordDao streetPassRecordDao) {
        Intrinsics.checkParameterIsNotNull(streetPassRecordDao, "recordDao");
        this.recordDao = streetPassRecordDao;
        this.allRecords = streetPassRecordDao.getRecords();
    }

    public final LiveData<List<StreetPassRecord>> getAllRecords() {
        return this.allRecords;
    }

    public final Object insert(StreetPassRecord streetPassRecord, Continuation<? super Unit> continuation) {
        Object insert = this.recordDao.insert(streetPassRecord, continuation);
        if (insert == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return insert;
        }
        return Unit.INSTANCE;
    }
}
