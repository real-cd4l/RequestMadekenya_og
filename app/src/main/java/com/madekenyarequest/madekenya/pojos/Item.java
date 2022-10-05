package com.madekenyarequest.madekenya.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    String jina;
    int idadi;
    String unapoenda;
    boolean unarisiti;

    public Item(String jina, int idadi, String unapoenda, boolean unarisiti) {
        this.jina = jina;
        this.idadi = idadi;
        this.unapoenda = unapoenda;
        this.unarisiti = unarisiti;
    }

    public Item() {
    }

    protected Item(Parcel in) {
        jina = in.readString();
        idadi = in.readInt();
        unapoenda = in.readString();
        unarisiti = in.readByte() != 0;
    }

    public String getJina() {
        return jina;
    }

    public void setJina(String jina) {
        this.jina = jina;
    }

    public int getIdadi() {
        return idadi;
    }

    public void setIdadi(int idadi) {
        this.idadi = idadi;
    }

    public String getUnapoenda() {
        return unapoenda;
    }

    public void setUnapoenda(String unapoenda) {
        this.unapoenda = unapoenda;
    }

    public boolean isUnarisiti() {
        return unarisiti;
    }

    public void setUnarisiti(boolean unarisiti) {
        this.unarisiti = unarisiti;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jina);
        dest.writeInt(idadi);
        dest.writeString(unapoenda);
        dest.writeByte((byte) (unarisiti ? 1 : 0));
    }
}
