package com.worklight.utils;

import androidx.core.view.MotionEventCompat;
import com.google.common.base.Ascii;
import java.io.UnsupportedEncodingException;

public class WLBase64 {
    private static final byte[] map = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};

    public static byte[] decode(byte[] bArr) {
        return decode(bArr, bArr.length);
    }

    public static byte[] decode(byte[] bArr, int i) {
        byte b;
        int i2;
        int i3 = (i / 4) * 3;
        if (i3 == 0) {
            return new byte[0];
        }
        byte[] bArr2 = new byte[i3];
        int i4 = i;
        int i5 = 0;
        while (true) {
            byte b2 = bArr[i4 - 1];
            b = 10;
            if (!(b2 == 10 || b2 == 13 || b2 == 32 || b2 == 9)) {
                if (b2 != 61) {
                    break;
                }
                i5++;
            }
            i4--;
        }
        int i6 = 0;
        byte b3 = 0;
        int i7 = 0;
        int i8 = 0;
        while (i6 < i4) {
            byte b4 = bArr[i6];
            if (!(b4 == b || b4 == 13 || b4 == 32 || b4 == 9)) {
                if (b4 >= 65 && b4 <= 90) {
                    i2 = b4 - 65;
                } else if (b4 >= 97 && b4 <= 122) {
                    i2 = b4 - 71;
                } else if (b4 >= 48 && b4 <= 57) {
                    i2 = b4 + 4;
                } else if (b4 == 43) {
                    i2 = 62;
                } else if (b4 != 47) {
                    return null;
                } else {
                    i2 = 63;
                }
                b3 = ((byte) i2) | (b3 << 6);
                if (i8 % 4 == 3) {
                    int i9 = i7 + 1;
                    bArr2[i7] = (byte) ((16711680 & b3) >> 16);
                    int i10 = i9 + 1;
                    bArr2[i9] = (byte) ((65280 & b3) >> 8);
                    int i11 = i10 + 1;
                    bArr2[i10] = (byte) (b3 & 255);
                    i7 = i11;
                }
                i8++;
            }
            i6++;
            b = 10;
        }
        if (i5 > 0) {
            int i12 = b3 << (i5 * 6);
            int i13 = i7 + 1;
            bArr2[i7] = (byte) ((i12 & 16711680) >> 16);
            if (i5 == 1) {
                i7 = i13 + 1;
                bArr2[i13] = (byte) ((i12 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
            } else {
                i7 = i13;
            }
        }
        byte[] bArr3 = new byte[i7];
        System.arraycopy(bArr2, 0, bArr3, 0, i7);
        return bArr3;
    }

    public static String encode(byte[] bArr, String str) throws UnsupportedEncodingException {
        int length = (bArr.length * 4) / 3;
        byte[] bArr2 = new byte[(length + (length / 76) + 3)];
        int length2 = bArr.length - (bArr.length % 3);
        int i = 0;
        for (int i2 = 0; i2 < length2; i2 += 3) {
            int i3 = i + 1;
            byte[] bArr3 = map;
            bArr2[i] = bArr3[(bArr[i2] & 255) >> 2];
            int i4 = i3 + 1;
            int i5 = i2 + 1;
            bArr2[i3] = bArr3[((bArr[i2] & 3) << 4) | ((bArr[i5] & 255) >> 4)];
            int i6 = i4 + 1;
            int i7 = i2 + 2;
            bArr2[i4] = bArr3[((bArr[i5] & Ascii.SI) << 2) | ((bArr[i7] & 255) >> 6)];
            i = i6 + 1;
            bArr2[i6] = bArr3[bArr[i7] & 63];
            int i8 = (i + 0) % 76;
        }
        int length3 = bArr.length % 3;
        if (length3 == 1) {
            int i9 = i + 1;
            byte[] bArr4 = map;
            bArr2[i] = bArr4[(bArr[length2] & 255) >> 2];
            int i10 = i9 + 1;
            bArr2[i9] = bArr4[(bArr[length2] & 3) << 4];
            int i11 = i10 + 1;
            bArr2[i10] = 61;
            i = i11 + 1;
            bArr2[i11] = 61;
        } else if (length3 == 2) {
            int i12 = i + 1;
            byte[] bArr5 = map;
            bArr2[i] = bArr5[(bArr[length2] & 255) >> 2];
            int i13 = i12 + 1;
            int i14 = (bArr[length2] & 3) << 4;
            int i15 = length2 + 1;
            bArr2[i12] = bArr5[((bArr[i15] & 255) >> 4) | i14];
            int i16 = i13 + 1;
            bArr2[i13] = bArr5[(bArr[i15] & Ascii.SI) << 2];
            i = i16 + 1;
            bArr2[i16] = 61;
        }
        return new String(bArr2, 0, i, str);
    }

    public static String encodeUrlSafe(byte[] bArr, String str) throws UnsupportedEncodingException {
        return encode(bArr, str).replace('+', '-').replace('/', '_');
    }
}
