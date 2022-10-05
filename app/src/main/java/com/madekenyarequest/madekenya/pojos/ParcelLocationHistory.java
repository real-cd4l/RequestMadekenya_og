package com.madekenyarequest.madekenya.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ParcelLocationHistory implements Parcelable, Serializable {
    public static final int PARCEL_ON_STORE = 1;
    public static final int PARCEL_ON_CAR = 2;
    public static final int PARCEL_DELIVERED = 3;
    public static final int INVOICE_RECORD = 4;
    public static final Creator<ParcelLocationHistory> CREATOR = new Creator<ParcelLocationHistory>() {
        @Override
        public ParcelLocationHistory createFromParcel(Parcel in) {
            return new ParcelLocationHistory(in);
        }

        @Override
        public ParcelLocationHistory[] newArray(int size) {
            return new ParcelLocationHistory[size];
        }
    };
    private int date = 0;
    private int month = 0;
    private int year = 0;
    private int hours = 0;
    private int minutes = 0;
    private String employeeName = "";
    private int statusCode;
    private String locationDetails = "";
    private String selectedDate = null;


    public ParcelLocationHistory(int statusCode, String locationDetails, String employeeName) {
        this.employeeName = employeeName;
        this.statusCode = statusCode;
        this.locationDetails = locationDetails;
        this.selectedDate = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.hours = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.date = calendar.get(Calendar.DATE);
        this.month = (calendar.get(Calendar.MONTH) + 1);
        this.year = calendar.get(Calendar.YEAR);
    }

    public ParcelLocationHistory(int statusCode, String locationDetails, String employeeName, String selectedDate) {
        this.employeeName = employeeName;
        this.statusCode = statusCode;
        this.locationDetails = locationDetails;
        this.selectedDate = selectedDate;
    }

    public ParcelLocationHistory() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.hours = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.date = calendar.get(Calendar.DATE);
        this.month = (calendar.get(Calendar.MONTH) + 1);
        this.year = calendar.get(Calendar.YEAR);
    }

    protected ParcelLocationHistory(Parcel in) {
        date = in.readInt();
        month = in.readInt();
        year = in.readInt();
        hours = in.readInt();
        minutes = in.readInt();
        employeeName = in.readString();
        statusCode = in.readInt();
        locationDetails = in.readString();
        selectedDate = in.readString();
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public ParcelLocationHistory deepCopy() throws Exception {
        //Serialization of object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);

        //De-serialization of object
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (ParcelLocationHistory) in.readObject();
    }

    public String getEventDate() {
        return this.date + "-" + this.month + "-" + this.year;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(date);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeInt(hours);
        dest.writeInt(minutes);
        dest.writeString(employeeName);
        dest.writeInt(statusCode);
        dest.writeString(locationDetails);
        dest.writeString(selectedDate);
    }
}
