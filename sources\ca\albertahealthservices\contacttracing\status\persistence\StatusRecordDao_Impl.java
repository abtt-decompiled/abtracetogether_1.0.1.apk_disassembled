package ca.albertahealthservices.contacttracing.status.persistence;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

public final class StatusRecordDao_Impl implements StatusRecordDao {
    /* access modifiers changed from: private */
    public final RoomDatabase __db;
    /* access modifiers changed from: private */
    public final EntityInsertionAdapter<StatusRecord> __insertionAdapterOfStatusRecord;
    private final SharedSQLiteStatement __preparedStmtOfNukeDb;
    /* access modifiers changed from: private */
    public final SharedSQLiteStatement __preparedStmtOfPurgeOldRecords;

    public StatusRecordDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfStatusRecord = new EntityInsertionAdapter<StatusRecord>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR IGNORE INTO `status_table` (`id`,`timestamp`,`msg`) VALUES (nullif(?, 0),?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, StatusRecord statusRecord) {
                supportSQLiteStatement.bindLong(1, (long) statusRecord.getId());
                supportSQLiteStatement.bindLong(2, statusRecord.getTimestamp());
                if (statusRecord.getMsg() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, statusRecord.getMsg());
                }
            }
        };
        this.__preparedStmtOfNukeDb = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM status_table";
            }
        };
        this.__preparedStmtOfPurgeOldRecords = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM status_table WHERE timestamp < ?";
            }
        };
    }

    public Object insert(final StatusRecord statusRecord, Continuation<? super Unit> continuation) {
        return CoroutinesRoom.execute(this.__db, true, new Callable<Unit>() {
            public Unit call() throws Exception {
                StatusRecordDao_Impl.this.__db.beginTransaction();
                try {
                    StatusRecordDao_Impl.this.__insertionAdapterOfStatusRecord.insert(statusRecord);
                    StatusRecordDao_Impl.this.__db.setTransactionSuccessful();
                    return Unit.INSTANCE;
                } finally {
                    StatusRecordDao_Impl.this.__db.endTransaction();
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
                SupportSQLiteStatement acquire = StatusRecordDao_Impl.this.__preparedStmtOfPurgeOldRecords.acquire();
                acquire.bindLong(1, j);
                StatusRecordDao_Impl.this.__db.beginTransaction();
                try {
                    acquire.executeUpdateDelete();
                    StatusRecordDao_Impl.this.__db.setTransactionSuccessful();
                    return Unit.INSTANCE;
                } finally {
                    StatusRecordDao_Impl.this.__db.endTransaction();
                    StatusRecordDao_Impl.this.__preparedStmtOfPurgeOldRecords.release(acquire);
                }
            }
        }, continuation);
    }

    public LiveData<List<StatusRecord>> getRecords() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * from status_table ORDER BY timestamp ASC", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"status_table"}, false, new Callable<List<StatusRecord>>() {
            public List<StatusRecord> call() throws Exception {
                Cursor query = DBUtil.query(StatusRecordDao_Impl.this.__db, acquire, false, null);
                try {
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, WLConfig.APP_ID);
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "timestamp");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_MESSAGE);
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        StatusRecord statusRecord = new StatusRecord(query.getString(columnIndexOrThrow3));
                        statusRecord.setId(query.getInt(columnIndexOrThrow));
                        statusRecord.setTimestamp(query.getLong(columnIndexOrThrow2));
                        arrayList.add(statusRecord);
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

    public List<StatusRecord> getCurrentRecords() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * from status_table ORDER BY timestamp ASC", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, WLConfig.APP_ID);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "timestamp");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_MESSAGE);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                StatusRecord statusRecord = new StatusRecord(query.getString(columnIndexOrThrow3));
                statusRecord.setId(query.getInt(columnIndexOrThrow));
                statusRecord.setTimestamp(query.getLong(columnIndexOrThrow2));
                arrayList.add(statusRecord);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public LiveData<StatusRecord> getMostRecentRecord(String str) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * from status_table where msg = ? ORDER BY timestamp DESC LIMIT 1", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"status_table"}, false, new Callable<StatusRecord>() {
            public StatusRecord call() throws Exception {
                StatusRecord statusRecord = null;
                Cursor query = DBUtil.query(StatusRecordDao_Impl.this.__db, acquire, false, null);
                try {
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, WLConfig.APP_ID);
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "timestamp");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_MESSAGE);
                    if (query.moveToFirst()) {
                        StatusRecord statusRecord2 = new StatusRecord(query.getString(columnIndexOrThrow3));
                        statusRecord2.setId(query.getInt(columnIndexOrThrow));
                        statusRecord2.setTimestamp(query.getLong(columnIndexOrThrow2));
                        statusRecord = statusRecord2;
                    }
                    return statusRecord;
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

    public List<StatusRecord> getRecordsViaQuery(SupportSQLiteQuery supportSQLiteQuery) {
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, supportSQLiteQuery, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(__entityCursorConverter_caAlbertahealthservicesContacttracingStatusPersistenceStatusRecord(query));
            }
            return arrayList;
        } finally {
            query.close();
        }
    }

    private StatusRecord __entityCursorConverter_caAlbertahealthservicesContacttracingStatusPersistenceStatusRecord(Cursor cursor) {
        String str;
        int columnIndex = cursor.getColumnIndex(WLConfig.APP_ID);
        int columnIndex2 = cursor.getColumnIndex("timestamp");
        int columnIndex3 = cursor.getColumnIndex(NotificationCompat.CATEGORY_MESSAGE);
        if (columnIndex3 == -1) {
            str = null;
        } else {
            str = cursor.getString(columnIndex3);
        }
        StatusRecord statusRecord = new StatusRecord(str);
        if (columnIndex != -1) {
            statusRecord.setId(cursor.getInt(columnIndex));
        }
        if (columnIndex2 != -1) {
            statusRecord.setTimestamp(cursor.getLong(columnIndex2));
        }
        return statusRecord;
    }
}
