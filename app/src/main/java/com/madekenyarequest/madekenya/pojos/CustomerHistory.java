package com.madekenyarequest.madekenya.pojos;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class CustomerHistory implements Parcelable, Serializable {
    public static final int STATUS_REGISTERED = 0;
    public static final int STATUS_RECORD_UPDATE = 1;
    public static final int STATUS_FROM_STORE_TO_CAR = 2;
    public static final int STATUS_FROM_CAR_TO_CAR = 3;
    public static final int STATUS_FROM_CAR_TO_STORE = 4;
    public static final int STATUS_FROM_CAR_TO_DELIVERY = 5;
    public static final int STATUS_CARGO_PRICE_CHANGED = 6;
    public static final int STATUS_CARGO_PRICE_LABELED = 7;
    public static final int STATUS_CARGO_PRICE_REMOVED = 8;
    public static final Creator<CustomerHistory> CREATOR = new Creator<CustomerHistory>() {
        @Override
        public CustomerHistory createFromParcel(android.os.Parcel in) {
            return new CustomerHistory(in);
        }

        @Override
        public CustomerHistory[] newArray(int size) {
            return new CustomerHistory[size];
        }
    };
    public Cargo previousRecord = null;
    public Cargo updatedRecord = null;
    public String employeeName = "";
    public int status = 1;
    public String details = "";
    private ParcelMovement parcelMovement = null;
    private int date = 0;
    private int month = 0;
    private int year = 0;
    private int number = 0;
    private int hours = 0;
    private int minutes = 0;

    public CustomerHistory() {

    }

    public CustomerHistory(int status, ParcelMovement parcelMovement, String employeeName, String details) {
        this.employeeName = employeeName;
        this.status = status;
        this.parcelMovement = parcelMovement;
        this.details = details;


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.hours = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.date = calendar.get(Calendar.DATE);
        this.month = (calendar.get(Calendar.MONTH) + 1);
        this.year = calendar.get(Calendar.YEAR);
    }


    public CustomerHistory(Cargo previousRecord, Cargo updatedRecord, String employeeName, int status, String details) {
        this.previousRecord = previousRecord;
        this.updatedRecord = updatedRecord;
        this.employeeName = employeeName;
        this.status = status;
        this.details = details;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.hours = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.date = calendar.get(Calendar.DATE);
        this.month = (calendar.get(Calendar.MONTH) + 1);
        this.year = calendar.get(Calendar.YEAR);
    }

    protected CustomerHistory(android.os.Parcel in) {
        previousRecord = in.readParcelable(Cargo.class.getClassLoader());
        updatedRecord = in.readParcelable(Cargo.class.getClassLoader());
        employeeName = in.readString();
        status = in.readInt();
        details = in.readString();
        parcelMovement = in.readParcelable(ParcelMovement.class.getClassLoader());
        date = in.readInt();
        month = in.readInt();
        year = in.readInt();
        number = in.readInt();
        hours = in.readInt();
        minutes = in.readInt();
    }

    public CustomerHistory(Cargo previousRecord, Cargo updatedRecord, String employeeName, String companyDbLink, int status, String locationDetails) {
        this.previousRecord = previousRecord;
        this.updatedRecord = updatedRecord;
        this.employeeName = employeeName;
//    this.off = companyDbLink;
        this.status = status;
        this.details = locationDetails;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.hours = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.date = calendar.get(Calendar.DATE);
        this.month = (calendar.get(Calendar.MONTH) + 1);
        this.year = calendar.get(Calendar.YEAR);
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(previousRecord, flags);
        dest.writeParcelable(updatedRecord, flags);
        dest.writeString(employeeName);
        dest.writeInt(status);
        dest.writeString(details);
        dest.writeParcelable(parcelMovement, flags);
        dest.writeInt(date);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeInt(number);
        dest.writeInt(hours);
        dest.writeInt(minutes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Cargo getPreviousRecord() {
        return previousRecord;
    }

    public void setPreviousRecord(Cargo previousRecord) {
        this.previousRecord = previousRecord;
    }

    public Cargo getUpdatedRecord() {
        return updatedRecord;
    }

    public void setUpdatedRecord(Cargo updatedRecord) {
        this.updatedRecord = updatedRecord;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ParcelMovement getParcelMovement() {
        return parcelMovement;
    }

    public void setParcelMovement(ParcelMovement parcelMovement) {
        this.parcelMovement = parcelMovement;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
