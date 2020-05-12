package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

@Immutable
final class Murmur3_128HashFunction extends AbstractHashFunction implements Serializable {
    static final HashFunction GOOD_FAST_HASH_128 = new Murmur3_128HashFunction(Hashing.GOOD_FAST_HASH_SEED);
    static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
    private static final long serialVersionUID = 0;
    private final int seed;

    private static final class Murmur3_128Hasher extends AbstractStreamingHasher {
        private static final long C1 = -8663945395140668459L;
        private static final long C2 = 5545529020109919103L;
        private static final int CHUNK_SIZE = 16;
        private long h1;
        private long h2;
        private int length = 0;

        private static long fmix64(long j) {
            long j2 = (j ^ (j >>> 33)) * -49064778989728563L;
            long j3 = (j2 ^ (j2 >>> 33)) * -4265267296055464877L;
            return j3 ^ (j3 >>> 33);
        }

        Murmur3_128Hasher(int i) {
            super(16);
            long j = (long) i;
            this.h1 = j;
            this.h2 = j;
        }

        /* access modifiers changed from: protected */
        public void process(ByteBuffer byteBuffer) {
            bmix64(byteBuffer.getLong(), byteBuffer.getLong());
            this.length += 16;
        }

        private void bmix64(long j, long j2) {
            long mixK1 = mixK1(j) ^ this.h1;
            this.h1 = mixK1;
            long rotateLeft = Long.rotateLeft(mixK1, 27);
            this.h1 = rotateLeft;
            long j3 = this.h2;
            long j4 = rotateLeft + j3;
            this.h1 = j4;
            this.h1 = (j4 * 5) + 1390208809;
            long mixK2 = mixK2(j2) ^ j3;
            this.h2 = mixK2;
            long rotateLeft2 = Long.rotateLeft(mixK2, 31);
            this.h2 = rotateLeft2;
            long j5 = rotateLeft2 + this.h1;
            this.h2 = j5;
            this.h2 = (j5 * 5) + 944331445;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0055, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(11))) << 24);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0064, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(10))) << 16);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0073, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(9))) << 8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0082, code lost:
            r0 = r0 ^ ((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(8)));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x008e, code lost:
            r2 = r12.getLong() ^ 0;
            r6 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x00a4, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(5))) << 40);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x00b2, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(4))) << 32);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x00c1, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(3))) << 24);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x00cf, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(2))) << 16);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x00dd, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(1))) << 8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x00eb, code lost:
            r2 = ((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(0))) ^ r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x00f6, code lost:
            r11.h1 ^= mixK1(r2);
            r11.h2 ^= mixK2(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0108, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0036, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(13))) << 40);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0045, code lost:
            r0 = r0 ^ (((long) com.google.common.primitives.UnsignedBytes.toInt(r12.get(12))) << 32);
         */
        public void processRemaining(ByteBuffer byteBuffer) {
            long j;
            long j2;
            this.length += byteBuffer.remaining();
            long j3 = 0;
            switch (byteBuffer.remaining()) {
                case 1:
                    long j4 = 0;
                    break;
                case 2:
                    long j5 = 0;
                    break;
                case 3:
                    long j6 = 0;
                    break;
                case 4:
                    long j7 = 0;
                    break;
                case 5:
                    long j8 = 0;
                    break;
                case 6:
                    j = 0;
                    break;
                case 7:
                    j = (((long) UnsignedBytes.toInt(byteBuffer.get(6))) << 48) ^ 0;
                    break;
                case 8:
                    long j9 = 0;
                    break;
                case 9:
                    long j10 = 0;
                    break;
                case 10:
                    long j11 = 0;
                    break;
                case 11:
                    long j12 = 0;
                    break;
                case 12:
                    long j13 = 0;
                    break;
                case 13:
                    long j14 = 0;
                    break;
                case 14:
                    j2 = 0;
                    break;
                case 15:
                    j2 = (((long) UnsignedBytes.toInt(byteBuffer.get(14))) << 48) ^ 0;
                    break;
                default:
                    throw new AssertionError("Should never get here.");
            }
        }

        public HashCode makeHash() {
            long j = this.h1;
            int i = this.length;
            long j2 = j ^ ((long) i);
            this.h1 = j2;
            long j3 = this.h2 ^ ((long) i);
            this.h2 = j3;
            long j4 = j2 + j3;
            this.h1 = j4;
            this.h2 = j3 + j4;
            this.h1 = fmix64(j4);
            long fmix64 = fmix64(this.h2);
            this.h2 = fmix64;
            long j5 = this.h1 + fmix64;
            this.h1 = j5;
            this.h2 = fmix64 + j5;
            return HashCode.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
        }

        private static long mixK1(long j) {
            return Long.rotateLeft(j * C1, 31) * C2;
        }

        private static long mixK2(long j) {
            return Long.rotateLeft(j * C2, 33) * C1;
        }
    }

    public int bits() {
        return 128;
    }

    Murmur3_128HashFunction(int i) {
        this.seed = i;
    }

    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hashing.murmur3_128(");
        sb.append(this.seed);
        sb.append(")");
        return sb.toString();
    }

    public boolean equals(@NullableDecl Object obj) {
        if (!(obj instanceof Murmur3_128HashFunction)) {
            return false;
        }
        if (this.seed == ((Murmur3_128HashFunction) obj).seed) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }
}
