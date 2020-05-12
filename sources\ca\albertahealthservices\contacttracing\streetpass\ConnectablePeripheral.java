package ca.albertahealthservices.contacttracing.streetpass;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0002\u0010\u0007J\t\u0010\u0015\u001a\u00020\u0005HÖ\u0001J\u0019\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0005HÖ\u0001R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0006\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0010\n\u0002\u0010\u0014\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013¨\u0006\u001b"}, d2 = {"Lca/albertahealthservices/contacttracing/streetpass/ConnectablePeripheral;", "Landroid/os/Parcelable;", "manuData", "", "transmissionPower", "", "rssi", "(Ljava/lang/String;Ljava/lang/Integer;I)V", "getManuData", "()Ljava/lang/String;", "setManuData", "(Ljava/lang/String;)V", "getRssi", "()I", "setRssi", "(I)V", "getTransmissionPower", "()Ljava/lang/Integer;", "setTransmissionPower", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "describeContents", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: ConnectablePeripheral.kt */
public final class ConnectablePeripheral implements Parcelable {
    public static final android.os.Parcelable.Creator CREATOR = new Creator();
    private String manuData;
    private int rssi;
    private Integer transmissionPower;

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 16})
    public static class Creator implements android.os.Parcelable.Creator {
        public final Object createFromParcel(Parcel parcel) {
            Intrinsics.checkParameterIsNotNull(parcel, "in");
            return new ConnectablePeripheral(parcel.readString(), parcel.readInt() != 0 ? Integer.valueOf(parcel.readInt()) : null, parcel.readInt());
        }

        public final Object[] newArray(int i) {
            return new ConnectablePeripheral[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2;
        Intrinsics.checkParameterIsNotNull(parcel, "parcel");
        parcel.writeString(this.manuData);
        Integer num = this.transmissionPower;
        if (num != null) {
            parcel.writeInt(1);
            i2 = num.intValue();
        } else {
            i2 = 0;
        }
        parcel.writeInt(i2);
        parcel.writeInt(this.rssi);
    }

    public ConnectablePeripheral(String str, Integer num, int i) {
        Intrinsics.checkParameterIsNotNull(str, "manuData");
        this.manuData = str;
        this.transmissionPower = num;
        this.rssi = i;
    }

    public final String getManuData() {
        return this.manuData;
    }

    public final void setManuData(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.manuData = str;
    }

    public final Integer getTransmissionPower() {
        return this.transmissionPower;
    }

    public final void setTransmissionPower(Integer num) {
        this.transmissionPower = num;
    }

    public final int getRssi() {
        return this.rssi;
    }

    public final void setRssi(int i) {
        this.rssi = i;
    }
}
