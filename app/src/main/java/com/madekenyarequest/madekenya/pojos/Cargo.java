package com.madekenyarequest.madekenya.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Cargo implements Parcelable, Serializable {
    public static final Creator<Cargo> CREATOR = new Creator<Cargo>() {
        @Override
        public Cargo createFromParcel(Parcel in) {
            return new Cargo(in);
        }

        @Override
        public Cargo[] newArray(int size) {
            return new Cargo[size];
        }
    };
    public int idadi;
    public ArrayList<ParcelLocationHistory> locationHistories;
    public boolean isPrintedReceipt = false;
    public long unitPrice = 0;
    public long totalPrice = 0;
    public String mzigoUnaRisiti = "false";
    public String unapoenda = "";
    public String umetoka_kwa = "";
    public String jina;
    public String label;
    public int invoiceIndex = -1;
    public Mpokeaji mpokeaji;
    private long updatedTime;
    private boolean chargedAsGroup;
    private long cashReceived;
    private String lastEventTime;
    private String lastEventUserName;
    private String lastEventDetails;
    private int countShusha = 0;
    private ArrayList<PaymentRecord> payments;
    private String recordRef;

    protected Cargo(Parcel in) {
        idadi = in.readInt();
        locationHistories = in.createTypedArrayList(ParcelLocationHistory.CREATOR);
        isPrintedReceipt = in.readByte() != 0;
        unitPrice = in.readLong();
        totalPrice = in.readLong();
        mzigoUnaRisiti = in.readString();
        unapoenda = in.readString();
        umetoka_kwa = in.readString();
        jina = in.readString();
        label = in.readString();
        invoiceIndex = in.readInt();
        mpokeaji = in.readParcelable(Mpokeaji.class.getClassLoader());
        updatedTime = in.readLong();
        chargedAsGroup = in.readByte() != 0;
        cashReceived = in.readLong();
        lastEventTime = in.readString();
        lastEventUserName = in.readString();
        lastEventDetails = in.readString();
        countShusha = in.readInt();
        payments = in.createTypedArrayList(PaymentRecord.CREATOR);
        recordRef = in.readString();
    }

    public Cargo() {
    }

    public Cargo(int idadi, ArrayList<ParcelLocationHistory> locationHistories, long unitPrice, long totalPrice, String mzigoUnaRisiti, String unapoenda, String umetoka_kwa, String jina, boolean chargedAsGroup, long cashReceived) {
        this.idadi = idadi;
        this.locationHistories = locationHistories;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.mzigoUnaRisiti = mzigoUnaRisiti;
        this.unapoenda = unapoenda;
        this.umetoka_kwa = umetoka_kwa;
        this.jina = jina;
        this.chargedAsGroup = chargedAsGroup;
        this.cashReceived = cashReceived;
        this.recordRef = String.valueOf(new Date().getTime());
    }

    public String getRecordRef() {
        return recordRef;
    }

    public void setRecordRef(String recordRef) {
        this.recordRef = recordRef;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idadi);
        dest.writeTypedList(locationHistories);
        dest.writeByte((byte) (isPrintedReceipt ? 1 : 0));
        dest.writeLong(unitPrice);
        dest.writeLong(totalPrice);
        dest.writeString(mzigoUnaRisiti);
        dest.writeString(unapoenda);
        dest.writeString(umetoka_kwa);
        dest.writeString(jina);
        dest.writeString(label);
        dest.writeInt(invoiceIndex);
        dest.writeParcelable(mpokeaji, flags);
        dest.writeLong(updatedTime);
        dest.writeByte((byte) (chargedAsGroup ? 1 : 0));
        dest.writeLong(cashReceived);
        dest.writeString(lastEventTime);
        dest.writeString(lastEventUserName);
        dest.writeString(lastEventDetails);
        dest.writeInt(countShusha);
        dest.writeTypedList(payments);
        dest.writeString(recordRef);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<PaymentRecord> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PaymentRecord> payments) {
        this.payments = payments;
    }

    public String getLastEventUserName() {
        return lastEventUserName;
    }

    public void setLastEventUserName(String lastEventUserName) {
        this.lastEventUserName = lastEventUserName;
    }

    public boolean isChargedAsGroup() {
        return chargedAsGroup;
    }

    public void setChargedAsGroup(boolean chargedAsGroup) {
        this.chargedAsGroup = chargedAsGroup;
    }

    public long getCashReceived() {
        return cashReceived;
    }

    public void setCashReceived(long cashReceived) {
        this.cashReceived = cashReceived;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Mpokeaji getMpokeaji() {
        return mpokeaji;
    }

    public void setMpokeaji(Mpokeaji mpokeaji) {
        this.mpokeaji = mpokeaji;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getInvoiceIndex() {
        return invoiceIndex;
    }

    public void setInvoiceIndex(int invoiceIndex) {
        this.invoiceIndex = invoiceIndex;
    }

    public Cargo deepCopy() throws Exception {
        //Serialization of object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);

        //De-serialization of object
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (Cargo) in.readObject();
    }

    public ParcelLocationHistory getCurrentLocation() {
        if (locationHistories.size() > 1) {
            int pointer = locationHistories.size() - 1;
            return locationHistories.get(pointer);
        } else if (locationHistories.size() == 1) {
            return locationHistories.get(0);
        }
        return null;
    }

    public ParcelLocationHistory getOnCarLocationDetails() {
        if (locationHistories.size() > 2) {
            int pointer = locationHistories.size() - 2;
            return locationHistories.get(pointer);
        }
        return null;
    }

    public ArrayList<ParcelLocationHistory> getParcelCurrentLocationStatus() {
        return locationHistories;
    }

    public void setParcelCurrentLocationStatus(ArrayList<ParcelLocationHistory> parcelLocationHistories) {
        this.locationHistories = parcelLocationHistories;
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

    public boolean isPrintedReceipt() {
        return isPrintedReceipt;
    }

    public void setPrintedReceipt(boolean printedReceipt) {
        isPrintedReceipt = printedReceipt;
    }

    public String getMzigoUnaRisiti() {
        return mzigoUnaRisiti;
    }

    public void setMzigoUnaRisiti(String mzigoUnaRisiti) {
        this.mzigoUnaRisiti = mzigoUnaRisiti;
    }

    public String getUnapoenda() {
        return unapoenda;
    }

    public void setUnapoenda(String unapoenda) {
        this.unapoenda = unapoenda;
    }

    public String getUmetoka_kwa() {
        return umetoka_kwa;
    }

    public void setUmetoka_kwa(String umetoka_kwa) {
        this.umetoka_kwa = umetoka_kwa;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<ParcelLocationHistory> getLocationHistories() {
        return locationHistories;
    }

    public void setLocationHistories(ArrayList<ParcelLocationHistory> locationHistories) {
        this.locationHistories = locationHistories;
    }

    public String getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(String lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

    public String getLastEventDetails() {
        return lastEventDetails;
    }

    public void setLastEventDetails(String lastEventDetails) {
        this.lastEventDetails = lastEventDetails;
    }

    public int getCountShusha() {
        return countShusha;
    }


    //jina: string,
    // idadi: number,
    // currentLocationStatus: ParcelLocationHistory,
    // unitPrice: number,
    // mzigoUnaRisiti: boolean,
    // unapoenda: string,
    // umetoka_kwa: string,
    // _receivedCash: number,
    // _totalPrice: number,
    // _isChargedAsGroup: boolean

    public void setCountShusha(int countShusha) {
        this.countShusha = countShusha;
    }
}
