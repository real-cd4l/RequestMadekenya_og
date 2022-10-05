package com.madekenyarequest.madekenya.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ParcelMovement implements Parcelable, Serializable {

    public static final int STATUS_FROM_STORE_TO_CAR = 2;
    public static final int STATUS_FROM_CAR_TO_CAR = 3;
    public static final int STATUS_FROM_CAR_TO_STORE = 4;
    public static final int STATUS_FROM_CAR_TO_DELIVERY = 5;
    public static final Creator<ParcelMovement> CREATOR = new Creator<ParcelMovement>() {
        @Override
        public ParcelMovement createFromParcel(Parcel in) {
            return new ParcelMovement(in);
        }

        @Override
        public ParcelMovement[] newArray(int size) {
            return new ParcelMovement[size];
        }
    };
    private int movementID = -1;
    private String fromLocation;
    private String toLocation;
    private String parcelLabel;
    private int parcelCount;
    private int remainCount;

    public ParcelMovement(int movementID, String fromLocation, String toLocation, String parcelLabel, int parcelCount, int remainCount) {
        this.movementID = movementID;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.parcelLabel = parcelLabel;
        this.parcelCount = parcelCount;
        this.remainCount = remainCount;
    }

    protected ParcelMovement(Parcel in) {
        movementID = in.readInt();
        fromLocation = in.readString();
        toLocation = in.readString();
        parcelLabel = in.readString();
        parcelCount = in.readInt();
        remainCount = in.readInt();
    }

    public ParcelMovement() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movementID);
        dest.writeString(fromLocation);
        dest.writeString(toLocation);
        dest.writeString(parcelLabel);
        dest.writeInt(parcelCount);
        dest.writeInt(remainCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getMovementID() {
        return movementID;
    }

    public void setMovementID(int movementID) {
        this.movementID = movementID;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getParcelLabel() {
        return parcelLabel;
    }

    public void setParcelLabel(String parcelLabel) {
        this.parcelLabel = parcelLabel;
    }

    public int getParcelCount() {
        return parcelCount;
    }

    public void setParcelCount(int parcelCount) {
        this.parcelCount = parcelCount;
    }

    public int getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(int remainCount) {
        this.remainCount = remainCount;
    }
}
