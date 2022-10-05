package com.madekenyarequest.madekenya.pojos;

import android.os.Parcelable;

import java.io.Serializable;

public class PaymentRecord implements Parcelable, Serializable {

    public static final Creator<PaymentRecord> CREATOR = new Creator<PaymentRecord>() {
        @Override
        public PaymentRecord createFromParcel(android.os.Parcel in) {
            return new PaymentRecord(in);
        }

        @Override
        public PaymentRecord[] newArray(int size) {
            return new PaymentRecord[size];
        }
    };
    private String office;
    private String employee;
    private String dateTime;
    private double amount;
    private boolean paidAsAGroup;


    public PaymentRecord(String office, String employee, String dateTime, double amount, boolean paidAsAGroup) {
        this.office = office;
        this.employee = employee;
        this.dateTime = dateTime;
        this.amount = amount;
        this.paidAsAGroup = paidAsAGroup;
    }

    public PaymentRecord() {
    }

    protected PaymentRecord(android.os.Parcel in) {
        office = in.readString();
        employee = in.readString();
        dateTime = in.readString();
        amount = in.readFloat();
        paidAsAGroup = in.readByte() != 0;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaidAsAGroup() {
        return paidAsAGroup;
    }

    public void setPaidAsAGroup(boolean paidAsAGroup) {
        this.paidAsAGroup = paidAsAGroup;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(office);
        dest.writeString(employee);
        dest.writeString(dateTime);
        dest.writeDouble(amount);
        dest.writeByte((byte) (paidAsAGroup ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
