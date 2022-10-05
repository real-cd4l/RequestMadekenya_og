package com.madekenyarequest.madekenya.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Subscriber implements Parcelable {
    public static final Creator<Subscriber> CREATOR = new Creator<Subscriber>() {
        @Override
        public Subscriber createFromParcel(Parcel in) {
            return new Subscriber(in);
        }

        @Override
        public Subscriber[] newArray(int size) {
            return new Subscriber[size];
        }
    };
    private String id;
    private String username;
    private String location;
    private String password;
    private String username_password;
    private String companyID;
    private long totalPendingRequestCargo;
    private boolean verified  = false;

    public Subscriber(String id, String username, String location, String password, String companyID) {
        this.id = id;
        this.username = username;
        this.location = location;
        this.password = password;
        this.companyID = companyID.trim();
        this.username_password = username.trim().toLowerCase() + "-" + password.trim();
    }


    public Subscriber() {
    }

    protected Subscriber(Parcel in) {
        id = in.readString();
        username = in.readString();
        location = in.readString();
        password = in.readString();
        username_password = in.readString();
        companyID = in.readString();
        totalPendingRequestCargo = in.readLong();
        verified = in.readByte() != 0;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getUsername_password() {
        return username_password;
    }

    public void setUsername_password(String username_password) {
        this.username_password = username_password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTotalPendingRequestCargo() {
        return totalPendingRequestCargo;
    }

    public void setTotalPendingRequestCargo(long totalPendingRequestCargo) {
        this.totalPendingRequestCargo = totalPendingRequestCargo;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(location);
        dest.writeString(password);
        dest.writeString(username_password);
        dest.writeString(companyID);
        dest.writeLong(totalPendingRequestCargo);
        dest.writeByte((byte) (verified ? 1 : 0));
    }
}
