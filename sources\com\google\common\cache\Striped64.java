package com.google.common.cache;

import java.util.Random;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import sun.misc.Unsafe;

abstract class Striped64 extends Number {
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;
    static final Random rng = new Random();
    static final ThreadLocal<int[]> threadHashCode = new ThreadLocal<>();
    volatile transient long base;
    volatile transient int busy;
    @NullableDecl
    volatile transient Cell[] cells;

    static final class Cell {
        private static final Unsafe UNSAFE;
        private static final long valueOffset;
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;
        volatile long value;

        Cell(long j) {
            this.value = j;
        }

        /* access modifiers changed from: 0000 */
        public final boolean cas(long j, long j2) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, j, j2);
        }

        static {
            try {
                Unsafe access$000 = Striped64.getUnsafe();
                UNSAFE = access$000;
                valueOffset = access$000.objectFieldOffset(Cell.class.getDeclaredField("value"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract long fn(long j, long j2);

    static {
        try {
            Unsafe unsafe = getUnsafe();
            UNSAFE = unsafe;
            Class<Striped64> cls = Striped64.class;
            baseOffset = unsafe.objectFieldOffset(cls.getDeclaredField("base"));
            busyOffset = UNSAFE.objectFieldOffset(cls.getDeclaredField("busy"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    Striped64() {
    }

    /* access modifiers changed from: 0000 */
    public final boolean casBase(long j, long j2) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, j, j2);
    }

    /* access modifiers changed from: 0000 */
    public final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x00ee A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0023 A[SYNTHETIC] */
    public final void retryUpdate(long j, int[] iArr, boolean z) {
        int[] iArr2;
        int i;
        boolean z2;
        boolean z3;
        long j2 = j;
        if (iArr == null) {
            iArr2 = new int[1];
            threadHashCode.set(iArr2);
            i = rng.nextInt();
            if (i == 0) {
                i = 1;
            }
            iArr2[0] = i;
        } else {
            i = iArr[0];
            iArr2 = iArr;
        }
        boolean z4 = false;
        int i2 = i;
        boolean z5 = z;
        while (true) {
            Cell[] cellArr = this.cells;
            if (cellArr != null) {
                int length = cellArr.length;
                if (length > 0) {
                    Cell cell = cellArr[(length - 1) & i2];
                    if (cell != null) {
                        if (!z5) {
                            z5 = true;
                        } else {
                            long j3 = cell.value;
                            if (!cell.cas(j3, fn(j3, j2))) {
                                if (length < NCPU && this.cells == cellArr) {
                                    if (!z4) {
                                        z4 = true;
                                    } else if (this.busy == 0 && casBusy()) {
                                        try {
                                            if (this.cells == cellArr) {
                                                Cell[] cellArr2 = new Cell[(length << 1)];
                                                for (int i3 = 0; i3 < length; i3++) {
                                                    cellArr2[i3] = cellArr[i3];
                                                }
                                                this.cells = cellArr2;
                                            }
                                            this.busy = 0;
                                            z4 = false;
                                        } catch (Throwable th) {
                                            this.busy = 0;
                                            throw th;
                                        }
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                        int i4 = i2 ^ (i2 << 13);
                        int i5 = i4 ^ (i4 >>> 17);
                        i2 = i5 ^ (i5 << 5);
                        iArr2[0] = i2;
                    } else if (this.busy == 0) {
                        Cell cell2 = new Cell(j2);
                        if (this.busy == 0 && casBusy()) {
                            try {
                                Cell[] cellArr3 = this.cells;
                                if (cellArr3 != null) {
                                    int length2 = cellArr3.length;
                                    if (length2 > 0) {
                                        int i6 = (length2 - 1) & i2;
                                        if (cellArr3[i6] == null) {
                                            cellArr3[i6] = cell2;
                                            z3 = true;
                                            if (!z3) {
                                                return;
                                            }
                                        }
                                    }
                                }
                                z3 = false;
                                if (!z3) {
                                }
                            } finally {
                                this.busy = 0;
                            }
                        }
                    }
                    z4 = false;
                    int i42 = i2 ^ (i2 << 13);
                    int i52 = i42 ^ (i42 >>> 17);
                    i2 = i52 ^ (i52 << 5);
                    iArr2[0] = i2;
                }
            }
            if (this.busy == 0 && this.cells == cellArr && casBusy()) {
                try {
                    if (this.cells == cellArr) {
                        Cell[] cellArr4 = new Cell[2];
                        cellArr4[i2 & 1] = new Cell(j2);
                        this.cells = cellArr4;
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (z2) {
                        return;
                    }
                } finally {
                    this.busy = 0;
                }
            } else {
                long j4 = this.base;
                if (casBase(j4, fn(j4, j2))) {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void internalReset(long j) {
        Cell[] cellArr = this.cells;
        this.base = j;
        if (cellArr != null) {
            for (Cell cell : cellArr) {
                if (cell != null) {
                    cell.value = j;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0010, code lost:
        return (sun.misc.Unsafe) java.security.AccessController.doPrivileged(new com.google.common.cache.Striped64.AnonymousClass1());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0011, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001d, code lost:
        throw new java.lang.RuntimeException("Could not initialize intrinsics", r0.getCause());
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0005 */
    public static Unsafe getUnsafe() {
        return Unsafe.getUnsafe();
    }
}
