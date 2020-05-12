package ca.albertahealthservices.contacttracing.streetpass.persistence;

import android.database.Cursor;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.worklight.common.WLConfig;
import com.worklight.wlclient.api.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

public final class StreetPassRecordDao_Impl implements StreetPassRecordDao {
    /* access modifiers changed from: private */
    public final RoomDatabase __db;
    /* access modifiers changed from: private */
    public final EntityInsertionAdapter<StreetPassRecord> __insertionAdapterOfStreetPassRecord;
    private final SharedSQLiteStatement __preparedStmtOfNukeDb;
    /* access modifiers changed from: private */
    public final SharedSQLiteStatement __preparedStmtOfPurgeOldRecords;

    public StreetPassRecordDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfStreetPassRecord = new EntityInsertionAdapter<StreetPassRecord>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR IGNORE INTO `record_table` (`id`,`timestamp`,`v`,`msg`,`org`,`modelP`,`modelC`,`rssi`,`txPower`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, StreetPassRecord streetPassRecord) {
                supportSQLiteStatement.bindLong(1, (long) streetPassRecord.getId());
                supportSQLiteStatement.bindLong(2, streetPassRecord.getTimestamp());
                supportSQLiteStatement.bindLong(3, (long) streetPassRecord.getV());
                if (streetPassRecord.getMsg() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, streetPassRecord.getMsg());
                }
                if (streetPassRecord.getOrg() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, streetPassRecord.getOrg());
                }
                if (streetPassRecord.getModelP() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, streetPassRecord.getModelP());
                }
                if (streetPassRecord.getModelC() == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, streetPassRecord.getModelC());
                }
                supportSQLiteStatement.bindLong(8, (long) streetPassRecord.getRssi());
                if (streetPassRecord.getTxPower() == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindLong(9, (long) streetPassRecord.getTxPower().intValue());
                }
            }
        };
        this.__preparedStmtOfNukeDb = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM record_table";
            }
        };
        this.__preparedStmtOfPurgeOldRecords = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM record_table WHERE timestamp < ?";
            }
        };
    }

    public Object insert(final StreetPassRecord streetPassRecord, Continuation<? super Unit> continuation) {
        return CoroutinesRoom.execute(this.__db, true, new Callable<Unit>() {
            public Unit call() throws Exception {
                StreetPassRecordDao_Impl.this.__db.beginTransaction();
                try {
                    StreetPassRecordDao_Impl.this.__insertionAdapterOfStreetPassRecord.insert(streetPassRecord);
                    StreetPassRecordDao_Impl.this.__db.setTransactionSuccessful();
                    return Unit.INSTANCE;
                } finally {
                    StreetPassRecordDao_Impl.this.__db.endTransaction();
                }
            }
        }, continuation);
    }

    public void nukeDb() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfNukeDb.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfNukeDb.release(acquire);
        }
    }

    public Object purgeOldRecords(final long j, Continuation<? super Unit> continuation) {
        return CoroutinesRoom.execute(this.__db, true, new Callable<Unit>() {
            public Unit call() throws Exception {
                SupportSQLiteStatement acquire = StreetPassRecordDao_Impl.this.__preparedStmtOfPurgeOldRecords.acquire();
                acquire.bindLong(1, j);
                StreetPassRecordDao_Impl.this.__db.beginTransaction();
                try {
                    acquire.executeUpdateDelete();
                    StreetPassRecordDao_Impl.this.__db.setTransactionSuccessful();
                    return Unit.INSTANCE;
                } finally {
                    StreetPassRecordDao_Impl.this.__db.endTransaction();
                    StreetPassRecordDao_Impl.this.__preparedStmtOfPurgeOldRecords.release(acquire);
                }
            }
        }, continuation);
    }

    public LiveData<List<StreetPassRecord>> getRecords() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * from record_table ORDER BY timestamp ASC", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"record_table"}, false, new Callable<List<StreetPassRecord>>() {
            public List<StreetPassRecord> call() throws Exception {
                Integer num;
                Cursor query = DBUtil.query(StreetPassRecordDao_Impl.this.__db, acquire, false, null);
                try {
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, WLConfig.APP_ID);
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "timestamp");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, SecurityUtils.VERSION_LABEL);
                    int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_MESSAGE);
                    int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "org");
                    int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "modelP");
                    int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "modelC");
                    int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "rssi");
                    int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "txPower");
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        int i = query.getInt(columnIndexOrThrow3);
                        String string = query.getString(columnIndexOrThrow4);
                        String string2 = query.getString(columnIndexOrThrow5);
                        String string3 = query.getString(columnIndexOrThrow6);
                        String string4 = query.getString(columnIndexOrThrow7);
                        int i2 = query.getInt(columnIndexOrThrow8);
                        if (query.isNull(columnIndexOrThrow9)) {
                            num = null;
                        } else {
                            num = Integer.valueOf(query.getInt(columnIndexOrThrow9));
                        }
                        StreetPassRecord streetPassRecord = new StreetPassRecord(i, string, string2, string3, string4, i2, num);
                        streetPassRecord.setId(query.getInt(columnIndexOrThrow));
                        streetPassRecord.setTimestamp(query.getLong(columnIndexOrThrow2));
                        arrayList.add(streetPassRecord);
                    }
                    return arrayList;
                } finally {
                    query.close();
                }
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        });
    }

    public LiveData<StreetPassRecord> getMostRecentRecord() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * from record_table ORDER BY timestamp DESC LIMIT 1", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"record_table"}, false, new Callable<StreetPassRecord>() {
            /* JADX WARNING: type inference failed for: r3v0 */
            /* JADX WARNING: type inference failed for: r3v1, types: [ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord] */
            /* JADX WARNING: type inference failed for: r3v3, types: [java.lang.Integer] */
            /* JADX WARNING: type inference failed for: r3v4 */
            /* JADX WARNING: type inference failed for: r20v0, types: [java.lang.Integer] */
            /* JADX WARNING: type inference failed for: r13v0, types: [ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord] */
            /* JADX WARNING: type inference failed for: r3v6 */
            /* JADX WARNING: type inference failed for: r13v1, types: [ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord] */
            /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v0
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord, java.lang.Integer]
  uses: [ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord, ?[OBJECT, ARRAY]]
  mth insns count: 52
            	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:30)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
             */
            /* JADX WARNING: Unknown variable types count: 4 */
            public StreetPassRecord call() throws Exception {
                ? r3 = 0;
                Cursor query = DBUtil.query(StreetPassRecordDao_Impl.this.__db, acquire, false, null);
                try {
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, WLConfig.APP_ID);
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "timestamp");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, SecurityUtils.VERSION_LABEL);
                    int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_MESSAGE);
                    int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "org");
                    int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "modelP");
                    int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "modelC");
                    int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "rssi");
                    int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "txPower");
                    if (query.moveToFirst()) {
                        int i = query.getInt(columnIndexOrThrow3);
                        String string = query.getString(columnIndexOrThrow4);
                        String string2 = query.getString(columnIndexOrThrow5);
                        String string3 = query.getString(columnIndexOrThrow6);
                        String string4 = query.getString(columnIndexOrThrow7);
                        int i2 = query.getInt(columnIndexOrThrow8);
                        if (!query.isNull(columnIndexOrThrow9)) {
                            r3 = Integer.valueOf(query.getInt(columnIndexOrThrow9));
                        }
                        ? streetPassRecord = new StreetPassRecord(i, string, string2, string3, string4, i2, r3);
                        streetPassRecord.setId(query.getInt(columnIndexOrThrow));
                        streetPassRecord.setTimestamp(query.getLong(columnIndexOrThrow2));
                        r3 = streetPassRecord;
                    }
                    return r3;
                } finally {
                    query.close();
                }
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        });
    }

    public List<StreetPassRecord> getCurrentRecords() {
        Integer num;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * from record_table ORDER BY timestamp ASC", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, WLConfig.APP_ID);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "timestamp");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, SecurityUtils.VERSION_LABEL);
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_MESSAGE);
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "org");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "modelP");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "modelC");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "rssi");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "txPower");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i = query.getInt(columnIndexOrThrow3);
                String string = query.getString(columnIndexOrThrow4);
                String string2 = query.getString(columnIndexOrThrow5);
                String string3 = query.getString(columnIndexOrThrow6);
                String string4 = query.getString(columnIndexOrThrow7);
                int i2 = query.getInt(columnIndexOrThrow8);
                if (query.isNull(columnIndexOrThrow9)) {
                    num = null;
                } else {
                    num = Integer.valueOf(query.getInt(columnIndexOrThrow9));
                }
                StreetPassRecord streetPassRecord = new StreetPassRecord(i, string, string2, string3, string4, i2, num);
                streetPassRecord.setId(query.getInt(columnIndexOrThrow));
                int i3 = columnIndexOrThrow;
                streetPassRecord.setTimestamp(query.getLong(columnIndexOrThrow2));
                arrayList.add(streetPassRecord);
                columnIndexOrThrow = i3;
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<StreetPassRecord> getRecordsViaQuery(SupportSQLiteQuery supportSQLiteQuery) {
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, supportSQLiteQuery, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(__entityCursorConverter_caAlbertahealthservicesContacttracingStreetpassPersistenceStreetPassRecord(query));
            }
            return arrayList;
        } finally {
            query.close();
        }
    }

    private StreetPassRecord __entityCursorConverter_caAlbertahealthservicesContacttracingStreetpassPersistenceStreetPassRecord(Cursor cursor) {
        int i;
        String str;
        String str2;
        String str3;
        String str4;
        Cursor cursor2 = cursor;
        int columnIndex = cursor2.getColumnIndex(WLConfig.APP_ID);
        int columnIndex2 = cursor2.getColumnIndex("timestamp");
        int columnIndex3 = cursor2.getColumnIndex(SecurityUtils.VERSION_LABEL);
        int columnIndex4 = cursor2.getColumnIndex(NotificationCompat.CATEGORY_MESSAGE);
        int columnIndex5 = cursor2.getColumnIndex("org");
        int columnIndex6 = cursor2.getColumnIndex("modelP");
        int columnIndex7 = cursor2.getColumnIndex("modelC");
        int columnIndex8 = cursor2.getColumnIndex("rssi");
        int columnIndex9 = cursor2.getColumnIndex("txPower");
        int i2 = 0;
        if (columnIndex3 == -1) {
            i = 0;
        } else {
            i = cursor2.getInt(columnIndex3);
        }
        Integer num = null;
        if (columnIndex4 == -1) {
            str = null;
        } else {
            str = cursor2.getString(columnIndex4);
        }
        if (columnIndex5 == -1) {
            str2 = null;
        } else {
            str2 = cursor2.getString(columnIndex5);
        }
        if (columnIndex6 == -1) {
            str3 = null;
        } else {
            str3 = cursor2.getString(columnIndex6);
        }
        if (columnIndex7 == -1) {
            str4 = null;
        } else {
            str4 = cursor2.getString(columnIndex7);
        }
        if (columnIndex8 != -1) {
            i2 = cursor2.getInt(columnIndex8);
        }
        int i3 = i2;
        if (columnIndex9 != -1 && !cursor2.isNull(columnIndex9)) {
            num = Integer.valueOf(cursor2.getInt(columnIndex9));
        }
        StreetPassRecord streetPassRecord = new StreetPassRecord(i, str, str2, str3, str4, i3, num);
        if (columnIndex != -1) {
            streetPassRecord.setId(cursor2.getInt(columnIndex));
        }
        if (columnIndex2 != -1) {
            streetPassRecord.setTimestamp(cursor2.getLong(columnIndex2));
        }
        return streetPassRecord;
    }
}
