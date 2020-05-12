package androidx.room.guava;

import android.os.Build.VERSION;
import android.os.CancellationSignal;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class GuavaRoom {
    private static Executor sDirectExecutor = new Executor() {
        public void execute(Runnable runnable) {
            runnable.run();
        }
    };

    private GuavaRoom() {
    }

    @Deprecated
    public static <T> ListenableFuture<T> createListenableFuture(Callable<T> callable, RoomSQLiteQuery roomSQLiteQuery, boolean z) {
        return createListenableFuture(ArchTaskExecutor.getIOThreadExecutor(), callable, roomSQLiteQuery, z, (CancellationSignal) null);
    }

    @Deprecated
    public static <T> ListenableFuture<T> createListenableFuture(RoomDatabase roomDatabase, Callable<T> callable, RoomSQLiteQuery roomSQLiteQuery, boolean z) {
        return createListenableFuture(roomDatabase.getQueryExecutor(), callable, roomSQLiteQuery, z, (CancellationSignal) null);
    }

    public static <T> ListenableFuture<T> createListenableFuture(RoomDatabase roomDatabase, boolean z, Callable<T> callable, RoomSQLiteQuery roomSQLiteQuery, boolean z2) {
        return createListenableFuture(getExecutor(roomDatabase, z), callable, roomSQLiteQuery, z2, (CancellationSignal) null);
    }

    public static <T> ListenableFuture<T> createListenableFuture(RoomDatabase roomDatabase, boolean z, Callable<T> callable, RoomSQLiteQuery roomSQLiteQuery, boolean z2, CancellationSignal cancellationSignal) {
        return createListenableFuture(getExecutor(roomDatabase, z), callable, roomSQLiteQuery, z2, cancellationSignal);
    }

    private static <T> ListenableFuture<T> createListenableFuture(Executor executor, Callable<T> callable, final RoomSQLiteQuery roomSQLiteQuery, boolean z, final CancellationSignal cancellationSignal) {
        final ListenableFuture<T> createListenableFuture = createListenableFuture(executor, callable);
        if (cancellationSignal != null && VERSION.SDK_INT >= 16) {
            createListenableFuture.addListener(new Runnable() {
                public void run() {
                    if (createListenableFuture.isCancelled()) {
                        cancellationSignal.cancel();
                    }
                }
            }, sDirectExecutor);
        }
        if (z) {
            createListenableFuture.addListener(new Runnable() {
                public void run() {
                    roomSQLiteQuery.release();
                }
            }, sDirectExecutor);
        }
        return createListenableFuture;
    }

    @Deprecated
    public static <T> ListenableFuture<T> createListenableFuture(RoomDatabase roomDatabase, Callable<T> callable) {
        return createListenableFuture(roomDatabase, false, callable);
    }

    public static <T> ListenableFuture<T> createListenableFuture(RoomDatabase roomDatabase, boolean z, Callable<T> callable) {
        return createListenableFuture(getExecutor(roomDatabase, z), callable);
    }

    private static <T> ListenableFuture<T> createListenableFuture(Executor executor, final Callable<T> callable) {
        final ResolvableFuture create = ResolvableFuture.create();
        executor.execute(new Runnable() {
            public void run() {
                try {
                    create.set(callable.call());
                } catch (Throwable th) {
                    create.setException(th);
                }
            }
        });
        return create;
    }

    private static Executor getExecutor(RoomDatabase roomDatabase, boolean z) {
        if (z) {
            return roomDatabase.getTransactionExecutor();
        }
        return roomDatabase.getQueryExecutor();
    }
}
