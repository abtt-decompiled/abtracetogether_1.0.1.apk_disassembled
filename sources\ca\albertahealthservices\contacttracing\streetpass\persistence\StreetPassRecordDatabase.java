package ca.albertahealthservices.contacttracing.streetpass.persistence;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ca.albertahealthservices.contacttracing.status.persistence.StatusRecordDao;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&¨\u0006\b"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDatabase;", "Landroidx/room/RoomDatabase;", "()V", "recordDao", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDao;", "statusDao", "Lca/albertahealthservices/contacttracing/status/persistence/StatusRecordDao;", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: StreetPassRecordDatabase.kt */
public abstract class StreetPassRecordDatabase extends RoomDatabase {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static volatile StreetPassRecordDatabase INSTANCE;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDatabase$Companion;", "", "()V", "INSTANCE", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecordDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: StreetPassRecordDatabase.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final StreetPassRecordDatabase getDatabase(Context context) {
            StreetPassRecordDatabase streetPassRecordDatabase;
            Intrinsics.checkParameterIsNotNull(context, "context");
            StreetPassRecordDatabase access$getINSTANCE$cp = StreetPassRecordDatabase.INSTANCE;
            if (access$getINSTANCE$cp != null) {
                return access$getINSTANCE$cp;
            }
            synchronized (this) {
                RoomDatabase build = Room.databaseBuilder(context, StreetPassRecordDatabase.class, "record_database").build();
                Intrinsics.checkExpressionValueIsNotNull(build, "Room.databaseBuilder(\n  …                 .build()");
                streetPassRecordDatabase = (StreetPassRecordDatabase) build;
                StreetPassRecordDatabase.INSTANCE = streetPassRecordDatabase;
            }
            return streetPassRecordDatabase;
        }
    }

    public abstract StreetPassRecordDao recordDao();

    public abstract StatusRecordDao statusDao();
}
