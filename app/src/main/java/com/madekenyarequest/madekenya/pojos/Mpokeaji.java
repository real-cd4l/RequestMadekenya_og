package com.madekenyarequest.madekenya.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Mpokeaji implements Parcelable, Serializable {
    public String jina;
    public String phone;

    public Mpokeaji(String jina, String phone) {
        this.jina = jina;
        this.phone = phone;
    }

    protected Mpokeaji(Parcel in) {
        jina = in.readString();
        phone = in.readString();
    }

    public Mpokeaji() {
    }

    public static final Creator<Mpokeaji> CREATOR = new Creator<Mpokeaji>() {
        @Override
        public Mpokeaji createFromParcel(Parcel in) {
            return new Mpokeaji(in);
        }

        @Override
        public Mpokeaji[] newArray(int size) {
            return new Mpokeaji[size];
        }
    };

    public String getJina() {
        return jina;
    }

    public void setJina(String jina) {
        this.jina = jina;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jina);
        dest.writeString(phone);
    }
}
