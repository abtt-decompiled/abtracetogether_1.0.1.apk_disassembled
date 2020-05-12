package com.worklight.utils;

import com.worklight.common.Logger;

public class Version implements Comparable<Version> {
    public static final Version EMPTY_VERSION = new Version("");
    private String version;
    private Object[] versionParts;

    private Version(Object[] objArr) {
        this.versionParts = objArr;
        this.version = "";
        for (int i = 0; i < this.versionParts.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.version);
            sb.append(this.versionParts[i]);
            this.version = sb.toString();
            if (i < this.versionParts.length - 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.version);
                sb2.append(".");
                this.version = sb2.toString();
            }
        }
    }

    public Version(String str) {
        String str2;
        if (str == null || str.length() == 0) {
            this.version = "";
            this.versionParts = new Object[0];
            return;
        }
        int indexOf = str.indexOf(45);
        if (indexOf != -1) {
            char[] charArray = str.toCharArray();
            charArray[indexOf] = '.';
            str2 = new String(charArray);
        } else {
            str2 = str;
        }
        this.version = str;
        String[] split = str2.split("\\.");
        this.versionParts = new Object[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                this.versionParts[i] = Long.valueOf(split[i]);
            } catch (NumberFormatException unused) {
                this.versionParts[i] = split[i];
            }
        }
    }

    public boolean isBiggerThan(Version version2) {
        String str = "isBiggerThan";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return compareTo(version2) == 1;
    }

    public boolean isSmallerThan(Version version2) {
        String str = "isSmallerThan";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return compareTo(version2) == -1;
    }

    public int compareTo(Version version2) {
        Version version3;
        String str = "compareTo";
        Logger.enter(getClass().getSimpleName(), str);
        if (version2 == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return 1;
        }
        Object[] objArr = this.versionParts;
        int length = objArr.length;
        Object[] objArr2 = version2.versionParts;
        if (length < objArr2.length) {
            version3 = addZeros(this, objArr2.length - objArr.length);
        } else {
            if (objArr2.length < objArr.length) {
                version2 = addZeros(version2, objArr.length - objArr2.length);
            }
            version3 = this;
        }
        int i = 0;
        while (true) {
            Object[] objArr3 = version3.versionParts;
            if (i < objArr3.length) {
                if (objArr3[i] instanceof Long) {
                    Object[] objArr4 = version2.versionParts;
                    if (objArr4[i] instanceof Long) {
                        int compareTo = ((Long) objArr3[i]).compareTo((Long) objArr4[i]);
                        if (compareTo != 0) {
                            Logger.exit(getClass().getSimpleName(), str);
                            return compareTo;
                        }
                    } else {
                        Logger.exit(getClass().getSimpleName(), str);
                        return -1;
                    }
                } else {
                    Object[] objArr5 = version2.versionParts;
                    if (objArr5[i] instanceof Long) {
                        Logger.exit(getClass().getSimpleName(), str);
                        return 1;
                    }
                    int compareTo2 = ((String) objArr3[i]).compareTo((String) objArr5[i]);
                    if (compareTo2 != 0) {
                        Logger.exit(getClass().getSimpleName(), str);
                        return compareTo2;
                    }
                }
                i++;
            } else if (version2.versionParts.length > objArr3.length) {
                Logger.exit(getClass().getSimpleName(), str);
                return -1;
            } else {
                Logger.exit(getClass().getSimpleName(), str);
                return 0;
            }
        }
    }

    private Version addZeros(Version version2, int i) {
        String str = "addZeros";
        Logger.enter(getClass().getSimpleName(), str);
        int length = version2.versionParts.length + i;
        Object[] objArr = new Object[length];
        for (int i2 = 0; i2 < length; i2++) {
            Object[] objArr2 = version2.versionParts;
            if (i2 < objArr2.length) {
                objArr[i2] = objArr2[i2];
            } else {
                objArr[i2] = Long.valueOf(0);
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return new Version(objArr);
    }

    public boolean equals(Object obj) {
        String str = "equals";
        Logger.enter(getClass().getSimpleName(), str);
        if (obj == null || !(obj instanceof Version)) {
            Logger.exit(getClass().getSimpleName(), str);
            return super.equals(obj);
        }
        Logger.exit(getClass().getSimpleName(), str);
        return ((Version) obj).version.equals(this.version);
    }

    public int hashCode() {
        String str = "hashCode";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.version.hashCode();
    }

    public String toString() {
        String str = "toString";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.version;
    }

    public Object getPart(int i) {
        String str = "getPart";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.versionParts[i];
    }

    public Version getPrefix(int i) {
        String str = "getPrefix";
        Logger.enter(getClass().getSimpleName(), str);
        if (i > this.versionParts.length) {
            Logger.exit(getClass().getSimpleName(), str);
            return this;
        }
        String str2 = "";
        for (int i2 = 0; i2 < i; i2++) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(this.versionParts[i2]);
            str2 = sb.toString();
            if (i2 < i - 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(".");
                str2 = sb2.toString();
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return new Version(str2);
    }

    public boolean equals3Digits(Version version2) {
        String str = "equals3Digits";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return equalsNDigits(version2, 3);
    }

    public boolean equals4Digits(Version version2) {
        String str = "equals4Digits";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return equalsNDigits(version2, 4);
    }

    private boolean equalsNDigits(Version version2, int i) {
        String str = "equalsNDigits";
        Logger.enter(getClass().getSimpleName(), str);
        if (version2 == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return false;
        } else if (compareTo(version2) == 0) {
            Logger.exit(getClass().getSimpleName(), str);
            return true;
        } else {
            Logger.exit(getClass().getSimpleName(), str);
            return version2.getPrefix(i).equals(getPrefix(i));
        }
    }

    public boolean isGreaterOrEqual(Version version2) {
        String str = "isGreaterOrEqual";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return compareTo(version2) >= 0;
    }
}
