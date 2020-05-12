package androidx.room;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CompletableJob;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.ThreadContextElementKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u001d\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\bH@ø\u0001\u0000¢\u0006\u0002\u0010\t\u001a9\u0010\n\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b*\u00020\b2\u001c\u0010\f\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\rH@ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011"}, d2 = {"acquireTransactionThread", "Lkotlin/coroutines/ContinuationInterceptor;", "Ljava/util/concurrent/Executor;", "controlJob", "Lkotlinx/coroutines/Job;", "(Ljava/util/concurrent/Executor;Lkotlinx/coroutines/Job;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createTransactionContext", "Lkotlin/coroutines/CoroutineContext;", "Landroidx/room/RoomDatabase;", "(Landroidx/room/RoomDatabase;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "withTransaction", "R", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "(Landroidx/room/RoomDatabase;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "room-ktx_release"}, k = 2, mv = {1, 1, 15})
/* compiled from: RoomDatabase.kt */
public final class RoomDatabaseKt {
    /* JADX WARNING: Removed duplicated region for block: B:14:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0090 A[PHI: r7 
  PHI: (r7v2 java.lang.Object) = (r7v4 java.lang.Object), (r7v1 java.lang.Object) binds: [B:24:0x008d, B:10:0x0029] A[DONT_GENERATE, DONT_INLINE], RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    public static final <R> Object withTransaction(RoomDatabase roomDatabase, Function1<? super Continuation<? super R>, ? extends Object> function1, Continuation<? super R> continuation) {
        RoomDatabaseKt$withTransaction$1 roomDatabaseKt$withTransaction$1;
        Object obj;
        Object coroutine_suspended;
        int i;
        CoroutineContext coroutineContext;
        if (continuation instanceof RoomDatabaseKt$withTransaction$1) {
            roomDatabaseKt$withTransaction$1 = (RoomDatabaseKt$withTransaction$1) continuation;
            if ((roomDatabaseKt$withTransaction$1.label & Integer.MIN_VALUE) != 0) {
                roomDatabaseKt$withTransaction$1.label -= Integer.MIN_VALUE;
                obj = roomDatabaseKt$withTransaction$1.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = roomDatabaseKt$withTransaction$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    TransactionElement transactionElement = (TransactionElement) roomDatabaseKt$withTransaction$1.getContext().get(TransactionElement.Key);
                    if (transactionElement != null) {
                        ContinuationInterceptor transactionDispatcher$room_ktx_release = transactionElement.getTransactionDispatcher$room_ktx_release();
                        if (transactionDispatcher$room_ktx_release != null) {
                            coroutineContext = transactionDispatcher$room_ktx_release;
                            Function2 roomDatabaseKt$withTransaction$2 = new RoomDatabaseKt$withTransaction$2(roomDatabase, function1, null);
                            roomDatabaseKt$withTransaction$1.L$0 = roomDatabase;
                            roomDatabaseKt$withTransaction$1.L$1 = function1;
                            roomDatabaseKt$withTransaction$1.L$2 = coroutineContext;
                            roomDatabaseKt$withTransaction$1.label = 2;
                            obj = BuildersKt.withContext(coroutineContext, roomDatabaseKt$withTransaction$2, roomDatabaseKt$withTransaction$1);
                            return obj == coroutine_suspended ? coroutine_suspended : obj;
                        }
                    }
                    roomDatabaseKt$withTransaction$1.L$0 = roomDatabase;
                    roomDatabaseKt$withTransaction$1.L$1 = function1;
                    roomDatabaseKt$withTransaction$1.label = 1;
                    obj = createTransactionContext(roomDatabase, roomDatabaseKt$withTransaction$1);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else if (i == 1) {
                    function1 = (Function1) roomDatabaseKt$withTransaction$1.L$1;
                    roomDatabase = (RoomDatabase) roomDatabaseKt$withTransaction$1.L$0;
                    ResultKt.throwOnFailure(obj);
                } else if (i == 2) {
                    CoroutineContext coroutineContext2 = (CoroutineContext) roomDatabaseKt$withTransaction$1.L$2;
                    Function1 function12 = (Function1) roomDatabaseKt$withTransaction$1.L$1;
                    RoomDatabase roomDatabase2 = (RoomDatabase) roomDatabaseKt$withTransaction$1.L$0;
                    ResultKt.throwOnFailure(obj);
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                coroutineContext = (CoroutineContext) obj;
                Function2 roomDatabaseKt$withTransaction$22 = new RoomDatabaseKt$withTransaction$2(roomDatabase, function1, null);
                roomDatabaseKt$withTransaction$1.L$0 = roomDatabase;
                roomDatabaseKt$withTransaction$1.L$1 = function1;
                roomDatabaseKt$withTransaction$1.L$2 = coroutineContext;
                roomDatabaseKt$withTransaction$1.label = 2;
                obj = BuildersKt.withContext(coroutineContext, roomDatabaseKt$withTransaction$22, roomDatabaseKt$withTransaction$1);
                if (obj == coroutine_suspended) {
                }
            }
        }
        roomDatabaseKt$withTransaction$1 = new RoomDatabaseKt$withTransaction$1(continuation);
        obj = roomDatabaseKt$withTransaction$1.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = roomDatabaseKt$withTransaction$1.label;
        if (i != 0) {
        }
        coroutineContext = (CoroutineContext) obj;
        Function2 roomDatabaseKt$withTransaction$222 = new RoomDatabaseKt$withTransaction$2(roomDatabase, function1, null);
        roomDatabaseKt$withTransaction$1.L$0 = roomDatabase;
        roomDatabaseKt$withTransaction$1.L$1 = function1;
        roomDatabaseKt$withTransaction$1.L$2 = coroutineContext;
        roomDatabaseKt$withTransaction$1.label = 2;
        obj = BuildersKt.withContext(coroutineContext, roomDatabaseKt$withTransaction$222, roomDatabaseKt$withTransaction$1);
        if (obj == coroutine_suspended) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    static final /* synthetic */ Object createTransactionContext(RoomDatabase roomDatabase, Continuation<? super CoroutineContext> continuation) {
        RoomDatabaseKt$createTransactionContext$1 roomDatabaseKt$createTransactionContext$1;
        int i;
        CompletableJob completableJob;
        RoomDatabase roomDatabase2;
        if (continuation instanceof RoomDatabaseKt$createTransactionContext$1) {
            roomDatabaseKt$createTransactionContext$1 = (RoomDatabaseKt$createTransactionContext$1) continuation;
            if ((roomDatabaseKt$createTransactionContext$1.label & Integer.MIN_VALUE) != 0) {
                roomDatabaseKt$createTransactionContext$1.label -= Integer.MIN_VALUE;
                Object obj = roomDatabaseKt$createTransactionContext$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = roomDatabaseKt$createTransactionContext$1.label;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    CompletableJob Job$default = JobKt.Job$default((Job) null, 1, (Object) null);
                    Executor transactionExecutor = roomDatabase.getTransactionExecutor();
                    Intrinsics.checkExpressionValueIsNotNull(transactionExecutor, "transactionExecutor");
                    Job job = Job$default;
                    roomDatabaseKt$createTransactionContext$1.L$0 = roomDatabase;
                    roomDatabaseKt$createTransactionContext$1.L$1 = Job$default;
                    roomDatabaseKt$createTransactionContext$1.label = 1;
                    Object acquireTransactionThread = acquireTransactionThread(transactionExecutor, job, roomDatabaseKt$createTransactionContext$1);
                    if (acquireTransactionThread == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Object obj2 = acquireTransactionThread;
                    roomDatabase2 = roomDatabase;
                    completableJob = Job$default;
                    obj = obj2;
                } else if (i == 1) {
                    completableJob = (CompletableJob) roomDatabaseKt$createTransactionContext$1.L$1;
                    roomDatabase2 = (RoomDatabase) roomDatabaseKt$createTransactionContext$1.L$0;
                    ResultKt.throwOnFailure(obj);
                } else {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ContinuationInterceptor continuationInterceptor = (ContinuationInterceptor) obj;
                TransactionElement transactionElement = new TransactionElement(completableJob, continuationInterceptor);
                ThreadLocal suspendingTransactionId = roomDatabase2.getSuspendingTransactionId();
                Intrinsics.checkExpressionValueIsNotNull(suspendingTransactionId, "suspendingTransactionId");
                return continuationInterceptor.plus(transactionElement).plus(ThreadContextElementKt.asContextElement(suspendingTransactionId, Boxing.boxInt(System.identityHashCode(completableJob))));
            }
        }
        roomDatabaseKt$createTransactionContext$1 = new RoomDatabaseKt$createTransactionContext$1(continuation);
        Object obj3 = roomDatabaseKt$createTransactionContext$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = roomDatabaseKt$createTransactionContext$1.label;
        if (i != 0) {
        }
        ContinuationInterceptor continuationInterceptor2 = (ContinuationInterceptor) obj3;
        TransactionElement transactionElement2 = new TransactionElement(completableJob, continuationInterceptor2);
        ThreadLocal suspendingTransactionId2 = roomDatabase2.getSuspendingTransactionId();
        Intrinsics.checkExpressionValueIsNotNull(suspendingTransactionId2, "suspendingTransactionId");
        return continuationInterceptor2.plus(transactionElement2).plus(ThreadContextElementKt.asContextElement(suspendingTransactionId2, Boxing.boxInt(System.identityHashCode(completableJob))));
    }

    static final /* synthetic */ Object acquireTransactionThread(Executor executor, Job job, Continuation<? super ContinuationInterceptor> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        cancellableContinuation.invokeOnCancellation(new RoomDatabaseKt$acquireTransactionThread$$inlined$suspendCancellableCoroutine$lambda$1(executor, job));
        try {
            executor.execute(new RoomDatabaseKt$acquireTransactionThread$$inlined$suspendCancellableCoroutine$lambda$2(cancellableContinuation, executor, job));
        } catch (RejectedExecutionException e) {
            cancellableContinuation.cancel(new IllegalStateException("Unable to acquire a thread to perform the database transaction.", e));
        }
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }
}
